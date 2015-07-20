/**
 * 管理所有的Activity以便完全退出程序和得到Activity的实例
 */
package org.example.myapp.client.view;
import java.util.HashMap;

import android.app.Activity;

public class ManageActivity {
	private static HashMap allActiviy=new HashMap<String,Activity>();

	public static void addActiviy(String name,Activity activity){
		allActiviy.put(name, activity);
	}
	
	public static Activity getActivity(String name){
		return (Activity)allActiviy.get(name);
	}
}
