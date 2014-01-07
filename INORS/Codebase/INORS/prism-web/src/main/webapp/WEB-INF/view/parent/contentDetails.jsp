<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<c:set var="stdAct" value='1' />
<c:set var="stdInd" value='2' />
<c:set var="rsc" value='3' />
<c:set var="eda" value='4' />
<c:set var="att" value='5' />
<div class="margin-bottom-medium" style="min-height: 425px;">
	
	<div id="contentDetailsHeader" class="relative thin"
		style="height: auto; text-align: justify; padding: 0 0 22px">	
		<c:choose>
			<c:when test="${menuId == rsc}">
				<h1>${menuName}</h1>
			</c:when>
			<c:when test="${(menuId == eda) || (menuId == att)}">
				<h1>${menuName}: ${studentGradeName}</h1>
			</c:when>
		</c:choose>
		<h1>${articleTypeDescription.contentName}</h1>		
	</div>
	
	<textarea id="taContent" style="display:none;">
		${articleTypeDescription.contentDescription}
	</textarea>
	<div id="contentDescription" class="relative"
		style="height: auto; text-align: justify">			
	</div>
	<a style="font-weight: bold" href="#nogo" id="backLink">
		Go back
	</a>
</div>


