package org.biz.employeesRESTH.rest;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.biz.employeesRESTH.entities.Role;
import org.biz.employeesRESTH.entities.User;
import org.biz.employeesRESTH.entities.UserRole;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Singleton()
@Lock(LockType.WRITE)
@Path("/usrl")
//@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
@Produces({MediaType.APPLICATION_JSON})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class UserRoleServiceImpl {
    @PersistenceContext
    private EntityManager em;

    public UserRoleServiceImpl() {
    	System.out.println("REST Webservice UserRoleServiceImpl created");
    }

    @Path("/create")
    @POST
    @RolesAllowed({"Administrator"})
    public String create(	@QueryParam("roleId")int roleId,
	                        @QueryParam("userId")int userId) {
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
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("create return : " + res);
        return res;
    }
    
    @Path("/createu")
    @POST
    public Response createu(@QueryParam("roleId")int roleId,
	                        @QueryParam("userId")int userId) {
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
        return Response.ok().entity(new Gson().toJson(sts)).build();        
    }    
    
    @Path("/createroles")
    @POST
    @RolesAllowed({"Administrator"})
    public String createroles(	@QueryParam("userId")  int userId,
	                        	@QueryParam("roleList") String roleList) {

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
            String res = new Gson().toJson(new ReturnStatus(true));
            System.out.println("createroles: alle rollen toegevoegd");
            return res;
        } else {
            String res = new Gson().toJson(new ReturnStatus(false, "createroles: User niet gevonden"));
            System.out.println("createroles: User niet gevonden");
            return res;            
        }    	
    }
    
    @Path("/createrolesu")
    @POST
    public Response createrolesu(	@QueryParam("userId")  int userId,
	                        		@QueryParam("roleList") String roleList) {

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
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();        
        } else {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("User id " + userId + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
        }    	
    }
    

 	@Path("/list")
    @POST
    @RolesAllowed({"Administrator"})
    public List<UserRole> list(	@QueryParam("first") @DefaultValue("0") int first,
    							@QueryParam("max") @DefaultValue("20") int max,
    							@QueryParam("userId") @DefaultValue("0") int userId,
    							@QueryParam("roleId") @DefaultValue("0") int roleId) {
 		
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
 	
 	@Path("/listu")
    @POST
    public Response listu(@QueryParam("first") @DefaultValue("0") int first,
    							@QueryParam("max") @DefaultValue("20") int max,
    							@QueryParam("userId") @DefaultValue("0") int userId,
    							@QueryParam("roleId") @DefaultValue("0") int roleId) {
 		
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
        //return userroles;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return Response.ok().entity(gson.toJson(userroles)).build();

    }
 		
    @Path("/show/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public UserRole find(@PathParam("id") int id) {
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

    @Path("/delete/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String delete(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
        String res;
    	UserRole urol = em.find(UserRole.class, id);
        if (urol != null) {
            em.remove(urol);
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
    	UserRole urol = em.find(UserRole.class, id);
        if (urol != null) {
            em.remove(urol);
            System.out.println("Delete OK");
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
        }
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(false))).build();
    }

    @Path("/delroles/{userId}/{roleList}")
    @POST
    @RolesAllowed({"Administrator"})
    public String delroles(@PathParam("userId") int userId, 
    					   @PathParam("roleList") String roleList) {
    	
    	roleList = "(" + roleList + ")";
    	
        System.out.println("delroles, userId = " + userId + " rolelist = " + roleList);
        
    	String qry = "DELETE FROM UserRole r WHERE r.user.userId = " + userId;
        System.out.println("query =" + qry);      
    	Query query=em.createQuery(qry);
        System.out.println("query created");      
    	int deleteRecord=query.executeUpdate();
        System.out.println("qry executed, updates = " + deleteRecord);
            
        String res = new Gson().toJson(new ReturnStatus(true));
             
        System.out.println("delroles, create return : " + res);        
        return res;
    }

    @Path("/delrolesu/{userId}/{roleList}")
    @POST
    public Response delrolesu(@PathParam("userId") int userId, 
    					    @PathParam("roleList") String roleList) {
    	
    	roleList = "(" + roleList + ")";
    	
        System.out.println("delroles, userId = " + userId + " rolelist = " + roleList);
        
    	String qry = "DELETE FROM UserRole r WHERE r.user.userId = " + userId;
        System.out.println("query =" + qry);      
    	Query query=em.createQuery(qry);
        System.out.println("query created");      
    	int deleteRecord=query.executeUpdate();
        System.out.println("qry executed, updates = " + deleteRecord);
            
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
    }

    
    @Path("/update/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String update(	@PathParam("id") long id,
    						@QueryParam("userId") int userId,
	                        @QueryParam("roleId") int roleId) {

    	System.out.println("update userrole id : " + id);
        
        UserRole urol = em.find(UserRole.class, id);
        if (urol == null) {
            String res = new Gson().toJson(new ReturnStatus(false));
            System.out.println("userrole not found");
            return res;
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
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("update return : " + res);
        return res;
    }
    
    @Path("/updateu/{id}")
    @POST
    public Response updateu(@PathParam("id") long id,
    						@QueryParam("userId") int userId,
	                        @QueryParam("roleId") int roleId) {

    	System.out.println("update userrole id : " + id);
        
        UserRole urol = em.find(UserRole.class, id);
        if (urol == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("UserRole id " + id + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
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
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
    }
    
}
