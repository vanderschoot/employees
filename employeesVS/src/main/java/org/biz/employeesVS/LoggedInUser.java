package org.biz.employeesVS;

import java.util.ArrayList;
import java.util.List;

public class LoggedInUser {

	private String username;
	private String password;
	private String email;
	private List<String> roles = new ArrayList<String>();
	
	public LoggedInUser() {
		System.out.println("LoggedInUser created");
		this.username = "";
		this.password = "";
		this.email = "";
		this.roles.clear();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		System.out.println("LoggedInUser setUsername : " + username);
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		System.out.println("LoggedInUser setPassword : " + password);
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		System.out.println("LoggedInUser setRoles" + roles);
		this.roles = roles;
	}
	
	public void clear() {
		this.username = "";
		this.password = "";
		this.email = "";
		this.roles.clear();
	}
}
