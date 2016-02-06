package com.application.service;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

/*
 * Manages connection
 */

public class BaseService {
	private Connection conn = null;
	
	private void createConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		String url = "jdbc:mysql://localhost:3307/";
		String dbName = "test";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";
			Class.forName(driver).newInstance();
			this.conn = (Connection) DriverManager.getConnection(url + dbName,
					userName, password);
		
	}
	
	protected void close() throws SQLException{
		if (conn != null)
			conn.close();
	}
	
	public Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if(conn == null){
			createConnection();
		}
		return conn;
	}
}
