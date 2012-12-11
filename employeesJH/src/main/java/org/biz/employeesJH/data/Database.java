package org.biz.employeesJH.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	public Database() {
	}

    public boolean createDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:employeedb","sa","");            
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
