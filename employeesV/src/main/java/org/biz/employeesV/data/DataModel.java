package org.biz.employeesV.data;

import java.sql.SQLException;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class DataModel {
   
    public static final Object[] DEPARTMENT_COLS = new Object[] {
    	"name", "address", "budget"};

    public static final String[] DEPARTMENT_LABELS = new String[] {
    	"Name", "Address", "Budget" };

    public static final Object[] EMPLOYEE_COLS = new Object[] {
    	"firstName", "lastName", "birthDate", "DepartmentId"};

    public static final String[] EMPLOYEE_LABELS = new String[] {
    	"First Name", "Last Name ", "Birthdate", "Department"};

    public static final Object[] USER_COLS = new Object[] {
    	"userName", "password", "email"};

    public static final String[] USER_LABELS = new String[] {
    	"User Name", "Password", "Email"};

    public static final Object[] ROLE_COLS = new Object[] {
    	"name"};

    public static final String[] ROLE_LABELS = new String[] {
    	"Name"};

    public static final Object[] USERROLE_COLS = new Object[] {
	"RoleId", "UserId"};

    public static final String[] USERROLE_LABELS = new String[] {
	"Role", "User"};
    
    public static final Object[] ROLES4USER_COLS = new Object[] {
	"RoleId", "roleName"};

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
        	QueryDelegate qdepartment = new TableQuery("department", pool.getConnectionPool());
            departmentContainer = new SQLContainer(qdepartment);
            QueryDelegate qemployee = new TableQuery("employee", pool.getConnectionPool());
            employeeContainer = new SQLContainer(qemployee);
            
            employeeContainer.addReference(departmentContainer, "DepartmentId", "departmentId");

        	QueryDelegate quser = new TableQuery("user", pool.getConnectionPool());
            userContainer = new SQLContainer(quser);
            QueryDelegate qrole = new TableQuery("role", pool.getConnectionPool());
            roleContainer = new SQLContainer(qrole);
            QueryDelegate quserrole = new TableQuery("userrole", pool.getConnectionPool());
            userroleContainer = new SQLContainer(quserrole);
            
            userroleContainer.addReference(userContainer, "UserId", "userId");
            userroleContainer.addReference(roleContainer, "RoleId", "roleId");  
            
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
	        		"SELECT userrole.RoleId AS RoleId, name AS roleName FROM user, userrole, role WHERE " +
        			"userrole.UserId = user.userId AND " +
        			"userrole.RoleId = role.roleId AND " +
        			"user.userId = 0";
		} else {
	        qrystring =	"SELECT userrole.RoleId AS RoleId, name AS roleName FROM user, userrole, role WHERE " +
	        			"userrole.UserId = user.userId AND " +
	        			"userrole.RoleId = role.roleId AND " +
	        			"user.userId = " + userId;
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
	    qrystring =	"SELECT userRoleId FROM userrole WHERE " +
        			"userrole.UserId = " + userId + " AND " +
        			"userrole.RoleId = " + roleId;
        System.out.println("getUserRole, query = " + qrystring);
		FreeformQuery query = new FreeformQuery(qrystring,pool.getConnectionPool());
		try {
			SQLContainer res = new SQLContainer(query);
    		Object item = null;
    		Object userRoleId = null;
    		for (Iterator<?> it = res.getItemIds().iterator(); it.hasNext();) {
    			item = res.getItem(it.next());
    			userRoleId = ((Item)item).getItemProperty("userRoleId").getValue();
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
	    qrystring =	"SELECT userName, password FROM user WHERE " +
        			"user.userName = '" + userName + "' AND " +
        			"user.password = '" + password + "'";
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
	    			userNameF = ((Item)item).getItemProperty("userName").getValue();
	    			passwordF = ((Item)item).getItemProperty("password").getValue();
	    		}
	    		if ((userNameF != null) && (passwordF != null)) {
	    	        System.out.println("userName = " + userNameF);
	    	        System.out.println("passwordF = " + passwordF);
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

	
}
