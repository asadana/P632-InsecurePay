package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.application.common.DaoFactory;
import com.application.common.Constants;
import com.application.dao.LoginDao;
import com.application.service.BO.LoginBO;
import com.application.service.BO.LoginValidationBO;

/**
 * Service called for login
 */

@Path("/login")
public class LoginService extends BaseService {

	@Context private HttpServletRequest request;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateLogin(LoginBO loginBO) {

		//int ageInSeconds = 60*60*24;
		int ageInSeconds = 60*2;
		LoginValidationBO validate = null;
		NewCookie newCookieObj = null;
		
		try {
			
			validate = DaoFactory.getInstance(LoginDao.class,
					this.getConnection()).validateUser(loginBO);
			if (validate.isValidUser()) {
				Constants.counter++;
				/*
				HttpSession sessionObj = request.getSession();

				logger.info("IN LOGIN - OUT - SESSION ID : " + sessionObj.getId());
				logger.info("IN LOGIN - OUT - SESSION String : " + sessionObj.toString());
				
				if (sessionObj.getAttribute(loginBO.getUsername()) == null) {
					sessionObj.setAttribute("User", loginBO.getUsername());

					logger.info("IN LOGIN - IN - SESSION ID : " + sessionObj.getId());
					logger.info("IN LOGIN - IN - SESSION String : " + sessionObj.toString());
				}
				
				//setting session to expire in ageInSeconds
	            sessionObj.setMaxInactiveInterval(ageInSeconds);
				*/
				
				// Getting today's date
				Calendar calendarObj = Calendar.getInstance();
				// Getting tomorrow's date
				calendarObj.add(Calendar.SECOND, ageInSeconds);
				//calendarObj.add(Calendar.DAY_OF_YEAR, 1);
				// Grabbing the date object
				Date dateObj = calendarObj.getTime();
				
				// Generating the cookie
				// TODO: Replace username with a low entropy string
				newCookieObj = new NewCookie(new Cookie("CookieID", loginBO.getUsername() + Constants.counter, "/", ""), 
	                									null, ageInSeconds, dateObj, false, false);
				Constants.cookieList.addCookie(newCookieObj);
				logger.info("REMOVE ME: " + newCookieObj.toString());
				logger.info("REMOVE ME: " +  Constants.cookieList);
				
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(this.getClass().getSimpleName(), e);
		} finally {
			
			try {
				close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return Response.status(Response.Status.OK).entity(validate).cookie(newCookieObj).build();
	}
}