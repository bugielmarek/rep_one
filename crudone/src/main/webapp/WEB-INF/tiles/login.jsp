<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value='/static/css/main.css'/>" rel="stylesheet"
	type="text/css" />
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->


<script type="text/javascript">
	$(document).ready(function() {
		document.formLogin.username.focus();
	});
</script>

<sf:form name='formLogin' action='${pageContext.request.contextPath}/login'
	method='POST'>
	<table class="loginTable">
		<tr>
			<th colspan=2><spring:message code="user.tableHeadLoginPanel" /></th>
		</tr>
		<tr>
			<td class="formText"><spring:message code="user.tableRowLogin" /></td>
			<td><input type='text' name='username' /></td>
		</tr>
		<tr>
			<td class="formText"><spring:message code="user.tableRowPassword" /></td>
			<td><input type='password' name='password' /></td>
		</tr>
		<tr>
			<td></td>
			<td><input class="formButton" name="submit" type="submit"
				value="ZALOGUJ" /></td>
		</tr>
		<tr>
			
			<td colspan=2><div class="rememberMe"><spring:message code="login.rememberME" />
				<input type='checkbox'
				name='remember-me' checked="checked" /></div></td>
		</tr>
		<c:if test="${param.error != null}">
			<tr>
				<td colspan=2><div class="errors"><spring:message code="login.error"/></div></td>
			</tr>
		</c:if>
	</table>
</sf:form>
