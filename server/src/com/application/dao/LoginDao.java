package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.application.service.BO.LoginBO;
import com.application.service.BO.LoginValidationBO;

public class LoginDao extends BaseDao {

	public LoginDao(Connection conn) {
		super(conn);
	}

	public LoginValidationBO validateUser(LoginBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		LoginValidationBO validationBO;
		boolean usernameExists;
		boolean validUser;
		int custNo = -1;
		ResultSet rs = null;
		String sql = "select * from cust_credentials where cust_username='"
				+ l.getUsername() + "'";
		s = conn.createStatement();
		rs = s.executeQuery(sql);
		if (rs.next()) {
			usernameExists = true;
			close();
			String sql2 = "select * from cust_credentials where cust_username='"
					+ l.getUsername()
					+ "' and password='"
					+ l.getPassword()
					+ "'";
			s = conn.createStatement();
			rs = s.executeQuery(sql2);
			if (rs.next()){
				custNo = rs.getInt("cust_no");
				validUser = true;
			}
			else
				validUser = false;
			close();
		} else {
			usernameExists = false;
			validUser = false;
		}

		validationBO = new LoginValidationBO(usernameExists, validUser, custNo);

		return validationBO;
	}
}
