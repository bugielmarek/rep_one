<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->


<script type="text/javascript">
	$(document).ready(function() {
		document.formUser.username.focus();
	});
</script>

<c:url var="createUrl" value="/users/createuser" />
<c:url var="usersUrl" value="/users" />

<table class="create">
	<tr>
		<th colspan=2><spring:message code="user.tableHeadEnterData" /></th>
	</tr>
	<sf:form name="formUser" method="post" action="${createUrl}"
		commandName="user">

		<tr>
			<td><spring:message code="user.tableRowLogin" /></td>
			<td><sf:input type="text" path="username" /><br>
				<div class="errors">
					<sf:errors path="username"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="user.tableRowPassword" /></td>
			<td><sf:input type="password" id="password"
					path="password" /><br>
				<div class="errors">
					<sf:errors path="password"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="user.tableRowConfirmPassword" /></td>
			<td><sf:input type="password" id="confirmpass" path="formPassword" /><br>
				<div class="errors">
					<sf:errors path="formPassword"></sf:errors>
				</div>
				<div id="matchpass"></div></td>
		</tr>
		<tr>
			<td></td>
			<td><sf:input type="hidden" path="id" value="${user.id}" /> <input
				type="submit" class="formButton"
				value="<spring:message code='button.add'/>" /> <a
				href="${usersUrl}" class="formButton"><spring:message
						code='button.cancel' /></a></td>
		</tr>
	</sf:form>
</table>
