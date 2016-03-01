package com.cigital.insecurepay.VOs;

import java.io.Serializable;

public class CommonVO implements Serializable {

    private String serverAddress;
    private int custNo;
    private int accountNo;
    private String username;
//    private Connectivity connectivityObj;


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getCustNo() {
        return custNo;
    }

    public void setCustNo(int custNo) {
        this.custNo = custNo;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
/*
    public Connectivity getConnectivityObj() {
        return connectivityObj;
    }

    public void setConnectivityObj(Connectivity connectivityObj) {
        this.connectivityObj = connectivityObj;
    }*/
}
