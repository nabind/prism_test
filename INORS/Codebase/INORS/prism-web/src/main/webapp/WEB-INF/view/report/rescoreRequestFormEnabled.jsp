<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none">Success</p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none">Error</p>
		<input type="hidden" value="/public/INORS/Report/Rescore_Request_Form_files" id="reportUrl" name="reportUrl">
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
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math<br />Session 1</th>
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
						    				class="rescore-date-dnp"
						    				studentBioId="${rescoreRequestStudentTO.studentBioId}" 
							    			id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
							    			value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" 
						    				class="rescore-date-dnp"
											studentBioId="${rescoreRequestStudentTO.studentBioId}" 
						    				id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" 
						    				value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:choose>
							    		<c:when test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
							    			<a class="performance-level-dnp" 
							    				subtestId="${rescoreSubtestTO.subtestId}"
							    				studentBioId="${rescoreRequestStudentTO.studentBioId}"
												href="#nogo">
													${rescoreSubtestTO.performanceLevel}
											</a>
							    		</c:when>
							    		<c:otherwise>
											<c:choose>
									    		<c:when test="${rescoreSubtestTO.performanceLevel==''}">
									    			UND
									    		</c:when>
									    		<c:otherwise>
									    			${rescoreSubtestTO.performanceLevel}
									    		</c:otherwise>
							    			</c:choose>
							    		</c:otherwise>
						    		</c:choose>
						    	</td>
						    	
						    	<c:forEach var="rescoreSessionTO" items="${rescoreSubtestTO.rescoreSessionTOList}">
						    		<td class="vertical-center">
										<!-- item-div start -->
										<c:if test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
											<div class="item-div-${rescoreRequestStudentTO.studentBioId}-${rescoreSubtestTO.subtestId}" style="display: none;">
										</c:if>
										<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
											<div class="item-div-${rescoreRequestStudentTO.studentBioId}-${rescoreSubtestTO.subtestId}">
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
																	<a class="item-link-dnp align-row" 
																		action="submitRescoreRequest" 
																		itemNumber="${rescoreItemTO.itemNumber}"
																		subtestId="${rescoreSubtestTO.subtestId}"
																		sessionId="${rescoreSessionTO.sessionId}"
																		moduleId="${rescoreSessionTO.moduleId}"
																		studentBioId="${rescoreItemTO.studentBioId}"
																		id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																		href="#nogo">
																			<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																	</a>
																</c:when>
																<c:when test="${rescoreItemTO.isRequested=='Y'}">
																	<a class="item-link-dnp align-row" 
																		action="submitRescoreRequest" 
																		itemNumber="${rescoreItemTO.itemNumber}"
																		subtestId="${rescoreSubtestTO.subtestId}"
																		sessionId="${rescoreSessionTO.sessionId}"
																		moduleId="${rescoreSessionTO.moduleId}"
																		studentBioId = "${rescoreItemTO.studentBioId}"
																		id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																		href="#nogo">
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
															<a class="item-link-dnp align-row" 
																action="submitRescoreRequest" 
																itemNumber="${rescoreItemTO.itemNumber}"
																subtestId="${rescoreSubtestTO.subtestId}"
																sessionId="${rescoreSessionTO.sessionId}"
																moduleId="${rescoreSessionTO.moduleId}"
																studentBioId = "${rescoreItemTO.studentBioId}"
																id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																href="#nogo">
																	<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
															</a>
														</c:when>
														<c:when test="${rescoreItemTO.isRequested=='Y'}">
															<a class="item-link-dnp align-row" 
																action="submitRescoreRequest" 
																itemNumber="${rescoreItemTO.itemNumber}"
																subtestId="${rescoreSubtestTO.subtestId}"
																sessionId="${rescoreSessionTO.sessionId}"
																moduleId="${rescoreSessionTO.moduleId}"
																studentBioId = "${rescoreItemTO.studentBioId}"
																id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																href="#nogo">
																	<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
															</a>		
														</c:when>
													</c:choose>
												</div>
												<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
													<c:if test="${rescoreItemTO.itemNumber != 0}">
														<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
													</c:if>
												</div>
											</c:forEach>
										</div>
										<!-- item-div end -->
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
						<a class="button blue-gradient glossy float-left margin-left addStudent" id="addStudent" href="#nogo">Add </a>
					</c:when>
					<c:otherwise>
						<a class="button blue-gradient glossy float-left margin-left disabled" id="addStudent" href="#nogo">Add </a>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
		<div id="sorting-advanced_wrapper_" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF_2">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 75px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Parent-Rescore Date<br />(MM/DD/YYYY)</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">ELA<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math<br />Session 1</th>
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
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF_2">
					<c:forEach var="rescoreRequestStudentTO" items="${notDnpStudentList}">
						<tr id="row_${rescoreRequestStudentTO.studentBioId}">
							<td class="vertical-center">
								<a class="button blue-gradient glossy float-left margin-left remove-student"
									studentBioId="${rescoreRequestStudentTO.studentBioId}" 
									studentFullName="${rescoreRequestStudentTO.studentFullName}" 
									href="#nogo">Remove</a>
							</td>
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
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
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
					    				<!-- item-div start -->
						    			<c:if test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
						    				<div class="item-div-${rescoreRequestStudentTO.studentBioId}-${rescoreSubtestTO.subtestId}" style="display: none;">
						    			</c:if>
						    			<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
						    				<div class="item-div-${rescoreRequestStudentTO.studentBioId}-${rescoreSubtestTO.subtestId}">
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
																		itemNumber="${rescoreItemTO.itemNumber}"
									    								subtestId="${rescoreSubtestTO.subtestId}"
																		sessionId="${rescoreSessionTO.sessionId}"
																		moduleId="${rescoreSessionTO.moduleId}"
																		studentBioId = "${rescoreItemTO.studentBioId}"
																		id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																		href="#nogo">
																			<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																	</a>
								    							</c:when>
									    						<c:when test="${rescoreItemTO.isRequested=='Y'}">
									    							<a class="item-link align-row" 
									    								action="submitRescoreRequest" 
																		itemNumber="${rescoreItemTO.itemNumber}"
									    								subtestId="${rescoreSubtestTO.subtestId}"
																		sessionId="${rescoreSessionTO.sessionId}"
																		moduleId="${rescoreSessionTO.moduleId}"
																		studentBioId = "${rescoreItemTO.studentBioId}"
																		id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																		href="#nogo">
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
																itemNumber="${rescoreItemTO.itemNumber}"
							    								subtestId="${rescoreSubtestTO.subtestId}"
																sessionId="${rescoreSessionTO.sessionId}"
																moduleId="${rescoreSessionTO.moduleId}"
																studentBioId = "${rescoreItemTO.studentBioId}"
																id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																href="#nogo">
																	<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
															</a>
							    						</c:when>
								    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
							    							<a class="item-link align-row" 
							    								action="submitRescoreRequest" 
																itemNumber="${rescoreItemTO.itemNumber}"
							    								subtestId="${rescoreSubtestTO.subtestId}"
																sessionId="${rescoreSessionTO.sessionId}"
																moduleId="${rescoreSessionTO.moduleId}"
																studentBioId = "${rescoreItemTO.studentBioId}"
																id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.itemNumber}"
																href="#nogo">
																	<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
															</a>		
							    						</c:when>
							    					</c:choose>
						    					</div>
						    					<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
						    						<c:if test="${rescoreItemTO.itemNumber != 0}">
														<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
													</c:if>
						    					</div>
						    				</c:forEach>
							    		</div>
							    		<!-- item-div end -->
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
					<c:if test="${reportMessage.messageType =='RM' && reportMessage.messageName == 'Report Message'}">
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
	<input type="hidden" id="q_grade" value="${grade}" />
	<input type="hidden" id="q_dataloadMessage" value="N" />	
</div>
