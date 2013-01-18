package org.biz.employees.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.dao.EmployeeDAO;
import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	protected static Logger logger = Logger.getLogger("EmployeeController");

    private static String EDIT = "employee";
    private static String LIST = "employeeList";
    
    @Autowired private EmployeeDAO dao;
    @Autowired private DepartmentDAO depdao;
       

	@RequestMapping(value = "/delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam(value="employeeId", required=true) String employeeId, Map<String, Object> model) {		
 
	   logger.info("deleteEmployee");
	   
       List<Employee> list;

       int id = Integer.parseInt(employeeId);
       logger.info("delete, delete Id=" + id);
       dao.delete(id);
       list = dao.getAll();
       logger.info("delete, list size =" + list.size());
       model.put("employees", list);
 
       // Wordt gekoppeld aan /jsp/employeeList.jsp
       return LIST;
	}	    

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(@RequestParam(value="employeeId", required=true) String employeeId, Map<String, Object> model) {		
 
	   logger.info("editEmployee");
	   
       int id = Integer.parseInt(employeeId);
       logger.info("edit, edit id = " + id);
       Employee emp = dao.findById(id);
       logger.info("edit, edit name = " + emp.getFirstName() + " " + emp.getLastName());
       model.put("employee", emp);
   	   List<Department> departments = depdao.getAll();
       model.put("departments", departments);

       // Wordt gekoppeld aan /jsp/department.jsp
       return EDIT;
	}	    

	@RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(Map<String, Object> model) {		
 
	   logger.info("addEmployee");
	   
   	   List<Department> departments = depdao.getAll();
       model.put("departments", departments);

       // Wordt gekoppeld aan /jsp/department.jsp
       return EDIT;
	}	    

	
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Map<String, Object> model) {		
 
	   logger.info("listEmployees");
	   
       List<Employee> list;

       list = dao.getAll();
       logger.info("list, list size =" + list.size());

       model.put("employees", list);

       // Wordt gekoppeld aan /jsp/employeeList.jsp
       return LIST;
	}
	
	@RequestMapping(value = "/list4dep.html", method = RequestMethod.GET)
    public String list4dep(@RequestParam(value="departmentId", required=true) String departmentId, Map<String, Object> model) {		
 
	   logger.info("listEmployees4dep");
	   
       List<Employee> list;

       list = dao.get4Department(Integer.parseInt(departmentId));
       logger.info("list, list size =" + list.size());

       model.put("employees", list);

       // Wordt gekoppeld aan /jsp/employeeList.jsp
       return LIST;
	}

    
	@RequestMapping(value = "/save.html", method = RequestMethod.POST)
	public String save(	@RequestParam(value="employeeId", required=true) String employeeId, 
						@RequestParam(value="firstName", required=true) String firstName, 
						@RequestParam(value="lastName", required=true) String lastName, 
						@RequestParam(value="birthDate", required=true) String sbirthDate, 
						@RequestParam(value="departmentId", required=true) String departmentId, 
						Map<String, Object> model) {
 
		logger.info("saveEmployee");	
				
		Employee emp = new Employee();
	    emp.setFirstName(firstName);
	    emp.setLastName(lastName);
 
		try {
			Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(sbirthDate);
			emp.setBirthDate(birthDate);
			logger.info("save, Emp Id = " + employeeId +  " name=" +firstName+" "+lastName+" birthDate=" + birthDate + " departmentId=" +departmentId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (departmentId != null) {
			int id = Integer.parseInt(departmentId.trim());
			Department dep = depdao.findById(id);
			emp.setDepartment(dep);
		}
        if(employeeId == null || employeeId.isEmpty())
        {
        	logger.info("save, call dao.save()");
            dao.save(emp);
        }
        else
        {
            int id = Integer.parseInt(employeeId);
        	emp.setEmployeeId(id);
        	logger.info("save, call dao.update()");
            dao.update(id, emp);
        }
        List<Employee> list = dao.getAll();
		System.out.println("save, list size =" + list.size());           
        model.put("employees", list);
 
	    // Wordt gekoppeld aan /jsp/employeeList.jsp
	    return LIST;
	}	
}