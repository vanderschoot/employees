<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Employees</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
<h1>Employees</h1>
    <table border=1>
        <thead>
            <tr>
                <th>Employee Id</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Birthdate</th>
                <th>Department</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${employees}" var="emp">
                <tr>
                    <td><c:out value="${emp.employeeId}" /></td>
                    <td><c:out value="${emp.firstName}" /></td>
                    <td><c:out value="${emp.lastName}" /></td>
                    <td><c:out value="${emp.birthDate}" /></td>
                    <td><c:out value="${emp.department.name}" /></td>
                    <td><a href="EmployeeController?action=edit&employeeId=<c:out value="${emp.employeeId}" />">Update</a></td>
                    <td><a href="EmployeeController?action=delete&employeeId=<c:out value="${emp.employeeId}" />">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="EmployeeController?action=insert">Add Employee</a></p>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "resources/includes/footer.html" %>   
</body>
</html>