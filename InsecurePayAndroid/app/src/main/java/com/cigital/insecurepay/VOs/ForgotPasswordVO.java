package com.cigital.insecurepay.VOs;

/**
 * Created by Jaini on 2/19/2016.
 */
public class ForgotPasswordVO {

    private String accountNo;
    private String textSSNNo;
    private String username;

    public ForgotPasswordVO(String accountNo, String textSSNNo, String username){
        this.accountNo = this.accountNo;
        this.textSSNNo = textSSNNo;
        this.username=username;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSSNNo() {
        return textSSNNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSSNNo(String textSSNNo) {
        this.textSSNNo = textSSNNo;
    }

}
