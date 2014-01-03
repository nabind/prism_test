/**
 * This js file is for Parent Network
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {
	showContent($('#studentOverviewMessage'));
	showContent($('#contentDescription'));
	
	//========= Function to get standards pages =====
	$(".activitydata").live('click', function() {
		getStudentPage('getStandardActivity.do', $(this));
	});
	$(".standardsdata").live('click', function() {
		getStudentPage('getStandardIndicator.do', $(this));
	});
	$(".articledata").live('click', function() {
		getStudentPage('getArticleDescription.do', $(this));
	});
	
	
});
//=====document.ready End===================================

//======== Function to get standards pages =================
function getStudentPage(urlParam, obj) {
	blockUI();
	var dataUrl = 'subtestId='+$(obj).attr('subtestId')
					+'&studentBioId='+$(obj).attr('studentBioId')
					+'&studentName='+$(obj).attr('studentName')
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+$(obj).attr('studentGradeId')
					+'&articleId='+$(obj).attr('articleId')
					+'&contentType='+$(obj).attr('contentType');
	 $.ajax({
		type : "GET",
		url : urlParam,
		data : dataUrl,
		dataType : 'html',
		cache:false,
		success : function(data) {
			unblockUI();
			$(".main-section").html(data);
		},
		error : function(data) {						
			unblockUI();
		}
	});
}

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
