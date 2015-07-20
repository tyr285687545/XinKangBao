package org.example.myapp.client.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.adapter.PostAdapter;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.model.NotReadMessage;
import org.example.myapp.client.model.PostList;
import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.ReturnObj;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Activity_question extends Activity {

	private ImageView iv_person;
	
	private RelativeLayout question_rela_public;

	private ListView lv;
	
	private PostList postList;

	private List<NotReadMessage> list_NotReadMessages = new ArrayList<NotReadMessage>();

	private PostAdapter postAdapter;

	private ProgressDialog getListDialog;

	private ReturnObj ret;

	private String userId;

	private List<Doctor> doc_info = new ArrayList<Doctor>();

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1:
				getListDialog.dismiss();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question);

		LoginActivity.mySharedPreferences = getSharedPreferences(
				MyAppConfig.SHARE_PREFERENCE_FILE, Activity.MODE_PRIVATE);

		userId = LoginActivity.mySharedPreferences.getString(
				"current_login_tel", "");

		getListDialog = new ProgressDialog(Activity_question.this);
		getListDialog.setMessage("正在医生列表,请稍后.");
		getListDialog.setCancelable(false);
		getListDialog.show();
		getNotReadMsg.run();
		getDco_info.run();

		initView();
	}

	private void initView() {
		question_rela_public = (RelativeLayout)findViewById(R.id.question_rela_public);
		question_rela_public.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			public void onClick(View v) {
				new AsyncTask() 
				{
					protected void onPreExecute() {
						getListDialog = new ProgressDialog(Activity_question.this);
						getListDialog.setMessage("正在获取数据,请稍后.");
						getListDialog.setCancelable(false);
						getListDialog.show();
					};
					@Override
					protected Object doInBackground(Object... params)
					{
						ret = MainActivity.client_in_strict_mode.get_post_list(Long
								.parseLong(userId));
						if (ret.getRet_code() == 0)
						{
							postList = PostList.parse(ret.getOrg_str());
						}
						return null;
					}
					@Override
					protected void onPostExecute(Object result) 
					{
						super.onPostExecute(result);
						Intent intent = new Intent(Activity_question.this,PublicQuestionList.class);
						intent.putExtra("postList", (Serializable)postList);
						getListDialog.dismiss();
						startActivity(intent);
					}
				}.execute("");
			}
		});
		// listView
		lv = (ListView) findViewById(R.id.question_listview);
		postAdapter = new PostAdapter(Activity_question.this, doc_info);
		lv.setAdapter(postAdapter);
		// 个人中心
		iv_person = (ImageView) findViewById(R.id.question_head_iv);
		iv_person.setOnClickListener(new OnClickListener() {

			public void onClick(View v) 
			{
				Intent intent = new Intent(Activity_question.this, Myself.class);
				startActivity(intent);
			}
		});
		initOnClick();//点击事件
	}

	private void initOnClick() {
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Activity_question.this, ChatActivity.class);
				intent.putExtra("tel", doc_info.get(position).getDoc_id());
				intent.putExtra("name", doc_info.get(position).getName());
				intent.putExtra("isinline", doc_info.get(position).getIsOnline());
				intent.putExtra("major", doc_info.get(position).getMajor());
				intent.putExtra("person", doc_info.get(position).getPerson());
				startActivity(intent);
			}
		});
	}

	// 获取医生信息
	Thread getDco_info = new Thread(new Runnable() {
		public void run() {
			for (int i = 0; i < list_NotReadMessages.size(); i++)
			{
				for (int j = 0; j < LoginActivity.buddyEntityList.size(); j++) 
				{
					if (Long.parseLong(list_NotReadMessages.get(i).getSendertel()) == LoginActivity.buddyEntityList
							.get(j).getDoc_id()) 
					{
						Doctor doctor = LoginActivity.buddyEntityList.get(i);
						doctor.setUnreadcount(Integer.parseInt(list_NotReadMessages
								.get(i).getUnreadcnt()));//设置未读消息数量
						doc_info.add(doctor);
					}
				}
			}
			Message msg = new Message();
			msg.arg1 = 2;
			handler.sendMessage(msg);
		}
	});

	// 获取未读消息
	Thread getNotReadMsg = new Thread(new Runnable() {
		public void run() {
			ret = MainActivity.client_in_strict_mode
					.get_user_doc_msg_toread_list(Long.parseLong(userId));
			Parser(ret.getOrg_str());
			Message msg = new Message();
			msg.arg1 = 1;
			handler.sendMessage(msg);
		}
	});

	private void Parser(String str) {
		try {
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("list");
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					NotReadMessage notReadMessage = new NotReadMessage();
					notReadMessage.setUnreadcnt(jsonObject
							.getString("unreadcnt"));
					notReadMessage.setSendertel(jsonObject
							.getString("sendertel"));
					notReadMessage.setRefresh(false);
					list_NotReadMessages.add(notReadMessage);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	// 获取帖子列表
		Thread getList = new Thread(new Runnable() {

			public void run() 
			{
				Message msg = new Message();
				ret = MainActivity.client_in_strict_mode.get_post_list(Long
						.parseLong(userId));
				if (ret.getRet_code() == 0) {
					postList = PostList.parse(ret.getOrg_str());
					msg.what = postList.getPageSize();
					msg.obj = postList;
				}
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
		});
}
