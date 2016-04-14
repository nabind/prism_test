<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/coCheck.css" type="text/css"/>

<script type="text/javascript" src="js/modules/coCheck.js"></script>
<script src="css/jquery.validate.js"></script>	
	
<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				<% 
				SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("coCheckTO");
				%>
				<div style="float: right">
					<a href='downloadCsvWin.htm' class='noteLink ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover' 
						style='padding:5px;top:5px;color:#00329B;text-decoration:underline' target="_blank">
						Download CSV
					</a>
				</div>
				<div class="search-criteria">
				<b>Showing search results for</b> Status: <%=searchProcess.getProcessStatusDesc()%>
				<%if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){%>
					, Winscore Export Date From: <%=searchProcess.getProcessedDateFrom()%>
				<%}%>
				<%if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){%>
					, Winscore Export Date To: <%=searchProcess.getProcessedDateTo()%>
				<%}%>
				<%if(searchProcess.getImagingId() != null && searchProcess.getImagingId().trim().length() > 0){%>
					, Imaging ID: <%=searchProcess.getImagingId()%>
				<%}%>
				<%if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){%>
					, Barcode: <%=searchProcess.getBarcode()%>
				<%}%>				
				</div>
				
				<table id="coCheckResultTable" width="100%">
					<thead>
						<tr>
							<th >&nbsp;</th>
							<th style="min-width: 60px;">Scan Batch</th>
							<th>District Number</th>
							<th>School Number</th>
							<th>UUID</th>
							<th>Bar Code</th>
							<th>Form</th>
							<th>Braille</th>
							<th>Large Print</th>
							<th>Date Test Taken</th>
							<th>LogIn Date</th>
							<th>Scan Date</th>
							<th>Winscore Export Date</th>
							<th>More Info</th>
						</tr>
					</thead>
				</table>
			
				<p><p>
					<b>Overall Status:</b><br/>
					<span class="completed legend" title="Completed"></span> = Completed/Success (CO) <br/>
					<span class="progress legend legend2" title="Completed" style="margin-left: -34px;"></span> = Record not received by Prism (NR)<br/>
					<span class="error legend legend2 legend3" title="Completed" style="margin-left: -34px;"></span> = Error (Record received by Prism, but there is an error) (ER)</br>
					<span class="invalidated legend4" title="Invalidated"></span> = Invalidated (IN)
				</p>
				
				<div id="moreInfoDialog" title="Loading ..." style='display:none; font-size:11px'>
					<p id="moreInfo"><p><p>
					<table width="100%" class="process_details">
						<tr>
							<td width="44%"><b>Imaging ID :</b></td><td width="56%"><span id="imagingId"></span></td>
						</tr>
						<tr>
							<td><b>OrgTPName :</b></td><td><span id="orgTpName"></span></td>
						</tr>
						<tr>
							<td><b>LastName :</b></td><td><span id="lastName"></span></td>
						</tr>
						<tr>
							<td><b>FirstName :</b></td><td><span id="firstName"></span></td>
						</tr>
						<tr>
							<td><b>MiddleInitial :</b></td><td><span id="middleInitial"></span></td>
						</tr>
						<tr>
							<td><b>LithoCode :</b></td><td><span id="lithoCode"></span></td>
						</tr>
						<tr>
							<td><b>StudentScanStk :</b></td><td><span id="scanStack"></span></td>
						</tr>
						<tr>
							<td><b>StudentScanSeq :</b></td><td><span id="scanSequence"></span></td>
						</tr>
						<tr>
							<td><b>WinscoreDocId :</b></td><td><span id="winsDocId"></span></td>
						</tr>
						<tr>
							<td><b>Commodity Code :</b></td><td><span id="comodityCode"></span></td>
						</tr>
						<tr>
							<td><b>WinscoreStatus :</b></td><td><span id="winStatus"></span></td>
						</tr>
						<tr>
							<td><b>Prism Status :</b></td><td><span id="prismProcessStatusDesc"></span></td>
						</tr>
						<tr>
							<td><b>Image File Path(s) :</b></td><td><span id="imageFilePath"></span></td>
						</tr>
						<tr>
							<td><b>Image File Name(s) :</b></td><td><span id="imageFileName"></span></td>
						</tr>
					</table>
				</div>
				<div id='errorLogDialog' title='Loading' style='display:none; font-size:10px'>
					<p id="errorLog"><img src="css/ajax-loader.gif"></img><p>
				</div>
			</div>
	</div>
</div>
</div>
