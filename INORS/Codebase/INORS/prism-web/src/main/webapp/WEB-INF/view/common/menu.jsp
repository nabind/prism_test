	<%@page import="com.ctb.prism.core.constant.IApplicationConstants"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
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
					<li><a href="userModule.do" class="shortcut-contacts" title="Manage Users">Manage Users</a></li>
					<li><a href="manageOrganizations.do" class="shortcut-settings" title="Manage Organizations">Manage Organizations</a></li>
					<c:if test="${product ne inorsProduct}">
						<li><a href="manageParent.do" class="shortcut-medias" title="Manage Parents">Manage Parents</a></li>
					</c:if>
					<li><a href="manageStudent.do" class="shortcut-agenda" title="Manage Students">Manage Students</a></li>
					<sec:authorize ifAnyGranted="ROLE_CTB">
						<!-- Only for CTB Admins -->
						<!-- Remove Manage Education Center -->
						<li><a href="manageReports.do" class="shortcut-dashboard" title="Manage Reports">Manage Reports</a></li>
						<!-- <li><a href="manageRole.do" class="shortcut-notes" title="Manage Roles">Manage Roles</a></li> -->
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
		
		<sec:authorize ifAnyGranted="ROLE_CTB">
			<li><a href="manageContent.do" class="shortcut-content" title="Manage Content">Manage Content</a></li>
		</sec:authorize>
		<!-- 
		<li><a href="explorer.html" class="shortcut-medias" title="Medias">Medias</a></li>
		<li><a href="form.html" class="shortcut-agenda" title="Settings">Settings</a></li>
		<li><span class="shortcut-notes" title="Notes">Notes</span></li>
		 -->
	</ul>