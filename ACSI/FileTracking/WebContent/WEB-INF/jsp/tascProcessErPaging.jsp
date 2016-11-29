<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
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
		margin-left: -2px;
		margin-top: 1px;
		position: relative;
		z-index: 88;
	}
	.progress {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -102px -94px;
		margin-left: -2px;
		margin-top: 1px;
		position: relative;
		z-index: 88;
	}
	.error {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -136px -94px;
		margin-left: -2px;
		margin-top: 1px;
		position: relative;
		z-index: 88;
	}
	.legend {
		margin-left: -12px !important;
		margin-top: -9px !important;
	}
	
	.legend2 {
		margin-left: -34px !important;
	}
	@-moz-document url-prefix() { 
	  .legend3 {
	     margin-left: -12px !important;
	  }
	}
	
	.search-criteria {
		background-color: rgb(174, 208, 187);
		padding: 10px;
		margin-bottom: 10px;
		text-align: justify;
		border-radius: 5px;
	}
	.dataTables_processing {
		  position: absolute;
		  top: 50%;
		  left: 50%;
		  width: 100%;
		  height: 40px;
		  margin-left: -50%;
		  margin-top: -25px;
		  padding-top: 20px;
		  text-align: center;
		  font-size: 1.2em;
		  background-color: white;
		  background: -webkit-gradient(linear, left top, right top, color-stop(0%, rgba(255, 255, 255, 0)), color-stop(25%, rgba(255, 255, 255, 0.9)), color-stop(75%, rgba(255, 255, 255, 0.9)), color-stop(100%, rgba(255, 255, 255, 0)));
		  /* Chrome,Safari4+ */
		  background: -webkit-linear-gradient(left, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.9) 25%, rgba(255, 255, 255, 0.9) 75%, rgba(255, 255, 255, 0) 100%);
		  /* Chrome10+,Safari5.1+ */
		  background: -moz-linear-gradient(left, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.9) 25%, rgba(255, 255, 255, 0.9) 75%, rgba(255, 255, 255, 0) 100%);
		  /* FF3.6+ */
		  background: -ms-linear-gradient(left, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.9) 25%, rgba(255, 255, 255, 0.9) 75%, rgba(255, 255, 255, 0) 100%);
		  /* IE10+ */
		  background: -o-linear-gradient(left, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.9) 25%, rgba(255, 255, 255, 0.9) 75%, rgba(255, 255, 255, 0) 100%);
		  /* Opera 11.10+ */
		  background: linear-gradient(to right, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.9) 25%, rgba(255, 255, 255, 0.9) 75%, rgba(255, 255, 255, 0) 100%);
		  /* W3C */
	}
</style>
<script src="css/jquery.validate.js"></script>	
<script type="text/javascript" charset="utf-8">

	function update_rows(){
	    $(".process_details tr:even").css("background-color", "#fff");
	    $(".process_details tr:odd").css("background-color", "#eee");
	}
	
	$(document).ready(function() {
		$.fn.dataTable.ext.errMode = 'none';
		
		$("#process").on( 'error.dt', function ( e, settings, techNote, message ) {
			alert( 'Error has occurred, please contact system administrator');
	    	}).dataTable( {
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"order": [[ 12, "desc" ]],
	        "bProcessing": true,
	        "bServerSide": true,
	        "sort": "position",
	        "bStateSave": false,
	        "iDisplayLength": 10,
	        "iDisplayStart": 0,
	        "fnDrawCallback": function () {
	        	update_rows();
	        },         
	        "sAjaxSource": "searchTascErPaging.htm",
	        "aoColumnDefs": [ 
							  { 'bSortable': false, 'aTargets': [ 0 ]},
							  { 'bSortable': false, 'aTargets': [ 13 ]}
							],
	        "aoColumns": [
				{ 
					"mData": "overallStatus",
					"mRender": function (oObj) {
						if(oObj == 'CO'){
	                		return "<span class='completed' title='Completed'></span>";
	                	}else if(oObj == 'ER'){
	                		return "<span class='error' title='Error'></span>";
	                	}else if(oObj == 'IN'){
	                		return "<span class='progress' title='Invalidated'></span>";
	                	}else{
	                		return "<span class='progress' title='In Progress'></span>";
	                	}
	                }		
				},	                      
				{
					"mRender": function ( data, type, row ) {
						if(row.erExcdId != "0"){
							var html = "<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getErrorLog("
											+ row.erExcdId + ");'>";
							if(row.erSsHistId =="0"){
								html += row.processId;
							}else{
								html += row.erSsHistId;
							}
							html += "</a>";
							return html;
		                }else{
								if(row.erSsHistId =="0"){
									return row.processId;
								}else{
									return row.erSsHistId;
								}
							}
						}
				},
				{ "mData": "studentName" },
				{ "mData": "uuid" },
				{ "mData": "testElementId" },
				{ "mData": "exceptionCode" },
				{ 
					"mData": "overallStatus",
					"mRender": function (oObj) {
						if(oObj == 'CO'){
	                		return "CO";
	                	}else if(oObj == 'ER'){
	                		return "ER";
	                	}else if(oObj == 'IN'){
	                		return "IN";
	                	}else{
	                		return "In Progress";
	                	}
	                }		
				},
				{ "mData": "barcode" },
				{ "mData": "dateScheduled" },
				{ "mData": "stateCode"},
				{ "mData": "form"},
				{ "mData": "subtestName"},
				{ "mData": "processedDate"},
				{
					"mRender": function ( data, type, row ) {
						var html = "<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='";
						if(row.sourceSystem == 'ERESOURCE'){
							html = html + "getStudentHist(" +row.erSsHistId + ");'>";
						}else{
							html = html + "getMoreInfo(" +row.erExcdId + ");'>";
						}
						html += "More Info</a>";
						return html;
					}
				}
	        ]
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
	
	function clearMoreInfoTableRows() {
		$("#testCenterCode_mi").html('');
		$("#testCenterName_mi").html('');
		$("#testLanguage_mi").html('');
		$("#lithoCode_mi").html('');
		$("#scoringDate_mi").html('');
		$("#scannedDate_mi").html('');
		$("#studentName_mi").html('');
		$("#numberCorrect_mi").html('');
		$("#statusCode_mi").html('');
		$("#scanBatch_mi").html('');
		$("#scanStack_mi").html('');
		$("#scanSequence_mi").html('');
		$("#bioImages_mi").html('');
	}
	
	function clearStudentDetailsTableWhenError(errMsg) {
		$("#stuHist").html( '<span style="color: red;">'+errMsg+'</span>' );
		clearStudentDetailsTableRows();
	}
	
	function clearMoreInfoTableWhenError(errMsg) {
		$("#moreInfo").html( '<span style="color: red;">'+errMsg+'</span>' );
		clearMoreInfoTableRows();
	}
	
	function getStudentHist(erSsHistId) {
		clearStudentDetailsTableRows();
		var dataString = "erSsHistId="+erSsHistId;
		$("#stuHist").html( '<img src="css/ajax-loader.gif"></img>' );
		var dialogTitle = 'Student Details for Record Id : '+erSsHistId;
		jQuery("#stuHistDialog").dialog({
			title: dialogTitle,
			width: 510,
			height: 448,
			draggable: false
		});
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
	
	function getMoreInfo(erExcdId) {
		clearMoreInfoTableRows();
		var dataString = "erExcdId="+erExcdId;
		$("#moreInfo").html( '<img src="css/ajax-loader.gif"></img>' );
		jQuery("#moreInfoDialog").dialog({
			title: 'More Info: ',
			width: 675,
			height: 410,
			draggable: false
		});
		$.ajax({
		      type: "POST",
		      url: "getMoreInfo.htm",
		      data: dataString,
		      success: function(data) {
		    	  if(data == "Error") {
		    		  clearMoreInfoTableWhenError('Failed to get Data');
		    	  } else if(data.length == 2) {
		    		  clearMoreInfoTableWhenError('Data Not Found');
		          } else {
		    		  $("#moreInfo").html('');
			    	  var obj = JSON.parse(data);
			    	  $("#testCenterCode_mi").html( obj.TESTING_SITE_CODE );
			    	  $("#testCenterName_mi").html( obj.TESTING_SITE_NAME );
			    	  $("#testLanguage_mi").html( obj.TEST_LANGUAGE );
			    	  $("#lithoCode_mi").html( obj.LITHOCODE );
			    	  $("#scoringDate_mi").html( obj.SCORING_DATE );
			    	  $("#scannedDate_mi").html( obj.SCANNED_DATE );
			    	  $("#studentName_mi").html( obj.LAST_NAME );
			    	  $("#numberCorrect_mi").html( obj.NCR_SCORE );
			    	  $("#statusCode_mi").html( obj.CONTENT_STATUS_CODE );
			    	  $("#scanBatch_mi").html( obj.SCAN_BATCH );
			    	  $("#scanStack_mi").html( obj.SCAN_STACK );
			    	  $("#scanSequence_mi").html( obj.SCAN_SEQUENCE );
			    	  $("#bioImages_mi").html( obj.BIO_IMAGES );
			     }
		      },
			  error: function(data) {
				  clearMoreInfoTableWhenError('Failed to get Data');
			  }
	    });
	    return false;
	}
	
	function getErrorLog(erExcdId) {
		var dataString = "erExcdId="+erExcdId;
		$("#errorLog").html( '<img src="css/ajax-loader.gif"></img>' );
		jQuery("#errorLogDialog").dialog({
			title: 'Error Log',
			width: 510,
			height: 448,
			draggable: false
		});
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
