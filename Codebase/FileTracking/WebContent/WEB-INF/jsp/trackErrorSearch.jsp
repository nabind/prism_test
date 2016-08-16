<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<link rel="stylesheet" href="css/coCheck.css" type="text/css"/>

<script type="text/javascript" src="js/modules/trackError.js"></script>	
<script src="css/ui/jquery.ui.datepicker.js"></script>
<script src="css/jquery.validate.js"></script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
	     
		<h1>Track Error for GHI forms</h1>
				
				<div class="container" >
					<div class="content">
						<form name="trackErrorForm" method="post" action="searchError.htm" id="trackErrorForm">
							<table width="100%">
								<tr>
									<td>Status:</td>
									<td>
										<select name="trackErrorStatus" id="trackErrorStatus">
											<option value="-1">All</option>
											<option value="ER">Error</option>
											<option value="CO">Completed</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Prism Process Date From:</td>
									<td>
										<input type="text" name="errorDateFrom" id="errorDateFrom">
									</td>
								</tr>
								<tr>
									<td>Prism Process Date To:</td>
									<td><input type="text" name="errorDateTo" id="errorDateTo"></td>
								</tr>
								<tr>
									<td>DRC Student ID:</td>
									<td><input type="text" name="DRCStudentID" id="DRCStudentID"></td>
								</tr>
								<tr>
									<td>DRC DocumentID:</td>
									<td><input type="text" name="DRCDocumentID" id="DRCDocumentID"></td>
								</tr>
								<tr>
									<td>UUID:</td>
									<td><input type="text" name="uuid" id="uuid"></td>
								</tr>
								<tr>
									<td>Last Name:</td>
									<td><input type="text" name="lastName" id="lastName"></td>
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
									<td><input type="submit" value="Search" id=errorSearchButton"></td>
								</tr>
							</table>
						</form>
					</div>
					<div id='errorFormErrorDialog' title='Loading' style='display:none; font-size:10px'>
						<p id="errorFormErrorLog"><img src="css/ajax-loader.gif"></img><p>
					</div>
				</div>
		</div>
	</div>
</div>