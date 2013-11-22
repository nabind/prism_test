<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="downloadStudentFile" modelAttribute="downloadStudentFile">
		<p class="success-message message small-margin-bottom green-gradient" style="display:none">PDF File Generation has been requested.<br/>Click on 'Group Download Files' for Status of request(s).</p>
		<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
		<div class="reportFilterCriteria panel-control report-filter report-filter-0" reportid="1" param="/public/TASC/Reports/TASC_Org_Hier/High_School_Equivalency_Dashboard_files" tabcount="0" count="0" assessment="1002">
			<span id="filter-icon" class="icon-leaf icon-size2"></span> <b>Data Filter Options</b>
		</div>
		<div class="mid-margin-bottom icholderinner" style="min-height:110px;min-width:200px;overflow-x:auto">
			<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
				<div class="columns with-padding">
					<div class="three-columns report-inputs">
						<h6 class="margin-bottom-small">Test Start Date</h6>
						<input id="p_Start_Date" class="input input-compact navy-gradient jqdatepicker validate[custom[date]]" type="text" style="width:128px" value="" name="p_Start_Date" />
					</div>
					<div class="three-columns report-inputs">
						<h6 class="margin-bottom-small">Test End Date</h6>
						<input id="p_End_Date" class="input input-compact navy-gradient jqdatepicker validate[custom[date]]" type="text" style="width:128px" value="" name="p_End_Date" />
					</div>
					
				</div>
			</div>
		</div>
		<div class="columns accordion with-padding" style="margin-bottom: 0">
			<div class="">
				<a href="/tasc/staticfiles/ISTEP S2012-13 GR 3-8 GRT Corp Version.xls">2012-13 GRT File Record Layout</a><br />
				<a href="/tasc/staticfiles/ISTEP S2011-12 GR 3-8 GRT Corp Version.xls">2011-12 GRT File Record Layout</a><br />
				<a href="/tasc/staticfiles/ISTEP S2010-11 GR 3-8 GRT Corp Version.xls">2010-11 GRT File Record Layout</a><br />
				<a href="/tasc/staticfiles/ISTEP S2009-10 GR 3-8 GRT Corp Version.xls">2009-10 GRT File Record Layout</a>
			</div>
			
				<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;">
					<span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File
				</a>
			
		</div>
		<div class="columns accordion with-padding" style="margin-bottom: 0">
			<div class="">
				<a href="/tasc/staticfiles/S2012-13 Invitation Code Layout.xls">2012-13 Invitation Code File Record Layout</a><br />
				<a href="/tasc/staticfiles/S2011-12 Invitation Code Layout.xls">2011-12 Invitation Code File Record Layout</a>
			</div>
			<a class="button float-right" id="downloadICFile" style="cursor: pointer;">
				<span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File
			</a>
		</div>
	</form:form>
</div>