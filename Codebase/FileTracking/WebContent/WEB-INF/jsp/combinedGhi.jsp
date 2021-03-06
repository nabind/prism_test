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
<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<style>
	.no-border {
		border: 0;
		/*padding: 3px*/
	}
</style>
<script src="css/jquery.validate.js"></script>
	
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		oTable = $('#processGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 10, "desc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] }
							]
		});
		
		oTable2 = $('#errorGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 26, "desc" ]],
			"aoColumnDefs": [ 
							  {'bSortable': false, 'aTargets':  [0]  },
							  { "width": "30%", "aTargets":  [22] }
							]
		});
    });
</script>
	
<div id="heromaskarticle">
	<%-- <div id="showCommentsDialog" title="Loading ..." style='display:none; font-size:11px'>
		<p></p>
		<div class="commentContent" style="text-align: center">
			<input type="hidden" value="${uuid}" id="commentUuid"/>
			<input type="hidden" value="${stateCode}" id="commentStateCode"/>
			<div style="display:none">
				<textarea rows="10" cols="60" id="hiddenComments">${savedComments}</textarea>
			</div>
			
			<div style="padding: 10px;">
				<label style='font-size:13px; font-weight:bold; color:black;'>
					Enter Comments for UUID: ${uuid} and State Code: ${stateCode}
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
	</div> --%>
	<!-- <div id="confirm" title="Confirm" style="display:none;">
	    Comments have changed. Do you want to continue without saving?
	</div> -->
		
	<div class="container" style="margin-left:25px;width: 900px !important;">
		<h1>Search a single student - GHI</h1>
		<div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 666px;float: left;">
			<%
			SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("combinedGhiRequestTO");
			/* String savedComments = (String)request.getAttribute("savedComments"); 
			String showCommentFlag = (String)request.getAttribute("showCommentFlag");  */
			if(searchProcess == null) searchProcess = new SearchProcess();
			%>
			<form name="combinedGhiForm" method="post" action="combinedGhi.htm" id="combinedGhiForm">
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
						<!-- State code will be 3 digit now. So, Commenting the State code field and making level1 org code as state prefix as both are same. : Nabin -->
						<%-- <tr>
							<td class="no-border">State Code:</td>
							<td class="no-border"><input type="text" name="stateCode" id="stateCode" value="<% if(searchProcess.getStateCode() != null) out.print(searchProcess.getStateCode()); %>"></td>
						</tr>  --%>
						<tr>
							<td class="no-border">State Prefix:</td>
							<td class="no-border"><input type="text" name="level1OrgCode" id="level1OrgCode" value="<% if(searchProcess.getLevel1OrgCode() != null) out.print(searchProcess.getLevel1OrgCode()); %>">
								<span style="color:red;">* 3 digit.</span>
							</td>
						</tr>
						<tr>
							<td class="no-border">DRC Student ID (exact ID is needed):</td>
							<td class="no-border"><input type="text" name="drcStudentId" id="drcStudentId" value="<% if(searchProcess.getDRCStudentId() != null) out.print(searchProcess.getDRCStudentId()); %>"></td>
						</tr>
						<tr>
							<td class="no-border"></td>
							<td class="no-border"><input type="Submit" value="Search"></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<%-- <div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 400px;height: 172px;float: right;">
			<% if("false".equals(showCommentFlag)){ %>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments </h3>
				<div style="padding: 20px;color: red;">
					Please provide 9 digit UUID and 2 character state code to fetch comments.
				</div>
			<%}else{%>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments for UUID: ${uuid} and State Code: ${stateCode}
					<a id="showComments" href="#nogo" style="color: #00329B;text-decoration:underline;padding-left: 17px;">Add/Edit</a>
				</h3>
				<div id="comment-div" style="padding: 0px 10px;font-weight:normal;height: 141px;overflow:auto;white-space: pre-line">
					${savedComments}
				</div>
			<%}%>
		</div> --%>
	</div>
	<div id="articlecontent">
		<div id="accordion" style="margin-top:25px">
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
			<h4> Latest Error: </h4>
			<table id="errorGhi" width="100%">
			<thead>
				<tr>
					<th >&nbsp;</th>
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
			
			<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
				<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
			</div>
			
		</div>
	</div>
</div>