package org.example.myapp.client.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class Position {
	private static double lat;
	private static double lon;
	private static String url;
	private static String result;
	double l1;
	double l2;

	public double getL1() {
		return l1;
	}

	public void setL1(double l1) {
		this.l1 = l1;
	}

	public double getL2() {
		return l2;
	}

	public void setL2(double l2) {
		this.l2 = l2;
	}

	public static Position paser_str(String str) {
		JSONTokener jsonParser = new JSONTokener(str);
		try {
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONObject post_json_obj = ret.getJSONObject("content");
			Position pos = new Position();
			lat = (Double.parseDouble(post_json_obj.getString("lat")));
			lon = (Double.parseDouble(post_json_obj.getString("lon")));

			new Runnable() {
				public void run() {
					Log.d("sky", "���������ص�lat��" + lat);
					Log.d("sky", "���������ص�lon��" + lon);
					/**
					 * �����ṩ�Ĺ�ʽ
					double _lon = lon/100.0;
					_lon = ((int)_lon) + (_lon - ((int)_lon))*100.0/60.0;
					
					double _lat = lat/100.0;
					_lat = ((int)_lat) + (_lat - ((int)_lat))*100.0/60.0;
					Log.d("sky", "���ù�ʽ���lon��" + _lat);
					Log.d("sky", "���ù�ʽ���lat��" + _lat);
					 * */
					/**
					 * ��ȡ������С������
					 * */
					double lat_second = first(lat);
					double lon_second = first(lon);
					double x = second(lat);
					double y = second(lon);
					double final_lat = ((x+lat_second)/60)+(int)(lat/100);
					double final_lon = ((y+lon_second)/60)+(int)(lon/100);
					Log.e("sky", "latת�������꣺ = "+final_lat);
					Log.e("sky", "lonת�������꣺ = "+final_lon);
					/**
					 * ���ðٶȽӿڽ�������ת��
					 * "http://api.map.baidu.com/geoconv/v1/?coords=26.104444, 119.299250&from=3&to=5&ak=Mk0gw9sjtR2m0zx1wkmHMPgU"
					 * ����˵��������http://developer.baidu.com/map/changeposition.htm��
					 * */
					url = "http://api.map.baidu.com/geoconv/v1/?coords="
							+ final_lon + "," + final_lat
							+ "&from=3&to=5&ak=Mk0gw9sjtR2m0zx1wkmHMPgU";
					// url = url.replaceAll(" ", "%20");
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpClientParams.setCookiePolicy(
								httpClient.getParams(),
								CookiePolicy.BROWSER_COMPATIBILITY);
						HttpGet httpGet = new HttpGet(url);
						HttpResponse httpResponse = httpClient.execute(httpGet);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							result = EntityUtils.toString(httpResponse
									.getEntity());
						}
					} catch (Exception e) {
					}
				}
			}.run();
			JSONObject jsonResult = new JSONObject(result);
			JSONArray array = jsonResult.getJSONArray("result");
			JSONObject getLonLat = array.getJSONObject(0);
			String getLat = getLonLat.getString("x");
			String getLon = getLonLat.getString("y");
			Log.d("sky", "getLat"+getLat);
			Log.d("sky", "getLon"+getLon);
			pos.setL1(Double.valueOf(getLon));
			pos.setL2(Double.valueOf(getLat));
			return pos;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ��ȡС������
	 * */ 
	public static double getdPoint(double num) {
		int _num = (int) num;
		return num - _num;
	}

	public static String get_device_id(String str) {
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			JSONObject oj_tmp = doc_list.getJSONObject(0);
			String ss = oj_tmp.getString("imeiid");
			return ss;
		} catch (JSONException e) {
			return "";
		}
	}

	/**
	 * 
	 * ��һ��γ��Ϊ37��25��19.222�����ʮ����ת��Ϊʮ���ƵĲ���Ϊ��
	 * ��һ��:19.222/60=0.3203666666666667,0.3203666666666667Ϊ����(��)��С�����֣�
	 * 
	 * �ڶ���:25+0.3203666666666667=25.3203666666666667��25.3203666666666667����(��)
	 * 
	 *  ������:25.3203666666666667/60=0.4220061111111111��
	 * 0.4220061111111111Ϊ����(��)��С������
	 * 37��25��19.222��ת�������ս��Ϊ37+0.4220061111111111=37.4220061111111111
	 * */
	private static double first(double db)
	{
		double _one = getdPoint(db);//���Ȼ�ȡ������С������ 
		_one = _one*100; //
		return _one/60;//19.222/60
	}
	
	//��ȡ��������������
	private static double  second(double db)
	{
		int _db =(int)db;
		double after_db = _db/100;
		double broad = _db-(after_db*100) ;
		return broad;
	}
}
