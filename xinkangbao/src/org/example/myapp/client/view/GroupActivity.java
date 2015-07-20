package org.example.myapp.client.view;
import org.example.myapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GroupActivity extends Activity{
	public static String groupStr="";
	ListView listView;
	private List<Map<String,String>> data = new ArrayList<Map<String,String>>();  
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		
		jieXi();
		listView = (ListView) findViewById(R.id.lv_group);  
        SimpleAdapter adapter=new SimpleAdapter(  
                this,  
                data,
                R.layout.group_listview_item,
                new String[]{"nick","info"},
                new int[]{R.id.nick_group,R.id.info_group});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int p,
					long arg3) {
				Map<String, String> temp = (Map<String, String>) listView.getItemAtPosition(p);
				//打开聊天页面
				Intent intent=new Intent(GroupActivity.this,ChatActivity.class);
				intent.putExtra("avatar", 7);
				intent.putExtra("account",Integer.parseInt(temp.get("account")));
				intent.putExtra("nick", temp.get("nick"));
				startActivity(intent);
			}
        });
	}
	
	public void jieXi(){
		String ss[] = groupStr.split(" ");
		 for(String a: ss){
	        	if(a!=""){
		        	String b[]=a.split("_");
		        	  Map<String, String> map = new HashMap<String, String>();
		        	  map.put("account", b[0]);
		              map.put("nick",b[1]);
		              map.put("info",b[2]);  
		              data.add(map);  
	        	}
	        }
	}
}
