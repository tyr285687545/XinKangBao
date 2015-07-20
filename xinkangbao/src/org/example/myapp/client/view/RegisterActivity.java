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
	String sex="��";
	
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
				//��д���£�
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
							sex="Ů";
						}
					}
				});
	
				CheckBox ck_xieyi = (CheckBox)findViewById(R.id.register_xieyi_ch);
				if (ck_xieyi.isChecked() == false) {
					Toast.makeText(RegisterActivity.this, "��ͬ��Э�飬", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(idEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")){
					Toast.makeText(RegisterActivity.this, "�˺Ż����벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
				} else  if (Long.toString(Long.parseLong(idEt.getText().toString())).length() != MyAppConfig.MOBILE_LEN){  
					Toast.makeText(RegisterActivity.this, "�˻�Ϊ�ֻ��ţ�" + MyAppConfig.MOBILE_LEN + "Ϊ���֣�", Toast.LENGTH_SHORT).show();
				} else if (nameEt.getText().toString().equals(""))  {		
					Toast.makeText(RegisterActivity.this, "�ǳƲ���Ϊ��", Toast.LENGTH_SHORT).show();
				} else if (ageEt.getText().toString().equals(""))  {		
					Toast.makeText(RegisterActivity.this, "���䲻��Ϊ��", Toast.LENGTH_SHORT).show();
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
						//ע��ɹ���ת����½
						Toast.makeText(RegisterActivity.this, "��ϲ�㣬ע��ɹ� ��", Toast.LENGTH_SHORT).show();
						builder.dismiss();
						startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					} else {
						builder.dismiss();
						Toast.makeText(RegisterActivity.this, "ע��ʧ�ܣ�" + obj.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
