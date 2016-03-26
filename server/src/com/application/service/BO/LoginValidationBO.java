package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginValidationBO {

	private boolean usernameExists;
	private boolean validUser;
	private int custNo;

	public LoginValidationBO() {}

	public LoginValidationBO(boolean usernameExists, boolean validUser, int custNo) {
		super();
		this.usernameExists = usernameExists;
		this.validUser = validUser;
		this.custNo = custNo;
	}

	public boolean isUsernameExists() {
		return usernameExists;
	}

	public void setUsernameExists(boolean usernameExists) {
		this.usernameExists = usernameExists;
	}

	public boolean isValidUser() {
		return validUser;
	}

	public void setValidUser(boolean validUser) {
		this.validUser = validUser;
	}

	public int getCustNo() {
		return custNo;
	}

	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	

}
