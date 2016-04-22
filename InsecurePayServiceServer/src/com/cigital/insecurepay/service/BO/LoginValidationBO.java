package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

/** 
 * LoginValidationBO is POJO to validate the user 
 * for the user.
 */
@XmlRootElement
public class LoginValidationBO {

	private boolean usernameExists;
	private boolean validUser;
	private int customerNumber;

	/**
     * LoginValidationBO default constructor
     */
	public LoginValidationBO() {}

    /**
     * LoginValidationBO parameterized constructor
     * 
     * @param	usernameExists
     * @param	validUser
     * @param	customerNumber
     */	
	public LoginValidationBO (boolean usernameExists, 
								boolean validUser, int customerNumber) {
		super();
		this.setUsernameExists(usernameExists);
		this.setValidUser(validUser);
		this.setCustomerNumber(customerNumber);
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

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	

}
