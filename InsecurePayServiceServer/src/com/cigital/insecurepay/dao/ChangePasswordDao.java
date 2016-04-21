package com.cigital.insecurepay.dao;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.BO.ChangePasswordBO;


public class ChangePasswordDao extends BaseDao{
	
	public ChangePasswordDao(Connection conn) {
		super(conn);
	}
	
	public Boolean ChangePassword(ChangePasswordBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		Boolean password_changed = false;

		List<Object> params = new ArrayList<Object>();
		params.add(l.getPassword());
		params.add(l.getUsername());
		int count = updateSql(Queries.UPDATE_PASSWORD, params);
		if (count>=1) 
			password_changed = true;
		else
			password_changed = false;	
	
		return password_changed;
	}

}
