package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chatService")
public class ChatService extends BaseService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatMessage(String subject) throws SQLException, InstantiationException,
	IllegalAccessException, NoSuchMethodException, SecurityException,
	IllegalArgumentException, InvocationTargetException,
	ClassNotFoundException	{	
		
		return Response.status(Response.Status.OK).entity(subject).build();
	}

}
