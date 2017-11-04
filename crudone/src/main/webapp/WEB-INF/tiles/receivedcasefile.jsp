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


<c:url var="editUrl" value="/receivedcasefiles/editreceivedcasefile/${receivedCaseFile.id}" />
<c:url var="deleteUrl"
	value="/receivedcasefiles/deletereceivedcasefile/${receivedCaseFile.id}" />

<table class="resultsTable">
	<tr>
		<th><spring:message code="receivedCaseFile.tableRowSignature" /></th>
		<th><spring:message code="receivedCaseFile.tableRowArrivedAt" /></th>
		<th><spring:message code="receivedCaseFile.tableRowOffice" /></th>
		<th><spring:message code="receivedCaseFile.tableRowName" /></th>
		<th><spring:message code="receivedCaseFile.tableRowModify" /></th>
	</tr>
	<tr>
		<td><c:out value="${receivedCaseFile.caseType}${'_'}${receivedCaseFile.sygNumber}" /></td>
		<td><c:out value="${receivedCaseFile.arrivedAt}" /></td>
		<td><c:out value="${receivedCaseFile.office}" /></td>
		<td class="cell"><c:out value="${receivedCaseFile.name}" /></td>
		<td><a href="${editUrl}" class="resultsButton_1"><spring:message
					code='button.edit' /></a> <a href="${deleteUrl}"
			class="resultsButton_2"><spring:message code='button.delete' /></a>
		</td>
	</tr>
</table>
