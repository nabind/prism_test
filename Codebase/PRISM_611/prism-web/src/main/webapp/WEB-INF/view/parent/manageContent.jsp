<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<spring:theme code="theme.name" var="themeName"/>
<div class="content-panel" style="padding-left: 0px; border: 0px">
	<hgroup id="main-title" class="thin">
		<h1><spring:message code="label.pn.manageContent"/></h1>
	</hgroup>
	<div class="right-column">
		<div class="content-panel" style="padding-left: 0px; border-radius: 0px 8px 8px 8px">
			<div class="report-panel-content panel-content linen">
				<input type="hidden" name="Tab" id="manageContentTab" value="manageContentTab">	
				<input type="hidden" name="purpose" id="purpose" value="manageContent" />				
				<div class="scrollable manage-message" style="min-height: 585px; color: #666">
					<div class="panel-control panel-control-report align-right padding-right"></div>
					<div class="panel-load-target scrollable with-padding report-layout padding-none">
						<form:form method="post" class="report-form" id="manageContentSearchForm">
							<div class="mid-margin-bottom">
								<div class="reportFilterCriteria1 panel-control rounded-border">
									<span id="filter-icon" class="icon-leaf icon-size2"></span> 
									<b><spring:message code="label.pn.manageContent"/></b>
								</div>
								<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
									<div class="cyan-gradient icholder rounded-border-bottom" style="border-bottom: 1px solid #CCC;">
										<div id="refresh-content" style="position: absolute; top: 5px; right: 20px; display: none;">
											<a href="javascript:void(0)" id="searchContent" class="button blue-gradient glossy"><spring:message code="button.content.search"/></a>
										</div>
										<div class="with-mid-padding mid-margin-bottom icholderinner" style="min-width: 200px; overflow-x: auto">
											<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer" style="height: 10px;">
												<div class="three-columns report-inputs" style="width: 15%">
													<h6 class="margin-bottom-small">
														<spring:message code="label.testAdministration"/>
														<span class="icon-star icon-size1 red"></span>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="custProdIdManageContent" name="custProdId" class="select navy-gradient expandable-list" style="width: 150px;">
																<option value='-1'><spring:message code="dropDown.default.text"/></option>
																<c:forEach var="customerProductTO" items="${serviceMapManageContentFilter.customerProductList}">
																	<option value="${customerProductTO.value}">${customerProductTO.name}</option>
																</c:forEach>
															</select>
														</p>
													</div>
												</div>
											</div>
											<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer" style="height: 10px;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.grade"/>:
														<span class="icon-star icon-size1 red"></span>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="gradeIdManageContent" name="gradeId" class="select navy-gradient expandable-list" style="width: 150px;">
																<option value='-1'><spring:message code="dropDown.default.text"/></option>
															</select>
														</p>
													</div>
												</div>
											</div>
											<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer" style="height: 10px;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.subtest"/>
														<span class="icon-star icon-size1 red"></span>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="subtestIdManageContent" name="subtestId" class="select navy-gradient expandable-list" style="width: 150px;">
																<option value='-1'><spring:message code="dropDown.default.text"/></option>
															</select>
														</p>
													</div>
												</div>
											</div>
											
											<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer" style="height: 10px;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.objective"/>
														<span class="icon-star icon-size1 red"></span>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="objectiveIdManageContent" name="objectiveId" class="select navy-gradient expandable-list" style="width: 150px;">
																<option value='-1'><spring:message code="dropDown.default.text"/></option>
															</select>
														</p>
													</div>
												</div>
											</div>
											
											<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.content.type"/>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="contentTypeIdManageContent" name="contentTypeId"
																class="select navy-gradient expandable-list"
																style="width: 150px;">
																<c:choose>
  																	<c:when test="${fn:contains(themeName, 'usmo')}" >
  																		<option value='<spring:message code="val.contentType.spl"/>'><spring:message code="name.contentType.spl"/></option>
  																		<option value='<spring:message code="val.contentType.opl"/>'><spring:message code="name.contentType.opl"/></option> 
  																	</c:when>
  																	<c:otherwise>
																		<option value='<spring:message code="val.contentType.act"/>'><spring:message code="name.contentType.act"/></option>
																		<option value='<spring:message code="val.contentType.ind"/>'><spring:message code="name.contentType.ind"/></option>
																		<option value='<spring:message code="val.contentType.std"/>'><spring:message code="label.objective"/></option>
																		<option value='<spring:message code="val.contentType.rsc"/>'><spring:message code="name.contentType.rsc"/></option>
																		<option value='<spring:message code="val.contentType.eda"/>'><spring:message code="name.contentType.eda"/></option>
																		<option value='<spring:message code="val.contentType.att"/>'><spring:message code="name.contentType.att"/></option>
																		<option value='<spring:message code="val.contentType.rbs"/>'><spring:message code="name.contentType.rbs"/></option>
																		<option value='<spring:message code="val.contentType.oar"/>'><spring:message code="name.contentType.oar"/></option>
																	</c:otherwise>
																</c:choose>
															</select>
														</p>
													</div>
												</div>
											</div>
											<div id="div_performanceLevel" class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px; display: none;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.content.performanceLevel"/>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="performanceLevelIdManageContent" name="performanceLevelId"
																class="select navy-gradient expandable-list"
																style="width: 150px;">
																<c:choose>
  																	<c:when test="${fn:contains(themeName, 'usmo')}" >
  																		<option value='<spring:message code="val.pl.adv"/>'><spring:message code="name.pl.adv"/></option>
  																		<option value='<spring:message code="val.pl.prf"/>'><spring:message code="name.pl.prf"/></option>
  																		<option value='<spring:message code="val.pl.bsc"/>'><spring:message code="name.pl.bsc"/></option>
  																		<option value='<spring:message code="val.pl.blb"/>'><spring:message code="name.pl.blb"/></option>
  																		<option value='<spring:message code="val.pl.lnd"/>'><spring:message code="name.pl.lnd"/></option>
  																	</c:when>
  																	<c:otherwise>
																		<option value='<spring:message code="val.pl.psp"/>'><spring:message code="name.pl.psp"/></option>
																		<option value='<spring:message code="val.pl.pas"/>'><spring:message code="name.pl.pas"/></option>
																		<option value='<spring:message code="val.pl.dnp"/>'><spring:message code="name.pl.dnp"/></option>
																		<option value='<spring:message code="val.pl.und"/>'><spring:message code="name.pl.und"/></option>
																		<option value='<spring:message code="val.pl.pri"/>'><spring:message code="name.pl.pri"/></option>
																		<option value='<spring:message code="val.pl.dnr"/>'><spring:message code="name.pl.dnr"/></option>
																	</c:otherwise>
																</c:choose>
															</select>
														</p>
													</div>
												</div>
											</div>
											<div id="div_performanceLevel_opl" class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px; display: none;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.content.performanceLevel"/>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="performanceLevelIdManageContentOpl" name="performanceLevelId"
																class="select navy-gradient expandable-list"
																style="width: 150px;">
																<c:choose>
  																	<c:when test="${fn:contains(themeName, 'usmo')}" >
  																		<option value='<spring:message code="val.pl.prf"/>'><spring:message code="name.pl.asd"/></option>
  																		<option value='<spring:message code="val.pl.bsc"/>'><spring:message code="name.pl.ans"/></option>
  																		<option value='<spring:message code="val.pl.blb"/>'><spring:message code="name.pl.bls"/></option>
  																		<option value='<spring:message code="val.pl.lnd"/>'><spring:message code="name.pl.lnd"/></option>
  																	</c:when>
  																	<c:otherwise>
																		<option value='<spring:message code="val.pl.psp"/>'><spring:message code="name.pl.psp"/></option>
																		<option value='<spring:message code="val.pl.pas"/>'><spring:message code="name.pl.pas"/></option>
																		<option value='<spring:message code="val.pl.dnp"/>'><spring:message code="name.pl.dnp"/></option>
																		<option value='<spring:message code="val.pl.und"/>'><spring:message code="name.pl.und"/></option>
																		<option value='<spring:message code="val.pl.pri"/>'><spring:message code="name.pl.pri"/></option>
																		<option value='<spring:message code="val.pl.dnr"/>'><spring:message code="name.pl.dnr"/></option>
																	</c:otherwise>
																</c:choose>
															</select>
														</p>
													</div>
												</div>
											</div>
											<div id="div_statusCode" class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer" style="height: 10px; display: none;">
												<div class="three-columns report-inputs" style="width: 15%; margin-bottom: 20px !important;">
													<h6 class="margin-bottom-small">
														<spring:message code="label.content.statusCode"/>
													</h6>
													<div class="float-left margin-right margin-bottom">
														<p class="button-height">
															<select id="statusCodeIdManageContent" name="statusCodeId" class="select navy-gradient expandable-list" style="width: 150px;">
																<option value='<spring:message code="val.sc.bnk"/>'><spring:message code="name.sc.bnk"/></option>
																<option value='<spring:message code="val.sc.tnt"/>'><spring:message code="name.sc.tnt"/></option>
																<option value='<spring:message code="val.sc.ibs"/>'><spring:message code="name.sc.ibs"/></option>
																<option value='<spring:message code="val.sc.spi"/>'><spring:message code="name.sc.spi"/></option>
																<option value='<spring:message code="val.sc.tiv"/>'><spring:message code="name.sc.tiv"/></option>
																<option value='<spring:message code="val.sc.tii"/>'><spring:message code="name.sc.tii"/></option>
															</select>
														</p>
													</div>
												</div>
											</div>
										</div>
										<%@ include file="../common/required.jsp" %>
									</div>
								</div>
								</div>
							</form:form>
						</div>
						<div class="panel-content linen">
							<div class="panel-control align-right">
								<sec:authorize access="!hasAnyRole('ROLE_SSO')">
									
									<fmt:message var="MANAGE_CONTENT_ADD" key="manage.content.add"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_ADD]}">
										<div id="addContentDiv" style="display: none;" >
											<a id="addContent" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.content.add"/>
											</a>
										</div>
									</c:if>
									
									<fmt:message var="MANAGE_CONTENT_STANDARD" key="manage.content.standard"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_STANDARD]}">
										<div id="modifyStandardDiv" style="display: none;" >
											<a id="modifyStandardButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.standard"/>
											</a>
										</div>
									</c:if>
									
									<fmt:message var="MANAGE_CONTENT_RSC" key="manage.content.rsc"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_RSC]}">
										<div id="modifyRscDiv" style="display: none;" >
											<a id="modifyRscButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.rsc"/>
											</a>
										</div>
									</c:if>
									
									<fmt:message var="MANAGE_CONTENT_EDA" key="manage.content.eda"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_EDA]}">
										<div id="modifyEdaDiv" style="display: none;" >
											<a id="modifyEdaButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.eda"/>
											</a>
										</div>
									</c:if>
										
									<fmt:message var="MANAGE_CONTENT_ATT" key="manage.content.att"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_ATT]}">
										<div id="modifyAttDiv" style="display: none;" >
											<a id="modifyAttButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.att"/>
											</a>
										</div>
									</c:if>										
									
									<fmt:message var="MANAGE_CONTENT_RBS" key="manage.content.rbs"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_RBS]}">
										<div id="modifyRbsDiv" style="display: none;" >
											<a id="modifyRbsButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.rbs"/>
											</a>
										</div>
									</c:if>
										
									<fmt:message var="MANAGE_CONTENT_OAR" key="manage.content.oar"  />
									<c:if test="${not empty actionMap[MANAGE_CONTENT_OAR]}">
										<div id="modifyOarDiv" style="display: none;" >
											<a id="modifyOarButton" href="#" class="button glossy margin-left">
												<span class="button-icon blue-gradient manage-btn"> 
													<span class="icon-page-list"></span>
												</span> 
												<spring:message code="button.modify.oar"/>
											</a>
										</div>
									</c:if>
									
									<div id="modifySplDiv" style="display: none;" >
										<a id="modifySplButton" href="#" class="button glossy margin-left">
											<span class="button-icon blue-gradient manage-btn"> 
												<span class="icon-page-list"></span>
											</span> 
											<spring:message code="button.modify.spl"/>
										</a>
									</div>
									
									<div id="modifyOplDiv" style="display: none;" >
										<a id="modifyOplButton" href="#" class="button glossy margin-left">
											<span class="button-icon blue-gradient manage-btn"> 
												<span class="icon-page-list"></span>
											</span> 
											<spring:message code="button.modify.opl"/>
										</a>
									</div>
								
								</sec:authorize>
							</div>
							<!-- Manage Content table Start -->
							<div id="contentTableDiv" style="display: none;" class="panel-load-target with-padding margin10 height-mid padding-none">
								<div id="moreDiv" style="display: none;" class="pagination panel-control margin-bottom-small rounded-border">
									<a href="#nogo" id="moreContents" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title='<spring:message code="title.content.more"/>'>
										<spring:message code="button.content.more"/>
									</a>
								</div>
								<div id="contentTable" class="report-container tabs-content padding-small" style="height: 450px">
									<div id="last_msg_loader" height="140" style="position: relative; left: 0px; z-index: 1"></div>
									<input type="hidden" name="lastid" id="lastid" value="">
									
									<fmt:message var="MANAGE_CONTENT_ADD" key="manage.content.add"  />
									<input type="hidden" id="MANAGE_CONTENT_ADD" value="${actionMap[MANAGE_CONTENT_ADD]}" />
									
									<fmt:message var="MANAGE_CONTENT_EDIT" key="manage.content.edit"  />
									<input type="hidden" id="MANAGE_CONTENT_EDIT" value="${actionMap[MANAGE_CONTENT_EDIT]}" />
									
									<fmt:message var="MANAGE_CONTENT_DELETE" key="manage.content.delete"  />
									<input type="hidden" id="MANAGE_CONTENT_DELETE" value="${actionMap[MANAGE_CONTENT_DELETE]}" />
									
									<fmt:message var="MANAGE_CONTENT_MORE" key="manage.content.more"  />
									<input type="hidden" id="MANAGE_CONTENT_MORE" value="${actionMap[MANAGE_CONTENT_MORE]}" />
									
									<fmt:message var="MANAGE_CONTENT_STANDARD" key="manage.content.standard"  />
									<input type="hidden" id="MANAGE_CONTENT_STANDARD" value="${actionMap[MANAGE_CONTENT_STANDARD]}" />
									
									<fmt:message var="MANAGE_CONTENT_RSC" key="manage.content.rsc"  />
									<input type="hidden" id="MANAGE_CONTENT_RSC" value="${actionMap[MANAGE_CONTENT_RSC]}" />
									
									<fmt:message var="MANAGE_CONTENT_EDA" key="manage.content.eda"  />
									<input type="hidden" id="MANAGE_CONTENT_EDA" value="${actionMap[MANAGE_CONTENT_EDA]}" />
									
									<fmt:message var="MANAGE_CONTENT_ATT" key="manage.content.att"  />
									<input type="hidden" id="MANAGE_CONTENT_ATT" value="${actionMap[MANAGE_CONTENT_ATT]}" />
									
									<fmt:message var="MANAGE_CONTENT_RBS" key="manage.content.rbs"  />
									<input type="hidden" id="MANAGE_CONTENT_RBS" value="${actionMap[MANAGE_CONTENT_RBS]}" />
									
									<fmt:message var="MANAGE_CONTENT_OAR" key="manage.content.oar"  />
									<input type="hidden" id="MANAGE_CONTENT_OAR" value="${actionMap[MANAGE_CONTENT_OAR]}" />
									
									
									<table class="simple-table responsive-table" id="report-list">
										<thead>
											<tr class="abc">
												<th scope="col" width="20%"><spring:message code="label.content.name"/></th>
												<th scope="col" width="25%"><spring:message code="label.content.subHeader"/></th>
												<th scope="col" width="22%"><spring:message code="label.content.grade"/></th>
												<th scope="col" width="20%"><spring:message code="table.columnName.action"/></th>
											</tr>
										</thead>
										<tbody id="content_details">
											<tr class="abc content-list-all" id="sample">
												<th scope="row">&nbsp;</th>
												<th scope="row">&nbsp;</th>
												<th scope="row">&nbsp;</th>
												<th scope="row">&nbsp;</th>
												<th scope="row">&nbsp;</th>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- Manage Content table End -->
							<!-- Manage Content add start -->
							<div id="addContentModal" class="display-none">
								<div class="">
									<form:form id="addNewContent" name="addNewContent" class="add-User-form small-margin-top">
										<input type="hidden" id="custProdId" name="custProdId"/>
										<input type="hidden" id="gradeId" name="gradeId"/>
										<input type="hidden" id="subtestId" name="subtestId"/>
										<input type="hidden" id="objectiveId" name="objectiveId"/>
										<input type="hidden" id="contentTypeName" name="contentTypeName"/>
										<input type="hidden" id="contentType" name="contentType"/>
										<p class="button-height inline-label">
											<label class="label" for="contentName"><spring:message code="label.content.name"/><span class="icon-star icon-size1 red"></span></label> 
											<input type="text" name="contentName" id="contentName" rel="contentName" style="width: 200px" class="input full-width newReportName validate[required,maxSize[250]]" />
										</p>
										<p style="width: 329px" id="imgHolderContainer">
											<span id="imgHolder"></span>
										</p>
										<p class="button-height inline-label">
											<label class="label" for="subHeader">
												<spring:message code="label.content.subHeader"/>
											</label> 
											<input type="text" name="subHeader" id="subHeader" style="width: 200px" class="input full-width newReportName" />
										</p>
				                        <div class="mandatoryDescription message small-margin-bottom red-gradient customError" style="display:none">
				                        	<spring:message code="error.field.required"/>
				                        </div>
										<p class="button-height inline-label">
											<fieldset class="fieldset" style="overflow:auto;min-height:200px;">
												<legend class="legend" style="padding-left:5px" for="contentDescriptionEditor">
													<spring:message code="label.content.description"/>
													<span class="icon-star icon-size1 red"></span>
												</legend>
												<input type="hidden" id="contentDescription" name="contentDescription" class="validate[required]"/>
												<textarea id="contentDescriptionEditor" class="manage-content-textarea validate[required]">
													<spring:message code="textarea.content.defaultText"/>
												</textarea>
											</fieldset>
										</p>
										<div id="imgHolder"></div>
									</form:form>
								</div>
								<%@ include file="../common/required.jsp"%>
							</div>
							<!-- Manage Content add end -->
							<!-- Manage Content edit start -->
							<div id="editContentModal" class="display-none">
								<div class="">
									<form:form id="editContent" name="editContent" class="add-User-form small-margin-top">
										<input type="hidden" id="contentId" name="contentId"/>
										<p class="button-height inline-label">
											<label class="label" for="contentName"><spring:message code="label.content.name"/><span class="icon-star icon-size1 red"></span></label> 
											<input type="text" name="contentName" id="contentName" rel="contentName" style="width: 200px" class="input full-width newReportName validate[required,maxSize[250]]" />
										</p>
										<p style="width: 329px" id="imgHolderContainer">
											<span id="imgHolder"></span>
										</p>
										<p class="button-height inline-label">
											<label class="label" for="subHeader"><spring:message code="label.content.subHeader"/></label> 
											<input type="text" name="subHeader" id="subHeader" style="width: 200px" class="input full-width newReportName" />
										</p>
				                       	<div class="mandatoryDescription message small-margin-bottom red-gradient customError" style="display:none">
				                       		<spring:message code="error.field.required"/>
				                       	</div>
										<p class="button-height inline-label">
											<fieldset class="fieldset" style="overflow:auto;min-height:200px;">
												<legend class="legend" style="padding-left:5px" for="contentDescriptionEditor">
													<spring:message code="label.content.description"/>
													<span class="icon-star icon-size1 red"></span>
												</legend>
												<input type="hidden" id="contentDescription" name="contentDescription" class="validate[required]"/>
												<textarea id="contentDescriptionEditorEdit" class="manage-content-textarea validate[required]">
													<spring:message code="textarea.content.defaultText"/>
												</textarea>
											</fieldset>
										</p>
										<div id="imgHolder"></div>
									</form:form>
								</div>
								<%@ include file="../common/required.jsp"%>
							</div>
							<!-- Manage Content edit end -->
							<!-- As Standard/Objective is dependent upon Test Administration, so the code is blocked by Joy -->
							<!-- Modify Everyday Activity and About the Test - start -->
							<div id="modifyGenericModal" class="display-none">
								<div class="">
									<form:form id="modifyGenericForm" name="modifyGenericForm" class="add-User-form small-margin-top">
										<input type="hidden" id="custProdId" name="custProdId"/>
										<input type="hidden" id="gradeId" name="gradeId"/>
										<input type="hidden" id="subtestId" name="subtestId"/>
										<input type="hidden" id="objectiveId" name="objectiveId"/>
										<input type="hidden" id="contentTypeName" name="contentTypeName"/>
										<input type="hidden" id="contentType" name="contentType"/>
										<input type="hidden" id="performanceLevel" name="performanceLevel"/>
										<input type="hidden" id="objectiveDesc" name="objectiveDesc"/>
										<input type="hidden" id="statusCode" name="statusCode"/>
										<p class="button-height inline-label">
											<label class="label" style="width: 150px;"><spring:message code="label.testAdministration"/></label> 
											<span id="testAdministrationText"></span>
										</p>
										<p class="button-height inline-label">
											<label class="label" style="width: 150px;"><spring:message code="label.grade"/>:</label> 
											<span id="gradeText"></span>
										</p>
										<p id="p_subtest" class="button-height inline-label" style="display:none;">
											<label class="label" style="width: 150px;"><spring:message code="label.subtest"/></label> 
											<span id="subtestText"></span>
										</p>
										<p id="p_performanceLevel" class="button-height inline-label" style="display:none;">
											<label class="label" style="width: 150px;"><spring:message code="label.content.performanceLevel"/></label> 
											<span id="performanceLevelText"></span>
										</p>
										<p id="p_statusCode" class="button-height inline-label" style="display:none;">
											<label class="label" style="width: 150px;"><spring:message code="label.content.statusCode"/></label> 
											<span id="statusCodeText"></span>
										</p>
										<p id="p_objective" class="button-height inline-label" style="display:none;">
											<label class="label" style="width: 150px;"><spring:message code="label.objective"/></label> 
											<span id="objectiveText"></span>
										</p>
										<div class="mandatoryDescription message small-margin-bottom red-gradient customError" style="display:none">
				                       		<spring:message code="error.field.required"/>
				                       	</div>
										<p class="button-height inline-label">
											<fieldset class="fieldset" style="min-height:300px;">
												<legend class="legend" style="padding-left:5px">
													<spring:message code="label.content.description"/>
													<span class="icon-star icon-size1 red"></span>
												</legend>
												<input type="hidden" id="contentDescription" name="contentDescription" class="validate[required]"/>
												<textarea id="genericDescriptionEditor" class="manage-content-textarea validate[required]">
														<spring:message code="textarea.content.defaultText"/>
												</textarea>
											</fieldset>
										</p>
										<div id="imgHolder"></div>
									</form:form>
								</div>
								<%@ include file="../common/required.jsp"%>
							</div>
							<!-- Modify Everyday Activity and About the Test - end -->
							<div id="loader" class="display-none"></div>
						</div>
					</div>
				</div>
			</div>
	</div>
</div>
