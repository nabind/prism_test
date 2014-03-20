<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><strong>PDF File Generation has been requested. Click on 'Group Download Files' for Status of request(s).</strong></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl" >
	<dl class="download-instructions accordion same-height">
		<c:if test="${not empty testAdministrationVal}"><dt class="closed accordion-header"><span class="icon-plus-round tracked"></span><span class="icon-minus-round tracked" style="display: none;"></span></c:if>
		<c:if test="${empty testAdministrationVal}"><dt class="open accordion-header"><span class="icon-plus-round tracked" style="display: none;"></span><span class="icon-minus-round tracked"></span></c:if>
		</span> Instructions</dt>
		<c:if test="${not empty testAdministrationVal}"><dd style="height: auto; display: none;" class="accordion-body with-padding"></c:if>
		<c:if test="${empty testAdministrationVal}"><dd style="height: auto;" class="accordion-body with-padding"></c:if>
				${groupDownloadInstructionMessage}
		</dd>
	</dl>
	<c:if test="${not empty reportMessages}">
		<c:forEach var="reportMessage" items="${reportMessages}">
			<c:if test="${reportMessage.displayFlag=='Y'}">
				<c:if test="${reportMessage.messageType=='DM'}">
					<div class="big-message">
						<span class="big-message-icon icon-warning with-text color">Important</span>
						<strong>${ reportMessage.messageName }</strong><br>
						${ reportMessage.message }
					</div>
					<p>&nbsp;</p>
				</c:if>
				<c:if test="${reportMessage.messageType!='DM'}">
					<fieldset class="fieldset">
						<legend class="legend">${ reportMessage.messageName }</legend>
						<p class="inline-label">${ reportMessage.message }</p>
					</fieldset>
				</c:if>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${hideContentFlag=='Y'}">
		<c:if test="${empty reportMessages}">
			<div class="message small-margin-bottom orange-gradient dataload-message">
				<c:if test="${groupFile=='ISR'}">
					ISTEP+ and IMAST Student Report (ISR) are available for the two most recent administrations. IREAD-3 Student Report (ISR) are available for the 2013 and 2014 administrations (Spring and Summer).
				</c:if>
				<c:if test="${groupFile=='IPR'}">
					Image of student responses (IP) to Applied Skills test are available for the two most recent ISTEP+ administrations. (Not available for IMAST or IREAD-3)<br /><br />
				</c:if>
				<c:if test="${groupFile=='BOTH'}">
					Invitation Code Letters (IC) are available for the current ISTEP+ administration only.<br /><br />
					Image of student responses (IP) to Applied Skills test are available for the two most recent ISTEP+ administrations. (Not available for IMAST or IREAD-3)<br /><br />
				</c:if>
				<c:if test="${groupFile=='ICL'}">
					Invitation Code Letters (IC) are available for the current ISTEP+ administration only.<br /><br />
				</c:if>
			</div>
		</c:if>
	</c:if>
	
	<sec:authorize ifAnyGranted="ROLE_CTB">
		<%@ include file="groupDownloadOptions.jsp" %>
	</sec:authorize>
	<sec:authorize ifNotGranted="ROLE_CTB">
		<c:if test="${empty dataloadMessage}">
			<c:if test="${hideContentFlag=='N'}">
				<%@ include file="groupDownloadOptions.jsp" %>
			</c:if>
		</c:if>
	</sec:authorize>
	
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
	<input type="hidden" id="q_klass" value="${klass}" />
	<input type="hidden" id="q_grade" value="${grade}" />
	<input type="hidden" id="q_groupFile" value="${groupFile}" />
	<input type="hidden" id="q_collationHierarchy" value="${collationHierarchy}" />
	<input type="hidden" id="klassOptionsString" />
	<c:forEach var="student" items="${studentList}"><input type="hidden" id="check-status-${student.id}" name="check-status-${student.id}" value="0" /></c:forEach>
</div>
