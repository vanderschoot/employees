package org.biz.employeesREST.rest;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Singleton()
//@Stateful
//@SessionScoped
@Lock(LockType.WRITE)
@Path("/dep")
@Produces({MediaType.APPLICATION_JSON})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class DepartmentServiceImpl {
    @PersistenceContext
    private EntityManager em;

    @Path("/create")
    @POST
    @RolesAllowed({"User"})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(	@QueryParam("name") String name,
					    	@QueryParam("address") String address,
	                        @QueryParam("budget") @DefaultValue("0") int budget) {

        System.out.println("create : " + name + " " + address + " / budget = " + budget);

    	Department department = new Department();
    	department.setName(name);
    	department.setAddress(address);
    	department.setBudget(budget);
    	
        em.persist(department);
        
        //String res = new Gson().toJson(new ReturnStatus(true));
        //System.out.println("create return : " + res);
        //return res;
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();        
    }
    
    
    @Path("/createu")
    @POST
    //@RolesAllowed({"User"})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createu(@QueryParam("name") String name,
					    	@QueryParam("address") String address,
	                        @QueryParam("budget") @DefaultValue("0") int budget) {

        System.out.println("create : " + name + " " + address + " / budget = " + budget);

    	Department department = new Department();
    	department.setName(name);
    	department.setAddress(address);
    	department.setBudget(budget);
    	
        em.persist(department);
               
        System.out.println("create, id = " + department.getDepartmentId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(department.getDepartmentId()).toString());
        return Response.ok().entity(new Gson().toJson(sts)).build();        
    }
	
    
 	@Path("/list")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public List<Department> list(	@QueryParam("first") @DefaultValue("0") int first,
    								@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("dep list!!!!");
        List<Department> departments = new ArrayList<Department>();
        List<Department> found = em.createQuery("select d from Department d",Department.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("dep list2!!!!");
     	
        for (Department d : found) {
        	System.out.println("e.copy().name" + d.copy().getName());
        	departments.add(d.copy());
        }

        return departments;
    }
 	
 	@Path("/listu")
    @POST
    public Response listu(	@QueryParam("first") @DefaultValue("0") int first,
    								@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("dep list!!!!");
        List<Department> departments = new ArrayList<Department>();
        List<Department> found = em.createQuery("select d from Department d",Department.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("dep list2!!!!");
     	
        for (Department d : found) {
        	System.out.println("e.copy().name" + d.copy().getName());
        	departments.add(d.copy());
        }

        //return departments;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return Response.ok().entity(gson.toJson(departments)).build();
    }
 	
	@SuppressWarnings("unused")
	class CBWrapper {
		private int id;
		private String name;			
		CBWrapper(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

 	@Path("/cblist")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public String cblist(	@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("dep cblist list!!!!");
        List<CBWrapper> departments = new ArrayList<CBWrapper>();
        List<Department> found = em.createQuery("select d from Department d",Department.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("dep cblist list2!!!!");
     	
        for (Department d : found) {
        	departments.add(new CBWrapper(d.getDepartmentId(), d.getName()));
            System.out.println("dep cblist add : " + d.getDepartmentId() + " / " + d.getName());
        }
                
        String res = new Gson().toJson(departments);
        System.out.println("dep cblist create return : " + res);
        return res;
    }
 	
 	@Path("/cblistu")
    @POST
    public String cblistu(	@QueryParam("first") @DefaultValue("0") int first,
    						@QueryParam("max") @DefaultValue("20") int max) {
 		
 		System.out.println("dep cblist list!!!!");
        List<CBWrapper> departments = new ArrayList<CBWrapper>();
        List<Department> found = em.createQuery("select d from Department d",Department.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("dep cblist list2!!!!");
     	
        for (Department d : found) {
        	departments.add(new CBWrapper(d.getDepartmentId(), d.getName()));
            System.out.println("dep cblist add : " + d.getDepartmentId() + " / " + d.getName());
        }
                
        String res = new Gson().toJson(departments);
        System.out.println("dep cblist create return : " + res);
        return res;
    }
	
    @Path("/show/{id}")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public Department find(@PathParam("id") int id) {
        System.out.println("Find, param = " + id);
        Department dep = em.find(Department.class, id);
        System.out.println("Find Finished");
        if (dep == null) {
        	System.out.println("Find, department = null");
            return null;
        }
        System.out.println("Find emp not null");
        System.out.println("Find, department = " + dep.copy().getName());
        return dep.copy();        
    }
    
    @Path("/showu/{id}")
    @POST
    public Department findu(@PathParam("id") int id) {
        System.out.println("Find, param = " + id);
        Department dep = em.find(Department.class, id);
        System.out.println("Find Finished");
        if (dep == null) {
        	System.out.println("Find, department = null");
            return null;
        }
        System.out.println("Find emp not null");
        System.out.println("Find, department = " + dep.copy().getName());
        return dep.copy();        
    }

    @Path("/delete/{id}")
    @POST
    @RolesAllowed({"User"})
    public String delete(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
        String res;
        Department dep = em.find(Department.class, id);
        if (dep != null) {
            em.remove(dep);
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
        Department dep = em.find(Department.class, id);
        if (dep != null) {
            em.remove(dep);
            System.out.println("Delete OK");
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
        }
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(false))).build();
    }

    
    @Path("/update/{id}")
    @POST
    @RolesAllowed({"User"})
    public String update(	@PathParam("id") long id,
                   			@QueryParam("name") String name,
                   			@QueryParam("address") String address,
                   			@QueryParam("budget") @DefaultValue("0") int budget) {

    	System.out.println("update : " + name);
        
        Department dep = em.find(Department.class, id);
        if (dep == null) {
            throw new IllegalArgumentException("employee id " + id + " not found");
        }

        dep.setName(name);
        dep.setAddress(address);
        dep.setBudget(budget);
        em.merge(dep);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("update return : " + res);
        return res;
    }
    
    @Path("/updateu/{id}")
    @POST
    public Response updateu(@PathParam("id") long id,
                   			@QueryParam("name") String name,
                   			@QueryParam("address") String address,
                   			@QueryParam("budget") @DefaultValue("0") int budget) {

    	System.out.println("update : " + name);
        
        Department dep = em.find(Department.class, id);
        if (dep == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Department id " + id + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
        }

        dep.setName(name);
        dep.setAddress(address);
        dep.setBudget(budget);
        em.merge(dep);
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();

    }

}
