<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
				<fieldset class="fieldset">
					<legend class="legend">${ reportMessage.messageName }</legend>
					<p class="inline-label">${ reportMessage.message }</p>
				</fieldset>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${hideContentFlag=='Y'}">
		<div class="message small-margin-bottom orange-gradient dataload-message">
			<c:if test="${not empty reportMessages}">${dataloadMessage}</c:if>
			<c:if test="${empty reportMessages}">
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
			</c:if>
		</div>
	</c:if>
	<c:if test="${hideContentFlag=='N'}">
		<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableGD">
				<thead>
					<tr role="row">
						<th aria-label="" style="width: 13px;" colspan="1" rowspan="1" role="columnheader" class="sorting_disabled" scope="col">
							<img id="checkAllImg" src="themes/acsi/img/empty.bmp" /><input type="hidden" id="checkAllVal" value="0" /><br />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 350px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 150px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Class</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Grade</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 120px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student PDFs</th>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListGD">
					<c:forEach var="student" items="${studentList}">
						<tr id="">
						    <td scope="row" class="checkbox-cell  sorting_1 vertical-center"><input name="checked[]" id="check-student-${student.id}" value="0" type="checkbox" /></td>
						    <td class="vertical-center">${student.name}</td>
						    <td class="vertical-center">${student.klass}</td>
						    <td class="vertical-center">${student.grade}</td>
						    <td aria-label="Text: activate to sort column ascending" style="width: 25px; text-align: center;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting vertical-center" scope="col">
						    	<span class="button-group compact">
						    	<c:choose>
							    	<c:when test="${(not empty student.ic) && (student.icFlag == 'AC')}"><a class="button compact icon-download blue glossy with-tooltip" target="_blank" title="Invitation Code Letter" href="downloadZippedPdf.do?fileName=${student.ic}&fileType=Invitation_Code_Letter"></a></c:when>
									<c:otherwise><a class="button compact icon-forbidden red disabled with-tooltip" title="No IC Letter Available" href="#"></a></c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${(not empty student.isr) && (student.isrFlag == 'Y')}"><a class="button compact icon-download green glossy with-tooltip" target="_blank" title="Individual Student Report" href="downloadZippedPdf.do?fileName=${student.isr}&fileType=Individual_Student_Report"></a></c:when>
									<c:otherwise><a class="button compact icon-forbidden red disabled with-tooltip" title="No ISR Available" href="#"></a></c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${(not empty student.ip) && (student.ipFlag == 'Y')}"><a class="button compact icon-download orange glossy with-tooltip" target="_blank" title="Image Print" href="downloadZippedPdf.do?fileName=${student.ip}&fileType=Image_Print"></a></c:when>
									<c:otherwise><a class="button compact icon-forbidden red disabled with-tooltip" title="No Image Print Available" href="#"></a></c:otherwise>
								</c:choose>
								</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<p class="success-message message small-margin-bottom green-gradient" style="display:none"><strong>PDF File Generation has been requested. Click on 'Group Download Files' for Status of request(s).</strong></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
		<div class="columns accordion with-padding" id="nameMailDiv">
			<p class="inline-label">
				<label for="fileName" class="label" style="width: 345px !important; float: left;">Name of Generated File (50 characters maximum): </label>
				<input name="fileName" type="text" value="${fileName}" id="fileName" class="input validate[required, custom[fileNameGD, maxSize[50]]" maxlength="50" style="width:200px;">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width: 123px;">
						Characters allowed are { A-Z a-z 0-9 _. } ( Items within{ } )
					</span>
				</span>
			</p>
			<p class="inline-label">
				<label for="email" class="label" style="width: 345px !important; float: left;">Email address for notification of Generated File complete: </label>
				<input name="email" type="text" value="${email}" id="email" class="input validate[required,custom[email]]" maxlength="30" style="width:200px;">
			</p>
		</div>
		<div class="columns accordion with-padding" style="margin-bottom:0">
			<a href="#" class="downloadBulkPdf button" id="downloadSeparatePdfsGD" style="margin-left: 0px; float: left;">
				<span class="button-icon icon-download blue-gradient report-btn"></span>
				Generate Download File <span class="tag">Separate</span> PDFs
			</a>
			<a href="#" class="downloadBulkPdf button" id="downloadCombinedPdfsGD" style="margin-left: 10px; float: right;">
				<span class="button-icon icon-download blue-gradient report-btn"></span>
				Generate Download File <span class="tag">Combined</span> PDFs
			</a>
			<input type="hidden" name="buttonGD" id="buttonGD" />
			<input type="hidden" name="startIndex" id="startIndex" />
			<input type="hidden" name="endIndex" id="endIndex" />
			<input type="hidden" name="length" id="length" />
		</div>
	</c:if>
	
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
