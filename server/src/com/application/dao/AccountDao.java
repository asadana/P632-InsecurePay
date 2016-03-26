package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.AccountBO;

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
		rs = querySql(Queries.GET_ACCOUNT_TBL, params);
		AccountBO account = new AccountBO();
		if (rs.next()) {
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
		rs = querySql(Queries.GET_ACCOUNT_TBL_WITH_ACCNO, params);
		if (rs.next()) 
			accountValid = true;
		else
			accountValid = false;	
	
		return accountValid;
		
	}
}
