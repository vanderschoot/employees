package org.biz.employeesVR.data.DTO;

import java.io.Serializable;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="rows")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	private int roleId;

	private String name;

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

	//public List<UserRole> getUserRoles() {
	//	return this.userroles;
	//}

	//public void setUserRoles(List<UserRole> userroles) {
	//	this.userroles = userroles;
	//}
	
	public Role copy() {
		Role rol = new Role();
		rol.setRoleId(getRoleId());
		rol.setName(getName());
		return rol;
	}

    @Override
    public boolean equals(Object object) {
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