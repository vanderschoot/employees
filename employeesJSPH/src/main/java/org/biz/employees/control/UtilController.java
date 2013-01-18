package org.biz.employees.control;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.biz.employees.model.data.Database;

/**
 * Servlet implementation class DepartmentController
 */
public class UtilController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger("UtilController");

	@EJB private Database db;    
    private RequestDispatcher view;
   
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String action = request.getParameter("action");

        if (action == null) action = "X";
        view = request.getRequestDispatcher("/index.jsp");
       
        if (action.equalsIgnoreCase("createdatabase")){
			System.out.println("doGet, createdatabase ");
	        db.createDatabase();
        } else if (action.equalsIgnoreCase("serverconfig")){
			System.out.println("doGet, serverconfig ");
	        view = request.getRequestDispatcher("/server.jsp");
        } else {
			System.out.println("doGet, action Other");
        }
        view.forward(request, response);		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost, serverconfig ");
        view = request.getRequestDispatcher("/index.jsp");
        view.forward(request, response);		
	}

	
}