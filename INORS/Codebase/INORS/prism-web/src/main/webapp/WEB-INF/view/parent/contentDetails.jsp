<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<%@ include file="../common/constant.jsp" %>
<div class="margin-bottom-medium" style="min-height: 425px;">
	
	<div id="contentDetailsHeader" class="relative thin">	
		<c:choose>
			<c:when test="${menuId == rsc}">
				<h1>${menuName}</h1>
			</c:when>
			<c:when test="${(menuId == eda) || (menuId == att)}">
				<h1>${menuName}: ${studentGradeName}</h1>
			</c:when>
		</c:choose>
		<h2>${articleTypeDescription.contentName}</h2>		
	</div>
	
	<textarea id="taContent" style="display:none;">
		${articleTypeDescription.contentDescription}
	</textarea>
	<div id="contentDescription" class="relative with-padding"
		style="height: auto; text-align: justify">			
	</div>
	<!-- 
	<a style="font-weight: bold" href="#nogo" id="backLink">
		Go back
	</a>
	 -->
</div>


