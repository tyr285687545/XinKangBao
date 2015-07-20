package org.example.myapp.client.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class Order implements java.io.Serializable{
	
	private Long patTel;
	private Long docTel;
	private String docName;
	private String patName;
	private int patientid;
	private int doctorid;
	
	private int status;
	private String appTime;
	
	private int orderId;
	

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public Long getPatTel() {
		return patTel;
	}


	public void setPatTel(Long patTel) {
		this.patTel = patTel;
	}


	public Long getDocTel() {
		return docTel;
	}


	public void setDocTel(Long docTel) {
		this.docTel = docTel;
	}


	public String getDocName() {
		return docName;
	}


	public void setDocName(String docName) {
		this.docName = docName;
	}


	public String getPatName() {
		return patName;
	}


	public void setPatName(String patName) {
		this.patName = patName;
	}


	public int getPatientid() {
		return patientid;
	}


	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}


	public int getDoctorid() {
		return doctorid;
	}


	public void setDoctorid(int doctorid) {
		this.doctorid = doctorid;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getAppTime() {
		return appTime;
	}


	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}

}
