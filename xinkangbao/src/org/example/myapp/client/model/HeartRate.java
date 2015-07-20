package org.example.myapp.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class HeartRate {
	private Date date;
	private float heartrate;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(float heartrate) {
		this.heartrate = heartrate;
	}

	public static HeartRate jiexi(JSONObject oj_tmp) {
		HeartRate ret_obj = new HeartRate();
		try {	
			ret_obj.setDate(sdf.parse(oj_tmp.getString("datetime")));
			ret_obj.setHeartrate(Float.parseFloat(oj_tmp.getString("heartratevalue")));
		} catch (Exception e) {
			ret_obj = null;
		}
		return ret_obj;	
		
	}	
}