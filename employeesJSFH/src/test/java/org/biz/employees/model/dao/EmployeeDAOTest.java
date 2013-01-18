package org.biz.employees.model.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.openejb.OpenEjbContainer;
import org.biz.employees.model.entities.Employee;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeDAOTest {
    private static Context context;
	
	@EJB private EmployeeDAO dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties properties = new Properties();
		properties.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
		
		properties.setProperty("employeedbh", "new://Resource?type=DataSource");
		properties.setProperty("employeedbh.JdbcDriver", "org.hsqldb.jdbcDriver");
		properties.setProperty("employeedbh.JdbcUrl", "jdbc:hsqldb:mem:employeedbh");
		properties.setProperty("employeedbh.UserName", "sa");
		properties.setProperty("employeedbh.Password", "");
		properties.setProperty("employeedbh.JtaManaged", "true");
		
		properties.setProperty("openejb.deployments.classpath.ear", "true");

		context = EJBContainer.createEJBContainer(properties).getContext();   		  
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
        if (context != null) {
            context.close();
        }
	}
	
	
	@Before
	public void beginTransaction() {
		
        System.out.println("lookup");
        try {
			dao = (EmployeeDAO) context.lookup("java:global/employeesJSFH/EmployeeDAO");
			if (dao == null) System.out.println("lookup: dao not found");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}

	@After
	public void rollbackTransaction() {
	}
	
	@Test
	public void testSave() {
		System.out.println("testSave");
		dao.deleteAll();
		Employee emp = new Employee();
		emp.setFirstName("Piet");
		emp.setLastName("Noordijk");
		emp.setBirthDate(new Date());
		assertTrue(dao.save(emp));
		List<Employee> l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getFirstName(), "Piet");
	}
	
	@Test
	public void testUpdate() {
		System.out.println("testUpdate");
		dao.deleteAll();
		Employee emp = new Employee();
		emp.setFirstName("Piet");
		emp.setLastName("Noordijk");
		emp.setBirthDate(new Date());
		assertTrue(dao.save(emp));
		List<Employee> l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getFirstName(), "Piet");	
		emp = l.get(0);
		emp.setFirstName("Jan");
		assertTrue(dao.update(emp.getEmployeeId(), emp));
		l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getFirstName(), "Jan");			
	}
}
