package com.application.dao;

import java.sql.Connection;
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
		LoginValidationBO validationBO = new LoginValidationBO(false, false, -1);
		validationBO.setCustNo(checkUsername(l.getUsername()));

		if (validationBO.getCustNo() != -1) {
			validationBO.setUsernameExists(true);
			String sql2 = "select * from cust_credentials where cust_username='"
					+ l.getUsername()
					+ "' and password='"
					+ l.getPassword()
					+ "'";
			s = conn.createStatement();
			rs = s.executeQuery(sql2);
			if (rs.next()) {
				validationBO.setValidUser(true);
			}
			close();

		}
		return validationBO;
	}

	public int checkUsername(String username) throws SQLException {
		String sql = "select * from cust_credentials where cust_username='"
				+ username + "'";
		int custNo = -1;
		s = conn.createStatement();
		rs = s.executeQuery(sql);
		if (rs.next()) {
			custNo = rs.getInt("cust_no");
		}
		close();

		return custNo;
	}
}
