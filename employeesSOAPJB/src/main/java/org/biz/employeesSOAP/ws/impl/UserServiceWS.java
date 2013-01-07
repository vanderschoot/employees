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

import org.biz.employeesSOAP.entities.User;
import org.biz.employeesSOAP.ws.ReturnStatus;
import org.biz.employeesSOAP.ws.UserService;

@Stateless
@WebService(
        portName = "UserPort",
        serviceName = "UserServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAP.ws.UserService")
//@DeclareRoles({"Administrator", "Anonymous","User"})
public class UserServiceWS implements UserService {
    @PersistenceContext
    private EntityManager em;
    
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus create(String email,String userName,String password) {
    	User user = new User();
    	user.setUserName(userName);
    	user.setEmail(email);
    	user.setPassword(password);
        System.out.println("create : " + userName );
    	
        em.persist(user);
        
        System.out.println("create, id = " + user.getUserId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(user.getUserId()).toString());
        return sts;        
    }
 	
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public List<User> list(int first,int max) {
 		
 		System.out.println("user list!!!!");
 		
        List<User> users = new ArrayList<User>();
        List<User> found;
        
 		System.out.println("zoek alle users");
    	found = em.createQuery("select u from User u order by u.userName ASC",User.class)
		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("user list2!!!!");
     	
        for (User u : found) {
        	System.out.println("u.copy().userName" + u.copy().getUserName());
        	users.add(u.copy());
        }
        return users;
    }
 		
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public User find(int id) {
        System.out.println("Find, param = " + id);
    	User usr = em.find(User.class, id);
        System.out.println("Find Finished");
        if (usr == null) {
        	System.out.println("Find, user = null");
            return null;
        }
        System.out.println("Find usr not null");
        System.out.println("Find, user = " + usr.copy().getUserName() + usr.copy().getUserName());

        return usr.copy();
    }
  
    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus delete(int id) {
        System.out.println("delete, id = " + id);
    	User usr = em.find(User.class, id);
        if (usr != null) {
            em.remove(usr);
            System.out.println("Delete OK");
            return new ReturnStatus(true);
        }
        return new ReturnStatus(false);
    }


    @WebMethod
//    @RolesAllowed({"Administrator"})
    public ReturnStatus update(long id,String email,String userName,String password) {

    	System.out.println("update : " + userName);
        
        User usr = em.find(User.class, id);
        if (usr == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("User id " + id + " not found");
            return sts;
        }

        usr.setUserName(userName);
        usr.setEmail(email);
        usr.setPassword(password);
        
        em.merge(usr);
        
        return new ReturnStatus(true);
    }

}
