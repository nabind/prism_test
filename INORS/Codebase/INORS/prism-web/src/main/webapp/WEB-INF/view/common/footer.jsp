<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:theme code="theme.name" var="themeName"/>
<footer class="margin-top main-footer">
	<div class="align-center" style="color: white;font-size: 11px; padding-left: 120px;"><spring:message code="footer.copyright"/> </div>
	<c:choose>
		<c:when test="${themeName == 'acsi'}">
			<div class="align-center" style="color: white;font-size: 11px;"> <spring:message code="footer.helpdesk"/> </div>
		</c:when>
		<c:otherwise>
			<div class="align-center" style="color: white;font-size: 11px;"> <spring:message code="footer.helpdesk.parent"/> </div>
		</c:otherwise>
	</c:choose>
</footer>