<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="secc" uri="http://www.springframework.org/security/tags" %>
<style>
.itemBox {
	margin-bottom: 4px;
  	width: 125px;
  	font-weight: bold;
  	font-size: 12px;
}
.itemBoxGrey {
	margin-left: 2px;
}
</style>
<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<secc:authorize ifAnyGranted="ROLE_PARENT"><h1>Rescore Request Form for ${studentFullName}</h1></secc:authorize>
	<form:form method="POST" id="rescoreRequestForm2015" modelAttribute="rescoreRequestForm2015">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none"><spring:message code="label.success" /></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none"><spring:message code="title.page.error" /></p>
		<input type="hidden" value="/public/INORS/Report/INORS_2015/Rescore_Request_Form_files" id="reportUrl" name="reportUrl">
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
		<secc:authorize ifNotGranted="ROLE_PARENT">
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'RL'}"><%-- Report Legend --%>
						<dl class="download-instructions accordion same-height">
							<dt class="closed accordion-header"><span class="icon-plus-round tracked"></span><span class="icon-minus-round tracked" style="display: none;"></span>
								<b>Instructions</b>
								<dd style="height: auto; display: none;" class="accordion-body with-padding">		
									${ reportMessage.message }
								</dd>
							</dt>
						</dl>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		</secc:authorize>
		<secc:authorize ifAnyGranted="ROLE_PARENT">
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType == 'RP'}"><%-- Report Purpose --%>
						<dl class="download-instructions accordion same-height">
							<dt class="closed accordion-header">
								<b>Par Instructions</b>
								<dd style="height: auto; display: none;" class="accordion-body with-padding">		
									${ reportMessage.message }
								</dd>
							</dt>
						</dl>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		</secc:authorize>
		
		<div class="columns" style="margin-top:20px">
			<div class="new-row six-columns with-small-padding vertical-center">
				<b><spring:message code="label.student" /> <span style="padding-left: 5px; " class="tag orange-bg with-small-padding">${studentFullName}</span></b>
			</div>
			<div class="new-row four-columns with-small-padding vertical-center">
				<b><spring:message code="label.parentRescoreDate" /></b>
				<c:choose>
		    		<c:when test="${requestedDate =='-1'}">
			    		<span style="padding-left: 5px; " class="tag orange-bg with-small-padding">NA</span>
		    		</c:when>
		    		<c:otherwise>
			    		<span style="padding-left: 5px; " class="tag orange-bg with-small-padding">${requestedDate}</span>
		    		</c:otherwise>
		    	</c:choose>	
			</div>
			<secc:authorize ifNotGranted="ROLE_PARENT">
			<div class="six-columns with-small-padding vertical-center">
				<a href="#nogo" class="button" id="" >
				<span class="button-icon icon-download blue-gradient report-btn"></span>
				<spring:message code="button.download.ip" />
			</a>
			</div>
			</secc:authorize>
		</div>
		
		<div id="" class="" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on" id="studentTableRRF2015">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.3" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.4b" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.5" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.ELA" /><br /><spring:message code="thead.session.6b" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.math" /><br /><spring:message code="thead.session.1" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">
							<spring:message code="thead.math" /><br /><spring:message code="thead.session.2" /> <br /><small><spring:message code="thead.item.abb" /></small>
						</th>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">
						    		<spring:message code="thead.science" /><br /><spring:message code="thead.session.7" /><br /><small><spring:message code="thead.item.abb" /></small>
						    	</th>
						    </c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="">
					<tr>
						<c:forEach begin="3" end="6" var="innerSessionId"> 
							<c:set var="sessionId" value="${innerSessionId}" />
							<td class=""> 
						    	<c:forEach var="rescoreSessionTO" items="${rescoreElaTO.rescoreSessionTOList}">
									<c:if test="${fn:contains(rescoreSessionTO.sessionId, sessionId)}"> 
										<%@ include file="rescoreRequestForm2015ItemDisabled.jsp" %>
									</c:if>
						    	</c:forEach>
				    		</td>
						</c:forEach>
						<c:forEach begin="1" end="2" var="innerSessionId">
							<c:set var="sessionId" value="${innerSessionId}" />
							<td class="">
						    	<c:forEach var="rescoreSessionTO" items="${rescoreMathTO.rescoreSessionTOList}">
						    		<c:if test="${fn:contains(rescoreSessionTO.sessionId, sessionId)}">
										<%@ include file="rescoreRequestForm2015ItemDisabled.jsp" %>
									</c:if> 
						    	</c:forEach>
				    		</td>
						</c:forEach>
						<c:choose>
							<c:when test="${grade =='10002' || grade =='10004'}">
						    	<td class="">
						    		<c:forEach var="rescoreSessionTO" items="${rescoreScienceTO.rescoreSessionTOList}">
						    			<c:if test="${fn:contains(rescoreSessionTO.sessionId, sessionId)}">
											<%@ include file="rescoreRequestForm2015ItemDisabled.jsp" %>
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
