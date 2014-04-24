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
	.modal-content .inline-label label,  .modal-content .inline-label input  {
		display:inline-block !important;
	}
</style>
<![endif]-->
<style>
	.label {width: 160px !important}
</style>
<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">
		<div class="right-column">
				<div class="content-panel" style="padding-left: 0px; border-radius: 0px 8px 8px 8px">
					<div class="report-panel-content panel-content linen" style="height: 575px">

						<div class="panel-control align-right">
							<a id="addDashboard" tenantName="" tenantId="" href="#" class="button glossy margin-left" style="box-shadow: grey -3px 4px 13px;">
								<span class="button-icon blue-gradient manage-btn"><span class="icon-plus-round"></span></span>
								Add Report
							</a>
						</div>

						<div class="panel-load-target with-padding margin10 padding-none height-mid">
							<div class="report-container1 tabs-content height-mid manage-report-container" style="padding-bottom: 20px;">
								<div class="panel-load-target scrollable with-padding margin10 height-mid padding-none">
								  <table class="simple-table responsive-table" id="report-list">

									<thead>
										<tr>
											<th scope="col" width="23%">Report</th>
											<!--<th scope="col" width="15%">Date</th>-->
											<th scope="col" width="13%">Status</th>
											<th scope="col" width="22%">Role</th>
											<th scope="col" width="20%">Menu Name</th>
											<th scope="col" width="22%" class="">Actions</th>
										</tr>
									</thead>

									<tbody id ="reportDetails">
										<c:forEach var="report" items="${reportList}">
											<tr id="${report.reportId}_${report.reportId}">
												<th scope="row"><span class="reportName">${report.reportName}</span>
												<br>
													<small class="reportUrl">${report.reportUrl}</small></th>
												<!--<td class="vertical-center">Jul 5, 2011</td>-->
												<td class="vertical-center">
											     <input type="hidden" class="reportDescription" name="reportDescription" value="${report.reportDescription}" />
											     <input type="hidden" class="reportType" name="reportType" id="reportType" value="${report.reportType}" />
											     <input type="hidden" class="linkName" name="linkName" id="linkName" value="${report.linkName}" />
											     <input type="hidden" class="allOrgNode" name="allOrgNode" id="allOrgNode" value="${report.allOrgNode}" />
											     
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
														<c:if test="${role == 'ROLE_ADMIN'}">
															<small class="tag orange-bg role ${role}">${role}</small>
															<br />
														</c:if>
														<c:if test="${role == 'ROLE_USER'}">
															<small class="tag black-bg role ${role}">${role}</small>
															<br />
														</c:if>
													</c:forEach>
												</td>
												<td class="vertical-center">
													<h5 class="menu_name">${report.menuName}</h5>
													<input type="hidden" class="menuName menuid" name="menuName" value="${report.menuId}" />
												</td>
												<c:choose>
                                                <c:when test="${report.reportName=='Generic System Configuration'}">
                                                <td class="vertical-center" nowrap>
                                                <input type="hidden" class="scmCopyButtonHide" name="scmCopyButtonHide" value="scmCopyButtonHide" />
													<span class="button-group compact" width="50px">
														<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
															class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
													</span>
												</td>
    											</c:when>
    											 <c:when test="${report.reportName=='Product Specific System Configuration'}">
                                                <td class="vertical-center" nowrap>
													<span class="button-group compact" width="50px">
														<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
															class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
													</span>
												</td>
    											</c:when>
												<c:otherwise>
												<td class="vertical-center" nowrap>
													<span class="button-group compact" width="50px">
														<a href="#"	class="button icon-pencil edit-report with-tooltip" title="Edit"
															reportId="${report.reportId}"></a>
														<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
															class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
														<a href="#"	reportId="${report.reportId}" reportName="${report.reportName}" class="button icon-trash with-tooltip confirm delete-Report"
															title="Delete"></a>
													</span>
												</td>
      											</c:otherwise>
	                                            </c:choose>
											</tr>
										</c:forEach>
									</tbody>
								</table></div>
							</div>
						</div>
					</div>
				</div>
					
				<div id="editRole" class="display-none">
					<div class="">
					
						<form:form id="editReportForm" method="post" class="edit-report-form" action="updateReport.do">
							<input type="hidden" name="reportId" id="reportId"/>
							<p  style="width:329px"><span  id="imgHolder"></span></p>
							<p class="button-height inline-label">
								<label class="label" for="reportName">Report Name<span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportName" value="${report.reportName}" id="reportName" style="width:200px" class="input full-width newReportName reset validate[required]"/>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportDescription">Report Description <span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportDescription" id="reportDescription" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
							
							<p style="width: 385px;"><small class="input-info">Report Type should be aligned with report development (otherwise report may not be loaded properly). Type starting with 'PN' are for parent network reports.</small></p>
							<p class="button-height inline-label">
									<label class="label" for="editReportType">Report Type</label>
									<select id="editReportType" name="reportType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="API">API</option>
										<option value="API_TABLE">API TABLE</option>
										<option value="API_CUSTOM">API CUSTOM</option>
										<option value="API_NFCUSTOM">API NO FILTER CUSTOM</option>
										<option value="PN">PN</option>
										<option value="PN_TABLE">PN TABLE</option>
										<option value="PN_CUSTOM">PN CUSTOM</option>
										<option value="PN_NFCUSTOM">PN NO FILTER CUSTOM</option>
									</select>
							</p>
							
							<p class="button-height inline-label">
									<label class="label" for="editMenuType">Menu Name</label>
									<select id="editMenuType" name="menuType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="101">Reports</option>
										<option value="102">Downloads</option>
										<option value="103">Resources</option>
										<option value="104">Useful Links</option>
									</select>
							</p>
							
							<p class="button-height inline-label">
								<label class="label" for="reportUrl">Report URI<span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportUrl" id="reportUrl" style="width:200px" class="input full-width newReportName reset validate[required]"/>
							</p>
							
							<p class="button-height inline-label">
									<label class="label" for="editCustomerType">Customer Product Link</label>
									<select id="editCustomerType" name="customerType" class="select multiple-as-single easy-multiple-selection check-list ">
										<c:forEach var="allCustomer" items="${allCustomer}">
											<option value="${allCustomer.value}">${allCustomer.name}</option>
										</c:forEach>
									</select>
							</p>

							<p class="button-height inline-label">
								<label class="label" for="editReportStatus">Status</label>
								<input type="checkbox" name="reportStatus" id="editReportStatus" class="switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
							</p>

							<p class="button-height inline-label">
								<label class="label" for="userRole">Roles<span class="icon-star icon-size1 red"></span></label>
								<select id="userRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple>
									<c:forEach var="allRoles" items="${allRoles}">
										<option value="${allRoles.name}">${allRoles.name}</option>
									</c:forEach>
								</select>
							</p>
							
							
							<p class="button-height inline-label">
									<label class="label" for="orgNodeLevel">Level<span class="icon-star icon-size1 red"></span></label>
									<select id="orgNodeLevel" name="allOrgNode" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple>
										<c:forEach var="allOrgNode" items="${allOrgNode}">
											<option value="${allOrgNode.value}">${allOrgNode.name}</option>
										</c:forEach>
									</select>
							</p>
							<input type="hidden" name="allOrgNode" id="allOrgNode" value="${report.allOrgNode}" />
														
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
								<label class="label" for="reportDescription">Report Description<span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportDescription" id="reportDescription" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
						
							<p style="width: 385px;"><small class="input-info">Report Type should be aligned with report development (otherwise report may not be loaded properly). Type starting with 'PN' are for parent network reports.</small></p>
							<p class="button-height inline-label">
									<label class="label" for="reportType">Report Type</label>
									<select id="reportType" name="reportType" class="select multiple-as-single easy-multiple-selection check-list " >
										<option value="API">API</option>
										<option value="API_TABLE">API TABLE</option>
										<option value="API_CUSTOM">API CUSTOM</option>
										<option value="API_NFCUSTOM">API NO FILTER CUSTOM</option>
										<option value="PN">PN</option>
										<option value="PN_TABLE">PN TABLE</option>
										<option value="PN_CUSTOM">PN CUSTOM</option>
										<option value="PN_NFCUSTOM">PN NO FILTER CUSTOM</option>
									</select>
							</p>
							
							<p class="button-height inline-label">
									<label class="label" for="reportType">Menu Name</label>
									<select id="menuType" name="menuType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="101">Reports</option>
										<option value="102">Downloads</option>
										<option value="103">Resources</option>
										<option value="104">Useful Links</option>
									</select>
							</p>
							
							<p class="button-height inline-label">
								<label class="label" for="reportUri">Report URI<span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportUri" id="reportUri" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
													
							<p class="button-height inline-label">
									<label class="label" for="customerType">Customer Product Link</label>
									<select id="customerType" name="customerType" class="select multiple-as-single easy-multiple-selection check-list ">
										<c:forEach var="allCustomer" items="${allCustomer}">
											<option value="${allCustomer.value}">${allCustomer.name}</option>
										</c:forEach>
									</select>
							</p>
							
							
							<p class="button-height inline-label">
								<label class="label" for="reportStatusCheck">Status</label>
								<input type="checkbox" name="reportStatusCheck"  id="reportStatusCheck" class="statusButton switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
							</p>
							<p class="button-height inline-label">
									<label class="label" for="addUserRole">Roles<span class="icon-star icon-size1 red"></span></label>
									<select id="addUserRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple >
										<c:forEach var="allRoles" items="${allRoles}">
											<option value="${allRoles.name}">${allRoles.name}</option>
										</c:forEach>
									</select>
							</p>
							
							<p class="button-height inline-label">
									<label class="label" for="allOrgNode">Level<span class="icon-star icon-size1 red"></span></label>
									<select id="allOrgNode" name="allOrgNode" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple>
									                                                                     
										<c:forEach var="allOrgNode" items="${allOrgNode}">
											<option value="${allOrgNode.value}">${allOrgNode.name}</option>
										</c:forEach>
									</select>
							</p>
							
							
							
								<div id ="imgHolder"></div>	
						</form:form>
						<%@ include file="../common/required.jsp" %>
					</div>
				</div>
			</div>

</div>