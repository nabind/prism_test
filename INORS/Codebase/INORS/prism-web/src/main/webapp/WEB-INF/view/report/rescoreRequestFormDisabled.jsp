<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none"><spring:message code="label.success" /></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none"><spring:message code="title.page.error" /></p>
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
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.student" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.parentRescoreDate" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.ELA" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.ELA" /><br /><spring:message code="thead.thead.session.2" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.ELA" /><br /><spring:message code="thead.thead.session.3" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.math" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.math" /><br /><spring:message code="thead.thead.session.1" /></th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.science" /></th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.science" /><br /><spring:message code="thead.thead.session.4" /></th>
						    </c:when>
						    <c:when test="${grade =='10003' || grade =='10005'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.socialStudies" /></th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.socialStudies" /><br /><spring:message code="thead.thead.session.4" /></th>
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
						    			<input type="text" disabled="disabled"
						    				class="rescore-date-dnp" 
							    			value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" disabled="disabled"
						    				class="rescore-date-dnp"
						    				value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:choose>
							    		<c:when test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
							    			<span class="performance-level-dnp" 
							    				subtestId="${rescoreSubtestTO.subtestId}"
							    				studentBioId="${rescoreRequestStudentTO.studentBioId}"
												>
													${rescoreSubtestTO.performanceLevel}
											</span>
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
						    			<div class="item-div-active">
							    			<!-- item-div start -->
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}" style="display: none;">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel != 'UND'}">
							    				<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
							    					<div class="item-div-normal-${rescoreRequestStudentTO.studentBioId}">
							    						<c:choose>
								    						<c:when test="${rescoreItemTO.requestedDate == '-1'}">
								    							<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
									    					</c:when>
									    					<c:otherwise>
								    							<c:choose>
								    								<c:when test="${rescoreItemTO.isRequested=='N'}">
									    								<span class="item-link-dnp align-row">
																				<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																		</span>
									    							</c:when>
										    						<c:when test="${rescoreItemTO.isRequested=='Y'}">
										    							<span class="item-link-dnp align-row">
																				<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																		</span>		
									    							</c:when>
								    							</c:choose>		
								    						</c:otherwise>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-act-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<c:choose>
							    							<c:when test="${rescoreItemTO.isRequested=='N'}">
								    							<span class="item-link-dnp align-row">
																		<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																</span>
								    						</c:when>
									    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
								    							<span class="item-link-dnp align-row">
																		<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																</span>		
								    						</c:when>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
							    					</div>
							    				</c:forEach>
							    			</c:if>
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
				<spring:message code="p.rescoreRequestFormDisabled.1" /><br />
				<select disabled="disabled" id="selectStudentRRF" name="" class="select navy-gradient expandable-list float-left disabled" style="width: 250px;">
					<c:forEach var="rescoreStudentTO" items="${notDnpStudents}">
						<option value="${rescoreStudentTO.studentBioId}">${rescoreStudentTO.studentFullName}</option>
					</c:forEach>
				</select>
				<c:choose>
					<c:when test="${not empty notDnpStudents}">
						<span class="button blue-gradient glossy float-left margin-left disabled">Add </span>
					</c:when>
					<c:otherwise>
						<span class="button blue-gradient glossy float-left margin-left disabled">Add </span>
					</c:otherwise>
				</c:choose>
			</p>
		</div>

		<div id="sorting-advanced_wrapper_" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF_2">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.student" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.parentRescoreDate" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.ELA" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.ELA" /><br /><spring:message code="thead.thead.session.2" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.ELA" /><br /><spring:message code="thead.thead.session.3" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.math" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.math" /><br /><spring:message code="thead.thead.session.1" /></th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.science" /></th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.science" /><br /><spring:message code="thead.thead.session.4" /></th>
						    </c:when>
						    <c:when test="${grade =='10003' || grade =='10005'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="thead.socialStudies" /></th>
								<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"><spring:message code="thead.socialStudies" /><br /><spring:message code="thead.thead.session.4" /></th>
						    </c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF_2">
					<c:forEach var="rescoreRequestStudentTO" items="${notDnpStudentList}">
						<tr id="row_${rescoreRequestStudentTO.studentBioId}">
						    <td class="vertical-center">${rescoreRequestStudentTO.studentFullName}</td>
						    <td class="vertical-center">
						    	<c:choose>
						    		<c:when test="${rescoreRequestStudentTO.requestedDate =='-1'}">
						    			<input type="text" disabled="disabled"
						    				class="rescore-date" 
							    			value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" disabled="disabled"
						    				class="rescore-date" 
						    				value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:choose>
							    		<c:when test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
							    			<span class="performance-level">
													${rescoreSubtestTO.performanceLevel}
											</span>
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
						    			<div class="item-div-active">
							    			<!-- item-div start -->
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='Pass' || rescoreSubtestTO.performanceLevel=='Pass+'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}" style="display: none;">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
							    				<div class="item-div-${rescoreSubtestTO.subtestId}">
							    			</c:if>
							    			<c:if test="${rescoreSubtestTO.performanceLevel != 'UND'}">
							    				<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
							    					<div class="item-div-normal-${rescoreRequestStudentTO.studentBioId}">
							    						<c:choose>
								    						<c:when test="${rescoreItemTO.requestedDate == '-1'}">
								    							<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
									    					</c:when>
									    					<c:otherwise>
								    							<c:choose>
								    								<c:when test="${rescoreItemTO.isRequested=='N'}">
									    								<span class="item-link align-row">
																				<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																		</span>
									    							</c:when>
										    						<c:when test="${rescoreItemTO.isRequested=='Y'}">
										    							<span class="item-link align-row">
																				<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																		</span>		
									    							</c:when>
								    							</c:choose>		
								    						</c:otherwise>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-act-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<c:choose>
							    							<c:when test="${rescoreItemTO.isRequested=='N'}">
								    							<span class="item-link align-row">
																		<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																</span>
								    						</c:when>
									    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
								    							<span class="item-link align-row">
																		<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																</span>		
								    						</c:when>
								    					</c:choose>
							    					</div>
							    					<div class="item-div-inact-${rescoreRequestStudentTO.studentBioId}" style="display: none;">
							    						<small class="item-tag tag align-row grey-bg">${rescoreItemTO.itemNumber}</small>
							    					</div>
							    				</c:forEach>
							    			</c:if>
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
	<input type="hidden" id="q_dataloadMessage" value="Y" />
</div>
