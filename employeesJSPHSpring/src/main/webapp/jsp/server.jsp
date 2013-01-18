<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "/jsp/resources/includes/resources.html" %>   
<script type="text/javascript" src="/jsp/resources/js/server.js"></script>
<title>Server Configuration</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "/jsp/resources/includes/header.html" %>   
<%@ include file = "/jsp/resources/includes/menu.html" %>   
<% String appname= application.getServletContextName(); %>
<div id="content" class="content">
	<h1>Server Configuration</h1>
    <form:form method="POST" action='<%=request.getContextPath()+"/control/util/saveserverconfig.html"%>' commandName="frm"  onSubmit="return saveServer()">
    <table>
        <tbody>
        <tr><td>Server :  </td><td><input type="text" id="server"/></td></tr>
        <tr><td>Port :    </td><td><input type="text" id="port"/></td></tr>
        <tr><td>Project : </td><td><input type="text" id="project"/></td></tr>
        <tr><td><input type="submit" value="OK"/></td><td></td></tr>
        </tbody>
    </table>
    </form:form>  
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "/jsp/resources/includes/footer.html" %>   
</body>
</html>