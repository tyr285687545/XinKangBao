package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HealthInfoAdapter extends BaseAdapter{

	private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	
	public HealthInfoAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = inflater.inflate(R.layout.item_healthinfo, null);
		TextView tv_cont = (TextView)convertView.findViewById(R.id.tv_info);
		tv_cont.setText(list.get(position));
		return convertView;
	}
	
}
