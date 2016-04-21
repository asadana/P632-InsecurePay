package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.Constants;
import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.LoginDao;
import com.cigital.insecurepay.service.BO.LoginBO;
import com.cigital.insecurepay.service.BO.LoginValidationBO;

/**
 * Service called for login
 */

@Path("/login")
public class LoginService extends BaseService {

	@Context
	private HttpServletRequest request;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateLogin(LoginBO loginBO) {

	
		LoginValidationBO validate = null;
		NewCookie newCookieObj = null;

		try {

			validate = DaoFactory.getInstance(LoginDao.class,
					this.getConnection()).validateUser(loginBO);
			if (validate.isValidUser()) {
				if (Constants.counter >= Constants.counterInitial && 
						Constants.counter < Constants.counterLimit) {
					Constants.counter++;
				} else {
					Constants.counter = Constants.counterInitial;	
				}

				newCookieObj = Constants.cookieList.allotCookie(loginBO.getUsername(),
																validate.getCustNo());
				logger.debug("REMOVE ME: " + newCookieObj.toString());
				logger.debug("REMOVE ME: " + Constants.cookieList);

			}
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
		return Response.status(Response.Status.OK).entity(validate)
				.cookie(newCookieObj).build();
	}
}