package org.example.myapp.client.view;

import java.util.ArrayList;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Order;
import org.example.myapp.common.ReturnObj;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class UserOrderActivity extends Activity {

	public ListView listView = null;
	public static List<Order> orderEntityList = new ArrayList<Order>();// 好友列表
	public static UserOrderAdapter ba = null;
	private String orderListStr = "";

	protected void onResume() {
		super.onResume();
		paser_user_order_list();
		ba.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_user_order);
		ManageActivity.addActiviy("UserOrderActivity", UserOrderActivity.this);

		TextView tel = (TextView) findViewById(R.id.buddy_top_tel);
		TextView name = (TextView) findViewById(R.id.buddy_top_name);
		ImageButton new_order_btn = (ImageButton) findViewById(R.id.add_new_order);
		new_order_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// DoctorOrderActivity

				// 新建一个页面；todo
				Intent intent_yuyue = new Intent(UserOrderActivity.this,
						DoctorOrderActivity.class);
				intent_yuyue.putExtra("tel", 0);
				startActivity(intent_yuyue);
			}
		});

		name.setText(MainActivity.myUser.getName());
		tel.setText(Long.toString(MainActivity.myUser.getId()));

		// 填充数据

		listView = (ListView) findViewById(R.id.listview);
		ba = new UserOrderAdapter(this, orderEntityList);
		listView.setAdapter(ba);

	}

	private void paser_user_order_list() {
		// TODO Auto-generated method stub
		ReturnObj ret_obj = MainActivity.client_in_strict_mode
				.get_user_order_list(MainActivity.myUser.getId());
		if (ret_obj.getRet_code() == 0) {
			orderListStr = ret_obj.getOrg_str();
			jieXi(orderListStr);
		} else {
			orderListStr = "";
		}
	}

	private void jieXi(String str) {
		// TODO Auto-generated method stub
		orderEntityList.clear();
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			for (int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				Order order_tmp = new Order();
				order_tmp.setAppTime(oj_tmp.getString("appdatetime"));

				order_tmp.setDocName(oj_tmp.getString("docname"));
				order_tmp.setPatName(oj_tmp.getString("patname"));

				order_tmp.setDocTel(Long.parseLong(oj_tmp.getString("doctel")));
				order_tmp.setPatTel(Long.parseLong(oj_tmp.getString("pattel")));
				order_tmp.setOrderId(Integer.parseInt(oj_tmp.getString("id")));
				order_tmp
						.setStatus(Integer.parseInt(oj_tmp.getString("status")));

				orderEntityList.add(order_tmp);
			}

		} catch (JSONException e) {
			orderEntityList.clear();
		}
	}
}
