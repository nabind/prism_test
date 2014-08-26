<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${treeSturcture != 'Yes'}">
	<ul id="org_details" class="files-list mini open-on-panel-content org_list">
			<c:forEach var="organization" items="${organizationList}">
				<li id=${organization.tenantId} class="with-right-arrow grey-arrow tenantHier">
					<span id=${organization.tenantId} class="icon grey icon-size2 icon-folder"></span> 
						<b class="glossy with-tooltip tracked"  data-tooltip-options="{"classes":["black-gradient","glossy"]}" 	id=${organization.tenantId} 
							title = ${organization.tenantName} tenantName = ${organization.tenantName} orgLevel = ${organization.orgLevel} ><s:eval expression="T(com.ctb.prism.core.util.CustomStringUtil).manageString(organization.tenantName)"  />
						</b>
				</li>
			</c:forEach>
		</ul>
</c:if>	
<c:if test="${treeSturcture == 'Yes'}">
		<div id="treeViewForOrg" style="height: 510px;" class="jstree jstree-0 jstree-focused jstree-apple" rootid="${rootOrgId}_${rootOrgLevel}" >
		</div>
</c:if>
		