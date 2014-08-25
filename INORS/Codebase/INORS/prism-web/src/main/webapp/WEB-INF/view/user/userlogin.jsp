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
  <title><spring:message code="title.tab.application"/></title>
  <link rel="shortcut icon" href="themes/acsi/img/favicons/favicon.ico">
  <%@ include file="../common/commonStyle.jsp"%>
  <spring:theme code="theme.name" var="themeName"/>
  <c:choose>
		<c:when test="${themeName == 'inors'}">
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
				.custom-p p{
					margin: 0!important;
				}
		  </style>
	</c:when>
		<c:when test="${themeName == 'tasc'}">
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
					padding: 12px;
				}
				.newStyle {
					padding: 5px 0 5px 0
				}
				.acsicol {
					color: #BA1A22;
				}
		  </style>
	</c:when>
	
	<c:otherwise>
		<style>
			html, body {height: auto !important}
		    html { padding: 30px 20px; font-size: 20px; line-height: 1.4; color: #737373; background: #5F80AB; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
		    html, input { font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; }
		    body { /*max-width: 1200px; _width: 500px;*/ background-color: #FFFFFF !important; padding: 5px 20px 20px; border: 1px solid #b3b3b3; border-radius: 8px; margin: 0 auto; box-shadow: 0 1px 10px #a7a7a7, inset 0 1px 0 #fff; background: #fcfcfc; }
		    h1 { margin: 0 10px; font-size: 50px; text-align: left; }
		    h1 span { color: #bbb; }
		    h3 { margin: 1.5em 0 0.5em; }
		    p { margin: 1em 0; }
		    ul { /*padding: 0 0 0 40px;*/ margin: 1em 0; }
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
				height: 550px !important;
				min-height: 550px !important;
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
			.custom-p p{
				margin: 0!important;
			}
	  </style>
	</c:otherwise>
</c:choose>
 
</head>
<body style="min-width:800px" class="">
	<a href="#" id="open-menu" class="" onClick="return false;"><span>Login</span></a>
	<div id="logo" class="newStyle" style="border-bottom: 1px solid #CCC">
		<h1>
		 	<span class="logo-title custom-p"> 
				${messageMap.commonHeaderMessage}
			</span>
		<br>
		<span class="description"> </span></h1>
	</div>
	<input type="hidden" id="userLogin" value="userLogin">
	<c:choose>
		<c:when test="${themeName == 'inors'}">
		<!-- State user page -->
			<div class="columnss">
				<div class="seven-columns">
					${messageMap.teacherPageContent}
					<div class="loginBtn margin-top margin-bottom-medium">
						<button type="button" class="button blue-gradient glossy icon-download" onclick="javascript:window.open('/displayAssest.do?assetPath=Static_Files/Quick_Start_Guide.pdf',800,600)">
							Download Quick Start Guide PDF
						</button>
					</div>
				</div>
			</div>
			
			<section id="menu" class="menu">
				<div class="">
					<div id="form-block" class="boxfshaxde">
						<div id="form-viewport">
		
							<form:form action="j_spring_security_check" method="post" id="form-login" class="input-wrapper blue-gradient glossy">
								<div class="loginTitle"><spring:message code="title.dpp.welcome"/></div>
								<ul class="inputs large">
									<li><span class="icon-user mid-margin-right"></span>
										<span class="input-unstyled no-padding">
											<input type="text" name="j_username" id="j_username" value="" class="input-unstyled" placeholder="Username" autocomplete="off">
											 <a href="#" class="icon-play login-username-help"><spring:message code="login.from.forget.username"/> <span class="icon-question"></span></a>
										</span>
									</li>
									<li><span class="icon-lock mid-margin-right"></span>
										<span class="input-unstyled  no-padding">
											<input type="password" name="j_password" id="j_password" value="" class="input-unstyled" placeholder="Password" autocomplete="off">
											<a href="#" class="icon-play login-password-help"><spring:message code="login.from.forget.password"/> <span class="icon-question"></span></a>
										</span>
									</li>
								</ul>
								<input type="hidden" name="j_contract" id="j_contract" value="inors" autocomplete="off">
		
								<p class="button-height">
									<button type="submit" class="button big glossy" id="login">Login</button>
									<!--<input type="checkbox" name="_spring_security_remember_me" id="remind" value="1" checked="checked" class="switch tiny mid-margin-right with-tooltip" title="Enable auto-login">
									<label for="remind">Remind me</label>-->
								</p>
							</form:form>
		
						</div>
					</div>
					
					<div class="margin-top">
						<div class="wrapped relative white-gradient custom-p" style="height: auto">
							${messageMap.teacherOutageContent}
						</div>
					</div>
					
				</div>
			</section>
		</c:when>
				<c:when test="${themeName == 'tasc'}">
		<!-- State user page -->
			<div class="columnss">
				<div class="seven-columns">
					<div>
						<img alt="" src="<spring:theme code="login.welcome" />" />
					</div>
					<div class="boxsshade scrollable" style="padding:1px;">
						<div class="rowTwo">
							<h1 class="h1style" style="margin: 0"><spring:message code="login.page.welcome"/></h1>
							<div class="loginMsg" height="auto">
								<p style="line-height:120%">12121212</p>
								<p style="line-height:100%">34343434</p>
							</div>
							<div class="loginBtn margin-top margin-bottom-medium">
								<button type="button" class="button blue-gradient glossy icon-download" onClick="javascript:window.open('<%=request.getContextPath()%>/scripts/Quick_Start_Guide.pdf',800,600)">
									Download Quick Start Guide PDF
								</button>
							</div>
						</div>
					</div>
				</div>
				
				
				
				
			</div>
			
			<section id="menu" class="menu">
				<div class="">
					<div id="form-block" class="boxfshaxde">
						<div id="form-viewport">
		
							<form:form action="j_spring_security_check" method="post" id="form-login" class="input-wrapper blue-gradient glossy">
								<div class="loginTitle"><spring:message code="title.dpp.welcome"/></div>
								<ul class="inputs large">
									<li><span class="icon-user mid-margin-right"></span>
										<span class="input-unstyled no-padding">
											<input type="text" name="j_username" id="j_username" value="" class="input-unstyled" placeholder="Username" autocomplete="off">
											 <a href="#" class="icon-play login-username-help"><spring:message code="login.from.forget.username"/> <span class="icon-question"></span></a>
										</span>
									</li>
									<li><span class="icon-lock mid-margin-right"></span>
										<span class="input-unstyled  no-padding">
											<input type="password" name="j_password" id="j_password" value="" class="input-unstyled" placeholder="Password" autocomplete="off">
											 <a href="#" class="icon-play login-password-help"><spring:message code="login.from.forget.password"/> <span class="icon-question"></span></a>
										</span>
									</li>
								</ul>
								<input type="hidden" name="j_contract" id="j_contract" value="tasc" autocomplete="off">
		
								<p class="button-height">
									<button type="submit" class="button big glossy" id="login"><spring:message code="login.from.head"/></button>
									<!--<input type="checkbox" name="_spring_security_remember_me" id="remind" value="1" checked="checked" class="switch tiny mid-margin-right with-tooltip" title="Enable auto-login">
									<label for="remind">Remind me</label>-->
								</p>
							</form:form>
		
						</div>
					</div>
					
					<div class="margin-bottom-medium">
						<p class="wrapped relative white-gradient" style="height: auto;text-align: justify">
							<spring:message code="login.otage.message"/>
						</p>
					</div>
					
									
				</div>
			</section>
		</c:when>
		
		<c:otherwise>
		<!-- Parent page -->
			<div class="columnss">
				<div class="seven-columns">
					${messageMap.parentPageContent}
				</div>
			</div>
			
			<section id="menu" class="menu">
				<div class="">
					<div id="form-block" class="boxfshaxde">
						<div id="form-viewport">
		
							<form:form action="j_spring_security_check" method="post" id="form-login" class="input-wrapper blue-gradient glossy">
								<div class="loginTitle"><spring:message code="title.dpp.welcome"/></div>
								<ul class="inputs large">
									<li><span class="icon-user mid-margin-right"></span>
										<span class="input-unstyled no-padding">
											<input type="text" name="j_username" id="j_username" value="" class="input-unstyled" placeholder="Username" autocomplete="off">
											<a href="#" class="icon-play login-username-help">Forgot Username <span class="icon-question"></span></a>
										
										
										</span>
									</li>
									<li><span class="icon-lock mid-margin-right"></span>
										<span class="input-unstyled no-padding">
											<input type="password" name="j_password" id="j_password" value="" class="input-unstyled" placeholder="Password" autocomplete="off">
											<a href="#" class="icon-play login-password-help">Forgot Password <span class="icon-question"></span></a>
										</span>
									</li>
								</ul>
								<input type="hidden" name="j_contract" id="j_contract" value="inors" autocomplete="off">
		
								<p class="button-height">
									<button type="submit" class="button big glossy" id="login">Login</button>
								</p>
							</form:form>
		
						</div>
					</div>
					<div class="margin-top">
						<div class="wrapped relative white-gradient custom-p" style="height: auto;text-align: justify">
							${messageMap.parentOutageContent}
						</div>
					</div>
					
					<div class="margin-bottom-medium margin-top">
						
						<div class="boxshade" style="height: auto">
							<ul style="" class="margin-bottom-medium bullet-list">
								<li>
									<a class=""  href="registration.do">
										<small class="tag orange-bg">New User:</small> Click here to enter your Invitation Code and create a new account!
									</a>
								</li><br>
								<li>
									<a class="" href="#nogo" onClick="javascript:window.open('<%=request.getContextPath()%>/displayAssest.do?assetPath=Static_Files/Quick_Start_Guide_For_Parents.pdf?theme=parent&parent=true#nogo',800,600)">
										For information about the <spring:message code="pnlogin.page.welcome"/>, access the <small class="tag blue-bg">QUICK START GUIDE FOR PARENTS</small> here.
									</a>
								</li>
							</ul>
							
						
						
						</div>
					</div>
					
				</div>
			</section>	  
		</c:otherwise>
	</c:choose>
	
	<footer class="margin-top">
		<c:choose>
			<c:when test="${themeName == 'inors'}">
				<div class="float-center custom-p">${messageMap.teacherFooterMessage} </div> 
			</c:when>
			<c:otherwise>
				<div class="float-center custom-p"> ${messageMap.parentFooterMessage} </div>  
			</c:otherwise>
		</c:choose>
	</footer>  
  
	<%@ include	file="../common/minimalScripts.jsp"%>	
	<script type="text/javascript" src="scripts/js/libs/jquery.watermark.min.js"></script>
	<script type="text/javascript" src="scripts/js/admin/login.js"></script>
	
	<script>
	
		$(document).ready(function() {
		
			// ============================= Password PlaceHolder in IE =============================
			if($.browser.msie) {
				$("#j_password").watermark("Password");
				$("#j_password").css("color", "#808080");
			}
			// ============================= display/hide menu section collapse icon =============================
			if($("#menu").hasClass("custom-scroll")) {
				$("#open-menu").css('cursor', 'default');
			} else {
				//$("#open-menu").css('display', 'none');
			}
			
			// ============================= Prevent Hiding menu in login page =============================
			$("#open-menu").click(function() {
				$('body').removeClass("menu-hidden");
				$("#open-menu").css('cursor', 'default');
			});
			
			// ============================= Add custom scroll to report =============================
			$('.body').customScroll({
				horizontal : true,
				animate : false,
				width: 10
			});
			/*
			$(document).on('init-queries',					function() { $("#open-menu").css('display', 'block'); })
				   .on('quit-query-mobile',				function() { $("#open-menu").css('display', 'none'); })
				   .on('quit-query-tablet',				function() { $("#open-menu").css('display', 'none'); })
				   .on('enter-query-mobile',			function() { $("#open-menu").css('display', 'block'); })
				   .on('enter-query-tablet',			function() { $("#open-menu").css('display', 'block'); })
				   .on('enter-query-desktop',			function() { $("#open-menu").css('display', 'none'); });
			*/
			
			// ============================= Force Focus password field - on blur username =============================
			$("#j_username").keydown(function(e){
				if (e.keyCode == 9 ) {
					$("#j_password").focus();
					e.returnValue = false; // for IE
					if (e.preventDefault) e.preventDefault(); // for Mozilla
				}
			});
			$("#j_password").keydown(function(e){
				if(e.shiftKey && e.keyCode == 9) { 
					$("#j_username").focus();
					e.returnValue = false; // for IE
					if (e.preventDefault) e.preventDefault(); // for Mozilla
				} else if (e.keyCode == 9 ) {
					$("#login").focus();
					e.returnValue = false; // for IE
					if (e.preventDefault) e.preventDefault(); // for Mozilla
				}
			});
			$("#login").keydown(function(e){
				if(e.shiftKey && e.keyCode == 9) {
					$("#j_password").focus();
					e.returnValue = false; // for IE
					if (e.preventDefault) e.preventDefault(); // for Mozilla
				} 
			});
			
			// ============================= Hack for IE (rotation text) =============================
			if($.browser.msie) $('.rotate').css('margin', '50px !important');
			
			
		});
		
		<c:if test="${not empty param.login_error}">
			displayError( 'Invalid username or password.' );
			$("#menu").removeClass("custom-scroll");
			$(document).ready(function() {
				if($("#open-menu")) {
					$("#open-menu").click();
				}
			});
		</c:if>
		<c:if test="${not empty param.sessionExpired}">
			displayError( 'Your session has expired. Please login again.' );
			$(document).ready(function() {
				if($("#open-menu")) {
					$("#open-menu").click();
				}
			});
		</c:if>
		
		/**
		 * Function to display error messages
		 * @param string message the error to display
		 */
		function displayError(message)
		{
			var image = $("#open-menu").find('span').css('background');
			if(image != null && image.indexOf('menu.png') != -1) {
				$("#open-menu").click();
			}
			// Show message
			var message = $('#form-login').message(message, {
				append: false,
				arrow: 'bottom',
				classes: ['red-gradient'],
				animate: false					// We'll do animation later, we need to know the message height first
			});
	
			// Vertical centering (where we need the message height)
			//centerForm(true, 'fast');
	
			// Watch for closing and show with effect
			message.bind('endfade', function(event)
			{
				// This will be called once the message has faded away and is removed
				//centerForm(true, message.get(0));
	
			}).hide().slideDown('fast');
		}
		

		// What about a notification?
		/*
		notify('Login to Enterprise Reporting', 'Please use your credential to login into system. If you don\'t have login-id please contact your school administrator. ', {
			autoClose: true,
			delay: 2500,
			icon: '<spring:theme code="icon.png" />'
		});*/
	
		
	</script>
	
	<%@ include file="../common/init.jsp"%>
	
	<div id="forgotPassword" class="display-none">
			<div class="">
			<div id ="forgotPasswordContainer"  name="forgotPasswordContainer">
				<p class="button-height inline-label">
					<label class="label isIE" for="username">Username</label>
					<input type="text" name="f_username" id="f_username" style="width:200px;margin:3px" class="input" />
				</p>
				<p style="width:306px;margin:3px" class="message red-gradient margin-top display-none" id="invalidUsernameMsg">The Username entered is not valid. Make sure you have entered correct username.</p>
				</div>
			</div>
			
		<div id="forgotUserName" class="display-none">
			<div id ="forgotUserNameContainer"  name="forgotUserNameContainer" class="">
				<p class="button-height inline-label">
					<label class="label isIE" for="email">Enter Email Id</label>
					<input type="text" name="f_email" id="f_email" style="width:200px;margin:3px" maxlength="100" class="input" />
				</p>
			</div>
		</div>
		
			
	</div>
	
</body>
</html>