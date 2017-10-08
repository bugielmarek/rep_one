<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript"
	src="<c:url value='/static/script/jquery.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/static/jquery-ui/jquery-ui.min.js'/>"></script>



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><tiles:insertAttribute name="title" /></title>
<link href="<c:url value='/static/css/main.css'/>"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value='/static/jquery-ui/jquery-ui.min.css'/>"
	rel="stylesheet" type="text/css" />
<tiles:insertAttribute name="includes"></tiles:insertAttribute>

</head>

<body>




	<div class="header">
		<tiles:insertAttribute name="header"></tiles:insertAttribute>
	</div> 
	<div class="content">
		<tiles:insertAttribute name="content"></tiles:insertAttribute>
	</div>
	<div class="footer">
		<tiles:insertAttribute name="footer"></tiles:insertAttribute>
	</div>
	
</body>
</html>