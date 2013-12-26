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
			<h3>Standards</h3>
			
            To read the standards your child is working toward, click on each standard number below. 
            The standards are organized so you can see how they align to the topics on the test. 
            These standards describe in detail what your child is expected to achieve this year based on 
            understanding the skills he or she needs to progress in school. 
            
            To learn more about what standards are and why they are important, choose Why Standards Matter 
            under the Explore Links section of the Parent Network Home page.

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
