package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransactionBO {

	private String description;
    private String date;
    private float finalAmount;
    private float transactionAmount;
    private int type;
    
    
	public TransactionBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionBO(String description, String date, float finalAmount,float transactionAmount, int type) {
		super();
		this.description = description;
		this.date = date;
		this.finalAmount = finalAmount;
		this.transactionAmount = transactionAmount;
		this.type = type;
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

	public float getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(float finalAmount) {
		this.finalAmount = finalAmount;
	}

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
    
	

}
