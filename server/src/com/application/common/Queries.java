package com.application.common;

/*
 * Query constants
 */

public class Queries {
	public static final String GET_CUSTOMER_DETAILS = "select * from customer where cust_username = ?";
	public static final String UPDATE_DEFAULT_PASSWORD = "update cust_credentials set password=? where cust_username = ?";
}
