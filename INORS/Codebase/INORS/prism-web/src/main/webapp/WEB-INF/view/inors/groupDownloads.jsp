<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="groupDownload" modelAttribute="groupDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none"><strong>PDF File Generation has been requested. Click on 'Group Download Files' for Status of request(s).</strong></p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl" >
	<dl class="download-instructions accordion same-height">
		<dt class="closed"><span class="icon-plus-round tracked"></span> <span class="icon-minus-round tracked" style="display:none"></span> Instructions</dt>
		<dd style="height: auto; display: none;" class="accordion-body">
				${groupDownloadInstructionMessage}
		</dd>
	</dl>
	<div class="columns with-padding" style="padding-left:23px !important;">
		<span class="tag" id="studCount">0</span> Student(s) in <span class="tag" id="classCount">0</span> Class(es) in <span class="tag" id="schoolCount">0</span> school(s) have been selected.	
	</div>
	<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px;">
		<div class="dataTables_header">
			<div class="dataTables_length" id="sorting-advanced_length">
				<label>Show <span class="select blue-gradient glossy replacement" style="width: 46px;" tabindex="0">
					<span class="select-value" id="studentTableGDSelectedVal" >10</span>
					<span class="select-arrow"></span>
					<span style="" class="drop-down custom-scroll">
						<span class="selected">10</span>
						<span>25</span>
						<span>50</span>
						<span>100</span>
						<div style="display: none;" class="custom-vscrollbar">
							<div></div>
						</div>
					</span>
					<select style="display: none;" tabindex="-1" class="" aria-controls="sorting-advanced" size="1" name="sorting-advanced_length" id="studentTableGDSelect" >
						<option selected="selected" value="10">10</option>
						<option value="25">25</option>
						<option value="50">50</option>
						<option value="100">100</option>
					</select>
				</span> entries</label>
				</div>
			<div id="sorting-advanced_filter" class="dataTables_filter">
				<label>Search: <input aria-controls="sorting-advanced" type="text"></label>
			</div>
		</div>
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
						    	<c:when test="${student.ic==''}"><a class="button compact icon-forbidden red-gradient disabled with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button compact icon-download blue glossy with-tooltip" title="Invitation Code File" href="downloadZippedPdf.do?fileName=${student.ic}">Download</a></c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${student.isr==''}"><a class="button compact icon-forbidden red-gradient disabled with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button compact icon-download green glossy with-tooltip" title="Individual Student Report" href="downloadZippedPdf.do?fileName=${student.isr}">Download</a></c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${student.ip==''}"><a class="button compact icon-forbidden red-gradient disabled with-tooltip" title="Not Available" href="#"></a></c:when>
								<c:otherwise><a class="button compact icon-download orange glossy with-tooltip" title="Image Print" href="downloadZippedPdf.do?fileName=${student.ip}">Download</a></c:otherwise>
							</c:choose>
							</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="dataTables_footer">
			<div id="sorting-advanced_info" class="dataTables_info">Showing 1 to 10 of 18 entries</div>
			<div id="sorting-advanced_paginate" class="dataTables_paginate paging_full_numbers">
				<a id="sorting-advanced_first" tabindex="0" class="first paginate_button paginate_button_disabled">First</a>
				<a id="sorting-advanced_previous" tabindex="0" class="previous paginate_button paginate_button_disabled">Previous</a>
				<span><a tabindex="0" class="paginate_active">1</a><a tabindex="0" class="paginate_button">2</a></span>
				<a id="sorting-advanced_next" tabindex="0" class="next paginate_button">Next</a>
				<a id="sorting-advanced_last" tabindex="0" class="last paginate_button">Last</a>
			</div>
		</div>
	</div>
	<div class="columns accordion with-padding" id="nameMailDiv">
		<p class="inline-label">
			<label for="fileName" class="label" style="width: 345px !important;">Name of Generated File (50 characters maximum): </label>
			<input name="fileName" type="text" value="${fileName}" id="fileName" class="input validate[required]" maxlength="50" style="width:200px;">
			<span class="info-spot">
				<span class="icon-info-round"></span>
				<span class="info-bubble" style="width: 123px;">
					Characters allowed are { A-Z a-z 0-9 -_ } ( Items within{ } )
				</span>
			</span>
		</p>
		<p class="inline-label">
			<label for="email" class="label" style="width: 345px !important;">Email address for notification of Generated File complete: </label>
			<input name="email" type="text" value="${email}" id="email" class="input validate[required,custom[email]]" maxlength="30" style="width:200px;">
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
		<input type="hidden" name="buttonGD" id="buttonGD" />
		<input type="hidden" name="startIndex" id="startIndex" />
		<input type="hidden" name="endIndex" id="endIndex" />
		<input type="hidden" name="length" id="length" />
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
