		<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
		<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
		
		<div id="logo" class="newStyle" style="z-index:100">
			<h1><span class="logo-title"> <img alt="" src="<spring:theme code="contract.logo.header" />"> </span>
			<br>
			<span class="description"> </span></h1>
		</div>
		<% if(request.getSession().getAttribute(IApplicationConstants.CURRUSER) != null) { %>
		<div id="" class="" style="text-align:right;margin: -41px 15px 0 0; z-index:101;position: relative;">
			<h5><span class="logo-title blue">Welcome:</span>
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
					
					<span class="logo-title blue"> | <a href="j_spring_security_logout">Logout</a></span>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><%=request.getSession().getAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME) %></span>
					as <span class="name black"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
					
					<span class="logo-title blue"> | <a href="j_spring_security_exit_user">Logout</a></span>
				</sec:authorize>
				
			</h5>
		</div>
		<%}%>
