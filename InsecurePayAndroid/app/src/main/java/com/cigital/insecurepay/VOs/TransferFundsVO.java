package com.cigital.insecurepay.VOs;

import java.io.Serializable;

public class TransferFundsVO implements Serializable {

    private AccountVO fromAccount;
    private AccountVO toAccount;
    private float transferAmount;
    private String transferDetails;

    public TransferFundsVO(AccountVO fromAccount, AccountVO toAccount, float transferAmount, String transferDetails) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.transferDetails = transferDetails;
    }

    public TransferFundsVO(){

    }

    public AccountVO getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(AccountVO fromAccount) {
        this.fromAccount = fromAccount;
    }

    public AccountVO getToAccount() {
        return toAccount;
    }

    public void setToAccount(AccountVO toAccount) {
        this.toAccount = toAccount;
    }

    public float getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(float transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferDetails() {
        return transferDetails;
    }

    public void setTransferDetails(String transferDetails) {
        this.transferDetails = transferDetails;
    }
}
