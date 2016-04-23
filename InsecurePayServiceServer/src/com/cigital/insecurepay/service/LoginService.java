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
 * LoginService extends {@link BaseService}. 
 * This class is a service that allows user to be validated 
 * before other services can be accessed from a client.
 */
@Path("/login")
public class LoginService extends BaseService {

	@Context
	private HttpServletRequest request;

	/**
	 * validateLogin is a function that validates the credentials 
	 * sent by the user against the database credentials using {@link LoginDao}.
	 * If valid, the user is allotted a cookie.
	 * 
	 * @param	loginBO		Contains the user credentials received
	 * 						in the form of {@link LoginBO} object.
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateLogin(LoginBO loginBO) {

		LoginValidationBO validate = null;
		NewCookie newCookieObj = null;

		try {

			validate = DaoFactory.getInstance(LoginDao.class,
												this.getConnection())
								.validateUser(loginBO);
			
			// If condition checks if the user was validated
			if (validate.isValidUser()) {
				// If condition checks if the cookieCounter is under a given range
				if (Constants.cookieCounter >= Constants.counterInitial 
						&& Constants.cookieCounter < Constants.counterLimit) {
					Constants.cookieCounter++;
				} else {
					Constants.cookieCounter = Constants.counterInitial;
				}

				// Alloting cookie to the validated user
				newCookieObj = Constants.cookieList.allotCookie(loginBO.getUsername(), 
																validate.getCustomerNumber());
			}
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
		return Response.status(Response.Status.OK).entity(validate).cookie(newCookieObj).build();
	}
}