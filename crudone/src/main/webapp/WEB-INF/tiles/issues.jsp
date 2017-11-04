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

<c:url var="url" value="/issues" />

<table class="resultsTable">
	<tr>
		<th><spring:message code="issue.tableRowAddedBy" /></th>
		<th><spring:message code="issue.tableRowSignature" /></th>
		<th><spring:message code="issue.tableRowName" /></th>
		<th><spring:message code="issue.tableRowDeadline" /></th>
		<th><spring:message code="issue.tableRowText" /></th>
		<th><spring:message code="issue.tableRowModify" /></th>
	</tr>
	<c:if test="${page.totalPages != 1 }">
		<tr>
			<td colspan=6 class="cell_2"><ct:pagination page="${page}"
					url="${url}" size="10" /></td>
		</tr>
	</c:if>
	<c:forEach var="issue" items="${page.content}">

		<c:url var="editUrl" value="/issues/editissue/${issue.id}" />
		<c:url var="deleteUrl" value="/issues/deleteissue/${issue.id}" />
		<tr>
			<sf:form commandName="issue">
				<td><c:out value="${issue.username}" /></td>
				<td><c:out value="${issue.caseType}${'_'}${issue.sygNumber}" /></td>
				<td><c:out value="${issue.name}" /></td>
				<td class="cell"><c:out value="${issue.deadline}" /></td>
				<td><c:out value="${issue.text}" /></td>
				<td><a href="${editUrl}" class="resultsButton_1"><spring:message
							code='button.edit' /></a> <a href="${deleteUrl}"
					class="resultsButton_2"><spring:message code='button.delete' /></a>
				</td>
			</sf:form>
		</tr>
	</c:forEach>
</table>
