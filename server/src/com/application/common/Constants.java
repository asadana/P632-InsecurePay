package com.application.common;


public class Constants {
	public final static String url = "jdbc:postgresql://tintin.cs.indiana.edu:9003/";
	public final static String dbName = "cigital_IP";
	public final static String driver = "org.postgresql.Driver";
	public final static String userName = "insecurepay";
	public final static String password = "insecurepay";
	public final static String defaultPassword = "12345";
	public static CookieList cookieList = new CookieList();
	public static int counter = 100;
	public final static int counterInitial = 100;
	public final static int counterLimit  = 1000000;
}