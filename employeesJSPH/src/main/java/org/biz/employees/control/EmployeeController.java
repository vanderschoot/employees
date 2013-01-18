package org.biz.employees.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.dao.EmployeeDAO;
import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;

/**
 * Servlet implementation class EmployeeController
 */
public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/employee.jsp";
    private static String LIST = "/employeeList.jsp";
    private static String HOME = "/index.jsp";
    
    @EJB private EmployeeDAO dao;
    @EJB private DepartmentDAO depdao;
    private RequestDispatcher view;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("doGet");

			String forward="";
	        String action = request.getParameter("action");
	        List<Employee> list;
	        int id;

	        if (action == null) action = "X";
	        
	        if (action.equalsIgnoreCase("delete")){
	            id = Integer.parseInt(request.getParameter("employeeId"));
				System.out.println("doGet, delete Id=" + id);
	            dao.delete(id);
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("employees", list);
	        } else if (action.equalsIgnoreCase("edit")){
	            forward = INSERT_OR_EDIT;
	            id = Integer.parseInt(request.getParameter("employeeId"));
				System.out.println("doGet, edit id = " + id);
	            Employee emp = dao.findById(id);
				System.out.println("doGet, edit name = " + emp.getFirstName() + " " + emp.getLastName());
	            request.setAttribute("employee", emp);
	        } else if (action.equalsIgnoreCase("list")){
				System.out.println("doGet, list");
	            forward = LIST;
	            String sId = request.getParameter("departmentId");
	            if (sId != null) {
	            	id = Integer.parseInt(sId);
	            	list = dao.get4Department(id);
	            } else {
	            	list = dao.getAll();
	            }
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("employees", list);
	        } else if (action.equalsIgnoreCase("home")) {
				System.out.println("doGet, go HOME");
	            forward = HOME;
	        } else {
				System.out.println("doGet, action Other");
	            forward = INSERT_OR_EDIT;
	        }
	        
	        if (forward.equalsIgnoreCase(INSERT_OR_EDIT)) {
	        	List<Department> departments = depdao.getAll();
	            request.setAttribute("departments", departments);
	        }

	        view = request.getRequestDispatcher(forward);
	        view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    Employee emp = new Employee();
		    String firstName = request.getParameter("firstName"); emp.setFirstName(firstName);
		    String lastName = request.getParameter("lastName"); emp.setLastName(lastName);
		    String sbirthDate = request.getParameter("birthDate"); 
		    Date birthDate = null;
	        String employeeId = request.getParameter("employeeId");
	        String departmentId = request.getParameter("departmentId");
			try {
				birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(sbirthDate);
				emp.setBirthDate(birthDate);
		        System.out.println("doPost, Emp Id = " + employeeId +  " name=" +firstName+" "+lastName+" birthDate=" + birthDate + " departmentId=" +departmentId);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (departmentId != null) {
				int id = Integer.parseInt(departmentId.trim());
				Department dep = depdao.findById(id);
				emp.setDepartment(dep);
			}
	        if(employeeId == null || employeeId.isEmpty())
	        {
		        System.out.println("doPost, call dao.save()");
	            dao.save(emp);
	        }
	        else
	        {
	            int id = Integer.parseInt(employeeId);
	        	emp.setEmployeeId(id);
		        System.out.println("doPost, call dao.update()");
	            dao.update(id, emp);
	        }
	        view = request.getRequestDispatcher(LIST);
            List<Employee> list = dao.getAll();
			System.out.println("doPost, list size =" + list.size());           
	        request.setAttribute("employees", list);
	        view.forward(request, response);
	}
}