<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

<hgroup id="main-title" class="thin">
	<h1><spring:message code="h1.manageReports" /></h1>
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
<div class="" style="background-color: #FFF">
		<div class="right-column">
				<div class="content-panel" style="padding-left: 0px; border-radius: 0px 8px 8px 8px">
					<div class="report-panel-content panel-content linen" style="height: 575px">
						
						<fmt:message var="mngRpt_addRpt" key="manage.reports.add"/>
						<c:if test="${not empty actionMap[mngRpt_addRpt]}">
							<div class="panel-control align-right">
								<a id="addDashboard" tenantName="" tenantId="" href="#" class="button glossy margin-left with-tooltip" title="<spring:message code="msg.addReport" />" style="box-shadow: grey -3px 4px 13px;">
									<span class="button-icon blue-gradient manage-btn"><span class="icon-plus-round"></span></span>
									<spring:message code="msg.addReport" />
								</a>
							</div>
						</c:if>
						
						<div class="panel-load-target with-padding margin10 padding-none height-mid">
							<div class="report-container1 tabs-content height-mid manage-report-container" style="padding-bottom: 20px;">
								<div class="panel-load-target scrollable with-padding margin10 height-mid padding-none">
								  <table class="simple-table responsive-table" id="report-list">
									<thead>
										<tr>
											<th scope="col" width="23%"><spring:message code="label.report" /></th>
											<th scope="col" width="13%"><spring:message code="table.label.status" /></th>
											<th scope="col" width="22%"><spring:message code="label.role" /></th>
											<th scope="col" width="20%"><spring:message code="label.menuName" /></th>
											<th scope="col" width="22%" class=""><spring:message code="table.label.actions" /></th>
										</tr>
									</thead>
									<tbody id ="reportDetails">
										<c:forEach var="report" items="${reportList}">
											<tr id="${report.reportId}_${report.reportId}">
												<th scope="row"><span class="reportName">${report.reportName}</span>
												<br>
													<small class="reportUrl">${report.reportUrl}</small></th>
												<td class="vertical-center">
											     <input type="hidden" class="reportDescription" name="reportDescription" value="${report.reportDescription}" />
											     <input type="hidden" class="reportType" name="reportType" id="reportType" value="${report.reportType}" />
											     <input type="hidden" class="linkName" name="linkName" id="linkName" value="${report.linkName}" />
											     <input type="hidden" class="allOrgNode" name="allOrgNode" id="allOrgNode" value="${report.allOrgNode}" />
													<c:if test="${report.enabled}">
														<small class="tag green-bg status"><spring:message code="label.enabled" /></small>
													</c:if>
													<c:if test="${not report.enabled}">
														<small class="tag red-bg status"><spring:message code="label.disabled" /></small>
													</c:if>
												</td>
												<td class="roleContainer vertical-center">
													<c:forEach var="role" items="${report.roles}">
														<c:if test="${role == 'ROLE_CTB'}">
														<small class="tag green-bg role ${role}">${role}</small><br/>
														</c:if>
														<c:if test="${role == 'ROLE_SUPER'}">
														<small class="tag blue-bg role ${role}">${role}</small><br/>
														</c:if>												
														<c:if test="${role == 'ROLE_PARENT'}">
														<small class="tag red-bg role ${role}">${role}</small><br/>
														</c:if>	
														<c:if test="${role == 'ROLE_ADMIN'}">
														<small class="tag orange-bg role ${role}">${role}</small><br />
														</c:if>
														<c:if test="${role == 'ROLE_GRW'}">
														<small class="tag grey-bg role ${role}">${role}</small><br />
														</c:if>
														<c:if test="${role == 'ROLE_USER'}">
														<small class="tag black-bg role ${role}">${role}</small><br />
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
                                                		
                                                		<fmt:message var="mngRpt_configureRptMsg" key="manage.reports.configureRptMsg"/>
														<c:if test="${not empty actionMap[mngRpt_configureRptMsg]}">
                                                			<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
																class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
                                                		</c:if>
                                                		
													</span>
												</td>
    											</c:when>
    											 <c:when test="${report.reportName=='Product Specific System Configuration'}">
                                                <td class="vertical-center" nowrap>
													<span class="button-group compact" width="50px">
														
														<fmt:message var="mngRpt_configureRptMsg" key="manage.reports.configureRptMsg"/>
														<c:if test="${not empty actionMap[mngRpt_configureRptMsg]}">
															<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
																class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
														</c:if>
													
													</span>
												</td>
    											</c:when>
												<c:otherwise>
												<td class="vertical-center" nowrap>
													<span class="button-group compact" width="50px">
														
														<fmt:message var="mngRpt_editRpt" key="manage.reports.edit"/>
														<c:if test="${not empty actionMap[mngRpt_editRpt]}">
															<a href="#"	class="button icon-pencil edit-report with-tooltip" title="Edit"
																reportId="${report.reportId}"></a>
															<input type="hidden" id="mngRpt_editRpt" value="true"/>
														</c:if>
														
														<fmt:message var="mngRpt_configureRptMsg" key="manage.reports.configureRptMsg"/>
														<c:if test="${not empty actionMap[mngRpt_configureRptMsg]}">
															<a href="getReportMessageFilter.do?reportId=${report.reportId}&reportName=${report.reportName}&reportUrl=${report.reportUrl}" 
																class="button icon-chat configure-report-message with-tooltip" title="Configure Report Message"></a>
															<input type="hidden" id="mngRpt_configureRptMsg" value="true"/>
														</c:if>
														
														<fmt:message var="mngRpt_deleteRpt" key="manage.reports.delete"/>
														<c:if test="${not empty mngRpt_deleteRpt}">
														<a href="#"	reportId="${report.reportId}" reportName="${report.reportName}" class="button icon-swap with-tooltip confirm edit-actions"
																title="Edit Actions"></a>
															<input type="hidden" id="mngRpt_editActions" value="true"/>
														</c:if>
														
														<fmt:message var="mngRpt_deleteRpt" key="manage.reports.delete"/>
														<c:if test="${not empty mngRpt_deleteRpt}">
															<a href="#"	reportId="${report.reportId}" reportName="${report.reportName}" class="button icon-trash with-tooltip confirm delete-Report"
																title="Delete"></a>
															<input type="hidden" id="mngRpt_deleteRpt" value="true"/>
														</c:if>
														
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
								<label class="label" for="reportName"><spring:message code="label.reportName" /><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportName" value="${report.reportName}" id="reportName" style="width:200px" class="input full-width newReportName reset validate[required]"/>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportDescription"><spring:message code="label.reportDescription" /> <span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportDescription" id="reportDescription" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
							<p style="width: 385px;"><small class="input-info"><spring:message code="info.report" /></small></p>
							<p class="button-height inline-label">
									<label class="label" for="editReportType"><spring:message code="label.reportType" /></label>
									<select id="editReportType" name="reportType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="API">API</option>
										<option value="API_LINK">API_LINK</option>
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
									<label class="label" for="editMenuType"><spring:message code="label.menuName" /></label>
									<select id="editMenuType" name="menuType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="101"><spring:message code="label.reports" /></option>
										<option value="102"><spring:message code="label.downloads" /></option>
										<option value="103"><spring:message code="menuName.content.rsc" /></option>
										<option value="104"><spring:message code="label.usefulLinks" /></option>
										<option value="105"><spring:message code="label.manage" /></option>
									</select>
							</p>
							<p class="button-height inline-label">
								<label for="reportSequence" class="label"><spring:message code="label.reportSequence" /></label>
								<span class="number input margin-right">
									<button type="button" class="button number-down">-</button>
									<input type="text" name="reportSequence" id="reportSequence" value="" class="input-unstyled">
									<button type="button" class="button number-up">+</button>
								</span>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportUrl"><spring:message code="label.reportURI" /><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportUrl" id="reportUrl" style="width:200px" class="input full-width newReportName reset validate[required]"/>
							</p>
							<p class="button-height inline-label">
									<label class="label" for="editCustomerType"><spring:message code="label.customerProductLink" /></label>
									<select id="editCustomerType" name="customerType" class="select multiple-as-single easy-multiple-selection check-list ">
										<c:forEach var="allCustomer" items="${allCustomer}">
											<option value="${allCustomer.value}">${allCustomer.name}</option>
										</c:forEach>
									</select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="editReportStatus"><spring:message code="table.label.status" /></label>
								<input type="checkbox" name="reportStatus" id="editReportStatus" class="switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
							</p>
							<p class="button-height inline-label">
								<label class="label" for="userRole"><spring:message code="table.label.roles" /><span class="icon-star icon-size1 red"></span></label>
								<select id="userRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple>
									<c:forEach var="allRoles" items="${allRoles}">
										<option value="${allRoles.name}">${allRoles.name}</option>
									</c:forEach>
								</select>
							</p>
							<p class="button-height inline-label">
									<label class="label" for="orgNodeLevel"><spring:message code="label.level" /><span class="icon-star icon-size1 red"></span></label>
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
							<input type="hidden" name="reportStatus" id="reportStatus" value="" />
							<p  style="width:329px"><span  id="imgHolder"></span></p>
							<p class="button-height inline-label">
								<label class="label" for="reportName"><spring:message code="label.reportName" /><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportName" id="reportName" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportDescription"><spring:message code="label.reportDescription" /><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportDescription" id="reportDescription" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
							<p style="width: 385px;"><small class="input-info"><spring:message code="info.report" /></small></p>
							<p class="button-height inline-label">
									<label class="label" for="reportType"><spring:message code="label.reportType" /></label>
									<select id="reportType" name="reportType" class="select multiple-as-single easy-multiple-selection check-list " >
										<option value="API">API</option>
										<option value="API_LINK">API_LINK</option>
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
									<label class="label" for="reportType"><spring:message code="label.menuName" /></label>
									<select id="menuType" name="menuType" class="select multiple-as-single easy-multiple-selection check-list" >
										<option value="101"><spring:message code="label.reports" /></option>
										<option value="102"><spring:message code="label.downloads" /></option>
										<option value="103"><spring:message code="menuName.content.rsc" /></option>
										<option value="104"><spring:message code="label.usefulLinks" /></option>
										<option value="105"><spring:message code="label.manage" /></option>
									</select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportUri"><spring:message code="label.reportURI" /><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="reportUri" id="reportUri" style="width:200px" class="input full-width newReportName reset validate[required]" />
							</p>
							<p class="button-height inline-label">
									<label class="label" for="customerType"><spring:message code="label.customerProductLink" /></label>
									<select id="customerType" name="customerType" class="select multiple-as-single easy-multiple-selection check-list ">
										<c:forEach var="allCustomer" items="${allCustomer}">
											<option value="${allCustomer.value}">${allCustomer.name}</option>
										</c:forEach>
									</select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="reportStatusCheck"><spring:message code="table.label.status" /></label>
								<input type="checkbox" name="reportStatusCheck"  id="reportStatusCheck" class="statusButton switch medium wide mid-margin-right" value="1" data-text-on="ENABLED" data-text-off="DISABLED">
							</p>
							<p class="button-height inline-label">
									<label class="label" for="addUserRole"><spring:message code="table.label.roles" /><span class="icon-star icon-size1 red"></span></label>
									<select id="addUserRole" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple >
										<c:forEach var="allRoles" items="${allRoles}">
											<option value="${allRoles.name}">${allRoles.name}</option>
										</c:forEach>
									</select>
							</p>
							<p class="button-height inline-label">
									<label class="label" for="allOrgNode"><spring:message code="label.level" /><span class="icon-star icon-size1 red"></span></label>
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
				<div id="editActions" class="display-none">
					<div class="">
						<form:form id="editActionsForm" name="editActionsForm" class="edit-action-form small-margin-top">
							<input type="hidden" name="reportIdForAction" id="reportIdForAction"/>
							<p class="button-height inline-label">
								<label class="label" for="reportNameForAction"><spring:message code="label.reportName" /></label>
								<input type="text" name="reportNameForAction" id="reportNameForAction" style="width:200px; border: none; background: none;" class="full-width newReportName" value="Student Roster" disabled="disabled" />
							</p>
							<p class="button-height inline-label">
								<label class="label" for="productForAction"><spring:message code="label.customerProductLink" /></label>
								<select id="productForAction" name="productForAction" class="select multiple-as-single easy-multiple-selection check-list "></select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="roleForAction"><spring:message code="table.label.roles" /></label>
								<select id="roleForAction" name="roleForAction" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple ></select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="levelForAction"><spring:message code="label.level" /></label>
								<select id="levelForAction" name="levelForAction" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple></select>
							</p>
							<p class="button-height inline-label">
								<label class="label" for="newAction">Actions</label>
								<select id="newAction" name="newAction" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list  validate[required]" multiple ></select>
							</p>
						</form:form>
					</div>
				</div>
			</div>
</div>