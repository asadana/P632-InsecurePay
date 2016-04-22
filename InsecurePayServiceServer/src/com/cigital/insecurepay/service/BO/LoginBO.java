package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;


/** 
 * LoginBO is POJO to store the user credentials
 * for the user.
 */
@XmlRootElement
public class LoginBO {
	private String username;
	private String password;

	/**
     * LoginBO default constructor
     */
	public LoginBO() {
	}

	/**
     * LoginBO parameterized constructor
     * 
     * @param	username
     * @param	password
     */
	public LoginBO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
