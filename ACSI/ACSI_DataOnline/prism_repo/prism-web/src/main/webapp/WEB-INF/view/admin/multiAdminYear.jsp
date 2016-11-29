<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>

<%
String adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
%>

<div class="panel-control">
	<div class="float-left">
		<a href="#nogo" id="hideHierarchy" class="button icon-backward with-tooltip" title="Hide Hierarchy"></a>
	</div>
	<c:set var="title" value="Select administration year to refresh the hierarchy."></c:set>
	<c:set var="selectedYear" value="<%=adminYear %>"></c:set>
	<div class="float-right">
		<select style="width: 160px;" name="AdminYear" id="AdminYear" 
			title="${title}"
			class="select silver-gradient glossy with-tooltip" onchange="reloadOrgTree($(this))"
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
	</div>
</div>
			