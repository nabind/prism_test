	<!-- Title bar -->
	
		<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
		<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
		
		<script src="scripts/js/libs/modernizr.custom.js"></script>
		<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
		<script src="scripts/js/setup.js"></script>
		<script src="scripts/js/highchart/highcharts-2.3.2.src.js"></script>
		<script src="scripts/js/highchart/default.service.js"></script>
		<script src="scripts/js/highchart/item.hyperlink.service.js"></script>
		
		<div id="logo" class="newStyle drilldown-logo" style="margin:0 50px !important">
			<h1><span class="logo-title"> <img alt="" src="<spring:theme code="contract.logo.header" />"> 	
				</span>
			<br>
			<span class="description"> </span></h1>
		</div>
		<% if(request.getSession().getAttribute(IApplicationConstants.CURRUSER) != null) { %>
		<div id="" class="" style="text-align:right;margin: -26px 64px 0 0; z-index:101;position: relative;">
			<h5><span class="logo-title blue">Welcome:</span>
				<sec:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
					
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR">
					<span class="name"><%=request.getSession().getAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME) %></span>
					as <span class="name black"><b><%=request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY) %></b></span>
					
				</sec:authorize>
				
			</h5>
		</div>
		<%}%>
		
	<div class="panel-control align-right padding-right margin-top rounded-border drilldown-pagination" style="padding:0 5px 0 5px; margin:0 50px !important">
		<div class="float-left drilldown-head-pagination" count="_TAB_COUNT_">
			<%if(request.getAttribute("totalPages") != null) { %>
				<%if((Integer) request.getAttribute("nextPage") == 1) { %>
					<a href="#" class="page_first paginate button compact icon-previous grey-gradient glossy with-tooltip disabled" title="First" page="0" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
					<a href="#" class="page_prev paginate button compact icon-backward grey-gradient glossy with-tooltip disabled" title="Prev" page="<%=request.getAttribute("prevPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
				<%} else {%>
					<a href="#" class="page_first paginate button compact icon-previous grey-gradient glossy with-tooltip" title="First" page="0" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
					<a href="#" class="page_prev paginate button compact icon-backward grey-gradient glossy with-tooltip" title="Prev" page="<%=request.getAttribute("prevPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
				<%} %>
				<%if((Integer) request.getAttribute("nextPage") > (Integer) request.getAttribute("lastPage")) { %>
					<a href="#" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip disabled" title="Next" page="<%=request.getAttribute("nextPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
					<a href="#" class="page_last paginate button compact icon-next grey-gradient glossy with-tooltip disabled" title="Last" page="<%=request.getAttribute("lastPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
				<%} else {%>
					<a href="#" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Next" page="<%=request.getAttribute("nextPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
					<a href="#" class="page_last paginate button compact icon-next grey-gradient glossy with-tooltip" title="Last" page="<%=request.getAttribute("lastPage")%>" param="_REPORT_URL_" count="_TAB_COUNT_"></a>
				<%} 
			}
			%>
		</div>
		<div class="float-right drilldown-head-export" count="_TAB_COUNT_">
			<a href="#" class="button margin-left glossy compact with-tooltip" 
				title="Download report as PDF" param="_REPORT_URL_" count="_TAB_COUNT_" assessment="_ASSESSMENT_" onClick="downloadPdf()">
				<span class="button-icon blue-gradient report-btn"><span class="icon-page-list"></span></span>
				PDF
			</a>
			<!-- <a href="#" class="button margin-left glossy compact with-tooltip" 
				title="Download report as Excel" param="_REPORT_URL_" count="_TAB_COUNT_" assessment="_ASSESSMENT_" onClick="downloadXls()">
				<span class="button-icon blue-gradient report-btn"><span class="icon-page-list"></span></span>
				Excel
			</a> -->
		</div>
	</div>
	
