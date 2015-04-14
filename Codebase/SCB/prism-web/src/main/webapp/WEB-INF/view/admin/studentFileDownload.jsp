<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:message var="studentFileDownload" key="student.file.download.button" />
<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="downloadStudentFile" modelAttribute="downloadStudentFile">
		<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload" /></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.fileDownload" /></p>
		<p class="error-message2 message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error2.fileDownload" /></p>
		<div class="reportFilterCriteria panel-control report-filter report-filter-0" reportid="1" param="/public/TASC/Reports/TASC_Org_Hier/High_School_Equivalency_Dashboard_files" tabcount="0" count="0" assessment="1002">
			<span id="filter-icon" class="icon-leaf icon-size2"></span> <b><spring:message code="label.dataFilterOptions" /></b>
		</div>
		<div class="mid-margin-bottom icholderinner" style="min-height:110px;min-width:200px;overflow-x:auto">
			<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
				<div class="columns with-padding">
					<div class="three-columns report-inputs">
						<h6 class="margin-bottom-small"><spring:message code="label.dateType" /></h6>
						<select id="p_Date_Type" name="p_Date_Type" class="select navy-gradient" >
							<option value="TTD"><spring:message code="label.testTakenDate" /></option>
							<option value="PD"><spring:message code="label.lastUpdatedDate" /></option>
						</select>
					</div>
					<div class="three-columns report-inputs">
						<h6 class="margin-bottom-small"><spring:message code="label.startDate" /></h6>
						<input id="p_Start_Date" class="input input-compact navy-gradient jqdatepicker validate[custom[date]]" type="text" style="width:128px" value="" name="p_Start_Date" />
					</div>
					<div class="three-columns report-inputs">
						<h6 class="margin-bottom-small"><spring:message code="label.endDate" /></h6>
						<input id="p_End_Date" class="input input-compact navy-gradient jqdatepicker validate[custom[date]]" type="text" style="width:128px" value="" name="p_End_Date" />
					</div>
				</div>
			</div>
		</div>
		<p class="with-padding">
			<span class="big-message-icon icon-chat orange"></span>
			<spring:message code="p.studentFileDownload.1" />
			<br/><br/>
			<span class="tag"><spring:message code="label.note" />:</span> <spring:message code="p.studentFileDownload.2" />
		</p>
		<c:if test="${not empty actionMap[studentFileDownload]}">
		<div class="columns accordion with-padding" style="margin-bottom: 0">
			<a class="tag with-small-padding grey-gradient icon-down-round" id="downloadStudentFileLayout" 
				style="cursor: pointer; top: 6px;position: relative;" 
				href="downloadAssest.do?assetPath=${tascStudentDataFileLayoutHref}">
				<spring:message code="button.downloadFileLayout" /> (XLSX)
			</a>
			<a class="button float-right" id="downloadStudentFileDAT" style="cursor: pointer;">
				<span class="button-icon icon-download blue-gradient report-btn">DAT</span> <spring:message code="label.generateDownloadFile" />
			</a>
		</div>
		<div class="columns accordion with-padding" style="margin-bottom: 0">
			<a class="button float-right" id="downloadStudentFileXML" style="cursor: pointer; top: 6px;position: right;">
				<span class="button-icon icon-download blue-gradient report-btn">XML</span> <spring:message code="label.generateDownloadFile" />
			</a>
		</div>
		</c:if>
	</form:form>
</div>