	<!-- JavaScript at the bottom for fast page loading -->

	<!-- Scripts -->
	<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
	<script src="scripts/js/libs/jquery-ui.min.js"></script>
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
		
		function openWindowNoControl(url) {
			window.open(url, 'Window', 'width=600,height=400,resizable=yes,scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no');
		}

		function openWindowNoControlRRF(url) {
			window.open(url, 'Window', 'width=810,height=705,resizable=yes,scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no');
		}

		function openWindowNoControlWithMessage(url, msg) {
			var answer = confirm(msg)

			if (answer)
				window.open(url, 'Window', 'width=810,height=705,resizable=yes,scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no');
		}
	</script>
	<script src="scripts/js/setup.js"></script>

	<!-- Template functions -->
	<script src="scripts/js/developr.input.js"></script>
	<script src="scripts/js/developr.message.js"></script>
	<script src="scripts/js/developr.modal.js"></script>
	<script src="scripts/js/developr.navigable.js"></script>
	<script src="scripts/js/developr.notify.js"></script>
	<script src="scripts/js/developr.scroll.js"></script>
	<script src="scripts/js/developr.progress-slider.js"></script>
	<script src="scripts/js/developr.tooltip.js"></script>
	<script src="scripts/js/developr.content-panel.js"></script>

	
	<!-- Custom functions -->
	<script src="scripts/js/report/drilldownReport.js"></script>
	
