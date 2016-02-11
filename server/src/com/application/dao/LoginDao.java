package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.application.service.BO.LoginBO;

public class LoginDao extends BaseDao {

	public LoginDao(Connection conn) {
		super(conn);
	}

	public boolean validateUser(LoginBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		ResultSet rs = null;
		rs = querySqlSt(l);
		if (rs.next())
			return true;
		close();

		return false;
	}
}
