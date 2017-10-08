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
		document.formLocation.country.focus();
	});
</script>



<c:url var="findLocation" value="/restclient/findweather" />

<table class="create">
	<tr>
		<th colspan=2>Supply data to find weather</th>
	</tr>

	<sf:form name="formLocation" method="post" commandName="location"
		action="${findLocation}">

		<tr>
			<td>Country</td>
			<td><sf:input type="text" path="country" /></td>
		</tr>
		<tr>
			<td>Region</td>
			<td><sf:input type="text" path="region" /></td>
		</tr>
		<tr>
			<td>City</td>
			<td><sf:input type="text" path="city" /><br></td>
		</tr>
		<tr>
			<td></td>
			<td><input class="formButton" type="submit"
				value="<spring:message code='button.add'/>" /></td>
		</tr>
	</sf:form>
</table>
