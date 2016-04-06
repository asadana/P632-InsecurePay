package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Constants;
import com.application.common.Queries;
import com.application.service.Logging;
import com.application.service.BO.ForgotPasswordBO;
import com.application.service.BO.LoginValidationBO;

public class ForgotPasswordDao extends BaseDao {

	public ForgotPasswordDao(Connection conn) {
		super(conn);
	}

	public LoginValidationBO validateUser(ForgotPasswordBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		LoginValidationBO loginValidationBO;

		boolean usernameExists;
		boolean validUser;

		int cust_no = -1;
		int cust_no_compare = -2;
		ResultSet rs = null;

		List<Object> params = new ArrayList<Object>();
		params.add(l.getUsername());
		rs = querySql(Queries.USERNAME_EXISTS, params);
		if (rs.next()) {
			usernameExists = true;
			close();

			params = new ArrayList<Object>();
			params.add(l.getAccountNo());
			rs = querySql(Queries.GET_CUSTNO_ACCOUNT_TBL, params);
			if (rs.next()) {
				cust_no = rs.getInt("cust_no");
			}

			close();

			params = new ArrayList<Object>();
			params.add(l.getEncodedSSNNo());
			rs = querySql(Queries.GET_CUSTNO_CUST_TBL, params);
			if (rs.next()) {
				cust_no_compare = rs.getInt("cust_no");
			}
			close();

			if (cust_no == cust_no_compare) {
				validUser = true;
				params = new ArrayList<Object>();
				params.add(Constants.defaultPassword);
				params.add(l.getUsername());
				int count = updateSql(Queries.UPDATE_DEFAULT_PASSWORD, params);
				if(count != 0) {
					Logging.logger.debug("Password reset successful");
				} else {
					Logging.logger.debug("Password reset failed");
				}
			} else
				validUser = false;

		} else {
			usernameExists = false;
			validUser = false;
		}

		loginValidationBO = new LoginValidationBO(usernameExists, validUser,
				cust_no);

		return loginValidationBO;
	}
}
