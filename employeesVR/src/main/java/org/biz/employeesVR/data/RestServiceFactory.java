package org.biz.employeesVR.data;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.biz.employeesVR.data.DTO.Department;
import org.biz.employeesVR.data.DTO.Employee;
import org.biz.employeesVR.data.DTO.Role;
import org.biz.employeesVR.data.DTO.User;
import org.biz.employeesVR.data.DTO.UserRole;
import org.biz.employeesVR.MainApp;
import org.biz.employeesVR.UI.screens.Connect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RestServiceFactory {
	private MainApp app;
	private WebResource service;
	private String userName;
	private String password;
	
	private String defaultURL = "http://localhost:8082/employeesRESTH";
	private String baseURL = "";
	static final Logger logger = Logger.getLogger(RestServiceFactory.class);
	
	public RestServiceFactory(MainApp app) {
		this.app = app;
		BasicConfigurator.configure();
		logger.debug("RestServiceFactory Created");			
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
	    WebClient client = WebClient.create(baseURL);
	    client.path("/dep/listu");
	    client.type("application/json").accept("application/json");
	    try {
		    String resp = client.post(null, String.class);
		    
		    if (client.getResponse().getStatus() == 200) {

		        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		        
		        Type type = new TypeToken<List<Department>>(){}.getType();
		        List<Department> list = gson.fromJson((String) resp, type);
		 
				return list;
				
		    } else {
				app.showError("Verbinding met server mislukt (Status = " + client.getResponse().getStatus() +  ")!!!");
				return null;   	
		    }	
		} catch (Exception e) {
			app.showError("Verbinding met server mislukt (Webservice fout " + e.getMessage() + ")");
			return null;   	
		}
	}

	public ReturnStatus addDepartment(String name, String address, String budget) {
		System.out.println("RestServiceFactory, addDepartment, name  = " + name + " / address = " + address + " / budget = " + budget + " / userName = " + userName + " /  password = " + password);

	    WebClient client = WebClient.create(baseURL);
	    client.path("/dep/createu")
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("name", name).query("address", address).query("budget", budget);
	    
	    String resp = client.post(null, String.class);
		System.out.println("RestServiceFactory, addDepartment, uri  = " + client.getCurrentURI());
		System.out.println("RestServiceFactory, addDepartment, Response  = " + client.getResponse());
		System.out.println("RestServiceFactory, addDepartment, Response.Status  = " + client.getResponse().getStatus());
	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addDepartment, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addDepartment, returnstatus = null");        	
        }        
		return sts;
	}
	
	
	public ReturnStatus addDepartment2(String name, String address, String budget) {
		System.out.println("RestServiceFactory, addDepartment2, name  = " + name + " / address = " + address + " / budget = " + budget + " / userName = " + userName + " /  password = " + password);
		String resp = "";
		try{
			HttpClient client = new HttpClient();
			PostMethod mPost = new PostMethod(baseURL);
			mPost.addParameter("name", name);
			mPost.addParameter("address", address);
			mPost.addParameter("budget", budget);
			Header mtHeader = new Header();
			mtHeader.setName("content-type");
			mtHeader.setValue("application/x-www-form-urlencoded");
			mtHeader.setName("accept");
			mtHeader.setValue("application/xml");
			//mtHeader.setValue("application/json");
			mPost.addRequestHeader(mtHeader);
			client.executeMethod(mPost);
			resp = mPost.getResponseBodyAsString( );
			mPost.releaseConnection( );
			System.out.println("output : " + resp);
		}catch(Exception e){
			e.printStackTrace();
		}	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addDepartment, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addDepartment, returnstatus = null");        	
        }        
		return sts;
	}

	public ReturnStatus updateDepartment(String departmentId, String name, String address, String budget) {
		System.out.println("RestServiceFactory, updateDepartment, departmentId = " + departmentId + " name  = " + name + " / address = " + address + " / budget = " + budget);
	    WebClient client = WebClient.create(baseURL);	    
	    client.path("/dep/updateu/{id}", departmentId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("name", name).query("address", address).query("budget", budget);
	    	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, updateDepartment, uri  = " + uri);
		
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, updateDepartment, resp  = " + resp);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);
        
        System.out.println("RestServiceFactory, updateDepartment, sts  = " + sts);

		System.out.println("RestServiceFactory, updateDepartment, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus deleteDepartment(String departmentId) {
		System.out.println("RestServiceFactory, deleteDepartment, departmentId = " + departmentId);
	    WebClient client = WebClient.create(baseURL);
	    
	    client.path("/dep/deleteu/{id}", departmentId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, deleteDepartment, uri  = " + uri);
	    
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, deleteDepartment, resp  = " + resp);
		
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);

        System.out.println("RestServiceFactory, deleteDepartment, sts  = " + sts);
        
		System.out.println("RestServiceFactory, deleteDepartment, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}

	
	public Collection<Department> getDepartmentsJersey() {		
		URI uri = UriBuilder.fromUri("http://localhost").port(8082).path("/EmployeesTC/dep/listu").build();
		WebResource.Builder builder = createWebResourceBuilder(uri);

		GenericType<Collection<Department>> genericType = new GenericType<Collection<Department>>() {};
		Collection<Department> coll = builder.post(genericType);
		return coll;
	}
	
	
	public List<Employee> getEmployees() {
	    WebClient client = WebClient.create(baseURL);
	    client.path("/emp/listu");
	    client.type("application/json").accept("application/json");
	    
		System.out.println("RestServiceFactory, getEmployees, uri  = " + client.getCurrentURI());

	    String resp = client.get(String.class);
	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<List<Employee>>(){}.getType();
        //System.out.println("getEmployees, Response = " + resp);
        List<Employee> empList = gson.fromJson((String) resp, type);
 
		return empList;
	}
			
	public ReturnStatus addEmployee(String firstName, String lastName, Date birthDate, String departmentId) {
		String birthDateString = new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
		System.out.println("RestServiceFactory, addEmployee, firstName  = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDateString + "  / departmentId = " + departmentId +  " / userName = " + userName + " /  password = " + password);

		WebClient client = WebClient.create(baseURL);
	    client.path("/emp/createu")
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("firstName", firstName).query("lastName", lastName).query("birthDate", birthDateString).query("departmentId", departmentId);
	    
	    String resp = client.post(null, String.class);
		System.out.println("RestServiceFactory, addEmployee, uri  = " + client.getCurrentURI());
		System.out.println("RestServiceFactory, addEmployee, Response  = " + client.getResponse());
		System.out.println("RestServiceFactory, addEmployee, Response.Status  = " + client.getResponse().getStatus());
	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addEmployee, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addEmployee, returnstatus = null");        	
        }        
		return sts;
	}	
	

	public ReturnStatus updateEmployee(String employeeId, String firstName, String lastName, Date birthDate, String departmentId) {
		String birthDateString = new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
		System.out.println("RestServiceFactory, updateEmployee, firstName  = " + firstName + " / lastName = " + lastName + " / birthDate = " + birthDateString + "  / departmentId = " + departmentId +  " / userName = " + userName + " /  password = " + password);
	    WebClient client = WebClient.create(baseURL);	    
	    client.path("/emp/updateu/{id}", employeeId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("firstName", firstName).query("lastName", lastName).query("birthDate", birthDateString).query("departmentId", departmentId);
	    	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, updateEmployee, uri  = " + uri);
		
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, updateEmployee, resp  = " + resp);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);
        
        System.out.println("RestServiceFactory, updateEmployee, sts  = " + sts);

		System.out.println("RestServiceFactory, updateEmployee, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus deleteEmployee(String employeeId) {
		System.out.println("RestServiceFactory, deleteEmployee, employeeId = " + employeeId);
	    WebClient client = WebClient.create(baseURL);
	    
	    client.path("/emp/deleteu/{id}", employeeId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, deleteEmployee, uri  = " + uri);
	    
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, deleteEmployee, resp  = " + resp);
		
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);

        System.out.println("RestServiceFactory, deleteEmployee, sts  = " + sts);
        
		System.out.println("RestServiceFactory, deleteEmployee, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public List<User> getUsers() {
	    WebClient client = WebClient.create(baseURL);
	    client.path("/usr/listu");
	    client.type("application/json").accept("application/json");
	    
	    String resp = client.post(null, String.class);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<List<User>>(){}.getType();
        List<User> list = gson.fromJson((String) resp, type);
 
		return list;
	}
	
	public ReturnStatus addUser(String email, String userName, String password) {
		System.out.println("RestServiceFactory, addUser, email  = " + email + " / userName = " + userName + " / password = " + password + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    WebClient client = WebClient.create(baseURL);
	    client.path("/usr/createu")
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(this.userName + ":" + this.password))
	    .query("email", email).query("userName", userName).query("password", password);
	    
	    String resp = client.post(null, String.class);
		System.out.println("RestServiceFactory, addUser, uri  = " + client.getCurrentURI());
		System.out.println("RestServiceFactory, addUser, Response  = " + client.getResponse());
		System.out.println("RestServiceFactory, addUser, Response.Status  = " + client.getResponse().getStatus());
	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addUser, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addUser, returnstatus = null");        	
        }        
		return sts;
	}	

	public ReturnStatus updateUser(String userId, String email, String userName, String password) {
		System.out.println("RestServiceFactory, updateUser, email  = " + email + " / userName = " + userName + " / password = " + password + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);
	    WebClient client = WebClient.create(baseURL);	    
	    client.path("/usr/updateu/{id}", userId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("email", email).query("userName", userName).query("password", password);
	    	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, updateUser, uri  = " + uri);
		
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, updateUser, resp  = " + resp);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);
        
        System.out.println("RestServiceFactory, updateUser, sts  = " + sts);

		System.out.println("RestServiceFactory, updateUser, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus deleteUser(String userId) {
		System.out.println("RestServiceFactory, deleteUser, userId = " + userId);
	    WebClient client = WebClient.create(baseURL);
	    
	    client.path("/usr/deleteu/{id}", userId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, deleteUser, uri  = " + uri);
	    
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, deleteUser, resp  = " + resp);
		
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);

        System.out.println("RestServiceFactory, deleteUser, sts  = " + sts);
        
		System.out.println("RestServiceFactory, deleteUser, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
		
	public List<Role> getRoles() {
	    WebClient client = WebClient.create(baseURL);
	    client.path("/role/listu");
	    client.type("application/json").accept("application/json");
	    
	    String resp = client.post(null, String.class);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<List<Role>>(){}.getType();
        List<Role> list = gson.fromJson((String) resp, type);
 
		return list;
	}
	
	public ReturnStatus addRole(String name) {
		System.out.println("RestServiceFactory, addRole, name  = " + name + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    WebClient client = WebClient.create(baseURL);
	    client.path("/role/createu")
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(this.userName + ":" + this.password))
	    .query("name", name);
	    
	    String resp = client.post(null, String.class);
		System.out.println("RestServiceFactory, addRole, uri  = " + client.getCurrentURI());
		System.out.println("RestServiceFactory, addRole, Response  = " + client.getResponse());
		System.out.println("RestServiceFactory, addRole, Response.Status  = " + client.getResponse().getStatus());
	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addRole, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addRole, returnstatus = null");        	
        }        
		return sts;
	}	

	public ReturnStatus updateRole(String roleId, String name) {
		System.out.println("RestServiceFactory, updateRole, name  = " + name + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);
	    WebClient client = WebClient.create(baseURL);	    
	    client.path("/role/updateu/{id}", roleId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("name", name);
	    	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, updateRole, uri  = " + uri);
		
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, updateRole, resp  = " + resp);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);
        
        System.out.println("RestServiceFactory, updateRole, sts  = " + sts);

		System.out.println("RestServiceFactory, updateRole, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus deleteRole(String roleId) {
		System.out.println("RestServiceFactory, deleteRole, roleId = " + roleId);
	    WebClient client = WebClient.create(baseURL);
	    
	    client.path("/role/deleteu/{id}", roleId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, deleteRole, uri  = " + uri);
	    
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, deleteRole, resp  = " + resp);
		
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);

        System.out.println("RestServiceFactory, deleteRole, sts  = " + sts);
        
		System.out.println("RestServiceFactory, deleteRole, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public List<UserRole> getUserRoles() {
	    WebClient client = WebClient.create(baseURL);
	    client.path("/usrl/listu");
	    client.type("application/json").accept("application/json");
	    
	    String resp = client.post(null, String.class);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<List<UserRole>>(){}.getType();
        List<UserRole> list = gson.fromJson((String) resp, type);
 
		return list;
	}
	
	
	public ReturnStatus addUserRole(String roleId, String userId) {
		System.out.println("RestServiceFactory, addUserRole, roleId  = " + roleId + " /  userId = " + userId + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);

	    WebClient client = WebClient.create(baseURL);
	    client.path("/usrl/createu")
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(this.userName + ":" + this.password))
	    .query("roleId", roleId).query("userId", userId);
	    
	    String resp = client.post(null, String.class);
		System.out.println("RestServiceFactory, addUserRole, uri  = " + client.getCurrentURI());
		System.out.println("RestServiceFactory, addUserRole, Response  = " + client.getResponse());
		System.out.println("RestServiceFactory, addUserRole, Response.Status  = " + client.getResponse().getStatus());
	    	    
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
 
        if (sts != null) {
        	System.out.println("RestServiceFactory, addUserRole, returnstatus = " + sts);
        } else {
        	System.out.println("RestServiceFactory, addUserRole, returnstatus = null");        	
        }        
		return sts;
	}	

	public ReturnStatus updateUserRole(String userRoleId, String roleId, String userId) {
		System.out.println("RestServiceFactory, updateUserRole, userRoleId = " + userRoleId + " / roleId  = " + roleId + " /  userId = " + userId + " / loggedin userName = " + this.userName + " /  loggedin password = " + this.password);
	    WebClient client = WebClient.create(baseURL);	    
	    client.path("/usrl/updateu/{id}", userRoleId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password))
	    .query("roleId", roleId).query("userId", userId);
	    	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, updateUserRole, uri  = " + uri);
		
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, updateUserRole, resp  = " + resp);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);
        
        System.out.println("RestServiceFactory, updateUserRole, sts  = " + sts);

		System.out.println("RestServiceFactory, updateUserRole, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus deleteUserRole(String userRoleId) {
		System.out.println("RestServiceFactory, deleteUserRole, userRoleId = " + userRoleId);
	    WebClient client = WebClient.create(baseURL);
	    
	    client.path("/usrl/deleteu/{id}", userRoleId)
	    .type(MediaType.APPLICATION_FORM_URLENCODED)
	    .accept("text/html,application/xhtml+xml,application/xml,application/json")
	    .header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
		URI uri = client.getCurrentURI();
		System.out.println("RestServiceFactory, deleteUserRole, uri  = " + uri);
	    
	    String resp = client.post(null, String.class);
	    
		System.out.println("RestServiceFactory, deleteUserRole, resp  = " + resp);
		
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson(resp, type);

        System.out.println("RestServiceFactory, deleteUserRole, sts  = " + sts);
        
		System.out.println("RestServiceFactory, deleteUserRole, returnstatus success = " + sts.isSucces() + " / message = " + sts.getMessage());
 
		return sts;
	}
	
	public ReturnStatus register(String userName, String password, String email) {
	    WebClient client = WebClient.create(baseURL);
	    client.path("/security/register");
	    client.type("application/json").accept("application/json");
	    client.header("Authorization", base64Encode(userName + ":" + password));
	    client.query("userName", userName).query("email", email).query("password", password);
	    
	    String resp = client.post(null, String.class);
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
		return sts;	
	}

	public ReturnStatus login(String userName, String password) {
		this.userName = userName;
		this.password = password;
		System.out.println("RestServiceFactory, login, userName = " + userName + " /  password = " + password);
	    WebClient client = WebClient.create(baseURL);
	    client.path("/security/login");
	    client.type("application/json").accept("application/json");
	    client.header("Authorization", "Basic " + base64Encode(userName + ":" + password));
	    
	    String resp = client.post(null, String.class);
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
		return sts;	
	}

	public ReturnStatus logout() {
		userName = "";
		password = "";
	    WebClient client = WebClient.create(baseURL);
	    client.path("/security/logout");
	    client.type("application/json").accept("application/json");
	    
	    String resp = client.post(null, String.class);
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type type = new TypeToken<ReturnStatus>(){}.getType();
        ReturnStatus sts = gson.fromJson((String) resp, type);
		return sts;	
	}
	 
	public WebResource.Builder createWebResourceBuilder(URI url) {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		service = client.resource(url);
		MediaType[] mediaTypes = new MediaType[] { MediaType.valueOf(MediaType.APPLICATION_JSON) };
		WebResource.Builder builder = service.accept(mediaTypes);
		return builder;
	}
	
    private static String base64Encode(String value) {
        return Base64Utility.encode(value.getBytes());
    }

}
