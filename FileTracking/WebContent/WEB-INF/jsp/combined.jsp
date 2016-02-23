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
				"aaSorting": [[ 2, "asc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
			
			oTable1 = $('#erbucket').dataTable({
				"bJQueryUI": true,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 2, "asc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
			
			oTable2 = $('#ererror').dataTable({
				"bJQueryUI": true,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 9, "desc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
			
			oTable3 = $('#oaspperror').dataTable({
				"bJQueryUI": true,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 3, "asc" ]],
				"aoColumnDefs": [ 
								  { "bVisible": false, "aTargets": [ 0 ] }
								]
			});
			
			$("#saveComment").click(function(){
				var comments = $("textarea#comments").val();
				var stateCode = $("#stateCode").val();
				var UUID = $("#uuid").val();
				
				if(stateCode.length != 0) {
					$("#commentErrorLog").text("");
					//if(comments.length == 0) {
					//	$("#commentErrorLog").text("No comment");
					//} else {
						var dataString = "comments="+comments+"&stateCode="+stateCode+"&uuId="+UUID;
						$.ajax({
						      type: "POST",
						      url: "saveComments.htm",
						      data: dataString,
						      success: function(data) {
						    	  if(data.indexOf("sucessfully") > 0) {
						    		  $("#commentErrorLog").css("color","green");
						    	  } else {
						    		  $("#commentErrorLog").css("color","red"); 
						    	  }						    	  
								  $("#commentErrorLog").text(data);
						      },
							  error: function(data) {
								  $("#commentErrorLog").css("color","red");
								  $("#commentErrorLog").text(data);
							  }
					    });
						
				//	}
				} else {
					$("#commentErrorLog").text("Please enter a State Code in the search box and click Save again.");
				}
			});
			
			var saveComments = $("#savedComments").val();
			if(saveComments != "null")
				$("textarea#comments").val(saveComments);
	    });
		
		window.onbeforeunload = function() {
			var comments = $("textarea#comments").val();
			var statusMsg = $("#commentErrorLog").text();
			if(comments.length > 0 && statusMsg.length == 0) {
				return "If you do not click \"Save\" the comments will not be saved. Do you want to continue without saving?";
			} 
		}
		
	</script>
	
<div id="heromaskarticle">
 <table>
 	<tr>
 		<td>
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
 		</td>
 		<td>
 			<div class="container" style="margin-left:25px">
 				<div>
 					<div class="commentContent">
 					<label>Comments</label> <button id="saveComment" >Save</button>
 					</div>
 				</div>
 				<div>
 					<textarea rows="10" cols="40" id="comments"></textarea>
 				</div> 				
 			</div>
 			<div id='errorLogDialog' title='Loading'>
						<p id="commentErrorLog" style='font-size:13px; font-weight:bold; color:red;'></p>
			</div>
 		</td>
 	</tr>
 </table>
				
				
				
		<div id="articlecontent">
			<div id="accordion" style="margin-top:25px">
				<!-- panel -->
				<h4>ER Data Compared to Operational (PRISM report data)</h4>
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
						<th>SS</th>
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
						<td><%=process.getSs() %></td>
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
				
				<% String savedComments = (String)request.getAttribute("savedComments"); %>
				<input type="hidden" value="<%=savedComments%>" id="savedComments"/>
				
				<h4> ER Bucket: </h4>
				<table id="erbucket" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Lock?</th>
						<th>State</th>
						<th>UUID</th>
						<th>Name</th>
						<th>Schedule Id</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Test Code</th>
						<th>Date test Taken</th>
						<th>Mode</th>
						<th>Lock Id</th>
						<th>Barcode</th>
						<th>Created Date</th>
						<th>Updated Date</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<StudentDetailsTO> erBucket = (ArrayList) request.getAttribute("erBucket");
				if(erBucket != null) {
				for(StudentDetailsTO bucket : erBucket) {
				%>
					<tr>
						<td>&nbsp;</td>
						
						<td style="padding-top: 12px;" nowrap>
							<%if(bucket.getPpOaslinkedId() != null && bucket.getPpOaslinkedId().length() > 0) { %>
								<span class="locked" title="Completed"></span> 
							<%} else {%>
								<span class="unlocked" title="Error"></span> 
							<%} %>
						</td>
						
						<td><%=bucket.getStateCode() %></td>
						<td><%=bucket.getUuid() %></td>
						<td><%=bucket.getStudentName() %></td>
						<td><%=bucket.getScheduleId() %></td>
						<td><%=bucket.getContantArea() %></td>
						<td><%=bucket.getForm() %></td>
						<td><%=bucket.getContentTestCode() %></td>
						<td><%=bucket.getDateScheduled() %></td>
						<td><%=bucket.getSourceSystem() %></td>
						<td><%=bucket.getPpOaslinkedId() %></td>
						<td><%=bucket.getBarcode() %></td>
						<td><%=bucket.getCreatedDate() %></td>
						<td><%=bucket.getUpdatedDate() %></td>
					</tr>
				
				
				<%}
				}
				%>
				</tbody>
				</table>
				
				<br/><br/>
				
				<h4> Latest ER Error: </h4>
				<table id="ererror" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Schedule Id</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Test Code</th>
						<th>Mode</th>
						<th>Barcode</th>
						<th>Test Date</th>
						<th>Description</th>
						<th>Date Received</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<StudentDetailsTO> erError = (ArrayList) request.getAttribute("erError");
				if(erError != null) {
				for(StudentDetailsTO error : erError) {
				%>
					<tr>
						<td>&nbsp;</td>
						<td><%=error.getScheduleId() %></td>
						<td><%=error.getContantArea() %></td>
						<td><%=error.getForm() %></td>
						<td><%=error.getContentTestCode() %></td>
						<td><%=error.getSourceSystem() %></td>
						<td><%=error.getBarcode() %></td>
						<td>
							<% 	
								String testDate = "";
								if(error.getTestDate() != null && error.getTestDate().length() >= 8 ) { 
									testDate = error.getTestDate().substring(0, 4) + "-" + error.getTestDate().substring(4, 6) + "-" + error.getTestDate().substring(6, 8);
								}
							%>
							<%=testDate %></td>
						<td><%=error.getErrorLog() %></td>
						<td><%=error.getCreatedDate() %></td>
					</tr>
				
				<%}
				}
				%>
				</tbody>
				</table>
				
				<br/><br/>
				
				<h4> Latest OAS/PP Error: </h4>
				<table id="oaspperror" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Source</th>
						<th>L Name</th>
						<th>Test Element Id</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Barcode</th>
						<th>Test Date</th>
						<th>Description</th>
						<th>Date Received</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<StudentDetailsTO> oasPpError = (ArrayList) request.getAttribute("oasPpError");
				if(oasPpError != null) {
				for(StudentDetailsTO error : oasPpError) {
				%>
					<tr>
						<td>&nbsp;</td>
						
						<td><%=error.getSourceSystem() %></td>
						<td><%=error.getStudentName() %></td>
						<td><%=error.getTestElementId() %></td>
						<td><%=error.getContantArea() %></td>
						<td><%=error.getForm() %></td>
						<td><%=error.getBarcode() %></td>
						<td><%=error.getTestDate() %></td>
						<td><%=error.getErrorLog() %></td>
						<td><%=error.getCreatedDate() %></td>
					</tr>
				
				<%}
				}
				%>
				</tbody>
				</table>
				
				<br/><br/>
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
