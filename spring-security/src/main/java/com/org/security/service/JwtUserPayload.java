package com.org.security.service;

import java.util.Date;

public class JwtUserPayload {
	private String userName;
	private String uniqueId;
	private Date time;
	
	public JwtUserPayload() {
	}
	
	public JwtUserPayload(String userName, String uniqueId, Date time) {
		this.userName = userName;
		this.uniqueId = uniqueId;
		this.time = time;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	

}
