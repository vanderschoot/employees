package org.biz.employeesVH.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class DataModel {
   
    public static final Object[] DEPARTMENT_COLS = new Object[] {
    	"NAME", "ADDRESS", "BUDGET"};

    public static final String[] DEPARTMENT_LABELS = new String[] {
    	"Name", "Address", "Budget" };

    public static final Object[] EMPLOYEE_COLS = new Object[] {
    	"FIRSTNAME", "LASTNAME", "BIRTHDATE", "DEPARTMENTID"};

    public static final String[] EMPLOYEE_LABELS = new String[] {
    	"First Name", "Last Name ", "Birthdate", "Department"};

    public static final Object[] USER_COLS = new Object[] {
    	"USERNAME", "PASSWORD", "EMAIL"};

    public static final String[] USER_LABELS = new String[] {
    	"User Name", "Password", "Email"};

    public static final Object[] ROLE_COLS = new Object[] {
    	"NAME"};

    public static final String[] ROLE_LABELS = new String[] {
    	"Name"};

    public static final Object[] USERROLE_COLS = new Object[] {
	"ROLEID", "USERID"};

    public static final String[] USERROLE_LABELS = new String[] {
	"Role", "User"};
    
    public static final Object[] ROLES4USER_COLS = new Object[] {
	"ROLEID", "ROLENAME"};

    public static final String[] ROLES4USER_LABELS = new String[] {
	"Role Id", "Role Name"};

    private SQLContainer departmentContainer = null;
    private SQLContainer employeeContainer = null;
    private SQLContainer userContainer = null;
    private SQLContainer roleContainer = null;
    private SQLContainer userroleContainer = null;
    private ConnectionPool pool = new ConnectionPool();

	public DataModel() {
		System.out.println("DataModel created");
        try {
        	QueryDelegate qdepartment = new TableQuery("DEPARTMENT", pool.getConnectionPool());
            departmentContainer = new SQLContainer(qdepartment);
            QueryDelegate qemployee = new TableQuery("EMPLOYEE", pool.getConnectionPool());
            employeeContainer = new SQLContainer(qemployee);
            
            employeeContainer.addReference(departmentContainer, "DEPARTMENTID", "DEPARTMENTID");

        	QueryDelegate quser = new TableQuery("USER", pool.getConnectionPool());
            userContainer = new SQLContainer(quser);
            QueryDelegate qrole = new TableQuery("ROLE", pool.getConnectionPool());
            roleContainer = new SQLContainer(qrole);
            QueryDelegate quserrole = new TableQuery("USERROLE", pool.getConnectionPool());
            userroleContainer = new SQLContainer(quserrole);
            
            userroleContainer.addReference(userContainer, "USERID", "USERID");
            userroleContainer.addReference(roleContainer, "ROLEID", "ROLEID");  
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public SQLContainer getDepartmentContainer() {
		return departmentContainer;
    }
	
	public SQLContainer getEmployeeContainer() {
		return employeeContainer;
    }
	public SQLContainer getUserContainer() {
		return userContainer;
    }
	public SQLContainer getRoleContainer() {
		return roleContainer;
    }
	public SQLContainer getUserRoleContainer() {
		return userroleContainer;
    }
	
	public SQLContainer getRoles4User(String userId) {
		String qrystring;		
		if (userId.equals("")) {			
	        qrystring =	
	        		"SELECT USERROLE.ROLEID AS ROLEID, NAME AS ROLENAME FROM USER, USERROLE, ROLE WHERE " +
        			"USERROLE.USERID = USER.USERID AND " +
        			"USERROLE.ROLEID = ROLE.ROLEID AND " +
        			"USER.USERID = 0";
		} else {
	        qrystring =	"SELECT USERROLE.ROLEID AS ROLEID, NAME AS ROLENAME FROM USER, USERROLE, ROLE WHERE " +
        			"USERROLE.USERID = USER.USERID AND " +
        			"USERROLE.ROLEID = ROLE.ROLEID AND " +
	        		"USER.USERID = " + userId;
		}
        System.out.println("getRoles4User, query = " + qrystring);
		FreeformQuery query = new FreeformQuery(qrystring,pool.getConnectionPool());
		try {
			SQLContainer res = new SQLContainer(query);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}            
    }
	
	public RowId getUserRole(String userId, String roleId) {
		String qrystring;		
	    qrystring =	"SELECT USERROLEID FROM USERROLE WHERE " +
        			"USERROLE.USERID = " + userId + " AND " +
        			"USERROLE.ROLEID = " + roleId;
        System.out.println("getUserRole, query = " + qrystring);
		FreeformQuery query = new FreeformQuery(qrystring,pool.getConnectionPool());
		try {
			SQLContainer res = new SQLContainer(query);
    		Object item = null;
    		Object userRoleId = null;
    		for (Iterator<?> it = res.getItemIds().iterator(); it.hasNext();) {
    			item = res.getItem(it.next());
    			userRoleId = ((Item)item).getItemProperty("USERROLEID").getValue();
    		}
    		if (userRoleId != null) {
    			return new RowId(new Object[] {userRoleId});
    		} else {
    			return null;
    		}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}            
    }

	public boolean Login(String userName, String password) {
		String qrystring;		
	    qrystring =	"SELECT USERNAME, PASSWORD FROM USER WHERE " +
        			"USER.USERNAME = '" + userName + "' AND " +
        			"USER.PASSWORD = '" + password + "'";
        System.out.println("Login, query = " + qrystring);
        
        if ((userName.contentEquals("")) || (password.contentEquals(""))) {
        	return false;
        } else {
			FreeformQuery query = new FreeformQuery(qrystring,pool.getConnectionPool());
			try {
				SQLContainer res = new SQLContainer(query);
	    		Object item = null;
	    		Object userNameF = null;
	    		Object passwordF = null;
	    		for (Iterator<?> it = res.getItemIds().iterator(); it.hasNext();) {
	    			item = res.getItem(it.next());
	    			userNameF = ((Item)item).getItemProperty("USERNAME").getValue();
	    			passwordF = ((Item)item).getItemProperty("PASSWORD").getValue();
	    		}
	    		if ((userNameF != null) && (passwordF != null)) {
	    	        System.out.println("USERNAMEF = " + userNameF);
	    	        System.out.println("PASSWORDF = " + passwordF);
	    			if ((userNameF.equals(userName)) && (passwordF.equals(password))) {
	    	   			return true;
	    			} else {
	    	   			return false;    				
	    			}
	     		} else {
	    			return false;
	    		}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} 
        }
    }
	
	public List<String> getroles(String userName) {
		System.out.println("checkrole");

		String qrystring;		
	    qrystring =	"SELECT ROLE.NAME FROM USERROLE, ROLE, USER WHERE " +
        			"USERROLE.ROLEID = ROLE.ROLEID AND " +
        			"USERROLE.USERID = USER.USERID AND " +
        			"USER.USERNAME = '" + userName + "'";
        System.out.println("Login, query = " + qrystring);

        if (userName.contentEquals("")) {
        	return null;
        } else {
			FreeformQuery query = new FreeformQuery(qrystring,pool.getConnectionPool());
			try {
				SQLContainer res = new SQLContainer(query);
	    		Object item = null;
	    		String roleName = null;
	    		List<String> roles = new ArrayList<String>();
	    		for (Iterator<?> it = res.getItemIds().iterator(); it.hasNext();) {
	    			item = res.getItem(it.next());
	    			roleName = ((Item)item).getItemProperty("NAME").getValue().toString();
	    			roles.add(roleName);
	    		}
	    		return roles;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} 
        }
		
	}


	
}
