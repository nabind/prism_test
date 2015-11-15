<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${not empty dataloadMessage}">
		<%@ include file="rescoreRequestFormDisabled.jsp" %>
	</c:when>
	<c:otherwise>
		<%@ include file="rescoreRequestFormEnabled.jsp" %>
	</c:otherwise>
</c:choose>