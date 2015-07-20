package org.example.myapp.client.model;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 评论实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Comment {

	public final static int CLIENT_MOBILE = 2;
	public final static int CLIENT_ANDROID = 3;
	public final static int CLIENT_IPHONE = 4;
	public final static int CLIENT_WINDOWS_PHONE = 5;
	
	
	
	private int id;
	private int topic_id;
	private int uid;
	private String content;
	private String replytime;
	private int role;
	private String username;
	private String avatar;
	private String signature;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReplytime() {
		return replytime;
	}
	public void setReplytime(String replytime) {
		this.replytime = replytime;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}	
		
	public static Comment jiexi(JSONObject oj_tmp) {
		Comment ret_obj = new Comment();
		try {	
			ret_obj.setTopic_id(Integer.parseInt(oj_tmp.getString("topic_id")));
			ret_obj.setId(Integer.parseInt(oj_tmp.getString("id")));
			ret_obj.setUid(Integer.parseInt(oj_tmp.getString("uid")));
		
			/*
			"id": "21",--回复id
"topic_id": "7",--话题id
"uid": "13",--创建者uid
"content": "赵聪回复的！2",--回复内容
"replytime": "1428325693",--回复时间
"role": "0",--没有用
"username": "赵聪",--回复人
"avatar": "uploads/avatar/default/",--没有用
"signature": null--没有用*/
			
			if (!oj_tmp.isNull("content")) {
				ret_obj.setContent(oj_tmp.getString("content"));
			} else {
				ret_obj.setContent("");
			}
			
			ret_obj.setReplytime(oj_tmp.getString("replytime"));
	
			//todo
			ret_obj.setUsername(oj_tmp.getString("username"));
			
		} catch (Exception e) {
			ret_obj = null;
		}
		return ret_obj;	
	}
	public static Comment jiexi_by_ret(String tmp) {
	
		int pos = tmp.indexOf("<div style");
		if (pos != -1) {
			tmp = tmp.substring(0, pos);
		}

		JSONTokener jsonParser = new JSONTokener(tmp);
		try {
			JSONObject ret = (JSONObject)(jsonParser.nextValue());
			JSONObject post_json_obj = ret.getJSONObject("content");
			Comment ret_obj = Comment.jiexi(post_json_obj);
			return ret_obj;	
		} catch (Exception e) {
			return null;
		}
	}
	
	/*private List<Reply> replies = new ArrayList<Reply>();
	private List<Refer> refers = new ArrayList<Refer>();
	
	public static class Reply implements Serializable{
		public String rauthor;
		public String rpubDate;
		public String rcontent;
	} 
	
	public static class Refer implements Serializable{
		public String refertitle;
		public String referbody;
	}*/

}
