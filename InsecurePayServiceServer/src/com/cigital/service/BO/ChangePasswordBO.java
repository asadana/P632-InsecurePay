package com.cigital.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChangePasswordBO {

    private String username;
    private String password;

    public ChangePasswordBO() {
	}
    
    public ChangePasswordBO(String username,String password){
    	super();
        this.username=username;
        this.password=password;
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
