package com.cigital.insecurepay.VOs;

import java.io.Serializable;

public class AccountVO implements Serializable{
    private int custNo;
    private int accNo;
    private float accountBalance;


    public AccountVO(int custNo, int accNo, float accountBalance) {
        this.custNo = custNo;
        this.accNo = accNo;
        this.accountBalance = accountBalance;

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

}
