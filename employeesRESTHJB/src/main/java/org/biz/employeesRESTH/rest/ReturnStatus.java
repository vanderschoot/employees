package org.biz.employeesRESTH.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReturnStatus {    	
    private boolean success;
    private String message;
    
	public ReturnStatus(boolean success) {
		this.success = success;
		this.message = "";
	}

	public ReturnStatus(boolean success, String mess) {
		this.success = success;
		this.message = mess;
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
}