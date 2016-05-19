<%@page import="com.vaannila.TO.TASCProcessTO"%>
<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/coCheck.css" type="text/css"/>
<link href="plugin/radio_switch/css/bootstrap-switch.css" rel="stylesheet">


<script type="text/javascript" src="js/modules/scoreReview.js"></script>
<script src="plugin/radio_switch/js/bootstrap.min.js"></script>
<script src="plugin/radio_switch/js/highlight.js"></script>
<script src="plugin/radio_switch/js/bootstrap-switch.js"></script>

<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				<% 
				SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("reviewTO");
				%>
				<div class="search-criteria">
				<b>Showing search results for</b> Source System: <%=searchProcess.getSourceSystemDesc()%>
				, Status: <%=searchProcess.getProcessStatusDesc()%>
				<%if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){%>
					, Processed Date From: <%=searchProcess.getProcessedDateFrom()%>
				<%}%>
				<%if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){%>
					, Processed Date To: <%=searchProcess.getProcessedDateTo()%>
				<%}%>
				<%if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){%>
					, UUID: <%=searchProcess.getUuid()%>
				<%}%>
				<%if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){%>
					, State Code: <%=searchProcess.getStateCode()%>
				<%}%>				
				</div>
				
				<table id="scoreResultTable" width="100%">
					<thead>
						<tr>
							<th style="min-width: 100px;">Student Name</th>
							<th>UUID</th>
							<th>Processed Date</th>
							<th>State Code</th>
							<th>Subtest</th>
							<th>Reported NCR</th>
							<th>Reported SS</th>
							<th>Reported HSE</th>
							<th>Review Score(s)</th>
						</tr>
					</thead>
					<tbody>
						<% 
						java.util.List<TASCProcessTO> allProcess = (ArrayList) request.getSession().getAttribute("reviewProcess");
						int count=0;
						for(TASCProcessTO process : allProcess) {
							count++;
						%>
						<tr>
							<td style="min-width: 100px;"><%=process.getStudentName() %></td>
							<td><%=process.getUuid() %></td>
							<td><%=process.getDateTimestamp() %></td>
							<td><%=process.getStateCode() %></td>
							<td><%=process.getSubtestName() %></td>
							<td><%=process.getNc() %></td>
							<td><%=process.getSs() %></td>
							<td><%=process.getHse() %></td>
							<td><a href="#" onclick="getReviewInfo('<%=process.getStudentBioId()%>','<%=process.getSubtest()%>', '<%=process.getStudentName() %>','<%=process.getSubtestName() %>')">Review</a></td>
						</tr>
						<%} %>
					</tbody>
				</table>
				<div id="reviewDialog" title="Loading ..." style='display:none; font-size:11px;'>
					<p id="review"><p><p>
					<table id="scoreReviewTable" width="100%">
						<thead>
							<tr>
								<th>Form Name</th>
								<th>New NCR</th>
								<th>New SS</th>
								<th>New HSE</th>
								<th>Processed Date</th>
								<th>Status</th>
								<th>Comment</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						
						</tbody>
					</table>
					<div id="wait" align="center" style='display:none;'><img src='css/ajax-loader-circle.gif' width="64" height="64" /><br>Loading..</div>
					<div id='errorLogDialog' style="text-align: center">
						<p id="errorLog" style='font-size:13px; font-weight:bold;'></p>
					</div>
				</div>	
				<div id="confirmDialog" class='dialog'>
	  				Do you want to proceed?
				</div>
				<div id="messageDialog" class='dialog'>
				</div>
			</div>
	</div>	
</div>

