package com.cigital.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

import com.cigital.common.CustomEncoder;

@XmlRootElement
public class ForgotPasswordBO {
	
	private int accountNo;
    private String sSNNo;
    private String username;

    public ForgotPasswordBO() {
	}
    
    public ForgotPasswordBO(int accountNo, String sSNNo, String username){
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

	public String getsSNNo() {
		return sSNNo;
	}

	public void setsSNNo(String sSNNo) {
		this.sSNNo = sSNNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEncodedSSNNo(){
		return CustomEncoder.encode(getsSNNo());
	}
    
 
}
