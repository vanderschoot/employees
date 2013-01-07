package org.biz.employeesSOAP.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;


/**
 * The persistent class for the Role database table.
 * 
 */
@Entity
@XmlRootElement(name="rows")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int roleId;

	@Lob
	private String name;

	//bi-directional many-to-one association to UserRole
	//@OneToMany(mappedBy="role")
	//private List<UserRole> userroles;

	public Role() {
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
/*
	public List<UserRole> getUserRoles() {
		return this.userroles;
	}

	public void setUserRoles(List<UserRole> userroles) {
		this.userroles = userroles;
	}
*/	
	public Role copy() {
		Role rol = new Role();
		rol.setRoleId(getRoleId());
		rol.setName(getName());
		return rol;
	}

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.roleId == 0 && other.roleId != 0) || (this.roleId != 0 && !(this.roleId == other.roleId))) {
            return false;
        }
        return true;
    }


}