	<%@ page import="com.ctb.prism.core.constant.IApplicationConstants"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<!-- Left Side tabs shortcuts with icons -->
	<c:set var="inorsProduct" value="${PDCT_NAME}"/>
	<c:set var="product" value="<%=request.getSession().getAttribute(IApplicationConstants.PRODUCT_NAME) %>"/>
	<ul id="shortcuts" role="complementary" class="children-tooltip" style="margin-top: 34px; top: 35px">
		<li class="current"><a href="dashboards.do" class="shortcut-messages" title="Dashboard"><spring:message code="label.dashboard" /></a></li>
		<sec:authorize ifNotGranted="ROLE_SSO">
			<li><a href="myAccount.do" class="shortcut-stats" title="My Account"><spring:message code="label.myAccount" /></a></li>
		</sec:authorize>
		<c:forEach var="menuSet" items="${menuSet}" >
			<c:if test="${menuSet.menuName == 'Manage'}">
				<li><a href="${menuSet.reportFolderUri}" class="${menuSet.cssClass}" title="${menuSet.reportName}">${menuSet.reportName} - shortcut-${menuSet.cssClass}</a></li>
			</c:if>
		</c:forEach>
	</ul>