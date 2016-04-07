<%@page import="com.vaannila.util.PropertyFile" %>

	<link rel="stylesheet" href="css/highlight.css" type="text/css">
		<link rel="stylesheet" href="css/demo.css" type="text/css">
		<!-- jquery core -->
		
		<!-- jquery plugins -->
		<script src="css/ui/jquery.ui.datepicker.js"></script>
		<script src="css/jquery.validate.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
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
	     
		<h1>Support Page</h1>
				
				<div class="container" >
					<div class="content">
						<form name="support" method="post" action="support.htm" id="supportForm">
						<table width="100%">
							<tr>
							    <td>Enter UUID</td>
								<td>
									<input type="text" name="uuid" id="uuid">
									<span style="color:red;"></span>
								</td>
								<td>Enter ScheduleId to unlock</td>
								<td>
									<input type="text" name="scheduleId" id="scheduleId">
									<span style="color:red;"></span>
								</td>
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
