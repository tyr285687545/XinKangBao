package org.example.myapp.client.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class User implements java.io.Serializable{
	
	String operation;
	int id_real;
	public int getId_real() {
		return id_real;
	}
	public void setId_real(int id_real) {
		this.id_real = id_real;
	}



	long id; //也就是手机号
	String password; //密码
	
	String name; //name
	String sex; //sex
	int age; //年龄
	String address; //地址
	String mail; //邮箱；
	String trends;
	String time;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTrends() {
		return trends;
	}
	public void setTrends(String trends) {
		this.trends = trends;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	public int  paser_str(String str) {
		if (str.equals("")) {
			return -1;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONArray ret_arr = (JSONArray)(jsonParser.nextValue());
			JSONObject ret = (JSONObject)ret_arr.get(0);
			this.id_real = Integer.parseInt(ret.getString("id"));
			this.address = ret.getString("address");
			this.password = ret.getString("password");
			this.name = ret.getString("name");
			if (ret.getString("age")==null) 
			{
				this.age = Integer.parseInt(ret.getString("age"));
			}else {
//				this.age = 0;
			}
			this.mail = ret.getString("email");
			this.id = Long.parseLong(ret.getString("tel"));
			this.sex = ret.getString("sex");
		} catch (JSONException  e) {
			Log.d("sky", "e.toString()"+e.toString());
			return -2;
		}
		return 0;
	}
}
