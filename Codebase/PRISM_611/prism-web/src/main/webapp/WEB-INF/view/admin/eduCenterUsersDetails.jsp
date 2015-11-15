<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="panel-content linen">
	<div class="panel-control align-right">
		<fmt:message var="MANAGE_EDU_USERS_SEARCH" key="manage.edu.center.users.search.user"  />
			<c:if test="${not empty actionMap[MANAGE_EDU_USERS_SEARCH]}">
				<span class="input search-input"> <input type="text"
					name="searchUser" id="searchUser" class="input-unstyled with-tooltip"
					title="Search Users by User ID OR <br/>Full Name (Last Name OR First Name)"
					placeholder="Search"> <a href="javascript:void(0)"
					class="button icon-search compact" id="search_icon"
					param="search_icon_user"></a>
				</span>
			</c:if>
		 
		
		<sec:authorize ifNotGranted="ROLE_SSO">
			<fmt:message var="MANAGE_EDU_USERS_ADD" key="manage.edu.center.users.add.user"  />
			<c:if test="${not empty actionMap[MANAGE_EDU_USERS_ADD]}">
				<a id="addUser" 
					tenantName="${serviceMapEduCenterDetails.eduCenterName}" 
					tenantId="${serviceMapEduCenterDetails.eduCenterId}" 
					orgLevel="-99" 
					href="#"
					class="button glossy margin-left"> 
				<span class="button-icon blue-gradient manage-btn">
					<span class="icon-add-user"></span>
				</span> 
					<spring:message code="label.addEducationCenterUser" />
				</a>
			</c:if>
		</sec:authorize>

	</div>

	<div class="panel-load-target with-padding margin10 height-mid padding-none">
		<fmt:message var="MANAGE_EDU_USERS_MORE" key="manage.edu.center.users.more"  />
		<c:if test="${not empty actionMap[MANAGE_EDU_USERS_MORE]}">
			<div class="pagination panel-control margin-bottom-small rounded-border">
				<a href="#nogo" id="moreUser"
					class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip"
					title="<spring:message code='message.more.help'/>"><spring:message code="button.content.more"/></a>
			</div>
		</c:if>
		
		<div id="userTable"
			class="report-container tabs-content padding-small"
			style="height: 450px">
			<div id="last_msg_loader" height="140"
				style="position: relative; left: 0px; z-index: 1"></div>
			<input type="text" style="display: none" name="last_user_tenant"
				id="last_user_tenant" value="">
			<table class="simple-table responsive-table" id="report-list">

				<thead>
					<tr class="abc">
						<th scope="col" width="16%"><spring:message code="thead.userID" /></th>
						<th scope="col" width="25%"><spring:message code="table.label.fullName" /></th>
						<th scope="col" width="11%" class="hide-on-tablet"><spring:message code="table.label.status" /></th>
						<th scope="col" width="16%" class="hide-on-tablet-portrait"><spring:message code="thead.educationCenterName" /></th>
						<th scope="col" width="16%"><spring:message code="table.label.userRoles" /></th>
						<th scope="col" width="16%"><spring:message code="table.label.actions" /></th>
					</tr>
				</thead>
				
				<tbody id="user_details">
					<tr class="abc users-list-all" id ="sample" >
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
	
	<div id="userModal" class="display-none">
		<div class="">
			<form:form id="editUser" name="editUser" class="edit-User-form">
				<input type="hidden" name="Id" id="Id"/>
				<input type="hidden" name="userId" id="userId"/>
				<input type="hidden" name="purpose" id="purpose" value="eduCenterUsers" />
		
				<p class="button-height inline-label">
					<label class="label" for="userid"><spring:message code="label.userId" /></label>
					<label type="text" name="userid" id="userid" style="width:200px" class="full-width newReportName"></label>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userName"><spring:message code="table.label.displayName" /><span class="icon-star icon-size1 red"></span></label>
					<input type="text" name="userName" id="userName" style="width:200px" class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
				</p>
				<p class="button-height inline-label">
					<label class="label"  for="validation-email"><spring:message code="table.label.email" /></label>
					<input type="text" id="validation-email" name="emailId" style="width:200px" maxlength="100" class="input full-width  validate[custom[email]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="password"><spring:message code="label.createPassword" /></label>
					<input type="password" rel="editPwd" name="password" id="password" style="width:200px" class="input full-width newReportName validate[maxSize[15],minSize[8]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="confPassword"><spring:message code="table.label.verifyPassword" /></label>
					<input type="password" rel="editConfPwd" name="confPassword" id="confPassword" style="width:200px" class="input full-width newReportName validate[equalsPassword[password]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userStatus"><spring:message code="table.label.status" /></label>
					<input type="checkbox" name="userStatus" id="userStatus" class="switch medium wide mid-margin-right" data-text-on="ENABLED" data-text-off="DISABLED">
				</p>
				<p class="button-height inline-label">
					<label class="label" for="input-3"><spring:message code="table.label.roles" /></label>
					<select id="userRole" name="userRole" style="width:150px" class="select multiple-as-single easy-multiple-selection check-list " multiple>
					</select>
				</p>																
			</form:form>
			<%@ include file="../common/required.jsp" %>
			<p><small class="input-info"><spring:message code="info.password" /></small></p>
		</div>
	</div>
	
	<div id="addUserModal" class="display-none">
		<div class="">
			<form:form id="addNewUser" name="addNewUser" class="add-User-form small-margin-top">
				<input type="hidden" name="tenantId" id="tenantId" value="" />
				<input type="hidden" name="orgLevel" id="orgLevel" value="" />
				<input type="hidden" name="userStatus" id="userStatus" rel="userStatus" value="" />
				<input type="hidden" name="purpose" id="purpose" value="eduCenterUsers" />
				<p class="button-height inline-label">
					<label class="label" for="userId"><spring:message code="label.userId" /><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="text" name="userId" id="userId" rel="userId" style="width:200px" class="input full-width newReportName validate[required,custom[onlyLetterNumber],maxSize[30],minSize[3]]" />
				</p>
				<p style="width:329px" id="imgHolderContainer"><span id="imgHolder"></span></p>
				<p class="button-height inline-label">
					<label class="label" for="userName"><spring:message code="table.label.displayName" /><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="text" name="userName" id="userName" style="width:200px" class="input full-width newReportName validate[required,maxSize[10],minSize[3]]" />
				</p>
				
				<p class="button-height inline-label">
					<label class="label"  for="validation-email"><spring:message code="table.label.email" /></label>
					<input autocomplete="off" type="text" id="validation-email" name="emailId" style="width:200px" maxlength="100" class="input full-width validate[custom[email]]" /> <!-- validate[required,custom[email]] -->
					
				</p>
				<p class="button-height inline-label">
					<label class="label" for="password"><spring:message code="label.password" /><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="password" name="password" id="password1" style="width:200px" class="input full-width newReportName validate[required,maxSize[15],minSize[8]]" />
				</p>
				<p class="button-height inline-label">
					<label class="label" for="confPassword"><spring:message code="label.confirmPassword" /><span class="icon-star icon-size1 red"></span></label>
					<input autocomplete="off" type="password" name="confPassword" id="confPassword" style="width:200px" class="input full-width newReportName validate[required,equalsPassword[password1]]"/>
				</p>
				<p class="button-height inline-label">
					<label class="label" for="userStatus"><spring:message code="table.label.status" /></label>
					<input type="checkbox" name="userStatusCheck" id="userStatusCheck" rel="userStatusCheck" class="switch medium wide mid-margin-right" data-text-on="ENABLED" data-text-off="DISABLED" >
				</p>
				<p class="button-height inline-label">
					<label class="label" for="input-3"><spring:message code="table.label.roles" /></label>
					<select id="addUserRole" name="userRole" style="width:150px" class="select multiple-as-single easy-multiple-selection check-list " multiple>				
					</select>
				</p>
					<div id ="imgHolder"></div>	
			</form:form>
		</div>
		<%@ include file="../common/required.jsp" %>
		<p><small class="input-info"><spring:message code="info.password" /></small></p>
	</div>
	<div id="loader" class="display-none">
		
	</div>

</div>
