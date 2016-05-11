		<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
		<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
		<div class="right-column">
				<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
					<ul class="tabs reporttabs">
						<li class="active"><a assessment="${homeReport.assessmentName}" href="#new-tab0" id="new-tab0_new-tab0">${homeReport.reportName}</a></li>
					</ul>
					<div class="tabs-content">
						<div id="new-tab0" class="with-padding relative">
							<%-- <%@ include file="../report/report.jsp"%> --%>
							<div class="report-panel-contenta linen linen-custom">
								<div class="panel-control panel-control-report align-right padding-right"></div>
							</div>
							<div class="report-frame" style="min-height:630px">
								<textarea id="taContent" style="display:none;"></textarea>
								<div id="inorsHome" class="relative with-padding" style="height: auto; text-align: justify">
								</div>
							</div>
						</div>
					</div>
				</div>
		</div>
		<div id="notification" style="display: none;">
					<span class="dismiss"><a title="dismiss this notification">x</a></span>
		</div>	
		<input type="hidden" value="${messageMapSession.lastLogin}" id="lastLogin"/>
	
<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>		
<script>
openHomePage();
function openHomePage() {
	if($('#inorsHome').length) {
		$.ajax({
			type : "GET",
			url : "loadHomePageMsg.do",
			data : 'homeMessage=inors',
			dataType : 'json',
			async : false,
			cache: false,
			success : function(data) {
				$('#taContent').val(data.value);
				$('#inorsHome').html($('#taContent').val());
			},
			error : function(data) {
			if (data.status == "200") {
				$('#taContent').val(data.responseText);
				$('#inorsHome').html($('#taContent').val());
			} else {
				$('#inorsHome').html("<p class='big-message icon-warning red-gradient'>"+strings['msg.err.homePageContent']+"</p>");
			}				
		  }
		});
	}
	return false;
}
var lastLogin = $("#lastLogin").val();
 $("#notification").fadeIn("slow").append(lastLogin);
$(".dismiss").click(function(){
	 $("#notification").delay(1000).fadeOut('slow');
}); 
/*
$(function () {
	  $('#notification').fadeIn('slow', function () {
	    $(this).delay(5000).fadeOut('slow');
	  }).append('your message');
	});*/

</script>
