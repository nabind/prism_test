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
			
			$('#tascSearchButton').live("click", function(){
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
			
			if(updatedDateFrom=="" || updatedDateTo==""){
				jQuery("#errorLogDialog").dialog("open");
				$("#ui-dialog-title-errorLogDialog").html('Error');
				$("#errorLog").html("Atleast Processed Date range is required.");
			} else{
				$('#searchTascForm').submit();
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
	     
		<h1>Search TASC Process</h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchTasc" method="post" action="searchTasc.htm" id="searchTascForm">
						<table width="100%">
							<tr>
								<td>Processed Date From:</td>
								<td>
									<input type="text" name="updatedDateFrom" id="updatedDateFrom">
									<span style="color:red;">*Date range should be 30 days.</span>
								</td>
							</tr>
							<tr>
								<td>Processed Date To:</td>
								<td><input type="text" name="updatedDateTo" id="updatedDateTo"></td>
							</tr>
							<tr>
								<td>Source System:</td>
								<td><select name="sourceSystem">
									<option value="">All</option>
									<option value="OL">Online</option>
									<option value="PP">Paper Pencil</option>
								</select></td>
							</tr>
							
							<tr>
								<td></td>
								<td><input type="button" id="tascSearchButton" value="Search Process"></td>
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