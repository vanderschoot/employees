package org.biz.employees.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.biz.employees.model.dao.RoleDAO;
import org.biz.employees.model.dao.UserDAO;
import org.biz.employees.model.dao.UserRoleDAO;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;

/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST = "/userList.jsp";
    private static String HOME = "/index.jsp";

    
    @EJB private UserDAO dao;
    @EJB private RoleDAO roledao;
    @EJB private UserRoleDAO usrldao;
    private RequestDispatcher view;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			String forward="";
	        String action = request.getParameter("action");
	        List<User> list;
	        int id;

	        if (action == null) action = "X";
			System.out.println("doGet, action = " + action);
	        
	        if (action.equalsIgnoreCase("delete")){
	            id = Integer.parseInt(request.getParameter("userId"));
				System.out.println("doGet, delete Id=" + id);
		        usrldao.delroles(id);
	            dao.delete(id);
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("users", list);
	        } else if (action.equalsIgnoreCase("edit")){
	            forward = INSERT_OR_EDIT;
	            id = Integer.parseInt(request.getParameter("userId"));
				System.out.println("doGet, edit id = " + id);
	            User user = dao.findById(id);
				System.out.println("doGet, edit name = " + user.getUserName());
	            request.setAttribute("user", user);
	            List<Role> roles = roledao.getAll();
				System.out.println("doGet, roles size = " + roles.size());
	            request.setAttribute("roles", roles);
	            List<UserRole> usroles = usrldao.list(user.getUserId(), 0);
				System.out.println("doGet, usroles size = " + usroles.size());
	            request.setAttribute("usroles", usroles);
	        } else if(action.equalsIgnoreCase("insert")) {
	            forward = INSERT_OR_EDIT;
	            User user = new User();
	            user.setUserId(-1);
	            request.setAttribute("user", user);
	            List<Role> roles = roledao.getAll();
	            request.setAttribute("roles", roles);
	            List<UserRole> usroles = new ArrayList<UserRole>();
	            request.setAttribute("usroles", usroles);
				System.out.println("doGet, roles size = " + roles.size());
	        } else if (action.equalsIgnoreCase("list")){
				System.out.println("doGet, list");
	            forward = LIST;
	            list = dao.getAll();
				System.out.println("doGet, list size =" + list.size());
	            request.setAttribute("users", list);
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
		    User user = new User();
		    String userName = request.getParameter("userName"); user.setUserName(userName);
		    String password = request.getParameter("password"); user.setPassword(password);
		    String email = request.getParameter("email"); user.setEmail(email);
	        String userId = request.getParameter("userId");

	        String[] roleIds =  request.getParameterValues("roleId");
	        for(int i=0; i< roleIds.length; i++) {
	        	System.out.println("selecter role : " + roleIds[i]);
	        }

	        System.out.println("doPost, name=" +userName+" password ="+password+" email="+email);
	        if(userId == null || userId.isEmpty() || userId.equals("-1"))
	        {
		        System.out.println("doPost, call dao.save()");
	            dao.save(user);
	            List<User> users = dao.findUsers(userName);
		        usrldao.createroles(users.get(0).getUserId(), roleIds);
	        }
	        else
	        {
		        System.out.println("doPost, userId=" + userId);
	            int id = Integer.parseInt(userId);
	        	user.setUserId(id);
		        usrldao.delroles(id);
		        usrldao.createroles(id, roleIds);
		        System.out.println("doPost, call dao.update()");
	            dao.update(id, user);
	        }
	        view = request.getRequestDispatcher(LIST);
            List<User> list = dao.getAll();
			System.out.println("doPost, list size =" + list.size());           
	        request.setAttribute("users", list);
	        view.forward(request, response);
	}
}