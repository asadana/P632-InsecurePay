package com.cigital.insecurepay.VOs;

import com.cigital.insecurepay.common.CustomDecoder;

/**
 * CustomerVO is a POJO used to store customer details
 */
public class CustomerVO {
    private int customerNumber;
    private String customerName;
    private String street;
    private String city;
    private String state;
    private int zipcode;
    private long phoneNo;
    private String birthDate;
    private String ssn;
    private String email;
    private String customerUsername;

    /**
     * CustomerVO parameterized constructor.
     * @param custNo
     * @param custName
     * @param street
     * @param city
     * @param state
     * @param zipcode
     * @param phoneNo
     * @param birthDate
     * @param ssn
     * @param email
     * @param custUsername
     */
    public CustomerVO(int custNo, String custName, String street, String city, String state, int zipcode, long phoneNo,
                      String birthDate, String ssn, String email, String custUsername) {
        super();
        this.customerNumber = custNo;
        this.customerName = custName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNo = phoneNo;
        this.birthDate = birthDate;
        this.ssn = ssn;
        this.email = email;
        this.customerUsername = custUsername;
    }

    public String getDecodedSsn() {
        return CustomDecoder.decode(getSsn());
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }
}

