/**
 * This js file is for Parent Network
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {
	showContent($('#studentOverviewMessage'));
	showContent($('#contentDescription'));
});
//=====document.ready End===================================

//============To show dynamic content in HTML===============
function showContent($container){
	var taVal = $('#taContent').val();
	$container.html(taVal);
}

function historyBack() {
	window.history.back();
	return false;
}


/*$('#browseContent').live("click", function() {
		var href = "getBrowseContent.do";
		$(".browseContent").attr("href", href);
});*/
