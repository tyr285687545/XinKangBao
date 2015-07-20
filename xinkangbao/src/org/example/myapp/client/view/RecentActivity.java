package org.example.myapp.client.view;
import org.example.myapp.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RecentActivity extends Activity{
	ListView listView;
	List<RecentEntity> chatEntityList=new ArrayList<RecentEntity>();
	String[] mes;
	MyBroadcastReceiver br;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recent);
		 //注册广播
		IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
        
	    listView = (ListView) findViewById(R.id.lv_recent);
        listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				int avatar=Integer.parseInt(mes[4]);
				int account=Integer.parseInt(mes[2]);
				String nick=mes[3];
				if(mes[7].equals("todo")){
					 avatar=7;
					 account=Integer.parseInt(mes[0]);
					 nick=mes[1];
				}
				//打开聊天页面
				Intent intent=new Intent(RecentActivity.this,ChatActivity.class);
				intent.putExtra("avatar", avatar);
				intent.putExtra("account", account);
				intent.putExtra("nick", nick);
				startActivity(intent);
			}
        });
        
        findViewById(R.id.recent_del).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new Builder(RecentActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确认清除所有会话吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						chatEntityList.clear();
						listView.setAdapter(null);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}

	@Override
	public void finish() {
		 unregisterReceiver(br);
		super.finish();
	}

	//广播接收器
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mes = intent.getStringArrayExtra("message");
			//如果是群消息
		    if(mes[7].equals("group_mes")){
			    Iterator it=chatEntityList.iterator();
			    if(chatEntityList!=null && chatEntityList.size()!=0){
			    	while(it.hasNext()){
			    		RecentEntity re=(RecentEntity) it.next();
			    		if(re.getAccount()==Integer.parseInt(mes[0])){
			    			chatEntityList.remove(re);
			    		}
			    	}
			    }
		    	
		    	chatEntityList.add(new RecentEntity(
		    			7, 
			    		Integer.parseInt(mes[0]), 
			    		mes[1]+"", 
			    		mes[5],
			    		mes[6], 
			    		false));
			    listView.setAdapter(new RecentAdapter(RecentActivity.this, chatEntityList));
			    Toast.makeText(context,mes[1]+"： "+mes[5], Toast.LENGTH_SHORT).show();
		    }else{
		    	//更新最近会话列表， 检测chatEntityList，防止同一个好友的消息出现多个会话实体
			    Iterator it=chatEntityList.iterator();
			    if(chatEntityList!=null && chatEntityList.size()!=0){
			    	while(it.hasNext()){
			    		RecentEntity re=(RecentEntity) it.next();
			    		if(re.getAccount()==Integer.parseInt(mes[2])){
			    			chatEntityList.remove(re);
			    		}
			    	}
			    }
			    chatEntityList.add(new RecentEntity(
			    		Integer.parseInt(mes[4]), 
			    		Integer.parseInt(mes[2]), 
			    		mes[3]+"", 
			    		mes[5],
			    		mes[6], 
			    		false));
			    listView.setAdapter(new RecentAdapter(RecentActivity.this, chatEntityList));
			    Toast.makeText(context,mes[3]+"： "+mes[5], Toast.LENGTH_SHORT).show();
		    }
		}
	}
}
