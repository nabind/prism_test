<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!doctype html>
<html lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta charset="utf-8">
  <title><spring:message code="title.tab.login"/></title>
  <link rel="shortcut icon" href="themes/acsi/img/favicons/favicon.ico">
  <%@ include file="../common/commonStyle.jsp"%>
  <spring:theme code="theme.name" var="themeName"/>
  
  <style>
		html, body {height: auto !important;}
		html { padding: 30px 20px; font-size: 20px; line-height: 1.4; color: #737373; background: #5F80AB; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
		html, input { font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; }
		body { /*max-width: 1200px; _width: 500px;*/ background-color: #FFFFFF !important; padding: 5px 20px 20px; border: 1px solid #b3b3b3; border-radius: 8px; margin: 0 auto; box-shadow: 0 1px 10px #a7a7a7, inset 0 1px 0 #fff; background: #fcfcfc; }
		h1 { margin: 0 10px; font-size: 50px; text-align: left; }
		h1 span { color: #bbb; }
		h3 { margin: 1.5em 0 0.5em; }
		p { margin: 1em 0; }
		/*ul { padding: 0 0 0 40px; margin: 1em 0; }*/
		.container { max-width: 380px; _width: 380px; margin: 0 auto; }
		/* google search */
		#goog-fixurl ul { list-style: none; padding: 0; margin: 0; }
		#goog-fixurl form { margin: 0; }
		#goog-wm-qt, #goog-wm-sb { border: 1px solid #bbb; font-size: 16px; line-height: normal; vertical-align: top; color: #444; border-radius: 2px; }
		#goog-wm-qt { width: 220px; height: 20px; padding: 5px; margin: 5px 10px 0 0; box-shadow: inset 0 1px 1px #ccc; }
		#goog-wm-sb { display: inline-block; height: 32px; padding: 0 10px; margin: 5px 0 0; white-space: nowrap; cursor: pointer; background-color: #f5f5f5; background-image: -webkit-linear-gradient(rgba(255,255,255,0), #f1f1f1); background-image: -moz-linear-gradient(rgba(255,255,255,0), #f1f1f1); background-image: -ms-linear-gradient(rgba(255,255,255,0), #f1f1f1); background-image: -o-linear-gradient(rgba(255,255,255,0), #f1f1f1); -webkit-appearance: none; -moz-appearance: none; appearance: none; *overflow: visible; *display: inline; *zoom: 1; }
		#goog-wm-sb:hover, #goog-wm-sb:focus { border-color: #aaa; box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1); background-color: #f8f8f8; }
		#goog-wm-qt:focus, #goog-wm-sb:focus { border-color: #105cb6; outline: 0; color: #222; }
		input::-moz-focus-inner { padding: 0; border: 0; }
		
		.menu {
			background: none !important;
			background-color: #FFF !important;
			border-left: 1px solid #FFF !important;
			height: 370px !important;
			min-height: 370px !important;
			position: absolute !important;
			right: 35px !important;
			top: 100px !important;
			width: 400px !important;
			z-index: 1 !important;
			box-shadow: none !important;
			padding: 10px !important;
			
		}
		#open-menu {
			margin-top: 42px;
			margin-right: 28px;
			position: absolute !important;
			/*display: none;*/
			z-index: 1;
		}
		#open-menu > span {
			color: #5F80AB;
		}
		footer {
			border-top: 1px solid #ccc; text-align: center; color: #AAA;
			box-shadow: 0px -9px 23px -7px #CCC;
			border-radius: 7px 7px 0 0;
			padding-top: 10px;
		}
		.newStyle {
			padding: 5px 0 5px 0
		}
		.acsicol {
			color: #BA1A22;
		}
		
		/* Devices */
		#ptcs {
			position: absolute;
			width: 434px;
			height: 100px;
			z-index: 6;
			top: 194px;
			left: 650px;
		}
		#tn3 {
			position: absolute;
			width: 143px;
			height: 100px;
			z-index: 5;
			top: 98px;
			left: 560px;
		}
		#inview {
			position: absolute;
			width: 143px;
			height: 100px;
			z-index: 4;
			top: 130px;
			left: 882px;
		}
		#bible {
			position: absolute;
			width: 143px;
			height: 100px;
			z-index: 3;
			top: 185px;
			left: 560px;
		}
  </style>
	
 
</head>
<body style="min-width:982px" class="">
	<div id="logo" class="newStyle" style="border-bottom: 0px solid #CCC">
		<h1><span class="logo-title"> <img alt="" src="<spring:theme code="contract.logo.header" />"> </span>
		<br>
		<span class="description"> </span></h1>
	</div>
	
	<!-- State user page -->
	<div class="columns" style="min-width:982px">
		<div class="twelve-columns">
			<div style="text-align:center; background-color: #C6DAF3" class="rounded-border">
				<img alt="" src="themes/acsi/img/slide/CommonLoginPageImage.jpg" />
			</div>
		</div>
	</div>
	<div class="columns">
		<div class="two-columns"></div>
		<div class="four-columns" style="min-width:317px">
			<p class="big-message white-gradient" style="height:50px">
				<span class="block-arrow"><span></span></span>
				<span class="big-message-icon icon-ladybug"></span>
				<strong><spring:message code="title.tab.application"/></strong><br>
				Administrators/Teachers click here
			</p>
			<a href="userlogin.do?theme=acsi&parent=false" class="button blue-gradient full-width">Login</a>
		</div>
		
		<div class="four-columns" style="min-width:317px">
			<p class="big-message white-gradient" style="height:50px">
				<span class="block-arrow"><span></span></span>
				<span class="big-message-icon icon-butterfly"></span>
				<strong><spring:message code="pnlogin.page.welcome"/></strong><br>
				Parents/Guardians click here
			</p>
			<a href="userlogin.do?theme=parent&parent=true" class="button blue-gradient full-width">Login</a>
		</div>
		<div class="two-columns"></div>
		<div class="twelve-columns">
				<span class="big-message-icon icon-chat orange"></span>
				${commonLogInInfoMessage}
		</div>
	</div>
				
	<footer class="margin-top">
		<div class="float-center"><spring:message code="footer.copyright"/></div>
		<div class="float-center"> <spring:message code="footer.helpdesk"/> </div> 
	</footer>    
  
	<%@ include	file="../common/minimalScripts.jsp"%>	
	
	<script>
		$(document).ready(function() {
			/*var win = $(window),
				doc = $(document),
				bod = $(document.body),
		
				// Devices
				ptcs = $('#ptcs'),
				tn3 = $('#tn3'),
				inview = $('#inview'),
				bible = $('#bible')
		
				// Initial position
				devicesPos = {
					ptcs:	parseInt(ptcs.css('left'), 10),
					tn3:	parseInt(tn3.css('left'), 10),
					inview:	parseInt(inview.css('left'), 10),
					bible:	parseInt(bible.css('left'), 10)
				}
		
				// Maximum move
				devicesMoves = {
					ptcs:	100,
					tn3:	68,
					inview:	50,
					bible:	40
				},
		
				// Term
				term = $('.term'),
				currentTerm = 0;
		
			// Devices parallax on mouse move
			doc.on('mousemove', function(event)
			{
					// Screen width
				var width = win.width(),
		
					// Position relative to screen center
					pos = (event.pageX-(width/2))/width;
		
				// Set positions
				ptcs.css('left', Math.round(devicesPos.ptcs+(devicesMoves.ptcs*pos))+'px');
				tn3.css('left', Math.round(devicesPos.tn3+(devicesMoves.tn3*pos))+'px');
				inview.css('left', Math.round(devicesPos.inview+(devicesMoves.inview*pos))+'px');
				bible.css('left', Math.round(devicesPos.bible+(devicesMoves.bible*pos))+'px');
			});*/
		});
	</script>
	
	<%@ include file="../common/init.jsp"%>
	
</body>
</html>