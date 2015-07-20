package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.view.ChatActivity;
import org.example.myapp.client.view.DoctorInfoActivity;
import org.example.myapp.client.view.LoginActivity;
import org.example.myapp.client.view.MainActivity;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "InflateParams", "ViewHolder" })
public class MyDocAdapter extends BaseAdapter {
	
	private Context context;
	private List<Doctor> list;
	LayoutInflater inflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public MyDocAdapter(Context context, List<Doctor> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public View getView(final int position, View convertView, ViewGroup root) {
		convertView = inflater.inflate(R.layout.mydoc_item, null);
		ImageView iv_ask = (ImageView) convertView
				.findViewById(R.id.item_mydoc_iv_ask);
		ImageView myDoc_iv = (ImageView) convertView
				.findViewById(R.id.item_mydoc_image_head);
		ImageView iv_deldoc = (ImageView)convertView.findViewById(R.id.item_mydoc_iv_delete);
		iv_deldoc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Async().execute(Integer.valueOf(position));
			}
		});
		
		if (!StringUtils.isEmpty(list.get(position).getThumb_url())) 
		{
			ImageLoader.getInstance().displayImage(
					
					"http://" + list.get(position).getThumb_url(), myDoc_iv);
		}
		iv_ask.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("tel", list.get(position).getDoc_id());
				intent.putExtra("name", list.get(position).getName());
				intent.putExtra("isinline", list.get(position).getIsOnline());
				intent.putExtra("major", list.get(position).getMajor());
				context.startActivity(intent);
			}
		});
		if (position % 2 == 1) {
			convertView.setBackgroundColor(Color.WHITE);
		} else {
			convertView.setBackgroundColor(Color.parseColor("#FCFCFC"));
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.item_mydoc_tv_name);
		TextView major = (TextView) convertView
				.findViewById(R.id.item_mydoc_tv_indications);
		TextView online = (TextView) convertView
				.findViewById(R.id.item_mydoc_tv_hospital);
		TextView weidu = (TextView) convertView
				.findViewById(R.id.item_mydoc_tv_post);

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
	
	/**
	 * 删除我的医生
	 * */
	class Async extends AsyncTask<Integer, Void, ReturnObj>
	{
		String Error;
		String Succeed;
		ProgressDialog dialog ;
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("删除中..");
			dialog.setCancelable(false);
			dialog.show();
		}

		protected ReturnObj doInBackground(Integer... params) 
		{
			// TODO Auto-generated method stub
			Integer a = params[0];
			int x = a.intValue();
			ReturnObj obj = MainActivity.client_in_strict_mode.del_user_doc(MainActivity.myUser.getId(), list.get(x).getDoc_id());
			if (obj.getRet_code() != 0) 
			{
				Error = obj.getMsg();
			}
			else {
				for (int i = 0; i < LoginActivity.doc.size(); i++) 
				{
					if (LoginActivity.doc.get(i).getDoc_id().equals(list.get(x).getDoc_id()))
					{
						LoginActivity.doc.remove(i);
					}
				}
				Succeed = "删除我的医生成功!";
			}
			return null;
		}
		@Override
		protected void onPostExecute(ReturnObj result) 
		{
			super.onPostExecute(result);
			dialog.dismiss();
			Toast.makeText(context, Succeed, Toast.LENGTH_SHORT).show();
			MyDocAdapter.this.notifyDataSetChanged();
		}
		
	}
}
