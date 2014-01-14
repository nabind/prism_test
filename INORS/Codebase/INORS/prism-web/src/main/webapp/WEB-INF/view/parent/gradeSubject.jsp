<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<%@ include file="../common/constant.jsp" %>
<div class="margin-bottom-medium">
	
	<div id="gradeSubjectHeader" class="relative thin" style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${(menuId == stdAct) || (menuId == stdInd) || (menuId == rsc) }">
				<h2>${menuName}: Choose a grade and subject</h2>
			</c:when>
			<c:otherwise>
				<h2>${menuName}: Choose a grade</h2>
			</c:otherwise>
		</c:choose>		
	</div>
	
	<div id="gradeSubjectMessage" class="relative" style="height: auto; text-align: justify">
			<c:choose>
				<c:when test="${menuId == stdAct}">
					Select one of the grade/subject combinations below to find additional Skill-Building Activities.
				</c:when>
				<c:when test="${menuId == stdInd}">
					Select one of the grade/subject combinations below to learn about the State Standards.
				</c:when>
				<c:when test="${menuId == rsc}">
					Select one of the grade/subject combinations below to find resources to help your child.
				</c:when>
				<c:when test="${menuId == eda}">
					Select one of the grades below to see additional Everyday Activities you can do with your child.
				</c:when>
				<c:when test="${menuId == att}">
					Select one of the grades below to learn About the Tests your child is expected to take.
				</c:when>
			</c:choose>				
	</div>
	<div id="gradeSubjectDeatils" class="wrapped relative white-gradient" style="height: auto;float:left;width:98%;text-align: justify" >
		<c:set var="curGradeId" value="0" />
		<c:forEach var="gradeSubtestTO" items="${gradeSubtestList}">
			<c:if test="${curGradeId != gradeSubtestTO.gradeId}">
				<div class="simple-div-table wrapped">
					<c:set var="gradeId" value="${gradeSubtestTO.gradeId}" />
					<div class="simple-div-table-col">
						<c:choose>
							<c:when test="${(menuId == stdAct) || (menuId == stdInd) || (menuId == rsc) }">
								<p>${gradeSubtestTO.gradeName}</p>
							</c:when>
							<c:when test="${menuId == eda}">
								<p>
									<a class="grade-link" action="getArticleDescription" studentGradeId="${gradeSubtestTO.gradeId}" 
										studentGradeName="${gradeSubtestTO.gradeName}" menuId = '<spring:message code="menuId.content.eda"/>' 
										menuName = '<spring:message code="menuName.content.eda"/>' 
										contentType = '<spring:message code="val.contentType.eda"/>' 
										style="font-weight: bold"
										href="#nogo" id="">
											${gradeSubtestTO.gradeName}
									</a>
								</p>
							</c:when>
							<c:when test="${menuId == att}">
								<p>
									<a class="grade-link" action="getArticleDescription" studentGradeId="${gradeSubtestTO.gradeId}" 
										studentGradeName="${gradeSubtestTO.gradeName}" 
										menuId = '<spring:message code="menuId.content.att"/>'  
										menuName = '<spring:message code="menuName.content.att"/>'
										contentType = '<spring:message code="val.contentType.att"/>' 
										style="font-weight: bold"
										href="#nogo" id="">
											${gradeSubtestTO.gradeName}
									</a>
								</p>
							</c:when>
						</c:choose>
					</div>
					<div class="simple-div-table-col">
						<dl>
							<c:forEach var="subtestTO" items="${gradeSubtestList}">
								<c:if test="${gradeId == subtestTO.gradeId}">
									<c:set var="curGradeId" value="${subtestTO.gradeId}" />
									<dd> 
										<c:choose>
											<c:when test="${menuId == stdAct}">
												<a class="subtest-link" action="getStandardActivity" subtestId="${subtestTO.subtestId}"
													studentName="" studentGradeId="${subtestTO.gradeId}"
													studentBioId="0" studentGradeName="${subtestTO.gradeName}"
													style="font-weight: bold"
													href="#nogo" id="">
														${subtestTO.subtestName}
												</a>
											</c:when>
											<c:when test="${menuId == stdInd}">
												<a class="subtest-link" action="getStandardIndicator" subtestId="${subtestTO.subtestId}"
													studentName="" studentGradeId="${subtestTO.gradeId}" 
													studentBioId="0" studentGradeName="${subtestTO.gradeName}"
													style="font-weight: bold"
													href="#nogo" id="">
														${subtestTO.subtestName}
												</a>
											</c:when>
											<c:when test="${menuId == rsc}">
												<a class="subtest-link" action="getArticleDescription" subtestId="${subtestTO.subtestId}"
													studentGradeId="${subtestTO.gradeId}" studentGradeName="${subtestTO.gradeName}" 
													menuId = '<spring:message code="menuId.content.rsc"/>'  
													menuName = '<spring:message code="menuName.content.rsc"/>'
													contentType = '<spring:message code="val.contentType.rsc"/>' 
													style="font-weight: bold"
													href="#nogo" id="">
														${subtestTO.subtestName}
												</a>
											</c:when>
										</c:choose>	
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
