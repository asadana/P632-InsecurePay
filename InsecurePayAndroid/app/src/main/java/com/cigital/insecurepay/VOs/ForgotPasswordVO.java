package com.cigital.insecurepay.VOs;


/**
 * ForgotPasswordVO is POJO to store details sent by the user
 * for ForgotPasswordService.
 */
public class ForgotPasswordVO {

    private int accountNumber;
    private String ssnNumber;
    private String username;

    /**
     * ForgotPasswordVO default constructor.
     */
    public ForgotPasswordVO() {
    }

    /**
     * ForgotPasswordBO parameterized constructor.
     *
     * @param	accountNumber
     * @param	ssnNumber
     * @param	username
     */
    public ForgotPasswordVO(int accountNumber, String ssnNumber, String username) {
        this.accountNumber = accountNumber;
        this.ssnNumber = ssnNumber;
        this.username=username;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSsnNumber() {
        return ssnNumber;
    }

    public void setSsnNumber(String ssnNumber) {
        this.ssnNumber = ssnNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
