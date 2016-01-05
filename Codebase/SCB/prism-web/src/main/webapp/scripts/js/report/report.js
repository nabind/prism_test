/**
 * This js file is to manage report module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
//var alertInterval = 55 * 60;
//var changeInt = '';
//var seconds;
var tabsAddedArr = [];
var tabsAddedHref = [];
var tabsAdded = 0;
var currentTabCount = 0;

var autoRefreshTimeout = 10 * 60 * 1000;
// this is for auto refresh report frame for DMS application
function refreshPage() {
	$('.report-frame' ).attr( 'src', function ( i, val ) { return val; });
	setTimeout("refreshPage();", autoRefreshTimeout);
}

$(document).ready(function() {
	 /*$('#report-list').tablesorter({
		headers: {
			5: { sorter: false }
		}
	});*/

	// To get the scroll bar appear in the screen 
	//var blankSpace = 600-(($.browser.msie) ? -20 : 60);
	//var height = $(document).height()-blankSpace;
	//$("#reportContainer").height(height);
	
	// initialize auto refresh report frame for DMS application
	//setTimeout("refreshPage();", autoRefreshTimeout);
	
	// hide menu while opening report page
	//$('.clearfix').addClass('menu-hidden');
	
	// ============================= DOWNLOAD BULK CANDIDATE REPORT =============================
	$('#BulkCandidateReport').live('click', function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadBulkCandidate(this,'REGULAR',event); //Fixed QC 76658
	});
	$('#BulkCandidateReportEdu').live('click', function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadBulkCandidate(this,'EDUCENTER',event); //Fixed QC 76658
	});
	
	// for only bulk candidate report button
	$(".jqdatepicker").live('blur', function() {
		try {
			$(this).parents('.icholder').siblings('.reportFilterCriteria').find('.download-button').hide(100);
			$(this).parents('.icholderinner').siblings('.refresh-report').find('.button').removeClass('blue-gradient').addClass('green-gradient');
			if($('#BulkCandidateReport').is(':visible')) $('#BulkCandidateReport').hide(100);
			if($('#BulkCandidateReportEdu').is(':visible')) $('#BulkCandidateReportEdu').hide(100);
		} catch (e) {}
	});
	$(".input").live('blur', function() {
		try {
			// change refresh button color
			$(this).parents('.icholderinner').siblings('.refresh-report').find('.button').removeClass('blue-gradient').addClass('green-gradient');
			// show tooltip on refresh button
			$(this).parents('.icholderinner').siblings('.refresh-report').find('.button').tooltip('Click <strong>here</strong> to get filtered data', {delay:300, classes: ['orange-gradient', 'with-padding']});
		} catch (e) {}
	});
	$("input[name='p_Last_Name']").live('keydown', function() {
		try {
			resetValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_First_Name']").live('keydown', function() {
		try {
			resetValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_StudentId']").live('keydown', function() {
		try {
			resetValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_Last_Name']").live('blur', function() {
		try {
			setValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_First_Name']").live('blur', function() {
		try {
			setValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_StudentId']").live('blur', function() {
		try {
			setValidationInputControls($(this).parents('.icholder').siblings('.reportFilterCriteria').attr("tabcount"));
		} catch (e) {}
	});
	$("input[name='p_StudentId']").live('click', function() {
		try {
			$(this).attr("maxlength", "9"); // QC #79620 - Fixed
		} catch (e) {}
	});
	
	// ============================= Hide validation error messages on tab switch =============================
	$('.tabs-content > div').live('hidetab', function() {
		try {
			//$(".report-form").validationEngine('hide');
			$(".formError").trigger("click");
		} catch (e) {}
	});
	
	// ============================= Add custom scroll to report =============================
	$('.report-container').customScroll({
		horizontal : true,
		showOnHover : false,
		animate : false
	});
	// ============================= Show product logo =============================
	$('.reporttabs > li > a').live('click', function(event) {
		var allLis = $('.reporttabs > li > a');
		$.each(allLis, function(index) { 
			if($(this).parent().attr('shortName') != null) {
				$(this).parent().find('.tabtext').text( $(this).parent().attr('shortName') );
			}
		});
		$(this).parent().find('.tabtext').text( $(this).parent().attr('name') );
		/** This is added for Missouri */
		var tabName = $(this).parent().find('.tabtext').text();
		if(tabName != '') $('.MAPNote').show();
		else $('.MAPNote').hide();
		/** end for Missouri */
		//$('.productImage').hide();
		//$('#productImage'+$(this).attr('assessment')).show();
		//$('#productImage'+$(this).attr('assessment')).attr('src', 'image.do?assessmentId='+$(this).attr('assessment'));
	});
	
	// ============================= Download report =============================
	$(".download-button-pdf").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadPdf($(this).attr('count'), $(this).attr('param'), $(this).attr('assessment'));
	});
	$(".download-button-xls").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadXls($(this).attr('count'), $(this).attr('param'), $(this).attr('assessment'));
	});
	
	$(".moreinfo-button-xls").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		moreInfo($(this).attr("reportId"));
	});
	
	$(".review-button-pdf").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadPdf($(this).attr('count'), $(this).attr('param'), $(this).attr('assessment'));
	});
	
	// ============================= Remove report tab =============================
	$(".closereport").live("click", function(e) {
		e.stopImmediatePropagation();
		var close = $(this), parent = close.parent();
		var closeId = close.attr('param');
		var dataURL = 'reportId=' + close.attr('reportId');
		$.ajax({
			type : "GET",
			url : 'clearSessionReports.do',
			data : dataURL,
			dataType: 'json',
			cache:false,
			success : function(data) {
				tabsAdded = tabsAdded - 1;
				$(".tabs a[href^='#new-tab0']").click();
				$("#new-tab0_new-tab0").click();
				
				$("#"+closeId+"-"+closeId+"").fadeAndRemove().trigger('close');
				$("#"+closeId).fadeAndRemove().trigger('close');
				
				// de-register the tab
				var tabsAddedArrTemp = [];
				var tabsAddedHrefTemp = [];
				$.each(tabsAddedArr, function(index, value) { 
					tabsAddedHrefTemp.push(tabsAddedHref[index]);
					tabsAddedArrTemp.push(tabsAddedArr[index]);
				});
				
				while (tabsAddedArr.length > 0) {
					tabsAddedArr.pop();
					tabsAddedHref.pop();
				}

				$.each(tabsAddedArrTemp, function(index, value) { 
					if(closeId != tabsAddedHrefTemp[index]) {
						tabsAddedHref.push(tabsAddedHrefTemp[index]);
						tabsAddedArr.push(tabsAddedArrTemp[index]);
					}
				});
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
			}
		});
		
	});

	// ============================= get input control list =============================
	//$(".reportFilterCriteria").live("click", function() {
	$(".reportFilterCriteria").live("click", function(event) {
		$("li.active").click();
		//fixed 71682 
		if($.browser.msie) {
			if(!$.template.ie7) {
				$(this).next(".icholder").toggleClass('display-none');
			}
		}
		else {
			$(this).next(".icholder").slideToggle(500);
		}
		$(this).toggleClass("rounded-border");
		var count = $(this).attr("count");
		var tabCount = $(this).attr("tabCount");
		// hide error messages
		try {
			//$(".report-form").validationEngine('hide');
			$(".formError").trigger("click");
		} catch (e) {}
		
		var dnBtn = $('.download-button-'+count);
		//var dnBtnObj = dnBtn.get(tabCount);
		//$(dnBtnObj).toggleClass("download-button-right");
		$(dnBtn).toggleClass("download-button-right");
		
		$(this).find(".reportButton").removeClass("disabled");
		var assessmentId = $(this).attr("assessment");
		//var allInputContainer = $('.inputControlContailer');
		//var containerObj = allInputContainer.get(tabCount);
		var containerObj = $('.inputControlContailer-'+count);
		var reportUrl = $(this).attr("param");
		var dataURL = "reportUrl="+reportUrl+"&count="+count+'&assessmentId='+assessmentId;
		if($(containerObj).html() && $(containerObj).html().indexOf('loading64.gif') != -1) {
			$.ajax({
				type : "GET",
				url : 'populateInputControls.do',
				data : dataURL,
				dataType: 'json',
				cache:false,
				success : function(data) {
					
					//patch for Student Roster Report for School
					if (reportUrl=='/public/CTB_School/Reports/TerraNova3/Student_Roster_Report_files'){
						setInputControlForStudentRoster(data, containerObj);
					}else{
						setInputControl(data, containerObj);
					}
					
					// patch for IE7
					if($.template.ie7) {
						var icHeight = $(".icholder-"+tabCount).height();
						icHeight = icHeight+20;
						icHeight = icHeight + "px";
						
						var inHeight = $(".icholder-"+tabCount).height();
						inHeight = inHeight+20;
						inHeight = inHeight + "px";
						$(".icholder").css("height", icHeight );
						$(".icholderinner").css("height", inHeight);

						blockUI();
						$(".icholder").toggleClass('display-none');
						setTimeout(patchForIE, 1000);
						setTimeout(patchForIE, 1500);
						setTimeout(unblockUI, 1500);
					}
				},
				error : function(data) {
					$.modal.alert(strings['script.common.error']);
				}
			});
		} else {
			// patch for IE7
			if($.template.ie7) {
				$(".icholder").toggleClass('display-none');
			}
		}
		event.stopPropagation();
		event.preventDefault();
	});
	
	// ============================= get filtered report =============================
	$(".reportButton").live("click", function(event) {
		event.stopImmediatePropagation();
		$(document).click();
		//blockUI();
		var count = $(this).attr("count");
		var tabCount = $(this).attr("tabCount");
		blockUI('new-tab'+count+'');
		//$(this).parent().siblings('.reportFilterCriteria').click();
		var reportUrl = $(this).attr("param");
		var reportName = $(this).attr("reportName");
		var reportId = $(this).attr("reportId");
		var formObj = $('.report-form-'+count);
		var apiUrl = $(this).attr("apiUrl");
		//var formObj = reportForm.get(tabCount);
		
		var lastname = formObj.find("input[name='p_Last_Name']");
		if(lastname != null && lastname.length > 0) {
			$("input[name='p_Last_Name']").focus();
			$("input[name='p_First_Name']").focus();
			$(document).click();
		}
		
		$(formObj).validationEngine();
		if(formObj.validationEngine('validate')) {
			var passSubtest = true;
			// validation for selected student report
			var minScore = formObj.find("input#p_MinScore");
			var maxScore = formObj.find("input#p_MaxScore");
			if(minScore != null && maxScore != null && minScore.length > 0) {
				if(minScore.val() < 0 || minScore.val().length==0 || isNaN(minScore.val())) {
					$.modal.alert(strings['script.report.minScore']);
					passSubtest = false;
				} else if(maxScore.val() < 0 || maxScore.val().length==0 || isNaN(maxScore.val())) {
					$.modal.alert(strings['script.report.maxScore']);
					passSubtest = false;
				}
			}
			
			var subtest1 = formObj.find("#p_Roster_Subtest1");
			var subtest2 = formObj.find("#p_Roster_Subtest2");
			var subtest3 = formObj.find("#p_Roster_Subtest3");
			// Validation for multiselect student dashboard
			if(subtest1 != null && subtest2 != null && subtest3.length > 0) {
				if( (subtest1.val() == subtest2.val()) || 
					(subtest1.val() == subtest3.val()) ||
					(subtest2.val() == subtest3.val()) ) {
					$.modal.alert(strings['msg.selectSubtest']);
					passSubtest = false;
				}
			}
			var minSubtestScore1 = formObj.find("input#p_Subtest1_MinScore");
			var maxSubtestScore1 = formObj.find("input#p_Subtest1_MaxScore");
			var minSubtestScore2 = formObj.find("input#p_Subtest2_MinScore");
			var maxSubtestScore2 = formObj.find("input#p_Subtest2_MaxScore");
			var minSubtestScore3 = formObj.find("input#p_Subtest3_MinScore");
			var maxSubtestScore3 = formObj.find("input#p_Subtest3_MaxScore");
			if(passSubtest && minSubtestScore1 != null && maxSubtestScore1 != null && minSubtestScore1.length > 0) {
				if(minSubtestScore1.val() < 0 || (minSubtestScore1.val()).replace(/\s/g,"").length==0 || isNaN(minSubtestScore1.val())) {
					$.modal.alert(strings['script.report.minScoreSubtest'] + ' ' + $("#p_Roster_Subtest1 option:selected").text() );
					passSubtest = false;
				} else if(maxSubtestScore1.val() < 0 || (maxSubtestScore1.val()).replace(/\s/g,"").length==0 || isNaN(maxSubtestScore1.val())) {
					$.modal.alert(strings['script.report.maxScoreSubtest'] + ' ' + $("#p_Roster_Subtest1 option:selected").text() );
					passSubtest = false;
				}
			} 
			if(passSubtest && minSubtestScore2 != null && maxSubtestScore2 != null && minSubtestScore2.length > 0) {
				if(minSubtestScore2.val() < 0 || (minSubtestScore2.val()).replace(/\s/g,"").length==0 || isNaN(minSubtestScore2.val())) {
					$.modal.alert(strings['script.report.minScoreSubtest'] + ' ' + $("#p_Roster_Subtest2 option:selected").text() );
					passSubtest = false;
				} else if(maxSubtestScore2.val() < 0 ||(maxSubtestScore2.val()).replace(/\s/g,"").length==0 || isNaN(maxSubtestScore2.val())) {
					$.modal.alert(strings['script.report.maxScoreSubtest'] + ' ' + $("#p_Roster_Subtest2 option:selected").text() );
					passSubtest = false;
				}
			} 
			if(passSubtest && minSubtestScore3 != null && maxSubtestScore3 != null && minSubtestScore3.length > 0) {
				if( $("#p_Roster_Subtest3").val() != -1 ) { 
					if(minSubtestScore3.val() < 0 || (minSubtestScore3.val()).replace(/\s/g,"").length==0 || isNaN(minSubtestScore3.val())) {
						$.modal.alert(strings['script.report.minScoreSubtest'] + ' ' + $("#p_Roster_Subtest3 option:selected").text() );
						passSubtest = false;
					} else if(maxSubtestScore3.val() < 0 || (maxSubtestScore3.val()).replace(/\s/g,"").length==0 || isNaN(maxSubtestScore3.val())) {
						$.modal.alert(strings['script.report.maxScoreSubtest'] + ' ' + $("#p_Roster_Subtest3 option:selected").text() );
						passSubtest = false;
					}
				}
				else{
					 minSubtestScore3.val("");
					 maxSubtestScore3.val("");
				}
			}
			// End Validation for multiselect student dashboard
			
			// validation for longitudinal
			var thisFormElements = formObj.find("select#p_Subtest_Class_MultiSelect");
			if(thisFormElements != null) {
				var vals = thisFormElements.val();
				if(vals != null && vals.length > 7) {
					$.modal.alert(strings['script.report.maxSubtest']);
					passSubtest = false;
				}
			} 
			thisFormElements = formObj.find("select#p_Subtest_MultiSelect");
			if(thisFormElements != null) {
				var vals = thisFormElements.val();
				if(vals != null && vals.length > 7) {
					$.modal.alert(strings['script.report.maxSubtest']);
					passSubtest = false;
				}
			}
			thisFormElements = formObj.find("select#p_Longitudinal_Roster_Subtest_MultiSelect");
			if(thisFormElements != null) {
				var vals = thisFormElements.val();
				if(vals != null && vals.length > 7) {
					$.modal.alert(strings['script.report.maxSubtest']);
					passSubtest = false;
				}
			}
			thisFormElements = formObj.find("select#p_Subtest_MultiSelect_School_Longitudinal_Summary");
			if(thisFormElements != null) {
				var vals = thisFormElements.val();
				if(vals != null && vals.length > 7) {
					$.modal.alert(strings['script.report.maxSubtest']);
					passSubtest = false;
				}
			}
			// end : validation for longitudinal
			
			if(passSubtest) {
				var dataURL = reportUrl + '&reportId='+reportId + '&reportName='+reportName+'&filter=true&' + $(formObj).serialize();
				
				var obj = $('.report-frame-'+count);
				//var obj = reportFrame.get(tabCount); 
				$(obj).attr('src', apiUrl+'.do?reportUrl='+dataURL);
				
				// reset pagination
				var currentObj = $('.pagination-'+count);
				//var currentObj = paginateDiv.get(tabCount); 
				resetPagination(currentObj);
			} else {
				//unblockUI();
				unblockUI('new-tab'+count+'');
				$(".page_first").css("cursor", "auto");
				$(".page_prev").css("cursor", "auto");
			}
			
			//patch for IE , refreshing the IE butons while report refresh.
			if($.browser.msie) {
			 $(".pagination-"+count).find("a.page_first").addClass("disabled");
			 $(".pagination-"+count).find("a.page_prev").addClass("disabled");
			 $(".pagination-"+count).find("a.page_next").removeClass("disabled");
			 $(".pagination-"+count).find("a.page_next").removeAttr("style");
			 $(".pagination-"+count).find("a.page_last").removeClass("disabled");
			 $(".pagination-"+count).find("a.page_last").removeAttr("style");
			 $(".pagination-"+count).find("a.page_first").removeAttr("style");
			 $(".pagination-"+count).find("a.page_first").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			 $(".pagination-"+count).find("a.page_first").css("cursor", "not-allowed");
			  $(".pagination-"+count).find("a.page_prev").removeAttr("style");
			 $(".pagination-"+count).find("a.page_prev").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			 $(".pagination-0").find("a.page_prev").css("cursor", "not-allowed");
			 
			 // patch for IE
				
			}
		} else {
			unblockUI('new-tab'+count+'');
		}
	});
	
	// ============================= set parameter for first report =============================
	if($('.report-frame').length > 0) {
		//addReportTab($("#reportUrl").val(), $("#reportId").val(), $("#reportName").val(), $("#assessment").val(), true);
		tabsAdded = tabsAdded + 1;
		currentTabCount = currentTabCount +1;
		tabsAddedArr.push($("#reportId").val());
		tabsAddedHref.push('new-tab0');
		//blockUI();
		if($.template.ie7) $('.download-button').addClass('download-button-ie');
		closeProgress($('.report-filter-0').attr('param'), '0', true);
	}
	
	// ----------------------- FETCH REPORT MENU -----------------------
	//if($('.report-frame').length > 0) fetchReportMenu();
	//else openAdminMenu();
	/** Report menu fetching is moved to keepAlive.jsp */
	
	// ----------------------- KEEP ALIVE USER SESSION -----------------------
	//if($('.report-frame').length > 0) resetTimer();
	
	// ============================= PAGINATE FOR MULTIPAGE REPORT =============================
	$('.paginate').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var reportUrl=$(this).attr('param');
			var reportid=$(this).attr('reportid');
			var tabCount=$(this).attr('count');
			
			$('#loading'+tabCount+'').show(200);
			
			var reportFrame = $('.report-frame');
			var obj = $('.report-frame-'+tabCount); //reportFrame.get(tabCount); 
			
			var page=parseInt($(this).attr('page'));
			$(obj).attr('src','openReportHtml.do?reportId='+reportid+'&reportUrl='+reportUrl+'&page='+page);
			
			$(this).parent().find(".pageCurrent").html(page+1);
		}
	});
	
	$('.page_first').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var page=parseInt($(this).attr('page'));
			$(this).siblings(".page_next").attr('page', page+1);
			// TODO disable prev, first
			//$('.paginate').removeClass('disabled');
			removeDisabled(this);
			$(this).siblings(".page_prev").addClass('disabled');
			$(this).addClass('disabled');
			$(this).siblings(".page_prev").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			$(this).css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			
			$(this).siblings(".page_next").removeClass('disabled');
			$(this).siblings(".page_last").removeClass('disabled');
			$(this).siblings(".page_next").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
			$(this).siblings(".page_last").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
		}
	});
	
	$('.page_last').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var page=parseInt($(this).attr('page'));
			$(this).siblings(".page_prev").attr('page', page-1);
			// TODO disable next, last
			//$('.paginate').removeClass('disabled');
			removeDisabled($(this));
			$(this).siblings(".page_next").addClass('disabled');
			$(this).addClass('disabled');
			$(this).siblings(".page_next").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			$(this).css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
			
			$(this).siblings(".page_first").css("cursor", "pointer");
			$(this).siblings(".page_prev").css("cursor", "pointer");
			$(this).siblings(".page_first").removeClass('disabled');
			$(this).siblings(".page_prev").removeClass('disabled');
			$(this).siblings(".page_first").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
			$(this).siblings(".page_prev").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
		}
	});
	
	$('.page_prev').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var page=parseInt($(this).attr('page'));
			if(page == 0) {
			// TODO disable prev, first
				//$('.paginate').removeClass('disabled');
				removeDisabled($(this));
				$(this).siblings(".page_first").addClass('disabled');
				$(this).addClass('disabled');
				$(this).siblings(".page_first").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				$(this).css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				
				$(this).attr('page', page);
				$(this).siblings(".page_next").attr('page', page+1);
			} else {
				//$('.paginate').removeClass('disabled');
				removeDisabled($(this));
				
				$(this).attr('page', page-1);
				$(this).siblings(".page_next").attr('page', page+1);
				/*if(page+1 == parseInt($(this).siblings('.page_last').attr('page'))) {
					$(this).siblings(".page_next").attr('page', page+1);
				} else {
					$(this).siblings(".page_next").attr('page', page);
				}*/
			}
			$(this).siblings(".page_next").removeClass('disabled');
			$(this).siblings(".page_last").removeClass('disabled');
			$(this).siblings(".page_next").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
			$(this).siblings(".page_last").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
		}
	});
	
	$('.page_next').live("click", function(){
		if(!$(this).hasClass('disabled')) {
			var page=parseInt($(this).attr('page'));
			var lastPage = parseInt($(this).siblings(".page_last").attr('page'));
			if(page == lastPage) {
			// TODO disable next, last
				//$('.paginate').removeClass('disabled');
				removeDisabled($(this));
				$(this).siblings(".page_last").addClass('disabled');
				$(this).addClass('disabled');
				$(this).siblings(".page_last").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				$(this).css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				
				$(this).attr('page', page);
				$(this).siblings(".page_prev").attr('page', page-1);
			} else {
				//$('.paginate').removeClass('disabled');
				removeDisabled($(this));
				
				$(this).attr('page', page+1);
				$(this).siblings(".page_prev").attr('page', page-1);
				/*if(page-1 == parseInt($(this).siblings('.page_first').attr('page'))) {
					$(this).siblings(".page_prev").attr('page', page-1);
				} else {
					$(this).siblings(".page_prev").attr('page', page);
				}*/
			}
			$(this).siblings(".page_first").css("cursor", "pointer");
			$(this).siblings(".page_prev").css("cursor", "pointer");
			$(this).siblings(".page_first").removeClass('disabled');
			$(this).siblings(".page_prev").removeClass('disabled');
			$(this).siblings(".page_first").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
			$(this).siblings(".page_prev").css("background", "#9C9C9C url(themes/acsi/img/old-browsers/colors/bg_grey-gradient_glossy.png) repeat-x !important");
		}
	});

});
// *********** END DOCUMENT.READY ************

function resetValidationInputControls(tabCount) {
	resetClasses($("input[name='p_Last_Name']"));
	resetClasses($("input[name='p_First_Name']"));
	resetClasses($("input[name='p_StudentId']"));
	$(".report-form-" + tabCount).validationEngine("hide");
}

function setValidationInputControls(tabCount) {
	var p_Last_Name1 = $("input[name='p_Last_Name']").val();
	var p_First_Name1 = $("input[name='p_First_Name']").val();
	var p_StudentId1 = $("input[name='p_StudentId']").val();
	
	//Mofify for TD - 81540 
	if (p_StudentId1.length > 0) {
		$("input[name='p_StudentId']").addClass("validate[custom[nineDigitNumber]]");
	}else if (p_Last_Name1.length > 0 || p_First_Name1.length > 0) {
		$("input[name='p_Last_Name']").addClass("validate[required, minSize[2]]");
		$("input[name='p_First_Name']").addClass("validate[required, minSize[2]]");
	} else { // All 3 are blank
		setDefaultValidationsToStudentsSearch();
	}

}

function setDefaultValidationsToStudentsSearch() {
	resetClasses($("input[name='p_Last_Name']"));
	resetClasses($("input[name='p_First_Name']"));
	resetClasses($("input[name='p_StudentId']"));
	$("input[name='p_Last_Name']").addClass("validate[required, minSize[2]]"); // default validation
	$("input[name='p_First_Name']").addClass("validate[required, minSize[2]]"); // default validation
	$("input[name='p_StudentId']").addClass("validate[custom[onlyNumber],maxSize[9],minSize[4]]"); // default validation
}

function resetClasses(e) {
	e.removeAttr("class");
	e.attr('class', "input input-compact navy-gradient");
}

// *********** END DOCUMENT.READY ************
//============================= BULK CANDIDATE REPORT =============================
function downloadBulkCandidate(obj, eduUser, event) { //Fixed QC 76658
	event.stopImmediatePropagation();
	$(document).click();
	var reportUrl = $(obj).attr("param");
	var count = $(obj).attr("count");
	var tabCount = $(obj).attr("tabCount");
	var formObj = $('.report-form-'+count);
	blockUI('new-tab'+tabCount+'');
	$.ajax({
		type : "GET",
		url : "downloadCandicateReport.do",
		data : $(formObj).serialize()+"&reportUrl="+reportUrl+"&userType="+eduUser,
		dataType: 'json',
		cache:false,
		success : function(data) {
			if (data.status == 'Success')	{
				notify('The bulk Candidate Report download request has been processed. Check the status of the same from Downloads -> Group Download Files', {
					autoClose: false,
					showCloseOnHover: false,
					classes: ['green-gradient']
				});
			} else {
				notify('The Bulk candidate Report download request is failed. Please try later.', {
					autoClose: false,
					showCloseOnHover: false,
					classes: ['red-gradient']
				});
			}
			unblockUI('new-tab'+tabCount+'');
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error']);
			unblockUI('new-tab'+tabCount+'');
		}
	});
}
//============================= USER KEEP ALIVE =============================
/*function keepAliveACSI() {
	$.modal.confirm('Your ACSI Data Online session is about to expire. Confirm to remain in the session.', function()
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
	} else {
		// Next count
		changeInt = setTimeout(reserAlert, 1000);
	}
}
function onClick( ev ) {
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

};
*/
//============================= FETCH ADMIN MENU =============================
function openAdminMenu() {
	if($("#adminMenu").length > 0) $("#adminMenu").show(200);
	//$("#tempMenu").hide(200);
	fetchReportMenu('ADM');
}

//============================= FETCH REPORT MENU =============================
function fetchReportMenu(typ) {
	var theme = strings['prism.theme'];
	var dataURL = "theme="+theme;
	$.ajax({
		type : "GET",
		url : 'fetchReportMenu.do',
		data : dataURL,
		dataType : 'html',
		cache : false,
		async : false,
		success : function(data) {
			$("#prismMenu").html(data);
			if(typ == 'ADM') {
				$("#menu_Reports").hide();
				// added following two lines for new requirement in inors
				$("[id='menu_Reports (ISTEP+ Spring 2015)']").hide();
				$("[id='menu_Reports (All except ISTEP+ Spring 2015)").hide();
				$("#menu_Downloads").hide();
				//$("#menu_Resources").addClass('black-gradient');
				//$("[id='menu_Useful Links']").addClass('black-gradient');
			}
			// $('#tab-1').removeTab();
			callbackMenu();
		},
		error : function(data) {
			$.modal.alert(strings['script.report.menu']);
		}
	});
}

//============================= open report in a tab =============================
/**
 * Function to check if there is enough space to open a new tab
 * @param string tabId the id of the tabs wrapper
 * @return boolean true if enough space, else false
 */
function canAddNewTab(tabsID) {
    var tabs = $('.tabs'),
        lastTab = tabs.children().last(),
        position = lastTab.position(),
        availableSpace = tabs.width()-position.left-lastTab.outerWidth();

    return (availableSpace > 250); // Set the minimum width here
}

var reportCloseSpan = '<span id="CLOSE_ID" class="close closereport show-on-parent-hover">x</span>';

var newTab = true;
// Add a tab
function addReportTab(reportUrl, reportId, reportName, assessmentId, isHome, reportType, customUrl)
{	
	$(".reportButton").removeTooltip();
	blockUI();
	newTab = true;
	// New tab id
	//var tabId = 'new-tab'+tabsAddedArr.length;
	var tabId = 'new-tab'+currentTabCount;
	// check if the report is already open
	$.each(tabsAddedArr, function(index, value) { 
		//alert(index + ': ' + value); 
		if(value == reportId) {
			//$(".tabs a[href^='#"+tabsAddedHref[index]+"']").trigger('click');
			$("#"+tabsAddedHref[index]+"_"+tabsAddedHref[index]).click();
			newTab = false;
			unblockUI();
		}
	});
	// ====== This section added for resource download =====
	if(reportUrl == 'resourcepdf') {
		newTab = false;
		window.open(customUrl+'&userType='+$('#customerId').val());
	}
	if(reportUrl == 'extLinks') {
		newTab = false;
		window.open(customUrl);
	}
	if(newTab) {
		//if(tabsAddedArr.length > 3) {
		if(!canAddNewTab(tabId) && !(tabsAddedArr.length == 1) ) {
			$.modal.alert(strings['script.report.maxTab']);
			
			// Patch for disabling pagination
			if(!($('.pagination .page_next').hasClass('disabled')) && ($('.pagination .page_prev').hasClass('disabled'))){
				$('.pagination .page_next').attr('page', '1');
				$('.pagination .page_prev').attr('page', '0');
				$('.pagination .page_prev').addClass('disabled');
				$('.pagination .page_first').addClass('disabled');
				
				// patch for IE
				$(".page_first").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				$(".page_prev").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
				$(".page_first").css("cursor", "not-allowed");
				$(".page_prev").css("cursor", "not-allowed");
			}
			
			unblockUI();
		} else {
			newTab = true;
			
			// prepare title
			var closeSpan = reportCloseSpan.replace(/CLOSE_ID/g, tabsAddedArr.length);
		
			// Register tab
			//tabsAdded = tabsAdded + 1;
			
			tabsAddedArr.push(reportId);
			tabsAddedHref.push(tabId);
			
			// create report content
			if(!isHome) { // for home page report URL is present we need to register that only
				//getmodel(reportUrl, reportId, reportName + closeSpan, tabId, tabsAddedArr.length-1, assessmentId);
				//getmodel(reportUrl, reportId, reportName, tabId, tabsAddedArr.length-1, assessmentId);
				//getmodel(reportUrl, reportId, reportName, tabId, currentTabCount, assessmentId);
				getmodel(reportUrl, reportId, reportName, tabId, currentTabCount, assessmentId, tabsAdded, reportType, customUrl);
			}
			tabsAdded = tabsAdded + 1;
			currentTabCount = currentTabCount + 1;
			
		}
	}
	unblockUI();
};

// check if report has multiple pages
var paginatedArr = [];
function checkpagination(reportUrl, tabCount) {
	// check if calling first time
	var newcall = true;
	$.each(paginatedArr, function(index, value) { 
		if(value == reportUrl) {
			newcall = false;
		}
	});
	if(newTab) {
		newcall = true;
		newTab = false;
	} 
	//if(newcall) {
		if(newcall) paginatedArr.push(reportUrl);
		var paginateDiv = $('.pagination');
		var currentObj = $(".pagination-"+tabCount); //paginateDiv.get(tabCount); 
		var reportIframe = $('.report-frame');
		var iFrameObj = $(".report-frame-"+tabCount); //reportIframe.get(tabCount);
		var wrapperObj = $(".iframeWrapper-"+tabCount);
		var reportContainer = $('.report-container');
		var iFrameContainerObj = $('.report-container-'+tabCount); //reportContainer.get(tabCount);
		
		var dataURL = "reportUrl="+reportUrl;
		$.ajax({
			type : "GET",
			url : 'checkpagination.do',
			data : dataURL,
			dataType: 'html',
			cache:false,
			async : false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				// set ifarme height and width
				$(iFrameObj).css('height', obj.height+'px');
				$(iFrameObj).css('width', obj.width+'px');
				$(wrapperObj).css('width', obj.width+'px');
				if($.template.iOs || $.template.android) {
					$(wrapperObj).draggable({ 
						axis: "x",
						stop: function( event, ui ) {
							var left = parseInt($(wrapperObj).css("left"));
							if(left > 0 ) {
								$(wrapperObj).animate({ left:0 });	
							} else {
								var l = (left-300) * -1;
								var w = parseInt($(wrapperObj).css("width"));
								if(l > w) {
									$(wrapperObj).animate({ left:left+300 });
								}
							}
						} 
					});
				} else {
					$(".iframeWrapper").css({'background':'none', 'padding-top':'0px', 'cursor': 'default'});
				}
				/*$(iFrameContainerObj).customScroll({
					horizontal : true,
					showOnHover : false,
					animate : false
				});*/
				$(iFrameContainerObj).smoothDivScroll({
					manualContinuousScrolling: true
				});
				hideScrollingHotSpot(reportUrl);
				if(newcall && obj != null && obj.paginate == "true") {
					$(currentObj).css('display', 'block');
					resetPagination(currentObj);
					$(currentObj).find('.page_last').attr('page', parseInt(obj.page)-1);
					$(currentObj).find('.pageEnd').html(parseInt(obj.page));
				} else if(obj != null && obj.paginate == "true") {
					$(currentObj).css('display', 'block');
					$(currentObj).find('.page_last').attr('page', parseInt(obj.page)-1);
					$(currentObj).find('.pageEnd').html(parseInt(obj.page));
					//$(".page_first").css("cursor", "auto");
					//$(".page_prev").css("cursor", "auto");
				}
				if(obj != null && !(obj.paginate == "true")) {
					$(currentObj).css('display', 'none');
				}
				//For TASC
				if(obj.reportUrl == '/public/TASC/Reports/TASC_Org_Hier/Student_Roster_files' 
										|| obj.reportUrl == '/public/TASC/Reports/TASC_Edu_Center/Student_Roster_files'){

					// code for enable/disable download button
					if (obj.totalPages > 0) {
						$("#BulkCandidateReportEdu").removeClass('disabled');
						$("#BulkCandidateReport").removeClass('disabled');
						$("#BulkCandidateReportEdu").removeClass('disable_a_href');
						$("#BulkCandidateReport").removeClass('disable_a_href');
					} else {
						$("#BulkCandidateReportEdu").addClass('disabled');
						$("#BulkCandidateReport").addClass('disabled');
						$("#BulkCandidateReportEdu").addClass('disable_a_href');
						$("#BulkCandidateReport").addClass('disable_a_href');
					}					
				}
				$('html, body').scrollTop(0);
			},
			error : function(data) {
				$.modal.alert(strings['script.report.pagination']);
			}
		});
	//} 
	
}
// reset fist, prev and next button
function resetPagination(currentObj) {
	//$('.paginate').removeClass('disabled');
	removeDisabled($(currentObj));
	$(currentObj).find('.page_next').attr('page', '1');
	$(currentObj).find('.page_prev').attr('page', '0');
	$(currentObj).find('.page_prev').addClass('disabled');
	$(currentObj).find('.page_first').addClass('disabled');
	
	// fallback
	$(".page_first").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
	$(".page_prev").css("background", "#B9B9B9 url(themes/acsi/img/old-browsers/colors/bg_button_grey-gradient_disabled.png) repeat-x !important");
	$(".page_first").css("cursor", "not-allowed");
	$(".page_prev").css("cursor", "not-allowed");
	
	// update current page # (page xx of yy) 
	$(currentObj).parent().find(".pageCurrent").html("1");
}

function removeDisabled(currentObj) {
	$(currentObj).find('.page_next').removeClass('disabled');
	$(currentObj).find('.page_last').removeClass('disabled');
	$(currentObj).find('.page_prev').removeClass('disabled');
	$(currentObj).find('.page_first').removeClass('disabled');
}

// fetch report data
/*
function getmodel(reportUrl, reportId, reportName, tabId, tabCount, assessmentId) {
	var dataURL = 'path=report/report';
	$.ajax({
		type : "GET",
		url : 'loadJSPView.do',
		data : dataURL,
		dataType: 'html',
		success : function(data) {
			// Create
			$('#add-tabs').addTab(tabId, reportName, reportName);
			// show
			$("a[href^='#"+tabId+"']").click();
			
			// set content
			var reportContent = data.replace(/_REPORT_URL_/g, reportUrl);
			reportContent = reportContent.replace(/_TAB_COUNT_/g, tabCount);
			reportContent = reportContent.replace(/_FRAME_ID_/g, tabCount);
			reportContent = reportContent.replace(/_LOAD_ID_/g, 'loading'+tabCount);
			reportContent = reportContent.replace(/_ASSESSMENT_/g, assessmentId);
			reportContent = reportContent.replace('loadJSPView.do?path=common/loading', 'openReportHtml.do?assessmentId='+assessmentId+'&reportUrl='+reportUrl);
			$(".tab-active").html(reportContent);
			
		},
		error : function(data) {
			$.modal.alert('Some error occured while fetching data. Please try after some time.');
		}
	});
}*/

function getmodel(reportUrl, reportId, reportName, tabId, tabCount, assessmentId, currentTabNumber, reportType, customUrl) {
	var dataURL = 'path=report/report&reportUrl='+reportUrl+'&reportName='+reportName+'&reportId='+reportId+'&tabCount='+tabCount+'&assessmentId='+assessmentId+'&currentTabNumber='+currentTabNumber+'&studentId='+$(".studentIdForTab").val()+'&reportType='+reportType+'&customUrl='+customUrl;
	dataURL = dataURL + "&CSRFToken=" + $( "input[name='CSRFToken']" ).val();
	$.ajax({
		type : "POST",
		url : 'loadReport.do',
		data : dataURL,
		dataType: 'html',
		cache:false,
		success : function(data) {
			// Create
			$('#add-tabs').addTab(tabId, reportName, '&nbsp;',false ,assessmentId, reportId);
			// show
			//$(".tabs a[href^='#"+tabId+"']").click();
			$("#"+tabId+"_"+tabId).click();
			
			$("#"+tabId+"").html(data);
			
			unblockUI();
			$("#"+tabId+"").css('position', 'relative');
			blockUI(tabId);
			
			//alert(reportContent);
			if($.browser.msie) {
				$('.icon-page-list').html('');
				$('.icon-leaf').html('');
				if($.template.ie7) {
					$('.download-button').addClass('download-button-ie');
					$(".report-btn").css("margin-right", "10px");
				}
			}
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error']);
			unblockUI();
		}
	});
}

function closeProgress(reportUrl, id, firstCall) {
	if(id == '_FRAME_ID_') $('#_LOAD_ID_').hide(100);
	
	else $('#loading'+id+'').hide(100);
	
	// show download buttons
	$(".download-button-"+id).show(100);
	$(".review-button-pdf-"+id).show(100);
	// change refresh button color
	$(".refreh-button-"+id).addClass('blue-gradient');
	$(".reportButton").removeTooltip();
	
	if(reportUrl != null && !firstCall) {
		checkpagination(reportUrl, id);
		if( 'none' == $(".icholder-"+id+"").css("display") )
			$(".report-filter-"+id+"").click();
	} else {
		if(reportUrl != null) checkpagination(reportUrl, 0);
		if( 'none' == $(".icholder-0").css("display") )
			$(".report-filter-0").click();
	}
	if(reportUrl == '/public/TASC/Reports/TASC_Org_Hier/Student_Roster_files' 
		|| reportUrl == '/public/TASC/Reports/TASC_Edu_Center/Student_Roster_files') {
		// code for bulk download button
		$("#BulkCandidateReportEdu").show(100);
		$("#BulkCandidateReport").show(100);
	}
	
	unblockUI();
	unblockUI('new-tab'+id+'');
	$("#report-iframe-"+id).contents().find('#blockDiv').remove();
}

//============================= Populate input controls =============================
function setInputControl(data, containerObj) {
	if (data && data != null) {
	$(containerObj).html(data.inputDom);
	// initialize date picker
	//$(containerObj).find('.datepicker').glDatePicker();
	$(containerObj).find('.jqdatepicker').datepicker({
		changeMonth: true,
	    changeYear: true
    });
	//$('.select').trigger();
	}
}
function setInputControlForStudentRoster(data, containerObj){

	$(containerObj).html(data.inputDom);
	//patch for rank order in Student Roster
	var selectedScores = $('#p_Roster_Score_List').val();
	var rankOrderDom="<option value='1Alpha-' >"+strings['option.p_Roster_Score_List.0']+"</option>";
$("select#p_Roster_Rank_Order option").each(function(index){
	var rankOrderName=$(this).text();
	var rankOrder=$(this).val();
	var rankOrderScore=rankOrder.substr(rankOrder.lastIndexOf("-")+1,rankOrder.length);
	for (var i = 0; i < selectedScores.length; i++){
		if (rankOrderScore==selectedScores[i]){
			rankOrderDom +="<option value='"+rankOrder+"' >"+rankOrderName+"</option>";
		}
	}
});
//alert(rankOrderDom);
$("select#p_Roster_Rank_Order").html(rankOrderDom);
}

//============================= Populate cascading input control values =============================
function getCascading(selectedObj) {
	//$(document).click(); // this code is to close multiselect dropdown
	//blockUI();
	var inputId = $(selectedObj).attr('id');
	if('p_Roster_Subtest_MultiSelect' == inputId || 'p_Roster_Score_List' == inputId /* School TN3 Roster*/
			|| 'p_PTCS_Subtest_MultiSelect' == inputId || 'p_PTCS_Roster_Score_School' == inputId /* School PTCS Roster*/
			|| 'p_Inview_Subtest_MultiSelect' == inputId || 'p_Inview_Roster_Score_School' == inputId /* School INVIEW Roster*/
			|| 'p_Bible_Roster_Score_Type' == inputId /* School BIBLE Roster*/
			|| 'p_Longitudinal_Roster_Subtest_MultiSelect'  == inputId /* School LONGITUDINAL Roster*/
			|| 'p_Score_Type_List'  == inputId /* Class - class dashboard*/
			|| 'p_PTCS_Roster'  == inputId /* Class - PTCS roster subtest */
			|| 'p_PTCS_Comb_Subtest_MultiSelect'  == inputId || 'p_PTCS_Comb_Score' == inputId /* Class - PTCS combination roster & summary dashboard */
			|| 'p_Inview_Subtest'  == inputId /* Class - InView roster subtest */
			|| 'p_Inview_Comb_Subtest_Multiselect'  == inputId || 'p_Inview_Comb_Score' == inputId /* Class - InView combination roster & summary dashboard */
			|| 'p_Subtest_Class_MultiSelect'  == inputId /* Class LONGITUDINAL Roster*/
			|| 'p_Bible_Roster_Score_Type'  == inputId ) { /* Class BIBLE Roster*/
		$(document).click(); // this code is to close multiselect dropdown
	}
	// code for bulk download button
	$("#BulkCandidateReportEdu").hide(100);
	$("#BulkCandidateReport").hide(100);
	
	var reportUrl = $(selectedObj).attr('param');
	var tabCount = $(selectedObj).attr('count');
	blockUI('filterHolder-'+tabCount+'');
	var assessmentId = $(selectedObj).attr('assessment');
	var dataURL = 'reportUrl='+reportUrl+'&changedObj='+inputId+'&changedValue='+$(selectedObj).val()+'&count='+tabCount+'&assessmentId='+assessmentId;
	
	var reportForm = $('.report-form');
	var formObj = $(".report-form-"+tabCount); //reportForm.get(tabCount);
	dataURL = dataURL + "&" + $(formObj).serialize();
	// hide download buttons
	$(".download-button-"+tabCount).hide(100);
	$(".review-button-pdf-"+tabCount).hide(100);
	
	// disable download buttons
	$('#report-iframe-'+tabCount).contents().find('#downloadCombinedPdfsMAP').addClass('disabled');
	$('#report-iframe-'+tabCount).contents().find('#downloadSeparatePdfsMAP').addClass('disabled');
	$('#report-iframe-'+tabCount).contents().find('#downloadSeparatePdfsGD').addClass('disabled');
	$('#report-iframe-'+tabCount).contents().find('#downloadCombinedPdfsGD').addClass('disabled');
	if($('#report-iframe-'+tabCount).contents().find('#downloadDisable').length == 0 ) {
		$('<p class="message red icon-warning" id="downloadDisable" style="margin-top:50px"><b>Attention:</b> One of the Filter Options has been changed. Please click the <span class="green-bg">Refresh</span> button at the top of the page to see the updated student selection and enable the Generate Download File PDFs buttons.</p>').insertAfter( $('#report-iframe-'+tabCount).contents().find('#downloadCombinedPdfsMAP') );
		$('<p class="message red icon-warning" id="downloadDisable" style="margin-top:50px"><b>Attention:</b> One of the Filter Options has been changed. Please click the <span class="green-bg">Refresh</span> button at the top of the page to see the updated student selection and enable the Generate Download File PDFs buttons.</p>').insertAfter( $('#report-iframe-'+tabCount).contents().find('#downloadCombinedPdfsGD') );
	}
	
	
	// change refresh report button color
	$(".refreh-button-"+tabCount).removeClass('blue-gradient').addClass('green-gradient');
	// show tooltip on refresh button
	$(".refreh-button-"+tabCount).tooltip('Click <strong>here</strong> to get filtered data', {delay:300, classes: ['orange-gradient', 'with-padding']});
	
	if($(selectedObj).val() != null) {
	$.ajax({
		type : "POST",
		url : 'checkCascading.do',
		data : dataURL,
		dataType: 'html',
		cache:false,
		success : function(data) {
			//alert(data);
			var obj = jQuery.parseJSON(data);
			if(obj.status == 'Success') {
				var dom = jQuery.parseJSON(obj.dom);
				$.each(dom, function(index, value) { 
					var objectVal = this.objectValue;
					$(formObj).find( $("select[rel^='"+objectVal.name+"']") ).empty().append(objectVal.value);
					$(formObj).find( $("select[rel^='"+objectVal.name+"']") ).change();
					$(formObj).find( $("select[rel^='"+objectVal.name+"']") ).trigger('update-select-list');
					//patch for Multi-Select Student Roster
					if (objectVal.name=="p_Roster_Subtest3"){
						$("input#p_Subtest3_MinScore").val("");
						$("input#p_Subtest3_MaxScore").val("");
					}
					if(objectVal.value == "") {
						unblockUI('filterHolder-'+tabCount+'');
					}
				});
			} else {
				//unblockUI();
				unblockUI('filterHolder-'+tabCount+'');
			}
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error']);
			//unblockUI();
			unblockUI('filterHolder-'+tabCount+'');
		}
	});
	}
}

function patchForIE() {
	$(".icholder").toggleClass('display-none');
}

//============================= Download Reports =============================
function downloadXls(count, reportUrl, assessmentId, event) {
	//event.preventDefault();
	//event.stopPropagation();
	download(count, reportUrl, assessmentId, 'xls');
}

function downloadPdf(count, reportUrl, assessmentId, event) {
	//event.preventDefault();
	//event.stopPropagation();
	download(count, reportUrl, assessmentId, 'pdf');
}



function download(count, reportUrl, assessmentId, type) {
	var reportForm = $('.report-form');
	//var formObj = reportForm.get(count);
	var formObj = $(".report-form-"+count);
	var allInputContainer = $('.inputControlContailer');
	var containerObj = $('.inputControlContailer-'+count); //allInputContainer.get(count);
	var dataUrl = '';
	if($(containerObj).html().indexOf('loading64.gif') != -1) {
		dataUrl = '&filter=false';
	} else {
		dataUrl = $(formObj).serialize() + '&filter=true';
	}
	if(type == 'xls') {
		window.open('download.do'+'?type='+type+'&reportUrl='+reportUrl+'&assessmentId='+assessmentId+'&token=0&'+dataUrl);
	} else {
		window.open('download.do'+'?type='+type+'&reportUrl='+reportUrl+'&assessmentId='+assessmentId+'&token=0&'+dataUrl);
	}
	
}

//Fix for TD 77743 - By Joy
function moreInfo(reportId){
	var productId = $('#p_test_administration').val();
	$.modal({
		url: 'reportMoreInfo.do?reportId='+reportId+'&productId='+productId,
		useIframe: true,
		title: 'More Info',
		height: 650,
		width: 1000,
		resizable: true,
		draggable: true
	});
}

function getfaq()
{
	$.modal({
		url: 'https://inorsredteamtest.turnleaf.com/FAQ.aspx',
		useIframe: true,
		title: 'FAQ',
		height: 650,
		width: 1000,
		resizable: true,
		draggable: true
	});
}

function getHelp()
{
	$.modal({
		url: 'http://help.turnleaf.com/INORS/wf_flashinstall.htm',
		useIframe: true,
		title: 'HELP',
		height: 650,
		width: 1000,
		resizable: true,
		draggable: true
	});
}

/**
 * Hides the Hot Spot Scroll bars for a particular reportUrl.
 * 
 * @param reportUrl
 */
function hideScrollingHotSpot(reportUrl) {
	if (reportUrl == "") { // Home page Url
		var scrollingHotSpotLeft = $('.scrollingHotSpotLeft');
		if (scrollingHotSpotLeft) {
			$('.scrollingHotSpotLeft').removeClass("scrollingHotSpotLeft");
		}
		var scrollingHotSpotRight = $('.scrollingHotSpotRight');
		if (scrollingHotSpotRight) {
			$('.scrollingHotSpotRight').removeClass("scrollingHotSpotRight");
		}
	}
}