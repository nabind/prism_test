	<%@ page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

	<!-- Sidebar/drop-down menu -->
	<section id="menu" role="complementary" style="margin-top:34px; width:262px;">
	    <c:set var="menuSet" value="<%=request.getSession().getAttribute(IApplicationConstants.MENU_SET) %>"/>
	    <c:set var="product" value="<%=request.getSession().getAttribute(IApplicationConstants.PRODUCT_NAME) %>"/>
		<c:set var="currOrg" value="<%=request.getSession().getAttribute(IApplicationConstants.CURRORG) %>"/>
		
		<!-- This wrapper is used by several responsive layouts -->
		<div id="menu-content">

			<header class="blue-gradient glossy">
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<spring:message code="label.administrator" />
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_ACSI">
					<sec:authorize ifNotGranted="ROLE_ADMIN">
						<spring:message code="label.state" />
					</sec:authorize>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_SCHOOL">
					<sec:authorize ifNotGranted="ROLE_ADMIN">
						<spring:message code="label.school" />
					</sec:authorize>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_CLASS">
					<spring:message code="label.group" />
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_PARENT">
					<spring:message code="label.parent" />
				</sec:authorize>
			</header>

			<div id="profile">
				<img src='<spring:theme code="user.png" />' width="38" height="38" alt="User name" class="user-icon">
				<spring:message code="label.welcome" />
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><%=request.getSession().getAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME) %></span>
					<span style="color:#FFF">as </span> <span class="name"  style="padding-left: 45px;"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
				</sec:authorize>
			</div>

			<!-- By default, this section is made for 4 icons, see the doc to learn how to change this, in "basic markup explained" -->
			<ul id="access" class="children-tooltip">
				<li><a href="dashboards.do" title="Home"><span class="icon-home"></span></a></li>
				<sec:authorize ifNotGranted="ROLE_SSO">
				<li>
					<a href="myAccount.do" title="My Account">
						<span class="icon-user"></span>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<li><a href="j_spring_security_exit_user" title="Logout"><span class="icon-extract"></span></a></li>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<li><a href="j_spring_security_logout" title="Logout"><span class="icon-extract"></span></a></li>
				</sec:authorize>
			</ul>

			<sec:authorize ifNotGranted="ROLE_PARENT">
			<div class="" id="prismMenu">
				<ul class="big-menu blue-gradient collapsible display-none" id="tempMenu">
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
				</ul>
			</div>
			</sec:authorize>
			<div style="background-color:#FFF">
				<div id="productImage" class="with-small-padding align-center custom-p" style="border-bottom:1px solid #5f80ab;">
					${messageMapSession.menuMessage} 
				</div>
			</div>

		</div>
		<!-- End content wrapper -->
	</section>
	<!-- End sidebar/drop-down menu -->