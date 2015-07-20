package org.example.myapp.client.view;

import org.example.myapp.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Data_change extends Activity {

	private EditText et_input_data;
	private int hint;
	private String inputData;
	private ImageView iv_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_data_change);
		hint = Integer.valueOf(getIntent().getStringExtra("Flag"));
		
		initView();
	}

	private void initView() {
		iv_back = (ImageView)findViewById(R.id.data_change_top_iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Data_change.this.finish();
			}
		});
		
		et_input_data = (EditText) findViewById(R.id.tv_data_change);
		// ��ȡ����
		et_input_data.setFocusable(true);
		et_input_data.setFocusableInTouchMode(true);
		et_input_data.requestFocus();
		switch (hint) 
		{
		case 0:
			et_input_data.setHint("����������");
			et_input_data.setInputType(InputType.TYPE_CLASS_TEXT);// ���������ı�����
			break;
		case 1:
			et_input_data.setHint("�������Ա�");
			et_input_data.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 2:
			et_input_data.setHint("����������");
			et_input_data.setInputType(InputType.TYPE_CLASS_NUMBER);
			break;
		case 3:
			et_input_data.setHint("������绰");
			et_input_data.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		}
	}

	// ���������
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			Log.v("sky", "KEYCODE_ENTER");
			/* ��������� */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(Data_change.this
						.getCurrentFocus().getWindowToken(), 0);
			}
			inputData = et_input_data.getText().toString().trim();
			
			//�ж������Ƿ���������
			switch (hint) {
			case 0:
				if (inputData.length() > 0 && inputData.length() < 10) {
					MainActivity.myUser.setName(inputData);
					Toast.makeText(Data_change.this, "�޸ĳɹ�!", Toast.LENGTH_LONG)
							.show();
					Data_change.this.finish();
				} else {
					Toast.makeText(Data_change.this, "�������������0��С��10!",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 1:
				if (inputData.equals("��") || inputData.equals("Ů")) {
					MainActivity.myUser.setSex(inputData);
					Toast.makeText(Data_change.this, "�޸ĳɹ�!", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(Data_change.this, "�Ա�ֻ�����к�Ů!",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				if (Integer.valueOf(inputData) > 0
						&& Integer.valueOf(inputData) <= 117) {
					MainActivity.myUser.setAge(Integer.valueOf(inputData));
					Toast.makeText(Data_change.this, "�޸ĳɹ�!", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(Data_change.this, "���䳬����Χ!",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 3:

				break;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
