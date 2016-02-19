package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.application.common.DaoFactory;
import com.application.dao.LoginDao;
import com.application.service.BO.LoginBO;
import com.application.service.BO.LoginValidationBO;

/*
 * Service called for login
 */

@Path("/login")
public class LoginService extends BaseService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginValidationBO validateLogin(LoginBO loginBO) {
		LoginValidationBO validate = null;
		try {
			validate = DaoFactory.getInstance(LoginDao.class,
					this.getConnection()).validateUser(loginBO);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
		} finally {

			try {
				close();
			} catch (SQLException e) {
			}
		}
		return validate;
	}
}