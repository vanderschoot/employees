package org.biz.employees.control.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.biz.employees.model.data.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ManagedBean(name="utilBean")
@Transactional
public class UtilBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger("UtilBean");

	@Autowired private Database db;  
    
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