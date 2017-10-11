<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->

<c:url var="payments" value="/payments/findpayment" />
<c:url var="convergences" value="/convergences/findconvergence" />
<c:url var="contacts" value="/contacts/findcontact" />
<c:url var="issues" value="/issues/findissue" />

<table class="searchTable">
	<sf:form method="post" action="${payments}" modelAtribute="formClass"
		commandName="formClass">
		<tr>
			<td colspan=2 class="cell_2"><spring:message
					code="search.tableRowPayment" /></td>
			<td colspan=2 class="cell_2"><div class="errors_2">
					<sf:errors path="payment.name" />
					<sf:errors path="payment.sygNumber" />
				</div></td>
		</tr>
		<tr>
			<td><ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowPaymentByName" />
						</div></li>
					<li><sf:input path="payment.name" type="text" /></li>
				</ul></td>
			<td><ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowPaymentBySignature" />
						</div></li>
					<li><sf:input path="payment.sygNumber" type="text" /></li>
				</ul></td>
			<td>
				<ul class="sygList">
					<li><sf:radiobutton path="payment.caseType" value="KM"
							checked="checked" />
						<div class="km">KM</div></li>
					<li><sf:radiobutton path="payment.caseType" value="KMS" />
						<div class="kms">KMS</div></li>
					<li><sf:radiobutton path="payment.caseType" value="KMP" />
						<div class="kmp">KMP</div></li>
				</ul>
			</td>
			<td><input class="formButton" type="submit"
				value="<spring:message code='button.search'/>" /></td>
		</tr>
	</sf:form>
	<sf:form method="post" action="${convergences}"
		modelAtribute="formClass" commandName="formClass">
		<tr>
			<td colspan=2 class="cell_2"><spring:message
					code="search.tableRowConvergence" /></td>
			<td colspan=2 class="cell_2"><div class="errors_2">
					<sf:errors path="convergence.name" />
				</div></td>
		</tr>

		<tr>
			<td colspan=3>
				<ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowConvergenceByName" />
						</div></li>
					<li><sf:input path="convergence.name" type="text" /></li>
				</ul>
			</td>

			<td><input class="formButton" type="submit"
				value="<spring:message code='button.search'/>" /></td>
		</tr>
	</sf:form>
	<sf:form method="post" action="${contacts}" modelAtribute="formClass"
		commandName="formClass">
		<tr>
			<td colspan=2 class="cell_2"><spring:message
					code="search.tableRowContact" /></td>
			<td colspan=2 class="cell_2"><div class="errors_2">
					<sf:errors path="contact.name" />
				</div></td>
		</tr>
		<tr>
			<td colspan=3>
				<ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowContactByName" />
						</div></li>
					<li><sf:input path="contact.name" type="text" /></li>
				</ul>
			</td>

			<td><input class="formButton" type="submit"
				value="<spring:message code='button.search'/>" /></td>
		</tr>
	</sf:form>
	<sf:form method="post" action="${issues}" modelAtribute="formClass"
		commandName="formClass">
		<tr>
			<td colspan=2 class="cell_2"><spring:message
					code="search.tableRowIssue" /></td>
			<td colspan=2 class="cell_2"><div class="errors_2">
					<sf:errors path="issue.name" />
					<sf:errors path="issue.sygNumber" />
				</div></td>
		</tr>
		<tr>
			<td>
				<ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowIssueByName" />
						</div></li>
					<li><sf:input path="issue.name" type="text" /></li>
				</ul>
			</td>
			<td><ul>
					<li><div class="smallItalic">
							<spring:message code="search.tableRowIssueBySignature" />
						</div></li>
					<li><sf:input path="issue.sygNumber" type="text" /></li>
				</ul></td>
			<td>
				<ul class="sygList">
					<li><sf:radiobutton path="issue.caseType" value="KM"
							checked="checked" />
						<div class="km">KM</div></li>
					<li><sf:radiobutton path="issue.caseType" value="KMS" />
						<div class="kms">KMS</div></li>
					<li><sf:radiobutton path="issue.caseType" value="KMP" />
						<div class="kmp">KMP</div></li>
				</ul>
			</td>
			<td><input class="formButton" type="submit"
				value="<spring:message code='button.search'/>" /></td>
		</tr>
	</sf:form>
</table>
