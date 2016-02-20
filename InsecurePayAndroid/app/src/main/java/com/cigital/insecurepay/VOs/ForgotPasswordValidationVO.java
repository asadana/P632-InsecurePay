package com.cigital.insecurepay.VOs;

/**
 * Created by Jaini on 2/19/2016.
 */
public class ForgotPasswordValidationVO {

    private boolean usernameExists;
    private boolean validUser;


    public ForgotPasswordValidationVO() {}

    public ForgotPasswordValidationVO(boolean usernameExists, boolean validUser) {
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
