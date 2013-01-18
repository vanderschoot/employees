<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Users</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
<h1>Users</h1>
    <table border=1>
        <thead>
            <tr>
                <th>User Id</th>
                <th>User Name</th>
                <th>Password</th>
                <th>Email</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="usr">
                <tr>
                    <td><c:out value="${usr.userId}" /></td>
                    <td><c:out value="${usr.userName}" /></td>
                    <td><c:out value="${usr.password}" /></td>
                    <td><c:out value="${usr.email}" /></td>
                    <td><a href="<%=request.getContextPath()%>/control/user/edit.html?userId=<c:out value="${usr.userId}" />">Update</a></td>
                    <td><a href="<%=request.getContextPath()%>/control/user/delete.html?userId=<c:out value="${usr.userId}" />">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="<%=request.getContextPath()%>/control/user/add.html">Add User</a></p>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>