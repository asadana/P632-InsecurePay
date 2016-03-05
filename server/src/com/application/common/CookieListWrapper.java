/**
 * 
 */
package com.application.common;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class CookieListWrapper {

	private ArrayList<NewCookie> newCookieList;

	public CookieListWrapper() {
		newCookieList = new ArrayList<NewCookie>();
	}

	public void addCookie(NewCookie newCookieObj) {
		newCookieList.add(newCookieObj);
	}

	public boolean deleteCookie(Cookie cookieObj) {
		Iterator<NewCookie> iteratorObj = newCookieList.iterator();
		NewCookie newCookieObj;
		while(iteratorObj.hasNext()) {
			newCookieObj = iteratorObj.next();
			if(newCookieObj.getValue().equalsIgnoreCase(cookieObj.getValue())) {
				newCookieList.remove(newCookieObj);
				return true;
			}			
		}
		return false;
	}
	
	public NewCookie findCookie(Cookie cookieObj) {
		Iterator<NewCookie> iteratorObj = newCookieList.iterator();
		NewCookie newCookieObj;
		while(iteratorObj.hasNext()) {
			newCookieObj = iteratorObj.next();
			if(newCookieObj.getValue().equalsIgnoreCase(cookieObj.getValue())) {
				return newCookieObj;
			}
		}
		return null;
	}

	public ArrayList<NewCookie> getNewCookieList() {
		return newCookieList;
	}

}
