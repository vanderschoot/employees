<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Employee JSP Demo</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
<h1>Employee JSP Demo</h1>
<ul>
<li><a href="<%=request.getContextPath()%>/control/department/list.html">Departments</a></li>
<li><a href="<%=request.getContextPath()%>/control/employee/list.html">Employees</a></li>
<li><a href="<%=request.getContextPath()%>/control/util/createdatabase.html">Create Database</a></li>
</ul>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>
