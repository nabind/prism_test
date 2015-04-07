	<!-- JavaScript at the bottom for fast page loading -->

	<!-- Scripts -->
	<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
	<script>
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
	<script src="scripts/js/setup.js"></script>

	<!-- Template functions -->
	<script src="scripts/js/developr.all.js"></script>
	<!-- <script src="scripts/js/developr.input.js"></script>
	<script src="scripts/js/developr.accordions.js"></script>
	<script src="scripts/js/developr.message.js"></script>
	<script src="scripts/js/developr.modal.js"></script>
	<script src="scripts/js/developr.collapsible.js"></script>
	<script src="scripts/js/developr.notify.js"></script>
	<script src="scripts/js/developr.scroll.js"></script>
	<script src="scripts/js/developr.progress-slider.js"></script>
	<script src="scripts/js/developr.tooltip.js"></script>
	<script src="scripts/js/developr.content-panel.js"></script>
	<script src="scripts/js/developr.tabs.js"></script>		Must be loaded last
	<script src="scripts/js/developr.table.js"></script> -->
	
	<!-- Plugins -->
	<script src="scripts/js/libs/jquery.tablesorter.min.js"></script>
	<script src="scripts/js/libs/DataTables/jquery.dataTables.min.js"></script>
	<script src="scripts/js/libs/jquery-ui.min.js"></script>
	<script src="scripts/js/libs/glDatePicker/glDatePicker.min.js"></script>
	<script src="scripts/js/libs/jsTree/jquery.jstree.js"></script>
	<script src="scripts/js/libs/formValidator/jquery.validationEngine.js"></script>
	<script src="scripts/js/libs/formValidator/languages/jquery.validationEngine-en.js"></script>
	<script src="scripts/js/libs/formValidator/other-validations.js"></script>
	<script src="scripts/js/libs/jquery.smoothDivScroll-1.3.js"></script>
	
	<!-- CKEditor -->
	<script src="scripts/js/libs/ckeditor/ckeditor.js"></script>
	
	<!-- Custom functions -->
	<script src="scripts/js/custom.all.js?v=2"></script>
	<!-- <script src="scripts/js/report/report.js"></script>
	<script src="scripts/js/report/manageReport.js"></script>
	<script src="scripts/js/report/manageMessage.js"></script>
	<script src="scripts/js/admin/manageUser.js"></script>
	<script src="scripts/js/admin/usermodule.js"></script>
	<script src="scripts/js/admin/manageOrganizations.js"></script>
	<script src="scripts/js/admin/manageRole.js"></script>
	<script src="scripts/js/parent/parent.js"></script>
	<script src="scripts/js/parent/manageParent.js"></script>
	<script src="scripts/js/parent/imageSlide.js" type="text/javascript" ></script>
	<script src="scripts/js/parent/manageStudent.js"></script>
	<script src="scripts/js/parent/manageContent.js"></script>
	<script src="scripts/js/inors/inors.js"></script> -->
