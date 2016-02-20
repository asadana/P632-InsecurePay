package com.cigital.insecurepay.VOs;

public class ForgotPasswordVO {

    private int accountNo;
    private int sSNNo;
    private String username;

    public ForgotPasswordVO(int accountNo, int sSNNo, String username){
        this.accountNo = accountNo;
        this.sSNNo = sSNNo;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getsSNNo() {
        return sSNNo;
    }

    public void setsSNNo(int sSNNo) {
        this.sSNNo = sSNNo;
    }
}
