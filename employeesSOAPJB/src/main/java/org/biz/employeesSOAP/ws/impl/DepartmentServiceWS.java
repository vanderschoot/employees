package org.biz.employeesSOAP.ws.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.biz.employeesSOAP.entities.Department;
import org.biz.employeesSOAP.ws.DepartmentService;
import org.biz.employeesSOAP.ws.ReturnStatus;


@Stateless
@WebService(
        portName = "DepartmentPort",
        serviceName = "DepartmentServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAP.ws.DepartmentService")
public class DepartmentServiceWS implements DepartmentService {
    @PersistenceContext
    private EntityManager em;

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
