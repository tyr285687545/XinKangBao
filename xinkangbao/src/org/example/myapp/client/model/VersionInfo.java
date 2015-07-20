package org.example.myapp.client.model;

public class VersionInfo {

	private String versionCode;

	private String versionName;

	private String downloadUrl;

	private String updateLog;

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String string) {
		this.versionCode = string;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

}
