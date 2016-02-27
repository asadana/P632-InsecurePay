package com.cigital.insecurepay.VOs;

import com.cigital.insecurepay.fragments.TransferFragment;

import java.util.Date;


public class TransferFundsVO extends TransferValidationVO {

    private int fromCustNo;
    private int fromAccountNo;
    private int toCustNo;
    private float transferAmount;
    private String transferDetails;



    public TransferFundsVO(int fromAccountNo,int fromCustNo,int toCustNo,float transferAmount,String transferDetails) {
        this.fromCustNo = fromCustNo;
        this.fromAccountNo = fromAccountNo;
        this.toCustNo = toCustNo;
        this.transferAmount = transferAmount;
        this.transferDetails = transferDetails;
    }

    public int getFromCustNo() {
        return fromCustNo;
    }

    public void setFromCustNo(int fromCustNo) {
        this.fromCustNo = fromCustNo;
    }

    public int getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(int fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public int getToCustNo() {
        return toCustNo;
    }

    public void setToCustNo(int toCustNo) {
        this.toCustNo = toCustNo;
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
