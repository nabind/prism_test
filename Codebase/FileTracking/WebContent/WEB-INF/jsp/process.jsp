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
	
			$( "#updatedDate" ).datepicker();
			
			$("#searchProcess").validate({
				  rules: {
					  processId: {
				      number: true
				    },
					structElem: {
				      number: true
				    }
				  }
			});
			
			var bEnabled = document.getElementById("hEnabled").value;
			if (bEnabled == 'F'){
				document.getElementById("theSelect").setAttribute("disabled", "disabled");
				//$("#theSelect option:selected").attr('disabled', 'disabled');
			}
		    
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
	     <%   Properties prop = PropertyFile.loadProperties("acsi.properties");
			  String isEnabled = prop.getProperty("admin.year.enable");
		 %>
	    <input type="hidden" value="<%=isEnabled%>" id="hEnabled">	  
		<h1>Search ACSI Process for Administration
		<select onChange="refreshList(this.value, 'view.htm')" id="theSelect">
		 <%     java.util.List<AdminTO> adminList = (ArrayList) request.getSession().getAttribute("adminList");
				String adminyear = (String) request.getSession().getAttribute("adminid");
				for(AdminTO admin : adminList) { 
					if(adminyear != null && adminyear.equals(admin.getAdminId())) {
					%>
					<option value="<%=admin.getAdminId() %>" selected="true" ><%=admin.getAdminName() %></option>
					<%} else {%>
					<option value="<%=admin.getAdminId() %>" ><%=admin.getAdminName() %></option>
					<%} %>
				<%} %>
			</select></h1>
				
				<div class="container" >
					<div class="content">
						<form name="searchProcess" method="post"
											action="search.htm" id="searchProcess">
						<table width="100%">
							<tr>
								<td width="30%">Process Id:</td>
								<td width="70%"><input type="text" name="processId" id="processId"><label for="processId">&nbsp;</label></td>
							</tr>
							<tr>
								<td>School Structure Element Id:</td>
								<td><input type="text" name="structElem" id="structElem"><label for="structElem">&nbsp;</label></td>
							</tr>
							<tr>
								<td>Processed Date:</td>
								<td><input type="text" name="updatedDate" id="updatedDate"></td>
							</tr>
							<tr>
								<td>Stage Load Status:</td>
								<td><select name="dataStatus">
									<option value="">All</option>
									<option value="AC">Active</option>
									<option value="BL">Blank</option>
									<option value="CP">Completed</option>
									<option value="ER">Error</option>
									<option value="IN">Inactive</option>
									<option value="IP">In Progress</option>
									<option value="SU">Success</option>
								</select></td>
							</tr>
							<tr>
								<td>Stage PDF Status:</td>
								<td><select name="pdfStatus">
									<option value="">All</option>
									<option value="AC">Active</option>
									<option value="BL">Blank</option>
									<option value="CP">Completed</option>
									<option value="ER">Error</option>
									<option value="IN">Inactive</option>
									<option value="IP">In Progress</option>
									<option value="SU">Success</option>
								</select></td>
							</tr>
							<tr>
								<td>Target Load Status:</td>
								<td><select name="targetStatus">
									<option value="">All</option>
									<option value="AC">Active</option>
									<option value="BL">Blank</option>
									<option value="CP">Completed</option>
									<option value="ER">Error</option>
									<option value="IN">Inactive</option>
									<option value="IP">In Progress</option>
									<option value="SU">Success</option>
								</select></td>
							</tr>
							<tr>
								<td>Email Status:</td>
								<td><select name="emailStatus">
									<option value="">All</option>
									<option value="AC">Active</option>
									<option value="BL">Blank</option>
									<option value="CP">Completed</option>
									<option value="ER">Error</option>
									<option value="IN">Inactive</option>
									<option value="IP">In Progress</option>
									<option value="SU">Success</option>
								</select></td>
							</tr>
							<tr>
								<td>Process Status:</td>
								<td><select name="processStatus">
									<option value="">All</option>
									<option value="AC">Active</option>
									<option value="BL">Blank</option>
									<option value="CP">Completed</option>
									<option value="ER">Error</option>
									<option value="IN">Inactive</option>
									<option value="IP">In Progress</option>
									<option value="SU">Success</option>
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