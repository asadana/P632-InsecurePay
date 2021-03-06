package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.AccountBO;

/**
 * AccountDao extends {@link BaseDao}. This class handles the querying and
 * retrieving basic account information for the user.
 */
public class AccountDao extends BaseDao {

	/**
	 * AccountDao is a parameterized constructor to initialize the super class.
	 */
	public AccountDao(Connection conn) {
		super(conn);
	}

	/**
	 * getAccountDetails is a function that queries the database using the
	 * user's custNo.
	 * 
	 * @param custNo
	 *            Contains the customer number sent by the user.
	 * 
	 * @return AccountBO Return the {@link AccountBO} object.
	 */
	public AccountBO getAccountDetails(int custNo) {

		ResultSet resultSet = null;
		List<Object> params = new ArrayList<Object>();
		params.add(custNo);

		AccountBO accountBO = null;

		try {
			// Query using params and store response in resultSet
			resultSet = querySql(Queries.GET_ACCOUNT_TBL, params);

			if (resultSet.next()) {
				accountBO = new AccountBO();
				// sets result of SQL query to AccountBO object
				accountBO.setCustomerNumber(custNo);
				accountBO.setAccountNumber(resultSet.getInt("account_no"));
				accountBO.setAccountBalance(resultSet.getFloat("account_balance"));
			}
			close();
			return accountBO;
		} catch (InstantiationException | IllegalAccessException | 
					ClassNotFoundException | SQLException e) {
			Logging.logger.error(e);
			return accountBO;
		}
	}

	/**
	 * accountNoValid queries the database to check if the account number is
	 * valid.
	 * 
	 * @param accountNumber
	 *            Contains the account number provided by the user.
	 * 
	 * @return Boolean Returns a boolean value depending if the account number
	 *         exists.
	 */
	public Boolean accountNumberValid(int accountNumber) {

		Boolean accountValid = false;
		ResultSet resultSet = null;

		List<Object> params = new ArrayList<Object>();
		params.add(accountNumber);

		Logging.logger.debug("Querying the database");
		try {
			// Query the database to check for the account number
			// and store it in resultSet.
			resultSet = querySql(Queries.GET_ACCOUNT_TBL_WITH_ACCNO, params);

			// If atleast one row is returned by SQL query, accountNo is valid
			if (resultSet.next())
				accountValid = true;
			else
				accountValid = false;
			return accountValid;
		} catch (InstantiationException | IllegalAccessException | 
					ClassNotFoundException | SQLException e) {
			Logging.logger.error(e);
			return accountValid;
		}
	}
}
