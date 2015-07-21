package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.common.AppContext;

import android.R.bool;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

public class ActivityHealthData extends FragmentActivity {

	private RadioButton radiobtn_archives;
	private RadioButton radiobtn_rate;
	private ImageView iv_archives;
	private ImageView iv_rate;
	private ImageView healthdata_head_back;
	private ImageView healthdata_head_menu;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private ArchivesBean archivesBean;
	private ArchivesBean archives;
	private boolean isBack = false;
	FragmentHealthData firstFragment = new FragmentHealthData();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.healthdata);
		Bundle bundle = this.getIntent().getExtras();
		// 健康数据传个boolean标识, 辨别是健康档案还是健康数据
		// 单纯是用来设置左上角的返回和进入个人信息界面隐藏或显示的标识.
		isBack = getIntent().getBooleanExtra("isBack", false);
		initView();
		if (bundle == null) {
			new getInfo().execute("");
		} else {
			// 拿到从Buddy传过来的健康数据
			archivesBean = (ArchivesBean) bundle.getSerializable("patient");
			fragmentManager = getSupportFragmentManager();
			transaction = fragmentManager.beginTransaction();
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

			FragmentHealthData firstFragment = new FragmentHealthData();

			// 将数据传到Fragment中
			bundle.putSerializable("info", archivesBean);

			firstFragment.setArguments(bundle);

			transaction.replace(R.id.health_fragment, firstFragment);

			transaction.commit();
		}

	}

	private void initView() {
		radiobtn_archives = (RadioButton) findViewById(R.id.radiobtn_health_archives);
		radiobtn_rate = (RadioButton) findViewById(R.id.radiobtn_health_rate);
		iv_archives = (ImageView) findViewById(R.id.tobottom_blue);
		iv_rate = (ImageView) findViewById(R.id.tobottom_green);
		// 设置监听
		radiobtn_archives.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (radiobtn_archives.isChecked()) {
					// 设置健康档案三角显示
					iv_archives.setVisibility(View.VISIBLE);
					// 设置心率信息三角隐藏
					iv_rate.setVisibility(View.GONE);
					fragmentManager = getSupportFragmentManager();
					transaction = fragmentManager.beginTransaction();
					transaction
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

					// 将数据传到Fragment中
					Bundle bundle = new Bundle();
					if (archivesBean == null) {
						bundle.putSerializable("info", archives);
						firstFragment.setArguments(bundle);
					} else {
						bundle.putSerializable("info", archivesBean);
						firstFragment.setArguments(bundle);
					}

					transaction.replace(R.id.health_fragment, firstFragment);

					transaction.commit();
				} else {
					// 设置健康档案三角隐藏
					iv_archives.setVisibility(View.GONE);
					// 设置心率信息三角显示
					iv_rate.setVisibility(View.VISIBLE);
				}

			}
		});
		radiobtn_rate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (radiobtn_rate.isChecked()) {
					// 设置心率信息三角显示
					iv_rate.setVisibility(View.VISIBLE);
					// 设置健康档案三角隐藏
					iv_archives.setVisibility(View.GONE);
					fragmentManager = getSupportFragmentManager();
					transaction = fragmentManager.beginTransaction();
					transaction
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

					FragmentHearthRateInfo hearthRateInfo = new FragmentHearthRateInfo();

					transaction.replace(R.id.health_fragment, hearthRateInfo);

					transaction.commit();
				} else {
					// 设置健康档案三角显示
					iv_rate.setVisibility(View.GONE);
					// 设置心率信息三角隐藏
					iv_archives.setVisibility(View.VISIBLE);

				}

			}
		});
		healthdata_head_menu = (ImageView) findViewById(R.id.healthdata_head_menu);
		healthdata_head_back = (ImageView) findViewById(R.id.healthdata_head_back);
		if (isBack) {
			healthdata_head_menu.setVisibility(View.GONE);
			healthdata_head_back.setVisibility(View.VISIBLE);
		} else {
			healthdata_head_back.setVisibility(View.GONE);
			healthdata_head_menu.setVisibility(View.VISIBLE);
		}
		healthdata_head_menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActivityHealthData.this,
						Myself.class);
				startActivity(intent);
			}
		});

		healthdata_head_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ActivityHealthData.this.finish();
			}
		});
		// new Async().execute("");
	}

	class getInfo extends AsyncTask {
		ProgressDialog getHealthDataDialog = new ProgressDialog(
				ActivityHealthData.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getHealthDataDialog.setMessage("请稍后,正在获取档案信息.");
			getHealthDataDialog.setCancelable(false);
			getHealthDataDialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			archives = LoginActivity.new_http_client.getArchives(Long
					.parseLong(LoginActivity.mySharedPreferences.getString(
							"current_login_tel", "")));
			AppContext.getInstance().setBean(archives);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			getHealthDataDialog.dismiss();
			fragmentManager = getSupportFragmentManager();
			transaction = fragmentManager.beginTransaction();
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

			// 将数据传到Fragment中
			Bundle bundle = new Bundle();
			if (archivesBean == null) {
				bundle.putSerializable("info", archives);
				firstFragment.setArguments(bundle);
			} else {
				bundle.putSerializable("info", archivesBean);
				firstFragment.setArguments(bundle);
			}

			transaction.replace(R.id.health_fragment, firstFragment);

			transaction.commit();
		}

	}
}
