<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:theme code="theme.name" var="themeName"/>
<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="grfDownload" modelAttribute="grfDownload">
		<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload"/></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.groupDownload"/></p>
		<input type="hidden" value="/public/Missouri/Report/GRF_files" name="reportUrl" >
		
		<dl class="download-instructions accordion same-height">
			<c:if test="${not empty reportMessage}"><dt class="closed accordion-header"><span class="icon-plus-round tracked"></span><span class="icon-minus-round tracked" style="display: none;"></span></c:if>
			<c:if test="${empty reportMessage}"><dt class="open accordion-header"><span class="icon-plus-round tracked" style="display: none;"></span><span class="icon-minus-round tracked"></span></c:if>
			</span> Report Messages</dt>
			<c:if test="${not empty reportMessage}"><dd style="height: auto; display: none;" class="accordion-body with-padding"></c:if>
			<c:if test="${empty reportMessage}"><dd style="height: auto;" class="accordion-body with-padding"></c:if>
					${reportMessage}
			</dd>
		</dl>
		
		<div class="columns accordion with-padding" style="margin-bottom:0">
			<!--<c:if test="${not empty actionMap[grfDownload]}"> -->
				<a href="#" class="button" id="downloadGrf" style="margin-left: 10px; float: right;">
					<span class="button-icon icon-download blue-gradient report-btn"></span>
					<spring:message code="button.grfDownload.downloadGrf" />
				</a>
			<!-- </c:if>-->
		</div>
	</form:form>
</div>
