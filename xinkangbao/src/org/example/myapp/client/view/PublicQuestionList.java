package org.example.myapp.client.view;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.adapter.PublicQuestionListAdapter;
import org.example.myapp.client.model.Post;
import org.example.myapp.client.model.PostList;
import org.example.myapp.common.MyAppConfig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class PublicQuestionList extends Activity {

	private String userId;
	private List<Post> list;
	private ListView lv;
	private PostList postList;
	private ImageView iv_person;
	private ProgressDialog getListDialog;
	private PublicQuestionListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.public_question);
		LoginActivity.mySharedPreferences = getSharedPreferences(
				MyAppConfig.SHARE_PREFERENCE_FILE, Activity.MODE_PRIVATE);

		userId = LoginActivity.mySharedPreferences.getString(
				"current_login_tel", "");
		postList = (PostList)getIntent().getSerializableExtra("postList");
		
		initView();
	}

	private void initView()
	{
		iv_person = (ImageView)findViewById(R.id.public_question_head_iv);
		iv_person.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PublicQuestionList.this, Myself.class);
				startActivity(intent);
			}
		});
		
		lv = (ListView)findViewById(R.id.public_question_lv);
		if (postList!=null && postList.getPostlist().size() > 0)
		{
			list =  postList.getPostlist();
			adapter = new PublicQuestionListAdapter(PublicQuestionList.this, list);
			lv.setAdapter(adapter);
		}
		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 跳转到问答详情
				Intent intent = new Intent(PublicQuestionList.this,
						QuestionDetailActivity.class);
				intent.putExtra("post_id", list.get(position).getTopic_id());
				/*
				 * intent.putExtra("addtime", post.getAddtime());
				 * intent.putExtra("title", post.getTitle());
				 * intent.putExtra("content", post.getContent());
				 * intent.putExtra("createName", post.getCreateName());
				 * intent.putExtra("views", post.getViews());
				 * intent.putExtra("comments", post.getComments());
				 */
				startActivity(intent);
			}
			
		});
	}
	
}
