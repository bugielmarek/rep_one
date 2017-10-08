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
		document.formIssue.name.focus();
	});
</script>

<script>
	$(function() {
		$("#date")
				.datepicker(
						{
							dateFormat : 'yy-mm-dd',
							firstDay : 1,
							minDate : 0,
							beforeShowDay : $.datepicker.noWeekends,
							numberOfMonths : 2,
							dayNamesMin : [ "Nie", "Pon", "Wt", "Śr", "Czw",
									"Pi", "So" ],
							dayNames : [ "Niedziela", "Poniedziałek", "Wtorek",
									"Środa", "Czwartek", "Piątek", "Sobota" ],
							monthNames : [ "Styczeń", "Luty", "Marzec",
									"Kwiecień", "Maj", "Czerwiec", "Lipiec",
									"Sierpień", "Wrzesień", "Październik",
									"Listopad", "Grudzień" ]
						});
	});
</script>

<c:url var="createUrl" value="/issues/createissue" />
<c:url var="issuesUrl" value="/issues" />

<table class="create">
	<tr>
		<th colspan=3><spring:message code="issue.tableHead" /></th>
	</tr>

	<sf:form name="formIssue" method="post" action="${createUrl}"
		commandName="issue">

		<tr>
			<td><spring:message code="issue.tableRowName" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="name" /><br>
				<div class="errors">
					<sf:errors path="name"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="issue.tableRowCaseType" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td>
				<ul class="sygList">
					<li><sf:radiobutton path="caseType" value="KM" />
						<div class="km">KM</div></li>
					<li><sf:radiobutton path="caseType" value="KMS" />
						<div class="kms">KMS</div></li>
					<li><sf:radiobutton path="caseType" value="KMP" />
						<div class="kmp">KMP</div></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td><spring:message code="issue.tableRowSygNumber" />
				<div class="smallItalic">
					<spring:message code="formOptionalField" />
				</div></td>
			<td><sf:input type="text" path="sygNumber" /><br>
				<div class="errors">
					<sf:errors path="sygNumber"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="issue.tableRowDeadline" /></td>
			<td><sf:input type="text" path="date" id="date" /><br>
				<div class="errors">
					<sf:errors path="date"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="issue.tableRowText" /></td>
			<td><sf:textarea path="text" class="textArea" /><br>
				<div class="errors">
					<sf:errors path="text"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td></td>
			<td><sf:input type="hidden" path="id" value="${issue.id}" /> <input
				class="formButton" type="submit"
				value="<spring:message code='button.add'/>" /> <a
				href="${issuesUrl}" class="formButton"><spring:message
						code='button.cancel' /></a></td>
		</tr>
	</sf:form>
</table>
