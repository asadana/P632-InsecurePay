package com.application.common;

/*
 * Query constants
 */

public class Queries {
	public static final String VALIDATE_USER = "select * from cust_credentials where cust_username=? and password=?";
}
