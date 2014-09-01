<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>

	<html lang="en">
	
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	
		<title><spring:message code="title.tab.application"/></title>
		<meta name="description" content="">
		<meta name="author" content="">
	
		<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
		<meta name="HandheldFriendly" content="True">
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta http-equiv="Content-Type" content="text/html; charset=${requestScope['com.ctb.prism.characterEncoding']}">
	
		<%@ include file="../view/common/commonStyle.jsp"%>
	
		<!-- Microsoft clear type rendering -->
		<meta http-equiv="cleartype" content="on">
		<script>
		function closeProgress(reportUrl, id) {
			
		}
		//Added for disabling browser back
		window.history.forward();
		function noBack() { window.history.forward(); }
		</script>
	</head>
	
	<body class="clearfix with-menu with-shortcuts" onpageshow="if (event.persisted) noBack();" onunload="">
		<!-- Prompt IE 6 users to install Chrome Frame -->
		<!--[if lt IE 7]><p class="message red-gradient simpler">Your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->
			<a href="#nogo" class="display-none" id="hiddenLink">Hidden link</a>
			<!--==============================header=================================-->
			<%@ include	file="../view/common/header.jsp"%>
				
			<%@ include file="../view/common/menu.jsp"%>
			
			<%@ include file="../view/common/sideBar.jsp"%>
			
			<section role="main" id="main" style="margin-top:69px" >
				<div class="with-padding main-section" style="background-color:#FFF; padding: 20px 10px !important;min-width:688px;overflow-x:auto;position: relative">
					<decorator:body/> 
				</div>
			</section>
			
			<%@ include file="../view/common/footer.jsp"%>
			<%@ include file="messages.jsp"%>
			<%@ include	file="../view/common/commonScripts.jsp"%>	
			<script src="scripts/js/highchart/highcharts-2.3.2.src.js"></script>
			<script src="scripts/js/highchart/default.service.js"></script>
			<script src="scripts/js/highchart/item.hyperlink.service.js"></script>
			<%@ include file="../view/common/init.jsp"%>
			<%@ include file="../view/common/keepAlive.jsp"%>
			<%@ include file="../view/parent/claimNewInvitation.jsp"%>
			<%@ include file="../view/common/constant.jsp"%>
	</body>
</html>
