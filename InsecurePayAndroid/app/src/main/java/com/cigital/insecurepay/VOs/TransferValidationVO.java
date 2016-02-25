package com.cigital.insecurepay.VOs;

/**
 * Created by Jaini on 2/25/2016.
 */
public class TransferValidationVO {

    private boolean usernameExists;
    private int custNo;


    public TransferValidationVO() {}

    public TransferValidationVO(boolean usernameExists, int custNo) {
        super();
        this.usernameExists = usernameExists;
        this.custNo=custNo;
    }

    public boolean isUsernameExists() {
        return usernameExists;
    }

    public void setUsernameExists(boolean usernameExists) {
        this.usernameExists = usernameExists;
    }

    public int getCustNo() {
        return custNo;
    }

    public void setCustNo(int custNo) {
        this.custNo = custNo;
    }
}
