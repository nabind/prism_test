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
	
	<div class="columns accordion with-padding" id="nameMailDiv">
		<p class="inline-label">
			<label for="groupFile" class="label">Generate Group File of: </label>
			<select name="groupFile" id="groupFile" class="select compact expandable-list">
			    <option value="5">Student PDF's</option>
				<option value="1">Individual Student Report</option>
				<option value="2">Image Prints</option>
				<option value="3">Both (IP and ISR)</option>
				<option value="4">Invitation Code Letter</option>
			</select>
		</p>
		
		<p class="inline-label">
			<label for="collationHierarchy" class="label">Collation Hierarchy: </label>
			<select name="collationHierarchy" id="collationHierarchy" class="select compact expandable-list">
				<option value="1">School, Class, Student Last Name, First Name, Middle Initial</option>
				<option value="2">Student Last Name, First Name, Middle Initial</option>
			</select>
		</p>
	</div>

	<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px;">
		<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableGD">
			<thead>
				<tr role="row">
					<th aria-label="" style="width: 13px;" colspan="1" rowspan="1" role="columnheader" class="sorting_disabled" scope="col">
						<input name="check-all" id="check-all" value="1" type="checkbox">
					</th>
					<th aria-label="Text: activate to sort column ascending" style="width: 356px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
				</tr>
			</thead>
			<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListGD">
			</tbody>
		</table>
	</div>

	<div class="columns accordion with-padding">
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
		<a href="#" class="downloadBulkPdf button" id="downloadCombinedPdfsGD" style="margin-left: 10px; float: left;">
			<span class="button-icon icon-download blue-gradient report-btn">Combined PDFs</span>
			Generate Download File
		</a>
		<a href="#" class="downloadBulkPdf button" id="downloadSinglePdfsGD" style="margin-left: 0px; float: right;">
			<span class="button-icon icon-download blue-gradient report-btn">PDF</span>
			Single Student
		</a>
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
</div>
