package com.application.service.BO;

import java.sql.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerBO {
	private int cust_no;
	private String cust_name;
	private String street;
	private String city;
	private String state;
	private int zipcode;
	private int phone_no;
	private Date birth_date;
	private int ssn;
	private String email;
	//private String cust_username;
	
	
	public CustomerBO(){
		
	}
	public CustomerBO(int cust_no, String cust_name, String street, String city, String state, int zipcode, int phone_no,
			Date birth_date, int ssn, String email, String cust_username) {
		super();
		this.cust_no = cust_no;
		this.cust_name = cust_name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.phone_no = phone_no;
		this.birth_date = birth_date;
		this.ssn = ssn;
		this.email = email;
		//this.cust_username = cust_username;
	}
	public int getCust_no() {
		return cust_no;
	}
	public void setCust_no(int cust_no) {
		this.cust_no = cust_no;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
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
	public int getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(int phone_no) {
		this.phone_no = phone_no;
	}
	public Date getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}
	public int getSsn() {
		return ssn;
	}
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setCity(String city){
		this.city=city;
	}
	public String getCity(){
		return city;
	}
	
	
}
