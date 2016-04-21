package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferFundsBO {
	
	private AccountBO fromAccount;
    private AccountBO toAccount;
    private float transferAmount;
    private String transferDetails;

    public TransferFundsBO(AccountBO fromAccount, AccountBO toAccount, float transferAmount, String transferDetails) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.transferDetails = transferDetails;
    }

    public TransferFundsBO(){

    }

    public AccountBO getFromAccount() {
        return fromAccount;
    }

    public void setFromAccountBO(AccountBO fromAccount) {
        this.fromAccount = fromAccount;
    }

    public AccountBO getToAccount() {
        return toAccount;
    }

    public void setToAccountBO(AccountBO toAccount) {
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
