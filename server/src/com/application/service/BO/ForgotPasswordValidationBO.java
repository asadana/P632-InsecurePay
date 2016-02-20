package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForgotPasswordValidationBO {
	private boolean usernameExists;
	private boolean validUser;


	public ForgotPasswordValidationBO() {}

	public ForgotPasswordValidationBO(boolean usernameExists, boolean validUser) {
		super();
		this.usernameExists = usernameExists;
		this.validUser = validUser;
		
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
}
