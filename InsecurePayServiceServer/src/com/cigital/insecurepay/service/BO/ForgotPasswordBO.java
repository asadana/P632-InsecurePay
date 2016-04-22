package com.cigital.insecurepay.service.BO;

import javax.xml.bind.annotation.XmlRootElement;

import com.cigital.insecurepay.common.CustomEncoder;

/** 
 * ForgotPasswordBO is POJO to store details sent by the user
 * for ForgotPasswordService.
 */
@XmlRootElement
public class ForgotPasswordBO {
	
	private int accountNumber;
    private String ssnNumber;
    private String username;

    /**
     * ForgotPasswordBO default constructor.
     */
    public ForgotPasswordBO() {}
    
    /**
     * ForgotPasswordBO parameterized constructor.
     * 
     * @param	accountNumber
     * @param	ssnNumber
     * @param	username
     */
    public ForgotPasswordBO(int accountNumber, String ssnNumber, 
    						String username){
        this.setAccountNumber(accountNumber);
        this.setSSNNumber(ssnNumber);
        this.setUsername(username);
    }

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSSNNumber() {
		return ssnNumber;
	}

	public void setSSNNumber(String ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEncodedSSNNumber(){
		return CustomEncoder.encode(getSSNNumber());
	}
    
 
}
