package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * BaseDao is a parent class that is inherited by all dao classes.
 * It includes core methods of creating and querying results.
 */
public class BaseDao {
	
	protected Connection connectionObj = null;
	protected PreparedStatement preparedStatement = null;
	protected Statement statementObj = null;
	protected ResultSet resultSet = null;

	/**
	 * BaseDao is a parameterized constructor that initializes 
	 * connectionObj.
	 * 
	 * @param	connectionObj	Contains the Connection object used
	 * 							to initialize connectionObj.
	 */
	public BaseDao(Connection connectionObj) {
		this.connectionObj = connectionObj;
	}

	/**
	 * querySql is a function that sends a query to the database.
	 * 
	 * @param	sqlString	Contains the string containing the sql query.
	 * @param	params		Contains the params that will be injected into 
	 * 						the sqlString.
	 * 
	 * @return	ResultSet	Returns the response from the database
	 */
	public ResultSet querySql(String sqlString, List<Object> params)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		preparedStatement = connectionObj.prepareStatement(sqlString);
		// Adding parameters to the sqlString
		createParams(params);
		// Storing the response from the database into resultSet
		resultSet = preparedStatement.executeQuery();
		
		return resultSet;
	}

	/**
	 * updateSql is a function that sends an update query to the database.
	 * 
	 * @param	sqlString	Contains the string containing the sql query.
	 * @param	params		Contains the params that will be injected into 
	 * 						the sqlString.
	 * 
	 * @return	int			Returns the response from the server.	
	 */
	public int updateSql(String sqlString, List<Object> params) 
			throws SQLException {
		
		preparedStatement = connectionObj.prepareStatement(sqlString);
		// Adding parameters to the sqlString
		createParams(params);
		
		return preparedStatement.executeUpdate();
	}

	/**
	 * createParams is a function that adds params to the preparedStatement.
	 * 
	 * @param	params		Contains the params in form of a List object.
	 */
	private void createParams(List<Object> params) throws SQLException {
		int i = 1;
		
		// For loop traverses the params list and adds them to the preparedStatement.
		for (Object param : params) {
			if (param instanceof Integer) {
				preparedStatement.setInt(i, ((Integer) param).intValue());
			} else if (param instanceof String) {
				preparedStatement.setString(i, param.toString());
			} else if (param instanceof Date) {
				preparedStatement.setDate(i, (Date) param);
			} else if(param instanceof Float){
				preparedStatement.setFloat(i, ((Float)param).floatValue());
			} else if (param instanceof Long) {
				preparedStatement.setLong(i, ((Long) param).longValue());
			}
			i++;
		}
	}

	/**
	 * close is a function that closes the preparedStatement and resultSet
	 */
	public void close() throws SQLException {
		if (preparedStatement != null)
			preparedStatement.close();
		if (resultSet != null)
			resultSet.close();
	}
}
