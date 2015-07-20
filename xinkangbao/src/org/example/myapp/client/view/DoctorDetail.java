package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorDetail extends FragmentActivity {
	public FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private long doctor_tel;
	private String doctor_name;
	TextView tv_name;
	TextView tv_position;
	TextView tv_hosptial;
	ImageView doctor_back;
	ImageView doctor_ask;
	ImageView doctor_head_iv;
	ImageView doctor_commend;
	ImageView doctor_add;
	String Error_msg;
	String Succeed_msg;
	private String thumb_src;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dotorinfomation);

		doctor_tel = getIntent().getLongExtra("tel", 0);
		doctor_name = getIntent().getStringExtra("name");
		thumb_src = getIntent().getStringExtra("thumb_src");
		
		tv_name = (TextView) findViewById(R.id.tv_detail_name);
		tv_position = (TextView) findViewById(R.id.tv_detail_position);
		tv_hosptial = (TextView) findViewById(R.id.tv_detail_local);
		doctor_back = (ImageView) findViewById(R.id.doctor_detail_back);
		doctor_ask = (ImageView) findViewById(R.id.detail_iv_ask);
		doctor_head_iv = (ImageView) findViewById(R.id.detail_iv);
		doctor_commend = (ImageView) findViewById(R.id.detail_iv_commend);
		doctor_add = (ImageView) findViewById(R.id.detail_iv_add);

		String[] position = getIntent().getStringExtra("job").split("，");
		if (!StringUtils.isEmpty(thumb_src)) 
		{
			imageLoader.init(ImageLoaderConfiguration.createDefault(DoctorDetail.this));
			ImageLoader.getInstance().displayImage(
					"http://" + thumb_src, doctor_head_iv);
		}
		
		tv_name.setText(getIntent().getStringExtra("name"));// 姓名
		if (position != null && position.length > 0) 
		{
			tv_position.setText(position[0]);// 职位
		} else
		{
			tv_position.setText(getIntent().getStringExtra("job"));// 职位
		}
		tv_hosptial.setText(getIntent().getStringExtra("hospital"));// 医院

		doctor_back.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		doctor_ask.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(DoctorDetail.this,
						ChatActivity.class);
				intent.putExtra("tel", getIntent().getLongExtra("tel", 0));
				intent.putExtra("name", getIntent().getStringExtra("name"));
				intent.putExtra("isinline",
						getIntent().getIntExtra("isonline", 0));
				intent.putExtra("major", getIntent().getStringExtra("major"));
				startActivity(intent);
			}
		});

		doctor_commend.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) {
				Intent intent_yuyue = new Intent(DoctorDetail.this,
						DoctorOrderActivity.class);
				intent_yuyue.putExtra("tel", doctor_tel);
				intent_yuyue.putExtra("name", doctor_name);
				startActivity(intent_yuyue);
			}
		});
		doctor_add.setOnClickListener(new OnClickListener()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) 
			{
				AsyncAddDoc addDoc = new AsyncAddDoc();
				addDoc.execute("");
			}
		});
		fragmentManager = getSupportFragmentManager();

		transaction = fragmentManager.beginTransaction();

		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		Fragment_left firstFragment = new Fragment_left();

		// 将数据传到Fragment中
		Bundle bundle = new Bundle();
		bundle.putString("person", getIntent().getStringExtra("person"));
		bundle.putString("major", getIntent().getStringExtra("major"));
		bundle.putString("job", getIntent().getStringExtra("job"));
		bundle.putString("department", getIntent().getStringExtra("department"));

		firstFragment.setArguments(bundle);

		transaction.replace(R.id.fragment_detail, firstFragment);

		transaction.commit();
	}

	void addMyDoc() {
		
		
	}
	@SuppressWarnings("rawtypes")
	class AsyncAddDoc extends AsyncTask
	{
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog = new ProgressDialog(DoctorDetail.this);
			dialog.setMessage("正在添加医生,请稍后.");
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Object doInBackground(Object... params) 
		{
			try {
				Long doc_id = getIntent().getLongExtra("tel", 0);
				Long user_id = Long.valueOf(LoginActivity.mySharedPreferences
						.getString("current_login_tel", ""));

				ReturnObj obj = MainActivity.client_in_strict_mode.add_user_doc(
						user_id, doc_id);
				if (obj.getRet_code() == 0) {
					Succeed_msg = "添加医生成功 ";
					dialog.dismiss();

					Doctor doc_tmp = Doctor
							.paser_str_to_obj((MainActivity.client_in_strict_mode
									.get_doc_info(doc_id)));
					BuddyActivity.buddyEntityList.add(doc_tmp);
				} else {
					Error_msg = "添加医生失败：  " + obj.getMsg();
					dialog.dismiss();
				}
			} catch (Exception e) {
				Error_msg = "添加医生失败：  " + e.getMessage();
				dialog.dismiss();
			}
			return null;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result)
		{
			super.onPostExecute(result);
			if (Error_msg!=null) {
				Toast.makeText(DoctorDetail.this, Error_msg, Toast.LENGTH_SHORT)
				.show();
			}else if (Succeed_msg != null) {
				Toast.makeText(DoctorDetail.this, Succeed_msg, Toast.LENGTH_SHORT)
				.show();
			}
			dialog.dismiss();
		}
	}
}
