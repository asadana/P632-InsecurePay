package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

/** 
 * ChangePasswordBO is POJO to store username and password
 * for the user.
 */
@XmlRootElement
public class ChangePasswordBO {

    private String username;
    private String password;

    /**
     * ChangePasswordBO default constructor
     */
    public ChangePasswordBO() {}
    
    /**
     * ChangePasswordBO parameterized constructor
     * 
     * @param	username
     * @param	password
     */
    public ChangePasswordBO(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
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
