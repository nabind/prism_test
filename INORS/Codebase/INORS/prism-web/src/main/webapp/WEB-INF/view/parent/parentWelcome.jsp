
	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<noscript class="message black-gradient simpler">Your browser does not support JavaScript! Some features won't work as expected...</noscript>

	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1>Welcome, <%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %>!</h1>
	</hgroup>

	


	<div class="right-column margin-bottom-medium" style="min-height:636px;">
			

		<div class="right-column-200px" style="margin-top: 15px;">

			<div class="right-column" style="min-width: 260px;">
			<div class="" style="height: 100px; margin-top:-45px;">
				<h3 style="position:relative">Your Children</h3>
				<!--<p class="big-message blue-gradient">-->
				<div style="height: 621px; z-index: 100;" id="child-holder" class="big-message blue-gradient">
					<div class="" style="color : #fff;">
						Choose one of the profiles below to see an individualized Action Plan for your child.
						<br/><br/>
						<span class="children-list"></span>
					</div>
				</div>
			</div></div>

		<div class="left-column" style="margin-right: 270px;">
			<div class="" style="height: auto">
				<!-- Image slider -->
				<div id="header" class="slide-image-outer margin-bottom">
					<div class="wrap">
						<div id="slide-holder">
							<div id="slide-runner">
								<img id="slide-img-1" src="themes/acsi/img/slide/parentHome.jpg" class="slide" alt="" />
								<img id="slide-img-2" src="themes/acsi/img/slide/parentHome2.jpg" class="slide" alt="" />
								<img id="slide-img-3" src="themes/acsi/img/slide/parentHome3.jpg" class="slide" alt="" />
								<div id="slide-controls">
									<p id="slide-client" class="text">
										<span></span>
									</p>
									<p id="slide-desc" class="text"></p>
									<p id="slide-nav"></p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End : Image slider -->
				
				<div class="boxshade scrollable" style="max-width:938px; height: 96px;">
					<p style="text-align:justify; margin-bottom: 0px;">
						To choose an action plan for your child, click on his or her name on the right side of this screen. The action plan includes detailed information about important subject standards. It also provides standards-based activities you can do together to help your child learn and be more successful in school.
						From the "Explore" menu on the right you may view additional information, including Why Standards Matter, an easy-to-understand overview of standards and testing; and informative websites recommended by your state education agency.
					</p>
				</div>
			
				<div class="margin-bottom" style="margin-left: -20px;margin-top: -30px;">
					<div class="" style="float:left; width: 240px;">
						<div class="with-padding" style="height: 170px">
							<h4 class="blue underline">Explore</h4>
							<div class="boxshade">
								<ul class="bullet-list">
									<li><a href="#nogo" class="menu-link" action="getStandardMatters"><spring:message code="menuName.std.matter"/></a></li>
									<li><a href="#nogo" class="menu-link" action="getBrowseContent"><spring:message code="menuName.content.browse"/></a></li>
									<li><a href="resourcepdf.do?pdfFileName=/Static_PDF/ISTEP_Translation_Guide_ENGLISH.PDF"  target="_blank">
														<spring:message code="menuName.second.egsr"/>
													</a></li>
									<li><a href="resourcepdf.do?pdfFileName=/Static_PDF/ISTEP_Translation_Guide_ESPANOL.PDF"  target="_blank">
														<spring:message code="menuName.second.sgsr"/>
													</a></li>						
								</ul>
							</div>
						</div>
					</div>
					<div class="" style="margin-left: 220px;">
						<div class="with-padding" style="height: 170px; margin-right: -19px;">
							<h4 class="blue underline">Manage My Account</h4>
							<div class="boxshade scrollable" style="height: 126px;">
								<p>Use this page to update your personal information. You can also view information about which test data is available on the Parent Network for your school.</p>
								<ul class="bullet-list">
									<li><a href="myAccount.do">My Account</a></li>
									<li><a href="#nogo" class="claim-Invitation">Claim new Invitation Code</a></li>
									<!--<li><a href="#nogo">Create New Child Information</a></li>-->
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>
	</div>
	
	



