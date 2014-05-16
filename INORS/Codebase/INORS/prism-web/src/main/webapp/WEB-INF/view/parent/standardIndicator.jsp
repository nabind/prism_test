<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium" style="min-height:800px">
	<div id="standardIndicatorHeader" class="relative thin" style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${studentName == '-1'}">
				<h1>Standards</h1>
			</c:when>
			<c:otherwise>
				<h1>Standards for ${studentName}</h1>
			</c:otherwise>
		</c:choose>		
	</div>
	
	
	
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
				Standards
			</a></li>
		</ul>
		<div class="tabs-content">
			<div class="relative with-padding" style="padding: 20px !important">
				
			
	
				<div id="standardIndicatorMessage" class="relative"
						style="height: auto; text-align: justify; margin-bottom: 10px">
						
						To read the standards your child is working toward, click on each standard number below. 
						The standards are organized so you can see how they align to the topics on the test. 
						These standards describe in detail what your child is expected to achieve this year based on 
						understanding the skills he or she needs to progress in school. 
						
						To learn more about what standards are and why they are important, choose Why Standards Matter 
						under the Explore Links section of the Parent Network Home page.
				</div>
				
				<div class="columns">
					<div class="new-row three-columns" style="margin-bottom: 0px;"><h4 class="blue underline">Standards</h4></div>
					<div class="nine-columns" style="margin-bottom: 0px;"><h4 class="blue underline">Indicators</h4></div>
				</div>
				
				<div id="standardActivityDeatils" class="">
					<c:set var="curStandardId" value="0" />
					<c:set var="count" value="0" scope="page" />
					<c:forEach var="standardIndicatorTO" items="${standardIndicatorDetailsList}">
						<c:if test="${curStandardId != standardIndicatorTO.objectiveId}">
							<c:set var="count" value="${count + 1}" scope="page"/>
							<div class="columns">
								<c:set var="standardId" value="${standardIndicatorTO.objectiveId}" />
								<div class="new-row three-columns">
									<a class="articledata" href="#nogo" 
										articleId="${standardIndicatorTO.objContentId}" 
										contentType= '<spring:message code="val.contentType.std"/>'  
										id="subtestIdLink" count="Standard ${count} -">
											Standard ${count} - ${standardIndicatorTO.objectiveName}
									</a>
									
									<c:if test="${studentName != '-1'}">
										<br>
										<c:choose>
											<c:when test="${standardIndicatorTO.proficiencyLevel == '+'}">
												<span class="prof-first2 unknown"><span class="proficient prof-last2"><span class="prof-text blue">Proficient</span></span></span>
											</c:when>
											<c:when test="${standardIndicatorTO.proficiencyLevel == '-'}">
												<span class="prof-first below-proficient"><span class="unknown prof-last"><span class="prof-text red">Below Proficient</span></span></span>
											</c:when>
											<c:otherwise>
												
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
							
							
							
								<div class="nine-columns">
									<ul class="bullet-list">
									<c:forEach var="IndicatorTO" items="${standardIndicatorDetailsList}">
										<c:if test="${standardId == IndicatorTO.objectiveId}">
											<c:set var="curStandardId" value="${IndicatorTO.objectiveId}" />
											<li> 
												<a class="articledata" href="#nogo" 
													articleId="${IndicatorTO.contentId}" 
													contentType= '<spring:message code="val.contentType.ind"/>' 
													id="subtestIdLink">
														${IndicatorTO.contentName}
												</a>
												<p>${IndicatorTO.subHeader}</p>
											</li>
										</c:if>
									</c:forEach>
									</ul>
								</div>
							</div>
							<hr/>
						</c:if>
						
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	
	
</div>
