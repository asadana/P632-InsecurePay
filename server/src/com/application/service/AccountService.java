package com.application.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.application.common.DaoFactory;
import com.application.dao.AccountDao;

import com.application.service.BO.AccountBO;


@Path("/accountService")
public class AccountService extends BaseService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccountBO getCustomerDetails(@QueryParam("custNo") int custNo) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		
		AccountBO accountBO = null;
		try {
			accountBO = DaoFactory.getInstance(AccountDao.class,this.getConnection()).getAccountDetails(custNo);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return accountBO;
	}

}
