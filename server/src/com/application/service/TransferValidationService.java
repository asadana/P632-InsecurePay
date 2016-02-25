package com.application.service;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.application.common.DaoFactory;
import com.application.dao.TransferValidationDao;
import com.application.service.BO.TransferValidationBO;


@Path("/transferValidation")
public class TransferValidationService extends BaseService{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TransferValidationBO validateCust(@QueryParam("username") String username) {
		TransferValidationBO validate = null;
		try {
			validate = DaoFactory.getInstance(TransferValidationDao.class,
					this.getConnection()).validateCust(username);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(this.getClass().getSimpleName(), e);
			e.printStackTrace();
		} finally {

			try {
				close();
			} catch (SQLException e) {
				logger.error(this.getClass().getSimpleName(), e);
				e.printStackTrace();
			}
		}
		return validate;
	}
}
