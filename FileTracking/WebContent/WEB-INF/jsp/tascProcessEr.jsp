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
			
			$("#stuHistDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 400, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
			$("#errorLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 500, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
			
			
		});
		
		function clearStudentDetailsTableRows() {
			$("#ctbCustomerId").html('');
    	  	$("#stateName").html('');
	    	  $("#dob").html('');
	    	  $("#gender").html('');
	    	  $("#govermentId").html('');
	    	  $("#govermentIdType").html('');
	    	  $("#address").html('');
	    	  $("#city").html('');
	    	  $("#county").html('');
	    	  $("#state").html('');
	    	  $("#zip").html('');
	    	  $("#email").html('');
	    	  $("#alternateEmail").html('');
	    	  $("#primaryPhoneNumber").html('');
	    	  $("#cellPhoneNumber").html('');
	    	  $("#alternatePhoneNumber").html('');
	    	  $("#resolvedEthnicityRace").html('');
	    	  $("#homeLanguage").html('');
	    	  $("#educationLevel").html('');
	    	  $("#attendCollege").html('');
	    	  $("#contact").html('');
	    	  $("#examineeCountyParishCode").html('');
	    	  $("#registeredOn").html('');
	    	  $("#registeredTestCenter").html('');
	    	  $("#registeredTestCenterCode").html('');
	    	  $("#scheduleId").html('');
	    	  $("#timeOfDay").html('');
	    	  $("#checkedInDate").html('');
	    	  $("#contentTestType").html( '');
	    	  $("#contentTestCode").html('');
	    	  $("#tascRadiness").html('');
	    	  $("#ecc").html('');
	    	  $("#testCenterCode").html('');
	    	  $("#testCenterName").html('');
	    	  $("#regstTcCountyParishCode").html('');
	    	  $("#schedTcCountyParishCode").html('');
		}
		
		function clearStudentDetailsTableWhenError(errMsg) {
			$("#stuHist").html( '<span style="color: red;">'+errMsg+'</span>' );
			clearStudentDetailsTableRows();
		}
		
		function getStudentHist(erSsHistId) {
			clearStudentDetailsTableRows();
			var dataString = "erSsHistId="+erSsHistId;
			$("#stuHist").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-stuHistDialog").html('Student Details for Record Id : '+erSsHistId);
			jQuery("#stuHistDialog").dialog("open");
			$.ajax({
			      type: "POST",
			      url: "getStudentHist.htm",
			      data: dataString,
			      success: function(data) {
			    	  if(data == "Error") {
			    		  clearStudentDetailsTableWhenError('Failed to get Student History');
			    	  } else if(data.length == 2) {
			    		  clearStudentDetailsTableWhenError('Student History Not Found');
				      } else {
			    		  $("#stuHist").html('');
				    	  var obj = JSON.parse(data);
				    	  $("#ctbCustomerId").html( obj.CTB_CUSTOMER_ID );
				    	  $("#stateName").html( obj.STATENAME );
				    	  $("#dob").html( obj.DATEOFBIRTH );
				    	  $("#gender").html( obj.GENDER );
				    	  $("#govermentId").html( obj.GOVERNMENTID );
				    	  $("#govermentIdType").html( obj.GOVERNMENTIDTYPE );
				    	  $("#address").html( obj.ADDRESS1 );
				    	  $("#city").html( obj.CITY );
				    	  $("#county").html( obj.COUNTY );
				    	  $("#state").html( obj.STATE );
				    	  $("#zip").html( obj.ZIP );
				    	  $("#email").html( obj.EMAIL );
				    	  $("#alternateEmail").html( obj.ALTERNATEEMAIL );
				    	  $("#primaryPhoneNumber").html( obj.PRIMARYPHONENUMBER );
				    	  $("#cellPhoneNumber").html( obj.CELLPHONENUMBER );
				    	  $("#alternatePhoneNumber").html( obj.ALTERNATENUMBER );
				    	  $("#resolvedEthnicityRace").html( obj.RESOLVED_ETHNICITY_RACE );
				    	  $("#homeLanguage").html( obj.HOMELANGUAGE );
				    	  $("#educationLevel").html( obj.EDUCATIONLEVEL );
				    	  $("#attendCollege").html( obj.ATTENDCOLLEGE );
				    	  $("#contact").html( obj.CONTACT );
				    	  $("#examineeCountyParishCode").html( obj.EXAMINEECOUNTYPARISHCODE );
				    	  $("#registeredOn").html( obj.REGISTEREDON );
				    	  $("#registeredTestCenter").html( obj.REGISTEREDATTESTCENTER );
				    	  $("#registeredTestCenterCode").html( obj.REGISTEREDATTESTCENTERCODE );
				    	  $("#scheduleId").html( obj.SCHEDULE_ID );
				    	  $("#timeOfDay").html( obj.TIMEOFDAY );
				    	  $("#checkedInDate").html( obj.DATECHECKEDIN );
				    	  $("#contentTestType").html( obj.CONTENT_TEST_TYPE );
				    	  $("#contentTestCode").html( obj.CONTENT_TEST_CODE );
				    	  $("#tascRadiness").html( obj.TASCREADINESS );
				    	  $("#ecc").html( obj.ECC );
				    	  $("#testCenterCode").html( obj.TESTCENTERCODE );
				    	  $("#testCenterName").html( obj.TESTCENTERNAME );
				    	  $("#regstTcCountyParishCode").html( obj.REGST_TC_COUNTYPARISHCODE );
				    	  $("#schedTcCountyParishCode").html( obj.SCHED_TC_COUNTYPARISHCODE );
			    	  }
			      },
				  error: function(data) {
					  clearStudentDetailsTableWhenError('Failed to get Student History');
				  }
		    });
		    return false;
		}
		
		function getErrorLog(erExcdId) {
			var dataString = "erExcdId="+erExcdId;
			$("#errorLog").html( '<img src="css/ajax-loader.gif"></img>' );
			jQuery("#errorLogDialog").dialog("open");
			$("#ui-dialog-title-errorLogDialog").html('Error Log');
			$.ajax({
			      type: "POST",
			      url: "getErrorLog.htm",
			      data: dataString,
			      success: function(data) {
			    	  $("#errorLog").html( data );
			      },
				  error: function(data) {
					  $("#errorLog").html( ' Failed to get Error Log' );
				  }
		    });
		    return false;
		}
		
	</script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				
				
				<table id="process" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th style="min-width: 60px;">Record Id</th>
						<th>Student Name</th>
						<th>UUID</th>
						<th>Subject (Content Area/Subtest)</th>
						<th>Test Element Id</th>
						<th>Process Id</th>
						<th>Exception Code</th>
						<th>Source System</th>
						<th>Bar Code</th>
						<th>Scheduled Date</th>
						<th>State Code</th>
						<th>Form</th>
						<th>More Info</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<TASCProcessTO> allProcessEr = (ArrayList) request.getSession().getAttribute("tascProcessEr");
				int count=0;
				for(TASCProcessTO processEr : allProcessEr) {
					count++;
				%>
					<tr>
						<td>&nbsp;</td>
						<td style="padding-top: 12px;" nowrap>
							
							<%if("CO".equals(processEr.getOverallStatus())) { %>
								<span class="completed" title="Completed"></span>
							<%} else if("ER".equals(processEr.getOverallStatus()) || "IN".equals(processEr.getOverallStatus())) {%>
								<span class="error" title="Error"></span>
							<%} else{%>
								<span class="progress" title="In Progress"></span>		
							<%} %>
							
							<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getErrorLog(<%=processEr.getErExcdId()%>);'>
							 <% if("0".equals(processEr.getErSsHistId())){%>
								<% if("0".equals(processEr.getProcessId())){%>
								NA
								<%}else{%>
									<%=processEr.getProcessId() %>
								<%}%>
							<%}else{%>
								<%=processEr.getErSsHistId() %>
							<%}%>
							</a>
						</td>
						<td><%=processEr.getStudentName() %></td>
						<td><%=processEr.getUuid() %></td>
						<td><%=processEr.getSubtestName() %></td>
						<td>
							<% if("0".equals(processEr.getTestElementId())){%>
							NA
							<%}else{%>
								<%=processEr.getTestElementId() %>
							<%}%>
						</td>
						<td>
							<% if("0".equals(processEr.getProcessId())){%>
							NA
							<%}else{%>
								<%=processEr.getProcessId() %>
							<%}%>
						</td>
						<td>
							<% if("0".equals(processEr.getExceptionCode())){%>
							NA
							<%}else{%>
								<%=processEr.getExceptionCode() %>
							<%}%>
						</td>
						<td><%=processEr.getSourceSystem() %></td>
						<td><%=processEr.getBarcode() %></td>
						<td><%=processEr.getDateScheduled() %></td>
						<td><%=processEr.getStateCode() %></td>
						<td><%=processEr.getForm() %></td>
						<td>
							<% if("0".equals(processEr.getErSsHistId())){%>
							NA
							<%}else{%>
								<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getStudentHist(<%=processEr.getErSsHistId() %>);'>
									More Info
								</a>
							<%}%>
						</td>
					</tr>
				
				<%} %>
				</tbody>
				</table>
				<p style="padding-top:20px">
					<b>Record Id:</b> = History ID for eResource, = Process ID for Online and Paper Pencil.
				</p>
				<p>
					<b>Overall Status:</b><br/>
					<span class="completed" title="Completed"></span> = Completed/Success <br/>
					<span class="progress" title="Completed" style="margin-left: -34px;"></span> = In Progress<br/>
					<span class="error" title="Completed" style="margin-left: -34px;"></span> = Error
				</p>
				
				<div id="stuHistDialog" title="Loading ..." style='display:none; font-size:11px'>
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
				<div id='errorLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="errorLog"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
			</div>

	</div>
</div>
</div>
