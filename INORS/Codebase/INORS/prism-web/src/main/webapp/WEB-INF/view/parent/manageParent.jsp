<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<hgroup id="main-title" class="thin h1-title">
	<h1><spring:message code="h1.manageParent" /></h1>
</hgroup>

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
			<div class="panel-load-target with-padding margin10 height-mid padding-none">
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreParent" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more users"><spring:message code="button.more" /></a>
				</div>
				<div id="parentTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
						<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>
					<table class="simple-table responsive-table" id="report-list">
						<input type="text"  style="display:none"  name="lastParentName" id="lastParentName" value="" />
						<thead>
							<tr class="abc">
								<th scope="col" width="20%"><span style="margin-left:24px"><spring:message code="thead.userID" /></span></th>
								<th scope="col" width="25%"><spring:message code="table.label.fullName" /></th>
								<th scope="col" width="30%"><spring:message code="thead.schoolName" /></th>
								<th scope="col" width="15%" class=""><spring:message code="table.label.actions" /></th>
							</tr>
						</thead>
						<tbody id="parent_details">
							<tr class="abc parent-list-result">
								<th scope="col" width="20%"></th>
								<th scope="col" width="25%"></th>
								<th scope="col" width="30%"></th>
								<th scope="col" width="15%" class=""></th>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="margin-top"></div>
	<div id="parentModal" class="display-none">
		<div id ="parentModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile"></div>
	</div>
	<div id="passwordModal" class="display-none">
		<div id ="passwordModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile"></div>
	</div>
