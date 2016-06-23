		<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
		<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
		<div class="right-column">
				<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
					<ul class="tabs reporttabs">
						<li class="active"><a assessment="${homeReport.assessmentName}" href="#new-tab0" id="new-tab0_new-tab0">${homeReport.reportName}</a></li>
					</ul>
					<div class="tabs-content">
						<div id="new-tab0" class="with-padding relative">
							<%@ include file="../report/report.jsp"%>
						</div>
					</div>
				</div>
		</div>
		<!-- Blocked for the time being, until customer approval -->
		<!-- <div class="notification" id="notification" style="display: none;">
					<span class="dismiss"><a title="dismiss this notification">x</a></span>
		</div>	 -->
		<input type="hidden" value="${lastLogin}" id="lastLogin"/>
		<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>		
		<script>
		var lastLogin = $("#lastLogin").val();
		if(lastLogin.length > 0) {
			$("#notification").fadeIn("slow").append(lastLogin);
			 $(".dismiss").click(function(){
					 $("#notification").delay(1000).fadeOut('slow');
			 });
		}
		 
		
		 /*$(function () {
			  $('#notification').fadeIn('slow', function () {
			    $(this).delay(5000).fadeOut('slow');
			  }).append('your message');
			});*/
		</script>
