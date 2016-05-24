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
	
  
  
  <script type="text/javascript" src="jquery.min.js"></script>
    
	<style>
		
		.jqplot-series-3 {font-weight:bold}
	</style>
    <script>
	
		function update_rows() {
			$(".table tr:even").css("background-color", "#fff");
			$(".table tr:odd").css("background-color", "#eeece1");
			$(".table tr.nobackground").css("background-color", "#fff");
		}
		$(document).ready(function() {
			 update_rows();
		});
	</script>
</head>
<body>
	<%
		String nodeId = (String) request.getAttribute("nodeId_");
	 	List<StudentTO> allStudents = (List<StudentTO>) request.getSession().getAttribute("webgraphStudentScores_"+nodeId);
 		long pageNumber = 0;
 		long totalPages = allStudents.size()*2;
 		String line1 = "";
 		String line2 = "";
 		String line3 = "";
 		String line4 = "";
 		String line5 = "";
		for(int i=0; i<1; i++) {
	 	for(StudentTO studentTO : allStudents) {
			ScoreTO scoreTO = studentTO.getScoreTO();
			for(ContentTO contentTO : scoreTO.getContent()) {
				for(ContentTO tempCont : studentTO.getContent()) {
					if(tempCont.getContentId() == contentTO.getContentId()) {
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
			<div id="chart_<%=contentTO.getContentId()%>_<%=studentTO.getStudentBioId()%><%=i%>" style="height:400px; width:745px;"></div>
		</div>
		
		<div  style="width:750px">
			<p><%=contentTO.getGraphText() %></P>
		</div>
		
		
		</div>
		<div class="table-content" style="height:410px;">
		
		<div class="borderTop" style="width:750px; padding-bottom:10px"></div>
		<div>
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
			
			
			<table class="table" cellspacing="0" width="750px">
				<tr class="nobackground">
					<td class="borderDown borderRight textAlignCenter">Grade</td>
					<td class="borderDown borderRight textAlignCenter" colspan="4"><%=contentTO.getContentName() %></td>
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
				for(StudentScoresTO studentScoresTO : contentTO.getStudentScores()) {
				%>
				<tr>
					<td class="borderRight textAlignCenter"><%=studentScoresTO.getGradeName() %></td>
					
					<td class="borderRight textAlignCenter"><%=studentScoresTO.getStudentScore() %></td>
					<td class="borderRight textAlignCenter"><%=studentScoresTO.getAchvLevel() %></td>
					<td class="borderRight textAlignCenter"><%=studentScoresTO.getAchvPercentile() %></td>
					<td class="borderRight textAlignCenter"><%=studentScoresTO.getGrowthScore() %></td>
					
					<td><%=studentScoresTO.getDateAttended() %></td>
					<td><%=studentScoresTO.getSchoolAttended() %></td>
				</tr>
				<%} %>
			</table>
			
		</div>
		<div style="width:750px">
			<p><%=contentTO.getTableText() %></p>
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
	<div style="float:left; padding-left:13%">
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
	
		line1 = contentTO.getGradeFixedScore(1);
		line2 = contentTO.getGradeFixedScore(2);
		line3 = contentTO.getGradeFixedScore(3);
		line4 = contentTO.getStudentScore();
		line5 = contentTO.getGrowthScore();
		
		int minScale = 350;
		if(1002 == contentTO.getContentId()) {
			minScale = 450;
		}
		
		%>
		<script class="code" type="text/javascript">
			$(document).ready(function(){
			  var line1 = <%=line3%>;
			  var line2 = <%=line2%>;
			  var line3 = <%=line1%>;
			  var line4 = <%=line4%>;
			  var line5 = <%=line5%>;
			  chart = new Highcharts.Chart({
						chart: {
							renderTo: 'chart_<%=contentTO.getContentId()%>_<%=studentTO.getStudentBioId()%><%=i%>',
							type: 'line',
							plotBackgroundColor:'#fffddd',
							borderColor: '#999999',
							plotBorderColor: '#DDD',
							plotBorderWidth: 1,
							marginRight: 40,
							spacingTop: 0
						},
						credits: {
							enabled: false
						},
						title: {
							text: ' '
						},
						xAxis: {
							title: {
								text: 'Grade Level'
							},
							categories: [0,1,2,3,4,5,6,7,8,9,10,11]
						},
						yAxis: {
							title: {
								text: 'Scale Score'
							},
							min:<%=minScale%>,
							max: 900,
							tickInterval: 50,
							plotLines: [{
								value: 0,
								width: 1,
								color: '#808080'
							}]
						},
						legend: {
							layout: 'vertical',
							align: 'right',
							verticalAlign: 'top',
							x: -50,
							y: 240,
							borderWidth: 1,
							backgroundColor: '#FFF'
						},
						plotOptions: {
							line: {
								dataLabels: {
									enabled: true
								},
								enableMouseTracking: true,
								shadow: false,
								animation: true
							}
						},
						series: [{
							name: 'Advanced Cut Score',
							color: '#9bbb59',
							marker: {symbol: 'diamond'},
							dataLabels: { y: -15},
							data: line1
						}, {
							name: 'Proficient Cut Score',
							color: '#be4c49',
							marker: {symbol: 'circle'},
							data: line2
						}, {
							name: 'Partially Proficient Cut Score',
							color: '#4f81bd',
							marker: {symbol: 'triangle'},
							dataLabels: { y: 20},
							data: line3
						}, {
							name: 'Student Score',
							color: '#8064a2',
							marker: {symbol: 'square'},
							lineWidth: 4,
							dataLabels: { 
								/*backgroundColor: 'rgba(252, 255, 197, 0.7)',
								borderWidth: 1,
								borderColor: '#AAA',*/
								style: {fontWeight: 'bold'},
								y: -10 
							},
							data: line4
							
						}, {
							name: '3-Year Path',
							color: '#46aac5',
							marker: {symbol: 'triangle-down', radius: 6 },
							lineWidth: 4,
							dataLabels: { enabled: false },
							dashStyle: 'longdash',
							data: line5
						}]
					});
				
			});
			</script>
		<%
					break;
					}
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