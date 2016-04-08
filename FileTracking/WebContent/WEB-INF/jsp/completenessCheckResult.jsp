<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/tasc.css" type="text/css"/>
<script src="css/jquery.validate.js"></script>	
<script type="text/javascript" src="js/modules/tasc.js"></script>

<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				<% 
				SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("tascRequestTO");
				%>
				<div style="float: right">
					<a href='downloadCsv.htm' class='noteLink ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover' 
						style='padding:5px;top:5px;color:#00329B;text-decoration:underline' target="_blank">
						Download CSV
					</a>
				</div>
				<div class="search-criteria">
				<b>Showing search results for</b> Source System: <%=searchProcess.getSourceSystemDesc()%>
				<%if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){%>
					, Processed Date From: <%=searchProcess.getProcessedDateFrom()%>
				<%}%>
				<%if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){%>
					, Processed Date To: <%=searchProcess.getProcessedDateTo()%>
				<%}%>
				<%if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){%>
					, UUID: <%=searchProcess.getUuid()%>
				<%}%>
				<%if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){%>
					, Record ID (Only for eResources): <%=searchProcess.getRecordId()%>
				<%}%>
				<%if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){%>
					, Process ID (Except eResources): <%=searchProcess.getProcessId()%>
				<%}%>
				<%if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){%>
					, Last Name: <%=searchProcess.getLastName()%>
				<%}%>
				<%if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){%>
					, Exception Code: <%=searchProcess.getExceptionCode()%>
				<%}%>
				<%if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){%>
					, State Code: <%=searchProcess.getStateCode()%>
				<%}%>
				<%if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){%>
					, Form: <%=searchProcess.getForm()%>
				<%}%>
				<%if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){%>
					, Test Element ID: <%=searchProcess.getTestElementId()%>
				<%}%>
				<%if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){%>
					, Barcode: <%=searchProcess.getBarcode()%>
				<%}%>					
				</div>
				
				<table id="process" width="100%">
					<thead>
						<tr>
							<th >&nbsp;</th>
							<th style="min-width: 60px;">Record Id</th>
							<th>Student Name</th>
							<th>UUID</th>
							<th>Test Element Id</th>
							<th>Ex. Code</th>
							<th>Status</th>
							<th>Bar Code</th>
							<th>Test/Schedule Date</th>
							<th>State Code</th>
							<th>Form</th>
							<th>Subtest</th>
							<th>Prism Process Date</th>
							<th>More Info</th>
						</tr>
					</thead>
				</table>
			
				<p style="padding-top:20px">
					<b>Record Id:</b> = History ID for eResource, = Process ID for Online and Paper Pencil.
				</p>
				<p>
					<b>Overall Status:</b><br/>
					<span class="completed legend" title="Completed"></span> = Completed/Success =(CO) <br/>
					<span class="progress legend legend2" title="Completed" style="margin-left: -34px;"></span> = Invalidated =(IN)<br/>
					<span class="error legend legend2 legend3" title="Completed" style="margin-left: -34px;"></span> = Error =(ER)
				</p>
				
				<div id="stuHistDialog" title="Loading ..." style='display:none; font-size:11px;'>
					<p id="stuHist"><p>
					<table class="process_details">
						<tr>
							<td colspan="2"><span id="_error_message" style="display:none;color:red"></span></td>
						</tr>
						<tr>
							<td width="44%"><b>CTB Customer ID :</b></td><td width="56%"><span id="ctbCustomerId"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>State Name :</b></td><td><span id="stateName"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>DOB :</b></td><td><span id="dob"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Gender :</b></td><td><span id="gender"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Government ID :</b></td><td><span id="govermentId"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Government ID Type :</b></td><td><span id="govermentIdType"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Address :</b></td><td><span id="address"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>City :</b></td><td><span id="city"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>County :</b></td><td><span id="county"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>State :</b></td><td><span id="state"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Zip :</b></td><td><span id="zip"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Email :</b></td><td><span id="email"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Alternate Email :</b></td><td><span id="alternateEmail"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Primary Phone Number :</b></td><td><span id="primaryPhoneNumber"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Cell Phone Number :</b></td><td><span id="cellPhoneNumber"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Alternate Phone Number :</b></td><td><span id="alternatePhoneNumber"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Resolved Ethnicity Race :</b></td><td><span id="resolvedEthnicityRace"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Home Language :</b></td><td><span id="homeLanguage"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Education Level :</b></td><td><span id="educationLevel"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Attend College :</b></td><td><span id="attendCollege"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Contact :</b></td><td><span id="contact"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Examinee County Parish Code :</b></td><td><span id="examineeCountyParishCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Registered On :</b></td><td><span id="registeredOn"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Registered Test Center :</b></td><td><span id="registeredTestCenter"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Registered Test Center Code :</b></td><td><span id="registeredTestCenterCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Schedule ID :</b></td><td><span id="scheduleId"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Time of Day :</b></td><td><span id="timeOfDay"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Checked in Date :</b></td><td><span id="checkedInDate"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Content Test Type :</b></td><td><span id="contentTestType"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Content Test Code :</b></td><td><span id="contentTestCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>TASC Radiness :</b></td><td><span id="tascRadiness"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>ECC :</b></td><td><span id="ecc"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Test Center Code :</b></td><td><span id="testCenterCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Test Center Name :</b></td><td><span id="testCenterName"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Regst TC County Parish Code :</b></td><td><span id="regstTcCountyParishCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Sched TC County Parish Code :</b></td><td><span id="schedTcCountyParishCode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
					</table>
				</div>
				
				<div id="moreInfoDialog" title="Loading ..." style='display:none; font-size:11px'>
					<p id="moreInfo"><p>
					<table width="100%" class="process_details">
						<tr>
							<td colspan="2"><span id="_error_message" style="display:none;color:red"></span></td>
						</tr>
						<tr>
							<td width="44%"><b>Test Center Code :</b></td><td width="56%"><span id="testCenterCode_mi"></span></td>
						</tr>
						<tr>
							<td><b>Test Center Name :</b></td><td><span id="testCenterName_mi"></span></td>
						</tr>
						<tr>
							<td><b>Test Language :</b></td><td><span id="testLanguage_mi"></span></td>
						</tr>
						<tr>
							<td><b>Litho Code :</b></td><td><span id="lithoCode_mi"></span></td>
						</tr>
						<tr>
							<td><b>Scoring Date :</b></td><td><span id="scoringDate_mi"></span></td>
						</tr>
						<tr>
							<td><b>Scanned/Processed Date :</b></td><td><span id="scannedDate_mi"></span></td>
						</tr>
						<tr>
							<td><b>Student Name (CBT/PBT) :</b></td><td><span id="studentName_mi"></span></td>
						</tr>
						<tr>
							<td><b>Number Correct :</b></td><td><span id="numberCorrect_mi"></span></td>
						</tr>
						<tr>
							<td><b>Status Code :</b></td><td><span id="statusCode_mi"></span></td>
						</tr>
						<tr>
							<td><b>Scan Batch=OrgTP_Struc-Lvl-Elm_Opunit :</b></td><td><span id="scanBatch_mi"></span></td>
						</tr>
						<tr>
							<td><b>Scan Stack :</b></td><td><span id="scanStack_mi"></span></td>
						</tr>
						<tr>
							<td><b>Scan Sequence :</b></td><td><span id="scanSequence_mi"></span></td>
						</tr>
						<tr>
							<td><b>Bio Image(s) :</b></td><td><span id="bioImages_mi"></span></td>
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
