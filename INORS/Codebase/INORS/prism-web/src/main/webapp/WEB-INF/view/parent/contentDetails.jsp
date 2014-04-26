<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<%@ include file="../common/constant.jsp" %>
<div class="margin-bottom-medium" style="min-height: 425px;">
	

	
	<textarea id="taContent" style="display:none;">
		${articleTypeDescription.contentDescription}
	</textarea>

	
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
			<c:choose>
				<c:when test="${menuId == rsc}">
					${menuName}
				</c:when>
				<c:when test="${(menuId == eda) || (menuId == att)}">
					${menuName}: ${studentGradeName}
				</c:when>
			</c:choose>
			${articleTypeDescription.contentName}
			</a></li>
		</ul>
		<div class="tabs-content">
			<div id="contentDescription" class="relative with-padding" style="padding: 20px !important">
				
			</div>
		</div>
	</div>
	
	<c:choose>
		<c:when test="${(menuId == stdAct) || (menuId == stdInd)}">
			<a style="font-weight: bold" href="#nogo" id="backLink" class="button blue-gradient glossy icon-replay-all">
				Go back
			</a>
		</c:when>
		<c:otherwise>
			<c:if test="${studentBioId == 0}">
				<a style="font-weight: bold" href="#nogo" id="backLink" class="button blue-gradient glossy icon-replay-all">
					Go back
				</a>
			</c:if>
		</c:otherwise>
	</c:choose>

</div>