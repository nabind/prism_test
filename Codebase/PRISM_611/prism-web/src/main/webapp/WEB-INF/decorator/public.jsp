<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>

	<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>${contractTitle}</title>
		<meta name="description" content="">
		<meta name="author" content="">
	
		<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
		<meta name="HandheldFriendly" content="True">
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta http-equiv="Content-Type" content="text/html; charset=${requestScope['com.ctb.prism.characterEncoding']}">
	
		<%@ include file="../view/common/minimalStyle.jsp"%>
	
		<!-- Microsoft clear type rendering -->
		<meta http-equiv="cleartype" content="on">
		

	</head>
	
	<body class="clearfix with-menu with-shortcuts">
		<!-- Prompt IE 6 users to install Chrome Frame -->
		<!--[if lt IE 7]><p class="message red-gradient simpler">Your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->
		
			<!--==============================header=================================-->
			
			<%@ include	file="../view/common/drilldownHeader.jsp"%>
			
			<input type="hidden" name="reportUrl" id="reportUrl" value="<%=request.getAttribute("reportUrl")%>">
			<input type="hidden" name="reportParam" id="reportParam" value="">
				
			
				<div class="with-padding main-section report-container drilldown-report-container" style="background-color:#FFF; margin: 10px 50px 0 50px;z-index:101">
					<decorator:body/> 
				</div>
			
			
			<%@ include file="../view/common/footer.jsp"%>
			<%@ include file="messages.jsp"%>
			<%@ include	file="../view/common/minimalScripts.jsp"%>		
			<%@ include file="../view/common/init.jsp"%>
			
			<script>
			$(document).ready(function() {
				// ============= Patch for showing logo during pagination ===============
				<% String assessmentId = request.getParameter("assessmentId"); 
				String achAssessmentId = request.getParameter("p_ach_assessmentId");
				if(achAssessmentId != null && achAssessmentId.trim().length() > 0) {
				} else if("101".equals(assessmentId)) {
				%>
				if($("img[src*=image]") != null) {
					$("img[src*=image]").css("height", "20px");
				}
				<%}%>
				
				// ============= Patch for IE to disable pagination buttons ===============
				<%if((Integer) request.getAttribute("nextPage") == 1) { %>
					if($.browser.msie) {
						$(".page_first").css("background", "#B9B9B9 url(themes/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
						$(".page_prev").css("background", "#B9B9B9 url(themes/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
						$(".page_first").css("cursor", "not-allowed");
						$(".page_prev").css("cursor", "not-allowed");
					}
				<%}%>
				<%if((Integer) request.getAttribute("nextPage") > (Integer) request.getAttribute("lastPage")) { %>
					if($.browser.msie) {
						$(".page_next").css("background", "#B9B9B9 url(themes/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
						$(".page_last").css("background", "#B9B9B9 url(themes/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
						$(".page_next").css("cursor", "not-allowed");
						$(".page_last").css("cursor", "not-allowed");
					}
				<%}%>
			});
			</script>
	</body>
</html>
