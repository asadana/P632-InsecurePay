package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

/** 
 * AccountBO is POJO to store basic account information
 * for the user.
 */
@XmlRootElement
public class AccountBO {
	private int customerNumber;
    private int accountNumber;
    private float accountBalance;

    /**
     * AccountBO default constructor
     */
    public AccountBO() {}
    
    /**
     * AccountBO parameterized constructor
     * 
     * @param	customerNumber
     * @param	accountNumber
     * @param	accountBalance
     */
    public AccountBO(int customerNumber, int accountNumber, float accountBalance) {
        this.setCustomerNumber(customerNumber);
        this.setAccountNumber(accountNumber);
        this.setAccountBalance(accountBalance);   
    }

    public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }
}
