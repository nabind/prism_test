<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:theme code="theme.name" var="themeName"/>
<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload"/></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.groupDownload"/></p>
	<c:choose>
		<c:when test="${fn:contains(themeName, 'usmo')}" >
			<input type="hidden" value="/public/Missouri/Report/Group_Download_MO_files" name="reportUrl" >
		</c:when>
		<c:otherwise>
			<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl" >
		</c:otherwise>
	</c:choose>
	
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
						<%-- <span class="big-message-icon icon-warning with-text color" style="margin-top: -15px;">Important</span> --%>
						<%-- <strong>${ reportMessage.messageName }</strong><br> --%>
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
					ISTEP+ and IMAST Student Report (ISR) are available for the three most recent administrations. IREAD-3 Student Report (ISR) are available for the 2013, 2014 and 2015 administrations (Spring and Summer).
				</c:if>
				<c:if test="${groupFile=='IPR'}">
					Image of student responses (IP) to Applied Skills test are available for the 2014 and 2013 ISTEP+ administrations. (Not available for ISTEP+ 2015, IMAST or IREAD-3)<br /><br />
				</c:if>
				<c:if test="${groupFile=='BOTH'}">
					Invitation Code Letters (IC) are available for the current ISTEP+ administration only.<br /><br />
					Image of student responses (IP) to Applied Skills test are available for the 2014 and 2013 ISTEP+ administrations. (Not available for ISTEP+ 2015, IMAST or IREAD-3)<br /><br />
				</c:if>
				<c:if test="${groupFile=='ICL'}">
					Invitation Code Letters (IC) are available for the current ISTEP+ administration only.<br /><br />
				</c:if>
			</div>
		</c:if>
	</c:if>
	
	<sec:authorize ifAnyGranted="ROLE_CTB">
		<c:choose>
			<c:when test="${fn:contains(themeName, 'usmo')}" >
				<%@ include file="groupDownloadOptionsMO.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="groupDownloadOptions.jsp" %>
			</c:otherwise>
		</c:choose>
	</sec:authorize>
	
	<sec:authorize ifNotGranted="ROLE_CTB">
		<c:if test="${empty dataloadMessage}">
			<c:if test="${hideContentFlag=='N'}">
				<c:choose>
					<c:when test="${fn:contains(themeName, 'usmo')}" >
						<%@ include file="groupDownloadOptionsMO.jsp" %>
					</c:when>
					<c:otherwise>
						<%@ include file="groupDownloadOptions.jsp" %>
					</c:otherwise>
				</c:choose>
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
	<c:forEach var="student" items="${studentList}"><input type="hidden" id="check-status-${student.id}" name="check-status-${student.rowNum - 1}" value="0" /></c:forEach>
</div>
