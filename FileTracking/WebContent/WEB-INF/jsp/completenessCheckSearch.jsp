<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<link rel="stylesheet" href="css/tasc.css" type="text/css"/>
	
<script src="css/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="js/modules/tasc.js"></script>
<script src="css/jquery.validate.js"></script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	     
		<h1>Search PP Completeness Check</h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchCompletenessCheck" method="post" action="searchCompletenessCheck.htm" id="searchCompletenessCheckForm">
						<table width="100%">
							<tr>
								<td>Status:</td>
								<td>
									<select name="coCheckStatus" id="coCheckStatus">
										<option value="-1">All</option>
										<option value="ER">Error</option>
										<option selected value="NR">Not Received</option>
										<option value="CO">Completed</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>Winscore Export Date From:</td>
								<td>
									<input type="text" name="coCheckDateFrom" id="coCheckDateFrom">
									<span style="">* Date range should be 365 days or less.</span>
								</td>
							</tr>
							<tr>
								<td>Winscore Export Date To:</td>
								<td><input type="text" name="coCheckDateTo" id="coCheckDateTo"></td>
							</tr>
							<tr>
								<td>Imaging ID:</td>
								<td><input type="text" name="imagingId" id="imagingId"></td>
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