package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.common.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonInfo extends Activity {

	private RelativeLayout relaImg;
	private RelativeLayout relaName;
	private RelativeLayout relaSex;
	private RelativeLayout relaAge;
	private RelativeLayout relaPhoneNum;
	private RelativeLayout relaCount;

	private TextView show_name;
	private TextView show_sex;
	private TextView show_age;
	private TextView show_count;
	private TextView show_phonenum;

	private ImageView img_back;

	private String name;
	private String sex;
	private int age;
	private long count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_detail);
		initView();
		initValue();
		initOnClickEven();
	}

	private void initOnClickEven() {
		relaImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		relaName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonInfo.this,Data_change.class);
				intent.putExtra("Flag", "0");
				startActivity(intent);
			}
		});
		relaSex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonInfo.this,Data_change.class);
				intent.putExtra("Flag", "1");
				startActivity(intent);
			}
		});
		relaAge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonInfo.this,Data_change.class);
				intent.putExtra("Flag", "2");
				startActivity(intent);
			}
		});
		show_count.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonInfo.this,Data_change.class);
				intent.putExtra("Flag", "3");
				startActivity(intent);
			}
		});
		relaPhoneNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initView() {
		relaImg = (RelativeLayout) findViewById(R.id.rela_person_top_head);
		relaName = (RelativeLayout) findViewById(R.id.rela_person_detail_name);
		relaSex = (RelativeLayout) findViewById(R.id.rela_person_detail_sex);
		relaAge = (RelativeLayout) findViewById(R.id.rela_person_detail_age);
		relaPhoneNum = (RelativeLayout) findViewById(R.id.rela_person_detail_count);
		relaCount = (RelativeLayout) findViewById(R.id.rela_person_detail_phonenum);

		show_name = (TextView) findViewById(R.id.tv_person_detail_show_name);
		show_sex = (TextView) findViewById(R.id.tv_person_detail_show_sex);
		show_age = (TextView) findViewById(R.id.tv_person_detail_show_age);
		show_count = (TextView) findViewById(R.id.tv_person_detail_show_count);
		show_phonenum = (TextView) findViewById(R.id.tv_person_detail_show_phonenum);

		img_back = (ImageView) findViewById(R.id.person_top_iv_back);
		img_back.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				PersonInfo.this.finish();
			}
		});
	}

	private void initValue() 
	{
		//获取用户数据
		name = MainActivity.myUser.getName();
		count = MainActivity.myUser.getId();
		age = MainActivity.myUser.getAge();
		sex = MainActivity.myUser.getSex();

		if (name != null && !StringUtils.isEmpty(name)) {
			show_name.setText(name);
		}
		if (sex != null && !StringUtils.isEmpty(sex)) {
			show_sex.setText(sex);
		}

		if (age != 0) {
			show_age.setText(String.valueOf(age));
		}else {
			show_age.setText("暂无数据");
		}
		if (count != 0) {
			show_count.setText(String.valueOf(count));
			show_phonenum.setText(String.valueOf(count));
		}
		img_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				PersonInfo.this.finish();
			}
		});
	}
}
