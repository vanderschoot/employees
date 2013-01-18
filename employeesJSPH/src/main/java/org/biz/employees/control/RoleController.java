package org.biz.employees.control;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.entities.Role;

/**
 * Servlet implementation class RoleController
 */
public class RoleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/role.jsp";
    private static String LIST = "/roleList.jsp";
    private static String HOME = "/index.jsp";

    
    @EJB private RoleDAO dao;
    private RequestDispatcher view;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoleController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("doGet");

			String forward="";
	        String action = request.getParameter("action");
	        List<Role> list;
	        int id;

	        if (action == null) action = "X";
	        
	        if (action.equalsIgnoreCase("delete")){
	            id = Integer.parseInt(request.getParameter("roleId"));
				System.out.println("doGet, delete Id=" + id);
	            dao.delete(id);
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("roles", list);
	        } else if (action.equalsIgnoreCase("edit")){
	            forward = INSERT_OR_EDIT;
	            id = Integer.parseInt(request.getParameter("roleId"));
				System.out.println("doGet, edit id = " + id);
	            Role role = dao.findById(id);
				System.out.println("doGet, edit name = " + role.getName());
	            request.setAttribute("role", role);
	        } else if (action.equalsIgnoreCase("list")){
				System.out.println("doGet, list");
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("roles", list);
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
		    Role role = new Role();
		    String name = request.getParameter("name"); role.setName(name);
	        String roleId = request.getParameter("roleId");
	        System.out.println("doPost, name=" +name);
	        if(roleId == null || roleId.isEmpty())
	        {
		        System.out.println("doPost, call dao.save()");
	            dao.save(role);
	        }
	        else
	        {
	            int id = Integer.parseInt(roleId);
	        	role.setRoleId(id);
		        System.out.println("doPost, call dao.update()");
	            dao.update(id, role);
	        }
	        view = request.getRequestDispatcher(LIST);
            List<Role> list = dao.getAll();
			System.out.println("doPost, list size =" + list.size());           
	        request.setAttribute("roles", list);
	        view.forward(request, response);
	}
}