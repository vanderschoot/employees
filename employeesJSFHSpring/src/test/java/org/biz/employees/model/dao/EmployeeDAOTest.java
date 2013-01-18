package org.biz.employees.model.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.biz.employees.model.entities.Employee;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {	"file:src/main/webapp/WEB-INF/applicationContext.xml",
		"file:src/main/webapp/WEB-INF/data-context.xml",
		"file:src/main/webapp/WEB-INF/security-context.xml",
		"file:src/main/webapp/WEB-INF/mail-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EmployeeDAOTest {
	
    @Autowired private EmployeeDAO dao;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	@Before
	public void beginTransaction() {
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
