package com.cigital.insecurepay.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.ChangePasswordDao;
import com.cigital.insecurepay.service.BO.ChangePasswordBO;

/**
 * ChangePasswordService extends {@link BaseService}. 
 * This class is a service that allows the user to update 
 * his existing password.
 */
@Path("/changePassword")
public class ChangePasswordService extends BaseService {

	/**
	 * changePassword is a function that calls an instance of
	 * {@link ChangePasswordDao} to update the database with the new password.
	 * 
	 * @param	changePasswordBO	Contains the details needed to update password in form of
	 * 								{@link ChangePasswordBO} object.
	 * 
	 * @return	Response	Return a {@link Response}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(ChangePasswordBO changePasswordBO) {

		Boolean passwordChanged = false;

		try {
			passwordChanged = DaoFactory.getInstance(ChangePasswordDao.class,
														this.getConnection())
										.changePassword(changePasswordBO);

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

		return Response.status(Response.Status.OK).entity(passwordChanged).build();
	}
}
