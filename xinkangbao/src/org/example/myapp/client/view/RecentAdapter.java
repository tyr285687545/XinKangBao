package org.example.myapp.client.view;
import java.util.List;

import org.example.myapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentAdapter extends BaseAdapter{
	private Context context;
	private List<RecentEntity> list;
	LayoutInflater inflater;
	
	public RecentAdapter(Context context,List<RecentEntity> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup root) {
		convertView = inflater.inflate(R.layout.recent_listview_item, null);
		
		ImageView avatar=(ImageView) convertView.findViewById(R.id.iv_avatar_r);
		TextView nick=(TextView) convertView.findViewById(R.id.tv_nick_r);
		TextView content=(TextView) convertView.findViewById(R.id.tv_chat_content_r);
		ImageView isRead=(ImageView) convertView.findViewById(R.id.iv_tip_mes_r);
		TextView time=(TextView) convertView.findViewById(R.id.tv_time_r);

		RecentEntity re=list.get(position);
		int id=re.getAvatar();
		avatar.setImageResource(ChatActivity.avatar[id]);
		nick.setText(re.getNick());
		content.setText(re.getContent());
		isRead.setImageResource(R.drawable.tips_message);
		time.setText(re.getTime());

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
