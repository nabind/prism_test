/**
 * This js file is for Parent Network - Start
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {
	
	showContent($('#studentOverviewMessage'));
	
	$("a[title='standard-details']").live('click', function(event) {
		event.preventDefault();
		event.stopPropagation();
		getGenericPage('getStandardActivity', $(this), 'report');
	});

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
	
	//==============To implement back functionality - By Joy===========
	$('#backLink').live('click', function() {
		getGenericPage('historyBack', $(this));
	});
	
	// ============================ GET STUDENT REPORT ==========================================
	$('.studResult').on('click', function() {
		getStudentReport('/public/PN/Report/Overall_Results_files', 1220, 'Overall Results', $(this), 0);
	});
	// tab 2
	$('.reporttabs > li > a#new-tab1_new-tab1').live('click', function() {
		if($("#new-tab1").html() && $("#new-tab1").html().indexOf('Loading ...') != -1) {
			getStudentReport('/public/PN/Report/resultsByStandard_files', 1221, 'Results by Standard', $('.studResult'), 1);
		}
	});
});
//=====document.ready End===================================

//============================ GET STUDENT REPORT ==========================================
var parentContainer_1 = '<div class="right-column">\
							<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">\
								<ul class="tabs reporttabs">\
									<li class="active"><a href="#new-tab0" id="new-tab0_new-tab0">Overall Results</a></li>\
									<li><a href="#new-tab1" id="new-tab1_new-tab1">Results by Standard</a></li>';
var parentContainer_2 = '</ul>\
								<div class="tabs-content" style="padding-bottom: 50px !important;">\
									<div id="new-tab0" class="with-padding relative">';
var parentContainerEnd = 			'</div>\
								<div id="new-tab1" class="with-padding relative">\
									<div style="width:100%; text-align: center;">Loading ...</div>\
								</div>\
							</div>\
						</div>\
						</div>';

function getFileName(studentBioId, custProdId, type) {
	var fileName = "";
	$.ajax({
		type : "GET",
		url : 'getStudentFileName.do',
		data : "studentBioId=" + studentBioId + "&type=" + type + "&custProdId=" + custProdId,
		dataType : 'html',
		async : false,
		success : function(data) {
			var obj = jQuery.parseJSON(data);
			fileName = obj.fileName;
		}
	});
	return fileName;
}

function getStudentReport(reportUrl, reportId, reportName, obj, tabCount) {
	blockUI();
	$('.main-section').css('min-height', '850px');
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var studentGradeId = (typeof $(obj).attr('studentGradeId') !== 'undefined') ? $(obj).attr('studentGradeId') : 0;
	var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
	var custProdId = (typeof $(obj).attr('custProdId') !== 'undefined') ? $(obj).attr('custProdId') : 0; 
	var customerId = 0;
	var isrFileName = getFileName(studentBioId, custProdId, 'ISR');
	var ipFileName = getFileName(studentBioId, custProdId, 'IPR');
	var linkContainer = '<div class="align-right">\
		<a class="button compact icon-download green glossy with-tooltip" target="_blank" title="Individual Student Report" href="downloadFile.do?fileName='+isrFileName+'&fileType=Individual_Student_Report"></a>\
		<a class="button compact icon-download orange glossy with-tooltip" target="_blank" title="Image Print" href="downloadFile.do?fileName='+ipFileName+'&fileType=Image_Print"></a>\
		</div>';
	//var reportUrl = "/public/PN/Report/resultsByStandard_files";
	//var reportId = 1220;
	//var reportName = "Results by Standard";
	var dataURL = 'reportUrl='+ reportUrl + '&reportId='+reportId + '&reportName='+reportName+'&filter=true';
	//dataURL = dataURL + '&p_test_administration='+custProdId+'&p_grade='+studentGradeId+'&p_Student_Bio_Id='+studentBioId+'&p_Subtest='+subtestId+'&p_customerid='+customerId;
	dataURL = dataURL + '&p_Student_Bio_Id='+studentBioId+'&p_Subtest='+subtestId+'&p_grade='+studentGradeId+'&p_test_administration='+custProdId;
	$.ajax({
		type : "GET",
		url : 'openReportHtml.do',
		data : dataURL,
		dataType : 'html',
		cache:false,
		success : function(data) {
			unblockUI();
			if(tabCount == 0) {
				$(".main-section").html(parentContainer_1 + linkContainer + parentContainer_2 + data + parentContainerEnd);
				
				var foundHigh = $('span:contains("BLUE_IMAGE")');
				$(foundHigh).html('<img src="themes/acsi/img/circle_blue.gif" name="BLUE_IMAGE" />');
				foundHigh = $('span:contains("GREEN_IMAGE")');
				$(foundHigh).html('<img src="themes/acsi/img/circle_dk_green.gif" name="GREEN_IMAGE" />');
				foundHigh = $('span:contains("RED_IMAGE")');
				$(foundHigh).html('<img src="themes/acsi/img/circle_red.gif" name="RED_IMAGE" />');
			} else if(tabCount == 1){
				$("#new-tab1").html(data);
				
				var foundHigh = $('span:contains("DMD_IMG")');
				$(foundHigh).html('<span class="icon-marker orange icon-size2"></span>');
			}
		},
		error : function(data) {						
			unblockUI();
			$.modal.alert(strings['script.common.error']);
		}
	});
}

//======== Function to get parent network pages =================
function getGenericPage(action, obj, typ) {
	blockUI();
	
	var dataUrl = getDataUrl(action, obj, typ);
	if(typ == 'report') {
		$(obj).attr('href', '#nogo');
	}
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

function getDataUrl(action, obj, typ){
	var dataUrl = '';
	if(action == 'getStandardActivity' || action == 'getStandardIndicator'){
		if(typ == 'report') {
			dataUrl = $(obj).attr('href');
		} else {
			dataUrl = 'subtestId='+$(obj).attr('subtestId')
					+'&studentBioId='+$(obj).attr('studentBioId')
					+'&studentName='+$(obj).attr('studentName')
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+$(obj).attr('studentGradeId');
		}
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


/**
 * This js file is for Parent Network - End
 * Author: Joy
 * Version: 1
 */