package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.application.common.DaoFactory;
import com.application.dao.ActivityHistoryDao;
import com.application.service.BO.TransactionBO;

@Path("/activityHistory")
public class ActivityHistoryService extends BaseService{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ActivityList(@QueryParam("accountNo") int accountNo){
		List<TransactionBO> resultlist = null;
		try {
			resultlist = DaoFactory.getInstance(ActivityHistoryDao.class,
					this.getConnection()).getActivityHistory(accountNo);
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
		return Response.status(Response.Status.ACCEPTED).entity(resultlist).build();

	}
}
