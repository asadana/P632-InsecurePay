package com.application.common;


public class Constants {
	public final static String url = "jdbc:postgresql://postgres:5432/";
	public final static String dbName = "insecurepaydatabase";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepassword";
	public final static String defaultPassword = "12345";
	public static CookieList cookieList = new CookieList();
	public static int counter = 100;
	public final static String fileUploadDir = "/opt/tomcat/temp/uploadedFiles";
	public final static int counterInitial = 100;
	public final static int counterLimit  = 1000000;
}
