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

<script type="text/javascript" src="js/modules/scoreReview.js"></script>
<script src="css/jquery.validate.js"></script>	
	
<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				<% 
				SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("reviewTO");
				%>
				<div class="search-criteria">
				<b>Showing search results for</b> Source System: <%=searchProcess.getProcessStatusDesc()%>
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
							<th >&nbsp;</th>
							<th style="min-width: 60px;">Student Name</th>
							<th>UUID</th>
							<th>Processed Date</th>
							<th>State Code</th>
							<th>Subtest</th>
							<th>Reported NC</th>
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
							<td >&nbsp;</td>
							<td style="min-width: 60px;"><%=process.getStudentName() %></td>
							<td><%=process.getUuid() %></td>
							<td><%=process.getDateTimestamp() %></td>
							<td><%=process.getStateCode() %></td>
							<td><%=process.getSubtestName() %></td>
							<td><%=process.getNc() %></td>
							<td><%=process.getSs() %></td>
							<td><%=process.getHse() %></td>
							<td><a href="#" onclick="studentDetails('<%=process.getTestElementId()%>','<%=process.getSubtestName()%>')">Review</a></td>
						</tr>
						<%} %>
					</tbody>
				</table>
				<div id="reviewDialog" title="Loading ..." style='display:none; font-size:11px'>
					<p id="review"><p><p>
					<table id="scoreResultTable" width="100%">
					<thead>
						<tr>
							<th >&nbsp;</th>
							<th style="min-width: 60px;">Student Test ID</th>
							<th>Form Name</th>
							<th>New NC</th>
							<th>New SS</th>
							<th>New HSE</th>
							<th>Processed Date</th>
							<th>IsApprove</th>
						</tr>
					</thead>
					<tbody>
						<% 
						java.util.List<TASCProcessTO> scoreList = (ArrayList) request.getSession().getAttribute("reviewProcess");
						int cnt=0;
						for(TASCProcessTO scores : scoreList) {
							cnt++;
						%>
						<tr>
							<td >&nbsp;</td>
							<td style="min-width: 60px;"><%=scores.getTestElementId() %></td>
							<td><%=scores.getForm() %></td>
							<td><%=scores.getNc() %></td>
							<td><%=scores.getSs() %></td>
							<td><%=scores.getHse() %></td>
							<td><%=scores.getDateTimestamp() %></td>
							<td><input type="checkbox" name="isApprove" id="<%=cnt %>" onclick="rejectOther(this)"></td>
						</tr>
						<%} %>
					</tbody>
					
				</table>
				<table id="commentSection" width="100%">
					<tr>
					
					<td><b>Comment</b></td>
					<td><textarea rows="5" cols="50" id="commentArea"></textarea></td>
					</tr>
					<tr>
						<td><input type="button" value="Save" id="saveReview" onclick="saveReview()"></td>
					</tr>
				</table>
				</div>
				<div id='errorLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="errorLog"><img src="css/ajax-loader.gif"></img><p>
				</div>
			</div>
	</div>
</div>
</div>
