package com.application.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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

@Provider
public class CookieRequestFilter implements ContainerRequestFilter {
	@Context
	private UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext clientRequest)
			throws IOException {

		if (!(uriInfo.getPath().equals("login") || uriInfo.getPath().equals("forgotPassword"))) {
			Logging.logger.info("REMOVE ME: StringConstants : "
					+ Constants.cookieList.displayCookies());
			Map<String, Cookie> cookies = clientRequest.getCookies();
			if (cookies.size() == 0) {
				Logging.logger.warn("Cookies list null");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			} else {
				Logging.logger.info("Request Cookies :" + cookies.toString());
				Cookie cookieObj = cookies.get("CookieID");
				Entry<NewCookie, Integer> cookieFound = Constants.cookieList
						.findCookie(cookieObj);
				NewCookie newCookieObj = null;
				int custNo = 1;
				if (cookieFound != null) {
					newCookieObj = cookieFound.getKey();
					custNo = cookieFound.getValue();
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
					clientRequest.getHeaders().add("CustNo", Integer.toString(custNo));
				}
			}
		}

	}
}
