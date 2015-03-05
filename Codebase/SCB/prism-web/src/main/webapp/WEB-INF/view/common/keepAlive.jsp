<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
	//============================= USER KEEP ALIVE =============================
	// alert will appear if idle time is xx * 60 seconds. Where xx indicates minute.
	var alertInterval = 25 * 60; // time in sec
	var forceInterval = 4.5 * 60 * 1000; // time in milisec
	var changeInt = '';
	var seconds;
	var forceLogout = '';
	
	resetTimer();
	function keepAliveACSI() {
		$.modal.confirm(strings['msg.keepAlive'], function()
		{
			// user clicked confirm
			$.ajax({
				type: "GET",
				url: "acsiAlert.do",
				data: '',
				cache:false,
				success: function(data) {
					//alert('session refreshed !! '+data);
					if(data == 'Success') {
						resetTimer();
						//changeInt = setTimeout("keepAliveACSI()", alertInterval);
					} else {
						$.modal.alert(strings['script.common.sessionExpired']);
						location.href = 'j_spring_security_logout';
					}
				},
				 error: function(data) {
					$.modal.alert(strings['script.common.sessionExpired']);
					location.href = 'j_spring_security_logout';
				}
			});


		}, function() {
			// user clicked cancel
			location.href = 'j_spring_security_logout';
			
		});
	}
	function resetTimer() {
		// reset timer
		clearTimeout(changeInt);
		clearTimeout(forceLogout);
		// Init
		seconds = 0;
		// Start counter
		changeInt = setTimeout(reserAlert, 1000);
	}
	function reserAlert() {
		++seconds;
		if(seconds > alertInterval) { 
			// show confirm
			keepAliveACSI();
			
			// trigger forcelogout if user is idle
			forceLogout = setTimeout(forcelogout, forceInterval);
		} else {
			// Next count
			changeInt = setTimeout(reserAlert, 1000);
		}
	}
	function forcelogout() {
		location.href = 'j_spring_security_logout';
	}
	/*function onClick( ev ) {
		resetTimer();
	}
	window.onload = function()
	{
		// Listen to the double click event.
		if ( window.addEventListener ) {
			document.body.addEventListener( 'click', onClick, false );
		}
		else if ( window.attachEvent ) {
			document.body.attachEvent( 'onclick', onClick );
		}

	};*/
	
	$(document).ready(function() {
		//$(document).live("click", function(event) {
		$(document).on('click', function(event) {
			resetTimer();
		});
		
		// ----------------------- FETCH REPORT MENU -----------------------
		<sec:authorize ifAnyGranted="ROLE_PARENT">
			//openAdminMenu();
			refreshChildrenList();
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_PARENT">
			if($('.report-frame').length > 0) fetchReportMenu();
			else openAdminMenu();
		</sec:authorize>
	});
	
</script>