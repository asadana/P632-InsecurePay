package com.application.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Connection;

/*
 * Includes core methods of creating and querying results
 */

public class BaseDao {
	protected Connection conn = null;

	protected PreparedStatement ps = null;
	protected ResultSet rs = null;

	public BaseDao(Connection conn) {
		this.conn = conn;
	}
	
	public ResultSet querySql(String sql, List<Object> params)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		ps = conn.prepareStatement(sql);
		createParams(params);
		rs = ps.executeQuery();

		return rs;
	}
	
	private void createParams(List<Object> params) throws SQLException {
		int i = 1;
		for (Object param : params) {
			if (param instanceof Integer) {
				ps.setInt(i, ((Integer) param).intValue());
			} else if (param instanceof String) {
				ps.setString(i, param.toString());
			}
			i++;
		}
	}
	
	public void close() throws SQLException {
		if (ps != null)
			ps.close();
		if (rs != null)
			rs.close();

	}

	
}
