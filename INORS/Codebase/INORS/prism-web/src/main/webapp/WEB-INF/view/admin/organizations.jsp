<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

<hgroup id="main-title" class="thin h1-title">
	<h1><spring:message code="h1.manageOrganizations" /></h1>
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
					<a href="#nogo" id="showHierarchy" class="button icon-forward with-tooltip"  style="display:none" title="Show Hierarchy"></a><span id="showOrgName"> </span></b></div>
				<span class="input search-input">
					<input type="text" name="searchOrg" id="searchOrg" class="input-unstyled with-tooltip" title="Search Organizations by Name" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_org"></a>
				</span>
			</div>
			<div class="panel-load-target with-padding margin10 height-mid padding-none">
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreOrg" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more organizations"><spring:message code="button.more" /></a>
				</div>
				<div id="orgTable" class="report-container tabs-content padding-small" style="height: 450px">
				<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>
					<input type="text"  style="display:none"  name="last_org_tenant" id="last_org_tenant" value="">
					<table class="simple-table responsive-table" id="org-list">
						<thead>
							<tr class="abc">
								<th scope="col" width="20%"><spring:message code="thead.orgID" /></th>
								<th scope="col" width="30%"><spring:message code="thead.orgName" /></th>
								<th scope="col" width="25%"><spring:message code="thead.HOfOrganizations" /></th>
								<th scope="col" width="25%"><spring:message code="thead.HOfUsers" /></th>
							</tr>
						</thead>
						<tbody id="org_details">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="organisationModal" class="display-none">
		<div id ="organisationModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile"></div>
	</div>
</div>
