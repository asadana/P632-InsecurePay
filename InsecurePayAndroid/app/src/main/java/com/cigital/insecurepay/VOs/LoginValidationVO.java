package com.cigital.insecurepay.VOs;

/**
 * LoginValidationVO is a POJO used to verify if the user is valid or not, username exists and the respective customer number
 */
public class LoginValidationVO {

    private boolean usernameExists;
    private boolean validUser;
    private int customerNumber;

    /**
     * LoginValidationVO default constructor.
     */
    public LoginValidationVO() {}

    /**
     * LoginValidationVO parameterized constructor.
     *
     * @param usernameExists
     * @param validUser
     */
    public LoginValidationVO(boolean usernameExists, boolean validUser,int customerNumber) {
        super();
        this.usernameExists = usernameExists;
        this.validUser = validUser;
        this.customerNumber=customerNumber;
    }

    public boolean isUsernameExists() {
        return usernameExists;
    }

    public void setUsernameExists(boolean usernameExists) {
        this.usernameExists = usernameExists;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }
}
