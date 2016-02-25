package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferFundsBO {
	
	private int fromCustNo;
    private int fromAccountNo;
    private int toCustNo;
    private int toAccountNo;
    private float transferAmount;
    private String transferDetails;
    
    public TransferFundsBO() {}
    
	public TransferFundsBO( int fromAccountNo,int fromCustNo, int toCustNo, int toAccountNo, float transferAmount,String transferDetails) {
		super();
		this.fromCustNo = fromCustNo;
		this.fromAccountNo = fromAccountNo;
		this.toCustNo = toCustNo;
		this.toAccountNo = toAccountNo;
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

	public int getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(int toAccountNo) {
		this.toAccountNo = toAccountNo;
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
