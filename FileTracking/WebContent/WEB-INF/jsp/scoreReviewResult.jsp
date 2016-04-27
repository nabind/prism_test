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
				<b>Showing search results for</b> Source Syatem: <%=searchProcess.getProcessStatusDesc()%>
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
							<td><a href="#">Review</a></td>
						</tr>
						<%} %>
					</tbody>
				</table>
			
				<div id="moreInfoDialog" title="Loading ..." style='display:none; font-size:11px'>
					<p id="moreInfo"><p><p>
					<table width="100%" class="process_details">
						<tr>
							<td width="44%"><b>Imaging ID :</b></td><td width="56%"><span id="imagingId"></span></td>
						</tr>
						<tr>
							<td><b>OrgTPName :</b></td><td><span id="orgTpName"></span></td>
						</tr>
						<tr>
							<td><b>LastName :</b></td><td><span id="lastName"></span></td>
						</tr>
						<tr>
							<td><b>FirstName :</b></td><td><span id="firstName"></span></td>
						</tr>
						<tr>
							<td><b>MiddleInitial :</b></td><td><span id="middleInitial"></span></td>
						</tr>
						<tr>
							<td><b>LithoCode :</b></td><td><span id="lithoCode"></span></td>
						</tr>
						<tr>
							<td><b>StudentScanStk :</b></td><td><span id="scanStack"></span></td>
						</tr>
						<tr>
							<td><b>StudentScanSeq :</b></td><td><span id="scanSequence"></span></td>
						</tr>
						<tr>
							<td><b>WinscoreDocId :</b></td><td><span id="winsDocId"></span></td>
						</tr>
						<tr>
							<td><b>Commodity Code :</b></td><td><span id="comodityCode"></span></td>
						</tr>
						<tr>
							<td><b>WinscoreStatus :</b></td><td><span id="winStatus"></span></td>
						</tr>
						<tr>
							<td><b>Prism Status :</b></td><td><span id="prismProcessStatusDesc"></span></td>
						</tr>
						<tr>
							<td><b>Image File Path(s) :</b></td><td><span id="imageFilePath"></span></td>
						</tr>
						<tr>
							<td><b>Image File Name(s) :</b></td><td><span id="imageFileName"></span></td>
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
