package com.cigital.insecurepay.VOs;


import org.joda.time.DateTime;

public class LoginLockoutVO {

    private boolean isLocked;
    private DateTime trialTime;
    private LoginValidationVO loginValidationVO;
    private int trialCount;
    private boolean addUser;


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
