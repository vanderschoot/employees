package org.biz.employees.model.dao;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.openejb.OpenEjbContainer;
import org.biz.employees.model.entities.Department;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DepartmentDAOTest {
    private static Context context;
	
	@EJB protected DepartmentDAO dao;
	@EJB protected EmployeeDAO empdao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties properties = new Properties();
		properties.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
		
		properties.setProperty("employeedb", "new://Resource?type=DataSource");
		properties.setProperty("employeedb.JdbcDriver", "org.hsqldb.jdbcDriver");
		properties.setProperty("employeedb.JdbcUrl", "jdbc:hsqldb:mem:employeedb");
		properties.setProperty("employeedb.UserName", "sa");
		properties.setProperty("employeedb.Password", "");
		properties.setProperty("employeedb.JtaManaged", "true");
		
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
			dao = (DepartmentDAO) context.lookup("java:global/employeesJSFH/DepartmentDAO");
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
		Department dep = new Department();
		dep.setName("Inkoop");
		dep.setAddress("Keizersgracht 121, Amsterdam");
		dep.setBudget(5000);
		assertTrue(dao.save(dep));
		List<Department> l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getName(), "Inkoop");
	}
	
	@Test
	public void testUpdate() {
		System.out.println("testUpdate");
		dao.deleteAll();
		Department dep = new Department();
		dep.setName("Inkoop");
		dep.setAddress("Keizersgracht 121, Amsterdam");
		dep.setBudget(5000);
		assertTrue(dao.save(dep));
		List<Department> l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getName(), "Inkoop");
		dep = l.get(0);
		dep.setName("Verkoop");
		assertTrue(dao.update(dep.getDepartmentId(), dep));
		l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getName(), "Verkoop");			
	}
}
