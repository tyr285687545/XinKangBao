package org.example.myapp.client.view;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.Order;
import org.example.myapp.common.ReturnObj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserOrderAdapter extends BaseAdapter {
	private Context mContext;
	private List<Order> list;

	public UserOrderAdapter(Context context, List<Order> list) {
		this.mContext = context;
		this.list = list;
	}

	class OrderViewHolder {

		TextView doc_name_textView;
		TextView doc_tel_textView;
		TextView doc_apptime_textView;
		ImageButton cal_order_btn;
	}

	public View getView(int position, View convertView, ViewGroup root) {

		OrderViewHolder holder = null;
		OnClick listener = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.order_listview_item,
					null);
			holder = new OrderViewHolder();
			holder.doc_name_textView = (TextView) convertView
					.findViewById(R.id.doc_name);
			holder.doc_tel_textView = (TextView) convertView
					.findViewById(R.id.doc_tel);
			holder.doc_apptime_textView = (TextView) convertView
					.findViewById(R.id.doc_apptime);
			holder.cal_order_btn = (ImageButton) convertView
					.findViewById(R.id.cal_order);

			listener = new OnClick();
			holder.cal_order_btn.setOnClickListener(listener);
			convertView.setTag(holder);
			convertView.setTag(holder.cal_order_btn.getId(), listener);
		} else {
			holder = (OrderViewHolder) convertView.getTag();
			listener = (OnClick) convertView.getTag(holder.cal_order_btn
					.getId());
		}
		Order be = list.get(position);
		holder.doc_name_textView.setText(be.getDocName());
		holder.doc_tel_textView.setText(Long.toString(be.getDocTel()));
		holder.doc_apptime_textView.setText(be.getAppTime());
		listener.setPosition(position);

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

	class OnClick implements OnClickListener {

		private int position;

		public void setPosition(int position) {
			this.position = position;
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			String msg = list.get(position).getDocName() + "@" + list.get(position).getAppTime();
			builder.setTitle("确定取消"+msg+"的订单么？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							int order_id = list.get(position).getOrderId();
							ReturnObj ret_obj = MainActivity.client_in_strict_mode
									.del_user_order(order_id);
							Log.i("wangbo debug",
									"jiexi: " + ret_obj.getOrg_str());
							if (ret_obj.getRet_code() == 0) {
								Toast.makeText(mContext,
										"取消订单成功! " + ret_obj.getMsg(),
										Toast.LENGTH_SHORT).show();
								list.remove(position);
								notifyDataSetChanged();
							} else {
								Toast.makeText(mContext,
										"取消订单失败! " + ret_obj.getMsg(),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							;
						}
					});
			builder.create().show();
		}
	}
}
