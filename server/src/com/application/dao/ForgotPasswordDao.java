package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.ForgotPasswordBO;
import com.application.service.BO.ForgotPasswordValidationBO;



public class ForgotPasswordDao extends BaseDao{

	public ForgotPasswordDao(Connection conn) {
		super(conn);
	}
	
	public ForgotPasswordValidationBO validateUser(ForgotPasswordBO l)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		ForgotPasswordValidationBO forgotPasswordValidationBO;
		
		boolean usernameExists;
		boolean validUser;
		
		int cust_no=-1;
		int cust_no_compare=-2;
		ResultSet rs = null;
		String sql = "select * from cust_credentials where cust_username='"
				+ l.getUsername() + "'";
		s = conn.createStatement();
		rs = s.executeQuery(sql);
		if (rs.next()) {
			usernameExists = true;
			close();
			
			String sql2 = "select cust_no from account where account_no="
					+ l.getAccountNo();
			s = conn.createStatement();
			rs = s.executeQuery(sql2);
			if (rs.next())
				{
				cust_no=rs.getInt("cust_no");
				}
			close();
			String sql3 = "select cust_no from customer where ssn="
					+ l.getTextSSNNo();
			s = conn.createStatement();
			rs = s.executeQuery(sql3);
			if (rs.next())
				{
				cust_no_compare=rs.getInt("cust_no");
				}
			close();	
			if(cust_no==cust_no_compare)	
				{
				validUser = true;
				List<Object> params = new ArrayList<Object>();
				int defaultPassword=12345;
				params.add(defaultPassword);
				params.add(l.getUsername());
				int count=updateSql(Queries.UPDATE_DEFAULT_PASSWORD, params);
				System.out.println("No.of rows updated are" +count);
				}
			else
				validUser = false;
			
		} else {
			usernameExists = false;
			validUser = false;
		}
		
		forgotPasswordValidationBO = new ForgotPasswordValidationBO(usernameExists, validUser);

		return forgotPasswordValidationBO;
	}
}
