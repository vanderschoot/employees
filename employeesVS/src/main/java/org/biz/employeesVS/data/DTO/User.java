package org.biz.employeesVS.data.DTO;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="User")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;

	private String userName;
	
	private String email;

	private String password;
	
	//private List<UserRole> userroles;

	public User() {
	}
	
	public User copy() {
		User usr = new User();
		usr.setUserId(getUserId());
		usr.setUserName(getUserName());
		usr.setEmail(getEmail());
		usr.setPassword(getPassword());
		return usr;
	}
	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//public List<UserRole> getUserRoles() {
	//	return this.userroles;
	//}

	//public void setUserRoles(List<UserRole> userroles) {
	//	this.userroles = userroles;
	//}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !User.class.isAssignableFrom(o.getClass())) {
            return false;
        }

        User usr = (User) o;

        return userId == usr.userId;
    }

    @Override
    public int hashCode() {
        return (int) (userId ^ (userId >>> 32));
    }
}