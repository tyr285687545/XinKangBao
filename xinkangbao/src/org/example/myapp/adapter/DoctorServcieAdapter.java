package org.example.myapp.adapter;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.view.BuddyActivity;
import org.example.myapp.client.view.ChatActivity;
import org.example.myapp.client.view.DoctorDetail;
import org.example.myapp.client.view.DoctorOrderActivity;
import org.example.myapp.client.view.LoginActivity;
import org.example.myapp.client.view.MainActivity;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorServcieAdapter extends BaseAdapter {
	private Context context;
	private List<Doctor> list;
	LayoutInflater inflater;
	private String error_msg;
	private String succeed_msg;
	private ProgressDialog dialog;
	protected ImageLoader imageLoader = ImageLoader.getInstance();  
	
	public DoctorServcieAdapter(Context context, List<Doctor> list) {
		this.context = context;
		this.list = list;
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		inflater = LayoutInflater.from(context);
	}

	public View getView(final int position, View convertView, ViewGroup root) {
		convertView = inflater.inflate(R.layout.item_doc_service, null);
		ImageView iv_ask = (ImageView) convertView
				.findViewById(R.id.item_service_iv_ask);
		ImageView iv_commend = (ImageView) convertView
				.findViewById(R.id.item_service__iv_commend);
		ImageView iv_add = (ImageView) convertView
				.findViewById(R.id.item_service__iv_add);
		ImageView head =  (ImageView)convertView.findViewById(R.id.item_service_image_head);
		if (!StringUtils.isEmpty(list.get(position).getThumb_url()))
		{
			ImageLoader.getInstance().displayImage("http://"+list.get(position).getThumb_url(), head);
		}
		iv_ask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("tel", list.get(position).getDoc_id());
				intent.putExtra("name", list.get(position).getName());
				intent.putExtra("isinline", list.get(position).getIsOnline());
				intent.putExtra("major", list.get(position).getMajor());
				intent.putExtra("person", list.get(position).getPerson());
				context.startActivity(intent);
			}
		});
		iv_commend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DoctorOrderActivity.class);
				intent.putExtra("tel", list.get(position).getDoc_id());
				intent.putExtra("name", list.get(position).getName());
				context.startActivity(intent);
			}
		});
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AsyncTask task = new AsyncTask() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						dialog = new ProgressDialog(context);
						dialog.setMessage("正在添加,请稍等");
						dialog.setCancelable(false);
						dialog.show();
					}

					@Override
					protected Object doInBackground(Object... params) {
						try {
							Long doc_id = list.get(position).getDoc_id();
							Long user_id = Long.valueOf(LoginActivity.mySharedPreferences
									.getString("current_login_tel", ""));

							ReturnObj obj = MainActivity.client_in_strict_mode
									.add_user_doc(user_id, doc_id);
							if (obj.getRet_code() == 0) 
							{
								succeed_msg = obj.getMsg();
								Doctor doc_tmp = Doctor
										.paser_str_to_obj((MainActivity.client_in_strict_mode
												.get_doc_info(doc_id)));
								LoginActivity.doc.add(doc_tmp);
								BuddyActivity.buddyEntityList.add(doc_tmp);
							}else
							{
								error_msg = obj.getMsg();
							}
						} catch (Exception e) 
						{
							error_msg = e.toString();

						}
						ReturnObj ret_obj = LoginActivity.new_http_client
								.get_user_doc_list(MainActivity.myUser.getId());
						if (ret_obj != null && ret_obj.getRet_code() == 0) {
							String buddyStr = ret_obj.getOrg_str();
							LoginActivity.doc = Doctor
									.paser_str_to_objlist(buddyStr);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Object result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						if (error_msg != null) {
							Toast.makeText(context, error_msg,
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							error_msg = null;
						}
						if (succeed_msg != null) {
							Toast.makeText(context, succeed_msg,
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							succeed_msg = null;
						}
						dialog.dismiss();
					}
				};
				task.execute("");
			}
		});
		if (position % 2 == 1) {
			convertView.setBackgroundColor(Color.WHITE);
		} else {
			convertView.setBackgroundColor(Color.parseColor("#FCFCFC"));
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.item_service_tv_name);
		TextView major = (TextView) convertView
				.findViewById(R.id.item_service_tv_indications);
		TextView online = (TextView) convertView
				.findViewById(R.id.item_service_tv_hospital);
		TextView weidu = (TextView) convertView
				.findViewById(R.id.item_service_tv_post);

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
