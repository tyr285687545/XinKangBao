package org.example.myapp.adapter;

import java.util.HashMap;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.common.Hospital;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Hospital_Adapter extends BaseAdapter {

	private List<Hospital> list;
	private Context context;
	private LayoutInflater inflater;
	private TextView tv_name;
	private TextView tv_level;
	private CheckBox tv_cb;
	private static HashMap<Integer, Boolean> isSelected;

	public Hospital_Adapter(Context context, List<Hospital> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		Hospital_Adapter.isSelected = isSelected;
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		convertView = inflater.inflate(R.layout.item_hospital, null);
		tv_name = (TextView) convertView
				.findViewById(R.id.hospital_item_name);
		tv_level = (TextView) convertView
				.findViewById(R.id.hospital_item_level);
		tv_cb = (CheckBox) convertView
				.findViewById(R.id.hospital_item_cb);

		tv_name.setText(list.get(position).getName());
		tv_level.setText(list.get(position).getLevel());
		tv_cb.setChecked(getIsSelected().get(position));
		if (!getIsSelected().get(position)) {
			tv_cb.setVisibility(View.GONE);
		}else {
			tv_cb.setVisibility(View.VISIBLE);
		}
		tv_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				Log.d("sky", "onCheckedChanged");
			}
		});
		return convertView;
	}
	@Override
	public void notifyDataSetChanged() 
	{
		super.notifyDataSetChanged();
	}
}
