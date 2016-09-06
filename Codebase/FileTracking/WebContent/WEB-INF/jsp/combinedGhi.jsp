<%@page import="com.vaannila.TO.StudentDetailsTO"%>
<%@page import="com.vaannila.TO.SearchProcess"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<link rel="stylesheet" href="css/highlight.css" type="text/css">
<link rel="stylesheet" href="css/demo.css" type="text/css">
<style>
	.locked {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -166px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
	.unlocked {
		display: block;
		float: left;
		width: 34px;
		height: 34px;
		background: url(css/sprites.png) no-repeat -30px -94px;
		margin-left: -12px;
		margin-top: -9px;
		position: relative;
		z-index: 88;
	}
	.no-border {
		border: 0;
		padding: 6px
	}
</style>
<script src="css/jquery.validate.js"></script>
	
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		oTable = $('#processGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 1, "asc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] }
							]
		});
		
		oTable2 = $('#errorGhi').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sScrollX": '100%',
			"aaSorting": [[ 9, "desc" ]],
			"aoColumnDefs": [ 
							  { "bVisible": false, "aTargets": [ 0 ] }
							]
		});
		
		/* $('#showComments').click(function(){
			showComments();
		});
		
		$("#saveComment").click(function(){
			saveComment();
		}); */
    });
	
	/* function showComments() {
		$("#commentErrorLog").hide();
		$("textarea#comments").val( $("textarea#hiddenComments").val() );
		jQuery("#showCommentsDialog").dialog({
			title: 'Comments: ',
			width: 510,
			height: 300,
			modal: true,
			closeOnEscape: false,
			beforeClose: function( event, ui ) {
				if( $("#hiddenComments").text() != $("textarea#comments").val() ) {
					var $dlg = $(this);
			        if($dlg.data('can-close')) {
			            $dlg.removeData('can-close');
			            return true;
			        }
			        $("#confirm").dialog({
			            width: 500,
			            modal: true,
			            closeOnEscape: false,
			            buttons: {
			                Confirm: function() {
			                    $(this).dialog('close');
			                    $dlg.data('can-close', true);
			                    $dlg.dialog('close');
			                },
			                Cancel: function() {
			                    $(this).dialog('close');
			                }
			            }
			        });
			        return false;
				}
			}
		});
		return false;
	}
	
	function saveComment(){
		var comments = $("textarea#comments").val();
		var stateCode = $("#commentStateCode").val();
		var uuid = $("#commentUuid").val();
		if(comments.length > 4000){
			$("#commentErrorLog").show();
			$("#commentErrorLog").css("color","red");
			$("#commentErrorLog").text("Maximum length is 4000. Please summarize your comment");
		}else{
			var dataString = "comments="+comments+"&stateCode="+stateCode+"&uuId="+uuid;
			$.ajax({
			      type: "POST",
			      url: "saveComments.htm",
			      data: dataString,
			      success: function(data) {
			    	  $("#commentErrorLog").show();
			    	  if(data.indexOf("sucessfully") > 0) {
			    		  $("#commentErrorLog").css("color","green");
			    		  
			    		  // resetting page hidden values
			    		  $("#comment-div").html("<br/>" + comments);
			    		  $("#hiddenComments").text(comments)
			    	  } else {
			    		  $("#commentErrorLog").css("color","red"); 
			    	  }						    	  
					  $("#commentErrorLog").text(data);
			      },
				  error: function(data) {
					  $("#commentErrorLog").show();
					  $("#commentErrorLog").css("color","red"); 
					  $("#commentErrorLog").text(data);
				  }
		    });
		}
	} */
</script>
	
<div id="heromaskarticle">
	<%-- <div id="showCommentsDialog" title="Loading ..." style='display:none; font-size:11px'>
		<p></p>
		<div class="commentContent" style="text-align: center">
			<input type="hidden" value="${uuid}" id="commentUuid"/>
			<input type="hidden" value="${stateCode}" id="commentStateCode"/>
			<div style="display:none">
				<textarea rows="10" cols="60" id="hiddenComments">${savedComments}</textarea>
			</div>
			
			<div style="padding: 10px;">
				<label style='font-size:13px; font-weight:bold; color:black;'>
					Enter Comments for UUID: ${uuid} and State Code: ${stateCode}
				</label>
			</div>
			<div>
				<textarea rows="10" cols="60" id="comments">${savedComments}</textarea>
			</div>
			<div style='font-size:13px; font-weight:bold;'>
				<p></p><button id="saveComment" >Save Comment</button>
			</div>
		</div>
		<div id='errorLogDialog' style="text-align: center">
			<p id="commentErrorLog" style='font-size:13px; font-weight:bold;'></p>
		</div>
	</div> --%>
	<!-- <div id="confirm" title="Confirm" style="display:none;">
	    Comments have changed. Do you want to continue without saving?
	</div> -->
		
	<div class="container" style="margin-left:25px;width: 900px !important;">
		<h1>Search a single student - GHI</h1>
		<div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 666px;float: left;">
			<%
			SearchProcess searchProcess = (SearchProcess) request.getSession().getAttribute("combinedRequestTO");
			/* String savedComments = (String)request.getAttribute("savedComments"); 
			String showCommentFlag = (String)request.getAttribute("showCommentFlag");  */
			if(searchProcess == null) searchProcess = new SearchProcess();
			%>
			<form name="combinedGhiForm" method="post" action="combinedGhi.htm" id="combinedGhiForm">
				<table width="100%">
					<thead>
						<tr>
							<td class="no-border" colspan="2">
                            	<div id="errorLogDialog" title="Loading" style="float: left;position: absolute;">
									<p id="errorLog" style="font-size:13px; font-weight:bold; color:red;">${message}</p>
								</div>
							</td>
                         </tr>
					</thead>
					 <tbody>
					 	<tr>
                           	<td class="no-border">&nbsp;</td>
                           	<td class="no-border">&nbsp;</td>
                        </tr>
						<tr>
							<td class="no-border">UUID:</td>
							<td class="no-border"><input type="text" name="uuid" id="uuid" value="<% if(searchProcess.getUuid() != null) out.print( searchProcess.getUuid());  %>"></td>
						</tr>
						<tr>
							<td class="no-border">State Code:</td>
							<td class="no-border"><input type="text" name="stateCode" id="stateCode" value="<% if(searchProcess.getStateCode() != null) out.print(searchProcess.getStateCode()); %>"></td>
						</tr>
						<tr>
							<td class="no-border">DRC Student ID (exact ID is needed):</td>
							<td class="no-border"><input type="text" name="drcStudentId" id="drcStudentId" value="<% if(searchProcess.getTestElementId() != null) out.print(searchProcess.getTestElementId()); %>"></td>
						</tr>
						
						<tr>
							<td class="no-border"></td>
							<td class="no-border"><input type="Submit" value="Search"></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<%-- <div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix" style="width: 400px;height: 172px;float: right;">
			<% if("false".equals(showCommentFlag)){ %>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments </h3>
				<div style="padding: 20px;color: red;">
					Please provide 9 digit UUID and 2 character state code to fetch comments.
				</div>
			<%}else{%>
				<h3 style="padding-left: 10px;margin-top: 10px;">Comments for UUID: ${uuid} and State Code: ${stateCode}
					<a id="showComments" href="#nogo" style="color: #00329B;text-decoration:underline;padding-left: 17px;">Add/Edit</a>
				</h3>
				<div id="comment-div" style="padding: 0px 10px;font-weight:normal;height: 141px;overflow:auto;white-space: pre-line">
					${savedComments}
				</div>
			<%}%>
		</div> --%>
	</div>
	<div id="articlecontent">
		<div id="accordion" style="margin-top:25px">
			<!-- panel -->
			<h4>Data Compared to Operational (PRISM report data)</h4>
			<table id="processGhi" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>DRC Student ID</th>
						<th>State</th>
						<th>Subtest</th>
						<th>Form</th>
						<th>Mode</th>
						<th>Barcode</th>
						<th>Status Code</th>
						<th>SS</th>
						<th>HSE</th>
						<th>Score Date</th>
						<th>TestElem ID</th>
						<th>Name</th>
						<th>UUID</th>
						<th>Bio ID</th>
						<th>Level1 Org Code</th>
					</tr>
					</thead>
				<tbody>
			<% 
			java.util.List<StudentDetailsTO> allProcess = (ArrayList) request.getAttribute("combinedGhiList");
			int count=0;
			if(allProcess != null) {
			for(StudentDetailsTO process : allProcess) {
				count++;
			%>
				<tr>
					<td>&nbsp;</td>
					<td><%=process.getDrcStudentId() %></td>
					<td><%=process.getStateCode() %></td>
					<td><%=process.getSubtestName() %></td>
					<td><%=process.getForm() %></td>
					<td><%=process.getSourceSystem() %></td>
					<td><%=process.getBarcode() %></td>
					<td><%=process.getStatusCode() %></td>
					<td><%=process.getSs() %></td>
					<td><%=process.getHse() %></td>
					<td><%=process.getUpdatedDate() %></td>
					<td><%=process.getTestElementId() %></td>
					<td><%=process.getStudentName() %></td>
					<td><%=process.getUuid() %></td>
					<td><%=process.getStudentBioId() %></td>
					<td><%=process.getLevel1OrgCode() %></td>
				</tr>
			<%}
			}
			%>
			</tbody>
			</table>
			
			<br/><br/>
			<h4> Latest Error: </h4>
			<table id="errorGhi" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>L Name</th>
					<th>Test Element Id</th>
					<th>Subtest</th>
					<th>Form</th>
					<th>Barcode</th>
					<th>Test Date</th>
					<th>Description</th>
					<th>Date Received</th>
				</tr>
				</thead>
				<tbody>
			<% 
			java.util.List<StudentDetailsTO> errorGhi = (ArrayList) request.getAttribute("errorGhi");
			if(errorGhi != null) {
			for(StudentDetailsTO error : errorGhi) {
			%>
				<tr>
					<td>&nbsp;</td>
					
					<td><%=error.getStudentName() %></td>
					<td><%=error.getTestElementId() %></td>
					<td><%=error.getContantArea() %></td>
					<td><%=error.getForm() %></td>
					<td><%=error.getBarcode() %></td>
					<td><%=error.getTestDate() %></td>
					<td><%=error.getErrorLog() %></td>
					<td><%=error.getCreatedDate() %></td>
				</tr>
			
			<%}
			}
			%>
			</tbody>
			</table>
			
			<br/><br/>
			<p><b>Source System:</b> PP = Paper Pencil</p>
			<!-- <p><b>Overall Status:</b><br/>
				<span class="locked" title="Completed"></span> = Locked <br/>
				<span class="unlocked" title="Completed" style="margin-left: -34px;"></span> = Open
			</p> -->
			
			<div id='processLogDialog' title='Loading' style='display:none; font-size:10px'>
				<p id="_process_log"><img src="css/ajax-loader.gif"></img><p>
			</div>
			
		</div>
	</div>
</div>