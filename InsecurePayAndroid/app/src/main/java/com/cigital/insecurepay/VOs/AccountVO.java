package com.cigital.insecurepay.VOs;

import java.util.Date;

/**
 * Created by Amish on 21-02-2016.
 */
public class AccountVO {
    private int custNo;
    private int accNo;
    private float accountBalance;
    private Date accountOpenDate;

    public AccountVO(int custNo, int accNo, float accountBalance, Date accountOpenDate) {
        this.custNo = custNo;
        this.accNo = accNo;
        this.accountBalance = accountBalance;
        this.accountOpenDate = accountOpenDate;
    }

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
