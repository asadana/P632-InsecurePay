package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.application.common.DaoFactory;
import com.application.dao.AccountDao;

@Path("/accountNumberValidation")
public class AccountNoValidationService extends BaseService {

	@GET
	public Response AccountNoValid(@QueryParam("accountNo") int accountNo) {

		Boolean accountValid = false;
		try {
			accountValid = DaoFactory.getInstance(AccountDao.class,
					this.getConnection()).accountNoValid(accountNo);
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
		return Response.status(Response.Status.OK).entity(accountValid).build();

	}

}
