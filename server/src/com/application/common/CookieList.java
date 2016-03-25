/**
 * 
 */
package com.application.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class CookieList {

	private ArrayList<CookieWrapper> cookieArrayList;
	
	//private int ageInSeconds = 60*60*24;
	private int ageInSeconds = 60 * 5;
	
	public CookieList() {
		cookieArrayList = new ArrayList<CookieWrapper>();
	}
	
	public NewCookie allotCookie(String custUserName, int custNo) {
		boolean availableCookie = false;
		CookieWrapper cookieWrapperObj = null;
		NewCookie newCookieObj = null;
		
		/*	
		if (cookieArrayList != null) {
			for(CookieWrapper iterateCookie : cookieArrayList) {
				if(iterateCookie.getLastAccessed())
			}
	
		}		
		*/
		
		newCookieObj = createCookie(custUserName);
		cookieWrapperObj = new CookieWrapper(newCookieObj, custNo);
		cookieWrapperObj.setLastAccessed(Calendar.getInstance().getTime());
		addCookie(cookieWrapperObj);
		
		return newCookieObj;
	}
	
	
	public NewCookie createCookie(String custUserName) {
		// Getting today's date
		Calendar calendarObj = Calendar.getInstance();
		// Getting tomorrow's date
		calendarObj.add(Calendar.SECOND, ageInSeconds);
		// Grabbing the date object
		Date dateObj = calendarObj.getTime();

		// Generating the cookie
		NewCookie newCookieObj = new NewCookie(new Cookie("CookieID",
				custUserName + Constants.counter, "/", ""),
				null, ageInSeconds, dateObj, false, false);
		
		return newCookieObj;
	}

	public void addCookie(CookieWrapper cookieWrapperObj) {
		cookieArrayList.add(cookieWrapperObj);
	}
/*
	public boolean deleteCookie(Cookie cookieObj) {
		Iterator<Entry<NewCookie, Integer>> iteratorObj = cookieMap.entrySet().iterator();
		NewCookie newCookieObj;
		while(iteratorObj.hasNext()) {
			Entry<?, ?> currentEntry = (Entry<?, ?>) iteratorObj.next();
			newCookieObj = (NewCookie) currentEntry.getKey();
			if(newCookieObj.getValue().equalsIgnoreCase(cookieObj.getValue())) {
				cookieMap.remove(newCookieObj);
				return true;
			}			
		}
		return false;
	}
	
	*/
	
	/* Function to find the matching cookie from the array
	 * 
	 * @param cookieObj : cookie received from the client
	 * @return CookieWrapper
	 */
	public CookieWrapper findCookie(Cookie cookieObj) {
		
		Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();
		
		while(iteratorObj.hasNext()) {
			CookieWrapper currentCookie = iteratorObj.next();
			
			// if condition checks if the value of cookie is the same
			if(currentCookie.getNewCookieObj().getValue().equals(cookieObj.getValue())) {
				return currentCookie;
			}
		}
		return null;
	}
	
	public boolean updateCustNo(Cookie cookieObj, int custNo) {
		
		CookieWrapper cookieWrapperObj = findCookie(cookieObj);
		
		if(cookieWrapperObj != null) {
			cookieWrapperObj.setCustNo(custNo);
			return true;
		} else {
			return false;	
		}
	}

	public String displayCookies() {
		return cookieArrayList.toString();
	}
}
