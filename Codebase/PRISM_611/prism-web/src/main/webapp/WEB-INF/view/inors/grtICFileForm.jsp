<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
	li.jstree-open > ul {
		padding-left: 29% !important;
	}
</style>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="grtDownload" modelAttribute="grtDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload"/></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.groupDownload"/></p>
	<input type="hidden" value="/public/INORS/Report/Report1_files" name="reportUrl" >
	<c:if test="${not empty reportMessages}">
		<c:forEach var="reportMessage" items="${reportMessages}">
			<c:if test="${reportMessage.displayFlag=='Y'}">
				<c:if test="${reportMessage.messageType=='DM'}">
					<div class="big-message">
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

	<sec:authorize access="hasAnyRole('ROLE_CTB')">
		<%@ include file="grtIcOptions.jsp" %>
	</sec:authorize>
	<sec:authorize access="!hasAnyRole('ROLE_CTB')">
		<c:if test="${empty dataloadMessage}">
			<%@ include file="grtIcOptions.jsp" %>
		</c:if>
	</sec:authorize>

	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
</div>
