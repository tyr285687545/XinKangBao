package org.example.myapp.client.view;

import java.util.Timer;
import java.util.TimerTask;

import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.MyTime;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BuddyService extends Service {

	Timer timer;
	Handler myHandler = new Handler();
	public static final String BROADCASTACTION = "myapp.BuddyService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId)
	 {	
	    timer = new Timer();
	    
	    timer.schedule(new TimerTask() {
	      @Override
	      public void run() {
	    	  	if (BuddyActivity.new_http_client != null) {
	    		  	BuddyActivity.get_user_doc_msg_toread_read();
		    		String place = "["
							+ Thread.currentThread().getStackTrace()[2]
									.getFileName()
							+ " "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "] ";
		    	
		    		Intent intent = new Intent();
		            intent.setAction( BROADCASTACTION );
		            intent.putExtra( "jsonstr", "");
		            sendBroadcast( intent );
		    		
					//Log.i("wangbo debug",
					//		place + "msg fresh " + " time: " + MyTime.geTime());	
	    	  	}
	      }
	    }, 0, MyAppConfig.UPDATE_MESSAGE_COUNT);

	    return super.onStartCommand( intent, START_STICKY, startId );
	 }
	 @Override
	 public void onDestroy()
	 {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    if(timer != null){
	    	timer.cancel();
	    }
	  }
}