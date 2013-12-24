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
	<div id="standardActivityDeatils" class="wrapped relative white-gradient" style="height: auto; text-align: justify" >
		<div style="float: left; height:auto; clear: both;">
			<div style="float: left; height: auto; width: 50%">
				Standards
			</div>
			<div style="float: left; height: auto; width: 50%">
				Activities
			</div>
		</div>
		<c:forEach var="standardActivityTO" items="${standardActivityDetailsList}">
			<c:set var="standardId" value="${standardActivityTO.objectiveId}" />
			<div style="float: left; height:auto; clear: both;">
				<div style="float: left; height: auto; width: 50%">
					<a href="getArticleDescription.do?articleId=${standardActivityTO.objectiveId}&contentType='STD'" 
								style="color: #fff; font-weight: bold"
								id="subtestIdLink">
									${standardActivityTO.objectiveName}
					</a>
					<p>${standardActivityTO.proficiencyLevel}</p>
				</div>
				<div style="float: left; height: auto; width: 50%">
					<dl>
						<c:forEach var="activityTO" items="${standardActivityDetailsList}">
							<c:if test="${standardId == activityTO.objectiveId}">
								<dd> 
									<a href="getArticleDescription.do?articleId=${activityTO.contentId}&contentType='ACT'" 
										style="color: #fff; font-weight: bold"
										id="subtestIdLink">
											${activityTO.contentName}
									</a>
									<p>${activityTO.subHeader}</p>
								</dd>
							</c:if>
						</c:forEach>
					</dl>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
