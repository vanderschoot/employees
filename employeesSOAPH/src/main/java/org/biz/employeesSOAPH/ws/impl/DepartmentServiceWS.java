package org.biz.employeesSOAPH.ws.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.biz.employeesSOAPH.data.Database;
import org.biz.employeesSOAPH.entities.Department;
import org.biz.employeesSOAPH.ws.DepartmentService;
import org.biz.employeesSOAPH.ws.ReturnStatus;


@Stateless
@WebService(
        portName = "DepartmentPort",
        serviceName = "DepartmentServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAPH.ws.DepartmentService")
public class DepartmentServiceWS implements DepartmentService {
    @PersistenceContext
    private EntityManager em;
    
    public DepartmentServiceWS() {
    	System.out.println("SOAP Webservice DepartmentServiceWS created");
		//-----Remove the following code if the HyperSQL Database will be replaced by a real database
		Database database = new Database();
	    if (!database.createDatabase()) {
			System.out.println("SOAP Webservice DepartmentServiceWS could not create HyperSQL database");
	    }
	    //-----
    }

    public ReturnStatus create(String name,String address, int budget) {

        System.out.println("create : " + name + " " + address + " / budget = " + budget);

    	Department department = new Department();
    	department.setName(name);
    	department.setAddress(address);
    	department.setBudget(budget);
    	
        em.persist(department);
                
        System.out.println("create, id = " + department.getDepartmentId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(department.getDepartmentId()).toString());
        return sts;        
    }
    
    public List<Department> list(int first, int max) {
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
 		
    public Department find(int id) {
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

    public ReturnStatus delete(int id) {
        System.out.println("delete, id = " + id);
        Department dep = em.find(Department.class, id);
        if (dep != null) {
            em.remove(dep);
            return new ReturnStatus(true);
        }
        return new ReturnStatus(false);
    }
        
    public ReturnStatus update(long id, String name,String address,int budget) {

    	System.out.println("update : " + name);
        
        Department dep = em.find(Department.class, id);
        if (dep == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Department id " + id + " not found");
            return sts;
        }

        dep.setName(name);
        dep.setAddress(address);
        dep.setBudget(budget);
        em.merge(dep);
        
        return new ReturnStatus(true);
    }

}
