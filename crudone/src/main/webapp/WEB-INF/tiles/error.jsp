<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<spring:message code="errorView" />
<c:out value="${error}"/>