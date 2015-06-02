<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<style>
	.locked {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -166px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
	.unlocked {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -30px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
</style>
<script src="css/jquery.validate.js"></script>
	
	<script type="text/javascript" charset="utf-8">
	

		
		$(document).ready(function() {
			oTable = $('#process').dataTable({
				"bJQueryUI": true,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 2, "desc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
	    });
		
	</script>
	
<div id="heromaskarticle">
				
				<div class="container" style="margin-left:25px">
					<h1>Search a single student</h1>
					<div class="content">
						<%
						SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("combinedRequestTO");
						if(searchProcess == null) searchProcess = new SearchProcess();
						%>
						<form name="searchTascEr" method="post" action="combined.htm" id="searchTascErForm">
						<table width="100%">
							<tr>
								<td>UUID:</td>
								<td><input type="text" name="uuid" id="uuid" value="<% if(searchProcess.getUuid() != null) out.print( searchProcess.getUuid());  %>"></td>
							</tr>
							<tr>
								<td>Test Element ID:</td>
								<td><input type="text" name="testElementId" id="testElementId" value="<% if(searchProcess.getTestElementId() != null) out.print(searchProcess.getTestElementId()); %>"></td>
							</tr>
							<tr>
								<td>State Code:</td>
								<td><input type="text" name="stateCode" id="stateCode" value="<% if(searchProcess.getStateCode() != null) out.print(searchProcess.getStateCode()); %>"></td>
							</tr>
							<tr>
								<td></td>
								<td><input type="Submit" value="Search"></td>
							</tr>
						</table>
						</form>
					</div>
					<div id='errorLogDialog' title='Loading'>
						<p id="errorLog" style='font-size:13px; font-weight:bold; color:red;'>${message}<p>
					</div>
				</div>
				
		<div id="articlecontent">
			<div id="accordion" style="margin-top:25px">
				<!-- panel -->
				<table id="process" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Lock?</th>
						<th>State</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Test Code</th>
						<th>Mode</th>
						<th>Lock Id</th>
						<th>Barcode</th>
						<th>Status Code</th>
						<th>NCR</th>
						<th>HSE</th>
						<th>Score Date</th>
						<th>Bio ID</th>
						<th>TestElem ID</th>
						<th>Name</th>
						<th>UUID</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<StudentDetailsTO> allProcess = (ArrayList) request.getAttribute("combinedList");
				int count=0;
				if(allProcess != null) {
				for(StudentDetailsTO process : allProcess) {
					count++;
				%>
					<tr>
						<td>&nbsp;</td>
						
						<td style="padding-top: 12px;" nowrap>
							<%if(process.getPpOaslinkedId() != null && process.getPpOaslinkedId().length() > 0) { %>
								<span class="locked" title="Completed"></span> 
							<%} else {%>
								<span class="unlocked" title="Error"></span> 
							<%} %>
						</td>
						
						<td><%=process.getStateCode() %></td>
						<td><%=process.getSubtestName() %></td>
						<td><%=process.getForm() %></td>
						<td><%=process.getContentTestCode() %></td>
						<td><%=process.getSourceSystem() %></td>
						<td>
							<%
							if(process.getPpOaslinkedId() != null && process.getPpOaslinkedId().equals(process.getTestElementId())) {
							%>
								<%=process.getPpOaslinkedId() %>
							<%
							} else if("".equals(process.getPpOaslinkedId())) {
							%>
								<%=process.getPpOaslinkedId() %>
							<%} else {%>
								NA
							<%} %>
						</td>
						<td><%=process.getBarcode() %></td>
						<td><%=process.getStatusCode() %></td>
						<td><%=process.getNcr() %></td>
						<td><%=process.getHse() %></td>
						<td><%=process.getUpdatedDate() %></td>
						<td><%=process.getStudentBioId() %></td>
						<td><%=process.getTestElementId() %></td>
						<td><%=process.getStudentName() %></td>
						<td><%=process.getUuid() %></td>
					</tr>
				
				
				<%}
				}
				%>
				</tbody>
				</table>
				<br><br>
				<p><b>Source System:</b> PP = Paper Pencil, OL = Online (Web service)</p>
				<p><b>Overall Status:</b><br/>
					<span class="locked" title="Completed"></span> = Locked <br/>
					<span class="unlocked" title="Completed" style="margin-left: -34px;"></span> = Open
				</p>
				
				<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
			</div>

	</div>
</div>
</div>
