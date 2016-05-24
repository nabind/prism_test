<!DOCTYPE html>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.StudentTO" %>
<%@page import="com.vaannila.TO.ScoreTO" %>
<%@page import="com.vaannila.TO.ContentTO" %>
<%@page import="com.vaannila.TO.StudentScoresTO" %>
<%@page import="java.util.ArrayList, java.util.List" %>
<%@page import="com.vaannila.web.UserController" %>
<html>
<head>
	
	<title>Growth Report</title>

    <link rel="stylesheet" type="text/css" href="examples.css" />
	
  
  <link rel="stylesheet" href="themes/base/jquery.ui.all.css"> 
  <script type="text/javascript" src="jquery.min.js"></script>
  <script type="text/javascript" src="ui/jquery-ui-1.8.17.custom.js"></script>
  

    
	<style>
		.progressbar {padding-bottom:5px}
		.ui-dialog-title {font-size:12px}
		.ui-progressbar-value { background: #00CD00 url(progressbar.gif) 50% 50% repeat-x; }
		.loading{font-weight:bold; padding-bottom:5px}
		.jqplot-series-3 {font-weight:bold}
	</style>
    <script>
	
		function update_rows() {
			$(".table tr:even").css("background-color", "#fff");
			$(".table tr:odd").css("background-color", "#eeece1");
			$(".table tr.nobackground").css("background-color", "#fff");
		}
		
		function getProgressStatus() {
			$(".keepAlive").trigger('click');
		}
		var alertInterval = 2000;
		var t = setTimeout("getProgressStatus()", alertInterval);
		
		$(document).ready(function() {
			
			 update_rows();
			 var dataString = "";
			 $(".keepAlive").click(function() {
				  $.ajax({
				      type: "POST",
				      url: "getCount.htm",
				      data: dataString,
				      success: function(data) {
				    	  $( "#progressbar_graph" ).progressbar( "option", "value", data.graph );
				    	  $( "#progressbar_table" ).progressbar( "option", "value", data.table );
				    	  $( "#g_ptg" ).text(data.graph + '%');
				    	  $( "#t_ptg" ).text(data.table + '%');
				    	  if(data.graph < 100 || data.table < 100) {
				    		  setTimeout("getProgressStatus()", alertInterval);
				    	  }
				      },
					  error: function(data) {
						  setTimeout("getProgressStatus()", alertInterval);
					  }
			    });
			    return false;
			 });
			 
			 $("#progressbar_graph").progressbar({ value: 0 });
			 $("#progressbar_table").progressbar({ value: 0 });
			 
			 $("#dialog_progress").dialog({
				bgiframe: true, 
				autoOpen: false, 
				modal: false, 
				minHeight: 50, 
				minWidth: 300, 
				closeOnEscape: true, 
				resizable: false,
				position: [850,25]
			});
			 $("#dialog_progress").dialog('open');
			
		});
	</script>
</head>
<body>
<div id="dialog_progress" title="Loading Status" style='display:none; font-size:11px'> 
	<span class="loading">Graph Loading ... <span id="g_ptg"></span></span>
	<div class="progressbar"> <div id="progressbar_graph"><span id="g_ptg"></span></div></div>
	<span class="loading">Table Loading ... <span id="t_ptg"></span></span>
	<div class="progressbar"> <div id="progressbar_table"></div></div>
</div>
<a class="keepAlive" style="display:none">&nbsp;</a>

	<%
	 	List<StudentTO> allStudents = (List<StudentTO>) request.getSession().getAttribute("studentScores");
 		long pageNumber = 0;
 		long totalPages = allStudents.size()*2;
 		
		for(int i=0; i<1; i++) {
	 	for(StudentTO studentTO : allStudents) {
			ScoreTO scoreTO = studentTO.getScoreTO();
			for(ContentTO contentTO : studentTO.getContent()) {
				pageNumber++;
		%>   

<div class="example-content" style="border:0px solid red; width:760px; height:1035px;">
	
	<div class="example-nav">
	
		<div class="graph-content" style="height:600px;">
		
		<!-- Example scripts go here -->
		<div class="studentHeader">
			<div class="headerText">
				<div class="headerText1 ">Student Growth Projection</div>
				<div class="headerText2 devider"><%=studentTO.getStudentName() %></div>
				<div>
					<span class="headerText3">Content:</span> <span class="headerText4"><%=contentTO.getContentName() %></span>&nbsp;&nbsp;
					<span class="headerText3">Grade: </span> <span class="headerText4"><%=studentTO.getGrade() %></span>
				</div>
			</div>
		</div>
		
		<div class="chartContent">
			<%-- div id="chart_<%=contentTO.getContentId()%>_<%=studentTO.getStudentBioId()%><%=i%>" style="height:400px; width:745px;"></div--%>
			<img height="400px" width="750px" src="getImage.htm?contentId=<%=contentTO.getContentId() %>&studentBioId=<%=studentTO.getStudentBioId() %>" name="image" alt="" />
		</div>
		
		<div  style="width:750px">
			<p><%=contentTO.getGraphText() %></P>
		</div>
		
		
		</div>
		<div class="table-content" style="height:410px;">
		
		<div class="borderTop" style="width:750px; padding-bottom:10px"></div>
		
			<table class="studentDetail">
				<tr>
					<td width="30%" class="tableHeader">Student:</td>
					<td colspan="4"><%=studentTO.getStudentName() %></td>
				</tr>
				<tr>
					<td class="tableHeader">Current District:</td>
					<td colspan="4"><%=studentTO.getCurrentDistrict() %></td>
				</tr>
				<tr>
					<td class="tableHeader">Current School:</td>
					<td colspan="4"><%=studentTO.getCurrentSchool() %></td>
				</tr>
			</table>
			
			<script>
			$(document).ready(function(){
				var dataString = "contentId=<%=contentTO.getContentId()%>&studentBioId=<%=studentTO.getStudentBioId()%>&contentName=<%=contentTO.getContentName() %>";
				$.ajax({
			      type: "POST",
			      url: "getTableScore.htm",
			      data: dataString,
			      success: function(data) {
			        //alert('session refreshed !! '+data);
			        $("#table_<%=contentTO.getContentId()%>_<%=studentTO.getStudentBioId()%>").html( data );
			        update_rows();
			      },
				  error: function(data) {
					  
				  }
			    });
			});
			</script>
			<div id="table_<%=contentTO.getContentId()%>_<%=studentTO.getStudentBioId()%>"> 
				<table class="table" cellspacing="0" width="750px">
					<tr class="nobackground">
						<td class="borderDown borderRight textAlignCenter">Grade</td>
						<td class="borderDown borderRight textAlignCenter" colspan="4"><img src="ajax-loader.gif"></img></td>
						<td class="borderDown textAlignCenter" colspan="2">North Dakota State Assessment Information</td>
					</tr>
					<tr class="nobackground">
						<td width="5%" class="borderDown borderRight textAlignRight"></td>
						
						<td width="8%" class="borderDown borderRight textAlignCenter">Student<br/>Score</td>
						<td width="10%" class="borderDown borderRight textAlignCenter">Achievement<br/>Level</td>
						<td width="10%" class="borderDown borderRight textAlignCenter">Achievement<br/>Percentile</td>
						<td width="8%" class="borderDown borderRight textAlignCenter">3-Year<br/>Path</td>
						
						<td width="9%" class="borderDown">Date</td>
						<td width="50%" class="borderDown">School Attended</td>
					</tr>
					<%
					for(int grd=3; grd<12; grd++) {
					%>
					<tr>
						<td class="borderRight textAlignCenter"><%=grd %></td>
						<td class="borderRight textAlignCenter">&nbsp;</td>
						<td class="borderRight textAlignCenter">&nbsp;</td>
						<td class="borderRight textAlignCenter">&nbsp;</td>
						<td class="borderRight textAlignCenter">&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<%} %>
					
				</table>
				
				<div style="width:750px">
					<p><%=contentTO.getTableText() %></p>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="footer example-content">
	<div style="float:right">
		<span class="footerPageNumber">Page <%=pageNumber %> of <%=totalPages %></span>
	</div>
	<div style="float:left">
		<span class="footerDate">North Dakota Department of Public Instruction</span>
	</div>
	<div style="float:left; padding-left:14%">
		<span class="footerDate"><%=UserController.getDate() %></span>
	</div>
</div>
<br/>
<%

		if(studentTO.getContent().size()%2 != 0) {
			// odd number of content present; we need to keep one page blank
			%>
			<div class="example-content" style="border:0px solid #eee; width:750px; height:1042px;"></div>
			<div class="footer example-content" style="padding-bottom:0px">
				<div style="float:right">
					<span class="footerPageNumber">Page <%=++pageNumber %> of <%=totalPages %></span>
				</div>
				<div style="float:left">
					<span class="footerDate">North Dakota Department of Public Instruction</span>
				</div>
				<div style="float:left; padding-left:14%">
					<span class="footerDate"><%=UserController.getDate() %></span>
				</div>
			</div>
			<br/>
			<%
			
		}

		
		} // end content loop
	 	
	} // end student loop 
	}
	%>


	
	<script src="highcharts.js"></script>

	<script>
	
	function loadImage() {
		//$('.jqplot-image-button').each(function(index) {
			$('.jqplot-image-button').click();
		//});
		$('.jqplot-target').hide();
		$('.jqplot-image-button').hide();
		$('.loadImage').hide();
	}
	</script>
	
</body>


</html>