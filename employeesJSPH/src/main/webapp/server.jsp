<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<script type="text/javascript" src="resources/js/server.js"></script>
<title>Server Configuration</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
	<h1>Server Configuration</h1>
    <form method="POST" action="UtilController" name="frm" onSubmit="return saveServer()">  
    <table>
        <tbody>
        <tr><td>Server :  </td><td><input type="text" id="server"/></td></tr>
        <tr><td>Port :    </td><td><input type="text" id="port"/></td></tr>
        <tr><td>Project : </td><td><input type="text" id="project"/></td></tr>
        <tr><td><input type="submit" value="OK"/></td><td></td></tr>
        </tbody>
    </table>
    </form>  
</div> <!-- content -->
</div> <!-- container -->
<div class="push"></div>
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>