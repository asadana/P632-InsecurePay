package com.application.service;

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

import com.application.common.Constants;
import com.application.common.DaoFactory;
import com.application.dao.AccountDao;
import com.application.service.BO.AccountBO;

@Path("/accountService")
public class AccountService extends BaseService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerDetails(@QueryParam("custNo") int custNo, 
			@CookieParam("CookieID") Cookie cookieObj)
			throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException {
		
		Constants.cookieList.updateCustNo(cookieObj, custNo);
		AccountBO accountBO = null;
		try {
			accountBO = DaoFactory.getInstance(AccountDao.class,
					this.getConnection()).getAccountDetails(custNo);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(this.getClass().getSimpleName(), e);
		} finally {
			try {
				close();
			} catch (SQLException e) {
				logger.error(this.getClass().getSimpleName(), e);
			}
		}
		return Response.status(Response.Status.ACCEPTED).entity(accountBO)
				.build();

	}

}
