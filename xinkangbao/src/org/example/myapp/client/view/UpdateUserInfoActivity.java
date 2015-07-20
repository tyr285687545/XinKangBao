package org.example.myapp.client.view;
import java.util.Date;

import org.example.myapp.R;
import org.example.myapp.client.model.User;
import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.MyTime;
import org.example.myapp.common.ReturnObj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class UpdateUserInfoActivity extends Activity {
	String sex="男";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_update_user_info);
		
		
		EditText idEt=(EditText) findViewById(R.id.update_account);
		EditText passwordEt=(EditText) findViewById(R.id.update_password);
		EditText nameEt =(EditText) findViewById(R.id.update_nick);
		EditText emailEt =(EditText) findViewById(R.id.update_email);
		EditText addressEt =(EditText) findViewById(R.id.update_address);
		EditText ageEt =(EditText) findViewById(R.id.update_age);
		RadioGroup group = (RadioGroup)findViewById(R.id.update_radiogroup);
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0,int id) {
				if(id == R.id.update_radio_nv){
					sex = "女";
				} else {
					sex = "男";
				}
			}
		});

		
		RadioButton nan = (RadioButton)findViewById(R.id.update_radio_nan);
		RadioButton nv = (RadioButton)findViewById(R.id.update_radio_nv);
		User myuser = MainActivity.myUser;
		
		idEt.setText(Long.toString(myuser.getId()));
		passwordEt.setText(myuser.getPassword());
		nameEt.setText(myuser.getName());
		emailEt.setText(myuser.getMail());
		addressEt.setText(myuser.getAddress());
		ageEt.setText(Integer.toString(myuser.getAge()));
		
		if (myuser.getSex().equals("男")) {
			nan.setChecked(true);
		} else {
			nv.setChecked(true);
		}
		
		findViewById(R.id.update_btn_cancel).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				UpdateUserInfoActivity.this.finish();
			}
		});
		
		

		
		findViewById(R.id.update_btn_exe).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				final User user = MainActivity.myUser;
				//简单写了下，
				EditText idEt=(EditText) findViewById(R.id.update_account);
				EditText passwordEt=(EditText) findViewById(R.id.update_password);
				EditText nameEt =(EditText) findViewById(R.id.update_nick);
				
				EditText emailEt =(EditText) findViewById(R.id.update_email);
				EditText addressEt =(EditText) findViewById(R.id.update_address);
				
				EditText ageEt =(EditText) findViewById(R.id.update_age);
				
				RadioGroup group = (RadioGroup)findViewById(R.id.update_radiogroup);
				
				
		
				
				if(idEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")){
					Toast.makeText(UpdateUserInfoActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				} else  if (Long.toString(Long.parseLong(idEt.getText().toString())).length() != MyAppConfig.MOBILE_LEN){  
					Toast.makeText(UpdateUserInfoActivity.this, "账户为手机号，" + MyAppConfig.MOBILE_LEN + "为数字！", Toast.LENGTH_SHORT).show();
				} else if (nameEt.getText().toString().equals(""))  {		
					Toast.makeText(UpdateUserInfoActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				} else if (ageEt.getText().toString().equals(""))  {		
					Toast.makeText(UpdateUserInfoActivity.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
				} else {
					
					user.setId(Long.parseLong(idEt.getText().toString()));
					user.setPassword(passwordEt.getText().toString());
					user.setName(nameEt.getText().toString());
			
					user.setMail(emailEt.getText().toString());
					user.setAddress(addressEt.getText().toString());
					Log.i("wangbo debug", ageEt.getText().toString());
					
					user.setAge(Integer.parseInt(ageEt.getText().toString()));
					
					user.setTime(MyTime.geTimeNoS());
					user.setSex(sex);
					
					
					
					ReturnObj obj = MainActivity.client_in_strict_mode.sendUpdateInfo(user);
					if (obj.getRet_code() == 0) {
						MainActivity.myUser = user;
						//注册成功跳转到登陆
						Toast.makeText(UpdateUserInfoActivity.this, "恭喜你，修改信息成功 ！", Toast.LENGTH_SHORT).show();
						UpdateUserInfoActivity.this.finish();
					} else {
						//todo
						MainActivity.myUser = user;
						
						String place = "[" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber()+"] ";
						Log.i("debug update user info  debug here", place + obj.getMsg());
						Toast.makeText(UpdateUserInfoActivity.this, "修改信息失败！" + obj.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
