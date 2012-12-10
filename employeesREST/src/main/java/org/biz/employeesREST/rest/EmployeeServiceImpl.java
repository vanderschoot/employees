package org.biz.employeesREST.rest;


import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

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
import javax.persistence.PersistenceContextType;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.biz.employeesREST.entities.Department;
import org.biz.employeesREST.entities.Employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Singleton()
@Lock(LockType.WRITE)
@Path("/emp")
@Produces({MediaType.APPLICATION_JSON})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class EmployeeServiceImpl {
    @PersistenceContext
    private EntityManager em;

    @Path("/create")
    @POST
    @RolesAllowed({"User"})
    public String create(	@QueryParam("firstName") String firstname,
					    	@QueryParam("lastName") String lastname,
	                        @QueryParam("birthDate") @DefaultValue("1900/01/01") Date birthdate,
	                        @QueryParam("departmentId") @DefaultValue("") int departmentId) {
    	Employee employee = new Employee();
    	employee.setFirstName(firstname);
    	employee.setLastName(lastname);
    	employee.setBirthDate(birthdate);
        System.out.println("create : " + firstname + " " + lastname + " / birthdate = " + birthdate);
    	
        Department dep = em.find(Department.class, departmentId);
        if (dep != null) {
        	employee.setDepartment(dep);
        }

        em.persist(employee);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("create return : " + res);
        return res;
    }
    
    @Path("/createu")
    @POST
    public Response createu(@QueryParam("firstName") String firstname,
					    	@QueryParam("lastName") String lastname,
	                        @QueryParam("birthDate") @DefaultValue("1900/01/01") Date birthdate,
	                        @QueryParam("departmentId") @DefaultValue("") int departmentId) {
    	Employee employee = new Employee();
    	employee.setFirstName(firstname);
    	employee.setLastName(lastname);
    	employee.setBirthDate(birthdate);
        System.out.println("create : " + firstname + " " + lastname + " / birthdate = " + birthdate);
    	
        Department dep = em.find(Department.class, departmentId);
        if (dep != null) {
        	employee.setDepartment(dep);
        }

        em.persist(employee);
        
        System.out.println("create, id = " + employee.getEmployeeId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(employee.getEmployeeId()).toString());
        return Response.ok().entity(new Gson().toJson(sts)).build();        
    }
    
	@SuppressWarnings("unused")
    private static class ListWrapper  {
		private int total;	
		private List<Employee> rows;
		public ListWrapper() {}
		public ListWrapper(int count, List<Employee> lst) {
			this.total = count;
			this.rows = lst;
		}
		public int getTotal() {return total;}
		public List<Employee> getRows() {return rows;}
	}
	
 	@Path("/listform")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @RolesAllowed({"Administrator", "User"})
    public Response listform(	@FormParam("page") @DefaultValue("0") int page,
	    						@FormParam("rows") @DefaultValue("20") int rows,
	    						@FormParam("sort") @DefaultValue("employeeId") String sort,
	    						@FormParam("order") @DefaultValue("asc") String order,
	    						@FormParam("departmentId") @DefaultValue("0") int departmentId) {
 		
 		System.out.println("emp list!!!!, departmentId = " + departmentId + " /  page=" + page + " / rows = " + rows + " / sort = " + sort + " / order = " + order);
 		
        List<Employee> employees = new ArrayList<Employee>();
        List<Employee> found;
        
    	int count = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult().intValue();
 		int start = (page -1) * rows;
 		String qry;
        if (departmentId > 0) {
     		System.out.println("zoek employees voor department");
     		qry = "select e from Employee e where e.department.departmentId = " + departmentId + " order by e." + sort + " " + order;
            System.out.println("query = " + qry);
            found = em.createQuery(qry,Employee.class)
        	.setFirstResult(start).setMaxResults(rows).getResultList();

        } else {
     		System.out.println("zoek alle employees");
     		qry = "select e from Employee e order by e." + sort + " " + order;
            System.out.println("query = " + qry);
        	found = em.createQuery(qry,Employee.class)
    		.setFirstResult(start).setMaxResults(rows).getResultList();
        }
        
        for (Employee e : found) {
        	System.out.println("name" + e.getFirstName() + e.getBirthDate());
        	employees.add(e.copy());
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        //return gson.toJson(new ListWrapper(count,employees));
        return Response.ok().entity(gson.toJson(new ListWrapper(count,employees))).build();
    }
 	
 	@Path("/list")
    @GET
    public Response list(	@QueryParam("departmentId") @DefaultValue("0") int departmentId) {
 		
 		System.out.println("emp list!!!!, departmentId = " + departmentId);
 		
        List<Employee> employees = new ArrayList<Employee>();
        List<Employee> found;
        
 		String qry;
        if (departmentId > 0) {
     		System.out.println("zoek employees voor department");
     		qry = "select e from Employee e where e.department.departmentId = " + departmentId + " order by e.firstName";
            System.out.println("query = " + qry);
        	found = em.createQuery(qry,Employee.class).getResultList();

        } else {
     		System.out.println("zoek alle employees");
     		qry = "select e from Employee e order by e.firstName";
            System.out.println("query = " + qry);
        	found = em.createQuery(qry,Employee.class).getResultList();
        }
        
        for (Employee e : found) {
        	System.out.println("name" + e.getFirstName());
        	employees.add(e.copy());
        }
         
        System.out.println("list return");
        //return employees;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        //return gson.toJson(new ListWrapper(count,employees));
        return Response.ok().entity(gson.toJson(new ListWrapper(found.size(), employees))).build();

    }
 	
 	@Path("/listu")
    @GET
    public Response listu(	@QueryParam("departmentId") @DefaultValue("0") int departmentId) {
 		
 		System.out.println("emp list!!!!, departmentId = " + departmentId);
 		
        List<Employee> employees = new ArrayList<Employee>();
        List<Employee> found;
        
 		String qry;
        if (departmentId > 0) {
     		System.out.println("zoek employees voor department");
     		qry = "select e from Employee e where e.department.departmentId = " + departmentId + " order by e.firstName";
            System.out.println("query = " + qry);
        	found = em.createQuery(qry,Employee.class).getResultList();

        } else {
     		System.out.println("zoek alle employees");
     		qry = "select e from Employee e order by e.firstName";
            System.out.println("query = " + qry);
        	found = em.createQuery(qry,Employee.class).getResultList();
        }
        
        for (Employee e : found) {
        	System.out.println("name" + e.getFirstName());
        	employees.add(e.copy());
        }
         
        System.out.println("list return");
        //return employees;
        
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        //return gson.toJson(new ListWrapper(count,employees));
        return Response.ok().entity(gson.toJson(employees)).build();
        //return gson.toJson(employees);

    }

 		
    @Path("/show/{id}")
    @POST
    @RolesAllowed({"Administrator", "Anonymous","User"})
    public Employee find(@PathParam("id") int id) {
        System.out.println("Find, param = " + id);
    	Employee emp = em.find(Employee.class, id);
        System.out.println("Find Finished");
        if (emp == null) {
        	System.out.println("Find, employee = null");
            return null;
        }
        System.out.println("Find emp not null");
        System.out.println("Find, employee = " + emp.copy().getFirstName() + emp.copy().getLastName());
        //ResponseBuilder builder = Response.ok(emp.copy());
        //return builder.build();
        return emp.copy();
        
        //return Response.status(200).type(MediaType.APPLICATION_JSON).entity(emp.copy()).build();
    }

    @Path("/delete/{id}")
    @POST
    @RolesAllowed({"User"})
    public String delete(@PathParam("id") int id) {
        System.out.println("delete, id = " + id);
        String res;
    	Employee emp = em.find(Employee.class, id);
        if (emp != null) {
            em.remove(emp);
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
    	Employee emp = em.find(Employee.class, id);
        if (emp != null) {
            em.remove(emp);
            System.out.println("Delete OK");
            return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
        }
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(false))).build();
    }

    @Path("/update/{id}")
    @POST
    @RolesAllowed({"User"})
    public String update(	@PathParam("id") int id,
                   			@QueryParam("firstName") String firstname,
                   			@QueryParam("lastName") String lastname,
                   			@QueryParam("birthDate") Date birthdate,
	                        @QueryParam("departmentId") @DefaultValue("0") int departmentId) {

    	System.out.println("update : " + firstname + " " + lastname);
        
        Employee emp = em.find(Employee.class, id);
        if (emp == null) {
            // throw new IllegalArgumentException("employee id " + id + " not found");
            String res = new Gson().toJson(new ReturnStatus(false));
            System.out.println("employee not found");
            return res;
        }

        emp.setFirstName(firstname);
        emp.setLastName(lastname);
        emp.setBirthDate(birthdate);

        Department dep = em.find(Department.class, departmentId);
        if (dep != null) {
        	emp.setDepartment(dep);
        }

        em.merge(emp);
        
        String res = new Gson().toJson(new ReturnStatus(true));
        System.out.println("update return : " + res);
        return res;
    }
    
    @Path("/updateu/{id}")
    @POST
    public Response updateu(@PathParam("id") int id,
                   			@QueryParam("firstName") String firstname,
                   			@QueryParam("lastName") String lastname,
                   			@QueryParam("birthDate") Date birthdate,
	                        @QueryParam("departmentId") @DefaultValue("0") int departmentId) {

    	System.out.println("update : " + firstname + " " + lastname);
        
        Employee emp = em.find(Employee.class, id);
        if (emp == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Employee id " + id + " not found");
            return Response.ok().entity(new Gson().toJson(sts)).build();
        }

        emp.setFirstName(firstname);
        emp.setLastName(lastname);
        emp.setBirthDate(birthdate);

        Department dep = em.find(Department.class, departmentId);
        if (dep != null) {
        	emp.setDepartment(dep);
        }
        
        em.merge(emp);
        
        return Response.ok().entity(new Gson().toJson(new ReturnStatus(true))).build();
    }

}
