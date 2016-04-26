package com.cigital.insecurepay.VOs;

import org.joda.time.DateTime;

/**
 * LoginLockoutVO is a POJO class that is used to keep track of
 * failed user login attempts.
 */
public class LoginLockoutVO {

    // isLocaked is a flag to indicate a locked out user
    private boolean isLocked;
    // trialTime is the duration of locked timeout
    private DateTime trialTime;
    // trialCount keeps track of number of failed attempts
    private int trialCount;
    // addUser is a flag to indicate if a new user is being used
    private boolean addUser;
    /**
     * loginValidationVO is used to validate the login
     * Object of {@link LoginValidationVO}
     */
    private LoginValidationVO loginValidationVO;

    public boolean isAddUser() {
        return addUser;
    }

    public void setAddUser(boolean addUser) {
        this.addUser = addUser;
    }

    public int getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(int trialCount) {
        this.trialCount = trialCount;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public DateTime getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(DateTime trialTime) {
        this.trialTime = trialTime;
    }

    public LoginValidationVO getLoginValidationVO() {
        return loginValidationVO;
    }

    public void setLoginValidationVO(LoginValidationVO loginValidationVO) {
        this.loginValidationVO = loginValidationVO;
    }
}
