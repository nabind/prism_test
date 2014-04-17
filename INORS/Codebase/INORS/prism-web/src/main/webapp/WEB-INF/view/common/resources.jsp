	<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	
	<c:set var="currOrg" value="<%=request.getSession().getAttribute(IApplicationConstants.CURRORG) %>"/>
		
	<input type="hidden" name="customerId" id="customerId" value="<%=(String) request.getSession().getAttribute(IApplicationConstants.CURRORG)%>">
	<%-- <li class="with-right-arrow" id="select-tooltip-0">
		<span>Resources</span>
		<div id="select-context-0" class="secondLevelMenu display-none">
			<ul class="big-menu report-menu white-gradient">
				<sec:authorize ifNotGranted="ROLE_PARENT">
					<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Admin_Users_Guide.pdf&userType=${currOrg}"> Admin User's Guide</a></li>
					</sec:authorize>
					
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Users_Guide.pdf&userType=${currOrg}" > User's Guide</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Score_Legend.pdf&userType=${currOrg}" > Score Legend</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Guide_Data_Online.pdf&userType=${currOrg}" > Guide to Data Online</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/ACSI_Bible_Performance_Level_Information.pdf&userType=${currOrg}"> Performance Levels - Bible</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/ACSI_TN3_Performance_Level_Information.pdf&userType=${currOrg}" > Performance Levels - Terranova</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/ACSI_2012_Linking_Study_Results.pdf&userType=${currOrg}" > ACSI 2012 Linking Study Results</a></li>
					<li class="mid-margin-left font-12 small-line-height"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Longitudinal_Tracking_Prep_Info.pdf&userType=${currOrg}" > Preparation for Longitudinal Tracking</a></li>
					
					<sec:authorize ifAnyGranted="ROLE_ACSI">
					<li class="mid-margin-left font-12 small-line-height no-shadow"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/2012_School_names.pdf&userType=${currOrg}" > School Name List</a></li>
					</sec:authorize>
				</sec:authorize>	
				<sec:authorize ifAnyGranted="ROLE_PARENT">
					<li class="mid-margin-left font-12 small-line-height">
						<a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Users_Guide_For_Parents.pdf&userType=${currOrg}"> User's Guide</a>
					</li>
					<li class="mid-margin-left font-12 small-line-height no-shadow">
						<a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Parent_Guide_Understanding_Tests.pdf&userType=${currOrg}"> Basics of Assessment for Parents</a>
					</li>
				
					<!-- <li class="mid-margin-left font-12 small-line-height no-shadow"><a class="resources" onclick="downloadResources(this)" href="#nogo" param="acsipdf.do?pdfFileName=Static_PDF/Guide_Data_Online.pdf&userType=${currOrg}" target="_blank"> Guide to Data Online</a></li> -->
				</sec:authorize>
			</ul>
		</div>
	</li> --%>
	
