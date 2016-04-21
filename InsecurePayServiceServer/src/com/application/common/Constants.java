package com.application.common;


public class Constants {
	
	// AWS and Tintin version
	public final static String url = "jdbc:postgresql://tintin.cs.indiana.edu:9003/";
	public final static String dbName = "cigital_IP";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepay";
	public final static String defaultPassword = "12345";
	public final static String fileUploadDir = "/usr/share/tomcat8/temp/uploadedFiles";
	
	/*
	// Docker Version
	public final static String url = "jdbc:postgresql://postgres:5432/";
	public final static String dbName = "insecurepaydatabase";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepassword";
	public final static String defaultPassword = "12345";
	public final static String fileUploadDir = "/opt/tomcat/temp/uploadedFiles";
	*/
	
	// List to store cookies on the server side
	public static CookieList cookieList = new CookieList();
	// Counter to keep track of logged in users
	public static int counter = 100;
	// Lower limit of counter, also used as a reset value
	public final static int counterInitial = 100;
	// Upper limit of counter
	public final static int counterLimit  = 1000000;
}
