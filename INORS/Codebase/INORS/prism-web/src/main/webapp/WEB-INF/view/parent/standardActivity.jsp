<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	<div id="standardActivityHeader" class="wrapped relative white-gradient"
		style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${studentName == '-1'}">
				<h2>Skill-Building Activities</h2>
			</c:when>
			<c:otherwise>
				<h2>Skill-Building Activities for ${studentName}</h2>
			</c:otherwise>
		</c:choose>		
	</div>
	<div id="standardActivityMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">
			In this section, you can select from a wide variety of ${studentGradeName} Skill-Building Activities. 
			These activities give you the opportunity to help your child improve his or her skills in a fun and engaging way. 
			Such support can significantly increase your child's appreciation for learning and his or her confidence when tackling new challenges. 
			The activities are organized in easy-to-follow steps that utilize everyday materials likely available in your home.			
	</div>
	<div id="standardActivityDeatils" class="wrapped relative white-gradient" style="height: auto;float:left;width:98%;text-align: justify" >
		<div class="simple-div-table wrapped">
			<div class="simple-div-table-col">
				<h4>Standards</h4>
			</div>
			<div class="simple-div-table-col">
				<h4>Activities</h4>
			</div>
		</div>
		<div>
			&nbsp;
		</div>
		<c:set var="curStandardId" value="0" />
		<c:forEach var="standardActivityTO" items="${standardActivityDetailsList}">
			<c:if test="${curStandardId != standardActivityTO.objectiveId}">
				<div class="simple-div-table wrapped">
					<c:set var="standardId" value="${standardActivityTO.objectiveId}" />
					<div class="simple-div-table-col">
						<a href="getArticleDescription.do?articleId=${standardActivityTO.objectiveId}&contentType='STD'" 
									style="font-weight: bold"
									id="subtestIdLink">
										${standardActivityTO.objectiveName}
						</a>
						<p>
							<c:choose>
								<c:when test="${standardActivityTO.proficiencyLevel == 1}">
									<p class="proficient"></p>
									<p>Proficient</p>
								</c:when>
								<c:when test="${standardActivityTO.proficiencyLevel == 0}">
									<p class="below-proficient"></p>
									<p>Below Proficient</p>
								</c:when>
							</c:choose>
						</p>
					</div>
					<div class="simple-div-table-col">
						<dl>
							<c:forEach var="activityTO" items="${standardActivityDetailsList}">
								<c:if test="${standardId == activityTO.objectiveId}">
									<c:set var="curStandardId" value="${activityTO.objectiveId}" />
									<dd> 
										<a href="getArticleDescription.do?articleId=${activityTO.contentId}&contentType='ACT'" 
											style="font-weight: bold"
											id="subtestIdLink">
												${activityTO.contentName}
										</a>
										<p>${activityTO.subHeader}</p>
									</dd>
									<br>
								</c:if>
							</c:forEach>
						</dl>
					</div>
				</div>
				<div>
					&nbsp;
				</div>
			</c:if>
		</c:forEach>
	</div>
</div>
