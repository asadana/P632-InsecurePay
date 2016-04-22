package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Constants;
import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.ForgotPasswordBO;
import com.cigital.insecurepay.service.BO.LoginValidationBO;

/**
 * ForgotPasswordDao extends {@link BaseDao}. This class handles the querying
 * the database and resetting the password for the user.
 */
public class ForgotPasswordDao extends BaseDao {

	/**
	 * ForgotPasswordDao is a parameterized constructor to initialize the super
	 * class.
	 */
	public ForgotPasswordDao(Connection connectionObj) {
		super(connectionObj);
	}

	/**
	 * validateUser is a function that checks if the incoming user in the
	 * request is a valid user. If the user exists then the user's password is
	 * reset to the defaultPassword from Constants.
	 * 
	 * @param forgotPasswordBO
	 *            Contains an object of {@link ForgotPasswordBO} from the user.
	 * 
	 * @return LoginValidationBO Returns an object of {@link LoginValidationBO}.
	 */
	public LoginValidationBO validateUser(ForgotPasswordBO forgotPasswordBO) {

		LoginValidationBO loginValidationBO = null;

		boolean usernameExists = false;
		boolean validUser = false;

		int customerNumber = -1;
		int customerNumberCompare = -2;
		ResultSet resultSet = null;
		loginValidationBO = new LoginValidationBO(usernameExists, validUser, customerNumber);

		List<Object> params = new ArrayList<Object>();
		params.add(forgotPasswordBO.getUsername());

		Logging.logger.debug("Querying the database for username.");

		try {
			resultSet = querySql(Queries.USERNAME_EXISTS, params);

			if (resultSet.next()) {
				usernameExists = true;
				close();

				params = new ArrayList<Object>();
				params.add(forgotPasswordBO.getAccountNumber());

				Logging.logger.debug("Querying database for " 
									+ "customer number from account table.");
				resultSet = querySql(Queries.GET_CUSTNO_ACCOUNT_TBL, params);
				if (resultSet.next()) {
					customerNumber = resultSet.getInt("cust_no");
				}
				close();

				params = new ArrayList<Object>();
				params.add(forgotPasswordBO.getEncodedSSNNumber());

				Logging.logger.debug("Querying database for " 
									+ "customer number from customer table.");
				resultSet = querySql(Queries.GET_CUSTNO_CUST_TBL, params);

				if (resultSet.next()) {
					customerNumberCompare = resultSet.getInt("cust_no");
				}
				close();

				// If condition checks if the customer numbers retrieved are
				// same.
				if (customerNumber == customerNumberCompare) {
					validUser = true;
					params = new ArrayList<Object>();
					params.add(Constants.defaultPassword);
					params.add(forgotPasswordBO.getUsername());

					Logging.logger.debug("Updating the database " 
										+ "with default password.");
					int count = updateSql(Queries.UPDATE_DEFAULT_PASSWORD, params);
					if (count != 0) {
						Logging.logger.debug("Password reset successful.");
					} else {
						Logging.logger.debug("Password reset failed.");
					}
				} else
					validUser = false;

			} else {
				Logging.logger.debug("Username does not exist.");
			}

			loginValidationBO = new LoginValidationBO(usernameExists, 
														validUser, customerNumber);
			return loginValidationBO;
		} catch (InstantiationException | IllegalAccessException | 
					ClassNotFoundException | SQLException e) {
			Logging.logger.error(e);
			return loginValidationBO;
		}
	}
}
