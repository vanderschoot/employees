package org.biz.employeesSOAPH.ws;


import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.biz.employeesSOAPH.entities.Employee;
import org.biz.employeesSOAPH.ws.ReturnStatus;

@WebService(targetNamespace = "http://biz.org/wsdl")
public interface EmployeeService {

	public ReturnStatus create(String firstname,String lastname,Date birthdate,int departmentId);
    
    public List<Employee> list(int departmentId);
 		
    public Employee find(int id);
    
    public ReturnStatus delete(int id);
    
    public ReturnStatus update(int id,String firstname,String lastname,Date birthdate,int departmentId);
}
