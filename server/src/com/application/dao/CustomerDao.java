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

	public CustomerBO getCustomerDetails(int custNo) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		ResultSet rs = null;
		List<Object> params = new ArrayList<Object>();
		params.add(custNo);
		rs = querySql(Queries.GET_CUSTOMER_DETAILS, params);
		CustomerBO customer = new CustomerBO();
		if (rs.next()) {
			customer.setCustNo(rs.getInt("cust_no"));
			customer.setCustName(rs.getString("cust_name"));
			customer.setStreet(rs.getString("street"));
			customer.setCity(rs.getString("city"));
			customer.setState(rs.getString("state"));
			customer.setZipcode(rs.getInt("zipcode"));
			customer.setPhoneNo(rs.getInt("phone_no"));
			customer.setBirthDate(rs.getDate("Birth_date"));
			customer.setSsn(rs.getString("ssn"));
			customer.setEmail(rs.getString("email"));
		}
		return customer;
	}

	public boolean updateCustomerDetails(CustomerBO customerBO)
			throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		List<Object> params = new ArrayList<Object>();
		params.add(customerBO.getCustName());
		params.add(customerBO.getStreet());
		params.add(customerBO.getCity());
		params.add(customerBO.getState());
		params.add(customerBO.getZipcode());
		params.add(customerBO.getPhoneNo());
		params.add(customerBO.getEmail());
		params.add(customerBO.getBirthDate());
		params.add(customerBO.getCustNo());
		
		if (updateSql(Queries.UPDATE_CUSTOMER_DETAILS, params) > 0) {
			return true;
		}
		return false;

	}

}
