<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin">
<h1>Manage Roles</h1>
</hgroup>

<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">

	<div class="right-column">

		<div class="content-panel" style="padding-left: 0px; border: none">
			<div class="report-panel-content panel-content linen">

				<div class="panel-control align-right">
					<a href="manageRole.do" class="button icon-undo">Refresh</a>
				</div>

				<div
					class="panel-load-target scrollable with-padding margin10 height-mid padding-none">
					<div class="report-container tabs-content padding-small">
						<table class="simple-table responsive-table" id="report-list">

							<thead>
								<tr>
									<th scope="col" width="30%" class="">Role Name</th>
									<th scope="col" width="50%" class="">Role Description</th>
									<th scope="col" width="20%" class="">Actions</th>
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
												<a href="#"	roleId="${role.roleId}" class="button icon-pencil with-tooltip edit-role" title="Edit" 
													roleId="${role.roleId}"></a>
												<a  href="#" roleId="${role.roleId}" roleName="${role.roleName}" class="button icon-trash with-tooltip confirm delete-Role"
													title="Delete"></a>
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
						<label class="label" for="roleName">Role Name</label>
						<input type="text" name="roleName" id="roleName" style="width:200px" class="input full-width newReportName"/>
					</p>
					
					<p class="button-height inline-label">
						<label class="label" for="roleDescription">Role Description</label>
						<input type="text" name="roleDescription" id="roleDescription" style="width:200px" class="input full-width newReportName"/>
					</p>

					<p class="button-height inline-label display-none">
						<label class="label" for="searchUser">Associate Users</label>
						<input type="text" name="searchUserId" id="searchUserId" style="width:200px" class="input with-tooltip" title="Type Exact User ID and Click Associate">
						&nbsp;<a href="#" class="button associate-user"  roleId="${role.roleId}">Associate</a>
						
					</p>
                   
					<div>
						<h4>List of users associated with this role:</h4>
						 <div class="pagination panel-control margin-bottom-small rounded-border">
					  <a href="#nogo" id="moreRole" class="page_next paginate button compact icon-forward green-gradient glossy with-tooltip" title="Display more users">More</a>
				    </div>
						<div class="scrollable" style="height:180px" id="roleTable">
						<input type="hidden" name="lastid" id="lastid" value="">
							<table class="simple-table responsive-table" id="report-list">

								<thead>
									<tr>
										<th scope="col" width="30%">User Code</th>
										<th scope="col" width="40%">User ID</th>
										<th scope="col" width="30%">Action</th>
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