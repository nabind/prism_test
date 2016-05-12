<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
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
			
			$( "#updatedDateFrom" ).datepicker({maxDate: '0',onSelect: function(date) {
				date = $(this).datepicker('getDate');
		        var maxDate = new Date(date.getTime());
		        maxDate.setDate(maxDate.getDate() + 30);
		        $('#updatedDateTo').datepicker('option', {minDate: date, maxDate: maxDate});
		    }});
			
			$( "#updatedDateTo" ).datepicker();
			
			$("#searchTasc").validate({
				  rules: {
					  updatedDateFrom: {
				      date: true
				    },
				    updatedDateTo: {
				      date: true
				    }
				  }
			});
			
			$('#mapSearchButton').live("click", function(){
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
			var updatedDateFrom = $('#updatedDateFrom').val();
			var updatedDateTo = $('#updatedDateTo').val();
			var processid = $('#processId').val();
			if(processid == "" && (updatedDateFrom=="" || updatedDateTo=="")){
				jQuery("#errorLogDialog").dialog("open");
				$("#ui-dialog-title-errorLogDialog").html('Error');
				$("#errorLog").html("Processed Date range is required.");
			} else{
				$('#searchMapForm').submit();
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
	     
		<h1>Search MAP Process</h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchTasc" method="post" action="searchMap.htm" id="searchMapForm">
						<table width="100%">
							<tr>
								<td>Processed Date From:</td>
								<td>
									<input type="text" name="updatedDateFrom" id="updatedDateFrom">
									<span style="color:red;">* Date range should be 30 days or less.</span>
								</td>
							</tr>
							<tr>
								<td>Processed Date To:</td>
								<td><input type="text" name="updatedDateTo" id="updatedDateTo"></td>
							</tr>
							<tr>
								<td>Process Id:</td>
								<td><input type="text" name="processId" id="processId"></td>
							</tr>
							<tr>
								<td>District Code:</td>
								<td><input type="text" name="districtCode" id="districtCode"></td>
							</tr>
							<tr>
								<td>Grade:</td>
								<td><select name="grade">
									<option value="">All</option>
									<option value="03">Grade 3</option>
									<option value="04">Grade 4</option>
									<option value="05">Grade 5</option>
									<option value="06">Grade 6</option>
									<option value="07">Grade 7</option>
									<option value="08">Grade 8</option>
								</select></td>
							</tr>
							<tr>
								<td>Content Area:</td>
								<td><select name="subtest">
									<option value="">All</option>
									<option value="English Language Arts">English/Language Arts</option>
									<option value="Mathematics">Mathematics</option>
									<option value="Science">Science</option>
								</select></td>
							</tr>
							<tr>
								<td>Process Status:</td>
								<td><select name="processStatus">
									<option value="">All</option>
									<option value="CO">Completed</option>
									<option value="IN">In Progress</option>
									<option value="ER">Error</option>
								</select></td>
							</tr>
							
							
							<tr>
								<td></td>
								<td><input type="button" id="mapSearchButton" value="Search Process"></td>
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