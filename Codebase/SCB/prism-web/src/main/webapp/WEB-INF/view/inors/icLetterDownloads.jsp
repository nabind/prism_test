<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
	li.jstree-open > ul {
		padding-left: 29% !important;
	}
</style>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload" /></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.fileDownload" /></p>
	<input type="hidden" value="/public/ISTEP/Reports/Dummy_Student_Report_files" name="reportUrl" >
	
	<dl class="download-instructions accordion same-height">
		<dt class="closed"><span class="icon-plus-round tracked"></span> <span class="icon-minus-round tracked" style="display:none"></span> <spring:message code="label.instructions" /></dt>
		<dd style="height: 136px; display: none;" class="accordion-body">
			<div class="with-padding">
				<p><spring:message code="p.icLetter.1" /></p>
				<p><spring:message code="p.icLetter.2" /></p>
				<p><spring:message code="p.icLetter.3" /></p>
			</div>
		</dd>
	</dl>
	
	<div class="columns with-padding" style="padding-left:23px !important;">
		<span class="tag" id="studCount">0</span> Student(s) in <span class="tag" id="classCount">0</span> Class(es) in <span class="tag" id="schoolCount">0</span> school(s) have been selected.	
	</div>
	
	<div class="columns accordion with-padding">
		<p class="inline-label">
			<label for="collationHierarchy" class="label"><spring:message code="label.collationHierarchy" />: </label>
			<c:set var="collationHierarchyVal" value="${groupDownload.collationHierarchy}"/>
			<%
				String collationHierarchyVal = (String) pageContext.getAttribute("collationHierarchyVal");
			%>
			<select name="collationHierarchy" id="collationHierarchy" class="select compact expandable-list">
				<option value="1" <%if("1".equals(collationHierarchyVal)) {%>selected="selected"<%} %>><spring:message code="icLetter.collation.1" /></option>
				<option value="2" <%if("2".equals(collationHierarchyVal)) {%>selected="selected"<%} %>><spring:message code="icLetter.collation.2" /></option>
			</select>
		</p>
	</div>
	
	<div class="columns accordion with-padding tree-container">
		<div class="four-columns">School</div>
		<div class="four-columns">Class</div>
		<div class="four-columns">Student</div>
		<div id="chkTreeViewForOrg" class="jstree jstree-0 jstree-focused jstree-apple" rootid="${rootOrgId}" reportUrl="${reportUrl}" style="width: 95%"></div>
		<input type="text" name="selectedNodes" id="selectedNodes" style="display:none">
	</div>
	
	<div class="columns accordion with-padding">
		<p class="inline-label">
			<label for="fileName" class="label" style="width: 345px !important;"><spring:message code="icLetter.fileName" /></label>
			<input name="fileName" type="text" value="${groupDownload.fileName}" id="fileName" class="input validate[required]" maxlength="50" style="width:187px;">
			<span class="info-spot">
				<span class="icon-info-round"></span>
				<span class="info-bubble" style="width: 123px;">
					<spring:message code="info.icLetter" />
				</span>
			</span>
		</p>
		
		<p class="inline-label">
			<label for="email" class="label" style="width: 345px !important;"><spring:message code="icLetter.email" /></label>
			<input name="email" type="text" value="${groupDownload.email}" id="email" class="input validate[required,custom[email]]" maxlength="100" style="width:187px;">
		</p>
	</div>
	
	<div class="columns accordion with-padding" style="margin-bottom:0">
		<p class="message small-margin-bottom orange-gradient" style="margin-bottom: 10px !important">
			<spring:message code="note.icLetter" />
		</p>
		<a href="" class="downloadBulkICPdf button" id="downloadBulkICPdfSeperate" >
			<span class="button-icon icon-download blue-gradient report-btn"><spring:message code="label.separatePDFs" /></span>
			<spring:message code="label.generateDownloadFile" />
		</a>
		<a href="" class="downloadBulkICPdf button" id="downloadBulkICPdf" style="margin-left: 20px; float: right">
			<span class="button-icon icon-download blue-gradient report-btn"><spring:message code="label.combinedPDFs" /></span>
			<spring:message code="label.generateDownloadFile" />
		</a>
	</div>
	</form:form>
</div>
