package org.biz.employeesRESTH.rest;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton()
@Lock(LockType.WRITE)
@Path("/util")
//@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
@Produces({MediaType.TEXT_PLAIN})
@DeclareRoles({"Administrator", "Anonymous","User"})
public class UtilServiceImpl {
    @PersistenceContext
    private EntityManager em;

    public UtilServiceImpl() {
    	System.out.println("REST Webservice UtilServiceImpl created");
    }

 	@Path("/query/{sql}")
    @POST
    @RolesAllowed({"Administrator"})
    public String query(@PathParam("sql") String sql) {
 		String result = "";
 	 	Statement st;
 	    try {
			Class.forName("com.mysql.jdbc.Driver");
	 	    System.out.println("Driver Loaded.");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:employeedb","sa","");            

	 	    System.out.println("Got Connection.");
	 	    st = conn.createStatement();

	 	    ResultSet rs = st.executeQuery(sql);
	 	    ResultSetMetaData metadata = rs.getMetaData();

	 	    for (int i = 0; i < metadata.getColumnCount(); i++) {
	 	      result = result + "\t"+ metadata.getColumnLabel(i + 1);
	 	    }
	 	    result = result + "\n----------------------------------\n";

	 	    while (rs.next()) {
	 	      for (int i = 0; i < metadata.getColumnCount(); i++) {
	 	        Object value = rs.getObject(i + 1);
	 	        if (value == null) {
	 	          result = result + "\t       ";
	 	        } else {
	 	 	      result = result + "\t"+value.toString().trim();
	 	        }
	 	      }
	 	      result = result + "\n";
	 	    }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
	    } 		
	    System.out.println("Result = \n" + result );
 		return result;
    } 		
}
