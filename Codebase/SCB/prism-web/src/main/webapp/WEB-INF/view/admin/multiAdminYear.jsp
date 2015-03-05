<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
<%@ include file="../common/constant.jsp" %>
<%
String adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
%>

<fmt:message var="MANAGE_ORG_SELECT_MODE" key="manage.orgs.mode"  />
<c:choose>
	<c:when test="${not empty actionMap[MANAGE_ORG_SELECT_MODE]}">
	  <div class="panel-control" style="height:60px">
	</c:when>
	<c:otherwise>
	  <div class="panel-control" style="height:30px">
	</c:otherwise>
</c:choose>
	<div class="float-left">
		<c:choose>
			<c:when test="${not empty actionMap[MANAGE_ORG_SELECT_MODE]}">
			  <a href="#nogo" id="hideHierarchy" class="button blue-gradient icon-backward icon-size1 with-tooltip" title="Hide Hierarchy" style="height: 58px;"></a>
			</c:when>
			<c:otherwise>
			  <a href="#nogo" id="hideHierarchy" class="button blue-gradient icon-backward icon-size1 with-tooltip" title="Hide Hierarchy"></a>
			</c:otherwise>
		</c:choose>
	</div>
	<c:set var="selectedYear" value="<%=adminYear %>"></c:set>
	<c:set var="selectedOrgMode" value="<%=orgMode %>"></c:set>
	<input type="hidden" value="<%=currentOrg %>" name="CurrentOrg" id="CurrentOrg">
	<div class="" style="margin-left:32px">
		<fmt:message var="MANAGE_ORG_SELECT_ADMIN" key="manage.orgs.admin"  />
		<c:if test="${not empty actionMap[MANAGE_ORG_SELECT_ADMIN]}">
			<select style="width: 190px;" name="AdminYear" id="AdminYear" 
				title='Select administration to refresh the hierarchy.'
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
		</c:if>
		<c:if test="${not empty actionMap[MANAGE_ORG_SELECT_MODE]}">
			<select style="width: 190px;" name="orgMode" id="orgMode" 
				title='Select organization mode to refresh the hierarchy.'
				class="select silver-gradient glossy with-tooltip expandable-list" 
				onchange="reloadOrgTree($(this))">
					<c:if test="${selectedOrgMode eq orgModePublic}">
						<option value='<spring:message code="orgMode.val.public"/>' selected><spring:message code="orgMode.name.public"/></option>
						<option value='<spring:message code="orgMode.val.nonPublic"/>'><spring:message code="orgMode.name.nonPublic"/></option>
					</c:if>
					<c:if test="${selectedOrgMode eq orgModeNonPublic}">
						<option value='<spring:message code="orgMode.val.public"/>'><spring:message code="orgMode.name.public"/></option>
						<option value='<spring:message code="orgMode.val.nonPublic"/>' selected><spring:message code="orgMode.name.nonPublic"/></option>
					</c:if>
			</select>
		</c:if>
	</div>
</div>
