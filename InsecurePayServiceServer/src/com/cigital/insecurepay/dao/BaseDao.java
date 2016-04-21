package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*
 * Includes core methods of creating and querying results
 */

public class BaseDao {
	protected Connection conn = null;

	protected PreparedStatement ps = null;
	protected Statement s = null;
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

	public int updateSql(String sql, List<Object> params) throws SQLException {
		ps = conn.prepareStatement(sql);
		createParams(params);
		return ps.executeUpdate();
	}

	private void createParams(List<Object> params) throws SQLException {
		int i = 1;
		for (Object param : params) {
			if (param instanceof Integer) {
				ps.setInt(i, ((Integer) param).intValue());
			} else if (param instanceof String) {
				ps.setString(i, param.toString());
			} else if (param instanceof Date) {
				ps.setDate(i, (Date) param);
			} else if(param instanceof Float){
				ps.setFloat(i, ((Float)param).floatValue());
			} else if (param instanceof Long) {
				ps.setLong(i, ((Long) param).longValue());
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
