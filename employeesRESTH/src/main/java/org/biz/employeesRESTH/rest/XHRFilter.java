package org.biz.employeesRESTH.rest;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class XHRFilter implements javax.servlet.Filter {

	@EJB
	private SecurityServiceImpl sec;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) resp;
	    /*
	    for(Enumeration<String> e = req.getAttributeNames(); e.hasMoreElements();){
	    	String attr = e.nextElement();
	    	System.out.println("attr = " + attr + " val = " + req.getAttribute(attr));
	    }
	    for(Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();){
	    	String par = e.nextElement();
	    	System.out.println("par = " + par + " val = " + req.getParameter(par));
	    }

	    for(Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();){
	    	String hdr = e.nextElement();
	    	System.out.println("headername = " + hdr + " val = " + request.getHeader(hdr));
	    }
	    	
	    Cookie[] ck = request.getCookies();
	    if (ck != null) {
	    	System.out.println("aantal cookies = " + ck.length);
	    	if (ck.length > 0) {
	    		for (int i=0; i < ck.length; i++) {
	    			System.out.println("cookie = " + ck[i].getName() + " / value= " + ck[i].getValue() + " / maxage = " + ck[i].getMaxAge() + " / domain = " + ck[i].getDomain() + " / version = " + ck[i].getVersion() + " / secure = " + ck[i].getSecure());
	    		}
		    }
	    } else {	    	
	    	System.out.println("cookies = null ");
	    }
	    
	    System.out.println("CharacterEncoding = " + req.getCharacterEncoding());
	    System.out.println("isSecure = " + req.isSecure());
	    */
	    /*
	    if (request.getHeader("Authorization") != null) {
	    	Response lresp = sec.login(request);
	    	if (lresp != null) {
	    		System.out.println("XHR, login response = " + lresp);
	    	} else {
	    		System.out.println("XHR, login response = null");	    		
	    	}
	    }
	    */
	    System.out.println("XHR, RequestedSessionId = " + request.getRequestedSessionId() + " server = " + req.getServerName());
	    
        response.setHeader("Access-Control-Allow-Origin", "http://" + req.getServerName());
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setHeader("Access-Control-Max-Age", "360");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("withCredentials", "true");
        response.setHeader("Authorization", request.getHeader("Authorization"));
        
        HttpSession ses = request.getSession();
        if (ses != null) {
        	System.out.println("XHR, Session aanwezig, id = " + ses.getId() + " created : " + new Date(ses.getCreationTime()) + " / experation (sec.) = " + ses.getMaxInactiveInterval() );
        }

	    //response.addHeader("Access-Control-Allow-Origin", "*");
        /*
	    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
	        response.addHeader("Access-Control-Allow-Credentials", "true");
	    }
	    */
	    System.out.println("XHR, method=" + request.getMethod() + " user=" + request.getRemoteUser() + "principal=" + request.getUserPrincipal() + " scheme=" + request.getScheme() +  " isinRole CalcUser?" +  request.isUserInRole("CalcUser")+  " isinRole Role1?" +  request.isUserInRole("Role1"));

	    chain.doFilter(req, resp);		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
