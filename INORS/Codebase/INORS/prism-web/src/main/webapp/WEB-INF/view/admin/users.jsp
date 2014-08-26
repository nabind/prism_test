<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<hgroup id="main-title" class="thin h1-title">
<h1>Manage Users</h1>
</hgroup>

<script>
	window.history.forward(1);
</script>
<style>
	.label {
		width: 150px !important;
	}
</style>

<!--<div class="with-padding">-->
<div class="user-main" style="background-color: #FFF">

	<input type="hidden" value="<c:if test='${not empty hierarchialOrgIds}'>${hierarchialOrgIds}</c:if><c:if test='${empty hierarchialOrgIds}'></c:if>" id="tempTree" name="tempTree">
	<div class="content-panel margin-bottom" style="min-width: 764px;">
		<div class="panel-navigation silver-gradient">

			<%@ include file="multiAdminYear.jsp" %>

			<div class="panel-load-target">
				<div id="slide_menu_user" class="navigable">
					
						<%@ include file="../admin/orgHierarchy.jsp" %>
					
				</div>
			</div>			

		</div>

		<div class="panel-content linen">
			<div class="panel-control align-right">
				<div class = "align-left" style="float:left"><b>
					<a href="#nogo" id="showHierarchy" class="button icon-forward with-tooltip" style="display:none" title="Show Hierarchy"></a> <span id="showOrgNameUser"><c:if test='${not empty userList}'><spring:message code="title.page.manageUser"/> ${orgName}</c:if> </span></b></div>
				<span class="input search-input">
					<input type="text" name="searchUser" id="searchUser" class="input-unstyled with-tooltip" title="<spring:message code='message.search.help'/>" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_user"></a>
				</span>
				
				<a id="addUser" tenantName="" tenantId="" orgLevel=""  href="#" class="button glossy margin-left with-tooltip" title="<spring:message code='title.page.addUser'/>">
					<span class="button-icon blue-gradient manage-btn"><span class="icon-add-user" ></span></span>
					<spring:message code="title.page.addUser"/>
				</a>
				<a class="button float-right with-tooltip" title="<spring:message code='title.page.downloadUser'/>" id="downloadUsers" style="cursor: pointer; margin-left: 10px;"><span class="button-icon icon-download blue-gradient report-btn">XLSX</span> <spring:message code="title.page.downloadUser"/></a>
			</div>
			
			<div
				class="panel-load-target with-padding margin10 height-mid padding-none">
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreUser" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="<spring:message code='message.more.help'/>"><spring:message code="button.content.more"/></a>
				</div>
				<div id="userTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
						<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>
					<input type="text"  style="display:none"  name="last_user_tenant" id="last_user_tenant" value="">
					<table class="simple-table responsive-table" id="report-list">

						<thead>
							<tr class="abc">
								<th scope="col" width="20%"><span style="margin-left:24px"><spring:message code="table.label.userId"/></span></th>
								<th scope="col" width="25%" class="hide-on-tablet"><spring:message code="table.label.fullName"/></th>
								<!-- <th scope="col" width="10%" class="hide-on-tablet">Status</th> -->
								<!--<th scope="col" width="10%">Status</th> -->
								<!--  <th scope="col" width="15%" class="hide-on-tablet-portrait">Org Name</th>-->
								<th scope="col" width="20%"><spring:message code="table.label.orgName"/></th>
								<th scope="col" width="20%"><spring:message code="table.label.userRoles"/></th>
								<th scope="col" width="15%"><spring:message code="table.label.actions"/></th>
							</tr>
						</thead>

						<tbody id="user_details">
							<tr class="abc users-list-all" id ="sample" >
								<th scope="row">&nbsp;</th>
								<th scope="row" class="hide-on-tablet">&nbsp;</th>
								<!--  <th scope="row" class="hide-on-tablet">&nbsp;</th>-->
								<!--  <th scope="row">&nbsp;</th>-->
								<!--  <th scope="row" class="hide-on-tablet-portrait">&nbsp;</th>-->
								<th scope="row">&nbsp;</th>
								<th scope="row">&nbsp;</th>
								<th scope="row">&nbsp;</th>
								
							</tr>
							<c:forEach var="users" items="${userList}" varStatus="loopCount" >
							
									<%-- <c:if test="${loopCount.last}">
										<input type="text"  style="display:none"  name="last_user_tenant" id="last_user_tenant" value="${users.loggedInOrgId}_${users.userName}" />
									</c:if> --%>

								<tr class="abc users-list-all" id =${users.tenantId}_${users.userId} scrollid=${users.loggedInOrgId}_${users.userName} >
									<th scope="row">${users.userName}</th>
									<td class="hide-on-tablet">${users.userDisplayName}</td>
									<%-- <c:if test="${users.status == 'AC'}">
										<!--  <td class="hide-on-tablet"><small class="tag green-bg">Enabled</small></td>-->
										<td><small class="tag green-bg">Enabled</small></td>
									</c:if>
									<c:if test="${users.status == 'IN'}" >
										<!-- <td class="hide-on-tablet"><small class="tag red-bg">Disabled</small></td>-->
										<td><small class="tag red-bg">Disabled</small></td>
									</c:if> --%>
									<!--  <td class="hide-on-tablet-portrait">${users.tenantName}</td>-->
									<td>${users.tenantName}</td>
									
									<td class="roleContainerForUsers vertical-center">
											<c:forEach var="role" items="${users.availableRoleList}">
												<c:if test="${role.roleName == 'ROLE_ACSI'}">
												<small class="tag blue-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>
												<c:if test="${role.roleName == 'ROLE_CTB'}">
												<small class="tag green-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>
												<c:if test="${role.roleName == 'ROLE_SCHOOL'}">
												<small class="tag anthracite-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>
												<c:if test="${role.roleName == 'ROLE_CLASS'}">
												<small class="tag grey-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>												
												<c:if test="${role.roleName == 'ROLE_PARENT'}">
												<small class="tag red-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>	
												<c:if test="${role.roleName == 'ROLE_ADMIN'}">
												<small class="tag orange-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>	
												<c:if test="${role.roleName == 'ROLE_USER'}">
												<small class="tag black-bg role ${role.roleName}">${role.label} ${role.roleDescription}</small><br/>
												</c:if>
											</c:forEach>
										</td>
									<td class="vertical-center">
										<span class="button-group compact"> 
											<sec:authorize ifNotGranted="ROLE_SSO">
												<a id="${users.userId}" parentId = ${users.parentId} tenantId = ${users.tenantId} href="#" class="button icon-pencil with-tooltip edit-User" title="Edit"></a>
											</sec:authorize>
											<a id="${users.userId}" param="${users.userName}" href="#" class="button icon-users icon with-tooltip login-as" title="Login as User"></a>
											<sec:authorize ifNotGranted="ROLE_SSO">
												<a id="${users.userId}" userName = ${users.userName} tenantId = ${users.tenantId} href="#" class="button icon-trash with-tooltip confirm delete-User" title="Delete"></a>
											</sec:authorize> 
										</span>
									</td>
								</tr>
							</c:forEach>							
						</tbody>

					</table>

				</div>
				
			</div>
			
		</div>
	</div>
	
	
	<div id="userModal" class="display-none">
		<div class="">
			<form:form id="editUser" name="editUser" class="edit-User-form">
				<input type="hidden" name="Id" id="Id"/>
				<input type="hidden" name="userId" id="userId"/>
		
				<p class="button-height inline-label">
					<label class="label" for="userid"><spring:message code="table.label.userId"/></label>
					<label type="text" name="userid" id="userid" style="width:200px" class="full-width newReportName"></label>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userName"><spring:message code="table.label.displayName"/><span class="icon-star icon-size1 red"></span></label>
					<input type="text" name="userName" id="userName" style="width:200px" class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
				</p>
				<p class="button-height inline-label">
					<label class="label"  for="validation-email"><spring:message code="table.label.email"/></label>
					<input type="text" id="validation-email" name="emailId" style="width:200px" maxlength="100" class="input full-width  validate[custom[email]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="password"><spring:message code="table.label.createPassword"/></label>
					<input type="password" rel="editPwd" name="password" id="password" style="width:200px" class="input full-width newReportName validate[maxSize[15],minSize[8]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="confPassword"><spring:message code="table.label.verifyPassword"/></label>
					<input type="password" rel="editConfPwd" name="confPassword" id="confPassword" style="width:200px" class="input full-width newReportName validate[equalsPassword[password]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userStatus"><spring:message code="table.label.status"/></label>
					<input type="checkbox" name="userStatus" id="userStatus" class="switch medium wide mid-margin-right" data-text-on="ENABLED" data-text-off="DISABLED" tabindex="-1" checked="">
				</p>
				<p class="button-height inline-label">
					<label class="label" for="input-3"><spring:message code="table.label.roles"/></label>
					<select id="userRole" name="userRole" style="width:150px" class="select multiple-as-single easy-multiple-selection check-list expandable-list" multiple>
					</select>
				</p>																
			</form:form>
			<%@ include file="../common/required.jsp" %>
			<p><small class="input-info"><spring:message code="message.password.instruction"/></small></p> 
		</div>
	</div>
	
	<div id="addUserModal" class="display-none">
		<div class="">
			<form:form id="addNewUser" name="addNewUser" class="add-User-form small-margin-top">
				<input type="hidden" name="tenantId" id="tenantId" value="" />
				<input type="hidden" name="orgLevel" id="orgLevel" value="" />
				<input type="hidden" name="userStatus" id="userStatus" rel="userStatus" value="" />

				<p class="button-height inline-label">
					<label class="label" for="userId"><spring:message code="table.label.userId"/><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="text" name="userId" id="userId" rel="userId" style="width:200px" class="input full-width newReportName validate[required,custom[onlyLetterNumber],maxSize[30],minSize[3]]" />
				</p>
				<p style="width:329px" id="imgHolderContainer"><span id="imgHolder"></span></p>
				<p class="button-height inline-label">
					<label class="label" for="userName"><spring:message code="table.label.displayName"/><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="text" name="userName" id="userName" style="width:200px" class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
				</p>
				
				<p class="button-height inline-label">
					<label class="label"  for="validation-email"><spring:message code="table.label.email"/></label>
					<input autocomplete="off" type="text" id="validation-email" name="emailId" style="width:200px" maxlength="100" class="input full-width validate[custom[email]]" /> <!-- validate[required,custom[email]] -->
					
				</p>
				<p class="button-height inline-label">
					<label class="label" for="password"><spring:message code="table.label.createPassword"/><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="password" name="password" id="password1" style="width:200px" class="input full-width newReportName validate[required,maxSize[15],minSize[8]]" />
				</p>
				<p class="button-height inline-label">
					<label class="label" for="confPassword"><spring:message code="table.label.verifyPassword"/><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="password" name="confPassword" id="confPassword" style="width:200px" class="input full-width newReportName validate[required,equalsPassword[password1]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userStatus"><spring:message code="table.label.status"/></label>
					<input type="checkbox" name="userStatusCheck" id="userStatusCheck" rel="userStatusCheck" class="switch medium wide mid-margin-right" data-text-on="ENABLED" data-text-off="DISABLED" tabindex="-1" checked="">
				</p>
				
				<p class="button-height inline-label">
					<label class="label" for="input-3"><spring:message code="table.label.roles"/></label>
					<select id="addUserRole" name="userRole" style="width:150px" class="select multiple-as-single easy-multiple-selection check-list expandable-list" multiple>				
					</select>
				</p>
					<div id ="imgHolder"></div>	
			</form:form>
		</div>
		<%@ include file="../common/required.jsp" %>
		<p><small class="input-info"><spring:message code="message.password.instruction"/></small></p>
	</div>
	<div id="loader" class="display-none">
		
	</div>
</div>

 