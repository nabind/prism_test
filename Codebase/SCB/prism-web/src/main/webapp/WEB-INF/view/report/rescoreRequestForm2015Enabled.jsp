<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm2015">
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
		<div>
			<div style="float: left; padding-left: 5px; padding-right: 200px;">
				<b><spring:message code="label.student" /> <span style="padding-left: " >${studentFullName}</span></b>
			</div>
			<div style="float: none;">
				<c:choose>
		    		<c:when test="${requestedDate =='-1'}">
			    		<span class="input" style="width: 100px;">
			    			<input type="text" readonly="true"
			    				class="rescore-date-dnp input-unstyled"
			    				studentBioId="${studentBioId}" 
				    			id="rescoreDate_${studentBioId}" 
				    			value="" /> 
				    	</span>
		    		</c:when>
		    		<c:otherwise>
			    		<span class="input" style="width: 100px;">
			    			<input type="text" readonly="true"
			    				class="rescore-date-dnp input-unstyled"
								studentBioId="${studentBioId}" 
			    				id="rescoreDate_${studentBioId}" 
			    				value="${requestedDate}" /> 
			    		</span>
		    		</c:otherwise>
		    	</c:choose>	
			</div>
		</div>
		
		<div id="sorting-advanced_wrapper_" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF2015">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.3" />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.4b" />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.5" />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.6b" />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.math" /><br /><spring:message code="thead.session.1" />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.math" /><br /><spring:message code="thead.session.2" />
						</th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">
						    		<spring:message code="thead.science" /><br /><spring:message code="thead.session.7" />
						    	</th>
						    </c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF2015">
					<tr>
						<c:forEach begin="3" end="6" var="innerSessionId">
							<c:set var="sessionId" value="${innerSessionId}" />
							
							<td class="vertical-center">
						    	<c:forEach var="rescoreSessionTO" items="${rescoreElaTO.rescoreSessionTOList}">
						    			<c:if test="${rescoreSessionTO.sessionId == innerSession}">
											<c:out value="${sessionId}" />
										</c:if>
						    	</c:forEach>
				    		</td>
						</c:forEach>
						<c:forEach begin="1" end="2" var="innerSessionId">
							<c:set var="sessionId" value="${innerSessionId}" />
							<c:out value="${innerSessionId}" />
							<td class="vertical-center">
						    	<c:forEach var="rescoreSessionTO" items="${rescoreMathTO.rescoreSessionTOList}">
						    			<c:if test="${rescoreSessionTO.sessionId == innerSession}">
											<%@ include file="rescoreRequestForm2015Item.jsp" %>
										</c:if>
						    	</c:forEach>
				    		</td>
						</c:forEach>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<td class="vertical-center">
						    		<c:forEach var="rescoreSessionTO" items="${rescoreScienceTO.rescoreSessionTOList}">
						    			<c:if test="${rescoreSessionTO.sessionId == innerSession}">
											<%@ include file="rescoreRequestForm2015Item.jsp" %>
										</c:if>
						    		</c:forEach>
				    			</td>
						    </c:when>
						</c:choose>
				   </tr>
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
	<input type="hidden" id="q_student" value="${studentBioId}" />
	<input type="hidden" id="q_dataloadMessage" value="N" />	
</div>
