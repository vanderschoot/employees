package org.biz.employees.control;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.biz.employees.model.dao.DepartmentDAO;
import org.biz.employees.model.entities.Department;

/**
 * Servlet implementation class DepartmentController
 */
public class DepartmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/department.jsp";
    private static String LIST = "/departmentList.jsp";
    private static String HOME = "/index.jsp";

    
    @EJB private DepartmentDAO dao;
    private RequestDispatcher view;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("doGet");

			String forward="";
	        String action = request.getParameter("action");
	        List<Department> list;
	        int id;

	        if (action == null) action = "X";
	        
	        if (action.equalsIgnoreCase("delete")){
	            id = Integer.parseInt(request.getParameter("departmentId"));
				System.out.println("doGet, delete Id=" + id);
	            dao.delete(id);
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("departments", list);
	        } else if (action.equalsIgnoreCase("edit")){
	            forward = INSERT_OR_EDIT;
	            id = Integer.parseInt(request.getParameter("departmentId"));
				System.out.println("doGet, edit id = " + id);
	            Department dep = dao.findById(id);
				System.out.println("doGet, edit name = " + dep.getName());
	            request.setAttribute("department", dep);
	        } else if (action.equalsIgnoreCase("list")){
				System.out.println("doGet, list");
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("departments", list);
	        } else if (action.equalsIgnoreCase("home")) {
				System.out.println("doGet, go HOME");
	            forward = HOME;
	        } else {
				System.out.println("doGet, action not OK");
	            forward = INSERT_OR_EDIT;
	        }

	        view = request.getRequestDispatcher(forward);
	        view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    Department dep = new Department();
		    String name = request.getParameter("name"); dep.setName(name);
		    String address = request.getParameter("address"); dep.setAddress(address);
		    String sbudget = request.getParameter("budget"); int budget = Integer.parseInt(sbudget); dep.setBudget(budget);
	        String departmentId = request.getParameter("departmentId");
	        System.out.println("doPost, name=" +name+" address ="+address+" budget="+budget);
	        if(departmentId == null || departmentId.isEmpty())
	        {
		        System.out.println("doPost, call dao.save()");
	            dao.save(dep);
	        }
	        else
	        {
	            int id = Integer.parseInt(departmentId);
	        	dep.setDepartmentId(id);
		        System.out.println("doPost, call dao.update()");
	            dao.update(id, dep);
	        }
	        view = request.getRequestDispatcher(LIST);
            List<Department> list = dao.getAll();
			System.out.println("doPost, list size =" + list.size());           
	        request.setAttribute("departments", list);
	        view.forward(request, response);
	}
}