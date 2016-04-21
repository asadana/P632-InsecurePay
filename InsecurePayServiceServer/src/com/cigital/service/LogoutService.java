package com.cigital.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.cigital.common.Constants;

@Path("/logout")
public class LogoutService extends BaseService{

	@POST
	public Response Logout()
	{
		Constants.cookieList.deleteExpiredCookies();
		Logging.logger.debug("Cookies after deletion : " + Constants.cookieList.displayCookies());
		
		return Response.status(Response.Status.ACCEPTED).entity(true).build();
	}

}
