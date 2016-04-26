package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.CustomerDao;
import com.cigital.insecurepay.service.BO.CustomerBO;

/**
 * CustomerService extends {@link BaseService}.
 * This class is a service that that retrieves and updates
 * customer information for the user.
 */
@Path("/custService")
public class CustomerService extends BaseService {

	/**
	 * getCustomerDetails is a function that calls an instance of 
	 * {@link CustomerDao} to retrieve customer details from the database.
	 * 
	 * @param custNo		Contains the customer number sent by the user as an
	 * 						int object.
	 * @param cookieCustNo	Contains the customer number retrieved from the cookie
	 * 						mapping from the {@link CookieRequestFilter} as a String.
	 * 
	 * @return	Response	Return a {@link Response}
	 */	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerDetails(@QueryParam("custNo") int custNo, 
										@HeaderParam("CustNo") String cookieCustNo) {

		CustomerBO customergenBO = null;
		try {
			customergenBO = DaoFactory.getInstance(CustomerDao.class, 
													this.getConnection())
										.getCustomerDetails(Integer.parseInt(cookieCustNo));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException 
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				close();
			} catch (SQLException | NumberFormatException e) {
				logger.error(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		if (customergenBO != null) {
			return Response.status(Response.Status.ACCEPTED).entity(customergenBO).build();	
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	/**
	 * updateCustomerDetails is a function that calls an instance of 
	 * {@link CustomerDao} to update the customer details.
	 * 
	 * @param	customergenBO		Contains the updated information received from the user
	 * 								in the form of {@link CustomerBO} object.
	 * @param	cookieCustNo		Contains the customer number retrieved from the cookie
	 * 								mapping from the {@link CookieRequestFilter} as a String.
	 *  
	 * @return	Response	Return a {@link Response}
	 */	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerDetails(CustomerBO customergenBO, 
											@HeaderParam("CustNo") String cookieCustNo) {
		Boolean booleanObj = false;

		try {
			customergenBO.setCustomerNumber(Integer.parseInt(cookieCustNo));

			booleanObj = DaoFactory.getInstance(CustomerDao.class, 
												this.getConnection())
									.updateCustomerDetails(customergenBO);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
				| NoSuchMethodException | SecurityException | IllegalArgumentException 
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				close();
			} catch (SQLException | NumberFormatException e) {
				logger.error(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}

		return Response.status(Response.Status.OK).entity(booleanObj).build();
	}
}
