	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	<!-- Sidebar/drop-down menu -->
	<section id="menu" role="complementary" style="margin-top:34px; width:262px;">
		<c:set var="menuMap" value="<%=request.getSession().getAttribute(IApplicationConstants.MENU_MAP) %>" />
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
					<span class="name"><%=request.getSession().getAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME) %></span>
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
				
			<!-- Commented as of now
			
			 <ul id="access" class="children-tooltip">
					<li>
							<a href="Javascript:getfaq();" title="FAQ"><span class="icon-directions icon-size2"></span></a>
					</li>
					<li>
							<a href="Javascript:getHelp();" title="HELP"><span class="icon-read icon-size2"></span></a>
					</li>
			</ul>	
			
			 -->

			<div id="adminMenuContainer">
				<sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_SUPER, ROLE_CTB">
					<ul class="big-menu blue-gradient display-none collapsible" id="adminMenu">
						<sec:authorize ifAnyGranted="ROLE_ADMIN">
						<%@ include file="resources.jsp"%>
						<c:if test="${not empty menuMap['Manage Users']}">
						<li>
							<a href="${menuMap['Manage Users']}">Manage Users</a>
						</li>
						</c:if>
						<c:if test="${not empty menuMap['Manage Organizations']}">
						<li>
							<a href="${menuMap['Manage Organizations']}">Manage Organizations</a>
						</li>
						</c:if>
						<%-- <sec:authorize ifNotGranted="ROLE_SSO"> --%>
						<c:if test="${not empty menuMap['Manage Parents']}">
							<li>
								<a href="${menuMap['Manage Parents']}">Manage Parents</a>
							</li>
						</c:if>
						<c:if test="${not empty menuMap['Manage Students']}">
							<li>
								<a href="${menuMap['Manage Students']}">Manage Students</a>
							</li>
						</c:if>
						<%-- </sec:authorize> --%>
						<sec:authorize ifAnyGranted="ROLE_SUPER">
							<!-- Remove Manage Education Center -->
							<c:if test="${not empty menuMap['Manage Reports']}">
							<li>
								<a href="${menuMap['Manage Reports']}">Manage Reports</a>
							</li>
							</c:if>
							<c:if test="${not empty menuMap['Manage Content']}">
							<li>
								<a href="${menuMap['Manage Content']}">Manage Content</a>
							</li>
							</c:if>
						</sec:authorize>
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_SUPER, ROLE_CTB">
							<c:if test="${not empty menuMap['Reset Password']}">
							<li><a href="${menuMap['Reset Password']}">Reset Password</a></li>
							</c:if>
						</sec:authorize>
					</ul>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_PARENT">
					<ul class="big-menu blue-gradient collapsible" id="parentMenu">
						
						<li class="with-right-arrow" id="select-tooltip-1">
							<span><spring:message code="menuName.first.children"/></span>
								<ul class="big-menu white-gradient collapsible" id="child_list">
								</ul>
						</li>
						
						<c:if test="${not empty childDataMap}">
							<li class="title-menu">Action Plan</li>
							<li class="menu-second-level" id="select-tooltip-10">
								<!--  
								<a class="menu-link" href="#nogo" 
									action="getChildData"
									studentBioId="${childDataMap.studentBioId}"
									studentName="${childDataMap.studentName}"
									studentGradeName="${childDataMap.studentGradeName}"
									studentGradeId="${childDataMap.studentGradeId}"
									id="">
										<spring:message code="menuName.children.overview"/>${childDataMap.studentName}
								</a>
								-->
								<a href="getChildData.do?testElementId=${childDataMap.testElementId}&studentBioId=${childDataMap.studentBioId}&studentName=${childDataMap.studentName}&studentGradeName=${childDataMap.studentGradeName}&studentGradeId=${childDataMap.studentGradeId}" >
									Overview of ${childDataMap.studentName}
								</a>
							</li>
						</c:if>
						
						<c:forEach var="subtestTO"	items="${childDataMap.studentSubtest}"	varStatus="loopSubtestTO">
							<li class="menu-second-level with-right-arrow black-arrow">
								<a href="#nogo" 
								id="">${subtestTO.name}</a>
										<ul class="big-menu white-gradient collapsible">
												<li class="menu-third-level">
													<a class="standard-activity" href="#" 
														subtestId="${subtestTO.value}" 
														studentName="${childDataMap.studentName}" 
														studentGradeId="${childDataMap.studentGradeId}" 
														studentGradeName="${childDataMap.studentGradeName}" 
														studentBioId="${childDataMap.studentBioId}" 
														id="">
															<spring:message code="menuName.content.stdAct"/>
													</a>
												</li>
												
												<li class="menu-third-level">
													<a class="standard-indicator" href="#"  
														subtestId="${subtestTO.value}" 
														studentName="${childDataMap.studentName}" 
														studentGradeId="${childDataMap.studentGradeId}" 
														studentGradeName="${childDataMap.studentGradeName}" 
														studentBioId="${childDataMap.studentBioId}" 
														id="">
															<spring:message code="menuName.content.stdInd"/>
													</a>
												</li>	
												
												<li class="menu-third-level">
													<a class="articledata" href="#" 
														subtestId="${subtestTO.value}"
														studentName="${childDataMap.studentName}" 
														studentGradeId="${childDataMap.studentGradeId}" 
														studentGradeName="${childDataMap.studentGradeName}" 
														studentBioId="${childDataMap.studentBioId}" 
														menuId = '<spring:message code="menuId.content.rsc"/>'  
														menuName = '<spring:message code="menuName.content.rsc"/>'
														contentType = '<spring:message code="val.contentType.rsc"/>' 
														id="">
															<spring:message code="menuName.content.rsc"/>
													</a>
												</li>
													
												<li class="menu-third-level">
													<a href="#" class="studResult"
														subtestId="${subtestTO.value}" 
														studentGradeId="${childDataMap.studentGradeId}" 
														studentBioId="${childDataMap.studentBioId}" 
														custProdId = "${subtestTO.other}" 
														id="">
														<spring:message code="menuName.stud.result"/>
													</a>
												</li>	
										</ul>
						   </li>
						</c:forEach>
						<c:if test="${not empty childDataMap}">
							<li class="menu-second-level">
								<a class="grade-link" action="getArticleDescription" studentGradeId="${childDataMap.studentGradeId}" 
											studentGradeName="${childDataMap.studentGradeName}" studentBioId="${childDataMap.studentBioId}"  
											menuId = '<spring:message code="menuId.content.eda"/>' 
											menuName = '<spring:message code="menuName.content.eda"/>' 
											contentType = '<spring:message code="val.contentType.eda"/>' 
											style="font-weight: bold"
											href="#" id="">-  <spring:message code="menuName.content.eda"/>
								</a>
							</li>
							<li class="menu-second-level">
								<a class="grade-link" action="getArticleDescription" studentGradeId="${childDataMap.studentGradeId}" 
											studentGradeName="${childDataMap.studentGradeName}" studentBioId="${childDataMap.studentBioId}"  
											menuId = '<spring:message code="menuId.content.att"/>' 
											menuName = '<spring:message code="menuName.content.att"/>' 
											contentType = '<spring:message code="val.contentType.att"/>' 
											style="font-weight: bold"
											href="#" id="">-  <spring:message code="menuName.content.att"/>
								</a>
							</li>
						</c:if>
						
						<li class="with-right-arrow" id="select-tooltip-10">
							<span><spring:message code="menuName.first.explore"/></span>
								<ul class="big-menu white-gradient collapsible">
									 <li class="menu-second-level"> 
									 	<a class="menu-link" href="#" 
									 		action="getStandardMatters"  
									 		id="">
									 			<spring:message code="menuName.std.matter"/>
									 	</a>
									 </li>
									 
									 <li class="menu-second-level with-right-arrow black-arrow"> 
									 	<span><spring:message code="menuName.content.browse"/></span>
									 		 <ul class="big-menu white-gradient collapsible">
											 		<li class="menu-third-level">
											 			<a class="menu-link" href="#" 
															action="getBrowseContent" id="">
																- <spring:message code="menuName.content.overview"/>
														</a>
											 		</li>
											 		<li class="menu-third-level">
												 		<a class="browse-content" href="#" 
	           												menuId='<spring:message code="menuId.content.stdAct"/>' 
											           		menuName='<spring:message code="menuName.content.stdAct"/>'  
											           		id=""> -  <spring:message code="menuName.content.stdAct"/>
											           	</a>
											        </li>
											 		<li class="menu-third-level">
											 			<a class="browse-content" href="#" 
															menuId='<spring:message code="menuId.content.stdInd"/>' 
											           		menuName='<spring:message code="menuName.content.stdInd"/>'  
											         		id=""> -  <spring:message code="menuName.content.stdInd"/> 
											         	</a>
											 		</li>
											 		<li class="menu-third-level">
											 			<a class="browse-content" href="#" 
															menuId='<spring:message code="menuId.content.rsc"/>' 
											           		menuName='<spring:message code="menuName.content.rsc"/>'
											           		id=""> -  <spring:message code="menuName.content.rsc"/>
											           	</a>
													</li>
											 		<li class="menu-third-level">
											 			<a class="browse-content" href="#" 
															menuId='<spring:message code="menuId.content.eda"/>' 
											           		menuName='<spring:message code="menuName.content.eda"/>'
											           		id=""> -  <spring:message code="menuName.content.eda"/> 
											           	</a>
													</li>
											 		<li class="menu-third-level">
											 			<a class="browse-content" href="#"
															menuId='<spring:message code="menuId.content.att"/>' 
											           		menuName='<spring:message code="menuName.content.att"/>' 
															id=""> -  <spring:message code="menuName.content.att"/> 
														</a>
													</li>
											 	</ul>
									 </li>
									 
									 <li class="menu-second-level"> 
									 	<a href="resourcepdf.do?pdfFileName=/Static_PDF/ISTEP_Translation_Guide_ENGLISH.PDF"  target="_blank">
									 		<spring:message code="menuName.second.egsr"/>
									 	</a>
									 </li>
									 
									 <li class="menu-second-level"> 
									 	<a href="resourcepdf.do?pdfFileName=/Static_PDF/ISTEP_Translation_Guide_ESPANOL.PDF"  target="_blank">
									 		<spring:message code="menuName.second.sgsr"/>
									 	</a>
									 </li>
									 
									 <li class="menu-second-level"> 
									 	<a href="resourcepdf.do?pdfFileName=/Static_PDF/Parent_Network_User_Guide.pdf"  target="_blank">
									 		User Guide
									 	</a>
									 </li>
									  
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
							<span><spring:message code="menuName.first.mngac"/></span>
								<ul class="big-menu white-gradient collapsible">
									<li class="menu-third-level">
										<a href="myAccount.do">
											<spring:message code="menuName.second.myac"/>
										</a>
									</li>
									<li class="menu-third-level">
										<a class="claim-Invitation" href="#">
											<spring:message code="menuName.second.enac"/>
										</a>
									</li>
								</ul>
						</li>
					</ul>
				</sec:authorize>
			</div>
			<sec:authorize ifNotGranted="ROLE_PARENT">
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
			</div>
			</sec:authorize>
			<div style="background-color:#FFF">
				<div id="productImage" class="with-small-padding align-center" style="border-bottom:1px solid #5f80ab;">
					<img id="productImage101" width="250px" class="productImage" src="<spring:theme code="contract.logo.single" />" />
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