package com.cigital.insecurepay.common;

/**
 * Queries is a class that contains constant String variables
 * that are used to send queries to the database.
 */
public class Queries {
	
	public static final String ATTEMPT_LOGIN = "select * from cust_credentials where"
			+ " cust_username=? and password=?";
	
	public static final String GET_CUSTOMER_DETAILS = "select * from customer where cust_no=?";
	
	public static final String UPDATE_DEFAULT_PASSWORD = "update cust_credentials set password=? "
			+ "where cust_username = ?";
	
	public static final String UPDATE_PASSWORD = "update cust_credentials set password=? where "
			+ "cust_username = ?";
	
	public static final String USERNAME_EXISTS = "select * from cust_credentials where "
			+ "cust_username=?";
	
	public static final String GET_ACCOUNT_TBL_WITH_ACCNO = "select * from account where "
			+ "account_no=?";
	
	public static final String GET_CUSTNO_ACCOUNT_TBL = "select cust_no from account where "
			+ "account_no=?";
	
	public static final String GET_CUSTNO_CUST_TBL = "select cust_no from customer where ssn=?";
	
	public static final String UPDATE_CUSTOMER_DETAILS = "update customer set cust_name=?, "
			+ "street=?, city=?, state=?, zipcode=?, phone_no=?,email=?, birth_date=? where cust_no = ?";
	
	public static final String GET_ACCOUNT_TBL = "select * from account where cust_no=?";
	
	public static final String INSERT_TRANSFER_TBL="INSERT INTO transfer_funds(from_account_no, "
			+ "from_cust_no, to_account_no, to_cust_no, transfer_amount, from_beforeamount, "
			+ "from_afteramount, to_beforeamount, to_afteramount, transfer_details) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String UPDATE_ACCOUNT_BALANCE = "update account set account_balance=? "
			+ "where account_no = ?";
	
	public static final String GET_ACTIVITY_HISTORY = "select b.type,b.transferid,b.transfer_details,"
			+ "b.transfer_date::timestamp::date,b.final_amount,b.transfer_amount from (select 1 as "
			+ "type,a.transferid,a.transfer_details,a.transfer_date,a.final_amount,a.transfer_amount "
			+ "from(select transferid, transfer_details, transfer_date, from_afteramount as final_amount,"
			+ "transfer_amount from transfer_funds where from_account_no=?)a union select 2 as type,"
			+ "a.transferid,a.transfer_details,a.transfer_date,a.final_amount,a.transfer_amount "
			+ "from(select transferid, transfer_details, transfer_date, to_afteramount as final_amount,"
			+ "transfer_amount from transfer_funds where to_account_no=?)a) b order by b.transfer_date desc";
}
