package com.cigital.insecurepay.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.Constants;

/**
 * LogoutService extends {@link BaseService}. 
 * This class is a service that is invoked when the user logs out.
 */
@Path("/logout")
public class LogoutService extends BaseService {

	/**
	 * onLogout is a function that calls the deleteExpiredCookies
	 * function, and always returns true.
	 * 
	 * @return	Response	Return a {@link Response}.
	 */
	@POST
	public Response onLogout() {
		// Deleting expired cookies
		Constants.cookieList.deleteExpiredCookies();
		
		Logging.logger.debug("Cookies after deletion : " + Constants.cookieList.displayCookies());

		return Response.status(Response.Status.ACCEPTED).entity(true).build();
	}

}
