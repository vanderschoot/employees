package org.biz.employees.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.dao.UserDAO;
import org.biz.employees.model.dao.UserRoleDAO;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Servlet implementation class UserController
 */
@Controller
@RequestMapping("/user")
public class UserController {
	protected static Logger logger = Logger.getLogger("UserController");
    private static String EDIT = "user";
    private static String LIST = "userList";
    
    @Autowired private UserDAO dao;
    @Autowired private RoleDAO roledao;
    @Autowired private UserRoleDAO usrldao;
    
	@RequestMapping(value = "/delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam(value="userId", required=true) String userId, Map<String, Object> model) {		
 
	   logger.info("deleteUser");
	   
       List<User> list;

       int id = Integer.parseInt(userId);
       logger.info("delete, delete Id=" + id);
       usrldao.delroles(id);
       dao.delete(id);
       list = dao.getAll();
       logger.info("delete, list size =" + list.size());
       model.put("users", list);
       
       // Wordt gekoppeld aan /jsp/userList.jsp
       return LIST;
	}	    

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(@RequestParam(value="userId", required=true) String userId, Map<String, Object> model) {		
 
	   logger.info("editUser");
	   
       int id = Integer.parseInt(userId);
       logger.info("edit, edit id = " + id);
       User user = dao.findById(id);
       logger.info("edit, edit name = " + user.getUserName());
       model.put("user", user);

       List<Role> roles = roledao.getAll();
       logger.info("edit, roles size = " + roles.size());
       model.put("roles", roles);

       List<UserRole> usroles = usrldao.list(user.getUserId(), 0);
       logger.info("edit, usroles size = " + usroles.size());
       model.put("usroles", usroles);

       // Wordt gekoppeld aan /jsp/user.jsp
       return EDIT;
 	}	    

	@RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(Map<String, Object> model) {		
 
	   logger.info("editUser");
	   
       User user = new User();
       user.setUserId(-1);
       model.put("user", user);

       List<Role> roles = roledao.getAll();
       logger.info("edit, roles size = " + roles.size());
       model.put("roles", roles);
       List<UserRole> usroles = new ArrayList<UserRole>();
       model.put("usroles", usroles);

       // Wordt gekoppeld aan /jsp/user.jsp
       return EDIT;
 	}	    

	
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Map<String, Object> model) {		
 
	   logger.info("listUsers");
	   
       List<User> list;

       list = dao.getAll();
       logger.info("list, list size =" + list.size());

       model.put("users", list);

       // Wordt gekoppeld aan /jsp/userList.jsp
       return LIST;
	}
    
	@RequestMapping(value = "/save.html", method = RequestMethod.POST)
	public String save(	@RequestParam(value="userId", required=true) String userId, 
						@RequestParam(value="userName", required=true) String userName, 
						@RequestParam(value="password", required=true) String password, 
						@RequestParam(value="email", required=true) String email, 
						@RequestParam(value="roleId", required=true) String[] roleIds, 
						Map<String, Object> model) {
 
		logger.info("saveEmployee");	
	    
	    User user = new User();
	    user.setUserName(userName);
	    user.setPassword(password);
	    user.setEmail(email);

	    
        for(int i=0; i< roleIds.length; i++) {
        	logger.info("selected role : " + roleIds[i]);
        }

        logger.info("save, name=" +userName+" password ="+password+" email="+email);
        if(userId == null || userId.isEmpty() || userId.equals("-1"))
        {
        	logger.info("save, call dao.save()");
            dao.save(user);
            List<User> users = dao.findUsers(userName);
	        usrldao.createroles(users.get(0).getUserId(), roleIds);
        }
        else
        {
        	logger.info("save, userId=" + userId);
            int id = Integer.parseInt(userId);
        	user.setUserId(id);
	        usrldao.delroles(id);
	        usrldao.createroles(id, roleIds);
	        logger.info("doPost, call dao.update()");
            dao.update(id, user);
        }
        List<User> list = dao.getAll();
        logger.info("save, list size =" + list.size());           
        model.put("users", list);
        
	    // Wordt gekoppeld aan /jsp/userList.jsp
	    return LIST;    
	}	 
       
}