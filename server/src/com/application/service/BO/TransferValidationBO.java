package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferValidationBO {

	private boolean usernameExists;
	private int custNo;

	public TransferValidationBO() {}

	public TransferValidationBO(boolean usernameExists,int custNo) {
		super();
		this.usernameExists = usernameExists;
		this.custNo = custNo;
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
