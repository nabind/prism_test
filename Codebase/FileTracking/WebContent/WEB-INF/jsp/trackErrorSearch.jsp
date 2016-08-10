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
	     
		<h1>Search Error</h1>
				
				<div class="container" >
					<div class="content">
						<form name="errorForm" method="post" action="searchError.htm" id="errorForm">
							<table width="100%">
								<tr>
									<td>DRC Student ID:</td>
									<td><input type="text" name="DRCStudentID" id="DRCStudentID"></td>
								</tr>
								<tr>
									<td>DRCDocumentID:</td>
									<td><input type="text" name="DRCDocumentID" id="DRCDocumentID"></td>
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
									<td></td>
									<td><input type="button" value="Search" id=errorSearchButton"></td>
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