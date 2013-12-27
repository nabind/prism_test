	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<!-- Sidebar/drop-down menu -->
	<section id="menu" role="complementary" style="margin-top:34px; width:262px;">
        <c:set var="tascProduct" value="${PDCT_NAME}"/>
	    <c:set var="product" value="<%=request.getSession().getAttribute(IApplicationConstants.PRODUCT_NAME) %>"/>
		<c:set var="currOrg" value="<%=request.getSession().getAttribute(IApplicationConstants.CURRORG) %>"/>
		
		<!-- This wrapper is used by several responsive layouts -->
		<div id="menu-content">

			<header class="blue-gradient glossy">
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					Administrator
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_ACSI">
					<sec:authorize ifNotGranted="ROLE_ADMIN">
						State
					</sec:authorize>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_SCHOOL">
					<sec:authorize ifNotGranted="ROLE_ADMIN">
						School
					</sec:authorize>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_CLASS">
					Group
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_PARENT">
					Parent
				</sec:authorize>
			</header>

			<div id="profile">
				<img src='<spring:theme code="user.png" />' width="38" height="38" alt="User name" class="user-icon">
				Welcome
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><%=request.getSession().getAttribute(IApplicationConstants.PREV_ADMIN) %></span>
					<span style="color:#FFF">as </span> <span class="name"  style="padding-left: 45px;"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
				</sec:authorize>
			</div>

			<!-- By default, this section is made for 4 icons, see the doc to learn how to change this, in "basic markup explained" -->
			<ul id="access" class="children-tooltip">
				<li><a href="dashboards.do" title="Home"><span class="icon-home"></span></a></li>
				<sec:authorize ifNotGranted="ROLE_SSO">
				<li>
					<a href="myAccount.do" title="My Account">
						<span class="icon-user"></span>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<li><a href="j_spring_security_exit_user" title="Logout"><span class="icon-extract"></span></a></li>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<li><a href="j_spring_security_logout" title="Logout"><span class="icon-extract"></span></a></li>
				</sec:authorize>
			</ul>

			<div class="" id="prismMenu">
				<ul class="big-menu blue-gradient collapsible display-none" id="tempMenu">
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
					<li class="with-right-arrow">
						<span class="loader big"></span>
					</li>
				</ul>
				<sec:authorize ifNotGranted="ROLE_EDU_ADMIN">
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<ul class="big-menu blue-gradient display-none collapsible" id="adminMenu">
						<%@ include file="resources.jsp"%>
						<li>
							<a href="userModule.do">Manage Users</a>
						</li>
						<li>
							<a href="manageOrganizations.do">Manage Organizations</a>
						</li>
						<c:if test="${product ne tascProduct}">
						 <li>
							<a href="manageParent.do">Manage Parents</a>
						</li>
						</c:if>
						<li>
							<a href="manageStudent.do">Manage Students</a>
						</li>
						<sec:authorize ifAnyGranted="ROLE_CTB">
						
							<c:if test="${product eq tascProduct}">
								<li>
									<a href="manageEducationCenter.do">Manage Education Center Users</a>
								</li>
							</c:if>
							<li>
								<a href="manageReports.do">Manage Reports</a>
							</li>
							<li>
								<a href="manageRole.do">Manage Roles</a>
							</li>
							<li>
								<a href="manageContent.do">Manage Content</a>
							</li>
						</sec:authorize>
						</sec:authorize>
			<sec:authorize ifAnyGranted="ROLE_EDU_ADMIN">
				<!-- Only for ROLE_EDU_ADMIN Admins -->
				<c:if test="${product eq tascProduct}">
					<li><a href="manageEducationCenter.do" class="shortcut-medias" title="Manage Education Center Users">Manage Education Center Users</a></li>
				</c:if>
			</sec:authorize>
					</ul>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_PARENT">
					<ul class="big-menu blue-gradient collapsible" id="parentMenu">
						
						<li class="with-right-arrow" id="select-tooltip-1">
							<span>Your Children</span>
								<ul class="big-menu report-menu white-gradient" id="child_list">
								</ul>
						</li>
						
						<li class="with-right-arrow" id="select-tooltip-10">
							<span>Overview</span>
								<ul class="big-menu report-menu white-gradient" id="overView_list">
								<c:forEach var="subtestTO"	items="${childDataMap.studentSubtest}"	varStatus="loopSubtestTO">
								<li class="mid-margin-left font-12 small-line-height">
								<a href="getStandardActivity.do?subtestId=${subtestTO.value}&studentName=${childDataMap.studentName}&studentGradeId=${childDataMap.studentGradeId}&studentBioId=${childDataMap.studentBioId}&studentGradeName=${childDataMap.studentGradeName}" 
								id="subtestIdLink">${subtestTO.name}</a></li>
								</c:forEach>
								<li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">Everyday Activities</a></li>
								<li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">About the Tests</a></li>
								</ul>
						</li>
						<li class="with-right-arrow" id="select-tooltip-10">
							<span>Explore</span>
								<ul class="big-menu report-menu white-gradient">
								 <li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">Why Standards Matter</a></li>
								 <li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">Browse Content </a></li>
								 <li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">English Guide to the Student Report </a></li>
								 <li class="mid-margin-left font-12 small-line-height"> <a href="#"  id="">Spanish Guide to the Student Report  </a></li> 
								</ul>
						</li>
						
						<c:forEach var="assessments" items="${assessmentList}" varStatus="loop">
							<c:forEach var="report" items="${assessments.reports}" varStatus="innerloop">
								<sec:authorize ifAnyGranted="${report.allRoles}">
									<c:set var="parentReport" value="true"/>
								</sec:authorize>
							</c:forEach>
						</c:forEach>
						
						<li class="with-right-arrow" id="select-tooltip-2">
							<span>Manage Account</span>
							<div id="select-context-2" class="secondLevelMenu display-none">
								<ul class="big-menu report-menu white-gradient">
									<li class="mid-margin-left font-12 small-line-height"><a id="myaccount" href="myAccount.do"> My Account</a></li>
									<c:if test="${parentReport != 'true'}">
										<li class="mid-margin-left font-12 small-line-height"><a class="claim-Invitation" href="#nogo"> Enter next Activation Code</a></li>
									</c:if>
								</ul>
							</div>
						</li>
						
						
						
						<c:if test="${parentReport == 'true'}">
						<li class="with-right-arrow" id="select-tooltip-3">
							<span>Reports</span>
							<div id="select-context-3" class="secondLevelMenu display-none">
								<ul class="big-menu report-menu white-gradient">
								<c:forEach var="assessments" items="${assessmentList}" varStatus="loop">
									<c:forEach var="report" items="${assessments.reports}" varStatus="innerloop">
										<sec:authorize ifAnyGranted="${report.allRoles}">
											<li class="mid-margin-left font-12 small-line-height"><a class="" href="#nogo" onclick="addReportTab('${report.reportUrl}', '${report.reportId}', '${report.reportName}', '${assessments.assessmentId}')"> ${report.reportName}</a></li>
										</sec:authorize>
									</c:forEach>
								</c:forEach>
								</ul>
							</div>
						</li>
						</c:if>
						<%@ include file="resources.jsp"%>
					</ul>
				</sec:authorize>
			</div>
			<div style="background-color:#FFF">
				<div id="productImage" class="with-small-padding align-center" style="border-bottom:1px solid #5f80ab;">
					<img id="productImage101" class="productImage" src="<spring:theme code="contract.logo.single" />" />
			    	<!--  <img id="productImage102" class="productImage display-none" src="themes/acsi/img/PTCSLogo.jpg" height="75px"/>
					<img id="productImage103" class="productImage display-none" src="themes/acsi/img/InViewLogo.jpg" height="75px"/>
					<img id="productImage104" class="productImage display-none" src="themes/acsi/img/BibleLogo.jpg" height="55px"/>
					<img id="productImage105" class="productImage display-none" src="themes/acsi/img/TerranovaLogo.jpg" height="40px"/>
					<img id="productImage1001" class="productImage display-none" src="<spring:theme code="contract.logo.single" />" height="40px"/>
					<img id="productImage1000" class="productImage display-none" src="<spring:theme code="contract.logo.single" />" height="40px"/>-->
				</div>
			</div>

		</div>
		<!-- End content wrapper -->


	</section>
	<!-- End sidebar/drop-down menu -->