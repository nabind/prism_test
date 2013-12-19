
	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	
	<noscript class="message black-gradient simpler">Your browser does not support JavaScript! Some features won't work as expected...</noscript>

	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1>Welcome, <%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></h1>
	</hgroup>

	


	<div class="right-column margin-bottom-medium" style="min-height:680px;">
			

		<div class="right-column-200px">

			<div class="right-column" style="min-width: 250px; width:22%">
			<div class="" style="height: auto; margin-top:-45px;">
				<h3 style="position:relative">Your Children</h3>
				<!--<p class="big-message blue-gradient">-->
				<p class="big-message blue-gradient" style="color : #fff">
					<!--Choose one of the profiles below to see an individualized Action Plan for your child.-->
					Select your child's name below to see their assessment results:
					<br/><br/>
					<span class="children-list"></span>
				</p>
			</div></div>

			<div class="left-column" style="margin-right: 270px;"><div class="" style="height: auto">
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
				
				<div class="boxshade" style="max-width:938px">
					<p style="text-align:justify">
					In partnership with CTB/McGraw-Hill, ACSI provides member schools a standardized assessment program called TerraNova3, ACSI Edition. It combines TerraNova3 with the ACSI Bible Assessment. <br><br>				
					TerraNova3, ACSI Edition is a next-generation achievement test that provides a research-based test blueprint that aligns to today's challenging content and performance standards. It assists educators in their plan to enhance classroom instruction and improve student achievement, and to empower all students to succeed.<br><br>
					The Bible Assessment allows Christian school educators to assess students Bible knowledge and understanding and application of Scripture. The assessment also assists with evaluating the Bible curriculum and promoting dialogue among the staff and the school community.
						<!--To choose an Action Plan for your child, click on his or her name on the right side of this screen. The Action Plan includes detailed information about important subject standards. It also provides standards-based activities you can do together to help your child learn and be more successful in school.
						Explore the links below for additional information, including <i>Why Standards Matter</i>, an easy-to-understand overview of standards and testing; and informative websites recommended by your state education agency.-->
					</p>
				</div>
				
				<div class="" style="max-width:980px">
					<h4 class="blue">Manage Account</h4>
					<div class="boxshade">
						<p>Use this page to update your personal information. You can also view information about which test data is available on ACSI Data Online for Parents for your school.</p>
						<ul class="bullet-list">
							<li><a href="myAccount.do">My Account</a></li>
							<li><a href="#nogo" class="claim-Invitation">Enter next Activation Code</a></li>
							<!--<li><a href="#nogo">Create New Child Information</a></li>-->
						</ul>
					</div>
				</div>
				
			</div></div>

			<%@ include file="claimNewInvitation.jsp"%>

		</div>
		
		
		
	</div>
	



