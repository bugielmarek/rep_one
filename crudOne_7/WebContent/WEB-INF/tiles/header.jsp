<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->


<!-- ========================================= HOME ========================================= -->
<div class="webAppTitle">
	<spring:message code="header.webAppTitle" />
</div>




<sec:authorize access="isAuthenticated()">
	<nav>
		<ul>
			<li><a href="<c:url value='/' />"><spring:message
						code="header.navbarSearch" /></a></li>
			<li><a href="<c:url value='/payments' />"> <spring:message
						code="header.navbarPayments" />
			</a>
				<ul>
					<li><a href="<c:url value='/payments/createpayment' />"><spring:message
								code="header.navbarAddDeadline" /> </a></li>
				</ul></li>

			<li><a href="<c:url value='/convergences' />"> <spring:message
						code="header.navbarConvergence" /></a>
				<ul>

					<li><a
						href="<c:url value='/convergences/createconvergence' />"><spring:message
								code="header.navbarAddConvergence" /> </a></li>
				</ul></li>

			<li><a href="<c:url value='/contacts' />"> <spring:message
						code="header.navbarContacts" /></a>
				<ul>

					<li><a href="<c:url value='/contacts/createcontact' />"><spring:message
								code="header.navbarAddContact" /> </a></li>
				</ul></li>

			<li><a href="<c:url value='/issues' />"> <spring:message
						code="header.navbarIssues" /></a>
				<ul>

					<li><a href="<c:url value='/issues/myissues' />"><spring:message
								code="header.navbarMyIssue" /> </a></li>
					<li><a href="<c:url value='/issues/createissue' />"><spring:message
								code="header.navbarAddIssue" /> </a></li>
				</ul></li>
			<li><div class="dropList">
					<spring:message code="language" />
				</div>
				<ul>
					<li><a href="<c:url value='/?language=pl_PL'/>"><spring:message
								code="language.pl" /></a></li>
					<li><a href="<c:url value='/?language=en_us'/>"><spring:message
								code="language.en" /></a></li>
				</ul></li>

			<li><a href="<c:url value='/quote'/>"><spring:message code="header.navbarQuote"/> </a> 
			</li>

			<li><sec:authorize access="hasAuthority('ROLE_ADMIN')">
					<div class="dropList">
						<spring:message code="header.navbarAdministrator" />
					</div>
				</sec:authorize>
				<ul>
					<li><a href="<c:url value='/users' /> "> <spring:message
								code="header.navbarDeleteUser" />
					</a></li>
					<li><a href="<c:url value='/users/createuser' /> "><spring:message
								code="header.navbarAddUser" /> </a></li>
				</ul></li>
			<li style="float: right"><c:url var="logoutUrl" value="/logout" />
				<form action="${logoutUrl}" method="post">
					<input class="logoutButton" type="submit"
						value="<spring:message code='button.logout' />" /> <input
						type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form></li>
			<li style="float: right"><div class="principal">
					<sec:authentication property="name" />
				</div></li>
		</ul>
	</nav>
</sec:authorize>















<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->


