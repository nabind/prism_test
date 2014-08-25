<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:theme code="theme.name" var="themeName"/>
<footer class="margin-top main-footer">
	<c:choose>
		<c:when test="${themeName == 'inors'}">
			<div class="align-center custom-p color-white" style="font-size: 11px;">
				${messageMapSession.teacherFooterMessage} 
			</div>
		</c:when>
		<c:otherwise>
			<div class="align-center custom-p color-white" style="font-size: 11px;"> 
				${messageMapSession.parentFooterMessage}
			</div>
		</c:otherwise>
	</c:choose>
</footer>