package org.biz.employeesSOAP.ws.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.biz.employeesSOAP.entities.Role;
import org.biz.employeesSOAP.entities.User;
import org.biz.employeesSOAP.entities.UserRole;
import org.biz.employeesSOAP.ws.ReturnStatus;
import org.biz.employeesSOAP.ws.UserRoleService;


@Stateless
@WebService(
        portName = "UserRolePort",
        serviceName = "UserRoleServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAP.ws.UserRoleService")
//@DeclareRoles({"Administrator", "Anonymous","User"})
public class UserRoleServiceWS implements UserRoleService {
    @PersistenceContext
    private EntityManager em;
    
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus create(int roleId,int userId) {
    	UserRole urole = new UserRole();
        System.out.println("create : userId=" + userId + " / roleId=" + roleId);
    	
        User usr = em.find(User.class, userId);
        if (usr != null) {
        	urole.setUser(usr);
        }

        Role rol = em.find(Role.class, roleId);
        if (rol != null) {
        	urole.setRole(rol);
        }
        
        em.persist(urole);
        
        System.out.println("create, id = " + urole.getUserRoleId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(urole.getUserRoleId()).toString());
        return sts;        
    }    
    
    
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus createroles(int userId, String roleList) {

    	System.out.println("createroles : userId = " + userId + " / roleList = " + roleList);
    	String[] roles = roleList.split(",");
    	UserRole urole;
        User usr = em.find(User.class, userId);
        if (usr != null) {
        	for (int i=0; i < roles.length; i++) {        		
        		urole = new UserRole();
            	urole.setUser(usr);

            	System.out.println("create role " + roles[i]);
                Role rol = em.find(Role.class, Integer.parseInt(roles[i]));
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
    
 	
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public List<UserRole> list(int first,int max, int userId,int roleId) {
 		
 		System.out.println("userrole list!!!!, userId = " + userId + " / roleId = " + roleId);
 		
        List<UserRole> userroles = new ArrayList<UserRole>();
        List<UserRole> found;
        
        if ((userId > 0) && (roleId > 0)) {
     		System.out.println("zoek users voor role en user");
        	found = em.createQuery("select ur from UserRole ur where ur.role.roleId = " + roleId + " and ur.user.userId = " + userId + " order by ur.user.userName ASC",UserRole.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
        } else if ((userId > 0) && (roleId <= 0)) {
         		System.out.println("zoek users voor user");
            	found = em.createQuery("select ur from UserRole ur where ur.user.userId = " + userId + " order by ur.user.userName ASC",UserRole.class)
            		.setFirstResult(first).setMaxResults(max).getResultList();
        } else if ((userId <= 0) && (roleId >  0)) {
     		System.out.println("zoek users voor role");
        	found = em.createQuery("select ur from UserRole ur where ur.role.roleId = " + roleId + " order by ur.role.roleName ASC",UserRole.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
        } else {
     		System.out.println("zoek alle users");
        	found = em.createQuery("select ur from UserRole ur",UserRole.class)
    		.setFirstResult(first).setMaxResults(max).getResultList();
        }
       
        System.out.println("userrole list2!!!!");
     	
        for (UserRole ur : found) {
        	System.out.println("userName=" + ur.copy().getUser().getUserName());
        	System.out.println("userRole=" + ur.copy().getRole().getName());
        	userroles.add(ur.copy());
        }
        return userroles;
    }
 		
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public UserRole find(int id) {
        System.out.println("Find, param = " + id);
    	UserRole urol = em.find(UserRole.class, id);
        System.out.println("Find Finished");
        if (urol == null) {
        	System.out.println("Find, userrole = null");
            return null;
        }
        System.out.println("Find userrole not null");
        System.out.println("Find, user/rol = " + urol.copy().getUser().getUserName() + " / " + urol.copy().getRole().getName());

        return urol.copy();
    }
    
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus delete(int id) {
        System.out.println("delete, id = " + id);
    	UserRole urol = em.find(UserRole.class, id);
        if (urol != null) {
            em.remove(urol);
            System.out.println("Delete OK");
            return new ReturnStatus(true);
        }
        return new ReturnStatus(false);
    }


    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus delroles(int userId, String roleList) {
    	
    	roleList = "(" + roleList + ")";
    	
        System.out.println("delroles, userId = " + userId + " rolelist = " + roleList);
        
    	String qry = "DELETE FROM UserRole r WHERE r.user.userId = " + userId;
        System.out.println("query =" + qry);      
    	Query query=em.createQuery(qry);
        System.out.println("query created");      
    	int deleteRecord=query.executeUpdate();
        System.out.println("qry executed, updates = " + deleteRecord);
            
        return new ReturnStatus(true);
    }
    
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus update(long id,int userId,int roleId) {

    	System.out.println("update userrole id : " + id);
        
        UserRole urol = em.find(UserRole.class, id);
        if (urol == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("UserRole id " + id + " not found");
            return sts;
        }

        Role rol = em.find(Role.class, roleId);
        if (rol != null) {
        	urol.setRole(rol);
        }

        User usr = em.find(User.class, userId);
        if (usr != null) {
        	urol.setUser(usr);
        }
        
        em.merge(urol);
        
        return new ReturnStatus(true);
    }
    
}
