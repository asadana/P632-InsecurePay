package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.TransferValidationBO;


public class TransferValidationDao extends BaseDao {
	
	public TransferValidationDao(Connection conn) {
		super(conn);
	}

	public TransferValidationBO validateCust(String username)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		TransferValidationBO transferValidationBO;
		
		boolean usernameExists=false;
		int custNo = -1;
		
		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		
		rs = querySql(Queries.USERNAME_EXISTS, params);
		
		if (rs.next()) {
			usernameExists = true;
			close();
			
			params = new ArrayList<Object>();
			params.add(username);
			rs = querySql(Queries.GET_CUSTNO_CRED_TBL, params);
			if (rs.next()) {
				custNo = rs.getInt("cust_no");
			}

			close();
		}
		else {
			usernameExists = false;
			custNo= -1;
		}
		
		transferValidationBO = new TransferValidationBO(usernameExists,custNo);

		return transferValidationBO;
		
	}
}
