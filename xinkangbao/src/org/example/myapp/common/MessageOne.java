package org.example.myapp.common;

import android.graphics.Bitmap;

public class MessageOne {

	private Long sender;
	private Long receiver;
	private Bitmap bitmap;
	private int sender_role;
	private int receiver_role;

	private Bitmap photo;
	private String path;
	private String content;
	private String sender_name;
	private String receiver_name;

	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	private String time;
	private boolean isLeft;// 是否为收到的消息，在左边

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

	public int getSender_role() {
		return sender_role;
	}

	public void setSender_role(int sender_role) {
		this.sender_role = sender_role;
	}

	public int getReceiver_role() {
		return receiver_role;
	}

	public void setReceiver_role(int receiver_role) {
		this.receiver_role = receiver_role;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public void paser_str() {

	}

}
