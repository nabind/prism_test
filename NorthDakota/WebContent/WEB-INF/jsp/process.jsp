<!DOCTYPE html>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgTO" %>
<%@page import="com.vaannila.TO.ScoreTO" %>
<%@page import="com.vaannila.TO.ContentTO" %>
<%@page import="com.vaannila.TO.StudentScoresTO" %>
<%@page import="java.util.ArrayList, java.util.List" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<html>
<head>
	
	<title>All Schools</title>

    <link rel="stylesheet" type="text/css" href="examples.css" />
	<link rel="stylesheet" type="text/css" href="colors.css" />
  <link rel="stylesheet" href="themes/blue/style.css" type="text/css" media="print, projection, screen" />
  <link rel="stylesheet" href="themes/base/jquery.ui.all.css"> 
  <script type="text/javascript" src="jquery.min.js"></script>
  <script type="text/javascript" src="ui/jquery-ui-1.8.17.custom.js"></script>
  <script type="text/javascript" src="jquery.tablesorter.js"></script>
  
	<style>
		.progressbar {width: 50px; height: 15px;}
		.ui-progressbar {height: 15px;}
		.ui-dialog-title {font-size:12px}
		.ui-progressbar-value { background: #00CD00 url(progressbar.gif) 50% 50% repeat-x; font-size: 11px; padding: 1px 1px 1px 5px;  }
		.loading{font-weight:bold; padding-bottom:5px}
		.jqplot-series-3 {font-weight:bold}
		.tablesorter{width: 930px !important; }
		.tableContainer{ padding: 20px }
		div#main h1 {
			border-bottom: 3px solid #CDCDCD;
			display: block;
			padding: 30px 0 2px;
			margin-top:10px;
			width: 930px;
			align:right;
			font-size: larger;
		}
		.ctblogo{
			float: right;
			height: 93px;
			position: absolute;
			width: 352px;
		}
		body {
			background: url('backgrounds.jpg') 60% -20% repeat;
		}
		.container {
			width: 982px;
			margin: 0 auto;
			padding: 0;
			text-align: left;			
		}
		.content {
			width: 982px;
			margin: 0;
			padding: 0;
			background: url(page_bg_mid.png) top left repeat-y;
			clear: both;
		}
		#progressbar_allRoster {
			width: 50px;
		}
		#progressbar_all {
			width: 50px;
		}
		.ui-button-text{
		font-size: 12px !important;
		}
		.ui-dialog .ui-dialog-buttonpane {
			    margin: 0px;
			    padding: 0px;
		}
		#dialogProjection  #dialogRoster { font-size: 15px !important; }
		.ui-dialog .ui-dialog-content {
			background: none repeat scroll 0 0 transparent;
			border: 0 none;
			overflow: auto;
			position: relative;
			font-size: 0.7em;
		}
	</style>
    
</head>
<body>
<div class="container">
	<div class="content">
	
 	<%
 	StringBuffer nodeIdList = new StringBuffer();
 	StringBuffer ungeneratedNodeIdList = new StringBuffer();
 	StringBuffer ungenRosterPdfNodeIdList = new StringBuffer();
 	List<OrgTO> allNodes = (List<OrgTO>) request.getAttribute("allNodes");
 	List<OrgTO> allUngeneratedNodes = (List<OrgTO>) request.getAttribute("allUngeneratedNodes");
 	List<OrgTO> allUngeneratedRosterPdfNodes = (List<OrgTO>) request.getAttribute("allUngeneratedRosterPdfNodes");
 	AdminTO admin = (AdminTO)request.getAttribute("admin");	
 			
		%>  
		<%
			if (allUngeneratedNodes!= null){
				int count1 = 0;
				for(OrgTO node : allUngeneratedNodes) {
					if(count1 > 0) ungeneratedNodeIdList.append(",");
					count1++;
					ungeneratedNodeIdList.append(node.getNodeId());
					}
			}
				
			  if (allUngeneratedRosterPdfNodes!= null){
			  int count2 = 0;
				for(OrgTO node : allUngeneratedRosterPdfNodes) {
					if(count2 > 0) ungenRosterPdfNodeIdList.append(",");
					count2++;
					ungenRosterPdfNodeIdList.append(node.getNodeId());
					}
			  }
				
				%> 
			
			<div id="main" class="tableContainer">
			<div>
				<div class="ctblogo" align="left"><img src="logoCTB.png"></img></div>
				<div align="center"><font size="5" color="#BDBDBD">Report PDF Generator</font></div>
			</div>
		    <div>
		    	<h1>North Dakota Schools <%=admin.getAdminName() %> Growth Reports
		    		<span style="padding-left: 230px;">Connected with: <%= request.getAttribute("dataSource")%></span>
		    	</h1>      
			</div>				
			<!-- <div>
					<span><a href="#nogo" onClick="mergePdfs('false')">Merge all <b>Projection</b> Growth Report PDFs</a> | </span>
					<span><a href="#nogo" onClick="mergePdfs('true')">Merge all <b>Roster</b> Growth Report PDFs</a></span>
					
					<span><span id="mergeStatus"></span> </span>
					<span id="mergeMessage" style="padding-top:10px">&nbsp;</span> 
				</div> -->
				<table id="growth" class="tablesorter">
					<thead>
					<tr>
						<th nowrap="nowrap" >Node Id&nbsp;</th>
						<th >District Id-School Id </th>
						<th>School Name</th>
						<th >Roster PDF <br/> <a href="#nogo" id="startAllPdfRoster">Generate ALL</a></th>
						<th><div id="progressbar_allRoster"></div></th>
						<th>Projection PDF  <br/> <a href="#nogo" id="startAllPdf">Generate ALL</a></th>
						<th><div id="progressbar_all"></div></th>
					</tr>
					</thead>
					<tbody>
					<%
					int count = 0;
					for(OrgTO node : allNodes) {
						if(count > 0) nodeIdList.append(",");
						count++;
						nodeIdList.append(node.getNodeId());
					
					%>
					<tr>
						<%--td><a target="_blank" href="welcome.htm?nodeId=<%=node.getNodeId() %>"><%=node.getNodeId() %></a></td--%>
						<td><%=node.getNodeId() %></td>
						<td><%=node.getDistrictNumber() %>-<%=node.getSchoolNumber() %></td>
						<td><%=node.getSchoolName() %></td>
						<%-- td><a target="_blank" href="webgraph.htm?nodeId=<%=node.getNodeId() %>">Interactive</a></td>  --%>
						
						<td>
							<a href="#nogo" id="rosterpdf_<%=node.getNodeId()%>" >
								<%if(node.getPdfFileNameRoster() != null && node.getPdfFileNameRoster().trim().length() > 0) { %>
								<span id="rosterPdfGen_<%=node.getNodeId()%>" class="generateAllPdfRoster">Regenerate PDF</span>
								<%} else { %>
								<span id="rosterPdfGen_<%=node.getNodeId()%>" class="generateAllPdfRoster">Generate PDF</span>
							<%} %>
							</a>
						</td>
						<td><span id="rosterPdfLink_<%=node.getNodeId()%>">
							<%if(node.getPdfFileNameRoster() != null && node.getPdfFileNameRoster().trim().length() > 0) { %>
								<a target="_blank" href="viewSchoolPdf.htm?nodeId=<%=node.getNodeId()%>&roster=true">View</a>
							<%} %>
						</span></td>
						
						<td>
							<a href="#nogo" id="pdf_<%=node.getNodeId()%>" >
							<%if(node.getPdfFileName() != null && node.getPdfFileName().trim().length() > 0) { %>
								<span id="pdfLink_<%=node.getNodeId()%>" class="generateAllPdf">Regenerate PDF</span>
							<%} else { %>
								<span id="pdfLink_<%=node.getNodeId()%>" class="generateAllPdf">Generate PDF</span>
							<%} %>
							</a>
							
							<script>
								$(document).ready(function() {
									 var growthFileName = "<%=node.getSchoolFullName()%>_<%=node.getNodeId()%>_P";
									 var dataString = "nodeId=<%=node.getNodeId()%>&growthFileName="+growthFileName;
									 $("#pdf_<%=node.getNodeId()%>").click(function() {
										 if(triggerCount > 1) {
											 alert('Please allow to complete other PDF generation');
											 return false;
										 }
										 triggerCount = triggerCount + 1;
										 $("#pdfLink_<%=node.getNodeId()%>").html('<img src="ajax-loader.gif"></img>');
										 $("#container_<%=node.getNodeId()%>").html('<div id="progressbar_<%=node.getNodeId()%>"></div>');
										 $("#progressbar_<%=node.getNodeId()%>").progressbar({ value: 0 });
							    		  setTimeout("getProgressStatus(<%=node.getNodeId()%>)", 100);
										  $.ajax({
										      type: "POST",
										      url: "getGrowthReport.htm",
										      data: dataString,
										      //beforeSend:alert("HI......"+dataString),
										      success: function(data) {
										    	  if(data.status == 'Success') {
										    		  if(data.message == 'null' || data.message == '') {
										    		  	$("#container_<%=node.getNodeId()%>").html('<a target="_blank" href="viewSchoolPdf.htm?nodeId=<%=node.getNodeId()%>">View</a>');
										    		  } else {
										    			  $("#container_<%=node.getNodeId()%>").html( data.message );
										    		  }
										    	  } else {
										    		  $("#container_<%=node.getNodeId()%>").html('Error');
										    	  }
										    	  $("#pdfLink_<%=node.getNodeId()%>").html('Regenerate PDF');
										    	  triggerCount = triggerCount - 1;
										      },
											  error: function(data) {
												  $("#container_<%=node.getNodeId()%>").html('Error');
												  $("#pdfLink_<%=node.getNodeId()%>").html('Regenerate PDF');
												  triggerCount = triggerCount - 1;
											  }
									    });
									    return false;
									 });
									 
									 // get roster pdf
									 var rosterFileName = "<%=node.getSchoolFullName()%>_<%=node.getNodeId()%>_R";
									 var rosterDataString = "nodeId=<%=node.getNodeId()%>&rosterFileName="+rosterFileName;
									 $("#rosterpdf_<%=node.getNodeId()%>").click(function() {
										 if(triggerCountRoster > 2) {
											 alert('Please allow to complete other PDF generation');
											 return false;
										 }
										 $("#rosterPdfLink_<%=node.getNodeId()%>").html('<img src="ajax-loader.gif"></img>');
										 $("#rosterPdfGen_<%=node.getNodeId()%>").html('Please wait ...');
										 triggerCountRoster = triggerCountRoster + 1;
										  $.ajax({
										      type: "POST",
										      url: "getRosterReport.htm",
										      data: rosterDataString,
										      success: function(data) {
										    	  if(data.status == 'Success') {
										    		  if(data.message == 'null' || data.message == '') {
										    		  	  $("#rosterPdfLink_<%=node.getNodeId()%>").html('<a target="_blank" href="viewSchoolPdf.htm?nodeId=<%=node.getNodeId()%>&roster=true">View</a>');
										    		  } else {
										    			  $("#rosterPdfLink_<%=node.getNodeId()%>").html( data.message );
										    		  }
										    	  } else {
										    		  $("#rosterPdfLink_<%=node.getNodeId()%>").html( data.message );
										    	  }
										    	  $("#rosterPdfGen_<%=node.getNodeId()%>").html('Regenerate');
										    	  triggerCountRoster = triggerCountRoster - 1;
										      },
											  error: function(data) {
												  $("#rosterPdfLink_<%=node.getNodeId()%>").html('Error');
												  $("#rosterPdfGen_<%=node.getNodeId()%>").html('Regenerate');
												  triggerCountRoster = triggerCountRoster - 1;
											  }
									    });
									    return false;
									 });
								});
							</script>
						</td>
						<td><div class="progressbar" id="container_<%=node.getNodeId()%>"> 
							<div id="progressbar_<%=node.getNodeId()%>">
							<%if(node.getPdfFileName() != null && node.getPdfFileName().trim().length() > 0) { %>
								<a target="_blank" href="viewSchoolPdf.htm?nodeId=<%=node.getNodeId()%>">View</a>
							<%} %>
							</div>
						</div></td>
					</tr>
					<%} %>
					</tbody>
				</table>
				<input type="hidden" id="allNodeList" value=<%=nodeIdList.toString() %>>
				<input type="hidden" id="allUngeneratedNodeList" value=<%=ungeneratedNodeIdList.toString() %>>
				<input type="hidden" id="allUngeneratedRosterNodeList" value=<%=ungenRosterPdfNodeIdList.toString() %>>
				<input type="hidden" id="errorCount" value=<%=request.getAttribute("errorCount") %>>
				<input type="hidden" id="alreadyGeneratedCount" value=<%=request.getAttribute("alreadyGeneratedCount") %>>
				<input type="hidden" id="allCount" value=<%=request.getAttribute("allCount") %>>
				<input type="hidden" id="errorCountRosterPdf" value=<%=request.getAttribute("errorCountRosterPdf") %>>
				<input type="hidden" id="alreadyGeneratedCountRosterPdf" value=<%=request.getAttribute("alreadyGeneratedCountRosterPdf") %>>
				<input type="hidden" id="allCountRosterPdf" value=<%=request.getAttribute("allCountRosterPdf") %>>
			</div>
			<br/>
		<div id="dialogProjection" style="min-height: 54px; height: 54px; width: 841px"></div>
		<div id="dialogRoster" style="min-height: 54px; height: 54px; width: 841px"></div>
<script>

// dialog for Rrojection report pdf status check
$( "#dialogProjection" ).dialog({
	 title:'Projection PDF',
	 autoOpen: false,
     modal: true,
     position: 'center',
     draggable: false,
     width: "841px",
     //height: "54px",
     resizable: false,
     autoResize: false,
     buttons: [{
    	 	id: 'btnRemainingProjection',
			text: 'Generate Remaining Projection PDFs',
			'class': 'blue-gradient glossy',
    	 	click: function() { 
		    	 		$("#progressbar_all").progressbar({ value: 0 });
						triggerLeftOutPdf();
						updatePdfProgress(false);
						//$(".generateAllPdf").html('Pending ...');
						$(this).dialog("close"); 
    	 			}},
    	 	{
    	 	id: 'btnAllProjection',
    	 	text: 'Generate All Projection PDFs',
			'class': 'blue-gradient glossy',
    	 	click: function() { 
    	 		$("#progressbar_all").progressbar({ value: 0 });
				triggerPdf();
				updatePdfProgress(true);
				$(".generateAllPdf").html('Pending ...');
    	 			$(this).dialog("close"); 
    	 			}  	 	
    	 	},
    	 	{
    	 	id: 'btnCloseProjection',
    	 	text: 'Close',
			'class': 'red-gradient glossy',
    	 	click: function() { 
    	 		$(this).dialog("close"); 
    	 			}  	 	
    	 	}]
    	 
    	});

//dialog for Roster report pdf status check
$( "#dialogRoster" ).dialog({
	 title:'Roster PDF',
	 autoOpen: false,
    modal: true,
    position: 'center',
    draggable: false,
    width: "841px",
    //height: "54px",
    resizable: false,
    autoResize: false,
    buttons: [{
   	 	id: 'btnRemainingRoster',
			text: 'Generate Remaining Roster PDFs',
			'class': 'blue-gradient glossy',
   	 	click: function() { 
   	 					$("#progressbar_allRoster").progressbar({ value: 0 });
		    	 		triggerLeftOutPdfRoster();
		    	 		updatePdfProgressRoster(false);
						//$(".generateAllPdf").html('Pending ...');
						$(this).dialog("close"); 
   	 			}},
   	 	{
   	 	id: 'btnAllRoster',
   	 	text: 'Generate All Roster PDFs',
			'class': 'blue-gradient glossy',
   	 	click: function() { 
		   	 	$("#progressbar_allRoster").progressbar({ value: 0 });
				triggerPdfRoster();
				updatePdfProgressRoster(true);
				$(".generateAllPdfRoster").html('Pending ...');
   	 			$(this).dialog("close"); 
   	 			}  	 	
   	 	},
   	 	{
   	 	id: 'btnCloseRoster',
   	 	text: 'Close',
			'class': 'red-gradient glossy',
   	 	click: function() { 
   	 		$(this).dialog("close"); 
   	 			}  	 	
   	 	}]
   	 
   	});

		


	function getProgressStatus(nodeId) {
		var alertInterval = 30000;
		var dataString = "nodeId="+nodeId;
		var status = $("#container_"+nodeId+"").html();
		if(status != 'Error') {
			$.ajax({
			      type: "POST",
			      url: "getPdfStatus.htm",
			      data: dataString,
			      success: function(data) {
			    	  $( "#progressbar_"+nodeId+"" ).progressbar( "option", "value", data.progress );
			    	  $( "#progressbar_"+nodeId+"" ).find(".ui-progressbar-value").text(data.progress + '%');
			    	  if(data.progress < 100) {
			    		  setTimeout("getProgressStatus("+nodeId+")", alertInterval);
			    	  } else {
						  $( "#progressbar_"+nodeId+"" ).find(".ui-progressbar-value").text('Done');
					  }
			      },
				  error: function(data) {
					  setTimeout("getProgressStatus("+nodeId+")", alertInterval);
				  }
		    });
		}
	    return false;
	}
	
	function mergePdfs(isRoster) {
		var dataString = "roster="+isRoster;
		$("#mergeStatus").html(' | <img src="ajax-loader.gif"></img>');
		$.ajax({
		      type: "POST",
		      url: "mergePdfs.htm",
		      data: dataString,
		      success: function(data) {
		    	  if(data.status == 'Success') {
		    		  if(isRoster == 'true') {
		    			  $("#mergeStatus").html(' | <a target="_blank" href="viewSchoolPdf.htm?mergedFile=true&roster=true">View Roster</a>');
		    		  } else {
		    			  $("#mergeStatus").html(' | <a target="_blank" href="viewSchoolPdf.htm?mergedFile=true">View Projection</a>');		    			  
		    		  }
		    	  	$("#mergeMessage").html( '<div style="border:1px solid red; height:100px;width:948px; overflow-x:scroll">'+data.message+'</div>' );
		    	  	
		    	  } else {
		    		  $("#mergeStatus").html(' | Error merging PDF');  
		    	  }
		      },
			  error: function(data) {
				  $("#mergeStatus").html(' | Error merging PDF');
			  } 
	    });
		
	    return false;
	}
	
	var nodes;
	var nodeArr;
	var triggerCount = 0;
	var pdfFireCount = 0;
	var triggerInterval = 5000;
	function triggerPdf() {
		nodes = $("#allNodeList").val();
		if(nodes != null) {
			nodeArr = nodes.split(',');
			setTimeout("clickNext()", triggerInterval);
		}
	}
	function triggerLeftOutPdf() {
		//pdfFireCount=$("#alreadyGeneratedCount").val();
		//updatePdfProgress();
		nodes = $("#allUngeneratedNodeList").val();
		if(nodes != null) {
			nodeArr = nodes.split(',');
			for (var i=0 ; i<nodeArr.length; i++){
				$("#pdfLink_"+nodeArr[i]).html('Pending ...');
			}
			setTimeout("clickNext()", triggerInterval);
		}
	}
	var pdfprogress = 0;
	
	function updatePdfProgress(allPDFs) {
		if (allPDFs)
		pdfprogress = pdfFireCount/nodeArr.length*100;
		else
		pdfprogress = (parseInt(pdfFireCount)+parseInt($("#alreadyGeneratedCount").val()))/$("#allCount").val()*100;
		
		$( "#progressbar_all" ).progressbar( "option", "value", pdfprogress );
  	    $( "#progressbar_all" ).find(".ui-progressbar-value").text(Math.round(pdfprogress) + '%');
  	if (allPDFs)
  	    if(pdfprogress < 100) setTimeout("updatePdfProgress(true)", triggerInterval);
  	 else
  		if(pdfprogress < 100) setTimeout("updatePdfProgress(false)", triggerInterval);
  	    
  	  	//$(".generateAllPdf").html('Pending ...');
	}
	function clickNext() {
		if(triggerCount < 1) {
			if (nodeArr.length > pdfFireCount) { 
				$( "#pdf_"+nodeArr[pdfFireCount]+"" ).click();
				pdfFireCount = pdfFireCount + 1;
				setTimeout("clickNext()", triggerInterval);
			}
		} else {
			setTimeout("clickNext()", triggerInterval);
		}
	}
	
	// roster
	var pdfprogressRoster = 0;
	var pdfFireCountRoster = 0;
	var triggerCountRoster = 0;
	function updatePdfProgressRoster(allRosterPdfs) {
		if (allRosterPdfs)
		pdfprogressRoster = pdfFireCountRoster/nodeArr.length*100;
		else
		pdfprogressRoster = (parseInt(pdfFireCountRoster)+parseInt($("#alreadyGeneratedCountRosterPdf").val()))/$("#allCountRosterPdf").val()*100;	
		$( "#progressbar_allRoster" ).progressbar( "option", "value", pdfprogressRoster );
  	    $( "#progressbar_allRoster" ).find(".ui-progressbar-value").text(Math.round(pdfprogressRoster) + '%');
  	    
  	    if (allRosterPdfs)
  	      if(pdfprogressRoster < 100) setTimeout("updatePdfProgressRoster(true)", triggerInterval);
  	    else
  		  if(pdfprogressRoster < 100) setTimeout("updatePdfProgressRoster(false)", triggerInterval); 
  	  	
  	  //$(".generateAllPdfRoster").html('Pending ...');
	}
	function triggerPdfRoster() {
		nodes = $("#allNodeList").val();
		if(nodes != null) {
			nodeArr = nodes.split(',');
			setTimeout("clickNextRoster()", triggerInterval);
		}
	}
	
	function triggerLeftOutPdfRoster() {
		nodes = $("#allUngeneratedRosterNodeList").val();
		if(nodes != null) {
			nodeArr = nodes.split(',');
			for (var i=0 ; i<nodeArr.length; i++){
				$("#rosterPdfGen_"+nodeArr[i]).html('Pending ...');
			}
			setTimeout("clickNextRoster()", triggerInterval);
		}
	}
	
	function clickNextRoster() {
		if(triggerCountRoster < 3) {
			if (nodeArr.length > pdfFireCountRoster) { 
				$( "#rosterpdf_"+nodeArr[pdfFireCountRoster]+"" ).click();
				pdfFireCountRoster = pdfFireCountRoster + 1;
				setTimeout("clickNextRoster()", triggerInterval);
			}
		} else {
			setTimeout("clickNextRoster()", triggerInterval);
		}
	}
	// end roster
	
	$(document).ready(function() {
		
		$("#startAllPdf").click(function() {
			var generateAllTxt='Click <b>Generate All Projection PDFs</b> to generate all the Projection PDFs.';
			if (($("#errorCount").val()>0)&& ($("#errorCount").val()<$("#allCount").val())) {
				var leftOutTxt='There are '+$("#errorCount").val()+' Pdfs that were not generated during last run. Click <b>Generate Remaining Projection PDFs</b> to generate them.';
					$( "#dialogProjection" ).html( leftOutTxt +"<br/><br/>"+generateAllTxt);
					$( "#btnRemaining" ).show();
					$( "#dialogProjection" ).dialog( "open" );			
				}				
			else if($("#alreadyGeneratedCount").val()==$("#allCount").val()) {
				$( "#dialogProjection" ).html('Click <b>Generate All Projection Pdfs</b> to regenerate all the Projection PDFs.');
				$( "#btnRemainingProjection" ).hide();
				$( "#dialogProjection" ).dialog( "open" );
			
				}
				else {
				$( "#dialogProjection" ).html(generateAllTxt);
				$( "#btnRemaining" ).hide();
				$( "#dialogProjection" ).dialog( "open" );				
				}
			
		});
		
		$("#startAllPdfRoster").click(function() {
			var generateAllTxt='Click <b>Generate All Roster PDFs</b> to generate all the Roster PDFs.';
			if (($("#errorCountRosterPdf").val()>0)&& ($("#errorCountRosterPdf").val()<$("#allCountRosterPdf").val())) {
				var leftOutTxt='There are '+$("#errorCountRosterPdf").val()+' Pdfs that were not generated during last run. Click <b>Generate Remaining Roster PDFs</b> to generate them.';
					$( "#dialogRoster" ).html( leftOutTxt +"<br/><br/>"+generateAllTxt);
					$( "#btnRemainingRoster" ).show();
					$( "#dialogRoster" ).dialog( "open" );			
				}				
			else if($("#alreadyGeneratedCountRosterPdf").val()==$("#allCountRosterPdf").val()) {
				$( "#dialogRoster" ).html('Click <b>Generate All Roster Pdfs</b> to regenerate all the Roster PDFs.');
				$( "#btnRemainingRoster" ).hide();
				$( "#dialogRoster" ).dialog( "open" );
				
				}
			else  {
				$( "#dialogRoster" ).html(generateAllTxt);
				$( "#btnRemainingRoster" ).hide();
				$( "#dialogRoster" ).dialog( "open" );				
				}
			
		});
		
		$("table")
		.tablesorter({
			widthFixed: true, 
			widgets: ['zebra'],
			headers: {
				3: { sorter: false },
				4: { sorter: false },
				5: { sorter: false },
				6: { sorter: false },
				7: { sorter: false }
			}
		});
	

	});
</script>
    </div>
</div>
</body>


</html>