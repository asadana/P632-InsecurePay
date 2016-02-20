package com.application.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForgotPasswordBO {
	
	private int accountNo;
    private int sSNNo;
    private String username;

    public ForgotPasswordBO() {
	}
    
    public ForgotPasswordBO(int accountNo, int sSNNo, String username){
    	super();
        this.accountNo = accountNo;
        this.sSNNo = sSNNo;
        this.username=username;
    }

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}


	public int getsSNNo() {
		return sSNNo;
	}

	public void setsSNNo(int sSNNo) {
		this.sSNNo = sSNNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
 
}
