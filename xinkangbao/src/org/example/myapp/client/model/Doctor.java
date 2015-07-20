package org.example.myapp.client.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.graphics.Bitmap;
import android.util.Log;

public class Doctor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String passwd;
	private String sex; // sex
	private Long doc_id;
	private int age; // ÄêÁä
	private String hospital; // Ò½Ôº
	private String department; // ¿ÆÊÒ
	private String job; // Ö°Îñ
	private String major; // Ö÷ÖÎ
	private String mail; // ÓÊÏä£»
	private int isOnline;
	private int mes_to_read; // Î´¶ÁÏûÏ¢Ê÷
	private String person;
	private Bitmap bitmap;
	private String thumb_url;
	
	private String img_url;
	
	private int unreadcount;

	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUnreadcount() {
		return unreadcount;
	}

	public void setUnreadcount(int unreadcount) {
		this.unreadcount = unreadcount;
	}

	public int getMes_to_read() {
		return mes_to_read;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public void setMes_to_read(int mes_to_read) {
		this.mes_to_read = mes_to_read;
	}

	public Doctor() {
		mes_to_read = 0;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
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

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public Long getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(Long doc_id) {
		this.doc_id = doc_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public static Doctor parse_json_to_obj(JSONObject ret) {
		final Doctor doc_ret = new Doctor();
		try {
			// doc_ret.passwd = ret.getString("password");
			doc_ret.name = ret.getString("name");
			doc_ret.age = Integer.parseInt(ret.getString("age"));
			doc_ret.mail = ret.getString("email");
			doc_ret.doc_id = Long.parseLong(ret.getString("tel"));
			doc_ret.sex = ret.getString("sex");
			doc_ret.hospital = ret.getString("hospital");
			doc_ret.department = ret.getString("department");
			doc_ret.job = ret.getString("job");
			doc_ret.img_url = ret.getString("src_url");
			doc_ret.thumb_url = ret.getString("thumb_url");
			doc_ret.person = ret.getString("summary");
//			if (!StringUtils.isEmpty(doc_ret.thumb_url)) 
//			{
//				new Runnable() 
//				{
//					public void run()
//					{
//						doc_ret.setBitmap(GetBitmapFromInternet.getBitmapFromUrl("http://"+doc_ret.thumb_url));
//					}
//				}.run();;
//			}
			if (ret.isNull("isonline")) {
				doc_ret.isOnline = 0;
			} else {
				doc_ret.isOnline = Integer.parseInt(ret.getString("isonline"));
			}
			
			doc_ret.major = ret.getString("attending");

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("sky", e.toString());
			return null;
		}
		return doc_ret;
	}

	public static Doctor paser_str_to_obj(String str) {
		if (str.equals("")) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONArray ret_arr = (JSONArray) (jsonParser.nextValue());
			JSONObject ret = (JSONObject) ret_arr.get(0);
			return parse_json_to_obj(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Doctor> paser_str_to_objlist(String str) {

		List<Doctor> buddyEntityList = new ArrayList<Doctor>();
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			for (int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				Doctor doc_tmp = parse_json_to_obj(oj_tmp);
				if (doc_tmp != null) {
					buddyEntityList.add(doc_tmp);
				}
			}
		} catch (JSONException e) {
			buddyEntityList.clear();
		}
		return buddyEntityList;
	}
}
