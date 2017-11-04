<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
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


<c:url var="editUrl" value="/users/edituser/${user.id}" />
<c:url var="deleteUrl" value="/users/deleteuser/${user.id}" />

<table class="users">
	<tr>
		<th colspan=2><spring:message code="users.tableHead" /></th>
	</tr>
	<tr>
		<td><c:out value="${user.username}"></c:out></td>
		<td><a href="${editUrl}" class="resultsButton_1"><spring:message
					code='button.edit' /></a> <a href="${deleteUrl}"
			class="resultsButton_2"><spring:message code='button.delete' /></a></td>
	</tr>

</table>
