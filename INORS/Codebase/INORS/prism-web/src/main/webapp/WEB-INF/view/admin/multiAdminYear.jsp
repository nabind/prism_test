<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>

<%
String adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
%>

<div class="panel-control">
	<div class="float-left">
		<a href="#nogo" id="hideHierarchy" class="button icon-backward with-tooltip" title="Hide Hierarchy"></a>
	</div>
	<c:set var="selectedYear" value="<%=adminYear %>"></c:set>
	<c:set var="selectedOrgMode" value="<%=orgMode %>"></c:set>
	<div class="float-right">
		<select style="width: 100px;" name="AdminYear" id="AdminYear" 
			title='Select organization mode to refresh the hierarchy.'
			class="select silver-gradient glossy with-tooltip expandable-list" 
			onchange="reloadOrgTree($(this))"
			tabindex="-1">
			<c:forEach var="adminYears" items="${adminList}" varStatus="loopCount" >
				<c:if test="${selectedYear eq adminYears.value}">
					<option value="${adminYears.value}" selected>${adminYears.name}</option>
				</c:if>
				<c:if test="${selectedYear ne adminYears.value}">
					<option value="${adminYears.value}">${adminYears.name}</option>
				</c:if>
			</c:forEach>
		</select>
		<select style="width: 100px;" name="orgMode" id="orgMode" 
			title='Select administration to refresh the hierarchy.'
			class="select silver-gradient glossy with-tooltip expandable-list" 
			onchange="reloadOrgTree($(this))">
					<option value="PUBLIC" selected>Public</option>
					<option value="NON PUBLIC">Non Public</option>
		</select>
	</div>
</div>
