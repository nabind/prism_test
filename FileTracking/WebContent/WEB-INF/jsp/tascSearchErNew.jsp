<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

	<link rel="stylesheet" href="css/highlight.css" type="text/css">
		<link rel="stylesheet" href="css/demo.css" type="text/css">
		<!-- jquery core -->
		
		<!-- jquery plugins -->
		<script src="css/ui/jquery.ui.datepicker.js"></script>
		<script src="css/jquery.validate.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$('#processedDateFrom').datepicker({maxDate: '0',onSelect: function(date) {
				date = $(this).datepicker('getDate');
		        var maxDate = new Date(date.getTime());
		        maxDate.setDate(maxDate.getDate() + 30);
		        $('#processedDateTo').datepicker('option', {minDate: date, maxDate: maxDate});
		    }});
			
			$('#processedDateTo').datepicker();
			
			$('#tascSearchErButton').on("click", function(){
				validateForm();
			});
			
			$("#errorLogDialog").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: true, 
				height: 100, 
				minWidth: 450, 
				closeOnEscape: true, 
				resizable: true
			});
			
		});
		
		function validateForm(){
			var processedDateFrom = $('#processedDateFrom').val();
			var processedDateTo = $('#processedDateTo').val();
			var uuid = $('#uuid').val();
			var recordId = $('#recordId').val();
			var processId = $('#processId').val();
			var lastName = $('#lastName').val();
			var exceptionCode = $('#exceptionCode').val();
			var stateCode = $('#stateCode').val();
			var form = $('#form').val();
			var testElementId = $('#testElementId').val();
			var barcode = $('#barcode').val();
			
			if((processedDateFrom=="" || processedDateTo=="") && uuid=="" && recordId==""
					&& processId=="" && lastName=="" && exceptionCode=="" && stateCode==""
					&& form=="" && testElementId=="" && barcode==""){
				jQuery("#errorLogDialog").dialog("open");
				$("#ui-dialog-title-errorLogDialog").html('Error');
				$("#errorLog").html("Processed Date range is required.");
			} else{
				$('#searchTascErForm').submit();
			}
		}
		
	</script>
	
	<style>
	label.error {
		padding-left: 16px;
		margin-left: .3em;
        color: red;
	}
	label.valid {
		display: block;
		width: 16px;
		height: 16px;
	}
	td {
		border: 0;
		/*border-right: 1px solid #e5e5e5;
		border-left: 1px solid #e5e5e5;*/
		border-bottom: 1px solid #e5e5e5;
		padding: 5px
	}
</style>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	     
		<h1>Search TASC Processes ER</h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchTascEr" method="post" action="searchTascErNew.htm" id="searchTascErForm">
						<table width="100%">
							<tr>
								<td>Source System:</td>
								<td>
									<select name="sourceSystem" id="sourceSystem">
										<option value="OAS">Online</option>
										<option value="PP">Paper Pencil</option>
										<option value="ERESOURCE">eResources</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>Processed Date From:</td>
								<td>
									<input type="text" name="processedDateFrom" id="processedDateFrom">
									<span style="color:red;">* Date range should be 30 days or less.</span>
								</td>
							</tr>
							<tr>
								<td>Processed Date To:</td>
								<td><input type="text" name="processedDateTo" id="processedDateTo"></td>
							</tr>
							<tr>
								<td>UUID:</td>
								<td><input type="text" name="uuid" id="uuid"></td>
							</tr>
							<tr>
								<td>Record ID (Only for eResources):</td>
								<td><input type="text" name="recordId" id="recordId"></td>
							</tr>
							<tr>
								<td>Process ID (Except eResources):</td>
								<td><input type="text" name="processId" id="processId"></td>
							</tr>
							<tr>
								<td>Last Name:</td>
								<td><input type="text" name="lastName" id="lastName"></td>
							</tr>
							<tr>
								<td>Exception Code:</td>
								<td><input type="text" name="exceptionCode" id="exceptionCode"></td>
							</tr>
							<tr>
								<td>State Code:</td>
								<td><input type="text" name="stateCode" id="stateCode"></td>
							</tr>
							<tr>
								<td>Form:</td>
								<td><input type="text" name="form" id="form"></td>
							</tr>
							<tr>
								<td>Test Element ID:</td>
								<td><input type="text" name="testElementId" id="testElementId"></td>
							</tr>
							<tr>
								<td>Barcode:</td>
								<td><input type="text" name="barcode" id="barcode"></td>
							</tr>
							<tr>
								<td></td>
								<td><input type="button" value="Search" id="tascSearchErButton"></td>
							</tr>
						</table>
						</form>
					</div>
					<div id='errorLogDialog' title='Loading' style='display:none; font-size:10px'>
						<p id="errorLog"><img src="css/ajax-loader.gif"></img><p>
					</div>
				</div>
		</div>
	</div>
</div>