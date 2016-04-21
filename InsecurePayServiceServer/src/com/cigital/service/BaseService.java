package com.cigital.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.cigital.common.Constants;

/*
 * Manages connection
 */

public class BaseService extends Logging {
	private Connection conn = null;
	
	private void createConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		String url = Constants.url;
		String dbName = Constants.dbName;
		String driver = Constants.driver;
		String userName = Constants.userName;
		String password = Constants.password;
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
}