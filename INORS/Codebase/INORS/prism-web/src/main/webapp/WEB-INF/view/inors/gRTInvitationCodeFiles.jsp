<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="content-panel" style="padding-left: 0px; border: 0px">
	<form:form method="post" class="report-form" id="grtIcFileDownloadForm" style="height: ">
		<div class="mid-margin-bottom">
			<div class="reportFilterCriteria1 panel-control rounded-border">
				<span id="filter-icon" class="icon-leaf icon-size2"></span> <b>Report Filters</b>
			</div>
			<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
				<div class="with-mid-padding mid-margin-bottom icholderinner" style="min-width: 200px; overflow-x: auto">
					<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px; min-width: 720px">
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">Test Administration</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="testAdministration" name="testAdministration" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value="19~25" selected="selected">ISTEP Spring 2013</option>
										<option value="20~25">IMAST Spring 2013</option>
										<option value="21~25">IREAD-3 Spring 2013</option>
										<option value="22~25">IREAD-3 Summer 2013</option>
										<option value="11~24">ISTEP Spring 2012</option>
										<option value="12~24">IMAST Spring 2012</option>
										<option value="13~24">IREAD-3 Spring 2012</option>
										<option value="17~24">IREAD-3 Summer 2012</option>
										<option value="8~23">ISTEP Spring 2011</option>
										<option value="10~23">IMAST Spring 2011</option>
										<option value="6~22">ISTEP Spring 2010</option>
										<option value="7~22">IMAST Spring 2010</option>
									</select>
								</p>
							</div>
						</div>
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">Test Program</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="testProgram" name="testProgram" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value="1" selected="selected">Public Schools</option>
										<option value="2">Non Public Schools</option>
									</select>
								</p>
							</div>
						</div>
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">Corp/Diocese</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="corpDiocese" name="corpDiocese" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value='-1'>Please Select</option>
										<c:forEach var="corpDioceseVar" items="${corporationList}">
											<option value="${corpDioceseVar.value}">${corpDioceseVar.name}</option>
										</c:forEach>
									</select>
								</p>
							</div>
						</div>
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">School</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="school" name="school" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value='-1'>Please Select Corp/Diocese</option>
									</select>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
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
			<a href="/inors/staticfiles/S2012-13 Invitation Code Layout.xls">2012-13 Invitation Code File Record Layout</a><br />
			<a href="/inors/staticfiles/S2011-12 Invitation Code Layout.xls">2011-12 Invitation Code File Record Layout</a>
		</div>
		<a class="button float-right" id="downloadICFile" style="cursor: pointer;">
			<span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File
		</a>
	</div>
</div>
