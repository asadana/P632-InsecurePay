package com.application.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/acm")
public class AccountManagementService {
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	public String validateLogin()
			throws InstantiationException, IllegalAccessException {
		return "jbhalla";
	}
}
