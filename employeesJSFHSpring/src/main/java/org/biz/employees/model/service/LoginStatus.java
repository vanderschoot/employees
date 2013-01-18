package org.biz.employees.model.service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginStatus {    	
    private boolean success;
	private String userName;
    private String roles;
    private String message;
    	
	public LoginStatus(boolean success, String userName, String roles, String message) {
		this.success = success;
		this.message = message;
		this.userName = userName;
		this.roles = roles;
		this.message = message;
	}
		
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSucces() {
		return success;
	}	
	
	public void setSucces(boolean success) {
		this.success = success;
	}	   
	
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
}