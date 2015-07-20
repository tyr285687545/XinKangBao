package org.example.myapp.client.view;
import org.example.myapp.R;


import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.view.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity{

	public void updateInfo() {
		TextView name=(TextView) findViewById(R.id.name_more);
		TextView sex=(TextView) findViewById(R.id.sex_more);
		TextView age=(TextView) findViewById(R.id.age_more);
		
		TextView mail=(TextView) findViewById(R.id.mail_more);
		TextView address=(TextView) findViewById(R.id.address_more);
		TextView tel=(TextView) findViewById(R.id.tel_more);
		
		/**
		TextView trends=(TextView) findViewById(R.id.trends_more);
		*/
		//avatar.setImageResource(ChatActivity.avatar[me.getAvatar()]);
	
		name.setText("姓名： " + MainActivity.myUser.getName());
		tel.setText("手机号： " + Long.toString(MainActivity.myUser.getId()));
		sex.setText("性别：" + MainActivity.myUser.getSex());
		age.setText("年龄：" + MainActivity.myUser.getAge()+"岁");
		mail.setText("邮箱：" + MainActivity.myUser.getMail());
		address.setText("地址：" + MainActivity.myUser.getAddress());
	
	}
	
	protected void onResume() {
		
		updateInfo();
		super.onResume();
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		ManageActivity.addActiviy("moreActivity", MoreActivity.this);
		updateInfo();
		
		/**
		trends.setText(me.getTrends());
		*/
		
		Button btnLogin=(Button) findViewById(R.id.add_doc_rela);
		
		btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) 
			{
				
//				final EditText et = new EditText(MoreActivity.this);
//				et.setHint("医生的11位手机号");
//				et.setInputType(InputType.TYPE_CLASS_NUMBER);
//				AlertDialog.Builder dialog = new AlertDialog.Builder(MoreActivity.this);
//				dialog.setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info);
//				dialog.setView(et);
//				
//				dialog.setPositiveButton("确定", 
//						new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								if (et.getText().toString().equals("")) {
//									Toast.makeText(MoreActivity.this, "医生的联系方式不能为空", Toast.LENGTH_SHORT).show();
//									dialog.dismiss();
//								} else {
//									try {
//										Long doc_id = Long.parseLong(et.getText().toString());
//										Long user_id = MainActivity.myUser.getId();
//										
//										ReturnObj obj = MainActivity.client_in_strict_mode.add_user_doc(user_id, doc_id);
//										//todo
//										obj.setRet_code(0);
//										if (obj.getRet_code() == 0) {
//											Toast.makeText(MoreActivity.this, "添加医生成功 ", Toast.LENGTH_SHORT).show();
//											dialog.dismiss();
//											
//											Doctor doc_tmp = Doctor.paser_str_to_obj((MainActivity.client_in_strict_mode.get_doc_info(doc_id)));
//										
//											BuddyActivity.buddyEntityList.add(doc_tmp);
//										} else {
//											Toast.makeText(MoreActivity.this, "添加医生失败：  " + obj.getMsg(), Toast.LENGTH_SHORT).show();
//											dialog.dismiss();
//										}
//									} catch (Exception e) {
//										Toast.makeText(MoreActivity.this, "添加医生失败：  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//										dialog.dismiss();
//									}
//									
//								}
//							}
//						});
//				
//				dialog.setNegativeButton("取消", null);
//				dialog.show();
			}
	    });
		
		Button btn_exit=(Button) findViewById(R.id.exit_app);
		btn_exit.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
		    	if(ManageActivity.getActivity("loginActivity")!=null){
		    		Log.i("wangbo", "debug here loginactivity exit!");
		    		ManageActivity.getActivity("loginActivity").finish();
		    	}
		    	if (ManageActivity.getActivity("buddyActivity") != null) {
		    		ManageActivity.getActivity("buddyActivity").finish();
		    	}
		    	MoreActivity.this.finish();
				System.exit(0);
				
			}
		});
		
		
		Button btn_update_user_info=(Button) findViewById(R.id.update_user_info);
		btn_update_user_info.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				//跳出新的app
				startActivity(new Intent(MoreActivity.this, UpdateUserInfoActivity.class));
				
			}
		});
		
		
		Button btn_order_detail = (Button) findViewById(R.id.order_item_detail);
		btn_order_detail.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {	
				//跳出新的app
				startActivity(new Intent(MoreActivity.this, UserOrderActivity.class));
			}
		});

		
		Button btn_pos_service = (Button) findViewById(R.id.pos_service);
		btn_pos_service.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {	
				//跳出新的app
				startActivity(new Intent(MoreActivity.this, PosServiceActivity.class));
			}
		});

	}

}
