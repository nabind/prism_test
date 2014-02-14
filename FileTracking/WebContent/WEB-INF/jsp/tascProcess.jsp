<%@page import="com.vaannila.TO.TASCProcessTO"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<script src="css/jquery.validate.js"></script>
	
	<script type="text/javascript" charset="utf-8">
	

	
	function update_rows()
	{
	    $(".process_details tr:even").css("background-color", "#fff");
	    $(".process_details tr:odd").css("background-color", "#eee");
	}
	
		$(document).ready(function() {
			 update_rows();
			 
			oTable = $('#process').dataTable({
				"bJQueryUI": true,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 1, "desc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
			
			//$("a[rel^='prettyPhoto']").prettyPhoto();
			
			$("#processLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 500, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
			
			
		} );
		
		
		
		function getProcessLog(processId) {
			var dataString = "processId="+processId;
			
			$("#_process_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-processLogDialog").html('Log for Process Id : '+processId);
			jQuery("#processLogDialog").dialog("open");
			
			
			$.ajax({
			      type: "POST",
			      url: "getTascProcessLog.htm",
			      data: dataString,
			      success: function(data) {
			    	  $("#_process_log").html( data );
			      },
				  error: function(data) {
					  $("#_process_log").html( ' Failed to get Process Log' );
				  }
		    });
		    return false;
		}
		
		
		
		$(document).ready(function() {
		var bEnabled = document.getElementById("hEnabled").value;
		if (bEnabled == 'F'){
			document.getElementById("theSelect").setAttribute("disabled", "disabled");
			//$("#theSelect option:selected").attr('disabled', 'disabled');
		}
	    });
		
	</script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	 <%   Properties prop = PropertyFile.loadProperties("acsi.properties");
			  String isEnabled = prop.getProperty("admin.year.enable");
	 %>
		<input type="hidden" value="<%=isEnabled%>" id="hEnabled">	  
		
		<div id="accordion">
				<!-- panel -->
				
				
				<table id="process" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Process Id</th>
						<th>Source System</th>
						<th>Hier Validation</th>
						<th>Bio Validation</th>
						<th>Demo Validation</th>
						<th>Content Validation</th>
						<th>Objective Validation</th>
						<th>Item Validation</th>
						<th>Partition Name</th>
						<th>Processed Date</th>
						<th>Source File Name</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<TASCProcessTO> allProcess = (ArrayList) request.getSession().getAttribute("tascProcess");
				int count=0;
				for(TASCProcessTO process : allProcess) {
					count++;
				%>
					<tr>
						<td>&nbsp;</td>
						
						<td>
							<%=process.getProcessId() %>
						</td>
						
						<td><%=process.getSourceSystem() %></td>
						<td><%=process.getHierValidation() %></td>
						<td><%=process.getBioValidation() %></td>
						<td><%=process.getDemoValidation() %></td>
						<td><%=process.getContentValidation() %></td>
						<td><%=process.getObjValidation() %></td>
						<td><%=process.getItemValidation() %></td>
						<td><a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getProcessLog(<%=process.getProcessId() %>);'>
							<%=process.getWkfPartitionName() %></a>
						</td>
						<td><%=process.getDateTimestamp() %></td>
						<%
							String sourceFileName = process.getFileName();
							if(sourceFileName != null && sourceFileName.length() > 16) {
								sourceFileName = sourceFileName.substring(0, 16) + " " + sourceFileName.substring(16, sourceFileName.length());
							} else {
								sourceFileName = "NA";
							}
						%>
						<td><%=sourceFileName %></td>
					</tr>
				
				
				<%} %>
				</tbody>
				</table>
				
				<p style="padding-top:20px"><b>Validation Status:</b> VA = Validated, CO = Completed, IN = In Progress, AC = Active </p>
				<p><b>Source System:</b> PP = Paper Pencil, OL = Online (Web service)</p>
				
				<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
			</div>

	</div>
</div>
</div>
