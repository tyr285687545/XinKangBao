package org.example.myapp.client.view;

import org.example.myapp.widget.LoadingDialog;
import org.example.myapp.common.AppException;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户资料
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class DoctorInfoActivity extends Activity {

	private ImageView back;
	private TextView doc_name;
	private TextView doc_major;
	private TextView doc_summary;
	private LinearLayout chat_ll;
	private LinearLayout opp_ll;
	private LinearLayout adddoc_ll;
	private LinearLayout deldoc_ll;
	private LoadingDialog loading;
	private Handler mHandler;
	private Doctor doctor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info);
		
		this.initView();
		
		this.initData();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.user_info_back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}});

		doc_name = (TextView) findViewById(R.id.user_info_username);
		doc_major = (TextView) findViewById(R.id.user_info_major);
		doc_summary = (TextView) findViewById(R.id.doc_info_summary);
		
		chat_ll = (LinearLayout) findViewById(R.id.chat_ll);
		opp_ll = (LinearLayout) findViewById(R.id.opp_ll);
		adddoc_ll = (LinearLayout) findViewById(R.id.adddoc_ll);
		deldoc_ll = (LinearLayout) findViewById(R.id.deldoc_ll);
		
		int doccatalog = getIntent().getIntExtra("doccatalog", 0);
		if (1 == doccatalog)
			deldoc_ll.setVisibility(View.GONE);
		else
			adddoc_ll.setVisibility(View.GONE);
	}

	private void initData() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				
				if (msg.what == 1 && msg.obj != null) 
				{
					doctor = (Doctor) msg.obj;
					doc_name.setText(doctor.getName());
					doc_major.setText(doctor.getMajor());
					doc_summary.setText(doctor.getHospital()+"|"+doctor.getDepartment()+"|"+doctor.getJob());
					
					chat_ll.setOnClickListener(chatClickListener);
					opp_ll.setOnClickListener(oppClickListener);
					adddoc_ll.setOnClickListener(addClickListener);
					deldoc_ll.setOnClickListener(delClickListener);
				}	else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(DoctorInfoActivity.this);
				}
			}
		};
		this.loadUserInfoThread(false);
	}
	
	private void loadUserInfoThread(final boolean isRefresh) {
		loading = new LoadingDialog(this);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = new Message();
				Doctor user = Doctor.paser_str_to_obj(MainActivity.client_in_strict_mode.get_doc_info(getIntent().getLongExtra("tel", 0)));
				msg.what = 1;
				msg.obj = user;
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private View.OnClickListener chatClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			//打开聊天页面
			Intent intent=new Intent(DoctorInfoActivity.this,ChatActivity.class);
			intent.putExtra("tel", doctor.getDoc_id());
			intent.putExtra("name",doctor.getName());
			intent.putExtra("major", doctor.getMajor());
			intent.putExtra("isonline", doctor.getIsOnline());
			startActivity(intent);
		}
	};

	private View.OnClickListener oppClickListener = new View.OnClickListener() 
	{
		public void onClick(View v) 
		{
			Intent intent_yuyue = new Intent(DoctorInfoActivity.this,DoctorOrderActivity.class);
			intent_yuyue.putExtra("tel", doctor.getDoc_id());
			intent_yuyue.putExtra("name",doctor.getName());
			//intent_yuyue.putExtra("major", doctor.getMajor());
			//intent_yuyue.putExtra("isonline", doctor.getIsOnline());
			startActivity(intent_yuyue);
		}
	};

	private View.OnClickListener addClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				Long doc_id = doctor.getDoc_id();
				Long user_id = MainActivity.myUser.getId();
				
				ReturnObj obj = MainActivity.client_in_strict_mode.add_user_doc(user_id, doc_id);
				//todo
				obj.setRet_code(0);
				if (obj.getRet_code() == 0) {
					adddoc_ll.setVisibility(View.GONE);
					deldoc_ll.setVisibility(View.VISIBLE);
					Toast.makeText(DoctorInfoActivity.this, "添加医生成功 ", Toast.LENGTH_SHORT).show();
					//BuddyActivity.buddyEntityList.add(doctor);
				} else {
					Toast.makeText(DoctorInfoActivity.this, "添加医生失败：  " + obj.getMsg(), Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(DoctorInfoActivity.this, "添加医生失败：  " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private View.OnClickListener delClickListener = new View.OnClickListener() {
		public void onClick(View v) 
		{
			//向服务器发送一个删除好友的包
			ReturnObj obj = MainActivity.client_in_strict_mode.del_user_doc(MainActivity.myUser.getId(), doctor.getDoc_id());
//			LoginActivity.buddyEntityList.remove(location)
			if (obj.getRet_code() != 0) {
				Toast.makeText(DoctorInfoActivity.this, "删除医生失败! " + obj.getMsg(), Toast.LENGTH_SHORT).show();
			}
			else
			{
				for (int i = 0; i < LoginActivity.doc.size(); i++) 
				{
					if (LoginActivity.doc.get(i).getDoc_id().equals(doctor.getDoc_id()))
					{
						LoginActivity.doc.remove(i);
					}
				}
				adddoc_ll.setVisibility(View.VISIBLE);
				deldoc_ll.setVisibility(View.GONE);
			}
		}
	};
}
