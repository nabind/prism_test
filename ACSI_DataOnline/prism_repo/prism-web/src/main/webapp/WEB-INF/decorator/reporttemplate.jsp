		<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
		<!DOCTYPE html>
		<html>
		<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		</head>
		<script src="scripts/js/libs/modernizr.custom.js"></script>
		<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
		<script src="scripts/js/setup.js"></script>
		<style>
			.otherspan {
				vertical-align:middle !important;
				line-height: 11px !important;
			}
			.ie7span {
				vertical-align:top !important;
				line-height: 15px !important;
			}
			.text {
				font-family: Arial;
				font-size: 10px;
				vertical-align: middle;
				float: left;
				margin-top: 3px;
			}
		</style>
		
		
		<script>
			$(document).ready(function() {
				if($.template.ie7) { 
					$('span').addClass('ie7span'); 
				} else {
					$('span').addClass('otherspan'); 
				}
				
				// ============================ PATCH TO DISPLAY IMAGES IN CLASS DASHBOARD (WEB ONLY) ================================	
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
			});
		</script>
		
		
		<body>
		<decorator:body/> 
		</body>
		
		
	</html>     	
		
		
			
			