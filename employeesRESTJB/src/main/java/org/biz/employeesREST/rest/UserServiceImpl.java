package org.biz.employeesREST.rest;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.biz.employeesREST.entities.Department;
import org.biz.employeesREST.entities.Employee;
import org.biz.employeesREST.entities.Role;
import org.biz.employeesREST.entities.User;
import org.biz.employeesREST.entities.UserRole;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Singleton()
@Lock(LockType.WRITE)
@Path("/usr")
@Produces({MediaType.APPLICATION_JSON})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class UserServiceImpl {
    @PersistenceContext
    private EntityManager em;

    @Path("/create")
    @POST
    @RolesAllowed({"Administrator"})
    public String create(	@QueryParam("email") String email,
	    					@QueryParam("userName") String userName,
					    	@QueryParam("password") String password) {
    	User user = new User();
    	user.setUserName(userName);
    	user.setEmail(email);
    	user.setPassword(password);
        System.out.println("create : " + userName );
    	
        em.persist(user);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("create return : " + res);
        return res;
    }
    
    @Path("/createu")
    @POST
    public Response createu(@QueryParam("email") String email,
	    					@QueryParam("userName") String userName,
					    	@QueryParam("password") String password) {
    	User user = new User();
    	user.setUserName(userName);
    	user.setEmail(email);
    	user.setPassword(password);
        System.out.println("create : " + userName );
    	
        em.persist(user);
        
        System.out.println("create, id = " + user.getUserId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(user.getUserId()).toString());
        return Response.ok().entity(new Gson().toJson(sts)).build();        
    }

 	@Path("/list")
    @POST
    @RolesAllowed({"Administrator"})
    public List<User> list(	@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
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
 	
 	@Path("/listu")
    @POST
    public Response listu(@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
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
        //return users;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return Response.ok().entity(gson.toJson(users)).build();
    }
 		
    @Path("/show/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public User find(@PathParam("id") int id) {
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

    @Path("/delete/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String delete(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
        String res;
    	User usr = em.find(User.class, id);
        if (usr != null) {
            em.remove(usr);
            res = new Gson().toJson(new ReturnStatus(true));
            System.out.println("create return : " + res);
            return res;
        }
        res = new Gson().toJson(new ReturnStatus(false));
        return res;
    }
    
    @Path("/deleteu/{id}")
    @POST
    public Response deleteu(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
    	User usr = em.find(User.class, id);
        if (usr != null) {
            em.remove(usr);
            System.out.println("Delete OK");
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
        }
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(false))).build();
    }


    @Path("/update/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String update(	@PathParam("id") long id,
                   			@QueryParam("email") String email,
                   			@QueryParam("userName") String userName,
                   			@QueryParam("password") String password) {

    	System.out.println("update : " + userName);
        
        User usr = em.find(User.class, id);
        if (usr == null) {
            String res = new Gson().toJson(new ReturnStatus(false));
            System.out.println("user not found");
            return res;
        }

        usr.setUserName(userName);
        usr.setEmail(email);
        usr.setPassword(password);
        
        em.merge(usr);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("update return : " + res);
        return res;
    }
    
    @Path("/updateu/{id}")
    @POST
    public Response updateu(@PathParam("id") long id,
                   			@QueryParam("email") String email,
                   			@QueryParam("userName") String userName,
                   			@QueryParam("password") String password) {

    	System.out.println("update : " + userName);
        
        User usr = em.find(User.class, id);
        if (usr == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("User id " + id + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
        }

        usr.setUserName(userName);
        usr.setEmail(email);
        usr.setPassword(password);
        
        em.merge(usr);
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
    }

}
