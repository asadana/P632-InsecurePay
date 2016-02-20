package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForgotPasswordBO {
	
	private String accountNo;
    private String textSSNNo;
    private String username;

    public ForgotPasswordBO() {
	}
    
    public ForgotPasswordBO(String accountNo, String textSSNNo, String username){
    	super();
        this.accountNo = accountNo;
        this.textSSNNo = textSSNNo;
        this.username=username;
    }

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getTextSSNNo() {
		return textSSNNo;
	}

	public void setTextSSNNo(String textSSNNo) {
		this.textSSNNo = textSSNNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
 
}
