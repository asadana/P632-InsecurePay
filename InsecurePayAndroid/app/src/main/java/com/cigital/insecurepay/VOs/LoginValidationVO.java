package com.cigital.insecurepay.VOs;


public class LoginValidationVO {

    private boolean usernameExists;
    private boolean validUser;

    public LoginValidationVO() {}

    public LoginValidationVO(boolean usernameExists, boolean validUser) {
        super();
        this.usernameExists = usernameExists;
        this.validUser = validUser;
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

}
