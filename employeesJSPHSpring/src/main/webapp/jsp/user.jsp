<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "/jsp/resources/includes/resources.html" %>   
<title>Add/Edit User</title>
</head>
<body>
<div id="container" class="container">
<%@ include file = "/jsp/resources/includes/header.html" %>   
<%@ include file = "/jsp/resources/includes/menu.html" %>   
<% String appname= application.getServletContextName(); %>
<div id="content" class="content">
	<h1>Add/Edit User</h1>
    <form:form method="POST" action='<%=request.getContextPath()+"/control/user/save.html"%>' commandName="frm">
    <c:set var="gevonden" scope="session" value="0"/>
 	<input type="hidden" name="userId" value="${user.userId}"/>
    <table>
        <tbody>
        <tr><td>User Name :    </td><td><input type="text" name="userName" value="${user.userName}" /></td></tr>
        <tr><td>Password : </td><td><input type="text" name="password" value="${user.password}" /></td></tr>
        <tr><td>Email  : </td><td><input type="text" name="email" value="${user.email}" /></td></tr>
        <tr><td>Roles : </td>
        <td>
        <select name="roleId" multiple="multiple">
          <c:forEach items="${roles}" var="role">
    		  <c:set var="gevonden" scope="session" value="0"/>
	          <c:forEach items="${usroles}" var="usrole">
	               <c:if test="${role.roleId == usrole.role.roleId}">
	            	   <option selected value="<c:out value="${role.roleId}"/>"> <c:out value="${role.name}"/></option>
				       <c:set var="gevonden" scope="session" value="1"/>
	               </c:if>
	          </c:forEach>
	          <c:if test="${gevonden eq 0}">
	             <option value="<c:out value="${role.roleId}"/>"> <c:out value="${role.name}"/></option>
	          </c:if>
          </c:forEach>
        </select><br />
        </td>
        </tr>
        <tr><td><input type="submit" value="Submit" /></td><td></td></tr>
        </tbody>
    </table>
    </form:form>    
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "/jsp/resources/includes/footer.html" %>   
</body>
</html>
