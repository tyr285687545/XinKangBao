package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.model.NotReadMessage;
import org.example.myapp.widget.Round;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter
{
	private List<Doctor> list;
	private Context context;
	private LayoutInflater inflater;
	public PostAdapter(Context context, List<Doctor> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		
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
		convertView = inflater.inflate(R.layout.question_lv_item, null);
		
		TextView tv_name = (TextView)convertView.findViewById(R.id.question_lv_item_docname);
		TextView tv_context = (TextView)convertView.findViewById(R.id.question_lv_item_context);
		Round round = (Round)convertView.findViewById(R.id.round);
		round.setSize(list.get(position).getUnreadcount());
		tv_name.setText(list.get(position).getName());
		tv_context.setText(list.get(position).getHospital());
		return convertView;
	}
	
}
