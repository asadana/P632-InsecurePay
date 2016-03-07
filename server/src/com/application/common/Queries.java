package com.application.common;

/*
 * Query constants
 */

public class Queries {
	public static final String GET_CUSTOMER_DETAILS = "select * from customer where cust_no = ?";
	public static final String UPDATE_PASSWORD = "update cust_credentials set password=? where cust_username = ?";
	public static final String USERNAME_EXISTS = "select * from cust_credentials where cust_username=?";
	public static final String GET_CUSTNO_ACCOUNT_TBL = "select cust_no from account where account_no=?";
	public static final String GET_CUSTNO_CUST_TBL = "select cust_no from customer where ssn=?";
	public static final String UPDATE_CUSTOMER_DETAILS = "update customer set cust_name=?, street=?, city=?, state=?, zipcode=?, phone_no=?,email=?, birth_date=? where cust_no = ?";
	public static final String GET_ACCOUNT_TBL = "select * from account where cust_no=?";
	public static final String INSERT_TRANSFER_TBL="INSERT INTO transfer_funds(from_account_no, from_cust_no, to_account_no, to_cust_no, transfer_amount, from_beforeamount, from_afteramount, to_beforeamount, to_afteramount, transfer_details) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_ACCOUNT_BALANCE = "update account set account_balance=? where account_no = ?";
}
