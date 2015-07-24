package org.example.myapp.client.view;

import java.util.ArrayList;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.model.User;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.utils.GetBitmapFromInternet;

public class LoginActivity extends Activity {

	public static String userInfo;

	EditText accountEt, passwordEt;

	Dialog dialog;

	ReturnObj obj;

	public static SharedPreferences mySharedPreferences;

	private ReturnObj ret_obj;

	public static long USER_ID;

	public static List<Doctor> doc = new ArrayList<Doctor>();

	public static YQClient new_http_client = new YQClient(true);

	public static ReturnObj ret;

	public static ReturnObj user_ret;

	public static List<Doctor> buddyEntityList = new ArrayList<Doctor>();// 好友列表

	boolean isReLoad = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		accountEt = (EditText) findViewById(R.id.et_login_id);
		passwordEt = (EditText) findViewById(R.id.et_login_password);
		if (getIntent().getBooleanExtra("isReLoad", false)) {
			isReLoad = true;
		}
		/**
		 * 这边判断是不是修改密码后重新登录的页面
		 * */
		if (isReLoad) {
			String name = mySharedPreferences
					.getString("current_login_tel", "");
			if (StringUtils.isEmpty(accountEt.getText().toString()))
			{
				accountEt.setText(name);
			}
			passwordEt.setText("");
		} else {
			MainActivity.client_in_strict_mode = new YQClient();
			mySharedPreferences = getSharedPreferences(
					MyAppConfig.SHARE_PREFERENCE_FILE, Activity.MODE_PRIVATE);
			String name = mySharedPreferences
					.getString("current_login_tel", "");
			String passwd = mySharedPreferences.getString("current_login_pass",
					"");
			if (StringUtils.isEmpty(accountEt.getText().toString())) {
				accountEt.setText(name);
			}
			if (StringUtils.isEmpty(passwordEt.getText().toString())) {
				passwordEt.setText(passwd);
			}
		}

		Button btnLogin = (Button) findViewById(R.id.login_btn);

		btnLogin.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View arg0) {

				try {
					if (accountEt.getText().equals("")
							|| passwordEt.getText().equals("")) {
						Toast.makeText(LoginActivity.this, "账号或密码不能为空！",
								Toast.LENGTH_SHORT).show();
					} else if (Long.toString(
							Long.parseLong(accountEt.getText().toString()))
							.length() != MyAppConfig.MOBILE_LEN) {
						Toast.makeText(LoginActivity.this,
								"账户为手机号，" + MyAppConfig.MOBILE_LEN + "为数字！",
								Toast.LENGTH_SHORT).show();
					} else if (passwordEt.getText().toString().trim().length() < 4) {
						Toast.makeText(LoginActivity.this, "密码长度至少为4位数!",
								Toast.LENGTH_SHORT).show();
					} else {
						SharedPreferences mySharedPreferences = getSharedPreferences(
								MyAppConfig.SHARE_PREFERENCE_FILE,
								Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = mySharedPreferences
								.edit();
						editor.putString("current_login_ch", "1");
						editor.putString("current_login_tel", accountEt
								.getText().toString());
						editor.putString("current_login_pass", passwordEt
								.getText().toString());
						editor.commit();
						dialog = ProgressDialog.show(LoginActivity.this, null,
								"正在登录中 …", true, true);
						dialog.setCancelable(false);
						/**
						 * 异步:登录
						 * */
						@SuppressWarnings("rawtypes")
						AsyncTask async = new AsyncTask() {
							@Override
							protected Object doInBackground(Object... arg0) {
								obj = login(Long.parseLong(accountEt.getText()
										.toString()), passwordEt.getText()
										.toString());
								if (obj!=null && obj.getRet_code() == 0) 
								{
									/**
									 * 获取医生列表
									 * */
									ret_obj = new_http_client
											.get_all_doc_list(0);
									if (null != ret_obj
											&& ret_obj.getRet_code() == 0) {
										String buddyStr = ret_obj.getOrg_str();
										buddyEntityList = Doctor
												.paser_str_to_objlist(buddyStr);
									}

									if (MainActivity.myUser != null) 
									{
										/**
										 * 加载我的医生列表
										 * */
										ret_obj = new_http_client
												.get_user_doc_list(MainActivity.myUser
														.getId());
										if (ret_obj != null
												&& ret_obj.getRet_code() == 0) {
											String buddyStr = ret_obj
													.getOrg_str();
											doc = Doctor
													.paser_str_to_objlist(buddyStr);
										}
										/**
										 * 获取健康信息
										 * */
										ret = MainActivity.client_in_strict_mode
												.get_patient_health_stat(MainActivity.myUser
														.getId());

										/**
										 * 获取心跳信息
										 * 
										 * @param id
										 *            当前用户ID
										 * @return
										 */
										user_ret = MainActivity.client_in_strict_mode
												.get_patient_heartrate_list(MainActivity.myUser
														.getId());
									}
								}
								return null;
							}

							@Override
							protected void onPostExecute(Object result) {
								if (obj.getRet_code() == 0) {
									dialog.dismiss();
									ManageActivity.addActiviy("loginActivity",
											LoginActivity.this);
									// 转到主界面
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));
								} else {
									if (!obj.getMsg().isEmpty()) {
										Toast.makeText(LoginActivity.this,
												"登陆失败! " + obj.getMsg(),
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(LoginActivity.this,
												"登陆失败! " + user_ret.getMsg(),
												Toast.LENGTH_SHORT).show();
									}
									dialog.dismiss();
								}
							}
						};
						async.execute("");
					}
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, "登陆失败! " + e.toString(),
							Toast.LENGTH_SHORT).show();
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}
		});
		/**
		 * 注册
		 * */
		// findViewById(R.id.btn_register).setOnClickListener(
		// new OnClickListener() {
		// public void onClick(View arg0) {
		// startActivity(new Intent(LoginActivity.this,
		// RegisterActivity.class));
		// }
		// });

	}

	/**
	 * param:手机号码，密码。
	 * */
	ReturnObj login(Long a, String p) {
		User user = new User();
		user.setId(a);
		user.setPassword(p);
		user.setOperation("login");
		ReturnObj b = MainActivity.client_in_strict_mode.sendLoginInfo(user);
		return b;
	}
}
