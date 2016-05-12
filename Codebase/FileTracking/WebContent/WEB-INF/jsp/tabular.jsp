<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="java.util.ArrayList" %>

	<link rel="stylesheet" href="css/highlight.css" type="text/css">
		<link rel="stylesheet" href="css/demo.css" type="text/css">
		<!-- jquery core -->
		<script type="text/javascript" language="javascript" src="css/jquery.dataTables.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
	
			$('#example').dataTable();
	
		});
	</script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
		<h1>Current Process 111</h1>
		
		<div id="accordion">
				<!-- panel -->
				<h3 class="accordion accordion-close" id="body-section">
					All Process
				</h3>
				
				<table id="example">
					<tr>
						<th>Process Id</th>
						<th>Created Date</th>
						<th>Updated Date</th>
						<th>Data Status</th>
						<th>PDF Status</th>
						<th>Target Status</th>
						<th>Target Email Status</th>
						<th>Process Status</th>
						<th>Process Log</th>
					</tr>
				<% 
				java.util.List<OrgProcess> allProcess = (ArrayList) request.getSession().getAttribute("allProcess");
				int count=0;
				for(OrgProcess process : allProcess) {
					count++;
				%>
				
				
					<tr>
						<td><%=process.getProcessid() %></td>
						<td><%=process.getCreatedDate() %></td>
						<td><%=process.getUpdatedDate() %></td>
						<td><%=process.getDataStatus() %></td>
						<td><%=process.getPdfStatus() %></td>
						<td><%=process.getTargetStatus() %></td>
						<td><%=process.getTargerEmailStatus() %></td>
						<td><%=process.getProcessStatus() %></td>
						<td><a href='#note' class='noteLink' style='color:#00329B;' onclick='jQuery("#dialog<%=count%>").dialog("open"); return false'>View Log</a></td>
					</tr>
					<div >
						<script>
							$(document).ready(function(){
								$("#dialog<%=count%>").dialog({bgiframe: true, autoOpen: false, modal: false, minHeight: 50, minWidth: 400, closeOnEscape: true, resizable: true});
							});
						</script>
						<div id='dialog<%=count%>' title='Log for Process # <%=process.getProcessid() %>' style='display:none; font-size:10px'><%=process.getProcessLog() %></div>
					</div>
				
				
				<%} %>
				</table>
				<!-- end panel -->
				
				
			</div>

	</div>
</div>
</div>