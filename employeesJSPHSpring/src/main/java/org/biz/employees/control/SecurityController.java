package org.biz.employees.control;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.biz.employees.model.service.ReturnStatus;
import org.biz.employees.model.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/security")
public class SecurityController {
	protected static Logger logger = Logger.getLogger("SecurityController");	
	
	@Autowired private SecurityService sec;
	
	private ReturnStatus rs = null;
    Gson gs = new Gson();
       
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	@ResponseBody
    public String login(HttpServletRequest request, @RequestHeader("Authorization") String auth) {		

       logger.info("login");
       rs = sec.login(request, auth);
       logger.info("login, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
       
       String json = gs.toJson(rs);
       return(json);
	}	
	
	@RequestMapping(value = "/loggedin.html", method = RequestMethod.GET)
	@ResponseBody
    public String loggedin(@RequestParam("userName") String userName) {		
 
       logger.info("loggedin");
	   rs = sec.loggedin(userName);

       logger.info("loggedin, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
       
       String json = gs.toJson(rs);
       return(json);
	}	
	
	@RequestMapping(value = "/logout.html", method = RequestMethod.GET)
	@ResponseBody
    public String logout(@RequestParam("userName") String userName) {		
 
       logger.info("logout");
	   rs = sec.logout(userName);

       logger.info("logout, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
       
       String json = gs.toJson(rs);
       return(json);
	}	    

	@RequestMapping(value = "/register.html", method = RequestMethod.GET)
	@ResponseBody
    public String register(	@RequestParam("userName") String userName,
    						@RequestParam("email") String email,
    						@RequestParam("password") String password) {		
 
       logger.info("register");
       rs = sec.register(email, userName, password);      
       String json = gs.toJson(rs);
       return(json);
	}	    

	@RequestMapping(value = "/askpassword.html", method = RequestMethod.GET)
	@ResponseBody
    public String askpassword(	@RequestParam("userName") String userName,
    							@RequestParam("email") String email) {		
 
       logger.info("askpassword");
       rs = sec.askpassword(email, userName);      
       String json = gs.toJson(rs);
       return(json);
	}	
	
	@RequestMapping(value = "/checkrole.html", method = RequestMethod.GET)
	@ResponseBody
    public String checkrole(@RequestParam("user") String user,
    						@RequestParam("rol") String role) {		
 
       logger.info("askpassword");
       rs = sec.checkrole(user, role);      
       String json = gs.toJson(rs);
       return(json);
	}	    


}