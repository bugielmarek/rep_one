<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<link href="<c:url value='/static/css/main.css'/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->



<script type="text/javascript">
	$(document).ready(function() {
		document.formPayment.name.focus();
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

<c:url var="createUrl" value="/payments/createpayment" />
<c:url var="paymentsUrl" value="/payments" />

<table class="create">
	<tr>
		<th colspan=3><spring:message code="payment.tableHead" /></th>
	</tr>

	<sf:form name="formPayment" method="post" commandName="payment"
		action="${createUrl}">

		<tr>
			<td><spring:message code="payment.tableRowName" /></td>
			<td><sf:input type="text" path="name" /><br>
				<div class="errors">
					<sf:errors path="name"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="payment.tableRowCaseType" /></td>
			<td>
				<ul class="sygList">
					<li><sf:radiobutton path="caseType" value="KM" checked="checked" />
						<div class="km">KM</div></li>
					<li><sf:radiobutton path="caseType" value="KMS" />
						<div class="kms">KMS</div></li>
					<li><sf:radiobutton path="caseType" value="KMP" />
						<div class="kmp">KMP</div></li>
				</ul> <br>
				<div class="errors">
				<sf:errors path="caseType" />
				</div>
			</td>
		</tr>
		<tr>
			<td><spring:message code="payment.tableRowSygNumber" /></td>
			<td><sf:input type="text" path="sygNumber" /><br>
				<div class="errors">
					<sf:errors path="sygNumber" />
				</div></td>
		</tr>
		<tr>
			<td><spring:message code="payment.tableRowDeadline" /></td>
			<td><sf:input type="text" path="date" id="date" /><br>
				<div class="errors">
					<sf:errors path="date"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td></td>
			<td><sf:input type="hidden" path="id" value="${payment.id}" />
				<input class="formButton" type="submit"
				value="<spring:message code='button.add'/>" /> <a
				href="${paymentsUrl}" class="formButton"><spring:message
						code='button.cancel' /></a></td>
		</tr>
	</sf:form>
</table>


<c:if test="${paymentExists != null}">
	<table class="existsTable">
		<tr>
			<th><spring:message code="createpayment.paymentExists.part1" /></th>
		</tr>
		<tr>
			<td><spring:message code="createpayment.paymentExists.part2" /></td>
		</tr>
		<tr>
			<td class="cell"><spring:message code="payment.tableRowName" />
				<c:out value=": ${paymentExists.name}" /></td>
		</tr>
		<tr>
			<td class="cell"><spring:message
					code="payment.tableRowSignature" /> <c:out
					value=": ${paymentExists.caseType}${paymentExists.sygNumber}" /></td>
		</tr>
		<tr>
			<td><spring:message code="createpayment.paymentExists.part3" /></td>
		</tr>
	</table>
</c:if>

