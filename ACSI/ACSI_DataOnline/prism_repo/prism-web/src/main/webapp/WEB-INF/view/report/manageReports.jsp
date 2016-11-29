<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin">
<h1>Manage Reports</h1>
</hgroup>
<!--[if gt IE 6]>
<style>
	.modal-buttons {
		margin: 0px 20px 0px 0px !important;
	}
</style>
<![endif]-->
<style>
	.label {width: 160px !important}
</style>
<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">

	<div class="right-column">

		<div class="content-panel" style="padding-left: 0px; border: none">
			<div class="report-panel-content panel-content linen">

				<div class="panel-control align-right">
					<a id="addDashboard" tenantName="" tenantId="" href="#" class="button glossy margin-left">
						<span class="button-icon blue-gradient manage-btn"><span class="icon-plus-round"></span></span>
						Add Report
					</a>
				</div>

				<div class="panel-load-target with-padding margin10 padding-none height-mid">
					<div class="report-container tabs-content padding-small height-mid manage-report-container">
						<div><table class="simple-table responsive-table" id="report-list">

							<thead>
								<tr>
									<th scope="col" width="25%">Report</th>
									<!--<th scope="col" width="15%">Date</th>-->
									<th scope="col" width="15%">Status</th>
									<th scope="col" width="25%">Role</th>
									<th scope="col" width="20%">Assessment Name</th>
									<th scope="col" width="15%" class="">Actions</th>
								</tr>
							</thead>

							<tbody id ="reportDetails">
								<c:forEach var="report" items="${reportList}">
									<tr id="${report.reportId}">
										<th scope="row"><span class="reportName">${report.reportName}</span><br>
											<small class="reportUrl">${report.reportUrl}</small></th>
										<!--<td class="vertical-center">Jul 5, 2011</td>-->
										<td class="vertical-center">
											<c:if test="${report.enabled}">
												<small class="tag green-bg status">Enabled</small>
											</c:if>
											<c:if test="${not report.enabled}">
												<small class="tag red-bg status">Disabled</small>
											</c:if>
										</td>
										<td class="roleContainer vertical-center">
											<c:forEach var="role" items="${report.roles}">
												<c:if test="${role == 'ROLE_ACSI'}">
												<small class="tag blue-bg role ${role}">${role}</small><br/>
												</c:if>
												<c:if test="${role == 'ROLE_CTB'}">
												<small class="tag green-bg role ${role}">${role}</small><br/>
												</c:if>
												<c:if test="${role == 'ROLE_SCHOOL'}">
												<small class="tag orange-bg role ${role}">${role}</small><br/>
												</c:if>
												<c:if test="${role == 'ROLE_CLASS'}">
												<small class="tag grey-bg role ${role}">${role}</small><br/>
												</c:if>												
												<c:if test="${role == 'ROLE_PARENT'}">
												<small class="tag red-bg role ${role}">${role}</small><br/>
												</c:if>	
											</c:forEach>
										</td>
										<td class="vertical-center">
											<small>${report.assessmentName}</small>
										</td>
										<td class="vertical-center" nowrap>
											<span class="button-group compact" width="50px">
												<a href="#"	class="button icon-pencil edit-report with-tooltip" title="Edit"
													reportId="${report.reportId}"></a>
												<a href="#"	reportId="${report.reportId}" reportName="${report.reportName}" class="button icon-trash with-tooltip confirm delete-Report"
													title="Delete"></a>
											</span>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table></div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="editRole" class="display-none" style="top:10px;position:relative;">
			<div class="">
				<form:form id="editReportForm" method="post" class="edit-report-form" action="updateReport.do">
					<input type="hidden" name="reportId" id="reportId"/>
					<p class="button-height inline-label">
						<label class="label" for="reportName">Report Name<span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="reportName" id="editReportName" style="width:200px" class="input full-width newReportName reset validate[required]"/>
					</p>
					
					<p class="button-height inline-label">
						<label class="label" for="reportUrl">Report URI<span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="reportUrl" id="editReportUrl" style="width:200px" class="input full-width newReportName reset validate[required]"/>
					</p>

					<p class="button-height inline-label">
						<label class="label" for="reportStatus">Status</label>
						<input type="checkbox" name="reportStatus" id="editReportStatus" class="switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
					</p>

					<p class="button-height inline-label">
						<label class="label" for="userRole">Roles</label>
						<select id="editUserRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list auto-open" multiple>
							<option value="ROLE_ACSI">ROLE_ACSI</option>
							<option value="ROLE_CTB">ROLE_CTB</option>
							<option value="ROLE_SCHOOL">ROLE_SCHOOL</option>
							<option value="ROLE_CLASS">ROLE_CLASS</option>
							<option value="ROLE_PARENT">ROLE_PARENT</option>
						</select>
					</p>
				</form:form>
			</div>
		</div>
	<div id="addReport" class="display-none">
		<div class="">
			<form:form id="addNewReport" name="addNewReport" class="add-User-form small-margin-top">
				<!--<input type="hidden" name="tenantId" id="tenantId" value="" />
				<input type="hidden" name="orgLevel" id="orgLevel" value="" />-->
				<input type="hidden" name="reportStatus" id="reportStatus" value="" />

				<p  style="width:329px"><span  id="imgHolder"></span></p>
				<p class="button-height inline-label">
					<label class="label" for="reportName">Report Name<span class="icon-star icon-size1 red"></span></label>
					<input type="text" name="reportName" id="reportName" style="width:200px" class="input full-width newReportName reset validate[required]" />
				</p>
				
				<p class="button-height inline-label">
					<label class="label" for="reportDescription">Report Description</label>
					<input type="text" name="reportDescription" id="reportDescription" style="width:200px" class="input full-width newReportName reset" />
				</p>
			
				<p class="button-height inline-label">
						<label class="label" for="reportType">Report Type</label>
						<select id="reportType" name="reportType" class="select multiple-as-single easy-multiple-selection check-list" >
							<option value="PN">PN</option>
							<option value="API">API</option>
						</select>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="reportUri">Report URI<span class="icon-star icon-size1 red"></span></label>
					<input type="text" name="reportUri" id="reportUri" style="width:200px" class="input full-width newReportName reset validate[required]" />
				</p>
				<p class="button-height inline-label">
						<label class="label" for="assessmentType">Assessment Name</label>
						<select id="assessmentType" name="assessmentType" class="select multiple-as-single easy-multiple-selection check-list auto-open" >
							<option value="101">TerraNova 3</option>
							<option value="102">TerraNova with PTCS</option>
							<option value="103">TerraNova with InView</option>
							<option value="104">Bible</option>
							<option value="105">TerraNova Longitudinal</option>
						</select>
				</p>
				
				<p class="button-height inline-label">
					<label class="label" for="reportStatusCheck">Status</label>
					<input type="checkbox" name="reportStatusCheck"  id="reportStatusCheck" class="statusButton switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
				</p>
				<p class="button-height inline-label">
						<label class="label" for="addUserRole">Roles</label>
						<select id="addUserRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list auto-open" multiple>
							<option value="ROLE_ACSI">ROLE_ACSI</option>
							<option value="ROLE_CTB">ROLE_CTB</option>
							<option value="ROLE_SCHOOL">ROLE_SCHOOL</option>
							<option value="ROLE_CLASS">ROLE_CLASS</option>
							<option value="ROLE_PARENT">ROLE_PARENT</option>
						</select>
				</p>
					<div id ="imgHolder"></div>	
			</form:form>
		</div>
	</div>
		
		
	</div>
</div>