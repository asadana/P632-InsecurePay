package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.AccountDao;

/**
 * AccountNumValidationService extends {@link BaseService}.
 * This class is a service that validates the account number
 * sent by the user by querying the database.
 */
@Path("/accountNumberValidation")
public class AccountNumValidationService extends BaseService {

	/**
	 * accountNumberValid is a function that calls an instance of 
	 * {@link AccountDao} to validate the account number.
	 * 
	 * @param	accountNo	Contains the account number as int variable.
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@GET
	public Response accountNumberValid(@QueryParam("accountNo") int accountNo) {

		Boolean accountValid = false;
		try {
			accountValid = DaoFactory.getInstance(AccountDao.class,
					this.getConnection()).accountNumberValid(accountNo);
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
		return Response.status(Response.Status.OK).entity(accountValid).build();
	}
}
