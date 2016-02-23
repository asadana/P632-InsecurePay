package com.cigital.insecurepay.VOs;

import com.cigital.insecurepay.common.CustomEncoder;

import java.sql.Date;

public class CustomerVO {
    private int custNo;
    private String custName;
    private String street;
    private String city;
    private String state;
    private int zipcode;
    private int phoneNo;
    private Date birthDate;
    private String ssn;
    private String email;
    private String custUsername;


    public CustomerVO() {

    }

    public CustomerVO(int custNo, String custName, String street, String city, String state, int zipcode, int phoneNo,
                      Date birthDate, String ssn, String email, String custUsername) {
        super();
        this.custNo = custNo;
        this.custName = custName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNo = phoneNo;
        this.birthDate = birthDate;
        this.ssn = ssn;
        this.email = email;
        this.custUsername = custUsername;
    }

    public int getCustNo() {
        return custNo;
    }

    public void setCustNo(int custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
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

    public String getDecodedSsn() {
        return CustomEncoder.decode(getSsn());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustUsername() {
        return custUsername;
    }

    public void setCustUsername(String custUsername) {
        this.custUsername = custUsername;
    }


}

