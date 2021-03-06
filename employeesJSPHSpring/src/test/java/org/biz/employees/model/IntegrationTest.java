package org.biz.employees.model;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.biz.employees.model.dao.*;
import org.biz.employees.model.entities.*;
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
public class IntegrationTest {
	
	private Log log = LogFactory.getLog(IntegrationTest.class);
	
	@Autowired protected DepartmentDAO departmentDao;
	@Autowired protected EmployeeDAO employeeDao;
	@Autowired protected UserDAO userDao;
	@Autowired protected RoleDAO roleDao;
	@Autowired protected UserRoleDAO userroleDao;
	
	Department dep1, dep2, dep3;
	Employee emp1, emp2, emp3;
	User usr1, usr2;
	Role rol1, rol2;
	UserRole ur1, ur2, ur3;

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
	public void integrationTest() {
		log.info("integrationTest");
		VulDatabase();
		
		log.info("====== List Employees in  Department " + dep1.getName() + " =========");
		List<Employee> employees = departmentDao.getEmployees(dep1);
		for (Employee e: employees) {
			log.info("Employee Id = " + e.getEmployeeId() + " /  Naam = " + e.getFirstName() + " " + e.getLastName() );
		}
		assertEquals("Paulusma", employees.get(0).getLastName());

		log.info("====== List Employees in  Department " + dep2.getName() + " =========");
		employees = departmentDao.getEmployees(dep2);
		for (Employee e: employees) {
			log.info("Employee Id = " + e.getEmployeeId() + " /  Naam = " + e.getFirstName() + " " + e.getLastName() );
		}
		assertEquals("Jagger", employees.get(0).getLastName());

		log.info("====== List Employees in  Department " + dep3.getName() + " =========");
		employees = departmentDao.getEmployees(dep3);
		for (Employee e: employees) {
			log.info("Employee Id = " + e.getEmployeeId() + " /  Naam = " + e.getFirstName() + " " + e.getLastName() );
		}
		assertEquals("Davis", employees.get(0).getLastName());
		
		log.info("====== Lijst Rollen =========");
		List<Role> rollen = roleDao.getAll();
		for (Role r: rollen) {
			log.info("Role Id = " + r.getRoleId() + " /  Naam = " + r.getName());
		}
		assertEquals(2, rollen.size());		

		log.info("====== Lijst Users =========");
		List<User> users = userDao.getAll();
		for (User u: users) {
			log.info("User Id = " + u.getUserId() + " /  Naam = " + u.getUserName());
		}
		assertEquals(2, users.size());		
		
		log.info("====== Lijst UserRollen =========");
		List<UserRole> usrollen = userroleDao.getAll();
		for (UserRole ur: usrollen) {
			log.info("UserRole Id = " + ur.getUserRoleId() + " /  User = " + ur.getUser().getUserName() + " / Role = " + ur.getRole().getName());
		}
		assertEquals(3, usrollen.size());
		
		log.info("====== Lijst Rollen van User " + usr1.getUserName() + " =========");
		rollen = userDao.getRoles(usr1);
		for (Role r: rollen) {
			log.info("Role Id = " + r.getRoleId() + " /  Naam = " + r.getName());
		}
		assertEquals(2, rollen.size());
		
		log.info("====== Lijst Rollen van User " + usr2.getUserName() + " =========");
		rollen = userDao.getRoles(usr2);
		for (Role r: rollen) {
			log.info("Role Id = " + r.getRoleId() + " /  Naam = " + r.getName());
		}
		assertEquals(1, rollen.size());
		assertEquals("User", rollen.get(0).getName());
	}	
	
	void VulDatabase() {
		departmentDao.deleteAll();
		dep1 = new Department();
		dep1.setName("Inkoop");
		departmentDao.save(dep1);
		dep2 = new Department();
		dep2.setName("Verkoop");
		departmentDao.save(dep2);
		dep3 = new Department();
		dep3.setName("Ontwikkeling");
		departmentDao.save(dep3);
		
		emp1 = new Employee();
		emp1.setFirstName("Piet");
		emp1.setLastName("Paulusma");
		emp1.setBirthDate(new Date());
		emp1.setDepartment(dep1);
		employeeDao.save(emp1);

		emp2 = new Employee();
		emp2.setFirstName("Mick");
		emp2.setLastName("Jagger");
		emp2.setBirthDate(new Date());
		emp2.setDepartment(dep2);
		employeeDao.save(emp2);

		emp3 = new Employee();
		emp3.setFirstName("Miles");
		emp3.setLastName("Davis");
		emp3.setBirthDate(new Date());
		emp3.setDepartment(dep3);
		employeeDao.save(emp3);

		usr1 = new User();
		usr1.setUserName("admin");
		usr1.setPassword("admin");
		usr1.setEmail("admin@gmail.com");
		userDao.save(usr1);

		usr2 = new User();
		usr2.setUserName("user");
		usr2.setPassword("secret");
		usr2.setEmail("user@gmail.com");
		userDao.save(usr2);
		
		rol1 = new Role();
		rol1.setName("Administrator");
		roleDao.save(rol1);
		
		rol2 = new Role();
		rol2.setName("User");
		roleDao.save(rol2);

		ur1 = new UserRole();
		ur1.setUser(usr1);
		ur1.setRole(rol1);
		userroleDao.save(ur1);
		
		ur2 = new UserRole();
		ur2.setUser(usr1);
		ur2.setRole(rol2);
		userroleDao.save(ur2);
		
		ur3 = new UserRole();
		ur3.setUser(usr2);
		ur3.setRole(rol2);
		userroleDao.save(ur3);
	}
}
