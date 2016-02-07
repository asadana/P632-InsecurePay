package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;

public class LoginDao extends BaseDao {

	public LoginDao(Connection conn) {
		super(conn);
	}

	public boolean validateUser(String username, String password)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		params.add(password);
		System.out.println(username);
		System.out.println(password);
		rs = querySql(Queries.VALIDATE_USER, params);
		if (rs.next())
			return true;
		close();

		return false;
	}
}
