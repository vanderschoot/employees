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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Singleton()
@Lock(LockType.WRITE)
@Path("/role")
//@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
@Produces({MediaType.APPLICATION_JSON})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class RoleServiceImpl {
    @PersistenceContext
    private EntityManager em;

    @Path("/create")
    @POST
    @RolesAllowed({"Administrator"})
    public String create(	@QueryParam("name") String name) {
    	Role rol = new Role();
    	rol.setName(name);
        System.out.println("create : " + name);
    	
        em.persist(rol);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("create return : " + res);
        return res;
    }
    
    @Path("/createu")
    @POST
    public Response createu(	@QueryParam("name") String name) {
    	Role rol = new Role();
    	rol.setName(name);
        System.out.println("create : " + name);
    	
        em.persist(rol);
        System.out.println("create, id = " + rol.getRoleId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(rol.getRoleId()).toString());
        return Response.ok().entity(new Gson().toJson(sts)).build();        
    }


 	@Path("/list")
    @POST
    @RolesAllowed({"Administrator"})
    public List<Role> list(	@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("role list!!!!");
        List<Role> roles = new ArrayList<Role>();
        List<Role> found = em.createQuery("select r from Role r",Role.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("role list2!!!!");
     	
        for (Role r : found) {
        	System.out.println("r.copy().name" + r.copy().getName());
        	roles.add(r.copy());
        }

        return roles;
    }
 	
 	@Path("/listu")
    @POST
    public Response listu(@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("role list!!!!");
        List<Role> roles = new ArrayList<Role>();
        List<Role> found = em.createQuery("select r from Role r",Role.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("role list2!!!!");
     	
        for (Role r : found) {
        	System.out.println("r.copy().name" + r.copy().getName());
        	roles.add(r.copy());
        }

        //return roles;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return Response.ok().entity(gson.toJson(roles)).build();

    }
 	
 	
	class CB {
		private int id;
			private String name;
			
			CB(int id, String name) {
				this.id = id;
				this.name = name;
			}
	}

 	@Path("/cblist")
    @POST
    @RolesAllowed({"Administrator"})
    public String cblist(	@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("role cblist list!!!!");
        List<CB> roles = new ArrayList<CB>();
        List<Role> found = em.createQuery("select r from Role r",Role.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("role cblist list2!!!!");
     	
        for (Role r : found) {
        	roles.add(new CB(r.getRoleId(), r.getName()));
            System.out.println("role cblist add : " + r.getRoleId() + " / " + r.getName());
        }
                
        String res = new Gson().toJson(roles);
        System.out.println("role cblist create return : " + res);
        return res;
    }
	
    @Path("/show/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public Role find(@PathParam("id") int id) {
        System.out.println("Find, param = " + id);
        Role rol = em.find(Role.class, id);
        System.out.println("Find Finished");
        if (rol == null) {
        	System.out.println("Find, role = null");
            return null;
        }
        System.out.println("Find role not null");
        System.out.println("Find, role = " + rol.copy().getName());
        return rol.copy();        
    }

    @Path("/delete/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String delete(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
        String res;
        Role  rol = em.find(Role.class, id);
        if (rol != null) {
            em.remove(rol);
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
        Role  rol = em.find(Role.class, id);
        if (rol != null) {
            em.remove(rol);
            System.out.println("Delete OK");
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
        }
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(false))).build();
    }
    

    @Path("/update/{id}")
    @POST
    @RolesAllowed({"Administrator"})
    public String update(	@PathParam("id") long id,
                   			@QueryParam("name") String name) {

    	System.out.println("update : " + name);
        
        Role rol = em.find(Role.class, id);
        if (rol == null) {
            throw new IllegalArgumentException("role id " + id + " not found");
        }

        rol.setName(name);
        em.merge(rol);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("update return : " + res);
        return res;
    }
    
    @Path("/updateu/{id}")
    @POST
    public Response updateu(@PathParam("id") long id,
                   			@QueryParam("name") String name) {

    	System.out.println("update : " + name);
        
        Role rol = em.find(Role.class, id);
        if (rol == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Role id " + id + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
        }

        rol.setName(name);
        em.merge(rol);
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
    }

    
}
