package com.application.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.ws.rs.core.NewCookie;

import com.application.common.StringConstants;

/*
 * Manages connection
 */

public class BaseService extends Logging {
	private Connection conn = null;
	public NewCookie newCookieObj = null;
	
	private void createConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		String url = StringConstants.url;
		String dbName = StringConstants.dbName;
		String driver = StringConstants.driver;
		String userName = StringConstants.userName;
		String password = StringConstants.password;
		Class.forName(driver).newInstance();
		this.conn = (Connection) DriverManager.getConnection(url + dbName,
				userName, password);

	}

	protected void close() throws SQLException {
		if (conn != null)
			conn.close();
	}

	public Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		if (conn == null) {
			createConnection();
		}
		return conn;
	}

	public NewCookie getNewCookieObj() {
		return newCookieObj;
	}

	public void setNewCookieObj(NewCookie newCookieObj) {
		this.newCookieObj = newCookieObj;
	}
}
