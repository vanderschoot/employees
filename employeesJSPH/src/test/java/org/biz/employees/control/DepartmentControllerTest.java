package org.biz.employees.control;


import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.entities.Department;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentControllerTest {
	
    @Mock private DepartmentDAO dao;
    @Mock private RequestDispatcher view;
	@InjectMocks  protected DepartmentController servlet;
	
	List<Department> list = new ArrayList<Department>();
	Department d = new Department();
	
	HttpServletRequest request = Mockito.mock(HttpServletRequest.class);			
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	@Before
	public void createMocks() {
		System.out.println("createMocks");		

		list = new ArrayList<Department>();
		d.setName("Inkoop");
		d.setAddress("Keizersgracht 121, Amsterdam");
		d.setBudget(5000);
		list.add(d);

		when(request.getParameter("name")).thenReturn("Inkoop");
		when(request.getParameter("address")).thenReturn("Keizersgracht 121, Amsterdam");
		when(request.getParameter("budget")).thenReturn("5000");	
		when(request.getRequestDispatcher(anyString())).thenReturn(view);
		when(dao.getAll()).thenReturn(list);
		when(dao.findById(1)).thenReturn(d);

		try {
			doNothing().when(view).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void rollbackTransaction() {
	}
	
	@Test
	public void testGetDelete() {
		System.out.println("testGetDelete");		
		try {
	
			when(request.getParameter("action")).thenReturn("delete");		
			when(request.getParameter("departmentId")).thenReturn("1");
			servlet.doGet(request, response);

			//Verifieer of de JPA queries zijn aangeroepen
			verify(dao).delete(1);
			verify(dao).getAll();
			verify(request).setAttribute("departments", list);			
			verify(request).getRequestDispatcher("/departmentList.jsp");			
			verify(view).forward(request, response);			
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testGetEdit() {
		System.out.println("testGetEdit");		
		try {
	
			when(request.getParameter("action")).thenReturn("edit");		
			when(request.getParameter("departmentId")).thenReturn("1");
			servlet.doGet(request, response);
			
			//Verifieer of de JPA queries zijn aangeroepen
			verify(dao).findById(1);
			verify(request).setAttribute("department", d);			
			verify(request).getRequestDispatcher("/department.jsp");			
			verify(view).forward(request, response);			
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testGetList() {
		System.out.println("testGetList");		
		try {
	
			when(request.getParameter("action")).thenReturn("list");		
			servlet.doGet(request, response);
			
			//Verifieer of de JPA queries zijn aangeroepen
			verify(dao).getAll();
			verify(request).setAttribute("departments", list);			
			verify(request).getRequestDispatcher("/departmentList.jsp");			
			verify(view).forward(request, response);			
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	public void testGetOther() {
		System.out.println("testDelete");		
		try {
	
			when(request.getParameter("")).thenReturn("delete");		
			servlet.doGet(request, response);

			//Verifieer of de JPA queries zijn aangeroepen
			verify(request).getRequestDispatcher("/department.jsp");			
			verify(view).forward(request, response);			
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	public void testPostSave() {
		System.out.println("testPostSave");		
		try {
	
			when(request.getParameter("departmentId")).thenReturn(null);
			servlet.doPost(request, response);

			//Verifieer of de JPA queries zijn aangeroepen
			verify(request).getParameter("name");		
			verify(request).getParameter("address");		
			verify(request).getParameter("budget");		
			verify(request).getParameter("departmentId");		
			verify(dao).save(d);
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	public void testPostUpdate() {
		System.out.println("testPostUpdate");		
		try {
			d.setDepartmentId(1);	
			when(request.getParameter("departmentId")).thenReturn("1");
			servlet.doPost(request, response);

			//Verifieer of de JPA queries zijn aangeroepen
			verify(request).getParameter("name");		
			verify(request).getParameter("address");		
			verify(request).getParameter("budget");		
			verify(request).getParameter("departmentId");		
			verify(dao).update(1, d);
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
}
