package com.application.service.BO;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountBO {
	private int custNo;
    private int accNo;
    private float accountBalance;
    private Date accountOpenDate;

    public AccountBO(int custNo, int accNo, float accountBalance, Date accountOpenDate) {
        this.custNo = custNo;
        this.accNo = accNo;
        this.accountBalance = accountBalance;
        this.accountOpenDate = accountOpenDate;
    }

    public AccountBO() {}

	public int getCustNo() {
        return custNo;
    }

    public void setCustNo(int custNo) {
        this.custNo = custNo;
    }

    public int getAccNo() {
        return accNo;
    }

    public void setAccNo(int accNo) {
        this.accNo = accNo;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }
    
	
	
}
