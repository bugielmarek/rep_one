<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->

<table class="quoteTable">
	<tr>
		<th colspan=2><spring:message code="quotetable.title"/></th>
	</tr>
	<tr>
		<td><spring:message code="quotetable.quote"/></td>
		<td><spring:message code="quotetable.author"/></td>
	</tr>
	<tr>
		<td><c:out value="${quote.quote}" /></td>
		<td><c:out value="${quote.author}" /></td>
	</tr>
</table>
