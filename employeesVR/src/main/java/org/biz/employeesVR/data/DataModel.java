package org.biz.employeesVR.data;

import java.util.Date;
import java.util.Collection;
import java.util.List;

import org.biz.employeesVR.data.DTO.Department;
import org.biz.employeesVR.data.DTO.Employee;
import org.biz.employeesVR.data.DTO.Role;
import org.biz.employeesVR.data.DTO.User;
import org.biz.employeesVR.data.DTO.UserRole;
import org.biz.employeesVR.MainApp;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

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
    
	private MainApp app = null;
	RestServiceFactory rsf = null;

    private IndexedContainer departmentContainer = null;
    private IndexedContainer employeeContainer = null;
    private IndexedContainer userContainer = null;
    private IndexedContainer roleContainer = null;
    private IndexedContainer userroleContainer = null;

	public DataModel(MainApp app) {
		System.out.println("DataModel created");
		this.app = app;
 	}
	
	public boolean getData() {
		System.out.println("getData");
		rsf = new RestServiceFactory(app);
		
		departmentContainer = getDepartments();
		if (departmentContainer != null) {
	        employeeContainer = getEmployees();
			
	        userContainer = getUsers();
	        roleContainer = getRoles();
	        userroleContainer = getUserRoles();  		
			app.showNotification("Server connected.");
			return true;
		} else {
			return false;
		}
	}

	private IndexedContainer getDepartments() {
		System.out.println("getDepartments");
		List<Department> departments = rsf.getDepartments();
		if (departments != null) {
			IndexedContainer container = new IndexedContainer();
			
			container.addContainerProperty("ID", Integer.class, "");
			container.addContainerProperty("name", String.class, "");
			container.addContainerProperty("address", String.class, "");
			container.addContainerProperty("budget", Integer.class, "");
			
			for (Department dep : departments) {
				System.out.println("getDepartments, department : " + dep.getName() + " / departmentId = " + dep.getDepartmentId());
				Object id = container.addItem();
				container.getContainerProperty(id, "ID").setValue(dep.getDepartmentId());
				container.getContainerProperty(id, "name").setValue(dep.getName());
				container.getContainerProperty(id, "address").setValue(dep.getAddress());
				container.getContainerProperty(id, "budget").setValue(dep.getBudget());
			}
			return container;
		} else {
			return null;
		}
	}
	
	public void refreshDepartments() {		
        departmentContainer = getDepartments();
	}
	
	public ReturnStatus deleteDepartment(Object itemId) {
		System.out.println("deleteDepartment, itemId = " + itemId);
		System.out.println("deleteDepartment, item = " + departmentContainer.getItem(itemId));
		System.out.println("deleteDepartment, departmentId = " + departmentContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.deleteDepartment(departmentContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
	}

	public ReturnStatus updateDepartment(Object itemId, String name, String address, String budget) {
		System.out.println("updateDepartment, itemId = " + itemId + " / name = " + name + " / address = " + address + " / budget = " + budget);
		System.out.println("updateDepartment, item = " + departmentContainer.getItem(itemId));
		System.out.println("updateDepartment, departmentId = " + departmentContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.updateDepartment(departmentContainer.getItem(itemId).getItemProperty("ID").getValue().toString(),
				name, address, budget);
	}

	public ReturnStatus addDepartment(String name, String address, String budget) {
		System.out.println("addDepartment, name = " + name + " / address = " + address + " / budget = " + budget);
		return rsf.addDepartment(name, address, budget);
	}

	private IndexedContainer getEmployees() {
		System.out.println("getEmployees");
		List<Employee> employees = rsf.getEmployees();
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("ID", Integer.class, "");
		container.addContainerProperty("firstName", String.class, "");
		container.addContainerProperty("lastName", String.class, "");
		container.addContainerProperty("birthDate", Date.class, new Date());
		container.addContainerProperty("DepartmentId", Integer.class, "");

		for (Employee emp : employees) {
			System.out.println("getEmployees, emp : " + emp.getFirstName() + " / departmentId = " + emp.getDepartment().getDepartmentId() + " / department = " + emp.getDepartment().getName());
			Object id = container.addItem();
			container.getContainerProperty(id, "ID").setValue(emp.getEmployeeId());
			container.getContainerProperty(id, "firstName").setValue(emp.getFirstName());
			container.getContainerProperty(id, "lastName").setValue(emp.getLastName());
			container.getContainerProperty(id, "birthDate").setValue(emp.getBirthDate());
			container.getContainerProperty(id, "DepartmentId").setValue(emp.getDepartment().getDepartmentId());
		}
		return container;
	}
	
	public void refreshEmployees() {		
        employeeContainer = getEmployees();
	}

	public ReturnStatus deleteEmployee(Object itemId) {
		System.out.println("deleteEmployee, itemId = " + itemId);
		System.out.println("deleteEmployee, item = " + employeeContainer.getItem(itemId));
		System.out.println("deleteEmployee, employeeId = " + employeeContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.deleteEmployee(employeeContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
	}

	public ReturnStatus updateEmployee(Object itemId, String firstName, String lastName, Date birthDate, String departmentId) {
		System.out.println("updateEmployee, itemId = " + itemId + " / firstName = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDate + " / departmentId = " + departmentId);
		System.out.println("updateEmployee, item = " + employeeContainer.getItem(itemId));
		System.out.println("updateEmployee, employeeId = " + employeeContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.updateEmployee(employeeContainer.getItem(itemId).getItemProperty("ID").getValue().toString(),firstName, lastName, birthDate,departmentId);
	}

	public ReturnStatus addEmployee(String firstName, String lastName, Date birthDate, String departmentId) {
		System.out.println("addEmployee, firstName = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDate + " / departmentId = " + departmentId);
		return rsf.addEmployee(firstName, lastName, birthDate, departmentId);
	}


	private IndexedContainer getUsers() {
		Collection<User> users = rsf.getUsers();
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("ID", Integer.class, "");
		container.addContainerProperty("userName", String.class, "");
		container.addContainerProperty("password", String.class, "");
		container.addContainerProperty("email", String.class, "");

		for (User usr : users) {
			Object id = container.addItem();
			container.getContainerProperty(id, "ID").setValue(usr.getUserId());
			container.getContainerProperty(id, "userName").setValue(usr.getUserName());
			container.getContainerProperty(id, "password").setValue(usr.getPassword());
			container.getContainerProperty(id, "email").setValue(usr.getEmail());
		}
		return container;
	}
	
	public void refreshUsers() {		
        userContainer = getUsers();
	}

	
	public ReturnStatus deleteUser(Object itemId) {
		System.out.println("deleteUser, itemId = " + itemId);
		System.out.println("deleteUser, item = " + userContainer.getItem(itemId));
		System.out.println("deleteUser, userId = " + userContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.deleteUser(userContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
	}

	public ReturnStatus updateUser(Object itemId, String email, String userName, String password) {
		System.out.println("updateUser, itemId = " + itemId + " / email = " + email + " / userName = " + userName + " / password = " + password);
		System.out.println("updateUser, item = " + userContainer.getItem(itemId));
		System.out.println("updateUser, userId = " + userContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.updateUser(userContainer.getItem(itemId).getItemProperty("ID").getValue().toString(),email, userName, password);
	}

	public ReturnStatus addUser(String email, String userName, String password) {
		System.out.println("addUser, email = " + email + " / userName = " + userName + " / password = " + password);
		return rsf.addUser(email, userName, password);
	}


	public IndexedContainer getRoles() {
		List<Role> roles = rsf.getRoles();
		
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("ID", Integer.class, "");
		container.addContainerProperty("name", String.class, "");

		for (Role role : roles) {
			Object id = container.addItem();
			container.getContainerProperty(id, "ID").setValue(role.getRoleId());
			container.getContainerProperty(id, "name").setValue(role.getName());
		}
		return container;
	}
	
	public void refreshRoles() {		
        roleContainer = getRoles();
	}

	
	public ReturnStatus deleteRole(Object itemId) {
		System.out.println("deleteRole, itemId = " + itemId);
		System.out.println("deleteRole, item = " + roleContainer.getItem(itemId));
		System.out.println("deleteRole, roleId = " + roleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.deleteRole(roleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
	}

	public ReturnStatus updateRole(Object itemId, String name) {
		System.out.println("updateRole, itemId = " + itemId + " / name = " + name);
		System.out.println("updateRole, item = " + roleContainer.getItem(itemId));
		System.out.println("updateRole, roleId = " + roleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.updateRole(roleContainer.getItem(itemId).getItemProperty("ID").getValue().toString(),name);
	}

	public ReturnStatus addRole(String name) {
		System.out.println("addRole, name = " + name);
		return rsf.addRole(name);
	}


	private IndexedContainer getUserRoles() {
		Collection<UserRole> userroles = rsf.getUserRoles();
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("ID", Integer.class, "");
		container.addContainerProperty("RoleId", Integer.class, "");
		container.addContainerProperty("UserId", Integer.class, "");

		for (UserRole usrl : userroles) {
			Object id = container.addItem();
			container.getContainerProperty(id, "ID").setValue(usrl.getUserRoleId());
			container.getContainerProperty(id, "RoleId").setValue(usrl.getRole().getRoleId());
			container.getContainerProperty(id, "UserId").setValue(usrl.getUser().getUserId());
		}
		return container;
	}
	
	public void refresUserRoles() {		
        userroleContainer = getUserRoles();
	}

	public ReturnStatus deleteUserRole(Object itemId) {
		System.out.println("deleteUserRole, itemId = " + itemId);
		System.out.println("deleteUserRole, item = " + userroleContainer.getItem(itemId));
		System.out.println("deleteUserRole, userRoleId = " + userroleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.deleteUserRole(userroleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
	}

	public ReturnStatus updateUserRole(Object itemId, String roleId, String userId) {
		System.out.println("updateUserRole, itemId = " + itemId + " / roleId = " + roleId + " / userId = " + userId);
		System.out.println("updateUserRole, item = " + userroleContainer.getItem(itemId));
		System.out.println("updateUserRole, userRoleId = " + userroleContainer.getItem(itemId).getItemProperty("ID").getValue().toString());
		return rsf.updateUserRole(userroleContainer.getItem(itemId).getItemProperty("ID").getValue().toString(),roleId,userId);
	}

	public ReturnStatus addUserRole(String roleId, String userId) {
		System.out.println("addUserRole, roleId = " + roleId + " / userId = " + userId);
		return rsf.addUserRole(roleId,userId);
	}

	public IndexedContainer getDepartmentContainer() {
		return departmentContainer;
    }
	
	public IndexedContainer getEmployeeContainer() {
		return employeeContainer;
    }
	public IndexedContainer getUserContainer() {
		return userContainer;
    }
	public IndexedContainer getRoleContainer() {
		return roleContainer;
    }
	public IndexedContainer getUserRoleContainer() {
		return userroleContainer;
    }
	
	public IndexedContainer getRoles4User(String userId) {
		if (userId != null) {
			IndexedContainer res = new IndexedContainer();
			res.addContainerProperty("ID", Integer.class, "");
			res.addContainerProperty("RoleId", Integer.class, "");
			res.addContainerProperty("roleName", String.class, "");
			
			userroleContainer.removeAllContainerFilters();
			userroleContainer.addContainerFilter("UserId", userId, true, false);
			
			Collection<?> ids = userroleContainer.getItemIds();
	
			for (Object id : ids) {
				System.out.println("getRoles4User, Item id = " + id);
				Item usrl = userroleContainer.getItem(id);
				Object userRoleId = userroleContainer.getItem(id).getItemProperty("ID").getValue();
				String roleId = usrl.getItemProperty("RoleId").getValue().toString();
				System.out.println("getRoles4User, userRoleId = " + userRoleId + " / roleId = " + roleId);
				Collection<?> rids = roleContainer.getItemIds();
				Item role = null;
				Object roleName = null;
				for (Object rid : rids) {
					role = roleContainer.getItem(rid);
					if (role.getItemProperty("ID").getValue().toString().equalsIgnoreCase(roleId)) {
						roleName = role.getItemProperty("name").getValue();
						System.out.println("getRoles4User, roleName = " + roleName.toString());
					}
				}
				Object newid = res.addItem();
				res.getContainerProperty(newid, "ID").setValue(usrl.getItemProperty("ID").getValue());
				res.getContainerProperty(newid, "RoleId").setValue(usrl.getItemProperty("RoleId").getValue());
				res.getContainerProperty(newid, "roleName").setValue(roleName);
			}	
			userroleContainer.removeAllContainerFilters();
			return res;
		} else {
			userroleContainer.removeAllContainerFilters();
			return null;
		}
    }
	
	public Object getUserRole(String userId, String roleId) {
		System.out.println("getUserRole, userId = " + userId + " / roleId = " + roleId);
	
		Object userRoleId = null;
		Object ItemId = null;
		
		if ((userId != null) && (roleId != null)) {
			userroleContainer.removeAllContainerFilters();
			userroleContainer.addContainerFilter("UserId", userId, true, false);
			userroleContainer.addContainerFilter("RoleId", roleId, true, false);
			
			Collection<?> ids = userroleContainer.getItemIds();
	
			if (userroleContainer.size() > 0) {
				for (Object id : ids) {
					System.out.println("getUserRole, Item id = " + id);
					ItemId = id;
					userRoleId = userroleContainer.getItem(id).getItemProperty("ID").getValue();
					System.out.println("getUserRole, userRoleId = " + userRoleId);
				}
				userroleContainer.removeAllContainerFilters();
				return ItemId;
			} else {
				userroleContainer.removeAllContainerFilters();
				return null;
			}
		} else {
			userroleContainer.removeAllContainerFilters();
			return null;
		}
    }

	public boolean Login(String userName, String password) {
		System.out.println("Login, userName = " + userName + " / password = " + password);
		
		String userNameF = ""; String passwordF = "";
		
		if ((!userName.contentEquals("")) && (!password.contentEquals(""))) {
			userContainer.removeAllContainerFilters();
			userContainer.addContainerFilter("userName", userName, true, false);
			userContainer.addContainerFilter("password", password, true, false);
			
			Collection<?> ids = userContainer.getItemIds();
	
			if (userContainer.size() > 0) {
				for (Object id : ids) {
					System.out.println("Login, Item id = " + id);
					userNameF = userContainer.getItem(id).getItemProperty("userName").getValue().toString();
					passwordF = userContainer.getItem(id).getItemProperty("password").getValue().toString();
					System.out.println("Login, userNameF = " + userNameF + " /  passwordF = " + passwordF);
				}
    			if ((userNameF.equals(userName)) && (passwordF.equals(password))) {
    	   			return true;
    			} else {
    	   			return false;    				
    			}
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
	
	public RestServiceFactory getRestServiceFactory() {
		return rsf;
	}
	
}
