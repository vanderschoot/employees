package org.biz.employees.control.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.biz.employees.model.data.Database;

@Stateless
@LocalBean
@Named
public class UtilBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger("UtilBean");

	@EJB private Database db;  
    
    public String createdatabase() {		
 
	   logger.info("createdatabase");
	   
	   if (db != null) {
          db.createDatabase();
	   } else {
		  logger.info("db = null");
	   }
 
       return null;
    }
}