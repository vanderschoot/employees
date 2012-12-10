package org.biz.employeesVH.data;


import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;

public class ConnectionPool {
	
	private JDBCConnectionPool connectionPool = null;
	
    public ConnectionPool() {
		System.out.println("ConnectionPool created");
        Database database = new Database();
		connectionPool = database.createDatabase();
		if (connectionPool == null) {
			System.out.println("Could not create HyperSQL database");
		}
    }
    
    JDBCConnectionPool getConnectionPool() {
    	return connectionPool;
    }
}
