package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.PrimitiveIterator.OfDouble;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.TransferFundsDao;
import com.cigital.insecurepay.service.BO.TransferFundsBO;

/**
 * TransferFundsService extends {@link BaseService}. 
 * This class is a service that allows one user to transfer 
 * funds to another.
 */
@Path("/transferFunds")
public class TransferFundsService extends BaseService {

	/**
	 * transferFunds is a function that calls an instance of 
	 * {@link TransferFundsDao} to allow funds to be transferred from
	 * one user to another
	 * 
	 * @param	transferFundsBO		Contains the details of the transfer as an
	 * 								object of {@link TransferFundsBO} class.
	 * @param	cookieCustNo		Contains the customer number from the cookie
	 * 								map in {@link CookieRequestFilter}.
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferFunds (TransferFundsBO transferFundsBO, 
							@HeaderParam("CustNo") String cookieCustNo) {
		
		Boolean fundsTransferred = false;
		
		try {
			// Resetting the incoming customer number with the mapped one.
			transferFundsBO.getFromAccount()
							.setCustomerNumber(Integer.parseInt(cookieCustNo));
			
			fundsTransferred = DaoFactory.getInstance(TransferFundsDao.class, 
														this.getConnection())
										.transfer(transferFundsBO);
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
		return Response.status(Response.Status.OK).entity(fundsTransferred).build();
	}
}
