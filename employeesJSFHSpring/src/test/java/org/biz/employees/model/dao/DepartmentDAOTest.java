package org.biz.employees.model.dao;

import static org.junit.Assert.*;
import java.util.List;

import org.biz.employees.model.entities.Department;
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
public class DepartmentDAOTest {
	
    @Autowired protected DepartmentDAO dao;

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
