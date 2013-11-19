<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
	li.jstree-open > ul {
		padding-left: 29% !important;
	}
</style>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none">PDF File Generation has been requested.<br/>Click on 'Group Download Files' for Status of request(s).</p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/ISTEP/Reports/Dummy_Student_Report_files" name="reportUrl" >
	
	<dl class="download-instructions accordion same-height">
		<dt class="closed"><span class="icon-plus-round tracked"></span> <span class="icon-minus-round tracked" style="display:none"></span> Instructions</dt>
		<dd style="height: 136px; display: none;" class="accordion-body">
			<div class="with-padding">
				<p>First section content</p>
				<p>The accordion plugin may also equalize the sections' height, just by adding a class - as for this example.</p>
				<p>Give it a try!</p>
			</div>
		</dd>
	</dl>
	
	<div class="columns with-padding" style="padding-left:23px !important;">
		<span class="tag" id="studCount">0</span> Student(s) in <span class="tag" id="classCount">0</span> Class(es) in <span class="tag" id="schoolCount">0</span> school(s) have been selected.	
	</div>
	
	<div class="columns accordion with-padding">
		<p class="inline-label">
			<label for="collationHierarchy" class="label">Collation Hierarchy: </label>
			<c:set var="collationHierarchyVal" value="${groupDownload.collationHierarchy}"/>
			<%
				String collationHierarchyVal = (String) pageContext.getAttribute("collationHierarchyVal");
			%>
			<select name="collationHierarchy" id="collationHierarchy" class="select compact expandable-list">
				<option value="1" <%if("1".equals(collationHierarchyVal)) {%>selected="selected"<%} %>>School, Class, Student Last Name, First Name, Middle Initial</option>
				<option value="2" <%if("2".equals(collationHierarchyVal)) {%>selected="selected"<%} %>>Student Last Name, First Name, Middle Initial</option>
			</select>
		</p>
	</div>
	
	<div class="columns accordion with-padding tree-container">
		<div class="four-columns">School</div>
		<div class="four-columns">Class</div>
		<div class="four-columns">Student</div>
		<div id="chkTreeViewForOrg" class="jstree jstree-0 jstree-focused jstree-apple" rootid="${rootOrgId}" reportUrl="${reportUrl}" style="width: 95%">
			
		</div>
		<input type="text" name="selectedNodes" id="selectedNodes" style="display:none">
	</div>
	
	<div class="columns accordion with-padding">
		<p class="inline-label">
			<label for="fileName" class="label" style="width: 345px !important;">Name of Generated File (50 characters maximum): </label>
			<input name="fileName" type="text" value="${groupDownload.fileName}" id="fileName" class="input validate[required]" maxlength="50" style="width:187px;">
			<span class="info-spot">
				<span class="icon-info-round"></span>
				<span class="info-bubble" style="width: 123px;">
					Characters allowed are { A-Z a-z 0-9 -_ } ( Items within{ } )
				</span>
			</span>
		</p>
		
		<p class="inline-label">
			<label for="email" class="label" style="width: 345px !important;">Email address for notification of Generated File complete: </label>
			<input name="email" type="text" value="${groupDownload.email}" id="email" class="input validate[required,custom[email]]" maxlength="30" style="width:187px;">
		</p>
	</div>
	
	<div class="columns accordion with-padding" style="margin-bottom:0">
		<p class="message small-margin-bottom orange-gradient" style="margin-bottom: 10px !important">
			Note: For those schools that administered the online ISTEP+ Applied Skills assessment, the combined PDFs group download feature to "Generate Group File of Both" should not be used. Please contact the CTB/Indiana Helpdesk for more information.
		</p>
		<a href="" class="downloadBulkICPdf button" id="downloadBulkICPdfSeperate" >
			<span class="button-icon icon-download blue-gradient report-btn">Separate PDFs</span>
			Generate Download File
		</a>
		<a href="" class="downloadBulkICPdf button" id="downloadBulkICPdf" style="margin-left: 20px; float: right">
			<span class="button-icon icon-download blue-gradient report-btn">Combined PDFs</span>
			Generate Download File
		</a>
	</div>
	</form:form>
</div>
