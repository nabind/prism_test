/**
 * This js file is for Parent Network - Start
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {
	showContent($('#studentOverviewMessage'));
	
	$(".standard-activity").live('click', function() {
		getGenericPage('getStandardActivity', $(this));
	});
	
	$(".standard-indicator").live('click', function() {
		getGenericPage('getStandardIndicator', $(this));
	});
	
	$(".articledata").live('click', function() {
		getGenericPage('getArticleDescription', $(this));
	});
	
	$('.browse-content').live('click', function() {
		getGenericPage('getGradeSubtestInfo', $(this));
	});
	
	$('.grade-link').live('click', function() {
		getGenericPage($(this).attr('action'), $(this));
	});
	
	$('.subtest-link').live('click', function() {
		getGenericPage($(this).attr('action'), $(this));
	});
	
	$('.menu-link').live('click', function() {
		getGenericPage($(this).attr('action'), $(this));
	});
	
	//TODO - If required
	$('#backLink').live('click', function() {
		historyBack();
	});
});
//=====document.ready End===================================

//======== Function to get pn pages =================
function getGenericPage(action, obj) {
	blockUI();
	var dataUrl = getDataUrl(action, obj);
	var urlParam = action +'.do';
	$.ajax({
		type : "GET",
		url : urlParam,
		data : dataUrl,
		dataType : 'html',
		cache:true,
		success : function(data) {
			unblockUI();
			$(".main-section").html(data);
			if(action == 'getArticleDescription'){
				showContent($('#contentDescription'));
			}
		},
		error : function(data) {						
			unblockUI();
		}
	});
}

function getDataUrl(action, obj){
	var dataUrl = '';
	if(action == 'getStandardActivity' || action == 'getStandardIndicator'){
		dataUrl = 'subtestId='+$(obj).attr('subtestId')
					+'&studentBioId='+$(obj).attr('studentBioId')
					+'&studentName='+$(obj).attr('studentName')
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+$(obj).attr('studentGradeId');
	}else if(action == 'getArticleDescription'){
		var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
		var articleId = (typeof $(obj).attr('articleId') !== 'undefined') ? $(obj).attr('articleId') : 0;
		var studentGradeId = (typeof $(obj).attr('studentGradeId') !== 'undefined') ? $(obj).attr('studentGradeId') : 0;
		var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
		var menuId = (typeof $(obj).attr('menuId') !== 'undefined') ? $(obj).attr('menuId') : 0;
		var custProdId = (typeof $(obj).attr('custProdId') !== 'undefined') ? $(obj).attr('custProdId') : 0;
		
		dataUrl = 'studentBioId='+studentBioId
					+'&articleId='+articleId
					+'&contentType='+$(obj).attr('contentType')
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+studentGradeId
					+'&subtestId='+subtestId
					+'&menuId='+menuId
					+'&menuName='+$(obj).attr('menuName')
					+'&custProdId='+custProdId;
	}else if(action == 'getGradeSubtestInfo'){
		dataUrl = 'menuId='+$(obj).attr('menuId')
					+'&menuName='+$(obj).attr('menuName');
	}else if(action == 'getChildData'){
		var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
		var studentName = (typeof $(obj).attr('studentName') !== 'undefined') ? $(obj).attr('studentName') : 0;
		var studentGradeName = (typeof $(obj).attr('studentGradeName') !== 'undefined') ? $(obj).attr('studentGradeName') : 0;
		var studentGradeId = (typeof $(obj).attr('studentGradeId') !== 'undefined') ? $(obj).attr('studentGradeId') : 0;
		
		dataUrl = 'studentBioId='+studentBioId
					+'&studentName='+studentName
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+studentGradeId;
	}
	return dataUrl;
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

/**
 * This js file is for Parent Network - End
 * Author: Joy
 * Version: 1
 */