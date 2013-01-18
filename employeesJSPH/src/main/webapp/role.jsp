<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Add/Edit Role</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
	<h1>Add/Edit Role</h1>
    <form method="POST" action="RoleController" name="frm" onSubmit="return validateForm()">
    <table>
        <tbody>
        <tr><td>Id :      </td><td><input type="text" name="roleId" readonly="readonly" value="${role.roleId}" /></td></tr>
        <tr><td>Name :    </td><td><input type="text" name="name" value="${role.name}" /></td></tr>
        <tr><td><input type="submit" value="Submit" /></td><td></td></tr>
        </tbody>
    </table>
    </form>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>
