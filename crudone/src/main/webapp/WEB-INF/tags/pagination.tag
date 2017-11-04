<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- --------------ATRIBUTES----------------- --%>
<%@ attribute name="page" required="true"
	type="org.springframework.data.domain.Page"%>
<%@ attribute name="url" required="true"%>
<%@ attribute name="size" required="true"%>

<%-- --------------VARIABLES----------------- --%>
<c:set var="block" value="${empty param.b ? 0 : param.b }" />
<c:set var="startPage" value="${block * size + 1}" />
<c:set var="endPage" value="${(block + 1) * size }" />
<c:set var="endPage"
	value="${endPage > page.totalPages ? page.totalPages : endPage}" />



<%-- --------------DISPLAY----------------- --%>
<div class="pageNumbers">

	<c:if test="${block != 0}">
		<a href="${url}?b=${block -1}&p=${(block - 1) * size + 1}">&lt;&lt;</a>
	</c:if>

	<c:forEach var="pageNumber" begin="${startPage}" end="${endPage}">

		<c:choose>
			<c:when test="${page.number != pageNumber -1}">
				<a href="${url}?p=${pageNumber}&b=${block}"><c:out value="${pageNumber}" /></a>
			</c:when>
			<c:otherwise>
				<div class="currentPage">
					<c:out value="${pageNumber}" />
				</div>
			</c:otherwise>
		</c:choose>

		<c:if test="${pageNumber != endPage}">
			|
			</c:if>
	</c:forEach>

	<c:if test="${endPage != page.totalPages}">
		<a href="${url}?b=${block +1}&p=${(block +1) * size + 1}">&gt;&gt;</a>
	</c:if>
</div>