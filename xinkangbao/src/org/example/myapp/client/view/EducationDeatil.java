package org.example.myapp.client.view;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.EducationArticle;
import org.example.myapp.common.AppContext;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EducationDeatil extends Activity {

	private TextView tv_title;
	private TextView tv_time;
	private TextView tv_author;
	private TextView tv_read;
	private TextView tv_content;

	private Button previous_article;
	private Button next_article;

	private String author = "作者:";
	private String reader = "阅读量:暂无统计";
	private int position;
	private int CURRENT_PAGE;
	private List<EducationArticle> list = AppContext.getInstance().getArticles();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.education_detail);

		position = getIntent().getIntExtra("position", 0);
		CURRENT_PAGE = position;
		initView();
		initOnClick();
	}

	private void initOnClick() {
		previous_article.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 如果用户有对疾病类型进行筛选,则显示用户所选择的该病
				if (CURRENT_PAGE > 0) {
					CURRENT_PAGE--;
					tv_title.setText(list
							.get(CURRENT_PAGE).getTitle());
					tv_time.setText(StringUtils.friendly_time(AppContext
							.getInstance().getArticles().get(CURRENT_PAGE)
							.getAddtime()));
					tv_author.setText(author
							+ list
									.get(CURRENT_PAGE).getName());
					tv_content.setText("		"
							+ list
									.get(CURRENT_PAGE).getContent());
					tv_read.setText(reader);
				} else {
					Toast.makeText(EducationDeatil.this, "已经是第一篇了。",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		next_article.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// 如果用户有对疾病类型进行筛选,则显示用户所选择的该病
				if (CURRENT_PAGE < list
						.size() - 1) {
					CURRENT_PAGE++;
					tv_title.setText(list
							.get(CURRENT_PAGE).getTitle());
					tv_time.setText(StringUtils.friendly_time(AppContext
							.getInstance().getArticles().get(CURRENT_PAGE)
							.getAddtime()));
					tv_author.setText(author
							+ list
									.get(CURRENT_PAGE).getName());
					tv_content.setText("  "
							+ list
									.get(CURRENT_PAGE).getContent());
					tv_read.setText(reader);
				} else {
					Toast.makeText(EducationDeatil.this, "已经是最后一篇了。",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_author = (TextView) findViewById(R.id.tv_author);
		tv_read = (TextView) findViewById(R.id.tv_education_reder);
		tv_content = (TextView) findViewById(R.id.tv_content);

		previous_article = (Button) findViewById(R.id.btn_on_a);
		next_article = (Button) findViewById(R.id.btn_next_article);

		tv_title.setText(list.get(position).getTitle());
		tv_time.setText(StringUtils.friendly_time(list.get(
				position).getAddtime()));
		tv_author.setText(author + list.get(position).getName());
		tv_content
				.setText("  " + list.get(position).getContent());
		tv_read.setText(reader);
	}

}
