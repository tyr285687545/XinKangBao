package org.example.myapp.client.view;

public class RecentEntity {
	private int avatar;
	private int account;
	private String nick;
	private String content;
	private String time;
	private boolean isRead;

	public RecentEntity(int avatar,int account,String nick,String content,String time,boolean isRead){
		this.avatar=avatar;
		this.account=account;
		this.nick=nick;
		this.content=content;
		this.time=time;
		this.isRead=isRead;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
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

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}
