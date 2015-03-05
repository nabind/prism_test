		<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
		<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
		
		<!DOCTYPE html>
		<html>
		<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		</head>
		<script src="scripts/js/libs/modernizr.custom.js"></script>
		<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
		<script src="scripts/js/setup.js"></script>
		<!-- <script src="scripts/js/highchart/highcharts-2.3.2.src.js"></script> -->
		<script src="scripts/js/highchart/highcharts-3.0.2.js"></script>
		<script src="scripts/js/highchart/default.service.js"></script>
		<script src="scripts/js/highchart/item.hyperlink.service.js"></script>
		
		<link rel="stylesheet" href="<spring:theme code="reset.css" />">
		<link rel="stylesheet" href="<spring:theme code="style.css" />">
		<link rel="stylesheet" href="<spring:theme code="colors.css" />">
		<link rel="stylesheet" href="<spring:theme code="form.css" />">
		<link rel="stylesheet" href="<spring:theme code="table.css" />">
		<link rel="stylesheet" href="<spring:theme code="datatable.css" />">
		<link rel="stylesheet" href="<spring:theme code="modal.css" />">
		<link rel="stylesheet" href="themes/acsi/css/datatableConverter.css" />
		
		<script src="scripts/js/libs/jquery-ui.min.js"></script>
		<script src="scripts/js/developr.input.js"></script>
		<script src="scripts/js/developr.modal.js"></script>
		<script src="scripts/js/libs/DataTables/jquery.dataTables.min.js"></script>
		<script src="scripts/js/libs/DataTables/jquery.dataTables.columnFilter.js"></script>
		<script src="scripts/js/libs/colResizable-1.3.min.js"></script>
		<script src="scripts/js/report/datatableConverter.js"></script>
		<style>
			.otherspan {
				vertical-align:middle !important;
				line-height: 11px !important;
			}
			.ie7span {
				vertical-align:top !important;
				line-height: 23px !important;
			}
			.text {
				font-family: Arial;
				font-size: 10px;
				vertical-align: middle;
				float: left;
				margin-top: 3px;
			}
			a {
			    text-decoration: none;
			}
			.CRZ td, .CRZ th {
				padding-left: 9px !important;
				padding-right: 9px !important;
				overflow: initial;
			}
		</style>
		
		
		<script>
			$(document).ready(function() {
				if(!$.template.ie8) {
					$(".rotated").css('filter', '');
					//$(".rotated").css('-ms-transform', 'rotate(-90deg)');
					//$(".rotated").css('left', '-150px');
					//$(".rotated").css('top', '200px');
				}

				/* if($.template.ie7) { 
					$('span').addClass('ie7span'); 
				} else {
					$('span').addClass('otherspan'); 
				} */
				
				// ============================ PATCH TO DISPLAY IMAGES IN CLASS DASHBOARD (WEB ONLY) ================================	
				/*
				var legendHigh = $('span:contains("High Mastery")');
				var legendMid = $('span:contains("Moderate Mastery")');
				var legendLow = $('span:contains("Low Mastery")');
				if(legendHigh.length > 0 && legendMid.length > 0 && legendLow.length > 0) {
					$(legendHigh).html('HMASTERY');
					$(legendMid).html('MMASTERY');
					$(legendLow).html('LMASTERY');
					
					var foundHigh = $('span:contains("High")');
					$(foundHigh).html('<img src="themes/acsi/img/Green_new.jpg" style="margin-top: 5px;" height="20px" name="High" />');
					var foundMid = $('span:contains("Moderate")');
					$(foundMid).html('<img src="themes/acsi/img/Yellow_new.jpg" style="margin-top: 5px;" height="20px" name="Moderate" />');
					var foundLow = $('span:contains("Low")');
					$(foundLow).html('<img src="themes/acsi/img/Red_new.jpg" style="margin-top: 5px;" height="20px" name="Low" />');
					
					var textHigh = $('span:contains("HMASTERY")');
					var textMid = $('span:contains("MMASTERY")');
					var textLow = $('span:contains("LMASTERY")');
					
					$(textHigh).html('<img src="themes/acsi/img/Green.jpg" style="margin-top: -3px;float:left" height="20px" name="High" /> <span class="text">High Mastery</span>');
					$(textMid).html('<img src="themes/acsi/img/Yellow.jpg" style="margin-top: -3px;float:left" height="20px" name="Moderate" /> <span class="text">Moderate Mastery</span>');
					$(textLow).html('<img src="themes/acsi/img/Red.jpg" style="margin-top: -3px;float:left" height="20px" name="Low" /> <span class="text">Low Mastery</span>');
				}
				*/
			});
		</script>
		
		
		<body>
			<decorator:body/> 
		
			<div id="dialog-modal" title="Select Chart Type" class="display-none">
				<div id = "columnChartType" class ="chartType">
					<div id = "columnTitle" class ="title">Column</div>
					<div id = "column_1" class ="icon iconColumn1 basic default" ></div>
					<div id = "column_2" class ="icon iconColumn2 normal default" ></div>
					<div id = "column_3" class ="icon iconColumn3 percent default" ></div>
				</div>
				<div id = "barChartType" class ="chartType">
					<div id = "barTitle" class ="title">Bar</div>
					<div id = "bar_1" class ="icon iconBar1 basic inverted"></div>
					<div id = "bar_2" class ="icon iconBar2 normal inverted"></div>
					<div id = "bar_3" class ="icon iconBar3 percent inverted" ></div>
				</div>
				<div id = "lineChartType" class ="chartType">
					<div id = "lineTitle" class ="title">Line</div>
					<div id = "line" class ="icon iconLine1 basic default" ></div>
					<div id = "spline" class ="icon iconLine2 basic default" ></div>
				</div>
				<div id = "areaChartType" class ="chartType">
					<div id = "areaTitle" class ="title">Area</div>
					<div id = "area_1" class ="icon iconArea1 basic default" ></div>
					<div id = "area_2" class ="icon iconArea2 normal default" ></div>
					<div id = "area_3" class ="icon iconArea3 percent default" ></div>
					<div id = "areaspline_4" class ="icon iconArea4 basic default" ></div>
				</div>
				<div id = "pieChartType" class ="chartType">
					<div id = "pieTitle" class ="title">Pie</div>
					<div id = "pie" class ="icon iconPie1 basic inverted" ></div>
				</div>
			</div>
		</body>
		
		
	</html>     	
		
		
			
			