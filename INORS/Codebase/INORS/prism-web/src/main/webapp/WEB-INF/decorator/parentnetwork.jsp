<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>

	<html lang="en">
	
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	

	</head>
	
	<body class="clearfix with-menu with-shortcuts">
		<div id="pn-content">
			<decorator:body/> 
		</div>
	</body>
	<script>
		$(document).ready(function() {
			showContent($('#studentOverviewMessage'));
			showContent($('#contentDescription'));
			$("#pn-content").css('min-height', $('#prismMenu').height()+300);
		});
		//=====document.ready End===================================

		//============To show dynamic content in HTML===============
		function showContent($container){
			var taVal = $('#taContent').val();
			$container.html(taVal);
		}

	</script>
</html>
