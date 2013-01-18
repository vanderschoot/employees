package org.biz.employees.control;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Servlet implementation class RoleController
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	protected static Logger logger = Logger.getLogger("RoleController");
    private static String EDIT = "role";
    private static String LIST = "roleList";

    @Autowired private RoleDAO dao;

	@RequestMapping(value = "/delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam(value="roleId", required=true) String roleId, Map<String, Object> model) {		
 
	   logger.info("deleteROle");
	   
       List<Role> list;

       int id = Integer.parseInt(roleId);
       logger.info("delete, delete Id=" + id);
       dao.delete(id);
       list = dao.getAll();
       logger.info("delete, list size =" + list.size());
       model.put("roles", list);
 
       // Wordt gekoppeld aan /jsp/roleList.jsp
       return LIST;
	}	    

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(@RequestParam(value="roleId", required=true) String roleId, Map<String, Object> model) {		
 
	   logger.info("editRole");
	   
       int id = Integer.parseInt(roleId);
       logger.info("edit, edit id = " + id);
       Role role = dao.findById(id);
       logger.info("edit, edit name = " + role.getName());
       model.put("role", role);

       // Wordt gekoppeld aan /jsp/role.jsp
       return EDIT;
	}	    

	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Map<String, Object> model) {		
 
	   logger.info("listRoles");
	   
       List<Role> list;

       list = dao.getAll();
       logger.info("list, list size =" + list.size());

       model.put("roles", list);

       // Wordt gekoppeld aan /jsp/roleList.jsp
       return LIST;
	}
    
	@RequestMapping(value = "/save.html", method = RequestMethod.POST)
	public String save(	@RequestParam(value="roleId", required=true) String roleId, 
						@RequestParam(value="name", required=true) String name, 
						Map<String, Object> model) {
 
		logger.info("saveEmployee");	
				
		Role role = new Role();
	    role.setName(name);

        if(roleId == null || roleId.isEmpty())
        {
        	logger.info("save, call dao.save()");
            dao.save(role);
        }
        else
        {
            int id = Integer.parseInt(roleId.trim());
        	role.setRoleId(id);
        	logger.info("save, call dao.update()");
            dao.update(id, role);
        }
        List<Role> list = dao.getAll();
        logger.info("save, list size =" + list.size());           
        model.put("roles", list);
 
	    // Wordt gekoppeld aan /jsp/roleList.jsp
	    return LIST;
	}	    
}