<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<spring:theme code="theme.name" var="themeName"/>
<footer class="margin-top main-footer">
	<div class="align-center custom-p color-white" style="font-size: 11px;">
		<c:choose>
			<c:when test="${fn:contains(themeName, 'parent')}" >
				${messageMapSession.parentFooterMessage}
			</c:when>
			<c:otherwise>
				${messageMapSession.teacherFooterMessage} 
			</c:otherwise>
		</c:choose>
	</div>
</footer>