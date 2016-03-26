package com.application.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.application.common.Constants;
import com.application.common.CookieWrapper;

@Provider
public class CookieRequestFilter implements ContainerRequestFilter {
	@Context
	private UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext clientRequest)
			throws IOException {

		if (!(uriInfo.getPath().equals("login") || uriInfo.getPath().equals("forgotPassword"))) {
			
			// display all the cookies
			Logging.logger.debug("Displaying cookies: " + Constants.cookieList.displayCookies());
			
			Map<String, Cookie> cookies = clientRequest.getCookies();
			if (cookies.size() == 0) {
				Logging.logger.warn("Cookies list null");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			} else {
				Logging.logger.info("Request Cookies :" + cookies.toString());
				Cookie cookieObj = cookies.get("CookieID");
				
				CookieWrapper cookieWrapperObj = Constants.cookieList.findCookie(cookieObj);
				
				// TODO: Remove these variables with direct get calls
				int custNo = 0;
				NewCookie newCookieObj = null;
				
				if (cookieWrapperObj != null) {
					newCookieObj = cookieWrapperObj.getNewCookieObj();
					custNo = cookieWrapperObj.getCustNo();
					Logging.logger.debug("REMOVE ME: Found cookie: " + 
											newCookieObj.getValue() + "; CustNo: " + Integer.toString(custNo));
				}
				
				Date dateObj = Calendar.getInstance().getTime();
				// Check to see if the value of the cookie is correct
				// and if the cookie is not yet expired
				
				if (newCookieObj == null
						|| dateObj.compareTo(newCookieObj.getExpiry()) > 0) {
					Logging.logger.warn("Invalid cookie used.");
					throw new WebApplicationException(Status.UNAUTHORIZED);
				} else {
					Logging.logger.info("REMOVE ME: Inside if : "
							+ cookieObj.toString());
					cookieWrapperObj.setLastAccessed(Calendar.getInstance().getTime());
					clientRequest.getHeaders().add("CustNo", Integer.toString(custNo));
				}
			}
		}

	}
}
