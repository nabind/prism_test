<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<style>
.contentDescriptionEditorClass {
	z-index: 999301 !important;
}
</style>

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
		<div
			class="panel-control panel-control-report align-right padding-right"></div>
		<div
			class="panel-load-target scrollable with-padding report-layout padding-none">
			<form:form method="post" class="report-form" id="manageContentSearchForm">
				<div class="mid-margin-bottom">
					<div class="reportFilterCriteria1 panel-control rounded-border">
						<span id="filter-icon" class="icon-leaf icon-size2"></span> 
						<b><spring:message code="label.pn.manageContent"/></b>
					</div>
					<div class="cyan-gradient icholder rounded-border-bottom"
						style="border-bottom: 1px solid #CCC;">
						<div class="refresh-report"
							style="position: absolute; top: 5px; right: 20px;">
							<a href="javascript:void(0)" id="searchEduCenterUsers"
								class="button blue-gradient glossy">Search</a> 
						</div>
						<div class="with-mid-padding mid-margin-bottom icholderinner"
							style="min-width: 200px; overflow-x: auto">
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px;">
								<div class="three-columns report-inputs" style="width: 15%">
									<h6 class="margin-bottom-small"><spring:message code="label.testAdministration"/></h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="custProdIdManageContent" name="custProdId"
												class="select navy-gradient expandable-list"
												style="width: 150px;">
												<option value='0'>Please Select</option>
												<c:forEach var="customerProductTO"
													items="${serviceMapManageContentFilter.customerProductList}">
													<option value="${customerProductTO.value}">${customerProductTO.name}</option>
												</c:forEach>
											</select>
										</p>
									</div>
								</div>
							</div>
							
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px;">
								<div class="three-columns report-inputs" style="width: 15%">
									<h6 class="margin-bottom-small"><spring:message code="label.grade"/></h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="gradeIdManageContent" name="gradeId"
												class="select navy-gradient expandable-list"
												style="width: 150px;">
											</select>
										</p>
									</div>
								</div>
							</div>
						
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px;">
								<div class="three-columns report-inputs" style="width: 15%">
									<h6 class="margin-bottom-small"><spring:message code="label.subtest"/></h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="subtestIdManageContent" name="subtestId"
												class="select navy-gradient expandable-list"
												style="width: 150px;">
											</select>
										</p>
									</div>
								</div>
							</div>
						
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px;">
								<div class="three-columns report-inputs" style="width: 15%">
									<h6 class="margin-bottom-small"><spring:message code="label.objective"/></h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="objectiveIdManageContent" name="objectiveId"
												class="select navy-gradient expandable-list"
												style="width: 150px;">
											</select>
										</p>
									</div>
								</div>
							</div>
						
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px;">
								<div class="three-columns report-inputs" style="width: 15%">
									<h6 class="margin-bottom-small"><spring:message code="label.content.type"/></h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="contentTypeIdManageContent" name="contentTypeId"
												class="select navy-gradient expandable-list"
												style="width: 150px;">
												<option value='<spring:message code="val.contentType.act"/>'><spring:message code="name.contentType.act"/></option>
												<option value='<spring:message code="val.contentType.ind"/>'><spring:message code="name.contentType.ind"/></option>
											</select>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	
		<div class="panel-content linen">
		
		<!-- TODO for manage Content table Start -->			
			<div class="panel-control align-right">
				<span class="input search-input display-none"> 
				<input type="text"
					name="searchUser" id="searchUser"
					class="input-unstyled with-tooltip"
					title="Search Users by User ID OR <br/>Full Name (Last Name OR First Name)"
					placeholder="Search"> 
				<a href="javascript:void(0)"
					class="button icon-search compact" id="search_icon"
					param="search_icon_user"></a>
				</span>

				<sec:authorize ifNotGranted="ROLE_SSO">
					<a id="addContent" href="#" class="button glossy margin-left">
						<span class="button-icon blue-gradient manage-btn"> <span
							class="icon-add-user"></span>
					</span> <spring:message code="button.content.add"/>
					</a>
				</sec:authorize>

			</div>

			<div
				class="panel-load-target with-padding margin10 height-mid padding-none display-none">
				<div
					class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreUser"
						class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip"
						title="Display more users">More</a>
				</div>
				<div id="userTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
					<div id="last_msg_loader" height="140"
						style="position: relative; left: 0px; z-index: 1"></div>
					<input type="hidden" name="last_user_tenant"
						id="last_user_tenant" value="">
					<table class="simple-table responsive-table" id="report-list">

						<thead>
							<tr class="abc">
								<th scope="col" width="16%">User ID</th>
								<th scope="col" width="25%">Full Name</th>
								<th scope="col" width="11%" class="hide-on-tablet">Status</th>
								<th scope="col" width="16%" class="hide-on-tablet-portrait">Education
									Center Name</th>
								<th scope="col" width="16%">User Roles</th>
								<th scope="col" width="16%">Actions</th>
							</tr>
						</thead>

						<tbody id="user_details">
							<tr class="abc users-list-all" id="sample">
								<th scope="row">&nbsp;</th>
								<th scope="row">&nbsp;</th>
								<th scope="row" class="hide-on-tablet">&nbsp;</th>
								<th scope="row" class="hide-on-tablet-portrait">&nbsp;</th>
								<th scope="row">&nbsp;</th>
								<th scope="row">&nbsp;</th>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- TODO for manage Content table End -->

			<div id="addContentModal" class="display-none">
				<div class="">
					<form:form id="addNewContent" name="addNewContent"
						class="add-User-form small-margin-top">
						
						<input type="hidden" id="custProdId" name="custProdId"/>
						<input type="hidden" id="gradeId" name="gradeId"/>
						<input type="hidden" id="subtestId" name="subtestId"/>
						<input type="hidden" id="objectiveId" name="objectiveId"/>
						<input type="hidden" id="contentTypeName" name="contentTypeName"/>
						<input type="hidden" id="contentType" name="contentType"/>
						
						<p class="button-height inline-label">
							<label class="label" for="contentName"><spring:message code="label.content.name"/><span
								class="icon-star icon-size1 red"></span></label> <input type="text"
								name="contentName" id="contentName" rel="contentName" style="width: 200px"
								class="input full-width newReportName validate[required,custom[onlyLetterNumber],maxSize[30],minSize[3]]" />
						</p>
						
						<p style="width: 329px" id="imgHolderContainer">
							<span id="imgHolder"></span>
						</p>
						
						<p class="button-height inline-label">
							<label class="label" for="subHeader"><spring:message code="label.content.subHeader"/><span
								class="icon-star icon-size1 red"></span></label> 
								<input type="text"
								name="subHeader" id="subHeader" style="width: 200px"
								class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
						</p>

						<p class="button-height inline-label">
							<fieldset class="fieldset" style="height: 200px">
								<legend class="legend" style="padding-left:0px" for="contentDescriptionEditor">
									<spring:message code="label.content.description"/>
								</legend>
								<input type="hidden"
									id="contentDescription"
									name="contentDescription"/>
								<div class="new-row contentDescriptionEditorClass">
									<textarea
										id="contentDescriptionEditor"
										class="manage-content-textarea validate[required]">
									</textarea>
								</div>
							</fieldset>
						</p>
						
						<p class="button-height inline-label">
							<label class="label" for="subHeader"><spring:message code="label.content.profLevel"/><span
								class="icon-star icon-size1 red"></span></label> 
								<select id="profLevel" name="profLevel"
												class="select expandable-list"
												style="width: 150px;">
									<option value='Pass+'/>Pass+</option>
									<option value='Pass'/>Pass</option>
									<option value='Did Not Pass'/>Did Not Pass</option>				
								</select>
						</p>

						<div id="imgHolder"></div>
					</form:form>
				</div>
				<%@ include file="../common/required.jsp"%>
			</div>
			
			<!-- TODO for manage Content edit start -->
			<div id="userModal" class="display-none">
				<div class="">
					<form:form id="editUser" name="editUser" class="edit-User-form">
						<input type="hidden" name="Id" id="Id" />
						<input type="hidden" name="userId" id="userId" />
						
						<p class="button-height inline-label">
							<label class="label" for="userid">User Id</label> <label
								type="text" name="userid" id="userid" style="width: 200px"
								class="full-width newReportName"></label>
						</p>
						<p class="button-height inline-label">
							<label class="label" for="userName">Display Name<span
								class="icon-star icon-size1 red"></span></label> <input type="text"
								name="userName" id="userName" style="width: 200px"
								class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
						</p>
						<p class="button-height inline-label">
							<label class="label" for="validation-email">Email</label> <input
								type="text" id="validation-email" name="emailId"
								style="width: 200px"
								class="input full-width  validate[custom[email]]" />
						</p>
						<p class="button-height inline-label">
							<label class="label" for="password">Create Password</label> <input
								type="password" rel="editPwd" name="password" id="password"
								style="width: 200px"
								class="input full-width newReportName validate[maxSize[15],minSize[8]]" />
						</p>
						<p class="button-height inline-label">
							<label class="label" for="confPassword">Verify
								Password</label> <input type="password" rel="editConfPwd"
								name="confPassword" id="confPassword" style="width: 200px"
								class="input full-width newReportName" />
						</p>
						<p class="button-height inline-label">
							<label class="label" for="userStatus">Status</label> <input
								type="checkbox" name="userStatus" id="userStatus"
								class="switch medium wide mid-margin-right"
								data-text-on="ENABLED" data-text-off="DISABLED">
						</p>
						<p class="button-height inline-label">
							<label class="label" for="input-3">Roles</label> <select
								id="userRole" name="userRole" style="width: 150px"
								class="select multiple-as-single easy-multiple-selection check-list "
								multiple>
							</select>
						</p>
					</form:form>
					<%@ include file="../common/required.jsp"%>
					<p>
						<small class="input-info">Passwords are case-sensitive.
							They must be at least 8 characters long and include at least
							one number, one uppercase letter, and one lowercase letter.</small>
					</p>
				</div>
			</div>
			<!-- TODO for manage Content edit end -->
			
			<div id="loader" class="display-none"></div>

		</div>
		</div>
</div>
</div>
	</div>
</div>
