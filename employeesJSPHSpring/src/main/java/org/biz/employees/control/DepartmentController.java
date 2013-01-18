package org.biz.employees.control;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Servlet implementation class DepartmentController
 */
@Controller
@RequestMapping("/department")
public class DepartmentController  {
	protected static Logger logger = Logger.getLogger("DepartmentController");

	private static String EDIT = "department";
    private static String LIST = "departmentList";

	@Autowired
    private DepartmentDAO dao;
       
	@RequestMapping(value = "/delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam(value="departmentId", required=true) String departmentId, Map<String, Object> model) {		
 
	   logger.info("deleteDepartment");
	   
       List<Department> list;

       int id = Integer.parseInt(departmentId);
       logger.info("delete, delete Id=" + id);
       dao.delete(id);
       list = dao.getAll();
       logger.info("delete, list size =" + list.size());
       model.put("departments", list);
 
       // Wordt gekoppeld aan /jsp/departmentList.jsp
       return LIST;
	}	    

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(@RequestParam(value="departmentId", required=true) String departmentId, Map<String, Object> model) {		
 
	   logger.info("editDepartment");
	   
       int id = Integer.parseInt(departmentId);
       logger.info("edit, edit id = " + id);
       Department dep = dao.findById(id);
       logger.info("edit, edit name = " + dep.getName());
       model.put("department", dep);

       // Wordt gekoppeld aan /jsp/department.jsp
       return EDIT;
	}	    

	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Map<String, Object> model) {		
 
	   logger.info("listDepartments");
	   
       List<Department> list;

       list = dao.getAll();
       logger.info("list, list size =" + list.size());

       model.put("departments", list);

       // Wordt gekoppeld aan /jsp/departmentList.jsp
       return LIST;
	}
    
	@RequestMapping(value = "/save.html", method = RequestMethod.POST)
	public String save(	@RequestParam(value="departmentId", required=true) String departmentId, 
						@RequestParam(value="name", required=true) String name, 
						@RequestParam(value="address", required=true) String address, 
						@RequestParam(value="budget", required=true) String budget, 
						Map<String, Object> model) {
 
		logger.info("saveDepartment");	
				
	    Department dep = new Department();
	    dep.setName(name);
	    dep.setAddress(address);
	    dep.setBudget(Integer.parseInt(budget));
        System.out.println("doPost, name=" +name+" address ="+address+" budget="+budget);
        if(departmentId == null || departmentId.isEmpty())
        {
        	logger.info("save, call dao.save()");
            dao.save(dep);
        }
        else
        {
            int id = Integer.parseInt(departmentId);
        	dep.setDepartmentId(id);
        	logger.info("save, call dao.update()");
            dao.update(id, dep);
        }
        List<Department> list = dao.getAll();
        logger.info("save, list size =" + list.size());           
        model.put("departments", list);
 
	    // Wordt gekoppeld aan /jsp/departmentList.jsp
	    return LIST;
	}	
}