<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<link href="<c:url value='/static/css/main.css'/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->



<script type="text/javascript">
	$(document).ready(function() {
		document.formContact.name.focus();
	});
</script>



<c:url var="createUrl" value="/contacts/createcontact" />
<c:url var="contactsUrl" value="/contacts" />
<table class="create">
	<tr>
		<th colspan=3><spring:message code="contact.tableHead" /></th>
	</tr>

	<sf:form name="formContact" method="post" action="${createUrl}"
		commandName="contact">

		<tr>
			<td><spring:message code="contact.tableRowName" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="name" /><br>
				<div class="errors">
					<sf:errors path="name"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="contact.tableRowFirstName" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="firstName" /><br>
				<div class="errors">
					<sf:errors path="firstName"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="contact.tableRowLastName" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="lastName" /><br>
				<div class="errors">
					<sf:errors path="lastName"></sf:errors>
				</div></td>
		</tr>

		<tr>
			<td><spring:message code="contact.tableRowPhoneNumber" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="phoneNumber" /><br>
				<div class="errors">
					<sf:errors path="phoneNumber"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="contact.tableRowEmail" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="email" /><br>
				<div class="errors">
					<sf:errors path="email"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="contact.tableRowText" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="text" /><br>
				<div class="errors">
					<sf:errors path="text"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td></td>
			<td><sf:input type="hidden" path="id" value="${contact.id}" />
				<input class="formButton" type="submit"
				value="<spring:message code='button.add'/>" /> <a
				href="${contactsUrl}" class="formButton"><spring:message
						code='button.cancel' /></a></td>
		</tr>
	</sf:form>
</table>

