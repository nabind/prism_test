<%@page import="com.vaannila.TO.TASCProcessTO"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<style>
	.completed {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -68px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
	.progress {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -102px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
	.error {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -136px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
</style>
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
			
			$("#erLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 500, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
			$("#stuLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 500, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
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
		
		
		
		function testElementIdList(processId) {
			var dataString = "processId="+processId;
			$("#_er_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-erLogDialog").html('Process Id : '+processId + '<br />ER validation failed for the following students. Click on the test element id for details.');
			jQuery("#erLogDialog").dialog("open");
			$.ajax({
				type: "POST",
				url: "testElementIdList.htm",
				data: dataString,
				success: function(data) {
					// alert("data=" + data);
					var testElementIds = data.split(',');
					// alert("testElementIds=" + testElementIds);
					var innerHtml = "";
					if(testElementIds && testElementIds.length > 1) {
						$(testElementIds).each(function() {
							var value = (typeof this.value !== 'undefined') ? this.value : "";
							if(value != "") {
								innerHtml += ' - <a href="#note" class="noteLink" style="color:red;text-decoration:underline" onclick="getStudentDetails('+value+','+processId+');">'+value+'</a><br />';
							}
						});
					} else {
						innerHtml += ' - <a href="#note" class="noteLink" style="color:red;text-decoration:underline" onclick="getStudentDetails('+testElementIds+','+processId+');">'+testElementIds+'</a>';
					}
					$("#_er_log").html( innerHtml );
				},
				error: function(data) {
					$("#_er_log").html( ' Failed to get ER Log' );
				}
			});
			return false;
		}
		
		function getStudentDetails(testElementId, processId) {
			var dataString = "processId="+processId+'&testElementId='+testElementId;
			$("#_stu_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-stuLogDialog").html('Student Details for Process Id : '+processId+', testElementId : '+testElementId);
			jQuery("#stuLogDialog").dialog("open");
			$.ajax({
			      type: "POST",
			      url: "getStudentDetails.htm",
			      data: dataString,
			      success: function(data) {
			    	  $("#_stu_log").html( data );
			      },
				  error: function(data) {
					  $("#_stu_log").html( ' Failed to get Student Details' );
				  }
		    });
		    return false;
		}
		
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
	 
		<div id="accordion">
				<!-- panel -->
				
				
				<table id="process" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th style="min-width: 60px;">Process Id</th>
						<th>Source System</th>
						<th>ER Validation</th>
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
						
						<td style="padding-top: 12px;" nowrap>
							<%if("CO".equals(process.getOverallStatus())) { %>
								<span class="completed" title="Completed"></span>
							<%} else if("IN".equals(process.getOverallStatus())) {%>
								<span class="progress" title="In Progress"></span>
							<%} else {%>
								<span class="error" title="Error"></span>
							<%} %>
							<%=process.getProcessId() %>
						</td>
						
						<td><%=process.getSourceSystem() %></td>
						<td>
							<%if("ER".equals(process.getErValidation())) { %>
								<a href='#note' class='noteLink' style='color:red;text-decoration:underline' onclick='testElementIdList(<%=process.getProcessId() %>);'><%=process.getErValidation() %></a>
							<%} else {%>
								<%=process.getErValidation() %>
							<%} %>
						</td>
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
				
				<p style="padding-top:20px"><b>Validation Status:</b> VA = Validated, CO = Completed, IN = In Progress, AC = Active, <span style="color: red">ER = Error</span> </p>
				<p><b>Source System:</b> PP = Paper Pencil, OL = Online (Web service)</p>
				<p><b>Overall Status:</b><br/>
					<span class="completed" title="Completed"></span> = Completed/Success <br/>
					<span class="progress" title="Completed" style="margin-left: -34px;"></span> = In Progress<br/>
					<span class="error" title="Completed" style="margin-left: -34px;"></span> = Error
				</p>
				<div id='erLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_er_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				<div id='stuLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_stu_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
			</div>

	</div>
</div>
</div>
