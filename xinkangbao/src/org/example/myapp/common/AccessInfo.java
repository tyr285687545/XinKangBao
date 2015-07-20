package org.example.myapp.common;
import org.example.myapp.R;
/**
 * 瀵邦喖宕ョ拋銈堢槈娣団剝浼呯猾浼欑窗OAuth鐠併倛鐦夋潻鏂挎礀閻ㄥ嫭鏆熼幑顕�娉﹂崥锟�
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AccessInfo 
{
	//userId
	private String userID;
	
	//accessToken
	private String accessToken;
	
	//accessSecret
	private String accessSecret;
	
	private long expiresIn;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessSecret() {
		return accessSecret;
	}
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
}