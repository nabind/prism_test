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

<script type="text/javascript" src="js/modules/trackError.js"></script>
<script src="css/jquery.validate.js"></script>	
	
<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<!-- panel -->
				<% 
				SearchProcess searchError = (SearchProcess) request.getSession().getAttribute("errorTrackingTO");
				%>
				<div style="float: right">
					<a href='downloadCsvGhi.htm' class='noteLink ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover' 
						style='padding:5px;top:5px;color:#00329B;text-decoration:underline' target="_blank">
						Download CSV
					</a>
				</div>
				<div class="search-criteria">
				<b>Showing search results for</b> Status: <%=searchError.getProcessStatusDesc()%>
				<%if(searchError.getErrorDateFrom() != null && searchError.getErrorDateFrom().trim().length() > 0){%>
					, Error tracking Date From: <%=searchError.getErrorDateFrom()%>
				<%}%>
				<%if(searchError.getErrorDateTo() != null && searchError.getErrorDateTo().trim().length() > 0){%>
					, Error tracking Date To: <%=searchError.getErrorDateTo()%>
				<%}%>
				<%if(searchError.getDRCStudentId() != null && searchError.getDRCStudentId().trim().length() > 0){%>
					, DRC Student ID: <%=searchError.getDRCStudentId()%>
				<%}%>
				<%if(searchError.getDRCDocumentId() != null && searchError.getDRCDocumentId().trim().length() > 0){%>
					, DRC Document ID: <%=searchError.getDRCDocumentId()%>
				<%}%>
				<%if(searchError.getUuid() != null && searchError.getUuid().trim().length() > 0){%>
					, UUID: <%=searchError.getUuid()%>
				<%}%>		
				<%if(searchError.getLastName() != null && searchError.getLastName().trim().length() > 0){%>
					, Last Name: <%=searchError.getLastName()%>
				<%}%>
				<%if(searchError.getStateCode() != null && searchError.getStateCode().trim().length() > 0){%>
					, State Code: <%=searchError.getStateCode()%>
				<%}%>
				<%if(searchError.getForm() != null && searchError.getForm().trim().length() > 0){%>
					, Form: <%=searchError.getForm()%>
				<%}%>
				<%if(searchError.getTestElementId() != null && searchError.getTestElementId().trim().length() > 0){%>
					, Test Element ID: <%=searchError.getTestElementId()%>
				<%}%>
				<%if(searchError.getBarcode() != null && searchError.getBarcode().trim().length() > 0){%>
					, Barcode: <%=searchError.getBarcode()%>
				<%}%>		
				</div>
				
				<table id="errorResultTable" width="100%">
					<thead>
						<tr>
							<th >&nbsp;</th>
							<th>Record Id</th>
							<th>File Name</th>
							<th>File Generation Date-Time</th>
							<th>OrgID~TP</th>
							<th>DRC Student ID</th>
							<th>State Code</th>
							<th>Examinee ID</th>
							<th>Error Code & Desc</th>
							<th>Student Name</th>
							<th>DOB</th>
							<th>Gender</th>
							<th>Prism Process Date</th>
							<th>Org Code Path</th>
							<th>Test Center Code</th>
							<th>Test Center Name</th>
							<th>Document ID</th>
							<th>Schedule ID</th>
							<th>TCA Schedule Date</th>
							<th>ImagingID</th>
							<th>Litho Code</th>
							<th>Test Mode</th>
							<th>Test Language</th>
							<th>Content Name</th>
							<th>Form</th>
							<th>Date Test Taken</th>
							<th>BarcodeID</th>
							<th>Content Score (NC)</th>
							<th>Content Test Code</th>
							<th>Scale Score</th>
							<th>Scanned Process Date</th>
							<th>Status Code for Content Area</th>
						</tr>
					</thead>
				</table>
			
				<p>
					<b>Overall Status:</b><br/>
					<span class="completed legend" title="Completed"></span> = Completed/Success (CO) <br/>
					<span class="error legend legend2" title="Completed" style="margin-left: -34px;"></span> = Error (Record received by Prism, but there is an error) (ER)</br>
				</p>
				
				<div id="historyDialog" title="Loading ..." style='display:none; font-size:11px;'>
					<p id="review"><p><p>
					<table id="historyTable" width="100%">
						<thead>
							<tr>
								<th>Record Id</th>
								<th>File Name</th>
								<th>File Generation Date-Time</th>
								<th>OrgID~TP</th>
								<th>State Code</th>
								<th>Examinee ID</th>
								<th>Error Code & Desc</th>
								<th>Student Name</th>
								<th>DOB</th>
								<th>Gender</th>
								<th>Prism Process Date</th>
								<th>Org Code Path</th>
								<th>Test Center Code</th>
								<th>Test Center Name</th>
								<th>Document ID</th>
								<th>Schedule ID</th>
								<th>TCA Schedule Date</th>
								<th>ImagingID</th>
								<th>Litho Code</th>
								<th>Test Mode</th>
								<th>Test Language</th>
								<th>Content Name</th>
								<th>Form</th>
								<th>Date Test Taken</th>
								<th>BarcodeID</th>
								<th>Content Score (NC)</th>
								<th>Content Test Code</th>
								<th>Scale Score</th>
								<th>Scanned Process Date</th>
								<th>Status Code for Content Area</th>
							</tr>
						</thead>
					</table>
				</div>	
		</div>
	</div>
</div>

