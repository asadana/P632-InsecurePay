package com.application.service.BO;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountBO {
	private int accno;
	private float accountBalance;
	private Date accountOpenDate;
	
	public AccountBO(){
		
	}
	public AccountBO(int accno, int custno, float accountbalance, Date accountOpenDate){
		super();
		this.accno = accno;
		this.accountBalance = accountbalance;
		this.accountOpenDate = accountOpenDate;
	}
	public int getAccno() {
		return accno;
	}
	public void setAccno(int accno) {
		this.accno = accno;
	}
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}
	public Date getAccountOpenDate() {
		return accountOpenDate;
	}
	public void setAccountOpenDate(Date accountOpenDate) {
		this.accountOpenDate = accountOpenDate;
	}
	
	
}
