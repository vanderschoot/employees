package org.biz.employees.control.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biz.employees.model.service.LoginStatus;
import org.biz.employees.model.service.ReturnStatus;
import org.biz.employees.model.service.SecurityService;

import com.google.gson.Gson;

@WebServlet(name="securityController", urlPatterns={"/SecurityController"})
public class SecurityController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    @Inject private SecurityService sec;
       
    public SecurityController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession();
        
        ReturnStatus rs = null;
        LoginStatus ls = null;
        
        Gson gs = new Gson();
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        
        if (action.equalsIgnoreCase("login")){
			System.out.println("doGet, login ");
            String auth = request.getHeader("Authorization");
            rs = sec.login(auth, request, session);
			writer.print(gs.toJson(rs));
	        System.out.println("doGet, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
        } else if (action.equalsIgnoreCase("loggedin")){
			System.out.println("doGet, loggedin ");
	        String userName = request.getParameter("userName");
			ls = sec.loggedin(userName, session);
			writer.print(gs.toJson(ls));
	        System.out.println("doGet, ls.success = " +  ls.isSucces() + " /  ls.message = " + ls.getMessage());
        } else if (action.equalsIgnoreCase("logout")){
			System.out.println("doGet, logout ");
			rs = sec.logout(request, session);
			writer.print(gs.toJson(rs));
	        System.out.println("doGet, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
        } else if (action.equalsIgnoreCase("register")){
			System.out.println("doGet, register");
	        String userName = request.getParameter("userName");
	        String email = request.getParameter("email");
	        String password = request.getParameter("password");						
	        rs = sec.register(email, userName, password);
			writer.print(gs.toJson(rs));
	        System.out.println("doGet, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
        } else if (action.equalsIgnoreCase("askpassword")){
			System.out.println("doGet, login ");
	        String userName = request.getParameter("userName");
	        String email = request.getParameter("email");
	        rs = sec.askpassword(email, userName);
			writer.print(gs.toJson(rs));
	        System.out.println("doGet, rs.success = " +  rs.isSucces() + " /  rs.message = " + rs.getMessage());
        } else {
			System.out.println("doGet, action Other");
        }         
	}
}