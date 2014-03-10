<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
	
<div class="margin-bottom-medium" style="min-height:800px">
	<c:if test="${not empty childDataMap}">
		<textarea id="taContent" style="display:none;">
			${childDataMap.studentOverviewMessage}
		</textarea>
		<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<div id="somHeader" class="relative"
			style="height: auto; text-align: justify">	
			<h1>Overview of Action Plan for ${childDataMap.studentName}</h1>		
		</div>
		</hgroup>
		
		<div id="studentOverviewMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">			
		</div>
		<div id="subtestDiv" class="left-column big-message blue-gradient" 
			style="color: #fff; height: auto; width:22%" >
			<dl>
			 <c:if test="${empty childDataMap.studentSubtest}">
			 	<dd style="min-height: 50px;">
			 		<spring:message code="pre.grtload.message"/>
			 	</dd>
			 </c:if>
			<c:if test="${not empty childDataMap.studentSubtest}">
				<c:forEach var="subtestTO"
					items="${childDataMap.studentSubtest}"
					varStatus="loopSubtestTO">
						<dd style="min-height: 50px;">
							 <a class="standard-activity" href="#nogo" subtestId="${subtestTO.value}" studentName="${childDataMap.studentName}" studentGradeId="${childDataMap.studentGradeId}" studentBioId="${childDataMap.studentBioId}" studentGradeName="${childDataMap.studentGradeName}" 
								style="color: #fff; font-weight: bold"
								id="subtestIdLink">
									${subtestTO.name}
							</a>
						</dd>
				</c:forEach>
			 </c:if>	
			</dl>
		</div>
	</c:if>
</div>

