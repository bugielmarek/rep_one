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
		document.formConvergence.name.focus();
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
							dayNamesMin : [ "Nie", "Pon", "Wt", "År", "Czw",
									"Pi", "So" ],
							dayNames : [ "Niedziela", "Poniedziałek", "Wtorek",
									"Środa", "Czwartek", "Piątek", "Sobota" ],
							monthNames : [ "Styczeń", "Luty", "Marzec",
									"Kwiecień", "Maj", "Czerwiec", "Lipiec",
									"Sierpień", "Wrzesień", "Październik",
									"Listopad", "Grudzień" ]
						});
	});
</script>

<c:url var="createUrl" value="/convergences/createconvergence" />
<c:url var="convergencesUrl" value="/convergences" />

<table class="create">
	<tr>
		<th colspan=3><spring:message code="convergence.tableHead" /></th>
	</tr>

	<sf:form name="formConvergence" method="post" action="${createUrl}"
		commandName="convergence">

		<tr>
			<td><spring:message code="convergence.tableRowName" /></td>
			<td><sf:input type="text" path="name" /><br>
				<div class="errors">
					<sf:errors path="name"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="convergence.tableRowOffice" /></td>
			<td><sf:input type="text" path="office" /><br>
				<div class="errors">
					<sf:errors path="office"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="convergence.tableRowCaseType" /></td>
			<td>
				<ul class="sygList">
					<li><sf:radiobutton path="caseType" value="KM"
							checked="checked" />
						<div class="km">KM</div></li>
					<li><sf:radiobutton path="caseType" value="KMS" />
						<div class="kms">KMS</div></li>
					<li><sf:radiobutton path="caseType" value="KMP" />
						<div class="kmp">KMP</div></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td><spring:message code="convergence.tableRowSygNumber" /></td>
			<td><sf:input type="text" path="sygNumber" /><br>
				<div class="errors">
					<sf:errors path="sygNumber"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="convergence.tableRowArrivedAt" /></td>
			<td><sf:input type="text" path="date" id="date" /><br>
				<div class="errors">
					<sf:errors path="date"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td></td>
			<td><sf:input type="hidden" path="id" value="${convergence.id}" />
				<input class="formButton" type="submit"
				value="<spring:message code='button.add'/>" /> <a
				href="${convergencesUrl}" class="formButton"><spring:message
						code='button.cancel' /></a></td>
		</tr>
	</sf:form>
</table>
