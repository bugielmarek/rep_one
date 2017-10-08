<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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

<c:url var="url" value="/payments" />


<table class="resultsTable">

	<tr>
		<th><spring:message code="payment.tableRowAddedBy" /></th>
		<th><spring:message code="payment.tableRowName" /></th>
		<th><spring:message code="payment.tableRowSignature" /></th>
		<th><spring:message code="payment.tableRowDeadline" /></th>
		<th><spring:message code="payment.tableRowModify" /></th>
	</tr>
	<c:if test="${page.totalPages != 1}">
		<tr>
			<td colspan=5 class="cell_2"><ct:pagination page="${page}"
					url="${url}" size="10" /></td>
		</tr>
	</c:if>
	<c:forEach var="payment" items="${page.content}">

		<c:url var="editUrl" value="/payments/editpayment/${payment.id}" />
		<c:url var="deleteUrl" value="/payments/deletepayment/${payment.id}" />
		<tr>
			<sf:form commandName="payment">
				<td><c:out value="${payment.username}" /></td>
				<td><c:out value="${payment.name}" /></td>
				<td><c:out value="${payment.caseType}${payment.sygNumber}" /></td>
				<td class="cell"><c:out value="${payment.deadline}" /></td>
				<td><sf:input type="hidden" path="id" value="${payment.id}" />
					<a href="${editUrl}" class="resultsButton_1"><spring:message
							code='button.edit' /></a> <a href="${deleteUrl}"
					class="resultsButton_2"><spring:message code='button.delete' /></a>
				</td>
			</sf:form>
		</tr>
	</c:forEach>
</table>
