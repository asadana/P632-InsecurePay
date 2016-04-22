package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.CustomerBO;

/**
 * CustomerDao extends {@link BaseDao}. This class handles the querying and
 * retrieving account information for the user to view and manage.
 */
public class CustomerDao extends BaseDao {

	/**
	 * CustomerDao is a parameterized constructor to initialize the super class.
	 */
	public CustomerDao(Connection connectionObj) {
		super(connectionObj);
	}

	/**
	 * getCustomerDetails is a function that queries the database to retrieve
	 * all the customer details linked to the custNo provided.
	 * 
	 * @param custNo
	 *            Contains the customer number of the user.
	 * 
	 * @return CustomerBO Return the {@link CustomerBO} object containing all
	 *         the information retrieved.
	 */
	public CustomerBO getCustomerDetails(int custNo) {

		ResultSet resultSet = null;

		List<Object> params = new ArrayList<Object>();
		params.add(custNo);

		CustomerBO customerBOObj = null;
		Logging.logger.debug("getCustomerDetails: Querying the database.");
		try {
			// Query the database and store the response in resultSet
			resultSet = querySql(Queries.GET_CUSTOMER_DETAILS, params);

			// Check if anything was returned
			// and store the result in customerBOObj
			if (resultSet.next()) {
				customerBOObj = new CustomerBO();
				customerBOObj.setCustNo(resultSet.getInt("cust_no"));
				customerBOObj.setCustName(resultSet.getString("cust_name"));
				customerBOObj.setStreet(resultSet.getString("street"));
				customerBOObj.setCity(resultSet.getString("city"));
				customerBOObj.setState(resultSet.getString("state"));
				customerBOObj.setZipcode(resultSet.getInt("zipcode"));
				customerBOObj.setPhoneNo(resultSet.getLong("phone_no"));
				customerBOObj.setBirthDate(resultSet.getDate("Birth_date"));
				customerBOObj.setSsn(resultSet.getString("ssn"));
				customerBOObj.setEmail(resultSet.getString("email"));
			}
			return customerBOObj;
		} catch (InstantiationException | IllegalAccessException | 
					ClassNotFoundException | SQLException e) {
			Logging.logger.error("getCustomerDetails: " + e);
			return customerBOObj;
		}
	}

	/**
	 * updateCustomerDetails is a function that queries the database to update
	 * the existing account information of the user.
	 * 
	 * @param customerBO
	 *            Contains the object of {@link CustomerBO} with new details
	 *            from the user.
	 * 
	 * @return boolean Return a boolean value depending on if the update was
	 *         successful.
	 */
	public boolean updateCustomerDetails(CustomerBO customerBO) {

		List<Object> params = new ArrayList<Object>();
		params.add(customerBO.getCustName());
		params.add(customerBO.getStreet());
		params.add(customerBO.getCity());
		params.add(customerBO.getState());
		params.add(customerBO.getZipcode());
		params.add(customerBO.getPhoneNo());
		params.add(customerBO.getEmail());
		// Converting java.util.Date (supported by JSON/GSON)
		// into java.sql.Date (supported by database)
		params.add(new java.sql.Date(customerBO.getBirthDate().getTime()));
		params.add(customerBO.getCustNo());

		Logging.logger.debug("updateCustomerDetails: Querying the database.");
		try {
			// If condition checks if the update was a success
			if (updateSql(Queries.UPDATE_CUSTOMER_DETAILS, params) > 0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			Logging.logger.error("updateCustomerDetails: " + e);
			return false;
		}
	}
}
