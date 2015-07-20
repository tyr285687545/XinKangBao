package org.example.myapp.common;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class ReturnObj {

	private String msg;
	private int ret_code;
	private String org_str;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRet_code() {
		return ret_code;
	}

	public void setRet_code(int ret_code) {
		this.ret_code = ret_code;
	}

	public String getOrg_str() {
		return org_str;
	}

	public void setOrg_str(String org_str) {
		this.org_str = org_str;
	}

	public void paser_return_code(String str) {
		this.org_str = str;
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			this.ret_code = Integer.parseInt(ret.getString("status"));
			this.msg = ret.getString("msg");
		} catch (JSONException e) {
			this.ret_code = -1;
			this.msg = e.getMessage();
		}
	}
}
