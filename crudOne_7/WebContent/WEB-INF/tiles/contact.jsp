<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ct" tagdir="/WEB-INF/tags"%>


<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->

<script type="text/javascript">
	function onDeleteClick(event) {

		var doDelete = confirm("USUNĄĆ REKORD?");

		if (doDelete == false) {
			event.preventDefault();
		}
	}

	function onReady() {
		$(".resultsButton_2").click(onDeleteClick);
	}

	$(document).ready(onReady);
</script>

<c:url var="editUrl" value="/contacts/editcontact/${contact.id}" />
<c:url var="deleteUrl" value="/contacts/deletecontact/${contact.id}" />

<table class="resultsTable">
	<tr>
		<th><spring:message code="contact.tableRowName" /></th>
		<th><spring:message code="contact.tableRowFirstName" /></th>
		<th><spring:message code="contact.tableRowLastName" /></th>
		<th><spring:message code="contact.tableRowEmail" /></th>
		<th><spring:message code="contact.tableRowPhoneNumber" /></th>
		<th><spring:message code="contact.tableRowText" /></th>
		<th><spring:message code="contact.tableRowModify" /></th>
	</tr>
	<tr>
		<td><c:out value="${contact.name}" /></td>
		<td><c:out value="${contact.firstName}" /></td>
		<td><c:out value="${contact.lastName}" /></td>
		<td><c:out value="${contact.email}" /></td>
		<td><c:out value="${contact.phoneNumber}" /></td>
		<td><c:out value="${contact.text}" /></td>
		<td><a href="${editUrl}" class="resultsButton_1"><spring:message
					code='button.edit' /></a> <a href="${deleteUrl}"
			class="resultsButton_2"><spring:message code='button.delete' /></a>
		</td>
	</tr>
</table>