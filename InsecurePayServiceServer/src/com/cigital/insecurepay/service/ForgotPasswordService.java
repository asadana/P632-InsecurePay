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
import com.cigital.insecurepay.dao.ForgotPasswordDao;
import com.cigital.insecurepay.service.BO.ForgotPasswordBO;
import com.cigital.insecurepay.service.BO.LoginValidationBO;

/**
 * ForgotPasswordService extends {@link BaseService}. 
 * This class is a service that allows the user to reset their 
 * account password.
 */
@Path("/forgotPassword")
public class ForgotPasswordService extends BaseService {

	/**
	 * validateLogin is a function that calls an instance of
	 * {@link ForgotPasswordDao} to query the database and reset the password.
	 * 
	 * @param	forgotPasswordBO	Contains the security information of the user
	 * 								that allows the password to be reset.
	 * 
	 * @return	Response	Return a {@link Response} 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateLogin(ForgotPasswordBO forgotPasswordBO) {

		LoginValidationBO validate = null;

		try {
			validate = DaoFactory.getInstance(ForgotPasswordDao.class, 
												this.getConnection())
								.validateUser(forgotPasswordBO);
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
		return Response.status(Response.Status.OK).entity(validate).build();
	}
}
