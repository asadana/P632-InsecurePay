package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.cigital.insecurepay.service.BO.LoginBO;
import com.cigital.insecurepay.service.BO.LoginValidationBO;

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
			statementObj = connectionObj.createStatement();
			resultSet = statementObj.executeQuery(sql2);
			if (resultSet.next()) {
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
		statementObj = connectionObj.createStatement();
		resultSet = statementObj.executeQuery(sql);
		if (resultSet.next()) {
			custNo = resultSet.getInt("cust_no");
		}
		close();

		return custNo;
	}
}
