<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>ACSI : Login</title>
  <%@ include file="../common/commonStyle.jsp"%>
  
  <style>
		html, body {height: auto !important}
    ::-moz-selection { background: #fe57a1; color: #fff; text-shadow: none; }
    ::selection { background: #BA1A22; color: #fff; text-shadow: none; }
    html { padding: 30px 20px; font-size: 20px; line-height: 1.4; color: #737373; background: #5F80AB; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
    html, input { font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; }
    body { /*max-width: 1200px; _width: 500px;*/ padding: 5px 20px 20px; border: 1px solid #b3b3b3; border-radius: 8px; margin: 0 auto; box-shadow: 0 1px 10px #a7a7a7, inset 0 1px 0 #fff; background: #fcfcfc; }
    h1 { margin: 0 10px; font-size: 50px; text-align: left; }
    h1 span { color: #bbb; }
    h3 { margin: 1.5em 0 0.5em; }
    p { margin: 1em 0; }
    ul { padding: 0 0 0 40px; margin: 1em 0; }
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
  </style>
 
</head>
<body style="" class="">
	<a href="#" id="open-menu" class=""><span>Login</span></a>
	<div id="logo" class="newStyle" style="border-bottom: 1px solid #CCC">
		<h1><span class="logo-title"> <a href="#"> <img alt="" src="themes/acsi/img/logoCTB.png"> </a></span>
		<br>
		<span class="description"> </span></h1>
	</div>
	
	
	<div class="columnss">
		<div class="seven-columns">
			<div>
				<img alt="" src="themes/acsi/img/login_welcome_img.jpg" />
			</div>
			<div class="boxsshade">
				<div class="rowTwo">
					
					<h1 class="h1style">Welcome to <span class="acsicol">ACSI Data Online for Parents!</span></h1>
					<div class="loginMsg">
						<p style="line-height:120%">The <i>Association of Christian Schools International</i> is committed to supporting academic success and to promoting the growth of all students. You can help by playing an active role in your child's education. Create an account on this site to access personalized resources, activities, and information, which will help support your child's education throughout the year.</p>
						<p style="line-height:120%">This site provides your child's assessment results to help you understand strengths and learning needs. You will be able to see the results as soon as they are available. Also, you can review your child's progress year after year.</p>
					</div>
					
				</div>
			</div>
		</div>
		
		<div class="seven-columns" style="margin-top:35px">
			<div class="boxshade scrollable">
				<div class="columns">
					<div class="two-columns" style="float:left;padding-top:50px;margin: 0;">
						<h2 class="rotate">Results</h2>
					</div>
					<div class="nine-columns" style="box-shadow: 0px 0px 20px 5px #5F80AB; padding: 20px">
						<h3>ACSI Assessment Results</h3>
						
						<p style="line-height:120%"><small class="tag orange-bg">NEW</small> - Assessment results for Spring 2012 ACSI + results:</p>

						<p style="padding-left:10px">Grades 3 - 8: English/Language Arts & Mathematics,</p>
						<p style="padding-left:10px">Grades 4 & 6: Science,</p>
						<p style="padding-left:10px">Grades 5 & 7: Social Studies.</p>

						<p style="line-height:120%"><small class="tag orange-bg">NEW</small> - Parents may request a rescore of open-ended items or essay questions. Requests for rescore must be submitted to the school between May 29 and June 15, 2012.</p>

						<p style="line-height:120%">For more information see the English/Spanish Guide to the Student Report.</p>
					</div>
					
				</div>
			</div>
		</div>
		
		
		
	</div>
	
	<section id="menu" class="menu">
		<div class="">
			<div class="margin-bottom-medium">
				<p class="wrapped relative white-gradient" style="height: auto">
					<span class="ribbon tiny"><span class="ribbon-inner orange-gradient"><span class="icon-info-round"></span></span></span>
					ACSI Data Online is available from 3am Sunday to 3am Saturday Pacific Time. Between the hours of 11pm and 3am users may experience slowness or system outages due to nightly maintenance. Saturdays are reserved for maintenance that may require outages.
				</p>
			</div>
			<div id="form-block" class="boxfshaxde">
				<div id="form-viewport">

					<form:form action="j_spring_security_check" method="post" id="form-login" class="input-wrapper blue-gradient glossy" title="Login">
						<div class="loginTitle"><spring:message code="title.dpp.welcome"/></div>
						<ul class="inputs large">
							<li><span class="icon-user mid-margin-right"></span><input type="text" name="j_username" id="j_username" value="" class="input-unstyled" placeholder="Username" autocomplete="off"></li>
							<li><span class="icon-lock mid-margin-right"></span><input type="password" name="j_password" id="j_password" value="" class="input-unstyled" placeholder="Password" autocomplete="off"></li>
						</ul>

						<p class="button-height">
							<button type="submit" class="button glossy float-right" id="login">Login</button>
							<!--<input type="checkbox" name="_spring_security_remember_me" id="remind" value="1" checked="checked" class="switch tiny mid-margin-right with-tooltip" title="Enable auto-login">
							<label for="remind">Remind me</label>-->
							<label class="button grey-gradient">Forgot Password?</label>
							<label class="button grey-gradient">Forgot Username?</label>
						</p>
					</form:form>

				</div>
			</div>
			
			<div class="margin-bottom-medium margin-top">
				
				<div class="boxshade" style="height: 146px;" >
					<h4 style="color:#5f80ab">New User?</h4>
					<ul style="padding-left: 15px;" class="margin-bottom-medium">
						<li>
							<a class="" href="registration.do">
								Click here to enter your Activation Code and <b>create a new account!</b>
							</a>
						</li>
						<li>
							<a class="" href="#">
								ACSI Data Online for Parents Tutorial for Parents
							</a>
						</li>
						<li>
							<a class="" href="#">
								ACSI Data Online for Parents Tutorial for School Administrators
							</a>
						</li>
					</ul>
					
					For questions about the ACSI Data Online for Parents, please email support@ctb.com or call 1-800-481-4769.
				
				</div>
			</div>
			
		</div>
	</section>
	
	<footer class="margin-top">Copyright &copy; 2016 Data Recognition Corporation. All rights reserved. Read our <a href="http://www.datarecognitioncorp.com/Pages/privacy.aspx" target="_blank" style="text-decoration: underline;">Privacy Policy</a>.<br/><i>TerraNova</i> is a registered trademark of Data Recognition Corporation. The ACSI logo is a registered trademark of the Association of Christian Schools International.</footer>
  
  
	<%@ include	file="../common/commonScripts.jsp"%>	
	
	
	<script>
	
		$(document).ready(function() {
			//display/hide menu section collapse icon
			if($("#menu").hasClass("custom-scroll")) {
				$("#open-menu").css('cursor', 'default');
			} else {
				//$("#open-menu").css('display', 'none');
			}
			
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
			//$("#open-menu").find('span').css('background', 'none');
		});
		
		<c:if test="${not empty param.login_error}">
			displayError( '<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />' );
		</c:if>
		<c:if test="${not empty param.sessionExpired}">
			displayError( 'Your Session has expired. Please login again.' );
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
			centerForm(true, 'fast');
	
			// Watch for closing and show with effect
			message.bind('endfade', function(event)
			{
				// This will be called once the message has faded away and is removed
				centerForm(true, message.get(0));
	
			}).hide().slideDown('fast');
		}
		

		// What about a notification?
		notify('Login to Enterprise Reporting', 'Please you your credential to login into system. If you don\'t have login-id please contact your school administrator. ', {
			autoClose: true,
			delay: 2500,
			icon: '<spring:theme code="icon.png" />'
		});

	
	</script>
	
	<%@ include file="../common/init.jsp"%>
</body>
</html>