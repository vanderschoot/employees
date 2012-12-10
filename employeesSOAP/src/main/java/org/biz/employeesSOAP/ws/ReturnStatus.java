package org.biz.employeesSOAP.ws;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReturnStatus {    	
    private boolean success;
    private String message;
    
	public ReturnStatus() {
		this.success = true;
		this.message = "";
	}

	public ReturnStatus(String message) {
		this.success = true;
		this.message = message;
	}

	public ReturnStatus(boolean success) {
		this.success = success;
		this.message = "";
	}
	
	public ReturnStatus(boolean success, String message) {
		this.success = success;
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
}