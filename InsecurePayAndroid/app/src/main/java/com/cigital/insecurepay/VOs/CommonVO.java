package com.cigital.insecurepay.VOs;

import java.io.Serializable;

/**
 * CommonVO is a POJO used to store username,account details,customer number and server address which are required often
 */
public class CommonVO implements Serializable {

    private String serverAddress;
    private int customerNumber;
    private AccountVO accountVO;
    private String username;

    /**
     * CommonVO parameterized constructor.
     * @param serverAddress
     * @param customerNumber
     * @param accountVO
     * @param username
     */
    public CommonVO(String serverAddress, int customerNumber, AccountVO accountVO, String username) {
        this.serverAddress = serverAddress;
        this.customerNumber = customerNumber;
        this.accountVO = accountVO;
        this.username = username;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public AccountVO getAccountVO() {
        return accountVO;
    }

    public void setAccountVO(AccountVO accountVO) {
        this.accountVO = accountVO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
