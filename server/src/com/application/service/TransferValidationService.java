package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.application.common.DaoFactory;
import com.application.dao.LoginDao;

@Path("/transferValidation")
public class TransferValidationService extends BaseService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateCust(@QueryParam("username") String username) {
		int custNo = -1;
		try {
			custNo = DaoFactory.getInstance(LoginDao.class,
					this.getConnection()).checkUsername(username);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(e);
		} finally {

			try {
				close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return Response.status(Response.Status.OK).entity(custNo).build();

	}
}
