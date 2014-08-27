<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<%@ include file="../common/constant.jsp" %>
<div class="margin-bottom-medium">
	<div id="gradeSubjectHeader" class="relative thin" style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${(menuId == stdAct) || (menuId == stdInd) || (menuId == rsc) }">
				<h1>${menuName}: <spring:message code="label.chooseGradeSubject" /></h1>
			</c:when>
			<c:otherwise>
				<h1>${menuName}: <spring:message code="label.chooseGrade" /></h1>
			</c:otherwise>
		</c:choose>		
	</div>
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
				${menuName}
			</a></li>
		</ul>
		<div class="tabs-content">
			<div class="relative with-padding" style="padding: 20px !important">
				<div id="gradeSubjectMessage" class="relative" style="height: auto; text-align: justify;margin-bottom: 10px">
						<c:choose>
							<c:when test="${menuId == stdAct}">
								<spring:message code="p.gradeSubject.1" />
							</c:when>
							<c:when test="${menuId == stdInd}">
								<spring:message code="p.gradeSubject.2" />
							</c:when>
							<c:when test="${menuId == rsc}">
								<spring:message code="p.gradeSubject.3" />
							</c:when>
							<c:when test="${menuId == eda}">
								<spring:message code="p.gradeSubject.4" />
							</c:when>
							<c:when test="${menuId == att}">
								<spring:message code="p.gradeSubject.5" />
							</c:when>
						</c:choose>				
				</div>
				<div class="columns">
					<c:choose>
						<c:when test="${(menuId == stdAct) || (menuId == stdInd) || (menuId == rsc) }">
							<div class="new-row three-columns" style="margin-bottom: 0px;"><h4 class="blue underline"><spring:message code="label.grade" /></h4></div>
							<div class="nine-columns" style="margin-bottom: 0px;"><h4 class="blue underline"><spring:message code="label.subtest" /></h4></div>
						</c:when>
						<c:otherwise>
							<div class="new-row twelve-columns" style="margin-bottom: 0px;"><h4 class="blue underline"><spring:message code="label.grade" /></h4></div>
						</c:otherwise>
					</c:choose>	
				</div>
				<div id="gradeSubjectDeatils" class="">
					<c:set var="curGradeId" value="0" />
					<c:forEach var="gradeSubtestTO" items="${gradeSubtestList}">
						<c:if test="${curGradeId != gradeSubtestTO.gradeId}">
							<div class="columns">
								<c:set var="gradeId" value="${gradeSubtestTO.gradeId}" />
								<div class="new-row three-columns">
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
													custProdId = "${gradeSubtestTO.custProdId}"
													style="font-weight: bold"
													href="#" id="">
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
													custProdId = "${gradeSubtestTO.custProdId}"
													style="font-weight: bold"
													href="#" id="">
														${gradeSubtestTO.gradeName}
												</a>
											</p>
										</c:when>
									</c:choose>
								</div>
								<div class="nine-columns">
									<c:forEach var="subtestTO" items="${gradeSubtestList}">
										<c:if test="${gradeId == subtestTO.gradeId}">
											<c:set var="curGradeId" value="${subtestTO.gradeId}" />
											<dd> 
												<c:choose>
													<c:when test="${menuId == stdAct}">
														<a class="subtest-link" action="getStandardActivity" subtestId="${subtestTO.subtestId}"
															studentName="" studentGradeId="${subtestTO.gradeId}"
															studentBioId="0" studentGradeName="${subtestTO.gradeName}"
															custProdId = "${gradeSubtestTO.custProdId}"
															style="font-weight: bold"
															href="#" id="">
																${subtestTO.subtestName}
														</a>
													</c:when>
													<c:when test="${menuId == stdInd}">
														<a class="subtest-link" action="getStandardIndicator" subtestId="${subtestTO.subtestId}"
															studentName="" studentGradeId="${subtestTO.gradeId}" 
															studentBioId="0" studentGradeName="${subtestTO.gradeName}"
															custProdId = "${gradeSubtestTO.custProdId}"
															style="font-weight: bold"
															href="#" id="">
																${subtestTO.subtestName}
														</a>
													</c:when>
													<c:when test="${menuId == rsc}">
														<a class="subtest-link" action="getArticleDescription" subtestId="${subtestTO.subtestId}"
															studentGradeId="${subtestTO.gradeId}" studentGradeName="${subtestTO.gradeName}" 
															menuId = '<spring:message code="menuId.content.rsc"/>'  
															menuName = '<spring:message code="menuName.content.rsc"/>'
															contentType = '<spring:message code="val.contentType.rsc"/>' 
															custProdId = "${gradeSubtestTO.custProdId}"
															style="font-weight: bold"
															href="#" id="">
																${subtestTO.subtestName}
														</a>
													</c:when>
												</c:choose>	
											</dd>
											<br>
										</c:if>
									</c:forEach>
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
