package org.biz.employees.control.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.dao.UserDAO;
import org.biz.employees.model.dao.UserRoleDAO;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;

@Stateless
@LocalBean
@Named
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger("UserBean");
	
	private int userId;
	private String userName;
    private String password;
    private String email; 
    
	private List<User> users;
    private List<Role> roles;
    private List<UserRole> usroles;
    
    private int[] roleIds;

	@EJB private UserDAO dao;
	@EJB private RoleDAO roledao;
	@EJB private UserRoleDAO usrldao;
       
    public List<User> getUsers() {		
	   logger.info("getUsers");
	   users = dao.getAll();
       logger.info("list, list size =" + users.size());
       return users;
	}
    
    public String delete(User user) {		
       logger.info("delete, delete Id=" + user.getUserId());
       usrldao.delroles(user.getUserId());
       dao.delete(user.getUserId());
       return null;
	}	    
    
    public String edit(User user) {
 	   logger.info("edit"); 
	   this.userId = user.getUserId();
	   this.userName = user.getUserName();
	   this.password = user.getPassword();
	   this.email = user.getEmail();
	   	   
       roles = roledao.getAll();
       usroles = usrldao.list(user.getUserId(), 0);
       
       roleIds = new int[usroles.size()];
       
       for (int i = 0; i < usroles.size();i++) {
    	   roleIds[i] = usroles.get(i).getRole().getRoleId();
       }
       
	   return "user";
	}
    
	public String add() {
		logger.info("add");		
		this.userId = -1;
		this.userName = "";
		this.password = "";
		this.email = "";
		
	    this.roles = roledao.getAll();
	    this.usroles = new ArrayList<UserRole>();

	    return "user";
	}	

	public String save() {
    	logger.info("save");
    	
        for(int i=0; i< roleIds.length; i++) {
        	logger.info("selected role : " + roleIds[i]);
        }
    	
    	User user = null;
    	if (this.userId == -1) {	
        	logger.info("add");
		    user = new User();
		    user.setUserName(this.userName);
		    user.setPassword(this.password);
		    user.setEmail(this.email);
	        dao.save(user);
            List<User> users = dao.findUsers(userName);
	        usrldao.createroles(users.get(0).getUserId(), roleIds);
    	} else {
        	logger.info("update");
    		user = dao.findById(this.userId);
		    user.setUserName(this.userName);
		    user.setPassword(this.password);
		    user.setEmail(this.email);
	        usrldao.delroles(this.userId);
	        usrldao.createroles(this.userId, roleIds);
		    dao.update(this.userId, user);
    	}
        System.out.println("name=" +this.userName+" / password = " +this.password+" / email ="+this.email);
	    return "userList";
 	}
    
    public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
    public List<Role> getRoles() {
		return roles;
	}

	public List<UserRole> getUsroles() {
		return usroles;
	}

	public int[] getRoleIds() {
		return roleIds;
	}
	
	public void setRoleIds(int[] roleIds) {
		this.roleIds = roleIds;
	}


}