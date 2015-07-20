package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Post;
import org.example.myapp.common.StringUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PublicQuestionListAdapter extends BaseAdapter{

	@SuppressWarnings("unused")
	private Context context ;
	private List<Post> list;
	LayoutInflater inflater;
	public PublicQuestionListAdapter(Context context , List<Post> list)
	{
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = inflater.inflate(R.layout.public_question_lv_item,null);
		TextView tv_name = (TextView)convertView.findViewById(R.id.item_pulic_lv_item_tv_postname);
		TextView tv_context = (TextView)convertView.findViewById(R.id.item_public_lv_item_tv_context);
		TextView tv_time = (TextView)convertView.findViewById(R.id.item_public_lv_item_tv_time);
		
		tv_name.setText(list.get(position).getTitle());
		tv_context.setText(list.get(position).getContent());
		tv_time.setText(StringUtils.friendly_time(list.get(position).getAddtime()));
		
		return convertView;
	}

}
