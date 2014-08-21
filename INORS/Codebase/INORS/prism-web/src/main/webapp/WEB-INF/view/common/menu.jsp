	<%@page import="com.ctb.prism.core.constant.IApplicationConstants"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
	<c:set var="menuMap" value="<%=request.getSession().getAttribute(IApplicationConstants.MENU_MAP) %>" />
	<!-- Side tabs shortcuts -->
	<c:set var="inorsProduct" value="${PDCT_NAME}"/>
	<c:set var="product" value="<%=request.getSession().getAttribute(IApplicationConstants.PRODUCT_NAME) %>"/>
	<ul id="shortcuts" role="complementary" class="children-tooltip " style="margin-top:34px; top:35px"  >
		<sec:authorize ifNotGranted="ROLE_PARENT">
			<li class="current"><a href="dashboards.do" class="shortcut-messages" title="Dashboard">Dashboard</a></li>
			<sec:authorize ifNotGranted="ROLE_SSO">
		    <li><a href="myAccount.do" class="shortcut-stats" title="My Account">My Account</a></li>
		    </sec:authorize>
		    <sec:authorize ifNotGranted="ROLE_EDU_ADMIN">
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<!-- Only for Admin users -->
					<c:if test="${not empty menuMap['Manage Users']}">
					<li><a href="${menuMap['Manage Users']}" class="shortcut-contacts" title="Manage Users">Manage Users</a></li>
					</c:if>
					<c:if test="${not empty menuMap['Manage Organizations']}">
					<li><a href="${menuMap['Manage Organizations']}" class="shortcut-settings" title="Manage Organizations">Manage Organizations</a></li>
					</c:if>
					<%-- <sec:authorize ifNotGranted="ROLE_SSO"> --%>
						<c:if test="${not empty menuMap['Manage Parents']}">
						<li><a href="${menuMap['Manage Parents']}" class="shortcut-medias" title="Manage Parents">Manage Parents</a></li>
						</c:if>
						<c:if test="${not empty menuMap['Manage Students']}">
						<li><a href="${menuMap['Manage Students']}" class="shortcut-agenda" title="Manage Students">Manage Students</a></li>
						</c:if>
					<%-- </sec:authorize> --%>
					<sec:authorize ifAnyGranted="ROLE_SUPER">
						<!-- Only for CTB Admins -->
						<!-- Remove Manage Education Center -->
						<c:if test="${not empty menuMap['Manage Reports']}">
						<li><a href="${menuMap['Manage Reports']}" class="shortcut-dashboard" title="Manage Reports">Manage Reports</a></li>
						</c:if>
					</sec:authorize>
				</sec:authorize>
			</sec:authorize>
			<sec:authorize ifAnyGranted="ROLE_EDU_ADMIN">
				<!-- Only for ROLE_EDU_ADMIN Admins -->
				<!-- Remove Manage Education Center -->
			</sec:authorize>
		</sec:authorize>
		
		<sec:authorize ifAnyGranted="ROLE_PARENT">	
			<!-- Only for Parents -->
			<li class="current"><a href="dashboards.do" class="shortcut-dashboard" title="Home">Home</a></li>
			<li><a href="myAccount.do" class="shortcut-stats" title="My Account">My Account</a></li>
		</sec:authorize>
		
		<sec:authorize ifAnyGranted="ROLE_SUPER">
			<c:if test="${not empty menuMap['Manage Content']}">
			<li><a href="${menuMap['Manage Content']}" class="shortcut-content" title="Manage Content">Manage Content</a></li>
			</c:if>
		</sec:authorize>
		
		<sec:authorize ifAnyGranted="ROLE_SUPER, ROLE_CTB">
			<c:if test="${not empty menuMap['Reset Password']}">
			<li><a href="${menuMap['Reset Password']}" class="shortcut-reset" title="Reset Password">Reset Password</a></li>
			</c:if>
		</sec:authorize>
		<!-- 
		<li><a href="explorer.html" class="shortcut-medias" title="Medias">Medias</a></li>
		<li><a href="form.html" class="shortcut-agenda" title="Settings">Settings</a></li>
		<li><span class="shortcut-notes" title="Notes">Notes</span></li>
		 -->
	</ul>