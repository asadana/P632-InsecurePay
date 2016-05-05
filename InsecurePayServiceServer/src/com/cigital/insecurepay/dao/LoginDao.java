package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.LoginBO;
import com.cigital.insecurepay.service.BO.LoginValidationBO;

/**
 * LoginDao extends {@link BaseDao}.
 * This class checks if the login credentials are valid
 */
public class LoginDao extends BaseDao {

	/**
	 * LoginDao is a parameterized constructor to initialize the 
	 * super class.
	 */
	public LoginDao(Connection connectionObj) {
		super(connectionObj);
	}

	/**
	 * validateUser is a function that queries the database to see if 
	 * the login attempt is valid.
	 * 
	 * @param	loginBO		Contains the information sent by the user in the form
	 * 						of {@link LoginBO} object.
	 * 
	 * @return	LoginValidationBO		Return an object of {@link LoginValidationBO}
	 * 									depending on if the details were valid.
	 */
	public LoginValidationBO validateUser(LoginBO loginBO) {
		
		LoginValidationBO validationBO = new LoginValidationBO(false, false, -1);
		validationBO.setCustomerNumber(checkUsername(loginBO.getUsername()));

		if (validationBO.getCustomerNumber() != -1) {
			validationBO.setUsernameExists(true);
			
			List<Object> params = new ArrayList<Object>();
			params.add(loginBO.getUsername());
			params.add(loginBO.getPassword());
			
			Logging.logger.debug("Attempting to login.");
			try {
				// Querying the database to attempt login
				String sql2 = "select * from cust_credentials where "
						+ "cust_username='" + loginBO.getUsername()
						+ "' and password='" + loginBO.getPassword()
						+ "'";
				statementObj = connectionObj.createStatement();
				resultSet = statementObj.executeQuery(sql2);				
				if (resultSet.next()) {
					validationBO.setValidUser(true);
				}
				close();
				
			} catch (SQLException e) {
				Logging.logger.error(e);
				return validationBO;
			}
		}
		return validationBO;
	}
	
	/**
	 * checkUsername is a function that queries the database to check if the 
	 * username being sent is valid.
	 * 
	 * @param	username	Contains the username sent from the user.
	 * 
	 * @return	int		Returns an integer depending on if the user is valid. 
	 */
	public int checkUsername(String username) {
		
		int custNo = -1;
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		
		try {
			// Querying the database to check if username exists
			Logging.logger.debug("Checking if the username exists.");
			resultSet = querySql(Queries.USERNAME_EXISTS, params);
			if (resultSet.next()) {
				custNo = resultSet.getInt("cust_no");
			}
			close();
			
			return custNo;
		} catch (SQLException | InstantiationException | 
					IllegalAccessException | ClassNotFoundException e) {
			Logging.logger.error(e);
			return custNo;
		}
	}
}
