package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.AccountBO;

/*
 * Instantiates the AccountDao 
 */
public class AccountDao extends BaseDao {

	public AccountDao(Connection conn) {
		super(conn);
	}

	public AccountBO getAccountDetails(int custNo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(custNo);
		//SQL query to get Account details for the particular Customer No
		rs = querySql(Queries.GET_ACCOUNT_TBL, params);
		//Instantiates AccountBO object
		AccountBO account = new AccountBO();
		if (rs.next()) {
			//sets result of SQL query to AccountBO object
			account.setCustNo(custNo);
			account.setAccNo(rs.getInt("account_no"));
			account.setAccountBalance(rs.getFloat("account_balance"));
		}
		return account;
	}
	
	public Boolean accountNoValid(int accountNo) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, SQLException {
		
		Boolean accountValid = false;
		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(accountNo);
		//Checks if Account No is valid by its presence in Account table
		rs = querySql(Queries.GET_ACCOUNT_TBL_WITH_ACCNO, params);
		if (rs.next()) 
			//If atleast one row is returned by SQL query,accountNo is valid
			accountValid = true;
		else
			//AccountNO is invalid if its not present in Account table
			accountValid = false;	
	
		return accountValid;
		
	}
}
