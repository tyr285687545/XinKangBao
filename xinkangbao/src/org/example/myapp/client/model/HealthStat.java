package org.example.myapp.client.model;


import java.text.DecimalFormat;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

/**
 * 帖子实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class HealthStat {

	private double mean;
	private double sdnn;
	private double sdann;
	private double sdnni;
	private double r_mssd;
	
	
	
	
	public double getMean() {
		return mean;
	}
	

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getSdnn() {
		return sdnn;
	}


	public void setSdnn(double sdnn) {
		this.sdnn = sdnn;
	}

	public double getSdann() {
		return sdann;
	}

	public void setSdann(double sdann) {
		this.sdann = sdann;
	}

	public double getSdnni() {
		return sdnni;
	}

	public void setSdnni(double sdnni) {
		this.sdnni = sdnni;
	}

	public double getR_mssd() {
		return r_mssd;
	}

	public void setR_mssd(double r_mssd) {
		this.r_mssd = r_mssd;
	}

	public static HealthStat jiexi(JSONObject oj_tmp) {
		
		HealthStat ret_obj = new HealthStat();
		try {	
			ret_obj.setMean(Double.parseDouble(oj_tmp.getString("MEAN")));
			ret_obj.setSdnn(Double.parseDouble(oj_tmp.getString("SDNN")));
			ret_obj.setSdann(Double.parseDouble(oj_tmp.getString("SDANN")));
			ret_obj.setSdnni(Double.parseDouble(oj_tmp.getString("SDNNI")));
			ret_obj.setR_mssd(Double.parseDouble(oj_tmp.getString("R_MSSD")));
		} catch (Exception e) {
			ret_obj = null;
		}
		return ret_obj;	
	}
	
	public static HealthStat jiexi_by_str(String detail) {
		int pos = detail.indexOf("<div style");
		if (pos != -1) {
			detail = detail.substring(0, pos);
		}

		JSONTokener jsonParser = new JSONTokener(detail);
		try {
			JSONObject ret = (JSONObject)(jsonParser.nextValue());
			JSONObject post_json_obj = ret.getJSONObject("list");
			HealthStat ret_obj = HealthStat.jiexi(post_json_obj);
			return ret_obj;	
		} catch (Exception e) {
			return null;
		}
	}
}
