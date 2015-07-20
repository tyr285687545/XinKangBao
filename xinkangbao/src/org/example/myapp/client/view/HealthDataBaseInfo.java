package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthDataBaseInfo extends Activity
{
	private TextView tv_name;
	private TextView tv_age;
	private TextView tv_sex;
	private TextView tv_live;//独居
	private TextView tv_tel;
	private TextView tv_address;
	private TextView tv_guardian;//监护人
	private TextView guardian_tel;//监护人电话
	private TextView tv_customhospital;//就诊医院
	private TextView tv_personaldoctor;//就诊医生
	private TextView tv_department;//就诊科室
	private TextView tv_time;//就诊时间
	
	private ImageView iv_health_head;
	
	private ArchivesBean bean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baseinfo);
		Bundle bundle = getIntent().getExtras();
		bean = (ArchivesBean)bundle.get("info");
		Log.i("sky", "bean = null?  "+(bean == null));
		initView();
		initValue();
	}

	private void initView() 
	{
		iv_health_head = (ImageView)this.findViewById(R.id.health_back);
		iv_health_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HealthDataBaseInfo.this.finish();
			}
		});
		tv_name = (TextView)this.findViewById(R.id.et_name);
		tv_sex = (TextView)this.findViewById(R.id.et_sex);
		tv_age = (TextView)this.findViewById(R.id.et_age);
		tv_live = (TextView)this.findViewById(R.id.et_alone);
		tv_tel = (TextView)this.findViewById(R.id.et_archives_phone);
		
		tv_address = (TextView)this.findViewById(R.id.et_address);
		tv_guardian = (TextView)this.findViewById(R.id.et_watch_person);
		guardian_tel = (TextView)this.findViewById(R.id.et_watch_person_phone);
		tv_customhospital = (TextView)this.findViewById(R.id.et_healeh_hospital);
		tv_department = (TextView)this.findViewById(R.id.et_healeh_depart);
		
		tv_personaldoctor = (TextView)this.findViewById(R.id.et_healeh_coctor);
		tv_time = (TextView)this.findViewById(R.id.et_healeh_time);
	}
	private void initValue()
	{
		tv_name.setText(bean.getName());
		tv_sex.setText(bean.getSex());
		tv_age.setText(bean.getAge());
		tv_live.setText(bean.getLife());
		tv_tel.setText(bean.getTel());
		
		tv_address.setText(bean.getAddress());
		tv_guardian.setText(bean.getGuardian());
		guardian_tel.setText(bean.getGuardian_tel());
		tv_customhospital.setText(bean.getCustomhospital());
		tv_department.setText(bean.getDepartment());
		
		tv_personaldoctor.setText(bean.getPersonaldoctor());
		tv_time.setText(bean.getTime());
	}
}
