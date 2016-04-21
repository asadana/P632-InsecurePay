package com.cigital.common;

import java.util.Date;

import javax.ws.rs.core.NewCookie;

public class CookieWrapper {

	private NewCookie newCookieObj;
	private int custNo;
	private Date lastAccessed;
	
	public CookieWrapper(NewCookie newCookieObj, int custNo) {
		this.newCookieObj = newCookieObj;
		this.custNo = custNo;
		this.lastAccessed = null;
	}
	
	public NewCookie getNewCookieObj() {
		return newCookieObj;
	}
	public void setNewCookieObj(NewCookie newCookieObj) {
		this.newCookieObj = newCookieObj;
	}
	public Date getLastAccessed() {
		return lastAccessed;
	}
	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}
	public int getCustNo() {
		return custNo;
	}
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}
}
