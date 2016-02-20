package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.application.common.DaoFactory;
import com.application.dao.CustomerDao;
import com.application.service.BO.CustomerBO;

@Path("/custService")
public class CustomerService extends BaseService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CustomerBO getCustomerDetails(@QueryParam("username") String username)
			throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException {

		CustomerBO customergenBO = null;
		try {
			customergenBO = DaoFactory.getInstance(CustomerDao.class,
					this.getConnection()).getCustomerDetails(username);
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
		return customergenBO;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean updateCustomerDetails(CustomerBO customergenBO) {
		try {
			return DaoFactory.getInstance(CustomerDao.class, this.getConnection())
					.updateCustomerDetails(customergenBO);
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
		return false;

	}

}
