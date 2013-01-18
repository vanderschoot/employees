<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Departments</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
<h1>Departments</h1>
    <table border=1>
        <thead>
            <tr>
                <th>Department Id</th>
                <th>Name</th>
                <th>Address</th>
                <th>Budget</th>
                <th colspan=2>Action</th>
                <th>Employees</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${departments}" var="dep">
                <tr>
                    <td><c:out value="${dep.departmentId}" /></td>
                    <td><c:out value="${dep.name}" /></td>
                    <td><c:out value="${dep.address}" /></td>
                    <td><c:out value="${dep.budget}" /></td>
                    <td><a href="<%=request.getContextPath()%>/control/department/edit.html?departmentId=<c:out value="${dep.departmentId}" />">Update</a></td>
                    <td><a href="<%=request.getContextPath()%>/control/department/delete.html?departmentId=<c:out value="${dep.departmentId}" />">Delete</a></td>
                    <td align="center"><a href="<%=request.getContextPath()%>/control/employee/list4dep.html?departmentId=<c:out value="${dep.departmentId}" />">-&gt;</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="<%=request.getContextPath()%>/jsp/department.jsp">Add Department</a></p>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>