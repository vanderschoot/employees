package org.biz.employeesREST.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;


/**
 * The persistent class for the UserRole database table.
 * 
 */
@Entity
@XmlRootElement(name="rows")
public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userRoleId;
	
	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="RoleId")
	private Role role;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="UserId")
	private User user;

	
	public UserRole() {
	}
	
	public UserRole copy() {
		UserRole usr = new UserRole();
		usr.setRole(getRole());
		usr.setUser(getUser());
		usr.setUserRoleId(getUserRoleId());
		return usr;
	}
	
	public int getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !UserRole.class.isAssignableFrom(o.getClass())) {
            return false;
        }

        UserRole usr = (UserRole) o;

        return userRoleId == usr.userRoleId;
    }

    @Override
    public int hashCode() {
        return (int) (userRoleId ^ (userRoleId >>> 32));
    }
}