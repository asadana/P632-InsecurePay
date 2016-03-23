package com.cigital.insecurepay.VOs;

import java.io.Serializable;

public class TransactionVO implements Serializable {

    private String description;
    private String date;
    private int finalAmount;
    private int transactionAmount;
    private int type;

    public TransactionVO(String description, String date, int finalAmount, int transactionAmount, int type) {
        this.type = type;
        this.transactionAmount = transactionAmount;
        this.description = description;
        this.date = date;
        this.finalAmount = finalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
