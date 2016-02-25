package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.TransferFundsBO;

public class TransferFundsDao extends BaseDao{
	
	public TransferFundsDao(Connection conn) {
		super(conn);
	}
	
	public Boolean transfer(TransferFundsBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		Boolean fundsTransferred = false;
		int toAccountNo=-1;
		float toBeforeAmt=-1 ;
		float fromBeforeAmt=-1;
		float fromAfterAmt=-1;
		float toAfterAmt=-1;

		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(l.getToCustNo());
		rs = querySql(Queries.GET_ACCOUNT_TBL, params);
		if (rs.next()) {
			toAccountNo=rs.getInt("account_no");
			toBeforeAmt=rs.getFloat("account_balance");
		}

		close();
	
		params = new ArrayList<Object>();
		params.add(l.getFromCustNo());
		rs = querySql(Queries.GET_ACCOUNT_TBL, params);
		if (rs.next()) {
			fromBeforeAmt=rs.getFloat("account_balance");	
		}
		
		close();
		
		fromAfterAmt=fromBeforeAmt - l.getTransferAmount();
		toAfterAmt=toBeforeAmt + l.getTransferAmount();
		
		params = new ArrayList<Object>();
		params.add(l.getFromAccountNo());
		params.add(l.getFromCustNo());
		params.add(l.getToAccountNo());
		params.add(l.getToCustNo());
		params.add(l.getTransferAmount());
		params.add(fromBeforeAmt);
		params.add(fromAfterAmt);
		params.add(toBeforeAmt);
		params.add(toAfterAmt);
		params.add(l.getTransferDetails());
		int count = updateSql(Queries.INSERT_TRANSFER_TBL, params);
		if (count>=1)
			fundsTransferred=true;
		else 
			fundsTransferred=false;
		
		//Update sender's and receiver's current balance in account table
		params = new ArrayList<Object>();
		params.add(fromAfterAmt);
		params.add(l.getFromAccountNo());
		count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);
		
		params = new ArrayList<Object>();
		params.add(toAfterAmt);
		params.add(l.getToAccountNo());
		count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);
		
		return fundsTransferred;
		
	}


}
