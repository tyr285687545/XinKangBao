package org.example.myapp.client.model;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.example.myapp.client.model.Doctor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * ÐÄÌøÐÅÏ¢
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class HeartRateList {

	private List<HeartRate> heartRateList = new ArrayList<HeartRate>();

	public List<HeartRate> getHeartRateList() {
		return heartRateList;
	}

	public void setHeartRateList(List<HeartRate> heartRateList) {
		this.heartRateList = heartRateList;
	}

	
	public static HeartRateList parse(String str) {
		HeartRateList postlist = new HeartRateList();
		HeartRate heartRate = null;
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject)(jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			for(int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				heartRate = HeartRate.jiexi(oj_tmp);

				if (heartRate != null) {
					postlist.getHeartRateList().add(heartRate);
				}			
			}
		} catch (JSONException  e) {
			postlist.getHeartRateList().clear();
		}
		return postlist;
	}
	
	
} 