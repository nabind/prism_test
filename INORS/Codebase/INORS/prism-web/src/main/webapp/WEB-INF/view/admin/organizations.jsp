<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin h1-title">
<h1>Manage Organizations</h1>
</hgroup>

<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">

	<div class="content-panel margin-bottom" style="min-width: 725px;">

		<div class="panel-navigation silver-gradient">

			<%@ include file="multiAdminYear.jsp" %>
			
			<div class="panel-load-target">
				<div id="slide_menu_org" class="navigable organizations">
					
						<%@ include file="../admin/orgHierarchy.jsp" %>
					
				</div>
			</div>			

		</div>

		<div class="panel-content linen">

			<div class="panel-control align-right">
				<div class = "align-left" style="float:left"><b>
					<a href="#nogo" id="showHierarchy" class="button icon-forward with-tooltip"  style="display:none" title="Show Hierarchy"></a><span id="showOrgName">Organizations of ${orgName}</span></b></div>
				<span class="input search-input">
					<input type="text" name="searchOrg" id="searchOrg" class="input-unstyled with-tooltip" title="Search Organizations by Name" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_org"></a>
				</span>
			</div>

			<div class="panel-load-target with-padding margin10 height-mid padding-none">
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreOrg" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more organizations">More</a>
				</div>
				<div id="orgTable" class="report-container tabs-content padding-small" style="height: 450px">
				<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>
					<input type="text"  style="display:none"  name="last_org_tenant" id="last_org_tenant" value="">
					<table class="simple-table responsive-table" id="org-list">
						<thead>
							<tr class="abc">
								<th scope="col" width="20%">Org ID</th>
								<th scope="col" width="30%">Org Name</th>
								<th scope="col" width="25%"># of Organizations</th>
								<th scope="col" width="25%"># of Users</th>
							</tr>
						</thead>

						<tbody id="org_details">
							<%-- <c:forEach var="org" items="${organizationList}" varStatus="loopCount" >
								<c:if test="${loopCount.last}">
									<input type="text"  style="display:none"  name="last_org_tenant" id="last_org_tenant" value="${org.parentTenantId}_${org.tenantId}" />
								</c:if>
								<tr id=${org.parentTenantId}_${org.tenantId} scrollid = ${org.selectedOrgId}_${org.tenantId} class="abc">
									<td>${org.tenantId}</td>
									<td>${org.tenantName}</td>
									<td>${org.noOfChildOrgs}</td>
									<c:if test="${org.noOfUsers != '0'}">
									<td><a style="text-decoration:underline" href="redirectToUser.do?AdminYear="+$("#AdminYear").val()+"&nodeId=${org.tenantId}&parentOrgId=${org.parentTenantId}">${org.noOfUsers}</a></td>
									</c:if>
									<c:if test="${org.noOfUsers == '0'}">
									<td>${org.org.noOfUsers}</td>
									</c:if>
								</tr>
							</c:forEach> --%>
						</tbody>

					</table>

				</div>
				
			</div>

		</div>
		
		
		
	</div>
	<div id="organisationModal" class="display-none">
			<div id ="organisationModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile">
			</div>
	</div>
	
</div>

