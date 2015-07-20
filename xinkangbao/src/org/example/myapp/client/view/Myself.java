package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Myself extends Activity {
	private RelativeLayout layoutAppoin;
	private RelativeLayout layoutMyDoc;
	private RelativeLayout layoutManager;
	private RelativeLayout layoutChangePsw;
	private Button btnOutLoad;
	private ImageView ivToDetail;
	private ImageView ivHead;
	private ImageView person_iv;
	private TextView tvUserName;
	private TextView tvNumber;

	private String number;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person);
		initView();
		initValue();
		onClickEvent();
	}

	private void initView() {
		layoutAppoin = (RelativeLayout) this.findViewById(R.id.item_1);
		layoutMyDoc = (RelativeLayout) this.findViewById(R.id.item_2);
		layoutManager = (RelativeLayout) this.findViewById(R.id.item_3);
		layoutChangePsw = (RelativeLayout) this.findViewById(R.id.item_4);

		tvUserName = (TextView) this.findViewById(R.id.tv_name);
		tvNumber = (TextView) this.findViewById(R.id.tv_phone);

		ivToDetail = (ImageView) this.findViewById(R.id.iv_toright);
		ivHead = (ImageView) this.findViewById(R.id.img_head);
		person_iv = (ImageView) this.findViewById(R.id.person_iv);

		btnOutLoad = (Button) this.findViewById(R.id.btn_out_loading);
	}

	private void initValue() {
		number = LoginActivity.mySharedPreferences.getString(
				"current_login_tel", "");
		name = MainActivity.myUser.getName();

		if (number != null && !StringUtils.isEmpty(number)) {
			tvNumber.setText(number);
		}
		if (name != null && !StringUtils.isEmpty(name)) {
			tvUserName.setText(name);
		}

	}

	private void onClickEvent() {
		// ��ϸ����
		ivToDetail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Myself.this, PersonInfo.class);
				startActivity(intent);
			}
		});

		// �ҵ�ԤԼ
		layoutAppoin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(Myself.this, "�ܱ�Ǹ,��ʱû���ҵ�ԤԼ����.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		// �ҵ�ҽ��
		layoutMyDoc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent toMyDoc = new Intent(Myself.this, MyDoctor.class);
				startActivity(toMyDoc);
			}
		});
		// �ն˹���
		layoutManager.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(Myself.this, "�ܱ�Ǹ,��ʱû���ն˹������.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		// �޸�����
		layoutChangePsw.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Myself.this, ChangePsw.class);
				startActivity(intent);
			}
		});
		// ����
		person_iv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Myself.this.finish();
			}
		});
		// �˳���¼
		btnOutLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent logoutIntent = new Intent(Myself.this,
						LoginActivity.class);
				logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(logoutIntent);
			}
		});
	}
}
