<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none">Success</p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none">Error</p>
		<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl">
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'DM'}"><%-- Dataload Message --%>
						<div class="big-message">
							<span>${ reportMessage.message }</span>
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'RP'}"><%-- Report Purpose --%>
						<div class="columns accordion with-padding">
							<span>${ reportMessage.message }</span>
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType =='RSCM' && reportMessage.messageName == 'Group Download Instruction'}">
						<div class="columns accordion with-padding">
							<span>${ reportMessage.message }</span>
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<div id="sorting-advanced_wrapper_" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Parent-Rescore Date<br />(MM/DD/YYYY)</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">E/la</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Math<br />Session 1</th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Science</th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Science<br />Session 4</th>
						    </c:when>
						    <c:when test="${grade =='10003' || grade =='10005'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Social Studies</th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Social Studies<br />Session 4</th>
						    </c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF">
					<c:forEach var="rescoreRequestStudentTO" items="${dnpStudentList}">
						<tr>
						    <td class="vertical-center">${rescoreRequestStudentTO.studentFullName}</td>
						    <td class="vertical-center">
						    	<c:choose>
						    		<c:when test="${rescoreRequestStudentTO.requestedDate =='-1'}">
						    			<input type="text" 
						    				class="rescore-date"
						    				studentBioId="${rescoreRequestStudentTO.studentBioId}" 
							    			id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
							    			value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" 
						    				class="rescore-date"
											studentBioId="${rescoreRequestStudentTO.studentBioId}" 
						    				id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
						    				value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='Pass'}">
						    			<a class="performance-level" 
						    				subtestId="${rescoreSubtestTO.subtestId}"
						    				studentBioId="${rescoreRequestStudentTO.studentBioId}"
											href="#nogo">
												${rescoreSubtestTO.performanceLevel}
										</a>
						    		</c:if>
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
						    			${rescoreSubtestTO.performanceLevel}
						    		</c:if>
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='UND'}">
						    			${rescoreSubtestTO.performanceLevel}
						    		</c:if>
						    	</td>
						    	
						    	<c:forEach var="rescoreSessionTO" items="${rescoreSubtestTO.rescoreSessionTOList}">
						    		<td class="vertical-center">
						    			<div class="item-div-active">
							    			<!-- item-div start -->
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='Pass'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}" style="display: none;">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}">
							    			</c:if>
							    				<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
							    					<div class="item-div-normal-${rescoreRequestStudentTO.studentBioId}">
							    						<c:choose>
								    						<c:when test="${rescoreItemTO.requestedDate == '-1'}">
								    							<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
									    					</c:when>
									    					<c:otherwise>
								    							<c:choose>
								    								<c:when test="${rescoreItemTO.isRequested=='N'}">
									    								<a class="item-link align-row" 
										    								action="submitRescoreRequest" 
																			itemsetId="${rescoreItemTO.itemsetId}"
													    					rrfId="${rescoreItemTO.rrfId}" 
													    					userId="${rescoreItemTO.userId}"
													    					userName="${rescoreItemTO.userName}"
													    					studentBioId="${rescoreItemTO.studentBioId}"
																			href="#nogo" 
																			id="item_${rescoreItemTO.itemsetId}">
																				<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																		</a>
									    							</c:when>
										    						<c:when test="${rescoreItemTO.isRequested=='Y'}">
										    							<a class="item-link align-row" 
										    								action="submitRescoreRequest" 
																			itemsetId = "${rescoreItemTO.itemsetId}"
													    					rrfId = "${rescoreItemTO.rrfId}" 
													    					userId = "${rescoreItemTO.userId}"
													    					userName = "${rescoreItemTO.userName}"
													    					studentBioId = "${rescoreItemTO.studentBioId}"
																			href="#nogo" 
																			id="item_${rescoreItemTO.itemsetId}">
																				<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																		</a>		
									    							</c:when>
								    							</c:choose>		
								    						</c:otherwise>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-act-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<c:choose>
							    							<c:when test="${rescoreItemTO.isRequested=='N'}">
								    							<a class="item-link align-row" 
								    								action="submitRescoreRequest" 
																	itemsetId="${rescoreItemTO.itemsetId}"
											    					rrfId="${rescoreItemTO.rrfId}" 
											    					userId="${rescoreItemTO.userId}"
											    					userName="${rescoreItemTO.userName}"
											    					studentBioId="${rescoreItemTO.studentBioId}"
																	href="#nogo" 
																	id="item_${rescoreItemTO.itemsetId}">
																		<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																</a>
								    						</c:when>
									    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
								    							<a class="item-link align-row" 
								    								action="submitRescoreRequest" 
																	itemsetId = "${rescoreItemTO.itemsetId}"
											    					rrfId = "${rescoreItemTO.rrfId}" 
											    					userId = "${rescoreItemTO.userId}"
											    					userName = "${rescoreItemTO.userName}"
											    					studentBioId = "${rescoreItemTO.studentBioId}"
																	href="#nogo" 
																	id="item_${rescoreItemTO.itemsetId}">
																		<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																</a>		
								    						</c:when>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
							    					</div>
							    				</c:forEach>
								    		</div>
								    		<!-- item-div end -->
							    		</div>
						    		</td>
						    	</c:forEach>
						    </c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'FN'}"><%-- Foot Note --%>
						<p><span>${ reportMessage.message }</span></p>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<div class="columns accordion" style="text-align: center; border: none;">
			<a class="button blue-gradient glossy" id="reviewRRF" >Review </a>
		</div>
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'RL'}"><%-- Report Legend --%>
						<div class="columns accordion with-padding">
							<span>${ reportMessage.message }</span>
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<div class="columns accordion with-padding">
			<p class="button-height">
				Students Pass/Pass+/UND  in all content areas:<br />
				<select id="selectStudentRRF" name="" class="select navy-gradient expandable-list float-left" style="width: 250px;">
					<c:forEach var="rescoreStudentTO" items="${notDnpStudents}">
						<option value="${rescoreStudentTO.studentBioId}">${rescoreStudentTO.studentFullName}</option>
					</c:forEach>
				</select>
				<c:choose>
					<c:when test="${not empty notDnpStudents}">
						<a class="button blue-gradient glossy float-left margin-left" id="addStudent" href="#">Add </a>
					</c:when>
					<c:otherwise>
						<a class="button blue-gradient glossy float-left margin-left disabled" id="addStudent" href="#">Add </a>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
		<div id="sorting-advanced_wrapper_" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF_2">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Parent-Rescore Date<br />(MM/DD/YYYY)</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">E/la</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Math<br />Session 1</th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Science</th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Science<br />Session 4</th>
						    </c:when>
						    <c:when test="${grade =='10003' || grade =='10005'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Social Studies</th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Social Studies<br />Session 4</th>
						    </c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF">
					<c:forEach var="rescoreRequestStudentTO" items="${notDnpStudentList}">
						<tr>
						    <td class="vertical-center">${rescoreRequestStudentTO.studentFullName}</td>
						    <td class="vertical-center">
						    	<c:choose>
						    		<c:when test="${rescoreRequestStudentTO.requestedDate =='-1'}">
						    			<input type="text" 
						    				class="rescore-date"
						    				studentBioId="${rescoreRequestStudentTO.studentBioId}" 
							    			id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
							    			value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" 
						    				class="rescore-date"
											studentBioId="${rescoreRequestStudentTO.studentBioId}" 
						    				id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
						    				value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='Pass'}">
						    			<a class="performance-level" 
						    				subtestId="${rescoreSubtestTO.subtestId}"
						    				studentBioId="${rescoreRequestStudentTO.studentBioId}"
											href="#nogo">
												${rescoreSubtestTO.performanceLevel}
										</a>
						    		</c:if>
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
						    			${rescoreSubtestTO.performanceLevel}
						    		</c:if>
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='UND'}">
						    			${rescoreSubtestTO.performanceLevel}
						    		</c:if>
						    	</td>
						    	
						    	<c:forEach var="rescoreSessionTO" items="${rescoreSubtestTO.rescoreSessionTOList}">
						    		<td class="vertical-center">
						    			<div class="item-div-active">
							    			<!-- item-div start -->
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='Pass'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}" style="display: none;">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}">
							    			</c:if>
							    				<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
							    					<div class="item-div-normal-${rescoreRequestStudentTO.studentBioId}">
							    						<c:choose>
								    						<c:when test="${rescoreItemTO.requestedDate == '-1'}">
								    							<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
									    					</c:when>
									    					<c:otherwise>
								    							<c:choose>
								    								<c:when test="${rescoreItemTO.isRequested=='N'}">
									    								<a class="item-link align-row" 
										    								action="submitRescoreRequest" 
																			itemsetId="${rescoreItemTO.itemsetId}"
													    					rrfId="${rescoreItemTO.rrfId}" 
													    					userId="${rescoreItemTO.userId}"
													    					userName="${rescoreItemTO.userName}"
													    					studentBioId="${rescoreItemTO.studentBioId}"
																			href="#nogo" 
																			id="item_${rescoreItemTO.itemsetId}">
																				<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																		</a>
									    							</c:when>
										    						<c:when test="${rescoreItemTO.isRequested=='Y'}">
										    							<a class="item-link align-row" 
										    								action="submitRescoreRequest" 
																			itemsetId = "${rescoreItemTO.itemsetId}"
													    					rrfId = "${rescoreItemTO.rrfId}" 
													    					userId = "${rescoreItemTO.userId}"
													    					userName = "${rescoreItemTO.userName}"
													    					studentBioId = "${rescoreItemTO.studentBioId}"
																			href="#nogo" 
																			id="item_${rescoreItemTO.itemsetId}">
																				<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																		</a>		
									    							</c:when>
								    							</c:choose>		
								    						</c:otherwise>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-act-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<c:choose>
							    							<c:when test="${rescoreItemTO.isRequested=='N'}">
								    							<a class="item-link align-row" 
								    								action="submitRescoreRequest" 
																	itemsetId="${rescoreItemTO.itemsetId}"
											    					rrfId="${rescoreItemTO.rrfId}" 
											    					userId="${rescoreItemTO.userId}"
											    					userName="${rescoreItemTO.userName}"
											    					studentBioId="${rescoreItemTO.studentBioId}"
																	href="#nogo" 
																	id="item_${rescoreItemTO.itemsetId}">
																		<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																</a>
								    						</c:when>
									    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
								    							<a class="item-link align-row" 
								    								action="submitRescoreRequest" 
																	itemsetId = "${rescoreItemTO.itemsetId}"
											    					rrfId = "${rescoreItemTO.rrfId}" 
											    					userId = "${rescoreItemTO.userId}"
											    					userName = "${rescoreItemTO.userName}"
											    					studentBioId = "${rescoreItemTO.studentBioId}"
																	href="#nogo" 
																	id="item_${rescoreItemTO.itemsetId}">
																		<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																</a>		
								    						</c:when>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
							    					</div>
							    				</c:forEach>
								    		</div>
								    		<!-- item-div end -->
							    		</div>
						    		</td>
						    	</c:forEach>
						    </c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType =='RSCM' && reportMessage.messageName == 'More Info'}">
						<p><span>${ reportMessage.message }</span></p>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
	<c:choose>
		<c:when test="${not empty dataloadMessage}"><input type="hidden" id="q_dataloadMessage" value="Y" /></c:when>
		<c:otherwise><input type="hidden" id="q_dataloadMessage" value="N" /></c:otherwise>
	</c:choose>
</div>
