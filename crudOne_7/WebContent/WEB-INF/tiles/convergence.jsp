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


<c:url var="editUrl" value="/convergences/editpayment/${convergence.id}" />
<c:url var="deleteUrl"
	value="/convergences/deletepayment/${convergence.id}" />

<table class="resultsTable">
	<tr>
		<th><spring:message code="convergence.tableRowAddedBy" /></th>
		<th><spring:message code="convergence.tableRowSignature" /></th>
		<th><spring:message code="convergence.tableRowArrivedAt" /></th>
		<th><spring:message code="convergence.tableRowOffice" /></th>
		<th><spring:message code="convergence.tableRowName" /></th>
		<th><spring:message code="convergence.tableRowModify" /></th>
	</tr>
	<tr>
		<td><c:out value="${convergence.username}" /></td>
		<td><c:out value="${convergence.caseType}${'_'}${convergence.sygNumber}" /></td>
		<td><c:out value="${convergence.arrivedAt}" /></td>
		<td><c:out value="${convergence.office}" /></td>
		<td class="cell"><c:out value="${convergence.name}" /></td>
		<td><a href="${editUrl}" class="resultsButton_1"><spring:message
					code='button.edit' /></a> <a href="${deleteUrl}"
			class="resultsButton_2"><spring:message code='button.delete' /></a>
		</td>
	</tr>
</table>
