<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	<div id="standardIndicatorHeader" class="wrapped relative white-gradient"
		style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${studentName == '-1'}">
				<h2>Standards</h2>
			</c:when>
			<c:otherwise>
				<h2>Standards for ${studentName}</h2>
			</c:otherwise>
		</c:choose>		
	</div>
	<div id="standardIndicatorMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">
			
			I To read the standards your child is working toward, click on each standard number below. 
            The standards are organized so you can see how they align to the topics on the test. 
            These standards describe in detail what your child is expected to achieve this year based on 
            understanding the skills he or she needs to progress in school. 
            
            To learn more about what standards are and why they are important, choose Why Standards Matter 
            under the Explore Links section of the Parent Network Home page.
	</div>
	<div id="standardIndicatorDeatils" class="wrapped relative white-gradient" style="height: auto;float:left;width:98%;text-align: justify" >
		<div class="simple-div-table wrapped">
			<div class="simple-div-table-col">
				<h4>Standards</h4>
			</div>
			<div class="simple-div-table-col">
				<h4>Indicators</h4>
			</div>
		</div>
		<div>
			&nbsp;
		</div>
		<c:set var="curStandardId" value="0" />
		<c:forEach var="standardIndicatorTO" items="${standardIndicatorDetailsList}">
			<c:if test="${curStandardId != standardIndicatorTO.objectiveId}">
				<div class="simple-div-table wrapped">
					<c:set var="standardId" value="${standardIndicatorTO.objectiveId}" />
					<div class="simple-div-table-col">
						<a href="getArticleDescription.do?articleId=${standardIndicatorTO.objectiveId}&contentType=STD" 
									style="font-weight: bold"
									id="subtestIdLink">
										${standardIndicatorTO.objectiveName}
						</a>
						<p>
							<c:choose>
								<c:when test="${standardIndicatorTO.proficiencyLevel == 1}">
									Proficient
								</c:when>
								<c:when test="${standardIndicatorTO.proficiencyLevel == 0}">
									Below Proficient
								</c:when>
								<c:otherwise>
									Unknown
								</c:otherwise>
							</c:choose>
						</p>
					</div>
					<div class="simple-div-table-col">
						<dl>
							<c:forEach var="IndicatorTO" items="${standardIndicatorDetailsList}">
								<c:if test="${standardId == IndicatorTO.objectiveId}">
									<c:set var="curStandardId" value="${IndicatorTO.objectiveId}" />
									<dd> 
										<a href="getArticleDescription.do?articleId=${IndicatorTO.contentId}&contentType=ACT" 
											style="font-weight: bold"
											id="subtestIdLink">
												${IndicatorTO.contentName}
										</a>
										<p>${IndicatorTO.subHeader}</p>
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
