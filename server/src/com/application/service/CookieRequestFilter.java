package com.application.service;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;


@Provider
public class CookieRequestFilter implements ContainerRequestFilter {
	@Context
	private UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext ClientRequest) throws IOException {
		
		if(!uriInfo.getPath().equals("login"))
		{
			if (ClientRequest.getCookies().size() == 0) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
			
		}
	}

}
