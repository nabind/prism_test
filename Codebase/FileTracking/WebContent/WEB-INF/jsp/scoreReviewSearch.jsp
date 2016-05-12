<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<link rel="stylesheet" href="css/coCheck.css" type="text/css"/>

<script type="text/javascript" src="js/modules/scoreReview.js"></script>	
<script src="css/ui/jquery.ui.datepicker.js"></script>
<script src="css/jquery.validate.js"></script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	     
		<h1>Score Review Search</h1>
				
				<div class="container" >
					<div class="content">
						<form name="coCheckForm" method="post" action="scoreReviewResult.htm" id="scoreReviewForm">
							<table width="100%">
								<tr>
									<td>Source System:</td>
									<td>
										<select name="sourceSystem" id="sourceSystem">
											<option value="-1" selected>All</option>
											<option value="OL">Online</option>
											<option value="PP">Paper Pencil</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Status:</td>
									<td>
										<select name="scrStatus" id="scrStatus">
											<option value="-1">All</option>
											<option value="RV" selected>In Review</option>
											<option value="AP">Approved</option>
											<option value="RJ">Rejected</option>
											<option value="AE">Approved with Error</option>
											<option value="PR">Processed</option>
											<option value="IN">Invalidated by System</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Processed Date From:</td>
									<td>
										<input type="text" name="dateFrom" id="dateFrom">
									</td>
								</tr>
								<tr>
									<td>Processed Date To:</td>
									<td><input type="text" name="dateTo" id="dateTo"></td>
								</tr>
								<tr>
									<td>UUID:</td>
									<td><input type="text" name="uuid" id="uuid"></td>
								</tr>
								<tr>
									<td>State Code:</td>
									<td><input type="text" name="stateCode" id="stateCode"></td>
								</tr>
								<tr>
									<td></td>
									<td><input type="button" value="Search" id="searchScoreReview"></td>
								</tr>
							</table>
						</form>
					</div>
					<div id='coCheckErrorDialog' title='Loading' style='display:none; font-size:10px'>
						<p id="coCheckErrorLog"><img src="css/ajax-loader.gif"></img><p>
					</div>
				</div>
		</div>
	</div>
</div>