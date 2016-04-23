package com.cigital.insecurepay.common;

import java.util.Date;

import javax.ws.rs.core.NewCookie;

/**
 * CookieWrapper is a wrapper class that contains a {@link NewCookie}, 
 * customer number and the last date cookie was accessed. 
 */
public class CookieWrapper {

	private NewCookie newCookieObj;
	private int custNo;
	private Date lastAccessed;
	
	/**
	 * CookieWrapper is a parameterized constructor for CookieWrapper.
	 * 
	 *  @param	newCookieObj	Contains the {@link NewCookie} object.
	 *  @param	custNo			Contains the customer number.
	 */
	public CookieWrapper(NewCookie newCookieObj, int custNo) {
		this.setNewCookieObj(newCookieObj);
		this.setCustNo(custNo);
		this.setLastAccessed(null);
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
