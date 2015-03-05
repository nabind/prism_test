/**
 * This js file is to manage report module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
$(document).ready(function() {
	// ============================= Add custom scroll to report =============================
	$('.report-container').customScroll({
		horizontal : true,
		animate : false,
		width: 10
	});
	
	$("#reportParam").val(window.location);
	
	// ============================= PAGINATE FOR MULTIPAGE REPORT =============================
	$('.paginate').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var page=parseInt($(this).attr('page'));
			var reportParam = $("#reportParam").val();
			if(reportParam.indexOf('page') != -1) {
				reportParam = reportParam.substring(0, reportParam.length-7 );
			} else {
				reportParam = reportParam.substring(0, reportParam.length );
			}
			//alert(reportParam);
		
			location.href = reportParam + '&page=' + page;
		}
	});
	

});
// *********** END DOCUMENT.READY ************


//============================= Download Reports =============================
function downloadXls() {download('xls');}

function downloadPdf() {download('pdf');}

function download(type) {
	var reportParam = $("#reportParam").val();
	var reportUrl = $("#reportUrl").val();
	if(reportParam.indexOf('page') != -1) {
		reportParam = reportParam.substring(0, reportParam.length-7 );
	} else {
		reportParam = reportParam.substring(0, reportParam.length );
	}
	if(reportParam.indexOf(reportUrl) != -1) 
		reportParam = reportParam.substring(reportParam.indexOf(reportUrl), reportParam.length );
	//alert(reportParam);
	window.open('download.do'+'?type='+type+'&token=0&reportUrl='+reportParam);
}


