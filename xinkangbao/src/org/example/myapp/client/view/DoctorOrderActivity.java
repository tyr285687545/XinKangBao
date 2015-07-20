package org.example.myapp.client.view;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.common.MyTime;
import org.example.myapp.common.ReturnObj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DoctorOrderActivity extends Activity {
	
	
	/** Called when the activity is first created. */
	private EditText startDateTime;
//	private Spinner doc_info_spin;
	private String initStartDateTime = ""; // 初始化开始时间
//	private ArrayAdapter<String> adapter;

	private ReturnObj ret_obj;
//	private List<Doctor> doc_list;
	private Long doc_tel;
	private String doc_name;
	private String appdatetime;
	private String name;
//	private ArrayList<String> select_list_str;
	
//	private Long doc_tel_selected;
//	private String doc_name_selected;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_order);

//		int select_pos = -1;
		doc_tel = getIntent().getLongExtra("tel", 0);
		Log.d("sky", "doc_tel = "+doc_tel);
		doc_name = getIntent().getStringExtra("name");
//		doc_info_spin = (Spinner) findViewById(R.id.editDocInfo);
//		
//		doc_list = Doctor.paser_str_to_objlist(MainActivity.client_in_strict_mode.get_user_doc_list(MainActivity.myUser.getId()).getOrg_str());
//		select_list_str = new ArrayList<String>();
//		for (int i=0; i < doc_list.size(); i++) {
//			select_list_str.add(doc_list.get(i).getName() + "|" + Long.toString(doc_list.get(i).getDoc_id()));
//			if (doc_tel != 0 && doc_list.get(i).getDoc_id().equals(doc_tel)) {
//				select_pos = i;
//				doc_tel_selected = doc_tel;
//				doc_name_selected = doc_list.get(i).getName();
//			}
//		}
//		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, select_list_str);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		doc_info_spin.setAdapter(adapter);
//		if (select_pos != -1) {
//			doc_info_spin.setSelection(select_pos);
//		}
//		doc_info_spin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//
//				String temp = doc_info_spin.getSelectedItem().toString();
//
//				int pos = temp.indexOf("|");
//				doc_tel_selected = Long.parseLong(temp.substring(pos + 1));
//				doc_name_selected = temp.substring(0, pos);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		name = LoginActivity.mySharedPreferences.getString("current_login_tel", "");
		// 两个输入框
		startDateTime = (EditText) findViewById(R.id.inputDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		initStartDateTime = sdf.format(new Date());
		startDateTime.setText(initStartDateTime);
		
		startDateTime.setOnFocusChangeListener(new OnFocusChangeListener() {
				  @Override
			        public void onFocusChange(View v, boolean hasFocus) 
				  {
			            // TODO Auto-generated method stub
					  if (hasFocus) 
					  {
						  DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
									DoctorOrderActivity.this, initStartDateTime);
							dateTimePicKDialog.dateTimePicKDialog(startDateTime);
					  }
				  }
		});
		
		
		startDateTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						DoctorOrderActivity.this, initStartDateTime);
				dateTimePicKDialog.dateTimePicKDialog(startDateTime);
			}
		});
			
		
		
		//监控确认时 
		Button btn_ok = (Button) findViewById(R.id.buttonOk);
		
		Button btn_cal = (Button) findViewById(R.id.buttonCal);
		
		btn_ok.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				//跳出新的app,发起预约请求
				appdatetime = startDateTime.getText().toString() + ":00";
				appdatetime = appdatetime.replace('年', '-');
				appdatetime = appdatetime.replace('月', '-');
				appdatetime = appdatetime.replace("日", "");

				new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ret_obj = MainActivity.client_in_strict_mode.add_user_oder(Long.valueOf(name), doc_tel, appdatetime);
					}
				}.run();
				if (ret_obj.getRet_code() == 0) {
					Toast.makeText(DoctorOrderActivity.this, "提交成功：" + doc_name + "@" + appdatetime, Toast.LENGTH_SHORT).show();
					DoctorOrderActivity.this.finish();
				} else {
					Toast.makeText(DoctorOrderActivity.this, "提交失败：" + ret_obj.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_cal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DoctorOrderActivity.this.finish();
			}
		});
	}
}
