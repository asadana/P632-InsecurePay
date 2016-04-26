package com.cigital.insecurepay.service.BO;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/** 
 * CustomerBO is POJO to store all account information
 * from account management for the user.
 */
@XmlRootElement
public class CustomerBO {
	private int customerNumber;
	private String customerName;
	private String street;
	private String city;
	private String state;
	private int zipcode;
	private long phoneNo;
	private Date birthDate;
	private String ssn;
	private String email;
	
	/**
	 * CustomerBO default constructor
	 */
	public CustomerBO(){}
	
	/**
	 * CustomerBO parameterized constructor
	 * 
	 * @param	customerNumber
	 * @param	customerName
	 * @param	street
	 * @param	city
	 * @param	state
	 * @param	zipcode
	 * @param	phoneNo
	 * @param	birthDate
	 * @param	ssn
	 * @param	email
	 */
	public CustomerBO(int customerNumber, String customerName, String street, 
						String city, String state, int zipcode, long phoneNo,
						Date birthDate, String ssn, String email) {
		this.setCustomerNumber(customerNumber);
		this.setCustomerName(customerName);
		this.setStreet(street);
		this.setCity(city);
		this.setState(state);
		this.setZipcode(zipcode);
		this.setPhoneNo(phoneNo);
		this.setBirthDate(birthDate);
		this.setSsn(ssn);
		this.setEmail(email);
	}
	
	public int getCustomerNumber() {
		return customerNumber;
	}
	
	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public int getZipcode() {
		return zipcode;
	}
	
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	
	public long getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getSsn() {
		return ssn;
	}
	
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
