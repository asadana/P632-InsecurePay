package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.ActivityHistoryDao;
import com.cigital.insecurepay.service.BO.TransactionBO;

/**
 * ActivityHistoryService extends {@link BaseService}.
 * This class is a service that retrieves the activity on the
 * account number sent by the user.
 */
@Path("/activityHistory")
public class ActivityHistoryService extends BaseService{

	/**
	 * getActivityHistory is a function that calls an instance of 
	 * {@link ActivityHistoryDao} to query the database.
	 * 
	 * @param	accountNo	Contains the account number as int variable.
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivityHistory(@QueryParam("accountNo") int accountNo){
		
		List<TransactionBO> resultlist = null;
		
		try {
			resultlist = DaoFactory.getInstance(ActivityHistoryDao.class,
					this.getConnection()).getActivityHistory(accountNo);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
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
		
		if (resultlist != null) {
			return Response.status(Response.Status.ACCEPTED).entity(resultlist).build();	
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
}
