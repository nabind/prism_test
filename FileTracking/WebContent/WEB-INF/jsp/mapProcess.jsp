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
				minHeight: 150, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
			$("#stuLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 400, 
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
			
			$("#errorLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 500, 
				minWidth: 750, 
				closeOnEscape: true, 
				resizable: true
			});
			
			
			
		} );
		
		
		
		function testElementIdList(processId) {
			var dataString = "processId="+processId;
			$("#_er_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-erLogDialog").html('ER validation details for Process Id : ' + processId);
			jQuery("#erLogDialog").dialog("open");
			$.ajax({
				type: "POST",
				url: "testElementIdList.htm",
				data: dataString,
				success: function(data) {
					var innertext = "<table class='simple_table'><tr><th>Test Element ID</th><th>Error Details</th></tr> <tbody>";
					for(i=0;i<data.length;i++) {
						innertext += "<tr><td>"+data[i].product.testElementId+"</td><td>"+data[i].product.erValidationError+"</td></tr>";
					}
					innertext += "</tbody></table>";
					
					$("#_er_log").html( innertext );
					
				},
				error: function(data) {
					$("#_er_log").html( ' Failed to get ER Log' );
				}
			});
			return false;
		}
		
		function clearStudentDetailsTableRows() {
			$("#_student_name").html( '' );
			$("#_dob").html( '' );
			$("#_gender").html( '' );
			$("#_grade").html( '' );
			$("#_barcode").html( '' );
			$("#_structure_element").html( '' );
			$("#_uuid").html( '' );
			$("#_cust_name").html( '' );
			$("#_school_name").html( '' );
			$("#_err_code").html( '' );
			$("#_err_details").html( '' );
		}
		
		function clearStudentDetailsTableWhenError(errMsg) {
			$("#_stu_log").html( '<span style="color: red;">'+errMsg+'</span>' );
			clearStudentDetailsTableRows();
		}
		
		function getStudentDetails(testElementId, processId) {
			clearStudentDetailsTableRows();
			var dataString = "processId="+processId+'&testElementId='+testElementId;
			$("#_stu_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-stuLogDialog").html('Student Details for Test Element Id : '+testElementId);
			jQuery("#stuLogDialog").dialog("open");
			$.ajax({
			      type: "POST",
			      url: "getStudentDetails.htm",
			      data: dataString,
			      success: function(data) {
			    	  if(data == "Error") {
			    		  clearStudentDetailsTableWhenError('Failed to get Student Details');
			    	  } else if(data.length == 2) {
			    		  clearStudentDetailsTableWhenError('Student Details Not Found');
				      } else {
			    		  $("#_stu_log").html( '' );
				    	  var obj = JSON.parse(data);
				    	  $("#_student_name").html( obj.STUDENT_NAME );
				    	  $("#_dob").html( obj.DOB );
				    	  $("#_gender").html( obj.GENDER );
				    	  $("#_grade").html( obj.GRADE );
				    	  $("#_barcode").html( obj.BARCODE );
				    	  $("#_structure_element").html( obj.STRUC_ELEMENT );
				    	  $("#_uuid").html( obj.EXT_STUDENT_ID );
				    	  $("#_cust_name").html( obj.CUSTOMER_NAME );
				    	  $("#_school_name").html( obj.ORG_NAME );
				    	  $("#_err_code").html( obj.EXCEPTION_CODE );
				    	  $("#_err_details").html( obj.DESCRIPTION );
			    	  }
			      },
				  error: function(data) {
					  clearStudentDetailsTableWhenError('Failed to get Student Details');
				  }
		    });
		    return false;
		}
		
		function getProcessLog(processId) {
			var dataString = "taskId="+processId;
			
			$("#_process_log").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-processLogDialog").html('Log for Task Id : '+processId);
			jQuery("#processLogDialog").dialog("open");
			
			
			$.ajax({
			      type: "POST",
			      url: "getMapProcessLog.htm",
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
		
		function getStudentLog(processId) {
			var dataString = "taskId="+processId;
			
			$("#errorLogDialog").html( '<img src="css/ajax-loader.gif"></img>' );
			$("#ui-dialog-title-processLogDialog").html('Error Log for Task Id : '+processId);
			jQuery("#errorLogDialog").dialog("open");
			
			
			$.ajax({
			      type: "POST",
			      url: "getErrorStudents.htm",
			      data: dataString,
			      success: function(data) {
			    	  var obj = JSON.parse(data);
			    	  var innertext = "<table class='simple_table'><tr><th>Student Name</th><th>District & School Code</th><th>DRC Stud. ID</th><th>Subtest</th><th>Error Code</th><th>Error Log</th></tr> <tbody>";
			    	  $.each(obj, function() {
						  innertext += "<tr><td>"+this.studentName+"</td><td>"+this.district+" | "+this.school+" </td><td>"+this.drcStudentId+"</td><td>"+this.contantArea+"</td><td>"+this.errorCode+"</td><td>"+this.errorLog+"</td></tr>";
					  });
					  innertext += "</tbody></table>";
						
			    	  $("#errorLogDialog").html( innertext );
			      },
				  error: function(data) {
					  $("#errorLogDialog").html( ' Failed to get Process Log' );
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
						<th style="min-width: 60px;">Task Id</th>
						<th>Process Id</th>
						<th>Hier Val.</th>
						<th>Bio Val.</th>
						<th>Demo Val.</th>
						<th>Content Val.</th>
						<th>Objective Val.</th>
						<th>Item Val.</th>
						<th>Partition Name</th>
						<th>Processed Date</th>
						<th>Source File Name</th>
					</tr>
					</thead>
					<tbody>
				<% 
				java.util.List<TASCProcessTO> allProcess = (ArrayList) request.getSession().getAttribute("mapProcess");
				int count=0;
				for(TASCProcessTO process : allProcess) {
					count++;
				%>
					<tr>
						<td>&nbsp;</td>
						
						<td style="padding-top: 12px;" nowrap>
							<%if("CO".equals(process.getOverallStatus())) { %>
								<span class="completed" title="Completed"></span>
								<%=process.getTaskId() %>
							<%} else if("IN".equals(process.getOverallStatus())) {%>
								<span class="progress" title="In Progress"></span>
								<%=process.getTaskId() %>
							<%} else {%>
								<span class="error" title="Error"></span>
								<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getStudentLog(<%=process.getTaskId() %>);'>
									<%=process.getTaskId() %>
								</a>
							<%} %>
							
						</td>
						
						<td><%=process.getProcessId() %></td>
						<td><%=process.getHierValidation() %></td>
						<td><%=process.getBioValidation() %></td>
						<td><%=process.getDemoValidation() %></td>
						<td><%=process.getContentValidation() %></td>
						<td><%=process.getObjValidation() %></td>
						<td><%=process.getItemValidation() %></td>
						<td><a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='getProcessLog(<%=process.getTaskId() %>);'>
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
				
				<p><b>Val.</b> = Validation Status</p>
				<p><b>Overall Status:</b><br/>
					<span class="completed" title="Completed"></span> = Completed/Success <br/>
					<span class="progress" title="Completed" style="margin-left: -34px;"></span> = In Progress<br/>
					<span class="error" title="Completed" style="margin-left: -34px;"></span> = Error
				</p>
				<div id='erLogDialog' title='Loading' style='display:none; font-size:12px'>
					<p id="_er_Heading"><b>ER validation failed for the following students. </b><p>
					<p id="_er_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				<div id="stuLogDialog" title="Loading ..." style='display:none; font-size:11px'>
					<p id="_stu_log"><p>
					<table class="process_details">
						<tr>
							<td colspan="2"><span id="_error_message" style="display:none;color:red"></span></td>
						</tr>
						<tr>
							<td width="44%"><b>Student Name :</b></td><td width="56%"><span id="_student_name"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>DOB :</b></td><td><span id="_dob"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Gender :</b></td><td><span id="_gender"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Grade :</b></td><td><span id="_grade"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Barcode :</b></td><td><span id="_barcode"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Structure Element :</b></td><td><span id="_structure_element"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Ext Student Id(UUID) :</b></td><td><span id="_uuid"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Customer Name :</b></td><td><span id="_cust_name"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>School/Testing Site Name :</b></td><td><span id="_school_name"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Exception Code :</b></td><td><span id="_err_code"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
						<tr>
							<td><b>Exception Description :</b></td><td><span id="_err_details"><img src="css/ajax-loader.gif"></img></span></td>
						</tr>
					</table>
				</div>
				<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
				<div id='errorLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="_error_log"><img src="css/ajax-loader.gif"></img><p>
				</div>
				
			</div>

	</div>
</div>
</div>
