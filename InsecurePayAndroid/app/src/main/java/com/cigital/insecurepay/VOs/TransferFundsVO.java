package com.cigital.insecurepay.VOs;

import com.cigital.insecurepay.fragments.TransferFragment;

import java.util.Date;

/**
 * Created by madhu on 24-02-2016.
 */
public class TransferFundsVO {

    private int CustomerNumber;
    private int AccountNumber;
    private int TransferID;
    private int toAccountNumber;
    private int toCustomerNumber;
    private float TransferAmount;
    private Date Timestamp;
    private float frombeforeAmount;
    private float fromafterAmount;
    private float tobeforeAmount;
    private float toafterAmount;

    public String getTransferDetails() {
        return TransferDetails;
    }

    public void setTransferDetails(String transferDetails) {
        TransferDetails = transferDetails;
    }

    public int getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        CustomerNumber = customerNumber;
    }

    public int getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        AccountNumber = accountNumber;
    }

    public int getTransferID() {
        return TransferID;
    }

    public void setTransferID(int transferID) {
        TransferID = transferID;
    }

    public int getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(int toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public int getToCustomerNumber() {
        return toCustomerNumber;
    }

    public void setToCustomerNumber(int toCustomerNumber) {
        this.toCustomerNumber = toCustomerNumber;
    }

    public float getTransferAmount() {
        return TransferAmount;
    }

    public void setTransferAmount(float transferAmount) {
        TransferAmount = transferAmount;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }

    public float getFrombeforeAmount() {
        return frombeforeAmount;
    }

    public void setFrombeforeAmount(float frombeforeAmount) {
        this.frombeforeAmount = frombeforeAmount;
    }

    public float getFromafterAmount() {
        return fromafterAmount;
    }

    public void setFromafterAmount(float fromafterAmount) {
        this.fromafterAmount = fromafterAmount;
    }

    public float getTobeforeAmount() {
        return tobeforeAmount;
    }

    public void setTobeforeAmount(float tobeforeAmount) {
        this.tobeforeAmount = tobeforeAmount;
    }

    public float getToafterAmount() {
        return toafterAmount;
    }

    public void setToafterAmount(float toafterAmount) {
        this.toafterAmount = toafterAmount;
    }

    private String TransferDetails;


    public TransferFundsVO(int customerNumber, int accountNumber, int transferID, int toAccountNumber, int toCustomerNumber, float transferAmount,
                           Date timestamp, float frombeforeAmount, float fromafterAmount, float tobeforeAmount, float toafterAmount, String transferDetails) {
        this.CustomerNumber = customerNumber;
        this.AccountNumber = accountNumber;
        this.TransferID = transferID;
        this.toAccountNumber = toAccountNumber;
        this.toCustomerNumber = toCustomerNumber;
        this.TransferAmount = transferAmount;
        this.Timestamp = timestamp;
        this.frombeforeAmount = frombeforeAmount;
        this.fromafterAmount = fromafterAmount;
        this.tobeforeAmount = tobeforeAmount;
        this.toafterAmount = toafterAmount;
        this.TransferDetails = transferDetails;
    }
}
