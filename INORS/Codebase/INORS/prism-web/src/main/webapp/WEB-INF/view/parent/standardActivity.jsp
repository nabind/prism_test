<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium" style="min-height:800px">
	
	<div id="standardActivityHeader" class="relative thin" style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${studentName == '-1'}">
				<h1>Skill-Building Activities</h1>
			</c:when>
			<c:otherwise>
				<h1>Skill-Building Activities for ${studentName}</h1>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
				Skill-Building Activities
			</a></li>
		</ul>
		<div class="tabs-content">
			<div class="relative with-padding" style="padding: 20px !important">
				
			
	
				<div id="standardActivityMessage" class="relative"
						style="height: auto; text-align: justify">
						In this section, you can select from a wide variety of Grade ${studentGradeName} Skill-Building Activities. 
						These activities give you the opportunity to help your child improve his or her skills in a fun and engaging way. 
						Such support can significantly increase your child's appreciation for learning and his or her confidence when tackling new challenges. 
						The activities are organized in easy-to-follow steps that utilize everyday materials likely available in your home.			
				</div>
				
				<div class="columns">
					<div class="new-row three-columns" style="margin-bottom: 0px;"><h4 class="blue underline">Standards</h4></div>
					<div class="nine-columns" style="margin-bottom: 0px;"><h4 class="blue underline">Activities</h4></div>
				</div>
				
				<div id="standardActivityDeatils" class="">
					<c:set var="curStandardId" value="0" />
					<c:set var="count" value="0" scope="page" />
					<c:forEach var="standardActivityTO" items="${standardActivityDetailsList}">
						<c:if test="${curStandardId != standardActivityTO.objectiveId}">
							<c:set var="count" value="${count + 1}" scope="page"/>
							<div class="columns">
								<c:set var="standardId" value="${standardActivityTO.objectiveId}" />
								<div class="new-row three-columns">
									<a class="articledata" href="#nogo" 
										articleId="${standardActivityTO.objContentId}" 
										contentType= '<spring:message code="val.contentType.std"/>' 
										id="subtestIdLink" count="Standard ${count} -">
											Standard ${count} - ${standardActivityTO.objectiveName}
									</a>
									<c:if test="${studentName != '-1'}">
										<br>
										<c:choose>
											<c:when test="${standardActivityTO.proficiencyLevel == '+'}">
												<span class="prof-first2 unknown"><span class="proficient prof-last2"><span class="prof-text blue">Proficient</span></span></span>
											</c:when>
											<c:when test="${standardActivityTO.proficiencyLevel == '-'}">
												<span class="prof-first below-proficient"><span class="unknown prof-last"><span class="prof-text red">Below Proficient</span></span></span>
											</c:when>
											<c:otherwise>
												
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
							
							
							
								<div class="nine-columns">
									<ul class="bullet-list">
									<c:forEach var="activityTO" items="${standardActivityDetailsList}">
										<c:if test="${standardId == activityTO.objectiveId}">
											<c:set var="curStandardId" value="${activityTO.objectiveId}" />
											<li> 
												<a class="articledata" href="#nogo" 
													articleId="${activityTO.contentId}" 
													contentType= '<spring:message code="val.contentType.act"/>'   
													id="subtestIdLink">
														${activityTO.contentName}
												</a>
											
												<p>${activityTO.subHeader}</p>
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
