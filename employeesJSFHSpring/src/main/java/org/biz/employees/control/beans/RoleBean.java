package org.biz.employees.control.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Repository
@ManagedBean(name="roleBean")
@Transactional
//@SessionScoped
public class RoleBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger("RoleBean");
	
    private List<Role> roles;

	private int roleId;
	private String name;

	@Autowired
    private RoleDAO dao;
       
    public List<Role> getRoles() {		
	   logger.info("getRoles");
	   roles = dao.getAll();
       logger.info("list, list size =" + roles.size());
       return roles;
	}
    
    public String delete(Role role) {		
       logger.info("delete, delete Id=" + role.getRoleId());
       
       try {
    	   System.out.println("Start delete");
    	   dao.delete(role.getRoleId());
    	   System.out.println("Executed delete");
       } catch (TransactionSystemException e) {
    	   System.out.println("TransactionSystemException");
    	   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fout opgetreden: " + e.getLocalizedMessage()));
       } catch ( DataAccessException e) {
    	   System.out.println("DataAccessException");
    	   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fout opgetreden: " + e.getLocalizedMessage()));
       }
	   System.out.println("End delete");
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