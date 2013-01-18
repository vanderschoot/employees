package org.biz.employees.control.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.dao.EmployeeDAO;
import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;

@Stateless
@LocalBean
@Named
public class EmployeeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger("EmployeeBean");
	
    private List<Employee> employees = null;
    private boolean listFilled = false;
    private boolean showAll = true;

	private int employeeId;
    private String firstName;
    private String lastName; 
    private Date birthDate = new Date();
    
	private int departmentId;
	private Department  department = new Department();

	@EJB private EmployeeDAO dao;

	@EJB private DepartmentDAO daoDep;
	
	public EmployeeBean() {
	}

    public List<Employee> getEmployees() {		
	   int id = department.getDepartmentId();
	   logger.info("getEmployees, listFilled = " + listFilled + " / showAll = " + showAll + " / departmentId = " + id);
	   
	   if (!showAll) {
		   if (!listFilled) {
			   logger.info("get4Department");
			   employees = dao.get4Department(id);
			   listFilled = true;
		   }
	   } else {
		   // JSF datatable calls this method for each item in the list
		   // So performance protection is needed here to cache te list
		   if (!listFilled) {
			   logger.info("getAll");
			   employees = dao.getAll();
			   listFilled = true;
		   }
	   }
       logger.info("list, list size =" + employees.size());
       return employees;
	}

    public String show4Department(Department department) {		
	   logger.info("show4Department");
	   this.department = department;	   
	   showAll = false;
	   listFilled = false;
       return "employeeList";
	}

    public String showAll() {		
	   logger.info("showAll");
	   showAll = true;
	   listFilled = false;
       return "employeeList";
	}

    public String delete(Employee emp) {		
       logger.info("delete, delete Id=" + emp.getEmployeeId());
       dao.delete(emp.getEmployeeId());
	   listFilled = false;
       return null;
	}	    
    
    public String edit(Employee emp) {
 	   logger.info("edit"); 
	   this.employeeId = emp.getEmployeeId();
	   this.firstName = emp.getFirstName();
	   this.lastName = emp.getLastName();
	   this.birthDate = emp.getBirthDate();
	   this.departmentId = emp.getDepartment().getDepartmentId();
	   return "employee";
	}
    
	public String add() {
		logger.info("add");		
		this.employeeId = -1;
		this.firstName = "";
		this.lastName = "";
		this.birthDate = new Date();
		this.departmentId = 0;
		return "employee";
	}	

    public String save() {
    	logger.info("save");
    	Employee emp = null;
    	if (this.employeeId == -1) {	
        	logger.info("add");
		    emp = new Employee();
		    emp.setFirstName(this.firstName);
		    emp.setLastName(this.lastName);
		    emp.setBirthDate(this.birthDate);
		    emp.setDepartment(daoDep.findById(this.departmentId));
	        dao.save(emp);
    	} else {
        	logger.info("update");
    		emp = dao.findById(this.employeeId);
		    emp.setFirstName(this.firstName);
		    emp.setLastName(this.lastName);
		    emp.setBirthDate(this.birthDate);
		    emp.setDepartment(daoDep.findById(this.departmentId));
		    dao.update(this.employeeId, emp);
    	}
        System.out.println("name=" +this.firstName+" " +this.lastName+" / birthDate ="+this.birthDate);
        listFilled = false;
	    return "employeeList";
 	}	
    
    public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

}