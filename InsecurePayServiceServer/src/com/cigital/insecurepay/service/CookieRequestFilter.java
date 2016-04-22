package com.cigital.insecurepay.service;

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

import com.cigital.insecurepay.common.Constants;
import com.cigital.insecurepay.common.CookieWrapper;

/**
 * CookieRequestFilter implements {@link ContainerRequestFilter}.
 * This class acts as a filter for all incoming requests.
 */
@Provider
public class CookieRequestFilter implements ContainerRequestFilter {
	
	@Context
	private UriInfo uriInfo;

	/**
	 * filter is an overridden function that defines constraints on the
	 * incoming requests.
	 */
	@Override
	public void filter(ContainerRequestContext clientRequest) {

		// If condition checks if the incoming service is not one of the listed
		// This is where you can add services that need to bypass cookie check.
		if (!(uriInfo.getPath().equals("login") 
				|| uriInfo.getPath().equals("forgotPassword"))) {

			// display all the cookies
			Logging.logger.debug("Displaying cookies: " 
									+ Constants.cookieList.displayCookies());

			Map<String, Cookie> cookies = clientRequest.getCookies();
			
			if (cookies.size() == 0) {
				Logging.logger.warn("Cookies list null");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			} else {
				Logging.logger.info("Request Cookies :" + cookies.toString());
				Cookie cookieObj = cookies.get("CookieID");

				// Find the incoming cookie in the list
				CookieWrapper cookieWrapperObj = Constants.cookieList.findCookie(cookieObj);

				int custNo = 0;
				NewCookie newCookieObj = null;

				if (cookieWrapperObj != null) {
					newCookieObj = cookieWrapperObj.getNewCookieObj();
					custNo = cookieWrapperObj.getCustNo();
					Logging.logger.debug("Found cookie: " + newCookieObj.getValue() 
										+ "; CustNo: " + Integer.toString(custNo));
				}

				// Get current date and time
				Date dateObj = Calendar.getInstance().getTime();

				// Check to see if the value of the cookie is correct
				// and if the cookie is not yet expired
				if (newCookieObj == null || dateObj.compareTo(newCookieObj.getExpiry()) > 0) {
					Logging.logger.debug("Invalid cookie used.");
					throw new WebApplicationException(Status.UNAUTHORIZED);
				} else {
					Logging.logger.debug("Valid cookie used.");
					cookieWrapperObj.setLastAccessed(Calendar.getInstance().getTime());
					// Add the customer number from the mapped object to the header for other requests.
					clientRequest.getHeaders().add("CustNo", Integer.toString(custNo));
				}
			}
		}
	}
}
