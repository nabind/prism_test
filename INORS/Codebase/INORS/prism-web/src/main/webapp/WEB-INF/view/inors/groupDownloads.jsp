<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><strong>PDF File Generation has been requested. Click on 'Group Download Files' for Status of request(s).</strong></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl" >
	
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

	<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px;">
		<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableGD">
			<thead>
				<tr role="row">
					<th aria-label="" style="width: 13px;" colspan="1" rowspan="1" role="columnheader" class="sorting_disabled" scope="col">
						<input name="check-all" id="check-all" value="1" type="checkbox">
					</th>
					<th aria-label="Text: activate to sort column ascending" style="width: 350px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
					<th aria-label="Text: activate to sort column ascending" style="width: 150px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Class</th>
					<th aria-label="Text: activate to sort column ascending" style="width: 150px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Grade</th>
					<th aria-label="Text: activate to sort column ascending" style="width: 100px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Single Student</th>
				</tr>
			</thead>
			<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListGD">
				<c:forEach var="student" items="${studentList}">
					<tr id="">
					    <td scope="row" class="checkbox-cell  sorting_1 vertical-center"><input name="checked[]" id="check-student-${student.id}" value="${student.id}" type="checkbox" /></td>
					    <td class="vertical-center">${student.name}</td>
					    <td class="vertical-center">${student.klass}</td>
					    <td class="vertical-center">${student.grade}</td>
					    <td aria-label="Text: activate to sort column ascending" style="width: 25px; text-align: center;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting vertical-center" scope="col">
					    	<span class="button-group compact">
					    	<c:choose>
						    	<c:when test="${student.ic==''}"><a class="button icon-forbidden red with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button icon-down-fat green with-tooltip" title="Invitation Code File" href="downloadZippedPdf.do?fileName=${student.ic}">Download</a></c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${student.isr==''}"><a class="button icon-forbidden red with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button icon-down green with-tooltip" title="Individual Student Report" href="downloadZippedPdf.do?fileName=${student.isr}">Download</a></c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${student.ip==''}"><a class="button icon-forbidden red with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button icon-down-round green with-tooltip" title="Image Print" href="downloadZippedPdf.do?fileName=${student.ip}">Download</a></c:otherwise>
							</c:choose>
							</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="columns accordion with-padding" id="nameMailDiv">
		<p class="inline-label">
			<label for="fileName" class="label" style="width: 345px !important;">Name of Generated File (50 characters maximum): </label>
			<input name="fileName" type="text" value="${fileName}" id="fileName" class="input validate[required]" maxlength="50" style="width:187px;">
			<span class="info-spot">
				<span class="icon-info-round"></span>
				<span class="info-bubble" style="width: 123px;">
					Characters allowed are { A-Z a-z 0-9 -_ } ( Items within{ } )
				</span>
			</span>
		</p>
		
		<p class="inline-label">
			<label for="email" class="label" style="width: 345px !important;">Email address for notification of Generated File complete: </label>
			<input name="email" type="text" value="${email}" id="email" class="input validate[required,custom[email]]" maxlength="30" style="width:187px;">
		</p>
	</div>
	
	<div class="columns accordion with-padding" style="margin-bottom:0">
		<p class="message small-margin-bottom orange-gradient" style="margin-bottom: 10px !important">
			Note: For those schools that administered the online ISTEP+ Applied Skills assessment, the combined PDFs group download feature to "Generate Group File of Both" should not be used. Please contact the CTB/Indiana Helpdesk for more information.
		</p>
		<a href="#" class="downloadBulkPdf button" id="downloadSeparatePdfsGD" style="margin-left: 0px; float: left;">
			<span class="button-icon icon-download blue-gradient report-btn">Separate PDFs</span>
			Generate Download File
		</a>
		<a href="#" class="downloadBulkPdf button" id="downloadCombinedPdfsGD" style="margin-left: 10px; float: right;">
			<span class="button-icon icon-download blue-gradient report-btn">Combined PDFs</span>
			Generate Download File
		</a>
		<!-- <a href="#" class="downloadBulkPdf button" id="downloadSinglePdfsGD" style="margin-left: 0px; float: right;">
			<span class="button-icon icon-download blue-gradient report-btn">PDF</span>
			Single Student
		</a> -->
		<input type="hidden" name="buttonGD" id="buttonGD" />
	</div>
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
</div>
