package org.biz.employees.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biz.employees.model.service.ReturnStatus;
import org.biz.employees.model.service.SecurityService;

import com.google.gson.Gson;

/**
 * Servlet implementation class DepartmentController
 */
public class SecurityController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
    @Inject private SecurityService sec;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SecurityController() {
        super();
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession();
        
        ReturnStatus rs = null;
        Gson gs = new Gson();
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        
        if (action.equalsIgnoreCase("login")){
			System.out.println("doGet, login ");
            String auth = request.getHeader("Authorization");
            rs = sec.login(auth, request, session);
        } else if (action.equalsIgnoreCase("loggedin")){
			System.out.println("doGet, loggedin ");
	        String userName = request.getParameter("userName");
			rs = sec.loggedin(userName, session);
        } else if (action.equalsIgnoreCase("logout")){
			System.out.println("doGet, logout ");
			rs = sec.logout(request, session);
        } else if (action.equalsIgnoreCase("register")){
			System.out.println("doGet, register");
	        String userName = request.getParameter("userName");
	        String email = request.getParameter("email");
	        String password = request.getParameter("password");						
	        rs = sec.register(email, userName, password);
        } else if (action.equalsIgnoreCase("askpassword")){
			System.out.println("doGet, login ");
	        String userName = request.getParameter("userName");
	        String email = request.getParameter("email");
	        rs = sec.askpassword(email, userName);
        } else {
			System.out.println("doGet, action Other");
        }
        System.out.println("doGet, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
        
        String json = gs.toJson(rs);
        writer.print(json);
	}
}