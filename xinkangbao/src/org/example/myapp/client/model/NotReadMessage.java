package org.example.myapp.client.model;

import java.util.ArrayList;
import java.util.List;

public class NotReadMessage {

	private String unreadcnt;
	private String sendertel;
	private boolean isRefresh;
	
	private List<NotReadMessage> NotReadMessage_list = new ArrayList<NotReadMessage>();
	public String getUnreadcnt() {
		return unreadcnt;
	}

	public void setUnreadcnt(String unreadcnt) {
		this.unreadcnt = unreadcnt;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public String getSendertel() {
		return sendertel;
	}

	public void setSendertel(String sendertel) {
		this.sendertel = sendertel;
	}

	public List<NotReadMessage> getNotReadMessage_list() {
		return NotReadMessage_list;
	}

	public void setNotReadMessage_list(List<NotReadMessage> notReadMessage_list) {
		NotReadMessage_list = notReadMessage_list;
	}

}
