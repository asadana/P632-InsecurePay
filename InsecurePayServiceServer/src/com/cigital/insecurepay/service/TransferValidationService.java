package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.LoginDao;

/**
 * TransferValidationService extends {@link BaseService}.
 * This class is a service that allows the username of the user
 * to be validated before the TransferFundsService is invoked.
 */
@Path("/transferValidation")
public class TransferValidationService extends BaseService {

	/**
	 * validateCustomer is a function that calls an instance of 
	 * {@link LoginDao} to validate the username of the customer.
	 * 
	 * @param	username	Contains the username entered by the customer
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateCustomer(@QueryParam("username") String username) {
		
		int custNo = -1;
		
		try {
			custNo = DaoFactory.getInstance(LoginDao.class,
					this.getConnection()).checkUsername(username);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {

			try {
				close();
			} catch (SQLException e) {
				logger.error(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		return Response.status(Response.Status.OK).entity(custNo).build();
	}
}
