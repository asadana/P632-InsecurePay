package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.Constants;
import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.AccountDao;
import com.cigital.insecurepay.service.BO.AccountBO;

/**
 * AccountService extends {@link BaseService}.
 * This class is a service that retrieves basic account
 * information based on the customer number sent by the user.
 */
@Path("/accountService")
public class AccountService extends BaseService {

	/**
	 * getCustomerDetails is a function that calls an instance of 
	 * {@link AccountDao} to get basic account details.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerDetails(@QueryParam("custNo") int custNo, 
			@CookieParam("CookieID") Cookie cookieObj) {
		
		// Updating the cookie object sent by the customer with the
		// corersponding customer number
		Constants.cookieList.updateCustNo(cookieObj, custNo);
		
		AccountBO accountBO = null;
		try {
			accountBO = DaoFactory.getInstance(AccountDao.class,
					this.getConnection()).getAccountDetails(custNo);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
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
		
		if (accountBO == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.status(Response.Status.ACCEPTED).entity(accountBO).build();	
		}
	}
}
