package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePsw extends Activity {

	private EditText et_old;
	private EditText et_new_1;
	private EditText et_new_2;

	private Button btn_confirm;

	private ImageView iv_back;
	
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changepsw);
		initView();
		initOnClickEven();
	}

	private void initView() {
		et_old = (EditText) findViewById(R.id.et_ole_psw);
		et_new_1 = (EditText) findViewById(R.id.et_new_psw_1);
		et_new_2 = (EditText) findViewById(R.id.et_new_psw_2);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		iv_back = (ImageView)findViewById(R.id.changepsw_iv_back);
	}

	private void initOnClickEven() {
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChangePsw.this.finish();
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (StringUtils.isEmpty(et_old.getText().toString().trim())) {
					Toast.makeText(ChangePsw.this, "请输入原始密码!",
							Toast.LENGTH_LONG).show();
				} else if (StringUtils.isEmpty(et_new_1.getText().toString()
						.trim())) {
					Toast.makeText(ChangePsw.this, "请输入新密码!", Toast.LENGTH_LONG)
							.show();
				} else if (StringUtils.isEmpty(et_new_2.getText().toString()
						.trim())) {
					Toast.makeText(ChangePsw.this, "请输入确认密码!",
							Toast.LENGTH_LONG).show();
				} else {
					dialog = new ProgressDialog(ChangePsw.this);
					dialog.setMessage("正在处理,请稍后..");
					dialog.setCancelable(false);
					new AsyncChangePsw().execute("");
				}
			}
		});
	}

	class AsyncChangePsw extends AsyncTask {

		private ReturnObj returnResult;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {

			returnResult = LoginActivity.new_http_client.change_psw(
					LoginActivity.mySharedPreferences.getString(
							"current_login_tel", ""), Long.valueOf(et_old
							.getText().toString().trim()), Long
							.valueOf(et_new_2.getText().toString().trim()));
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (returnResult.getRet_code() == 0) {
				Toast.makeText(ChangePsw.this, "修改密码成功,请重新登录.",
						Toast.LENGTH_SHORT).show();
				ChangePsw.this.finish();
				//注销
				Intent logoutIntent = new Intent(ChangePsw.this,
						LoginActivity.class);
				logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				logoutIntent.putExtra("isReLoad", true);
				startActivity(logoutIntent);
			} else {
				Toast.makeText(ChangePsw.this, returnResult.getMsg(),
						Toast.LENGTH_SHORT).show();
				ChangePsw.this.finish();
			}
		}
	}
}
