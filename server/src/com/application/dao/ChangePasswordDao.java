package com.application.dao;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.ChangePasswordBO;


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
