<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "resources/includes/resources.html" %>   
<title>Add/Edit Employee</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "resources/includes/header.html" %>   
<%@ include file = "resources/includes/menu.html" %>   
<div id="content" class="content">
	<h1>Add/Edit Employee</h1>
    <form method="POST" action="EmployeeController" name="frm">
    <table>
        <tbody>
        <tr><td>Id :         </td><td><input type="text" readonly="readonly" name="employeeId" value="${employee.employeeId}" /></td></tr>
        <tr><td>First Name : </td><td><input type="text" name="firstName" value="${employee.firstName}" /></td></tr>
        <tr><td>Last Name  : </td><td><input type="text" name="lastName" value="${employee.lastName}" /></td></tr>
        <tr><td>Birthdate  : </td><td><input type="text" name="birthDate" value="${employee.birthDate}" /></td></tr>
        <tr><td>Department : </td>
        <td>
        <select name="departmentId">
          <c:forEach items="${departments}" var="dep">
            <c:if test="${dep.departmentId == employee.department.departmentId}">
            	<option selected value=" <c:out value="${dep.departmentId}"/>"> <c:out value="${dep.name}"/></option>
            </c:if>
            <c:if test="${dep.departmentId != employee.department.departmentId}">
            	<option value=" <c:out value="${dep.departmentId}"/>"> <c:out value="${dep.name}"/></option>
            </c:if>
          </c:forEach>
        </select><br />
        </td>
        </tr>
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

