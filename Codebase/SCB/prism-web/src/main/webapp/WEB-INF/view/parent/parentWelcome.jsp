	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1><spring:message code="label.welcome" />, <%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %>!</h1>
	</hgroup>

	<div class="right-column margin-bottom-medium" style="min-height:675px;">
		<div class="right-column-200px" style="margin-top: 15px;">

			<div class="right-column" style="min-width: 260px;">
			<div class="" style="height: 100px; margin-top:-45px;">
				<h3 style="position:relative"><spring:message code="menuName.first.children"/></h3>
				<!--<p class="big-message blue-gradient">-->
				<div style="height: 647px; z-index: 100;" id="child-holder" class="big-message blue-gradient">
					<div class="" style="color : #fff;">
						<spring:message code="p.parentWelcome.1" />
						<br /><br />
						<span class="children-list"></span>
					</div>
				</div>
			</div>
			</div>
			
			<div class="left-column" style="margin-right: 270px;">
				${messageMapSession.systemMessage}
			</div>
	</div>
	</div>
