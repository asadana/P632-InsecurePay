package com.cigital.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

import com.cigital.service.Logging;

/**
 * CookieList is class that handles everything related to cookies.
 */
public class CookieList {

	// An ArrayList of CookieWrapper class
	private ArrayList<CookieWrapper> cookieArrayList;

	// Duration of cookie expiry
	private int cookieAgeInSeconds = 24 * 60 * 60 * 1000;

	// Duration of time passed before re-assigning cookie
	private int cookieAccessReassign = 15 * 60 * 1000;
	
	// Duration of minimum time left before re-assigning cookie
	private int cookieTimeLeftReassign = 12 * 60 * 60 * 1000;
	
	/**
	 * CookieList is the default constructor of this class.
	 */
	public CookieList() {
		cookieArrayList = new ArrayList<CookieWrapper>();

	}

	/**
	 * allotCookie is a function that is called when a cookie needs to be
	 * assigned to a user. It checks if a cookie exists that can be reassigned,
	 * otherwise a new cookie is created.
	 * 
	 * @param	custUserName 	Contains the customer's username.
	 * @param	custNo			Contains the customer number.
	 * 
	 * @return {@link NewCookie} 	An existing cookie or a new cookie is returned
	 * 								as a Newcookie object.
	 */
	public NewCookie allotCookie(String custUserName, int custNo) {
		CookieWrapper cookieWrapperObj = null;
		NewCookie newCookieObj = null;
		
		// Get the current calendar instance
		Calendar calendarObj = Calendar.getInstance();
		// Adding minimum time left to reassign
		calendarObj.add(Calendar.MILLISECOND, cookieTimeLeftReassign);
		Date minTimeLeftDateObj = calendarObj.getTime();
		
		// Storing current date and time instance
		Date dateObj = Calendar.getInstance().getTime();

		// If condition checks if the cookieArrayList has cookies in it
		if (cookieArrayList != null) {
			// Traversing the cookieArrayList
			for (CookieWrapper iterateCookie : cookieArrayList) {
				
				// If condition checks if the cookie has not expired yet
				// and if the cookie is valid for more than cookieAccessReassign
				if (minTimeLeftDateObj.before(iterateCookie.getNewCookieObj().getExpiry()) 
						&& (dateObj.getTime() - iterateCookie.getLastAccessed().getTime() 
								>= cookieAccessReassign)) {
					
					// Reassigning the cookie to the current user
					iterateCookie.setCustNo(custNo);
					iterateCookie.setLastAccessed(dateObj);
					
					Logging.logger.debug("allotCookie: Reassigning cookie : " 
							+ iterateCookie.getNewCookieObj().getValue()
							+ " to CustNo: " + custNo);
					
					// Return the cookie
					return iterateCookie.getNewCookieObj();
				}
			}
		}
		
		// If the above if condition is not met, a new cookie is created
		newCookieObj = createCookie(custUserName);
		
		Logging.logger.debug("allotCookie: Creating new cookie: " 
								+ newCookieObj.getValue());
		
		// Mapping the cookie created to the customer number
		cookieWrapperObj = new CookieWrapper(newCookieObj, custNo);
		cookieWrapperObj.setLastAccessed(Calendar.getInstance().getTime());
		
		// Adding cookie to the cookieArrayList
		addCookie(cookieWrapperObj);

		return newCookieObj;
	}

	/**
	 * createCookie is a function that is called to generate a NewCookie for the user.
	 * 
	 * @param	custUserName		Contains the customer's username.
	 * 
	 * @return	{@link NewCookie}	Return the NewCookie object created.
	 */
	public NewCookie createCookie(String custUserName) {
		// Getting today's date
		Calendar calendarObj = Calendar.getInstance();
		// Adding cookieAgeInSeconds as cookie's life from current time
		calendarObj.add(Calendar.MILLISECOND, cookieAgeInSeconds);
		// Grabbing the date object
		Date dateObj = calendarObj.getTime();

		// Generating the cookie
		NewCookie newCookieObj = new NewCookie(
									new Cookie("CookieID", 
												custUserName + Constants.counter, "/", ""), 
									null, cookieAgeInSeconds, dateObj, false, false);

		// Return the generated NewCookie object
		return newCookieObj;
	}

	/**
	 * addCookie is a function that is called to add CookieWrapper object to
	 * the cookieArrayList.
	 * 
	 * @param	cookieWrapperObj	Contains the CookieWrapper object that contains the cookie.
	 */
	public void addCookie(CookieWrapper cookieWrapperObj) {
		cookieArrayList.add(cookieWrapperObj);
	}

	/**
	 * deleteCookies is a function that is called when expired cookies need to be deleted.
	 */
	public void deleteExpiredCookies() {
		
		// An iterator to traverse the cookieArrayList
		Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();
		// Get the current date and time
		Date dateObj = Calendar.getInstance().getTime();

		// Iterating the cookieArrayList
		while (iteratorObj.hasNext()) {
			// Traversing to the next object 
			CookieWrapper currentCookie = iteratorObj.next();

			// If condition checks if the current date is after the cookie's expiry
			if (dateObj.after(currentCookie.getNewCookieObj().getExpiry())) {
				Logging.logger.debug("deleteExpiredCookies: : " 
										+ currentCookie.getNewCookieObj().getValue());
				// Deleting cookie object
				iteratorObj.remove();
			}
		}
	}

	/**
	 * findCookie is a function used to find the matching cookie from the cookieArrayList.
	 * 
	 * @param	cookieObj		Contains the cookie received from the client.
	 * 
	 * @return	CookieWrapper	Return the CookieWrapper object that matches the cookie.
	 */
	public CookieWrapper findCookie(Cookie cookieObj) {

		// Iterator for the cookieArrayList
		Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();

		// While loop traverses through the cookieArrayList
		while (iteratorObj.hasNext()) {
			CookieWrapper currentCookie = iteratorObj.next();

			// If condition checks if the value of cookie is the same
			if (currentCookie.getNewCookieObj().getValue().equals(cookieObj.getValue())) {
				return currentCookie;
			}
		}
		return null;
	}

	/**
	 * updateCustNo is a function that updates the corresponding custNo for the incoming cookie.
	 * 
	 * @param	cookieObj	Contains the cookie received from the client
	 * @param	custNo		Contains the current client's custNo
	 * 
	 * @return	boolean		Returns if the cookieObj was successfully updated. 
	 */
	public boolean updateCustNo(Cookie cookieObj, int custNo) {

		CookieWrapper cookieWrapperObj = findCookie(cookieObj);

		// If condition checks if the current CookieWrapper object is not null.
		if (cookieWrapperObj != null) {
			cookieWrapperObj.setCustNo(custNo);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * displayCookie is a function that displays the cookieArrayList elements.
	 */
	public String displayCookies() {
		
		// If condition checks if the cookieArrayList is not empty
		if (cookieArrayList != null) {
			
			ArrayList<String> cookieString = new ArrayList<String>();
			Iterator<CookieWrapper> iteratorObj = cookieArrayList.iterator();

			// While loop traverses the cookieArrayList
			while (iteratorObj.hasNext()) {
				CookieWrapper currentCookie = iteratorObj.next();
				
				// Puts individual values of each CookieWrapper object into a string format
				cookieString.add(currentCookie.getNewCookieObj().toString() + " --> CustNo: "
						+ Integer.toString(currentCookie.getCustNo()) + " --> LastAccessed: "
						+ currentCookie.getLastAccessed().toString());
			}
			return cookieString.toString();
		}

		return null;
	}
}
