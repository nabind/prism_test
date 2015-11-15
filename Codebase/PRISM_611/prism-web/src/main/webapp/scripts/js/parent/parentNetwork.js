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
	
	//==============Rescore request from Parent===========
	$('#studRescore').live('click', function() {
		getGenericPage('rescoreRequestFormParent', $(this));
	});
	
	// ============================ GET STUDENT REPORT ==========================================
	var tabReportObj;
	$('.studResult').on('click', function() {
		var product = $(this).attr('product');
		if(product == 'ISTEPS15') {
			getStudentReport('/public/PN/Report/PN_2015/Overall_Results_files', 1220, strings['label.overallResults'], $(this), 0);
		} else {
			getStudentReport('/public/PN/Report/Overall_Results_files', 1220, strings['label.overallResults'], $(this), 0);
		}
		
		tabReportObj = $(this);
	});
	// tab 2
	$('.reporttabs > li > a#new-tab1_new-tab1').live('click', function() {
		if($("#new-tab1").html() && $("#new-tab1").html().indexOf('Loading ...') != -1) {
			var product = $(tabReportObj).attr('product');
			if(product == 'ISTEPS15') {
				//getStudentReport('/public/PN/Report/PN_2015/resultsByStandard_files', 1221, strings['label.resultsByStandard'], $(tabReportObj), 1);
				var msg = '<p class="wrapped left-icon icon-info-round red">\
								<b>Results by Standard</b><br>\
								Performance by Standard information will be available after the final ISTEP+ results are released in December.\
							</p>'
				getEmptyStudentReport(msg);
			} else {
				getStudentReport('/public/PN/Report/resultsByStandard_files', 1221, strings['label.resultsByStandard'], $(tabReportObj), 1);
			}
		}
	});
});
//=====document.ready End===================================

//============================ GET STUDENT REPORT ==========================================
var parentContainer_1 = '<div class="right-column">\
							<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">\
								<ul class="tabs reporttabs">\
									<li class="active"><a href="#new-tab0" id="new-tab0_new-tab0">'+strings['label.overallResults']+'</a></li>\
									<li><a href="#new-tab1" id="new-tab1_new-tab1">'+strings['label.resultsByStandard']+'</a></li>';
var parentContainer_2 = '</ul>\
								<div class="tabs-content" style="padding-bottom: 50px !important;">\
									<div id="new-tab0" class="with-padding relative">';
var parentContainer_3 = '</ul>\
								<div class="tabs-content" style="padding: 50px !important;">\
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

function getEmptyStudentReport(data) {
	// show not available message for 'results by standards' in first release
	$("#new-tab1").html(data);
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
	var linkContainer = '<div class="align-right">';
	if(isrFileName != null && (isrFileName.indexOf(".pdf") != -1 || isrFileName.indexOf(".PDF") != -1 )) {
		linkContainer = linkContainer + '<a class="button compact with-tooltip" target="_blank" title="'+strings['title.individualStudentReport']+'" href="downloadFile.do?fileName='+isrFileName+'&fileType=Individual_Student_Report"><span class="button-icon"><span class="icon-read"></span></span>'+strings['msg.studentReport']+'</a>';
	} else {
		linkContainer = linkContainer + '<a class="button compact disabled with-tooltip" title="'+strings['title.noStudentReportAvailable']+'" href="#"><span class="button-icon"><span class="icon-read"></span></span>'+strings['msg.studentReport']+'</a>';
	}
	if(ipFileName != null && (ipFileName.indexOf(".pdf") != -1 || ipFileName.indexOf(".PDF") != -1 )) {
		linkContainer = linkContainer + '<a class="button compact with-tooltip margin-left" target="_blank" title="'+strings['title.appliedSkillsImagePDF']+'" href="downloadFile.do?fileName='+ipFileName+'&fileType=Image_Print"><span class="button-icon"><span class="icon-pages"></span></span>'+strings['msg.imagePDF']+'</a>';
	} else {
		linkContainer = linkContainer + '<a class="button compact disabled with-tooltip margin-left" title="'+strings['title.noImagePrintAvailable']+'" href="#"><span class="button-icon"><span class="icon-pages"></span></span>'+strings['msg.imagePDF']+'</a>';
	}
	linkContainer = linkContainer + '</div>';
	//var reportUrl = "/public/PN/Report/resultsByStandard_files";
	//var reportId = 1220;
	//var reportName = "Results by Standard";
	var dataURL = 'reportUrl='+ reportUrl + '&reportId='+reportId + '&reportName='+reportName+'&filter=true';
	//dataURL = dataURL + '&p_test_administration='+custProdId+'&p_grade='+studentGradeId+'&p_Student_Bio_Id='+studentBioId+'&p_Subtest='+subtestId+'&p_customerid='+customerId;
	dataURL = dataURL + '&p_Student_Bio_Id='+studentBioId+'&p_Subtest='+subtestId+'&p_grade='+studentGradeId+'&p_test_administration='+custProdId;
	$.ajax({
		type : "GET",
		url : 'openReportHtmlAjax.do',
		data : dataURL,
		dataType : 'html',
		cache:false,
		success : function(data) {
			unblockUI();
			if(data.indexOf('The report is empty.') != -1) {
				var msg = '<p class="wrapped left-icon icon-info-round">\
					<b>Overall Results</b><br>\
					The report is empty.\
				</p>'
					$(".main-section").html( parentContainer_1 + linkContainer + parentContainer_3 +  msg  + parentContainerEnd);
			} else {
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
				$(foundHigh).html('<span class="icon-tick black icon-size2"></span>');
			}
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
		cache: false,
		success : function(data) {
			unblockUI();
			$(".main-section").html(data);
			if(action == 'getArticleDescription'){
				showContent($('#contentDescription'), $(obj).attr('count'));
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
			var custProdId = (typeof $(obj).attr('custProdId') !== 'undefined') ? $(obj).attr('custProdId') : 0;
			dataUrl = 'subtestId='+$(obj).attr('subtestId')
					+'&studentBioId='+$(obj).attr('studentBioId')
					+'&studentName='+$(obj).attr('studentName')
					+'&studentGradeName='+$(obj).attr('studentGradeName')
					+'&studentGradeId='+$(obj).attr('studentGradeId')
					+'&custProdId='+custProdId;
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
	}else if(action == 'rescoreRequestFormParent'){
		var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
		
		dataUrl = 'p_student='+studentBioId+'&p_test_administration=3023&reportUrl=/public/INORS/Report/INORS_2015/Rescore_Request_Form_files&studentName='+$(obj).attr('studentName')+'&p_grade='+$(obj).attr('grade');
	}
	return dataUrl;
}

//============To show dynamic content in HTML===============
function showContent($container, stdCount){
	var taVal = $('#taContent').val();
	$container.html(taVal);
	$('.standardCount').html(stdCount);
}


/**
 * This js file is for Parent Network - End
 * Author: Joy
 * Version: 1
 */