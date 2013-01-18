<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file = "/jsp/resources/includes/resources.html" %>   
<title>Add/Edit Department</title>
<script>
function validateForm()
{
    if(document.frm.name.value=="")
    {
      alert("Name should not be left blank");
      document.frm.name.focus();
      return false;
    }
    else if(document.frm.address.value=="")
    {
      alert("Address should not be left blank");
      document.frm.address.focus();
      return false;
    } else if(document.frm.budget.value=="") {
      alert("Budget should not be left blank");
      document.frm.budget.focus();
      return false;
    }
}
</script>
</head>
<body>
<div id="container" class="container">
<%@ include file = "/jsp/resources/includes/header.html" %>   
<%@ include file = "/jsp/resources/includes/menu.html" %>   
<% String appname= application.getServletContextName(); %>
<div id="content" class="content">
	<h1>Add/Edit Department</h1>
    <form:form method="POST" action='<%=request.getContextPath()+"/control/department/save.html"%>' commandName="frm" onSubmit="return validateForm()">
    <table>
        <tbody>
        <tr><td>Id :      </td><td><input type="text" name="departmentId" readonly="readonly" value="${department.departmentId}" /></td></tr>
        <tr><td>Name :    </td><td><input type="text" name="name" value="${department.name}" /></td></tr>
        <tr><td>Address : </td><td><input type="text" name="address" value="${department.address}" /></td></tr>
        <tr><td>Budget  : </td><td><input type="text" name="budget" value="${department.budget}" /></td></tr>
        <tr><td><input type="submit" value="OK" /></td><td></td></tr>
        </tbody>
    </table>
    </form:form>
</div> <!-- content -->
<div class="push"></div>
</div> <!-- container -->
<%@ include file = "/jsp/resources/includes/footer.html" %>   
</body>
</html>
