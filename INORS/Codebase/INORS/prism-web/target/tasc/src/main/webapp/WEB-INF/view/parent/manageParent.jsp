<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<hgroup id="main-title" class="thin h1-title">
<h1>Manage Parents</h1>
</hgroup>

<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">

		<div class="content-panel margin-bottom" style="min-width: 725px;">
		<div class="panel-navigation silver-gradient">

			<%@ include file="../admin/multiAdminYear.jsp" %>

			<div class="panel-load-target">
				<div id="slide_menu_parent" class="navigable parents">
					
						<%@ include file="../admin/orgHierarchy.jsp" %>
					
				</div>
			</div>			

		</div>

		<div class="panel-content linen">

			<div class="panel-control align-right">
			<div class = "align-left" style="float:left"><b>
					<a href="#nogo" id="showHierarchy" class="button icon-forward with-tooltip" style="display:none" title="Show Hierarchy"></a> <span id="showOrgNameParent"></span></b></div>
				<span class="input search-input">
					<input type="text" name="searchParent" id="searchParent" class="input-unstyled with-tooltip" title="Search Parent by User ID OR <br/>Full Name (Last Name OR First Name)" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_parent"></a>
				</span>

			</div>

			<div style="height: 540px;"
				class="panel-load-target with-padding margin10 height-mid padding-none">
				<p class="message icon-speech orange-gradient small-margin-bottom">
					<span class="block-arrow down"><span></span></span>
					<a href="#" title="Hide message" class="close show-on-parent-hover">X</a>
					Parent users are associated with <b>School</b> level (not with Class/Teacher level).
				</p>
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreParent" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more users">More</a>
				</div>
				<div id="parentTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
						<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>
					<table class="simple-table responsive-table" id="report-list">
						<input type="text"  style="display:none"  name="lastParentName" id="lastParentName" value="" />
						<thead>
							<tr class="abc">
								<th scope="col" width="20%">User ID</th>
								<th scope="col" width="25%">Full Name</th>
								<!--th scope="col" width="16%">Last Login</th-->
								<th scope="col" width="10%" class="hide-on-tablet">Status</th>
								<th scope="col" width="30%">School Name</th>
								<th scope="col" width="15%" class="">Actions</th>
							</tr>
						</thead>

						<tbody id="parent_details">
							<tr class="abc">
								<th scope="col" width="20%"></th>
								<th scope="col" width="25%"></th>
								<!--th scope="col" width="16%">Last Login</th-->
								<th scope="col" width="10%" class="hide-on-tablet"></th>
								<th scope="col" width="30%"></th>
								<th scope="col" width="15%" class=""></th>
							</tr>
							<%-- <c:forEach var="parent" items="${parentList}" varStatus="loopCount" >
									<c:if test="${loopCount.last}">
										<input type="text"  style="display:none"  name="lastParentName" id="lastParentName" value="${parent.userName}" />
									</c:if>
								<tr class="abc parent-list-result" id ="${parent.userName}" scrollid="${parent.userName}" >
									<th scope="row">${parent.userName}</th>
									<td>${parent.displayName}</td>
									<!--td>${parent.lastLoginAttempt}</td-->
									<c:if test="${parent.status == 'AC'}">
										<td class="hide-on-tablet"><small class="tag green-bg">Enabled</small></td>
									</c:if>
									<c:if test="${parent.status == 'IN'}" >
										<td class="hide-on-tablet"><small class="tag red-bg">Disabled</small></td>
									</c:if>
									<td>${parent.orgName}</td>
									<td class="vertical-center">
										<span class="button-group compact"> 
											<a id=${parent.userId} parentName=${parent.userName} parentDisplayName=${parent.displayName}  href="#" class="button icon-lock with-tooltip reset-Password" title="Reset Password"></a>
											<a id=${parent.userId}  parentName=${parent.userName}  href="#" class="button icon-users icon with-tooltip view-Children" title="View Children"></a>											
										</span>
									</td>
								</tr>
							</c:forEach>	 --%>						
						</tbody>

					</table>

				</div>
			
			</div>

		</div>
	</div>
	<div class="margin-top"></div>

	<div id="parentModal" class="display-none">
			<div id ="parentModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile">
													
			</div>
	</div>
	<div id="passwordModal" class="display-none">
			<div id ="passwordModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile">
													
			</div>
	</div>

