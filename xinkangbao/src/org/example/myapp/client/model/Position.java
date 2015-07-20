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
					Log.d("sky", "服务器返回的lat：" + lat);
					Log.d("sky", "服务器返回的lon：" + lon);
					/**
					 * 超哥提供的公式
					double _lon = lon/100.0;
					_lon = ((int)_lon) + (_lon - ((int)_lon))*100.0/60.0;
					
					double _lat = lat/100.0;
					_lat = ((int)_lat) + (_lat - ((int)_lat))*100.0/60.0;
					Log.d("sky", "调用公式后的lon：" + _lat);
					Log.d("sky", "调用公式后的lat：" + _lat);
					 * */
					/**
					 * 获取分数的小数部分
					 * */
					double lat_second = first(lat);
					double lon_second = first(lon);
					double x = second(lat);
					double y = second(lon);
					double final_lat = ((x+lat_second)/60)+(int)(lat/100);
					double final_lon = ((y+lon_second)/60)+(int)(lon/100);
					Log.e("sky", "lat转换后坐标： = "+final_lat);
					Log.e("sky", "lon转换后坐标： = "+final_lon);
					/**
					 * 调用百度接口进行坐标转换
					 * "http://api.map.baidu.com/geoconv/v1/?coords=26.104444, 119.299250&from=3&to=5&ak=Mk0gw9sjtR2m0zx1wkmHMPgU"
					 * 参数说明见：“http://developer.baidu.com/map/changeposition.htm”
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
	 * 获取小数部分
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
	 * 将一个纬度为37°25′19.222″的六十进制转换为十进制的步骤为：
	 * 第一步:19.222/60=0.3203666666666667,0.3203666666666667为分数(′)的小数部分，
	 * 
	 * 第二步:25+0.3203666666666667=25.3203666666666667，25.3203666666666667分数(′)
	 * 
	 *  第三步:25.3203666666666667/60=0.4220061111111111，
	 * 0.4220061111111111为度数(°)的小数部分
	 * 37°25′19.222″转换的最终结果为37+0.4220061111111111=37.4220061111111111
	 * */
	private static double first(double db)
	{
		double _one = getdPoint(db);//首先获取分数的小数部分 
		_one = _one*100; //
		return _one/60;//19.222/60
	}
	
	//获取分数的整数部分
	private static double  second(double db)
	{
		int _db =(int)db;
		double after_db = _db/100;
		double broad = _db-(after_db*100) ;
		return broad;
	}
}
