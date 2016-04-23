package com.cigital.insecurepay.common;

/**
 * Constants class contains the constant values used in other classes 
 */
public class Constants {
	
	// AWS and Tintin version
	// JDBC details
	public final static String url = "jdbc:postgresql://tintin.cs.indiana.edu:9003/";
	public final static String dbName = "cigital_IP";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepay";
	
	// Directory on the server where files should be uploaded and stored
	public final static String fileUploadDir = "/usr/share/tomcat8/temp/uploadedFiles";
	
	/*
	// Docker Version
	// JDBC details
	public final static String url = "jdbc:postgresql://postgres:5432/";
	public final static String dbName = "insecurepaydatabase";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepassword";
	
	// Directory on the server where files should be uploaded and stored
	public final static String fileUploadDir = "/opt/tomcat/temp/uploadedFiles";
	*/
	
	// Reset password
	public final static String defaultPassword = "12345";
	
	/*
	 * cookieList stores cookies on the server side. 
	 * Object of class {@link CookieList}	
	 */
	public static CookieList cookieList = new CookieList();
	// Counter to keep track of logged in users
	public static int cookieCounter = 100;
	// Lower limit of cookieCounter, also used as a reset value
	public final static int counterInitial = 100;
	// Upper limit of cookieCounter
	public final static int counterLimit  = 1000000;
}
