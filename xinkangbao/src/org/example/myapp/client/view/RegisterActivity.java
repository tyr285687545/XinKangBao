package org.example.myapp.client.view;
import org.example.myapp.R;
import org.example.myapp.client.model.User;
import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.MyTime;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.widget.LoadingDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	String sex="男";
	
	private ReturnObj obj;
	
	User user=new User();
	private Dialog builder;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_register);
		
		findViewById(R.id.rigister_btn_register).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) 
			{
				//简单写了下，
				EditText idEt=(EditText) findViewById(R.id.register_account);
				EditText passwordEt=(EditText) findViewById(R.id.register_password);
				EditText nameEt =(EditText) findViewById(R.id.register_nick);
				EditText emailEt =(EditText) findViewById(R.id.register_email);
				EditText addressEt =(EditText) findViewById(R.id.register_address);
				EditText ageEt =(EditText) findViewById(R.id.register_age);
				RadioGroup group = (RadioGroup)findViewById(R.id.register_radiogroup);
				
				group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup arg0,int id) {
						if(id==R.id.register_radio_nv){
							sex="女";
						}
					}
				});
	
				CheckBox ck_xieyi = (CheckBox)findViewById(R.id.register_xieyi_ch);
				if (ck_xieyi.isChecked() == false) {
					Toast.makeText(RegisterActivity.this, "请同意协议，", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(idEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")){
					Toast.makeText(RegisterActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				} else  if (Long.toString(Long.parseLong(idEt.getText().toString())).length() != MyAppConfig.MOBILE_LEN){  
					Toast.makeText(RegisterActivity.this, "账户为手机号，" + MyAppConfig.MOBILE_LEN + "为数字！", Toast.LENGTH_SHORT).show();
				} else if (nameEt.getText().toString().equals(""))  {		
					Toast.makeText(RegisterActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				} else if (ageEt.getText().toString().equals(""))  {		
					Toast.makeText(RegisterActivity.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
				} else {
					
					
					user.setId(Long.parseLong(idEt.getText().toString()));
					user.setPassword(passwordEt.getText().toString());
					user.setName(nameEt.getText().toString());
					
					user.setSex(sex);
					user.setMail(emailEt.getText().toString());
					user.setAddress(addressEt.getText().toString());
					Log.i("wangbo debug", ageEt.getText().toString());
					
					user.setAge(Integer.parseInt(ageEt.getText().toString()));
					
					user.setTime(MyTime.geTimeNoS());
					user.setOperation("register");
					builder = new LoadingDialog(RegisterActivity.this);
					builder.show();
					new Runnable() {
						public void run() 
						{
							obj = MainActivity.client_in_strict_mode.sendRegisterInfo(user);
						}
					}.run();
					if (obj.getRet_code() == 0) {
						//注册成功跳转到登陆
						Toast.makeText(RegisterActivity.this, "恭喜你，注册成功 ！", Toast.LENGTH_SHORT).show();
						builder.dismiss();
						startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					} else {
						builder.dismiss();
						Toast.makeText(RegisterActivity.this, "注册失败！" + obj.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
