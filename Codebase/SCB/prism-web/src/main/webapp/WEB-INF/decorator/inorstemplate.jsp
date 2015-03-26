	<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<!DOCTYPE html>
	<html>
		<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<%@ include file="messages.jsp"%>
		</head>
		
		<script src="scripts/js/libs/modernizr.custom.js"></script>
		
		<link rel="stylesheet" href="<spring:theme code="reset.css" />">
		<link rel="stylesheet" href="<spring:theme code="style.css" />">
		<link rel="stylesheet" href="<spring:theme code="colors.css" />">
		<link rel="stylesheet" href="<spring:theme code="form.css" />">
		<link rel="stylesheet" href="<spring:theme code="table.css" />">
		<link rel="stylesheet" href="<spring:theme code="datatable.css" />">
		<link rel="stylesheet" href="<spring:theme code="modal.css" />">
		<link rel="stylesheet" href="scripts/js/libs/theme/jquery-ui-redmond.css">
		<link rel="stylesheet" href="themes/acsi/css/overrideinors.css">
		
		<!-- Form Validation -->
		<link rel="stylesheet" href="<spring:theme code="validationEngine.css" />">		
		
		<style>
			
		</style>
		
		
		<body>
			<decorator:body/> 
		</body>
		
		<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
		<script src="scripts/js/setup.js"></script>
		
		<script>
		// As we don't need horizontal scrolling for custom jsp 
		$(document).ready(function() {
			$(".scrollingHotSpotRight", window.parent.document).hide();
			$(".scrollingHotSpotLeft", window.parent.document).hide();
		});
		// ========================== CODE FOR MODAL PROGRESSBAR ==========================
	    function blockUI(obj) {
			if ($("#blockDiv").length > 0) {
				$("#blockDiv").show();
				$("#blockDiv").css("cursor", "wait");
				return;
			}
			if ($("#blockDiv"+obj).length > 0) {
				$("#blockDiv"+obj).show();
				$("#blockDiv"+obj).css("cursor", "wait");
				return;
			}
			if(obj == '' || obj == null) {
				$("body").append('<div id="blockDiv" class="blockDiv loader huge" style="position:fixed;top:0;left:0;width:100%;height:100%;z-index:999999"></div>');
				$("#blockDiv").css("cursor", "wait");
			} else {
				$("#"+obj).append('<div id="blockDiv'+obj+'" class="blockDiv loader huge" style="position:absolute;top:0;left:0;width:100%;height:100%;z-index:999999"></div>');
				$("#blockDiv"+obj).css("cursor", "wait");
			}
		}
		// -- show progressbar while the page is loading
	    blockUI();
		// code for removing progressbar
		function unblockUI(obj) {
			if(obj == '' || obj == null) {
				$("#blockDiv").css("cursor", "normal");
				$("#blockDiv").remove();
			} else {
				$("#blockDiv"+obj).css("cursor", "normal");
				$("#blockDiv"+obj).remove();
			}
		}
	</script>
		<script src="scripts/js/libs/jquery.tablesorter.min.js"></script>
		<script src="scripts/js/libs/DataTables/jquery.dataTables.min.js"></script>
		<script src="scripts/js/libs/jquery-ui.min.js"></script>
		<script src="scripts/js/developr.input.js"></script>
		<script src="scripts/js/developr.modal.js"></script>
		<script src="scripts/js/developr.tooltip.js"></script>
		<script src="scripts/js/libs/jsTree/jquery.jstree.js"></script>
		<script src="scripts/js/libs/formValidator/jquery.validationEngine.js"></script>
		<script src="scripts/js/libs/formValidator/languages/jquery.validationEngine-en.js"></script>
		<script src="scripts/js/libs/formValidator/other-validations.js"></script>
	
		<script src="scripts/js/inors/inors.js"></script>
		<script src="scripts/js/report/rescoreRequest.js"></script>
		
	</html>     	
			