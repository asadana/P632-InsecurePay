package com.application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Queries;
import com.application.service.BO.CustomerBO;

public class CustomerDao extends BaseDao {

	public CustomerDao(Connection conn) {
		super(conn);
	}

	public CustomerBO getCustomerDetails(String username) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		rs = querySql(Queries.GET_CUSTOMER_DETAILS, params);
		CustomerBO customer = new CustomerBO();
		if (rs.next()) {
			customer.setCust_no(rs.getInt("cust_no"));
			customer.setCust_name(rs.getString("cust_name"));
			customer.setStreet(rs.getString("street"));
			customer.setCity(rs.getString("city"));
			customer.setState(rs.getString("state"));
			customer.setZipcode(rs.getInt("zipcode"));
			customer.setPhone_no(rs.getInt("phone_no"));
			customer.setBirth_date(rs.getDate("Birth_date"));
			customer.setSsn(rs.getInt("SSN"));
			customer.setEmail(rs.getString("email"));
		}
		return customer;
	}

}
