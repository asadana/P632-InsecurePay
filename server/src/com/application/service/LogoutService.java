package com.application.service;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.application.common.Constants;

@Path("/logout")
public class LogoutService extends BaseService{

	public void Logout()
	{
		Constants.cookieList.deleteCookie();
	}

}
