package org.biz.employeesSOAPH.ws.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.biz.employeesSOAPH.entities.Department;
import org.biz.employeesSOAPH.entities.Employee;
import org.biz.employeesSOAPH.ws.EmployeeService;
import org.biz.employeesSOAPH.ws.ReturnStatus;

@Stateless
@WebService(
        portName = "EmployeePort",
        serviceName = "EmployeeServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAPH.ws.EmployeeService")
public class EmployeeServiceWS implements EmployeeService {
	
    @PersistenceContext
    private EntityManager em;
    
    public EmployeeServiceWS() {
    	System.out.println("SOAP Webservice EmployeeServiceWS created");
    }

    
    @WebMethod
    public ReturnStatus create(String firstname,String lastname,Date birthdate,int departmentId) {
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
        return sts;        
    }
     	
    @WebMethod
    public List<Employee> list(int departmentId) {
 		
 		System.out.println("emp list!!!!, departmentId = " + departmentId);
 		
        List<Employee> employees = new ArrayList<Employee>();
        List<Employee> found;
        
 		String qry;
        if (departmentId >= 0) {
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
        return employees;
    }

 		
    @WebMethod
    public Employee find(int id) {
        System.out.println("Find, param = " + id);
    	Employee emp = em.find(Employee.class, id);
        System.out.println("Find Finished");
        if (emp == null) {
        	System.out.println("Find, employee = null");
            return null;
        }
        System.out.println("Find emp not null");
        System.out.println("Find, employee = " + emp.copy().getFirstName() + emp.copy().getLastName());
        return emp.copy();
    }
    
    @WebMethod
    public ReturnStatus delete(int id) {
        System.out.println("delete, id = " + id);
    	Employee emp = em.find(Employee.class, id);
        if (emp != null) {
            em.remove(emp);
            System.out.println("Delete OK");
            return new ReturnStatus(true);
        }
        return new ReturnStatus(false);
    }
    
    @WebMethod
    public ReturnStatus update(int id,String firstname,String lastname,Date birthdate,int departmentId) {

    	System.out.println("update : " + firstname + " " + lastname);
        
        Employee emp = em.find(Employee.class, id);
        if (emp == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Employee id " + id + " not found");
            return sts;
        }

        emp.setFirstName(firstname);
        emp.setLastName(lastname);
        emp.setBirthDate(birthdate);

        Department dep = em.find(Department.class, departmentId);
        if (dep != null) {
        	emp.setDepartment(dep);
        }
        
        em.merge(emp);
        
        return new ReturnStatus(true);
    }

}
