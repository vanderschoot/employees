package org.biz.employees.control.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ManagedBean(name="departmentBean")
@Transactional
//@SessionScoped
public class DepartmentBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger("DepartmentBean");
	
    private List<Department> departments;

    private int departmentId;
    private String name;
    private String address; 
    private int budget;

	@Autowired
    private DepartmentDAO dao;
       
    public List<Department> getDepartments() {		
    	if (departments == null) {
		   logger.info("getDepartments");
	       departments = dao.getAll();
	       logger.info("list, list size =" + departments.size());
    	}
    	return departments;
	}
    
    public String delete(Department dep) {		
       logger.info("delete, delete Id=" + dep.getDepartmentId());
       dao.delete(dep.getDepartmentId());
       departments = null;
       return null;
	}	    
    
    public String edit(Department dep) {
 	   logger.info("edit"); 
	   this.departmentId = dep.getDepartmentId();
	   this.name = dep.getName();
	   this.budget = dep.getBudget();
	   this.address = dep.getAddress();
	   return "department";
	}
    
	public String add() {
		logger.info("add");		
		this.departmentId = -1;
		this.name = "";
		this.budget = 0;
		this.address = "";
		return "department";
	}	


    public String save() {
    	logger.info("save");
    	Department dep = null;
    	if (this.departmentId == -1) {	
        	logger.info("add");
		    dep = new Department();
		    dep.setName(this.name);
		    dep.setAddress(this.address);
		    dep.setBudget(this.budget);
	        dao.save(dep);
    	} else {
        	logger.info("update");
    		dep = dao.findById(this.departmentId);
		    dep.setName(this.name);
		    dep.setAddress(this.address);
		    dep.setBudget(this.budget);   
		    dao.update(this.departmentId, dep);
    	}
        System.out.println("name=" +this.name+" this ="+this.address+" budget="+this.budget);
        departments = null;
	    return "departmentList";
 	}	    
	
    public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}
	
}