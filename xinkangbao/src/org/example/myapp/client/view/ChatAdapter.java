package org.example.myapp.client.view;

import java.util.List;

import org.example.myapp.R;
import org.example.myapp.common.MessageOne;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	private Context context;
	private List<MessageOne> list;
	LayoutInflater inflater;

	private int[] avatar = new int[] { 0, R.drawable.h001, R.drawable.h002,
			R.drawable.h003, R.drawable.h004, R.drawable.h005, R.drawable.h006 };

	public ChatAdapter(Context context, List<MessageOne> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup root) {

		TextView content;
		TextView time;
		TextView name;
		ImageView iv_pic;
		MessageOne ce = list.get(position);

		if (ce.isLeft()) {
			convertView = inflater.inflate(R.layout.chat_listview_item_left,
					null);
			content = (TextView) convertView
					.findViewById(R.id.message_chat_left);
			time = (TextView) convertView.findViewById(R.id.sendtime_chat_left);
			name = (TextView) convertView.findViewById(R.id.chat_left_name);

			name.setText(ce.getSender_name());
			time.setText(ce.getTime());
			content.setText(ce.getContent());

		} else {
			convertView = inflater.inflate(R.layout.chat_listview_item_right,
					null);

			name = (TextView) convertView.findViewById(R.id.chat_right_name);
			content = (TextView) convertView
					.findViewById(R.id.message_chat_right);
			time = (TextView) convertView
					.findViewById(R.id.sendtime_chat_right);
			iv_pic = (ImageView) convertView.findViewById(R.id.image_chat);
			if (ce.getBitmap() != null) {

				iv_pic.setImageBitmap(ce.getBitmap());
				iv_pic.setVisibility(View.VISIBLE);
				content.setVisibility(View.GONE);
				name.setText(ce.getSender_name());
				time.setText(ce.getTime());
			} else {
				content.setVisibility(View.VISIBLE);
				iv_pic.setVisibility(View.GONE);
				content.setText(ce.getContent());
				name.setText(ce.getSender_name());
				time.setText(ce.getTime());
			}
		}

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
