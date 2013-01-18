<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Roles</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
<h1>Roles</h1>
    <table border=1>
        <thead>
            <tr>
                <th>Role Id</th>
                <th>Name</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${roles}" var="role">
                <tr>
                    <td><c:out value="${role.roleId}" /></td>
                    <td><c:out value="${role.name}" /></td>
                    <td><a href="<%=request.getContextPath()%>/control/role/edit.html?roleId=<c:out value="${role.roleId}" />">Update</a></td>
                    <td><a href="<%=request.getContextPath()%>/control/role/delete.html?roleId=<c:out value="${role.roleId}" />">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="<%=request.getContextPath()%>/jsp/role.jsp">Add Role</a></p>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>