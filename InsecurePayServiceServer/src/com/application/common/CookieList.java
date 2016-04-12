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

import com.application.service.Logging;

public class CookieList {

	private ArrayList<CookieWrapper> cookieArrayList;

	// Duration of cookie expiry
	//private int ageInSeconds = 24 * 60 * 60 * 1000;
	private int ageInSeconds = 10 * 60 * 1000;

	// Duration of time passed before re-assigning cookie
	//private int cookieAccessReassign = 15 * 60 * 1000;
	private int cookieAccessReassign = 1 * 60 * 1000;
	
	// Duration of minimum time left before re-assigning cookie
	//private int cookieTimeLeftReassign = 12 * 60 * 60 * 1000;
	private int cookieTimeLeftReassign = 5 * 60 * 1000;
	
	public CookieList() {
		cookieArrayList = new ArrayList<CookieWrapper>();

	}

	public NewCookie allotCookie(String custUserName, int custNo) {
		CookieWrapper cookieWrapperObj = null;
		NewCookie newCookieObj = null;
		
		Calendar calendarObj = Calendar.getInstance();
		calendarObj.add(Calendar.MILLISECOND, cookieTimeLeftReassign);
		Date minTimeLeftDateObj = calendarObj.getTime();
		
		Date dateObj = Calendar.getInstance().getTime();

		if (cookieArrayList != null) {
			for (CookieWrapper iterateCookie : cookieArrayList) {
				if (dateObj.getTime() - iterateCookie.getLastAccessed().getTime() >= cookieAccessReassign 
						&& minTimeLeftDateObj.before(iterateCookie.getNewCookieObj().getExpiry())) {
					iterateCookie.setCustNo(custNo);
					iterateCookie.setLastAccessed(dateObj);
					Logging.logger.debug("Reassigning cookie : " + iterateCookie.getNewCookieObj().getValue()
							+ " to CustNo: " + custNo);
					return iterateCookie.getNewCookieObj();
				}
			}
		}
		
		newCookieObj = createCookie(custUserName);
		cookieWrapperObj = new CookieWrapper(newCookieObj, custNo);
		Logging.logger.debug("Creating new cookie: " + newCookieObj.getValue());
		cookieWrapperObj.setLastAccessed(Calendar.getInstance().getTime());
		addCookie(cookieWrapperObj);

		return newCookieObj;
	}

	public NewCookie createCookie(String custUserName) {
		// Getting today's date
		Calendar calendarObj = Calendar.getInstance();
		// Getting tomorrow's date
		calendarObj.add(Calendar.MILLISECOND, ageInSeconds);
		// Grabbing the date object
		Date dateObj = calendarObj.getTime();

		// Generating the cookie
		NewCookie newCookieObj = new NewCookie(new Cookie("CookieID", custUserName + Constants.counter, "/", ""), null,
				ageInSeconds, dateObj, false, false);

		return newCookieObj;
	}

	public void addCookie(CookieWrapper cookieWrapperObj) {
		cookieArrayList.add(cookieWrapperObj);
	}

	public void deleteCookies() {

		Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();
		Date dateObj = Calendar.getInstance().getTime();

		while (iteratorObj.hasNext()) {
			CookieWrapper currentCookie = iteratorObj.next();

			// TODO: Throw this cookie back into assignable pool
			if (dateObj.compareTo(currentCookie.getNewCookieObj().getExpiry()) > 0) {
				Logging.logger.debug("Deleting cookie: " + currentCookie.getNewCookieObj().getValue());
				iteratorObj.remove();
			}
		}
	}

	/*
	 * Function to find the matching cookie from the array
	 * 
	 * @param Cookie : cookie received from the client
	 * 
	 * @return CookieWrapper
	 */
	public CookieWrapper findCookie(Cookie cookieObj) {

		Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();

		while (iteratorObj.hasNext()) {
			CookieWrapper currentCookie = iteratorObj.next();

			// if condition checks if the value of cookie is the same
			if (currentCookie.getNewCookieObj().getValue().equals(cookieObj.getValue())) {
				return currentCookie;
			}
		}
		return null;
	}

	/*
	 * Function to update the corresponding CustNo for the incoming cookie
	 * 
	 * @param Cookie : cookie received from the client
	 */
	public boolean updateCustNo(Cookie cookieObj, int custNo) {

		CookieWrapper cookieWrapperObj = findCookie(cookieObj);

		if (cookieWrapperObj != null) {
			cookieWrapperObj.setCustNo(custNo);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Function to display the arrayList of cookies stored along with 
	 * CustNo and LastAccessed date
	 */
	public String displayCookies() {
		if (cookieArrayList != null) {
			ArrayList<String> cookieString = new ArrayList<String>();
			Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();

			while (iteratorObj.hasNext()) {
				CookieWrapper currentCookie = iteratorObj.next();
				cookieString.add(currentCookie.getNewCookieObj().toString() + " --> CustNo: "
						+ Integer.toString(currentCookie.getCustNo()) + " --> LastAccessed: "
						+ currentCookie.getLastAccessed().toString());
			}
			return cookieString.toString();
		}

		return null;
	}
}
