<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>

	<html lang="en">
	<c:set var='contractName' value='<%=session.getAttribute("CONTRACT_NAME") %>' />
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>
			<c:if test="${contractName == 'inors'}"><spring:message code="title.tab.application.inors" /></c:if>
			<c:if test="${contractName == 'tasc'}"><spring:message code="title.tab.application.tasc" /></c:if>
		</title>
		<meta name="description" content="">
		<meta name="author" content="">
	
		<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
		<meta name="HandheldFriendly" content="True">
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta http-equiv="Content-Type" content="text/html; charset=${requestScope['com.ctb.prism.characterEncoding']}">
	
		<%@ include file="../view/common/commonStyle.jsp"%>
		<link rel="stylesheet" href="scripts/js/libs/formValidator/developr.validationEngine.css?v=1">
		
	
		<!-- Microsoft clear type rendering -->
		<meta http-equiv="cleartype" content="on">
		

	</head>
	
	<body class="full-page-wizard blue-background">
		<!-- Prompt IE 6 users to install Chrome Frame -->
		<!--[if lt IE 7]><p class="message red-gradient simpler">Your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->
		
			<!--==============================header=================================-->
			<%@ include	file="../view/common/regnHeader.jsp"%>
			
			<decorator:body/> 
			
			<%@ include file="../view/common/footer.jsp"%>
			<%@ include file="messages.jsp"%>
			<%@ include	file="../view/common/commonScripts.jsp"%>		
			<script src="scripts/js/developr.wizard.register.js"></script>
			<script src="scripts/js/parent/parent.js"></script>
			<%@ include file="../view/common/init.jsp"%>
			
			<script>
				$(document).ready(function()
				{
						// Elements
					var form = $('.wizard'),

						// If layout is centered
						centered;

					// Handle resizing (mostly for debugging)
					function handleWizardResize()
					{
						//centerWizard(false);
					};

					// Register and first call
					$(window).bind('normalized-resize', handleWizardResize);

					/*
					 * Center function
					 * @param boolean animate whether or not to animate the position change
					 * @return void
					 */
					function centerWizard(animate)
					{
						form[animate ? 'animate' : 'css']({ marginTop: Math.max(0, Math.round(($.template.viewportHeight-30-form.outerHeight())/2))+'px' });
					};

					// Initial vertical adjust
					//centerWizard(false);

					// Refresh position on change step
					form.on('wizardchange', function() { centerWizard(true); });

					// Validation
					if ($.validationEngine)
					{
						form.validationEngine();
					}
				});
			</script>
	</body>
</html>
