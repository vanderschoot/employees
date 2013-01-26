package org.biz.employees.model.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.biz.employees.model.entities.Department;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentDAOTestMocked {
	
	@InjectMocks  protected DepartmentDAO dao;
	
	@Mock private EntityManager em;
	@Mock private Query query;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	@Before
	public void createMocks() {
        System.out.println("createMocks");
        
		// Stel het verwachte antwoord in voor dao.getAll()
        // dao.getAll() wordt 3x aangeroepen. 
        // De 1e en 2e keer wordt lst verwacht. De 3e keer wordt lst2 verwacht.        
		List<Department> lst = new ArrayList<Department>();
		Department d = new Department();
		d.setName("Inkoop");
		lst.add(d);
		assertEquals(lst.size(), 1);
		
		List<Department> lst2 = new ArrayList<Department>();
		Department d2 = new Department();
		d2.setName("Vekoop");
		lst2.add(d2);
		assertEquals(lst.size(), 1);

		when(em.createNamedQuery("Department.findAll")).thenReturn(query);
		when(query.getResultList()).thenReturn(lst).thenReturn(lst).thenReturn(lst2);

		// Stel het verwachte antwoord in voor dao.deletAll()
		when(em.createQuery("Delete from Department")).thenReturn(query);
		when(query.executeUpdate()).thenReturn(1);

		// Stel het verwachte antwoord in voor dao.update() die een em.find() en em.merge() uitvoert
		when(em.find(Department.class,0)).thenReturn(d);
		when(em.merge(anyObject())).thenReturn(null);

		// Stel het verwachte antwoord in voor dao.save() die een em.persist() uitvoert
		// Omdat persist een void return heeft is when() niet mogelijk
		// Er wordt daarom gebruik gemaakt van doAnswer() of doNothing() of doThrow()
		// Er is maar een aanpak nodig, maar als voorbeeld staan ze er alle 3
		// Voor doThrow() moet de @Test(expected=RuntimeException.class) worden gebruikt. 
		doAnswer(new Answer<Object>() {
	        public Object answer(InvocationOnMock invocation) {
	            Object[] args = invocation.getArguments();
	            return "called with arguments: " + args;
	        }
	    }).when(em).persist(anyObject());
		
		doNothing().when(em).persist(anyObject());

		doThrow(new RuntimeException()).when(em).persist(anyObject());		
	}
		
	@Test(expected=RuntimeException.class)
	public void testSave() {
		System.out.println("testSave");
		dao.deleteAll();
		Department dep = new Department();
		dep.setDepartmentId(0);
		dep.setName("Inkoop");
		dep.setAddress("Keizersgracht 121, Amsterdam");
		dep.setBudget(5000);
		assertTrue(dao.save(dep));
		List<Department> l = dao.getAll();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getName(), "Inkoop");
		
		//Verifieer of de JPA queries zijn aangeroepen
		verify(em).persist(dep);
		verify(em).createQuery("Delete from Department");
		verify(em, times(1)).createNamedQuery("Department.findAll");
	}
	
	@Test(expected=RuntimeException.class)
	public void testUpdate() {
		System.out.println("testUpdate");
		dao.deleteAll();
		Department dep = new Department();
		dep.setDepartmentId(0);
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

		//Verifieer of de JPA queries zijn aangeroepen
		verify(em).persist(dep);
		verify(em).find(Department.class,0);
		verify(em).createQuery("Delete from Department");
		verify(em, times(2)).createNamedQuery("Department.findAll");
	}
}
