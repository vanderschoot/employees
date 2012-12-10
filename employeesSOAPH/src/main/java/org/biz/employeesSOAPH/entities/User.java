package org.biz.employeesSOAPH.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@XmlRootElement(name="rows")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userId;

	@Lob
	private String userName;
	
	@Lob
	private String email;

	@Lob
	private String password;
	
	//bi-directional many-to-one association to UserRole
	@OneToMany(mappedBy="user")
	private List<UserRole> userroles;

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

	public List<UserRole> getUserRoles() {
		return this.userroles;
	}

	public void setUserRoles(List<UserRole> userroles) {
		this.userroles = userroles;
	}

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