package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private LayoutInflater inflater;

	public SortAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_hospital, null);
		TextView tv_name = (TextView) convertView
				.findViewById(R.id.hospital_item_name);
		tv_name.setText(list.get(position));
		return convertView;
	}

}
