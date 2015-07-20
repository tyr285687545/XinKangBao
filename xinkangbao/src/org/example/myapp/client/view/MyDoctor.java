package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.adapter.MyDocAdapter;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.ReturnObj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MyDoctor extends Activity {
	private YQClient new_http_client;
	private ImageView iv_back;
	private ListView lv;
	ReturnObj ret_obj = null;

	MyDocAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_doctor);
		if (adapter!=null) {
			adapter.notifyDataSetChanged();
		}
		if (new_http_client == null) {
			new_http_client = new YQClient(true);
		}
		initView();
		initEvent();
	}

	private void initView() 
	{
		lv = (ListView) findViewById(R.id.my_doctor_listview);
		iv_back = (ImageView)findViewById(R.id.doctor_head_menu_back);
		iv_back = (ImageView)findViewById(R.id.doctor_head_menu_back);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(MyDoctor.this,DoctorDetail.class);
				intent.putExtra("tel", LoginActivity.doc.get(position).getDoc_id());
				intent.putExtra("name", LoginActivity.doc.get(position).getName());
				intent.putExtra("isonline", LoginActivity.doc.get(position).getIsOnline());
				intent.putExtra("major", LoginActivity.doc.get(position).getMajor());
				intent.putExtra("person", LoginActivity.doc.get(position).getPerson());
				intent.putExtra("department", LoginActivity.doc.get(position).getDepartment());
				intent.putExtra("job", LoginActivity.doc.get(position).getJob());
				intent.putExtra("hospital", LoginActivity.doc.get(position).getHospital());
				intent.putExtra("thumb_src", LoginActivity.doc.get(position).getThumb_url());
				startActivity(intent);
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initEvent() 
	{
		adapter = new MyDocAdapter(MyDoctor.this, LoginActivity.doc);
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
		iv_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}