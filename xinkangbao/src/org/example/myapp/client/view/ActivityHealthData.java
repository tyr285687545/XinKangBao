package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.common.AppContext;

import android.app.ProgressDialog;
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
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private ArchivesBean archivesBean;
	private ArchivesBean archives;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.healthdata);
		Bundle bundle = this.getIntent().getExtras();
		initView();
		if (bundle == null) 
		{
			new getInfo().execute("");
		}else 
		{
			// �õ���Buddy�������Ľ�������
			archivesBean = (ArchivesBean) bundle.getSerializable("patient");
			fragmentManager = getSupportFragmentManager();
			transaction = fragmentManager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			
			FragmentHealthData firstFragment = new FragmentHealthData();
			
			// �����ݴ���Fragment��
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
		// ���ü���
		radiobtn_archives.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				if (radiobtn_archives.isChecked()) {
					// ���ý�������������ʾ
					iv_archives.setVisibility(View.VISIBLE);
					// ����������Ϣ��������
					iv_rate.setVisibility(View.GONE);
				} else {
					// ���ý���������������
					iv_archives.setVisibility(View.GONE);
					// ����������Ϣ������ʾ
					iv_rate.setVisibility(View.VISIBLE);
				}

			}
		});
		radiobtn_rate.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) {
				if (radiobtn_rate.isChecked()) {
					// ����������Ϣ������ʾ
					iv_rate.setVisibility(View.VISIBLE);
					// ���ý���������������
					iv_archives.setVisibility(View.GONE);
				} else {
					// ���ý�������������ʾ
					iv_rate.setVisibility(View.GONE);
					// ����������Ϣ��������
					iv_archives.setVisibility(View.VISIBLE);
				}

			}
		});
		healthdata_head_back = (ImageView) findViewById(R.id.healthdata_head_back);
		healthdata_head_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityHealthData.this.finish();
			}
		});
		// new Async().execute("");
	}

	class getInfo extends AsyncTask
	{
		ProgressDialog getHealthDataDialog = new ProgressDialog(ActivityHealthData.this);
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			getHealthDataDialog.setMessage("���Ժ�,���ڻ�ȡ������Ϣ.");
			getHealthDataDialog.setCancelable(false);
			getHealthDataDialog.show();
		}
		
		@Override
		protected Object doInBackground(Object... params) 
		{
			archives = LoginActivity.new_http_client.getArchives(Long.parseLong(LoginActivity.mySharedPreferences.getString("current_login_tel", "")));
			AppContext.getInstance().setBean(archives);
			return null;
		}
		@Override
		protected void onPostExecute(Object result)
		{
			getHealthDataDialog.dismiss();
			fragmentManager = getSupportFragmentManager();
			transaction = fragmentManager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

			FragmentHealthData firstFragment = new FragmentHealthData();

			// �����ݴ���Fragment��
			Bundle bundle = new Bundle();
			if (archivesBean == null) {
				bundle.putSerializable("info", archives);
				firstFragment.setArguments(bundle);
			}else {
				bundle.putSerializable("info", archivesBean);
				firstFragment.setArguments(bundle);
			}
			
			transaction.replace(R.id.health_fragment, firstFragment);

			transaction.commit();
		}
	
	}
}