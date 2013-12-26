<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	<div id="standardIndicatorHeader" class="wrapped relative white-gradient"
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
	<div id="standardIndicatorMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">
			
			Space for Message from Parent Network Site for standards
				
	</div>
	<div id="standardIndicatorDeatils" class="wrapped relative white-gradient" style="height: auto; text-align: justify" >
		<div style="float: left; height:auto; clear: both;">
			<div style="float: left; height: auto; width: 50%">
				Standards
			</div>
			<div style="float: left; height: auto; width: 50%">
				Activities
			</div>
		</div>
		<c:forEach var="standardIndicatorTO" items="${standardIndicatorDetailsList}">
			<c:set var="standardId" value="${standardIndicatorTO.objectiveId}" />
			<div style="float: left; height:auto; clear: both;">
				<div style="float: left; height: auto; width: 50%">
					<a href="getArticleDescription.do?articleId=${standardIndicatorTO.objectiveId}&contentType='STD'" 
								style="color: #fff; font-weight: bold"
								id="subtestIdLink">
									${standardIndicatorTO.objectiveName}
					</a>
					<p>${standardIndicatorTO.proficiencyLevel}</p>
				</div>
				<div style="float: left; height: auto; width: 50%">
					<dl>
						<c:forEach var="IndicatorTO" items="${standardIndicatorDetailsList}">
							<c:if test="${standardId == IndicatorTO.objectiveId}">
								<dd> 
									<a href="getArticleDescription.do?articleId=${IndicatorTO.contentId}&contentType='IND'" 
										style="color: #fff; font-weight: bold"
										id="subtestIdLink">
											${IndicatorTO.contentName}
									</a>
									<p>${IndicatorTO.subHeader}</p>
								</dd>
							</c:if>
						</c:forEach>
					</dl>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
