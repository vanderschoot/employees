package org.biz.employees.control.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.entities.Role;

@Stateless
@LocalBean
@Named
public class RoleBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger("RoleBean");
	
    private List<Role> roles;

	private int roleId;
	private String name;

	@EJB private RoleDAO dao;
       
    public List<Role> getRoles() {		
	   logger.info("getRoles");
	   roles = dao.getAll();
       logger.info("list, list size =" + roles.size());
       return roles;
	}
    
    public String delete(Role role) {		
       logger.info("delete, delete Id=" + role.getRoleId());       
	   dao.delete(role.getRoleId());
       return null;
	}	    
    
    public String edit(Role role) {
 	   logger.info("edit"); 
	   this.roleId = role.getRoleId();
	   this.name = role.getName();
	   return "role";
	}
    
	public String add() {
		logger.info("add");		
		this.roleId = -1;
		this.name = "";
		return "role";
	}	

    public String save() {
    	logger.info("save");
    	Role role = null;
    	if (this.roleId == -1) {	
        	logger.info("add");
		    role = new Role();
		    role.setName(this.name);
	        dao.save(role);
    	} else {
        	logger.info("update");
    		role = dao.findById(this.roleId);
		    role.setName(this.name);
		    dao.update(this.roleId, role);
    	}
        System.out.println("rolename=" +this.name);
	    return "roleList";
 	}
    
    public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}