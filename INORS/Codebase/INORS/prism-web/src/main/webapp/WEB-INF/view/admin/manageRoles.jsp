<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

<hgroup id="main-title" class="thin">
	<h1><spring:message code="h1.manageRoles" /></h1>
</hgroup>

<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">
	<div class="right-column">
		<div class="content-panel" style="padding-left: 0px; border: none">
			<div class="report-panel-content panel-content linen">
				<div class="panel-control align-right">
					<a href="manageRole.do" class="button icon-undo"><spring:message code="button.refresh" /></a>
				</div>
				<div
					class="panel-load-target scrollable with-padding margin10 height-mid padding-none">
					<div class="report-container tabs-content padding-small">
						<table class="simple-table responsive-table" id="report-list">
							<thead>
								<tr>
									<th scope="col" width="30%" class=""><spring:message code="thead.roleName" /></th>
									<th scope="col" width="50%" class=""><spring:message code="thead.roleDescription" /></th>
									<th scope="col" width="20%" class=""><spring:message code="table.label.actions" /></th>
								</tr>
							</thead>
							<tbody id="roleList">
								<c:forEach var="role" items="${roleList}">
									<tr id="${role.roleId}">
										<th scope="row"><span class="reportName">${role.roleName}</span><br>
											</th>
										<td class="vertical-center">${role.roleDescription}</td>
										<td class="vertical-center">
											<span class="button-group compact">
												<a href="#"	roleId="${role.roleId}" class="button icon-pencil with-tooltip edit-role" title="Edit" roleId="${role.roleId}"></a>
												<a href="#" roleId="${role.roleId}" roleName="${role.roleName}" class="button icon-trash with-tooltip confirm delete-Role" title="Delete"></a>
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
		<div id="roleModal" class="display-none">
			<div class="">
				<form:form id="editRoleForm" method="post" class="edit-role-form small-margin-top" action="updateRole.do">
					<input type="hidden" name="roleId" id="roleId"/>
					<p class="button-height inline-label">
						<label class="label" for="roleName"><spring:message code="thead.roleName" /></label>
						<input type="text" name="roleName" id="roleName" style="width:200px" class="input full-width newReportName"/>
					</p>
					<p class="button-height inline-label">
						<label class="label" for="roleDescription"><spring:message code="thead.roleDescription" /></label>
						<input type="text" name="roleDescription" id="roleDescription" style="width:200px" class="input full-width newReportName"/>
					</p>
					<p class="button-height inline-label display-none">
						<label class="label" for="searchUser"><spring:message code="label.associateUsers" /></label>
						<input type="text" name="searchUserId" id="searchUserId" style="width:200px" class="input with-tooltip" title="Type Exact User ID and Click Associate">
						&nbsp;<a href="#" class="button associate-user"  roleId="${role.roleId}"><spring:message code="label.associate" /></a>
						
					</p>
					<div>
						<h4><spring:message code="h4.manageRoles.listUsers" /></h4>
						 <div class="pagination panel-control margin-bottom-small rounded-border">
					  <a href="#nogo" id="moreRole" class="page_next paginate button compact icon-forward green-gradient glossy with-tooltip" title="Display more users"><spring:message code="button.more" /></a>
				    </div>
						<div class="scrollable" style="height:180px" id="roleTable">
						<input type="hidden" name="lastid" id="lastid" value="">
							<table class="simple-table responsive-table" id="report-list">
								<thead>
									<tr>
										<th scope="col" width="30%"><spring:message code="thead.userCode" /></th>
										<th scope="col" width="40%"><spring:message code="thead.userID" /></th>
										<th scope="col" width="30%"><spring:message code="thead.action" /></th>
									</tr>
								</thead>
								<tbody id="role_users_popup">
								</tbody>
							</table>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="margin-top"></div>
	</div>
</div>