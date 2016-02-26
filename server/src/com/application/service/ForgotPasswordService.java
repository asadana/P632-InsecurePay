package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.application.common.DaoFactory;
import com.application.dao.ForgotPasswordDao;
import com.application.service.BO.ForgotPasswordBO;
import com.application.service.BO.LoginValidationBO;


@Path("/ForgotPassword")
public class ForgotPasswordService extends BaseService{
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginValidationBO validateLogin(ForgotPasswordBO forgotPasswordBO) {
		LoginValidationBO validate = null;
		try {
			validate = DaoFactory.getInstance(ForgotPasswordDao.class,
					this.getConnection()).validateUser(forgotPasswordBO);
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
		return validate;
	}
}
