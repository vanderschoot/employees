package org.biz.employees.model.dao;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.biz.employees.model.entities.UserRole;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.service.ReturnStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Named("userroleDAO")
@Repository
@Transactional
public class UserRoleDAO extends AbstractDAO<UserRole> {

	@PersistenceContext
	private EntityManager em;
	
	public UserRoleDAO(){
		setClassT(UserRole.class);
	}
	
    public List<UserRole> list(int userId,int roleId) {
 		
 		System.out.println("userrole list!!!!, userId = " + userId + " / roleId = " + roleId);
 		
        List<UserRole> userroles = new ArrayList<UserRole>();
        List<UserRole> found;
        
        if ((userId > 0) && (roleId > 0)) {
     		System.out.println("zoek userroles voor role en user");
        	found = em.createQuery("select ur from UserRole ur where ur.role.roleId = " + roleId + " and ur.user.userId = " + userId + " order by ur.user.userName ASC",UserRole.class)
        		.getResultList();
        } else if ((userId > 0) && (roleId <= 0)) {
         		System.out.println("zoek userroles voor user");
            	found = em.createQuery("select ur from UserRole ur where ur.user.userId = " + userId + " order by ur.user.userName ASC",UserRole.class)
            		.getResultList();
        } else if ((userId <= 0) && (roleId >  0)) {
     		System.out.println("zoek userroles voor role");
        	found = em.createQuery("select ur from UserRole ur where ur.role.roleId = " + roleId + " order by ur.role.roleName ASC",UserRole.class)
        		.getResultList();
        } else {
     		System.out.println("zoek alle userroles");
        	found = em.createQuery("select ur from UserRole ur",UserRole.class).getResultList();
        }
       
        System.out.println("userrole list2!!!!");
     	
        for (UserRole ur : found) {
        	System.out.println("userName=" + ur.copy().getUser().getUserName());
        	System.out.println("userRole=" + ur.copy().getRole().getName());
        	userroles.add(ur.copy());
        }
        return userroles;
    }
    
    public ReturnStatus createroles(int userId, String []roleList) {
    	System.out.println("createroles : userId = " + userId + " / roleList = " + roleList);
    	UserRole urole;
        User usr = em.find(User.class, userId);
        if (usr != null) {
        	for (int i=0; i < roleList.length; i++) {        		
        		urole = new UserRole();
            	urole.setUser(usr);

            	System.out.println("create role " + roleList[i]);
                Role rol = em.find(Role.class, Integer.parseInt(roleList[i]));
                if (rol != null) {
                	urole.setRole(rol);
                    em.persist(urole);
                }                
        	}	        
            System.out.println("createroles: alle rollen toegevoegd");
            return new ReturnStatus(true);        
        } else {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("User id " + userId + " not found");
            return sts;
        }    	
    }

    public ReturnStatus delroles(int userId) {
    	
        System.out.println("delroles, userId = " + userId );
        
    	String qry = "DELETE FROM UserRole r WHERE r.user.userId = " + userId;
        System.out.println("query =" + qry);      
    	Query query=em.createQuery(qry);
        System.out.println("query created");      
    	int deleteRecord=query.executeUpdate();
        System.out.println("qry executed, updates = " + deleteRecord);
            
        return new ReturnStatus(true);
    }
    
}