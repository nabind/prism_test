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
			$( "#processedDateFrom" ).datepicker();
			$( "#processedDateTo" ).datepicker();
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
	     
		<h1>Search TASC Processes ER</h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchTascEr" method="post" action="searchTascEr.htm" id="searchTascEr">
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
								<td><input type="text" name="processedDateFrom" id="processedDateFrom"></td>
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
								<td>Last Name:</td>
								<td><input type="text" name="lastName" id="lastName"></td>
							</tr>
							<tr>
								<td>Exception Code:</td>
								<td><input type="text" name="exceptionCode" id="exceptionCode"></td>
							</tr>
							<tr>
								<td>Subject (Content Area/Subtest) :</td>
								<td>
									<select name="subjectCa" id="subjectCa">
										<option value="ALL">All</option>
										<option value="1">Reading</option>
										<option value="2">Writing</option>
										<option value="3">English-Language Arts</option>
										<option value="4">Mathematics</option>
										<option value="5">Science</option>
									</select>
								</td>
							</tr>
							<tr>
								<td></td>
								<td><input type="submit" value="Search"></td>
							</tr>
						</table>
						</form>
					</div>
				</div>
		</div>
	</div>
</div>