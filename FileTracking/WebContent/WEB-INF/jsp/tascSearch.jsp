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
			$( "#updatedDateFrom" ).datepicker();
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
		});
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
						<form name="searchTasc" method="post" action="searchTasc.htm" id="searchTasc">
						<table width="100%">
							<tr>
								<td>Processed Date From:</td>
								<td><input type="text" name="updatedDateFrom" id="updatedDateFrom"></td>
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
								<td><input type="submit" value="Search Process"></td>
							</tr>
						</table>
						</form>
					</div>
					
				
				
			</div>

	</div>
</div>
</div>