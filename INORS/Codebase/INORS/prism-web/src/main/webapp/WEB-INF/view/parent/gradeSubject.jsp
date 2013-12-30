<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	<div id="gradeSubjectHeader" class="wrapped relative white-gradient"
		style="height: auto; text-align: justify">
		<c:choose>
			<c:when test="${(menuId == 1) || (menuId == 2) || (menuId == 3) }">
				<h2>${menuName}: Choose a grade and subject</h2>
			</c:when>
			<c:otherwise>
				<h2>${menuName}: Choose a grade</h2>
			</c:otherwise>
		</c:choose>		
	</div>
	<div id="gradeSubjectMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">
			<c:choose>
				<c:when test="${menuId == 1}">
					Select one of the grade/subject combinations below to find additional Skill-Building Activities.
				</c:when>
				<c:when test="${menuId == 2}">
					Select one of the grade/subject combinations below to learn about the State Standards.
				</c:when>
				<c:when test="${menuId == 3}">
					Select one of the grade/subject combinations below to find resources to help your child.
				</c:when>
				<c:when test="${menuId == 4}">
					Select one of the grades below to see additional Everyday Activities you can do with your child.
				</c:when>
				<c:when test="${menuId == 5}">
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
							<c:when test="${(menuId == 1) || (menuId == 2) || (menuId == 3) }">
								<p>${gradeSubtestTO.gradeName}</p>
							</c:when>
							<c:when test="${menuId == 4}">
								<p>
									<a href="getEverydayActivity.do?studentGradeId=${gradeSubtestTO.gradeId}&studentGradeName=${gradeSubtestTO.gradeName}" 
										style="font-weight: bold"
										id="gradeIdLink">
											${gradeSubtestTO.gradeName}
									</a>
								</p>
							</c:when>
							<c:when test="${menuId == 5}">
								<p>
									<a href="getAboutTest.do?studentGradeId=${gradeSubtestTO.gradeId}&studentGradeName=${gradeSubtestTO.gradeName}" 
										style="font-weight: bold"
										id="gradeIdLink">
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
											<c:when test="${menuId == 1}">
												<a href="getStandardActivity.do?subtestId=${subtestTO.subtestId}&studentGradeId=${subtestTO.gradeId}&studentGradeName=${subtestTO.gradeName}" 
													style="font-weight: bold"
													id="subtestIdLink">
														${subtestTO.subtestName}
												</a>
											</c:when>
											<c:when test="${menuId == 2}">
												<a href="getStandardIndicator.do?subtestId=${subtestTO.subtestId}&studentGradeId=${subtestTO.gradeId}&studentGradeName=${subtestTO.gradeName}" 
													style="font-weight: bold"
													id="subtestIdLink">
														${subtestTO.subtestName}
												</a>
											</c:when>
											<c:when test="${menuId == 3}">
												<a href="getResources.do?subtestId=${subtestTO.subtestId}&studentGradeId=${subtestTO.gradeId}&studentGradeName=${subtestTO.gradeName}" 
													style="font-weight: bold"
													id="subtestIdLink">
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
