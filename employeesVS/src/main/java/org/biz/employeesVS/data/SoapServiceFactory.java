package org.biz.employeesVS.data;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import java.net.URL;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.biz.employeesVS.MainApp;
import org.biz.employeesVS.UI.screens.Connect;
import org.biz.employeesVS.data.DTO.Department;
import org.biz.employeesVS.data.DTO.Employee;
import org.biz.employeesVS.data.DTO.Role;
import org.biz.employeesVS.data.DTO.User;
import org.biz.employeesVS.data.DTO.UserRole;
import org.biz.employeesVS.ws.DepartmentService;
import org.biz.employeesVS.ws.EmployeeService;
import org.biz.employeesVS.ws.ReturnStatus;
import org.biz.employeesVS.ws.RoleService;
import org.biz.employeesVS.ws.SecurityService;
import org.biz.employeesVS.ws.UserRoleService;
import org.biz.employeesVS.ws.UserService;

public class SoapServiceFactory {
	private String userName;
	private String password;
	
	private MainApp app;
	
	private String defaultURL = "http://localhost:8082/employeesSOAPH";
	private String baseURL = "";
	static final Logger logger = Logger.getLogger(SoapServiceFactory.class);
	
	public SoapServiceFactory(MainApp app) {
		this.app = app;
		BasicConfigurator.configure();
		logger.debug("SoapServiceFactory Created");			
		//baseURL = System.getenv("URL_EMPLOYEES");
		Connect con = app.getHeader().getConnection();
		baseURL = "http://" + con.getServerHost() + ":" + con.getServerPort() + "/" + con.getServerProject();
		if (baseURL == null) {
			baseURL = defaultURL;
			logger.debug("Could not read URL form settings. Uses default url " + baseURL);
		} else {
			logger.debug("Uses url from settings : " + baseURL);			
		}
	}
	
	public List<Department> getDepartments() {
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/DepartmentServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "DepartmentServiceWS"));
	        DepartmentService department = service.getPort(DepartmentService.class);
	        return department.list(0,20);		
		} catch (WebServiceException e) {
			//e.printStackTrace();
			app.showError("Verbinding met server mislukt (Webservice fout)!!!");
			return null;
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			app.showError("Verbinding met server mislukt (URL fout)!!!");
			return null;
		}
	}

	public ReturnStatus addDepartment(String name, String address, String budget) {
		System.out.println("SoapServiceFactory, addDepartment, name  = " + name + " / address = " + address + " / budget = " + budget + " / userName = " + userName + " /  password = " + password);

	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/DepartmentServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "DepartmentServiceWS"));
	        DepartmentService department = service.getPort(DepartmentService.class);
	        
	        ReturnStatus sts = department.create(name, address, Integer.parseInt(budget));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, addDepartment, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, addDepartment, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReturnStatus updateDepartment(String departmentId, String name, String address, String budget) {
		System.out.println("SoapServiceFactory, updateDepartment, departmentId = " + departmentId + " name  = " + name + " / address = " + address + " / budget = " + budget);
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/DepartmentServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "DepartmentServiceWS"));
	        DepartmentService department = service.getPort(DepartmentService.class);
	        
	        ReturnStatus sts = department.update(Integer.parseInt(departmentId), name, address, Integer.parseInt(budget));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, updateDepartment, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, updateDepartment, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus deleteDepartment(String departmentId) {
		System.out.println("SoapServiceFactory, deleteDepartment, departmentId = " + departmentId);
		
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/DepartmentServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "DepartmentServiceWS"));
	        DepartmentService department = service.getPort(DepartmentService.class);
	        
	        ReturnStatus sts = department.delete(Integer.parseInt(departmentId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, deleteDepartment, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, deleteDepartment, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Employee> getEmployees() {
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/EmployeeServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "EmployeeServiceWS"));
			EmployeeService employee = service.getPort(EmployeeService.class);
	        return employee.list(-1);		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
			
	public ReturnStatus addEmployee(String firstName, String lastName, Date birthDate, String departmentId) {
		//String birthDateString = new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
		System.out.println("SoapServiceFactory, addEmployee, firstName  = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDate + "  / departmentId = " + departmentId +  " / userName = " + userName + " /  password = " + password);

	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/EmployeeServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "EmployeeServiceWS"));
			EmployeeService employee = service.getPort(EmployeeService.class);
	        
	        ReturnStatus sts = employee.create(firstName, lastName,birthDate, Integer.parseInt(departmentId));
    		
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, addEmployee, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, addEmployee, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}		
	}	
	
	public ReturnStatus updateEmployee(String employeeId, String firstName, String lastName, Date birthDate, String departmentId) {
		String birthDateString = new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
		System.out.println("SoapServiceFactory, updateEmployee, firstName  = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDateString + "  / departmentId = " + departmentId +  " / userName = " + userName + " /  password = " + password);
		
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/EmployeeServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "EmployeeServiceWS"));
			EmployeeService employee = service.getPort(EmployeeService.class);
	        
	        ReturnStatus sts = employee.update(Integer.parseInt(employeeId), firstName, lastName, birthDate, Integer.parseInt(departmentId));
	        	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, updateEmployee, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, updateEmployee, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus deleteEmployee(String employeeId) {
		System.out.println("SoapServiceFactory, deleteEmployee, employeeId = " + employeeId);
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/EmployeeServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "EmployeeServiceWS"));
			EmployeeService employee = service.getPort(EmployeeService.class);
	        
	        ReturnStatus sts = employee.delete(Integer.parseInt(employeeId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, deleteEmployee, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, deleteEmployee, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getUsers() {
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/UserServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "UserServiceWS"));
			UserService user = service.getPort(UserService.class);
	        return user.list(0,20);		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus addUser(String email, String userName, String password) {
		System.out.println("SoapServiceFactory, addUser, email  = " + email + " / userName = " + userName + " / password = " + password + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/UserServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "UserServiceWS"));
			UserService user = service.getPort(UserService.class);
	        
	        ReturnStatus sts = user.create(email, userName, password);
    		
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, addUser, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, addUser, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}	

	public ReturnStatus updateUser(String userId, String email, String userName, String password) {
		System.out.println("SoapServiceFactory, updateUser, email  = " + email + " / userName = " + userName + " / password = " + password + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/UserServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "UserServiceWS"));
			UserService user = service.getPort(UserService.class);
	        
	        ReturnStatus sts = user.update(Integer.parseInt(userId), email, userName, password);

	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, updateUser, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, updateUser, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus deleteUser(String userId) {
		System.out.println("SoapServiceFactory, deleteUser, userId = " + userId);
		
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/UserServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "UserServiceWS"));
			UserService user = service.getPort(UserService.class);
	        
	        ReturnStatus sts = user.delete(Integer.parseInt(userId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, deleteUser, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, deleteUser, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public List<Role> getRoles() {
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/RoleServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "RoleServiceWS"));
			RoleService role = service.getPort(RoleService.class);
	        return role.list(0,20);		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus addRole(String name) {
		System.out.println("SoapServiceFactory, addRole, name  = " + name + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/RoleServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "RoleServiceWS"));
			RoleService role = service.getPort(RoleService.class);
	        
	        ReturnStatus sts = role.create(name);
    		
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, addRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, addRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}	

	public ReturnStatus updateRole(String roleId, String name) {
		System.out.println("SoapServiceFactory, updateRole, name  = " + name + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);		
		
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/RoleServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "RoleServiceWS"));
			RoleService role = service.getPort(RoleService.class);
	        
	        ReturnStatus sts = role.update(Integer.parseInt(roleId), name);
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, updateRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, updateRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus deleteRole(String roleId) {
		System.out.println("SoapServiceFactory, deleteRole, roleId = " + roleId);
		
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/RoleServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "RoleServiceWS"));
			RoleService role = service.getPort(RoleService.class);
	        
	        ReturnStatus sts = role.delete(Integer.parseInt(roleId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, deleteRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, deleteRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<UserRole> getUserRoles() {
	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/UserRoleServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "UserRoleServiceWS"));
			UserRoleService userrole = service.getPort(UserRoleService.class);
	        return userrole.list(0,20,0,0);		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public ReturnStatus addUserRole(String roleId, String userId) {
		System.out.println("SoapServiceFactory, addUserRole, roleId  = " + roleId + " /  userId = " + userId + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/UserRoleServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "UserRoleServiceWS"));
			UserRoleService userrole = service.getPort(UserRoleService.class);
	        
	        ReturnStatus sts = userrole.create(Integer.parseInt(roleId), Integer.parseInt(userId));
    		
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, addUserRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, addUserRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}	

	public ReturnStatus updateUserRole(String userRoleId, String roleId, String userId) {
		System.out.println("SoapServiceFactory, updateUserRole, userRoleId = " + userRoleId + " / roleId  = " + roleId + " /  userId = " + userId + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);		
		
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/UserRoleServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "UserRoleServiceWS"));
			UserRoleService userrole = service.getPort(UserRoleService.class);
	        
	        ReturnStatus sts = userrole.update(Integer.parseInt(userRoleId), Integer.parseInt(userId), Integer.parseInt(roleId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, updateUserRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, updateUserRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public ReturnStatus deleteUserRole(String userRoleId) {
		System.out.println("SoapServiceFactory, deleteUserRole, userRoleId = " + userRoleId);
	    Service service = null;
		try {
			service = Service.create(
		            new URL(baseURL + "/webservices/UserRoleServiceWS?wsdl"),
		            new QName("http://biz.org/wsdl", "UserRoleServiceWS"));
			UserRoleService userrole = service.getPort(UserRoleService.class);
	        
	        ReturnStatus sts = userrole.delete(Integer.parseInt(userRoleId));
	        
	        if (sts != null) {
	        	System.out.println("SoapServiceFactory, deleteUserRole, returnstatus = " + sts);
	        } else {
	        	System.out.println("SoapServiceFactory, deleteUserRole, returnstatus = null");        	
	        }        
			return sts;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ReturnStatus register(String userName, String password, String email) {
		Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/SecurityServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "SecurityServiceWS"));
			SecurityService security = service.getPort(SecurityService.class);
	        return security.register(email, userName, password);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReturnStatus login(String userName, String password) {
		this.userName = userName;
		this.password = password;

		Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/SecurityServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "SecurityServiceWS"));
			SecurityService security = service.getPort(SecurityService.class);
	        return security.login("Basic " + base64Encode(userName + ":" + password));		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReturnStatus logout() {
		userName = "";
		password = "";
		Service service = null;
		try {
			service = Service.create(
			            new URL(baseURL + "/webservices/SecurityServiceWS?wsdl"),
			            new QName("http://biz.org/wsdl", "SecurityServiceWS"));
			SecurityService security = service.getPort(SecurityService.class);
	        return security.logout();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	 	
    private static String base64Encode(String value) {
        return Base64Utility.encode(value.getBytes());
    }

}
