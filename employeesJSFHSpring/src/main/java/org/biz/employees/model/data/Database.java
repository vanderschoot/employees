package org.biz.employees.model.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Named("db")
@Repository
public class Database {

	@PersistenceContext
	private EntityManager em;

	public Database() {
    	System.out.println("Database class constructed");
	}
	
	@Transactional
    public boolean createDatabase() {     	
    	
    	System.out.println("Database.createDatabase");
    	List<Department> deps;
    	List<Role> roles;
    	List<User> users;
    	boolean dbready = false;
    	
    	System.out.println("createDatabase: Start query");  
        deps = em.createQuery("select d from Department d where d.name = 'Inkoop'",Department.class).getResultList();
        if (deps != null)  {
	        if (deps.size() > 0) {
	        	System.out.println("createDatabase: database already created");  
	        	dbready = true;
	        } else {
	        	System.out.println("createDatabase: no database available, must be created");  
	        }
        }  else {
        	System.out.println("createDatabase: no database available, must be created");  
        }
        
        if (!dbready) {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
	
        	System.out.println("createDatabase: Create departments");  
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

}
