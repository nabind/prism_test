<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="content-panel" style="padding-left: 0px; border: 0px">
	<form:form method="post" class="report-form" id="eduCenterUsersSearchForm">
		<div class="mid-margin-bottom">
			<div class="reportFilterCriteria1 panel-control rounded-border">
				<span id="filter-icon" class="icon-leaf icon-size2"></span> <b>Report Filters</b>
			</div>
			<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
				<div class="refresh-report display-none" style="position: absolute; top: 5px; right: 20px;">
					<a href="javascript:void(0)" id="searchEduCenterUsers" class="button blue-gradient glossy">Search</a>
				</div>
				<div class="with-mid-padding mid-margin-bottom icholderinner" style="min-width: 200px; overflow-x: auto">
					<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px; min-width: 720px">
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">Test Administration</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="eduCenterId" name="eduCenterId" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
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
									<select id="eduCenterId" name="eduCenterId" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
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
									<select id="eduCenterId" name="eduCenterId" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value="1" selected="selected">ADAMS CENTRAL</option>
										<option value="2">AJ BROWN CHRT</option>
										<option value="3">ALEXANDRIA</option>
										<option value="4">ANDERSON COMM</option>
										<option value="348">ANDERSON PREP A</option>
										<option value="379">ANDREW ACADEMY</option>
										<option value="5">ARGOS COMM</option>
										<option value="349">ASPIRE CH ACA</option>
										<option value="6">ATTICA CONS</option>
										<option value="7">AVON COMM</option>
										<option value="8">BARR REEVE</option>
										<option value="9">BARTHOLOMEW</option>
										<option value="10">BATESVILLE</option>
										<option value="11">BAUGO COMM</option>
										<option value="350">BEACON ACADEMY</option>
										<option value="12">BEECH GROVE</option>
										<option value="13">BENTON COMM</option>
										<option value="14">BLACKFORD</option>
										<option value="15">BLOOMFIELD</option>
										<option value="360">BLOOMINGTON P</option>
										<option value="16">BLUE RIVER</option>
										<option value="17">BLUFFTON HARR</option>
										<option value="18">BOONE TWP</option>
										<option value="19">BREMEN PUBLIC</option>
										<option value="20">BROWN CO COMM</option>
										<option value="21">BROWNSBURG</option>
										<option value="22">BROWNSTOWN</option>
										<option value="23">BURRIS LAB SCH</option>
										<option value="24">C A BEARD MEM</option>
										<option value="25">C MONTESSORI</option>
										<option value="26">CA TINDLEY CH </option>
										<option value="393">CANAAN COMM A</option>
									</select>
								</p>
							</div>
						</div>
						<div class="three-columns report-inputs">
							<h6 class="margin-bottom-small">School</h6>
							<div class="float-left margin-right margin-bottom">
								<p class="button-height">
									<select id="eduCenterId" name="eduCenterId" class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list" style="width: 150px;">
										<option value="0" selected="selected">All Schools</option>
										<option value="11">ADAMS C ES M</option>
										<option value="13">ADAMS C MS M </option>
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
