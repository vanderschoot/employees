package org.biz.employeesRESTH.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;

import org.biz.employeesRESTH.entities.Department;
import org.biz.employeesRESTH.entities.Employee;
import org.biz.employeesRESTH.entities.Role;
import org.biz.employeesRESTH.entities.User;
import org.biz.employeesRESTH.entities.UserRole;

public class Database {

	public Database() {
	}
    public static boolean createDatabase(EntityManager em) {     	
    	
    	System.out.println("createDatabase");
    	List<Department> deps;
    	List<Role> roles;
    	List<User> users;
    	
        deps = em.createQuery("select d from Department d where d.name = 'Inkoop'",Department.class).getResultList();
        if (deps.size() > 0) {
        	System.out.println("createDatabase: database already created");  
        } else {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
	
	    	Department d1 = new Department();
	    	d1.setName("Inkoop"); d1.setAddress("Keizersgracht 100, Amsterdam"); d1.setBudget(1000);
	        em.persist(d1); 
	        deps = em.createQuery("select d from Department d where d.name = 'Inkoop'",Department.class).getResultList();
	        Department inkoop = deps.get(0);
	
	    	Department d2 = new Department();
	    	d2.setName("Verkoop"); d2.setAddress("Herengracht 101, Amsterdam"); d2.setBudget(1500);
	        em.persist(d2);
	        deps = em.createQuery("select d from Department d where d.name = 'Verkoop'",Department.class).getResultList();
	        Department verkoop = deps.get(0);
	
	    	Department d3 = new Department();
	    	d3.setName("Ontwikkeling"); d3.setAddress("Prinsengracht 102, Amsterdam"); d3.setBudget(20000);
	        em.persist(d3);
	        deps = em.createQuery("select d from Department d where d.name = 'Ontwikkeling'",Department.class).getResultList();
	        Department ontwikkeling = deps.get(0);
	    	    	
	    	try {
	        	Employee e1 = new Employee();
	        	e1.setFirstName("Frank"); e1.setLastName("Zappa");e1.setBirthDate(dateFormat.parse("1959-03-21")); e1.setDepartment(inkoop);
	            em.persist(e1);
	
	        	Employee e2 = new Employee();
	            e2.setFirstName("Miles"); e2.setLastName("Davis"); e2.setBirthDate(dateFormat.parse("1935-09-12")); e2.setDepartment(verkoop);
		        em.persist(e2); 
	
	        	Employee e3 = new Employee();
		    	e3.setFirstName("Mick"); e3.setLastName("Jagger"); e3.setBirthDate(dateFormat.parse("1951-07-21")); e3.setDepartment(ontwikkeling);
		        em.persist(e3); 
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			} 
	
	        Role r1 = new Role();
	        r1.setName("Administrator"); 
	        em.persist(r1); 
	        roles = em.createQuery("select r from Role r where r.name = 'Administrator'",Role.class).getResultList();
	        Role radmin = roles.get(0);
	
	        Role r2 = new Role();
	        r2.setName("User"); 
	        em.persist(r2); 
	        roles = em.createQuery("select r from Role r where r.name = 'User'",Role.class).getResultList();
	        Role ruser = roles.get(0);
	
	        User u1 = new User();
	        u1.setUserName("admin"); u1.setPassword("admin"); u1.setEmail("admin@gmail.com");
	        em.persist(u1); 
	        users = em.createQuery("select u from User u where u.userName = 'admin'",User.class).getResultList();
	        User admin = users.get(0);
	
	        User u2 = new User();
	        u2.setUserName("user"); u2.setPassword("secret"); u2.setEmail("user@gmail.com");
	        em.persist(u2); 
	        users = em.createQuery("select u from User u where u.userName = 'user'",User.class).getResultList();
	        User user = users.get(0);
	
	        UserRole ur1 = new UserRole();
	        ur1.setRole(radmin); ur1.setUser(admin);
	        em.persist(ur1); 
	
	        UserRole ur2 = new UserRole();
	        ur2.setRole(ruser); ur2.setUser(user);
	        em.persist(ur2); 
        	System.out.println("createDatabase: new database created");  
       }

       return true;
    }

    public boolean createDatabase2() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:employeedbh","sa","");            
            Statement statement = conn.createStatement();
            try {
                PreparedStatement pst=conn.prepareStatement("SELECT * FROM department");
                pst.clearParameters();
                ResultSet rs=pst.executeQuery();
                while(rs.next()){
                	System.out.println(rs.getString(1) + " / " + rs.getString(2) + " / " + rs.getString(3) + " / " + rs.getString(4));
                }
            } catch (SQLException e) {
            	
                statement.execute("create table department ("
                				+ "departmentId integer generated always as identity, " 
                				+ "address varchar(64), "
                				+ "budget integer, "
                				+ "name  varchar(64))");
                statement.execute("alter table department add primary key (departmentId)");
                statement.execute("create table employee ("
                                + "employeeId integer generated always as identity, "
                                + "firstName varchar(64), "
                                + "lastName varchar(64), "
                                + "birthDate date, "
                                + "DepartmentId integer, "
                                + "FOREIGN KEY (DepartmentId) REFERENCES department(departmentId))");
                statement.execute("alter table employee add primary key (employeeId)");
                statement.execute("create table user ("
		        				+ "userId integer generated always as identity, " 
		        				+ "userName varchar(64), "
		        				+ "password varchar(64), "
		        				+ "email  varchar(64))");
                statement.execute("alter table user add primary key (userId)");
                statement.execute("create table role ("
		        				+ "roleId integer generated always as identity, " 
		        				+ "name  varchar(64))");
                statement.execute("alter table role add primary key (roleId)");
                statement.execute("create table userrole ("
		                        + "userRoleId integer generated always as identity, "
		                        + "RoleId integer, "
		                        + "UserId integer, "
		                        + "FOREIGN KEY (RoleId) REFERENCES role(roleId), "
		                        + "FOREIGN KEY (UserId) REFERENCES user(userId))");
                statement.execute("alter table userrole add primary key (userRoleId)");
		        	        
                statement.execute("INSERT INTO department (name,address,budget) VALUES ('Inkoop', 'Keizersgracht 100, Amsterdam', '1000')");
                statement.execute("INSERT INTO department (name,address,budget) VALUES ('Verkoop', 'Herengracht 101, Amsterdam', 1500)");
                statement.execute("INSERT INTO department (name,address,budget) VALUES ('Ontwikkeling', 'Keizersgracht 102, Amsterdam', 2000)");

		        String Id = "";
                PreparedStatement pst=conn.prepareStatement("select departmentId from department where name = 'Inkoop'");
                pst.clearParameters();
                ResultSet rs=pst.executeQuery();
                while(rs.next()){
                	Id = rs.getString(1);
                }
		        
		        statement.execute("INSERT INTO employee (firstName,lastName,birthDate, DepartmentId) VALUES ('Frank', 'Zappa', '1959-03-21',  '" + Id + "')");

                pst=conn.prepareStatement("select departmentId from department where name = 'Verkoop'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	Id = rs.getString(1);
                }
		        
		        statement.execute("INSERT INTO employee (firstName,lastName,birthDate, DepartmentId) VALUES ('Miles', 'Davis', '1935-09-12',  '" + Id + "')");

                pst=conn.prepareStatement("select departmentId from department where name = 'Ontwikkeling'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	Id = rs.getString(1);
                }
               
		        statement.execute("INSERT INTO employee (firstName,lastName,birthDate, DepartmentId) VALUES ('Mick', 'Jagger', '1951-07-21',  '" + Id + "')");

		        statement.execute("INSERT INTO role (name) VALUES ('Administrator')");
		        statement.execute("INSERT INTO role (name) VALUES ('User')");
		        
		        statement.execute("INSERT INTO user (userName, password, email) VALUES ('admin', 'admin', 'admin@gmail.com')");
		        statement.execute("INSERT INTO user (userName, password, email) VALUES ('user', 'secret', 'user@gmail.com')");
 
		        String RID = "";
                pst=conn.prepareStatement("select roleId from role where name = 'Administrator'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	RID = rs.getString(1);
                }
                String UID = "";
                pst=conn.prepareStatement("select userId from user where userName = 'admin'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	UID = rs.getString(1);
                }

		        statement.execute("INSERT INTO userrole (RoleId, UserId) VALUES ('" +  RID + "' , '" +  UID + "')");

                pst=conn.prepareStatement("select roleId from role where name = 'User'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	RID = rs.getString(1);
                }
                pst=conn.prepareStatement("select userId from user where userName = 'user'");
                pst.clearParameters();
                rs=pst.executeQuery();
                while(rs.next()){
                	UID = rs.getString(1);
                }

		        statement.execute("INSERT INTO userrole (RoleId, UserId) VALUES ('" +  RID + "' , '" +  UID + "')");
		        
		        System.out.println("Database created");
		        
                PreparedStatement pstd=conn.prepareStatement("SELECT * FROM department");
                pstd.clearParameters();
                ResultSet rsd=pstd.executeQuery();
                while(rsd.next()){
                	System.out.println("Department: " + rsd.getString(1) + " / " + rsd.getString(2) + " / " + rsd.getString(3) + " / " + rsd.getString(4));
                    PreparedStatement pste=conn.prepareStatement("SELECT * FROM employee where DepartmentId = " + rsd.getString(1));
                    pste.clearParameters();
                    ResultSet rse=pste.executeQuery();
                    while(rse.next()){
                    	System.out.println("Employee: " + rse.getString(1) + " / " + rse.getString(2) + " / " + rse.getString(3) + " / " + rse.getString(4));
                    }
                }
		        
            }
            statement.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
