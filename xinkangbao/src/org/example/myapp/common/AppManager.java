package org.example.myapp.common;
import org.example.myapp.R;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 鎼存梻鏁ょ粙瀣碍Activity缁狅紕鎮婄猾浼欑窗閻€劋绨珹ctivity缁狅紕鎮婇崪灞界安閻€劎鈻兼惔蹇涳拷锟介崙锟�
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){}
	/**
	 * 閸楁洑绔寸�圭偘绶�
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * 濞ｈ濮濧ctivity閸掓澘鐖㈤弽锟�
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 閼惧嘲褰囪ぐ鎾冲Activity閿涘牆鐖㈤弽鍫滆厬閺堬拷閸氬簼绔存稉顏勫竾閸忋儳娈戦敍锟�
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 缂佹挻娼ぐ鎾冲Activity閿涘牆鐖㈤弽鍫滆厬閺堬拷閸氬簼绔存稉顏勫竾閸忋儳娈戦敍锟�
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 缂佹挻娼幐鍥х暰閻ㄥ嚈ctivity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 缂佹挻娼幐鍥х暰缁鎮曢惃鍑檆tivity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 缂佹挻娼幍锟介張鍫縞tivity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 闁拷閸戝搫绨查悽銊р柤鎼达拷
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
}