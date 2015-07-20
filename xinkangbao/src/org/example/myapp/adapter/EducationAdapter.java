package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.EducationArticle;
import org.example.myapp.common.StringUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EducationAdapter extends BaseAdapter {
	private Context context;
	private List<EducationArticle> articles;
	private LayoutInflater inflater;

	public EducationAdapter(Context context, List<EducationArticle> articles) {
		super();
		this.context = context;
		this.articles = articles;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return articles.size();
	}

	@Override
	public Object getItem(int position) {
		return articles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = inflater.inflate(R.layout.education_lv_item, null);

		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_context = (TextView) convertView
				.findViewById(R.id.tv_content);
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);

		String title = articles.get(position).getTitle();
		String content = articles.get(position).getContent();
		String time = StringUtils.friendly_time(articles.get(position)
				.getAddtime());

		if (!StringUtils.isEmpty(title)) {
			tv_name.setText(articles.get(position).getTitle());
		}
		if (!StringUtils.isEmpty(content)) {
			tv_context.setText(articles.get(position).getContent());
		}
		if (!StringUtils.isEmpty(time)) {
			tv_time.setText(StringUtils.friendly_time(articles.get(position)
					.getAddtime()));
		}

		return convertView;
	}

}
