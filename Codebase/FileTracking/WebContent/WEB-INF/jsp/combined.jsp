<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.StudentDetailsGhiTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/coCheck.css" type="text/css"/>
<link rel="stylesheet" href="css/demo.css" type="text/css">
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
	.no-border {
		border: 0;
		padding: 6px
	}
</style>
<script src="css/jquery.validate.js"></script>
	
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		oTable33 = $('#processGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 10, "desc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] }
							]
		});
		
		oTable21 = $('#docStatusGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 22, "desc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] },
							  { "width": "30%", "aTargets":  [10] }
							]
		});
		
		oTable22 = $('#errorGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 27, "desc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] },
							  {'bSortable': false, 'aTargets':  [1]  },
							  { "width": "30%", "aTargets":  [9] }
							]
		});
		
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
		
		$('#showComments').click(function(){
			showComments();
		});
		
		$("#saveComment").click(function(){
			saveComment();
		});
		
		$( "#ghiTab" ).click(function() {
		  $( "#accordion1" ).toggle( "fast");
		});
		$( "#defTab" ).click(function() {
		  $( "#accordion2" ).toggle( "slow");
		});
    });
	
	function showComments() {
		$("#commentErrorLog").hide();
		$("textarea#comments").val( $("textarea#hiddenComments").val() );
		jQuery("#showCommentsDialog").dialog({
			title: 'Comments: ',
			width: 510,
			height: 300,
			modal: true,
			closeOnEscape: false,
			beforeClose: function( event, ui ) {
				if( $("#hiddenComments").text() != $("textarea#comments").val() ) {
					var $dlg = $(this);
			        if($dlg.data('can-close')) {
			            $dlg.removeData('can-close');
			            return true;
			        }
			        $("#confirm").dialog({
			            width: 500,
			            modal: true,
			            closeOnEscape: false,
			            buttons: {
			                Confirm: function() {
			                    $(this).dialog('close');
			                    $dlg.data('can-close', true);
			                    $dlg.dialog('close');
			                },
			                Cancel: function() {
			                    $(this).dialog('close');
			                }
			            }
			        });
			        return false;
				}
			}
		});
		return false;
	}
	
	function saveComment(){
		var comments = $("textarea#comments").val();
		var stateCode = $("#commentStateCode").val();
		var uuid = $("#commentUuid").val();
		if(comments.length > 4000){
			$("#commentErrorLog").show();
			$("#commentErrorLog").css("color","red");
			$("#commentErrorLog").text("Maximum length is 4000. Please summarize your comment");
		}else{
			var dataString = "comments="+comments+"&stateCode="+stateCode+"&uuId="+uuid;
			$.ajax({
			      type: "POST",
			      url: "saveComments.htm",
			      data: dataString,
			      success: function(data) {
			    	  $("#commentErrorLog").show();
			    	  if(data.indexOf("sucessfully") > 0) {
			    		  $("#commentErrorLog").css("color","green");
			    		  
			    		  // resetting page hidden values
			    		  $("#comment-div").html("<br/>" + comments);
			    		  $("#hiddenComments").text(comments)
			    	  } else {
			    		  $("#commentErrorLog").css("color","red"); 
			    	  }						    	  
					  $("#commentErrorLog").text(data);
			      },
				  error: function(data) {
					  $("#commentErrorLog").show();
					  $("#commentErrorLog").css("color","red"); 
					  $("#commentErrorLog").text(data);
				  }
		    });
		}
	}
</script>
	
<div id="heromaskarticle">
	<div id="showCommentsDialog" title="Loading ..." style='display:none; font-size:11px'>
		<p></p>
		<div class="commentContent" style="text-align: center">
			<input type="hidden" value="${uuid}" id="commentUuid"/>
			<input type="hidden" value="${stateCode}" id="commentStateCode"/>
			<div style="display:none">
				<textarea rows="10" cols="60" id="hiddenComments">${savedComments}</textarea>
			</div>
			
			<div style="padding: 10px;">
				<label style='font-size:13px; font-weight:bold; color:black;'>
					Enter Comments for UUID: ${uuid} and State Prefix: ${stateCode}
				</label>
			</div>
			<div>
				<textarea rows="10" cols="60" id="comments">${savedComments}</textarea>
			</div>
			<div style='font-size:13px; font-weight:bold;'>
				<p></p><button id="saveComment" >Save Comment</button>
			</div>
		</div>
		<div id='errorLogDialog' style="text-align: center">
			<p id="commentErrorLog" style='font-size:13px; font-weight:bold;'></p>
		</div>
	</div>
	<div id="confirm" title="Confirm" style="display:none;">
	    Comments have changed. Do you want to continue without saving?
	</div>
		
	<div class="container" style="margin-left:25px;width: 900px !important;">
		<h1>Search a single student</h1>
		<div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 488px;float: left;">
			<%
			SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("combinedRequestTO");
			String savedComments = (String)request.getAttribute("savedComments"); 
			String showCommentFlag = (String)request.getAttribute("showCommentFlag"); 
			if(searchProcess == null) searchProcess = new SearchProcess();
			%>
			<form name="searchTascEr" method="post" action="combined.htm" id="searchTascErForm">
				<table width="100%">
					<thead>
						<tr>
							<td class="no-border" colspan="2">
                            	<div id="errorLogDialog" title="Loading" style="float: left;position: absolute;">
									<p id="errorLog" style="font-size:13px; font-weight:bold; color:red;">${message}</p>
								</div>
							</td>
                         </tr>
					</thead>
					 <tbody>
					 	<tr>
                           	<td class="no-border">&nbsp;</td>
                           	<td class="no-border">&nbsp;</td>
                        </tr>
						<tr>
							<td class="no-border">UUID:</td>
							<td class="no-border"><input type="text" name="uuid" id="uuid" value="<% if(searchProcess.getUuid() != null) out.print( searchProcess.getUuid());  %>"></td>
						</tr>
						<%-- <tr>
							<td class="no-border">Test Element ID:</td>
							<td class="no-border"><input type="text" name="testElementId" id="testElementId" value="<% if(searchProcess.getTestElementId() != null) out.print(searchProcess.getTestElementId()); %>"></td>
						</tr> --%>
						<tr>
							<td class="no-border">State Prefix:</td>
							<td class="no-border"><input type="text" name="stateCode" id="stateCode" value="<% if(searchProcess.getStateCode() != null) out.print(searchProcess.getStateCode()); %>">
							<span style="color:red;">* 3 digit.</span>
							</td>
						</tr>
						<tr>
							<td class="no-border">DRC Student ID (exact ID is needed):</td>
							<td class="no-border"><input type="text" name="drcStudentId" id="drcStudentId" value="<% if(searchProcess.getDRCStudentId() != null) out.print(searchProcess.getDRCStudentId()); %>">
							<span style="color:red;">* Only for GHI</span>
							</td>
						</tr>
						<tr>
							<td class="no-border"></td>
							<td class="no-border"><input type="Submit" value="Search"></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 393px;height: 172px;float: right;">
			<% if("false".equals(showCommentFlag)){ %>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments (only for DEF)</h3>
				<div style="padding: 20px;color: red;">
					Please provide 9 digit UUID and 3 character state prefix to fetch comments.
				</div>
			<%}else{%>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments for UUID: ${uuid} and State Prefix: ${stateCode}
					<a id="showComments" href="#nogo" style="color: #00329B;text-decoration:underline;padding-left: 17px;">Add/Edit</a>
				</h3>
				<div id="comment-div" style="padding: 0px 10px;font-weight:normal;height: 141px;overflow:auto;white-space: pre-line">
					${savedComments}
				</div>
			<%}%>
		</div>
	</div>
	<div id="articlecontent">
		<h3 style="padding-top:25px;cursor: pointer;" id="ghiTab"><u>GHI Forms ONLY</u></h3>
		<div id="accordion1" style="margin-top:25px">
			<!-- panel -->			
			<h4>Data Compared to Operational (PRISM report data)</h4>
			<table id="processGhi" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>DRC Student ID</th>
						<th>State</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Mode</th>
						<th>Barcode</th>
						<th>Status Code</th>
						<th>SS</th>
						<th>HSE</th>
						<th>Score Date</th>
						<th>TestElem ID</th>
						<th>Name</th>
						<th>UUID</th>
						<th>Bio ID</th>
						<th>State Prefix</th>
						<th>Test Date</th>
						<th>Test Center Code</th>
						<th>Test Center Name</th>
						<th>Document Id</th>
						<th>Schedule Id</th>
						<th>Tca Schedule Date</th>
						<th>Test Language</th>
					</tr>
					</thead>
				<tbody>
			<% 
			java.util.List<StudentDetailsTO> allProcess = (ArrayList) request.getAttribute("combinedGhiList");
			int count=0;
			if(allProcess != null) {
			for(StudentDetailsTO process : allProcess) {
				count++;
			%>
				<tr>
					<td>&nbsp;</td>
					<td><%=process.getDrcStudentId() %></td>
					<td><%=process.getStateCode() %></td>
					<td><%=process.getSubtestName() %></td>
					<td><%=process.getForm() %></td>
					<td><%=process.getSourceSystem() %></td>
					<td><%=process.getBarcode() %></td>
					<td><%=process.getStatusCode() %></td>
					<td><%=process.getSs() %></td>
					<td><%=process.getHse() %></td>
					<td><%=process.getUpdatedDate() %></td>
					<td><%=process.getTestElementId() %></td>
					<td><%=process.getStudentName() %></td>
					<td><%=process.getUuid() %></td>
					<td><%=process.getStudentBioId() %></td>
					<td><%=process.getLevel1OrgCode() %></td>
					<td><%=process.getTestDate() %></td>
					<td><%=process.getTestCenterCode() %></td>
					<td><%=process.getTestCenterName()%></td>
					<td><%=process.getDocumentId() %></td>
					<td><%=process.getScheduleId() %></td>
					<td><%=process.getTcaScheduleDate() %></td>
					<td><%=process.getTestLanguage() %></td>
				</tr>
			<%}
			}
			%>
			</tbody>
			</table>
			
			<br/><br/>
			<h4> Document Status: </h4>
			<table id="docStatusGhi" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>State Prefix</th>
					<th>Test Mode</th>
					<th>Student Name</th>
					<th>Examinee ID (UUID)</th>
					<th>DRC Student ID</th>
					<th>Doc Process Status</th>
					<th>Expired Date</th>
					<th>Check-in Date</th>
					<th>Barcode ID</th>
					<th>Schedule ID</th>
					<th>TCA Schedule Date</th>
					<th>Date Test Taken</th>
					<th>Form</th>
					<th>Content Name</th>
					<th>Content Test Code</th>
					<th>Test Language</th>
					<th>Litho Code</th>
					<th>Field Test Form</th>
					<th>Test Event Update Date</th>
					<th>Last Updated Doc Date</th>
					<th>Org Code Path</th>
					<th>Prism Process Date</th>
					<th>Doc ID</th>
					<th>File Name</th>
					<th>File Generation Date-Time</th>
				</tr>
				</thead>
				<tbody>
			<% 
			java.util.List<StudentDetailsGhiTO> docStatusGhi = (ArrayList) request.getAttribute("docStatusGhi");
			if(docStatusGhi != null) {
			for(StudentDetailsGhiTO docStatusTo : docStatusGhi) {
			%>
				<tr>
					<td>&nbsp;</td>
					<td><%=docStatusTo.getStateCode() %></td>
					<td><%=docStatusTo.getTestMode() %></td>
					<td><%=docStatusTo.getStudentName() %></td>
					<td><%=docStatusTo.getExamineeID() %></td>
					<td><%=docStatusTo.getDrcStudentID() %></td>
					<td><%=docStatusTo.getDocProcessStatus() %></td>
					<td><%=docStatusTo.getExpiredDate() %></td>
					<td><%=docStatusTo.getCheckinDate() %></td>
					<td><%=docStatusTo.getBarcodeID() %></td>
					<td><%=docStatusTo.getScheduleID() %></td>
					<td><%=docStatusTo.getTcaScheduleDate() %></td>
					<td><%=docStatusTo.getDateTestTaken() %></td>
					<td><%=docStatusTo.getForm() %></td>
					<td><%=docStatusTo.getContentName() %></td>
					<td><%=docStatusTo.getContentTestCode() %></td>
					<td><%=docStatusTo.getTestLanguage() %></td>
					<td><%=docStatusTo.getLithoCode() %></td>
					<td><%=docStatusTo.getFieldTestForm() %></td>
					<td><%=docStatusTo.getTestEventUpdateDate() %></td>
					<td><%=docStatusTo.getTestEventUpdateDate() %></td>
					<td><%=docStatusTo.getOrgCodePath() %></td>
					<td><%=docStatusTo.getProcesDate() %></td>
					<td><%=docStatusTo.getDocumentID() %></td>
					<td><%=docStatusTo.getFileName() %></td>
					<td><%=docStatusTo.getFileGenDateTime() %></td>
				</tr>
			
			<%}
			}
			%>
			</tbody>
			</table>
			
			<br/><br/>
			<h4> Latest Error: </h4>
			<table id="errorGhi" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>&nbsp;</th>
					<th>Record Id</th>
					<th>State Prefix</th>
					<th>Test Mode</th>
					<th>Student Name</th>
					<th>Examinee ID (UUID)</th>
					<th>DRC Student ID</th>
					<th>Error Status</th>
					<th>Barcode ID</th>
					<th>Schedule ID</th>
					<th>TCA Schedule Date</th>
					<th>Date Test Taken</th>
					<th>Form</th>
					<th>Content Name</th>
					<th>Content Test Code</th>
					<th>Test Language</th>
					<th>Litho Code</th>
					<th>Scale Score</th>
					<th>Content Score (NC)</th>
					<th>Status Code for Content Area</th>
					<th>Test Center Code</th>
					<th>Test Center Name</th>
					<th>Error Description</th>
					<th>Last Updated Doc Date</th>
					<th>Scanned/Process Date</th>
					<th>Org Code Path</th>
					<th>Prism Process Date</th>
					<th>Doc ID</th>
					<th>File Name</th>
					<th>File Generation Date-Time</th>
				</tr>
				</thead>
				<tbody>
			<% 
			java.util.List<StudentDetailsGhiTO> errorGhi = (ArrayList) request.getAttribute("errorGhi");
			if(errorGhi != null) {
			for(StudentDetailsGhiTO error : errorGhi) {
			%>
				<tr>
					<td>&nbsp;</td>
					<td style="padding-top: 12px;" nowrap>
						<%if("CO".equals(error.getPrismProcessStatus())) { %>
							<span class="completed" title="Completed"></span> 
						<%} else {%>
							<span class="error" title="Error"></span> 
						<%} %>
					</td>
					<td><%=error.getRecordId() %></td>
					<td><%=error.getStateCode() %></td>
					<td><%=error.getTestMode() %></td>
					<td><%=error.getStudentName() %></td>
					<td><%=error.getExamineeID() %></td>
					<td><%=error.getDrcStudentID() %></td>
					<td><%=error.getPrismProcessStatus() %></td>
					<td><%=error.getBarcodeID() %></td>
					<td><%=error.getScheduleID() %></td>
					<td><%=error.getTcaScheduleDate() %></td>
					<td><%=error.getDateTestTaken() %></td>
					<td><%=error.getForm() %></td>
					<td><%=error.getContentName() %></td>
					<td><%=error.getContentTestCode() %></td>
					<td><%=error.getTestLanguage() %></td>
					<td><%=error.getLithoCode() %></td>
					<td><%=error.getScaleScore() %></td>
					<td><%=error.getContentScore() %></td>
					<td><%=error.getStatusCodeContentArea() %></td>
					<td><%=error.getTestCenterCode() %></td>
					<td><%=error.getTestCenterName() %></td>
					<td><%=error.getErrCodeErrDesc() %></td>
					<td><%=error.getTestEventUpdateDate() %></td>
					<td><%=error.getScannedProcessDate() %></td>
					<td><%=error.getOrgCodePath() %></td>
					<td><%=error.getProcesDate() %></td>
					<td><%=error.getDocumentID() %></td>
					<td><%=error.getFileName() %></td>
					<td><%=error.getFileGenDateTime() %></td>
				</tr>
			
			<%}
			}
			%>
			</tbody>
			</table>
			
			<br/><br/>
			<p>
				<b>Overall Status:</b><br/>
				<span class="completed legend" title="Completed"></span> = Completed/Success (CO) <br/>
				<span class="error legend legend2" title="Completed" style="margin-left: -34px;"></span> = Error (Record received by Prism, but there is an error) (ER)</br>
			</p>
		</div>
		<br/><br/>
		<!-- panel for DEF-->
		<h3 id="defTab" style="cursor: pointer;"><u>DEF Forms ONLY</u></h3>
		<div id="accordion2" >
			<h4>ER Data Compared to Operational (PRISM report data)</h4>
			<table id="process" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>Lock?</th>
					<th>State Prefix</th>
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
			java.util.List<StudentDetailsTO> allProcessDef = (ArrayList) request.getAttribute("combinedList");
			count=0;
			if(allProcessDef != null) {
			for(StudentDetailsTO process : allProcessDef) {
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
			
			<h4> ER Bucket: </h4>
			<table id="erbucket" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>Lock?</th>
					<th>State Prefix</th>
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