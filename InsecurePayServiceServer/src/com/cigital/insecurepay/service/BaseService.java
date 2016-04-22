package com.cigital.insecurepay.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.cigital.insecurepay.common.Constants;

/**
 * BaseService is a parent class that is extended by all Services. It contains
 * the common variable and methods that can be used to establish a connection
 * with the database server.
 */
public class BaseService extends Logging {
	private Connection connectionObj = null;

	/**
	 * createConnection is a function that is called to establish a connection
	 * to the database server using the values from Constants file.
	 */
	private void createConnection()
			throws 	InstantiationException, IllegalAccessException, 
					ClassNotFoundException, SQLException {
		String url = Constants.url;
		String dbName = Constants.dbName;
		String driver = Constants.driver;
		String userName = Constants.userName;
		String password = Constants.password;
		Class.forName(driver).newInstance();
		this.connectionObj = (Connection) DriverManager
								.getConnection(url + dbName, userName, password);
	}

	/**
	 * close is a function that is called when the database connection 
	 * needs to be terminated.
	 */
	protected void close() throws SQLException {
		if (connectionObj != null)
			connectionObj.close();
	}
	
	/**
	 * getConnection is a function that initializes the connectionObj
	 * if one doesn't exists.
	 * 
	 * @return	Connection	Return the {@link Connection} object created.
	 */
	public Connection getConnection()
			throws 	InstantiationException, IllegalAccessException, 
					ClassNotFoundException, SQLException {
		if (connectionObj == null) {
			createConnection();
		}
		return connectionObj;
	}
}
