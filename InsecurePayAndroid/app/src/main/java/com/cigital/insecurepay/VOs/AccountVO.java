package com.cigital.insecurepay.VOs;

import java.io.Serializable;

/**
 * AccountVO is POJO to store basic account information of the user.
 */
public class AccountVO implements Serializable{
    private int customerNumber;
    private int accountNumber;
    private float accountBalance;

    public AccountVO() {
    }

    /**
     * AccountVO parameterized constructor
     *
     * @param	customerNumber
     * @param	accountNumber
     * @param	accountBalance
     */
    
    public AccountVO(int customerNumber, int accountNumber, float accountBalance) {
        this.customerNumber = customerNumber;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;

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
