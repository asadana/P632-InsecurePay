package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.application.common.DaoFactory;
import com.application.common.Constants;
import com.application.dao.CustomerDao;
import com.application.service.BO.CustomerBO;

@Path("/custService")
public class CustomerService extends BaseService {

	@Context HttpServletRequest request;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerDetails(@CookieParam("CookieID") Cookie cookieObj, @QueryParam("custNo") int custNo)
			throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException {
		
		/*		
		HttpSession sessionObj = request.getSession();
		logger.info("IN CUST - SESSION ID : " + sessionObj.getId());
		logger.info("IN CUST - SESSION ID : " + sessionObj.toString());
		*/
		
		logger.info("REMOVE ME: StringConstants : " + Constants.cookieList.getNewCookieList());
		Date dateObj = Calendar.getInstance().getTime();
		
		NewCookie newCookieObj = Constants.cookieList.findCookie(cookieObj);
		
		// Check to see if the value of the cookie is correct
		// and if the cookie is not yet expired
		if(newCookieObj != null
				&& dateObj.compareTo(newCookieObj.getExpiry()) < 0) {
			
			logger.info("REMOVE ME: Inside if : " + cookieObj.toString());
			CustomerBO customergenBO = null;
			try {
				customergenBO = DaoFactory.getInstance(CustomerDao.class,
						this.getConnection()).getCustomerDetails(custNo);
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
			return Response.status(Response.Status.ACCEPTED).entity(customergenBO).build();
		} else {
			logger.warn("Invalid cookie used.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerDetails(@CookieParam("CookieID") Cookie cookieObj, CustomerBO customergenBO) {
		if(cookieObj != null) {
			Boolean booleanObj = false;
			try {
				booleanObj = DaoFactory.getInstance(CustomerDao.class,
						this.getConnection()).updateCustomerDetails(customergenBO);
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
			return Response.status(Response.Status.OK).entity(booleanObj).build();
		} else {
			logger.warn(this.getClass().getSimpleName(), "Invalid cookie used.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}
