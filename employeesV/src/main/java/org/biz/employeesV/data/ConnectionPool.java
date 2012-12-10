package org.biz.employeesV.data;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class ConnectionPool {
	
	private JDBCConnectionPool connectionPool = null;
	
    public ConnectionPool() {
		System.out.println("ConnectionPool created");
        try {
            connectionPool = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    "jdbc:mysql://localhost:3306/employeedb", "root", "admin", 2, 5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    JDBCConnectionPool getConnectionPool() {
    	return connectionPool;
    }
}
