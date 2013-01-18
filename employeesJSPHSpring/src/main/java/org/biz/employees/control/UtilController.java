package org.biz.employees.control;

import org.apache.log4j.Logger;
import org.biz.employees.model.data.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/util")
public class UtilController  {
	protected static Logger logger = Logger.getLogger("UtilController");

	@Autowired private Database db;  
    
	@RequestMapping(value = "/createdatabase.html", method = RequestMethod.GET)
    public String createdatabase() {		
 
	   logger.info("createdatabase");
	   
       db.createDatabase();
 
       // Wordt gekoppeld aan /jsp/index.jsp
       return "index";
	}	    

	@RequestMapping(value = "/serverconfig.html", method = RequestMethod.GET)
    public String serverconfig() {		
 
	   logger.info("serverconfig");
	   
       // Wordt gekoppeld aan /jsp/server.jsp
       return "server";
	}	
	
	@RequestMapping(value = "/saveserverconfig.html", method = RequestMethod.POST)
    public String saveserverconfig() {		
 
	   logger.info("saveserverconfig");
	   
       // Wordt gekoppeld aan /jsp/index.jsp
       return "index";
	}	    
      	
}