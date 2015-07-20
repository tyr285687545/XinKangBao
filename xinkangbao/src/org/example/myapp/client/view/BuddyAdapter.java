package org.example.myapp.client.view;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.common.AppContext;
import org.example.myapp.common.StringUtils;

import com.example.myapp.utils.GetBitmapFromInternet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "ViewHolder" })
public class BuddyAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<Doctor> list;
	LayoutInflater inflater;
	private ImageView img_head;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();  
	public BuddyAdapter(Context context, List<Doctor> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public View getView(final int position, View convertView, ViewGroup root)
	{
		convertView = inflater.inflate(R.layout.doctor_item, null);
		if (position % 2 == 1) {
			convertView.setBackgroundColor(Color.WHITE);
		} else 
		{
			convertView.setBackgroundColor(Color.parseColor("#FCFCFC"));
		}
		TextView name = (TextView) convertView.findViewById(R.id.item_tv_name);
		TextView major = (TextView) convertView
				.findViewById(R.id.item_tv_indications);
		TextView online = (TextView) convertView
				.findViewById(R.id.item_tv_hospital);
		TextView weidu = (TextView) convertView.findViewById(R.id.item_tv_post);

		img_head = (ImageView) convertView.findViewById(R.id.item_image_head);

		if (!StringUtils.isEmpty(list.get(position).getThumb_url())) 
		{
			ImageLoader.getInstance().displayImage("http://"+list.get(position).getThumb_url(), img_head);
		} 

		Doctor be = list.get(position);
		
		if (!StringUtils.isEmpty(be.getHospital())) {
			online.setText(be.getHospital());
		}
		
		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.WHITE);
		} else {
			convertView.setBackgroundColor(Color.parseColor("#FCFCFC"));
		}
		if (be.getMes_to_read() != 0) {
			weidu.setText("(" + Integer.toString(be.getMes_to_read()) + ")");
			weidu.setTextColor(Color.rgb(255, 0, 0));
			name.setTextColor(Color.rgb(255, 0, 0));
		} else {
			weidu.setTextColor(Color.rgb(0, 0, 0));
			name.setTextColor(Color.rgb(0, 0, 0));
		}
		weidu.setText(be.getJob());

		name.setText(be.getName());
		major.setText(be.getMajor());

		return convertView;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
