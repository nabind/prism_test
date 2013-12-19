<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
	li.jstree-open > ul {
		padding-left: 29% !important;
	}
</style>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="grtDownload" modelAttribute="grtDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none">PDF File Generation has been requested.<br/>Click on 'Group Download Files' for Status of request(s).</p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/INORS/Report/Report1_files" name="reportUrl" >
	
	<div id="grtDiv" class="columns accordion with-padding hidden" style="margin-bottom: 0">
		<div id="grtLinks" class="">
			<!-- <a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a><br />
			<a class="button" id="grt2011" href="/inors/staticfiles/ISTEP S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2010-11 GRT File Record Layout</a><br />
			<a class="button" id="grt2012" href="/inors/staticfiles/ISTEP S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a><br />
			<a class="button" id="grt2013" href="/inors/staticfiles/ISTEP S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a> -->
		</div>
		<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;">
			<span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File
		</a>
	</div>
	<div id="icDiv" class="columns accordion with-padding hidden" style="margin-bottom: 0">
		<div id="icLinks" class="">
			<!-- <a class="button" id="ic2013" href="/inors/staticfiles/S2011-12 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 Invitation Code File Record Layout</a><br />
			<a class="button" id="ic2013" href="/inors/staticfiles/S2012-13 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 Invitation Code File Record Layout</a> -->
		</div>
		<a class="button float-right" id="downloadICFile" style="cursor: pointer;">
			<span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File
		</a>
	</div>
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
</div>
