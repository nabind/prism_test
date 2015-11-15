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
/**
 * This js file is to manage report module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
$(window).load(function() {
	$("#blockDiv").remove();
});
$(document).ready(function() {

	 //enabling the validation of the report addition modal
	 $("#addNewReport").validationEngine({promptPosition : "centerRight", scroll: false});
	 $("#editReportForm").validationEngine({promptPosition : "centerRight", scroll: false});
	 
	// CKEditor
	 //This block effects other text area throughout the application - give js error
	 /*
	 $('textarea').each(function(){
		CKEDITOR.inline( $(this).attr('id') );
	});*/
	
	 // edit report in Manager Reports screen
	if($('.edit-report').length > 0) {
		$('#report-list').tablesorter({
			headers: {
				4: { sorter: false }
			},
			sortList: [[0,1]]
		});
		
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-dashboard").parent().addClass("current");
		
		$('.edit-report').live("click", function() {
			openModal($(this).attr('reportid'));
		});
		
		
		$('#addDashboard').live("click", function() {
		resetAddReportModal($("#addNewReport"),"addNewReport","reportStatusCheck");
		openAddReportModal($("#addNewReport"));
		});
	}
	
	if($('.edit-actions').length > 0) {
		$('.edit-actions').live("click", function() {
			openModalForEditActions($(this).attr('reportid'));
		});
	}
	
	$("#productForAction").on("change", function(event) {
		populateActionsForEditAction();
		$("#newAction").change();
	});
	
	$("#newAction").on("change", function(event) {
		$("#actionAccessTable").html('');
		drawActionAccessMatrix();
	});

});
// *********** END DOCUMENT.READY ************

function populateActionsForEditAction() {
	$("#newAction").html('');
	var reportId = $("#reportIdForAction").val();
	var custProdId = $("#productForAction").val();
	var param = "reportId=" + reportId + "&custProdId=" + custProdId;
	blockUI();
	$.ajax({
		type : "GET",
		url : "getActionsForEditActions.do",
		data : param,
		dataType : 'json',
		cache : false,
		async: false,
		success : function(data) {
			unblockUI();
			populateSingleSelectedOptionsFromObjectValueTO("newAction", data, "actionId", "actionName");
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	});
}

function populateSingleSelectedOptionsFromObjectValueTO(dropdownId, list, key, value) {
	var innerHtml = "";
	var selectCount = 0;
	if(typeof list != "undefined") {
		$.each(list, function(index, item) {
			innerHtml = innerHtml + '<option value="' + item[key] + '"';
			if(selectCount < 1) {
				innerHtml = innerHtml + ' selected="true"';
			}
			innerHtml = innerHtml + '>' + item[value] + '</option>';
			selectCount = selectCount + 1;
		});
	}
	$("#"+dropdownId).html(innerHtml);
}

function openModalForEditActions(reportId) {
	$(".error-message").attr('style', 'width: 342px; display: none;');
    populateProductsForEditActions(reportId);
	populateActionsForEditAction();
	drawActionAccessMatrix();
	drawModalForEditActions();
}

function populateProductsForEditActions(reportId) {
	var param = "reportId=" + reportId;	
    blockUI();
    $.ajax({
		type : "GET",
		url : "getProductsForEditActions.do",
		data : param,
		dataType : 'json',
		cache:false,
		async:false,
		success : function(data) {
			unblockUI();
			$("input#reportIdForAction").val(data[0].reportId);
			$("input#reportNameForAction").val(data[0].reportName);
			populateSingleSelectedOptionsFromObjectValueTO("productForAction", data, "custProdId", "productName");
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	});
}

function drawModalForEditActions() {
	$("#actionAccessTable").html('');
	$("#editActionsForm").modal({
		title: 'Edit Actions',
		//height: 235,
		//width: 400,
		minWidth: 360,
		resizable: true,
		draggable: true,
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
					clickMe(e);
					win.closeModal(); 
				}
			},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left large-margin-right',
				click: function(win,e) {
					clickMe(e);
					updateActionsDetails($(".edit-actions-form"), win);
				}
			}
		},
		onOpen: function(){
			// $("input#newAction").change();
			// $("input#newAction").trigger('update-select-list');		
		}
	});
}

function drawActionAccessMatrix() {
	var reportId = $("#reportIdForAction").val();
	var custProdId = $("#productForAction").val();
	var actionId = $("#newAction").val();
	if(actionId == null || actionId == "null"){
		actionId = "0";
	}
	var param = "reportId=" + reportId + "&custProdId=" + custProdId + "&actionId=" + actionId;
	blockUI();
	$.ajax({
		type : "GET",
		url : "getActionAccess.do",
		data : param,
		dataType : 'json',
		cache : false,
		success : function(data) {
			unblockUI();
			drawActionAccessMatrixTable(data);
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	});
}

function drawActionAccessMatrixTable(data) {
	var roleMap = makeMapFromData(data, "roleId", "roleName");
	var levelMap = makeMapFromData(data, "levelId", "levelName");
	var tableHtml = "";
	var headerHtml = "";
	$.each(roleMap, function(keyR, valueR) {
		headerHtml += '<th>' + valueR + '</th>';
	});
	if(headerHtml.length > 0) {
		headerHtml = '<tr><th>&nbsp;</th>' + headerHtml + '</tr>';
		tableHtml += headerHtml;
	}
	var bodyHtml = "";
	$.each(levelMap, function(keyL, valueL) {
		bodyHtml += '<tr><td>'+valueL+'</td>';
		$.each(roleMap, function(keyR, valueR) {
			bodyHtml += '<td>' + getActionAccessStatusCheckBox(data, keyR, keyL) + '</td>';
		});
		bodyHtml += '</tr>';
	});
	if(bodyHtml.length > 0) {
		tableHtml += bodyHtml;
	}
	$("#actionAccessTable").html(tableHtml);
}

function getActionAccessStatusCheckBox(data, keyR, keyL) {
	var checkBox = '<input type="checkbox" name="actionAccess" id="actionAccess" value="'+keyR + '-' + keyL+'">';
	$.each(data, function(index, item) {
		if(item["roleId"] == keyR && item["levelId"] == keyL && item["activationStatus"] == "AC") {
			checkBox = '<input type="checkbox" name="actionAccess" id="actionAccess" value="'+keyR + '-' + keyL+'" checked>';
		}
	});
	return checkBox;
}

function makeMapFromData(data, key, value) {
	var map = new Object();
	$.each(data, function(index, item) {
		map[item[key]] = item[value];
	});
	return map;
}

function updateActionsDetails(form, win) {
	var formData = form.serialize();
	var reportId = $("#reportIdForAction").val();
	var custProdId = $("#productForAction").val();
	var actionId = $("#newAction").val();
	if(actionId == null || actionId == "null") {
		$.modal.alert(strings['script.report.noActionFound']);
	} else {
		var roleIdLevelId = $("input[name=actionAccess]:checked").map(function () {return this.value;}).get().join(",");
		var param = "reportIdForAction=" + reportId + "&productForAction=" + custProdId + "&newAction=" + actionId + "&roleIdLevelId=" + roleIdLevelId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'updateActions.do',
			data : param,
			dataType: 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				win.closeModal(); 
				if(data.status == 'Success') {
					$.modal.alert(strings['script.report.actionsSavedSuccessfully']);
				} else {
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}
}

//===================== Manage Report screen ===========================
//----------------------Edit Report-------------------------------
 
function openModal(reportId) {
var row = $("#"+reportId + '_' +reportId);
    manageIconIE('icon-star');
    var param = "reportId=" + reportId;	
    blockUI();
    $.ajax({
		type : "GET",
		url : "getEditDataForReport.do",
		data : param,
		dataType : 'json',
		cache:false,
		success : function(data) {
			unblockUI();
			$("input#reportId").val(data[0].reportId);
			$("input#reportName").val(data[0].reportName);
			$("input#reportDescription").val(data[0].reportDescription);
			$("input#reportUrl").val(data[0].reportOriginalUrl);
			//$("input#menuType").val(data[0].menuType);
			$("input#reportType").val(data[0].reportType);
			$("input#reportSequence").val(data[0].reportSequence);
			$("input#customerType").val(data[0].customerType);
			$("input#reportStatus").val(data[0].reportStatus);
			//$("input#userRole").val(data[0].userRole);
			if(data[0].enabled == true) {
				$("input#editReportStatus").attr('checked', true);
			} else {
				$("input#editReportStatus").removeAttr('checked');
			}
			$("input#editReportStatus").change();
			/*var menuType = data[0].menuId;
			if(typeof menuType != "undefined") {
				$.each(menuType, function(index, value) {
					$("#editMenuType option").each(function() {				
						if($(this).val() == menuType) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			}*/
			
			var menuType = data[0].menuId;
			if(typeof menuType != "undefined") {
				$.each(menuType, function(index, value) {
					$("#editMenuType option").each(function() {				
						if($(this).val() == menuType) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#editMenuType option").change();
				$("#editMenuType option").trigger('update-select-list');
			}
			
			var reportType = data[0].reportType;
			if(typeof reportType != "undefined") {
				$.each(reportType, function(index, value) {
					$("#editReportType option").each(function() {				
						if($(this).val() == reportType) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			}
			var customerProductArr = data[0].customerProductArr;
			if(typeof customerProductArr != "undefined") {
				$.each(customerProductArr, function(index, value) {
					$("#editCustomerType option").each(function() {				
						if($(this).val() == customerProductArr[index]) {
							$(this).attr('selected', 'true');
						}
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#editCustomerType option").change();
				$("#editCustomerType option").trigger('update-select-list');
			}

			var roles = data[0].roles;
			if(typeof roles != "undefined") {
				$.each(roles, function(index, value) {
					$("#userRole option").each(function() {				
						if($(this).val() == roles[index]) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#userRole option").change();
				$("#userRole option").trigger('update-select-list');
			}
			$("#orgNodeLevel option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			});
			var nodeLevels = data[0].orgNodeLevelArr;
			if(typeof nodeLevels != "undefined") {
				$.each(nodeLevels, function(index, value) {
					$("#orgNodeLevel option").each(function() {
						if(nodeLevels[index].match("^" + $(this).text())) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#orgNodeLevel option").change();
				$("#orgNodeLevel option").trigger('update-select-list');
			}
			
			$("#editReportForm").modal({
				title: strings['msg.editReport'],
				height: 470,
				width: 440,
				resizable: false,
				draggable: false,
				buttons: {
					'Cancel': {
						classes: 'glossy mid-margin-left',
						click: function(win,e) {
							$("#editReportForm").validationEngine('hide');
							clickMe(e);
							win.closeModal(); 
						}
					},
					'Save' : {
						classes : 'blue-gradient glossy mid-margin-left',
						click : function(win, e) {
							clickMe(e);
							if ($("#editReportForm").validationEngine('validate')) {
								$("#editReportForm").validationEngine('hide');
								updateReportDetails($(".edit-report-form"), win, row);
								var editActionsButton = '<a href="#" reportId="' + data[0].reportId + '" reportName="' + data[0].reportName + '" class="button icon-swap with-tooltip confirm edit-actions" title="' + strings['msg.editActions'] + '"></a>';
								var deleteReportButton = '<a href="#" reportId="' + data[0].reportId + '" reportName="' + data[0].reportName + '" class="button icon-trash with-tooltip confirm delete-Report" title="' + strings['label.delete'] + '"></a>';
								row.find('.edit-actions').remove();
								row.find('.delete-Report').remove();
								var reportTypeSelected = $("#editReportType").val();
								var reportName = $("#reportName").val();
								if (reportTypeSelected == 'API_LINK' || reportTypeSelected == 'API_CUSTOM' 
									|| reportName == 'Group Download Files'
									|| reportName == 'Student Data File' ) {
									row.find('.button-group').append(editActionsButton);
								} else {
									row.find('.button-group').append(deleteReportButton);
								}
							}
						}
					}
				},
				onOpen: function(){
					$("input#userRole").change();
					$("input#userRole").trigger('update-select-list');		
				}
			});		
			if(reportType == 'API_LINK' || reportType == 'API_CUSTOM') {
				$("#editReportType").parent().removeClass("disabled");
				$("input#reportName").removeClass("disabled");
				$("input#reportName").live("click",function(e) {
					$("input#reportName").focus();
				});
			} else {
				$("#editReportType").parent().addClass("disabled");
				$("input#reportName").addClass("disabled");
				$("input#reportName").live("click",function(e) {
					$("input#reportName").blur();
				});
			}
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	})	
}

	
//==================================CLEAN THE ROLES DROPDOWN ON CLICK====================
	function clearRolesOption(roles)
	{
	    //$.each(roles, function(index, value) { 
			$("#editUserRole option").each(function() {
				
				$(this).removeAttr('selected');
				
				$(this).change();
				$(this).trigger('update-select-list');
			});
			$("#addUserRole option").each(function() {
				
				$(this).removeAttr('selected');
				
				$(this).change();
				$(this).trigger('update-select-list');
			});
			
		//});
	}
	
//==================CLEAN THE ORG LEVEL OPTION WHIOLE OPENING======================
	function clearOrgLevelOption(){
		$('#addReport #allOrgNode').val([]);
	}

	function clearReportTypeOption(){		
		$('#addReport #reportType').val([]);
	}
	
	function clearCustomerProdOption(){
		$('#addReport #customerType').val([]);
	}
	
	function clearMenuOption(){
		$('#addReport #menuType').val([]);
	}
	
//==================================CLEAN THE  DROPDOWN AND TEXT FIELDS WHILE OPENING====================
			function resetAllFields(addFormObj){
			
			addFormObj.find("#reportType option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			 });
			 addFormObj.find("#assessmentType option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			 });
			 setDefaultValuesForAddReportModal();
			}	

//----------------------------Update report details---------------------
function updateReportDetails(form, win, row) {
	//alert($(form).serialize());
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateReport.do',
		data : form.serialize(),
		dataType: 'html',
		cache:false,
		success : function(data) {
			var obj = jQuery.parseJSON(data);
			win.closeModal(); 
			if(obj.status == 'Success') {
				$.modal.alert(strings['script.manageReport.update']);
				updateRowValuesForRole(row);
				enableSorting(true);
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
			unblockUI();
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError']);
		}
	});
}
//----------------------------Update row if update success ---------------------
var tag = '<small class="tag _BGCOLOR_ _CLASS_ ">_VALUE_</small>';
function updateRowValuesForRole(row) {
	var statusTag = tag;
	var roleTag = '';
	var statusClass = 'status';
	var statusVal = '';
	
	if($("#editReportStatus").attr('checked') ==  true || $("#editReportStatus").attr('checked') == 'checked' ) {
		statusClass = statusClass + ' green-bg';
		statusVal = 'Enabled';
	} else {
		statusClass = statusClass + ' red-bg';
		statusVal = 'Disabled';
	}
	var statusTag = statusTag.replace(/_CLASS_/g, statusClass);
	statusTag = statusTag.replace(/_VALUE_/g, statusVal);
	
	$("#userRole option").each(function() {
		if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
			var roleClass = 'role' + ' ' + $(this).val();
			var roleTagTemp = tag +'<br/>';
			roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
			roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).val());
			
			if($(this).val()=="ROLE_USER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "black-bg");
			else if ($(this).val()=="ROLE_CTB")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "green-bg");
			else if ($(this).val()=="ROLE_SUPER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "blue-bg");
			else if ($(this).val()=="ROLE_ADMIN")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
			else if ($(this).val()=="ROLE_EDU_ADMIN")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
			else if ($(this).val()=="ROLE_GRW")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "grey-bg");
			else ($(this).val()=="ROLE_PARENT")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "red-bg");	
			
				
			roleTag = roleTag + roleTagTemp;
			//alert(roleTag);
		}
	});
	
	/*
	 * Fix for TD 79043 - By Joy
	 * As the method updateRowValuesForRole() called only from updateReportDetails() so
	 * we use $("#editReportForm #reportName").val() to get edited report name
	 * */
	row.find('.reportName').html( $("#editReportForm #reportName").val() );
	
	row.find('.reportUrl').html( $("#reportUrl").val() );
	row.find('.status').parent().html(statusTag);
	row.find('.roleContainer').html(roleTag);
	row.find('.menu_name').html($("#editMenuType option:selected'").html());
	row.find('.menuid').val($("#editMenuType").val());
}

//=============================DELETE REPORT ON CLICK======================
$('.delete-Report').live("click", function() {

    var row = $(this);
	var reportId = $(this).attr("reportId");
	var reportName = $(this).attr("reportName");
	$.modal.confirm(strings['msg.confirm.delete'],
		function () {
			deleteReportDetails(reportId, reportName,row);
		},function() {//this function closes the confirm modal on clicking cancel button
		} 
	);
	
});	
//=============================AJAX CALL TO DELETE REPORTS FROM DB TABLES=============================
	function deleteReportDetails(reportId, reportName,row) {
			blockUI();
			$.ajax({
				type : "GET",
				url : 'deleteReport.do',
				data : "reportId="+reportId,
				dataType: 'html',
				cache:false,
				success : function(data) {
					enableSorting(true);
					var obj = jQuery.parseJSON(data);
					//win.closeModal(); 
					if(obj.status == 'Success') {
						$.modal.alert(strings['msg.reportDeletedSuccessfully']);				
						deleteRowValues(row);//this method is present in manageUser.js
					} else {
						$.modal.alert(strings['script.user.deleteError']);
					}
					unblockUI();
				},
				error : function(data) {
					$.modal.alert(strings['script.user.deleteError']);
					unblockUI();
				}
			});
		}
	
	
//====================================OPEN REPORT MODAL==========================
	function openAddReportModal(addFormObj){
		manageIconIE('icon-star');
		clearRolesOption();
		clearOrgLevelOption();
		clearReportTypeOption();
		clearCustomerProdOption();
		clearMenuOption();
		resetAllFields(addFormObj);
		$("#reportStatusCheck").attr('checked', true);
		$("#reportStatusCheck").change();
		$("#addReport").modal({
			title: strings['msg.addReport'],
			height:470,
			width:440,
			draggable: false,
			buttons: {
				'Cancel': {
					classes: 'glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						addFormObj.validationEngine('hide');
						win.closeModal(); 
					}
				},
				'Save': {
					classes: 'blue-gradient glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						if (addFormObj.validationEngine('validate')){
							addFormObj.validationEngine('hide');
							if(addFormObj.find("#reportStatusCheck").attr("checked")=="checked" || addFormObj.find("#reportStatusCheck").attr("checked")==true){
								addFormObj.find("input#reportStatus").val(addFormObj.find("#reportStatusCheck").val());
							}
						//addFormObj.find("input#reportStatus").val(addFormObj.find("#reportStatusCheck").val());
							addDashboard(addFormObj,win);
						}
					}
				}
			}
		});	
	}
	function setDefaultValuesForAddReportModal() {
		$('#reportType option:first-child').attr("selected", "selected");
		$('#menuType option:first-child').attr("selected", "selected");
		$('#customerType option:first-child').attr("selected", "selected");
		$("#addUserRole option[value='ROLE_USER']").attr("selected", "selected");
		$('#allOrgNode option:first-child').attr("selected", "selected");
	}
	/*var obj = jQuery.parseJSON(data);
					
					if(obj.status == 'Success') {
						$.modal.alert('Report added successfully');				
						insertNewDasboardRow(row);
					} else {
						$.modal.alert('Some error occured while deleting. Please try after some time.');
					}*/
	function addDashboard(addFormObj,win)
	{
		blockUI();
		$.ajax({
				type : "POST",
				url : 'addDashboard.do',
				data :addFormObj.serialize(),
				dataType: 'json',
				cache:false,
				success : function(data) {
					if (data!=null){
					
						if(data.status =="Faliure"){
							$.modal.alert(strings['script.manageReport.exists']);
						}
						else {
								win.closeModal();
								$.modal.alert(strings['script.manageReport.addSuccess']);
								unblockUI();
								if (insertNewDasboardRow(data)){
									enableSorting(true);
								}
						}					
					}
					unblockUI();
					
				},
				error : function(data) {
					$.modal.alert(strings['script.user.deleteError']);
					unblockUI();
				}
			});
	}
	
	
	
	//===================================CREATES THE ROW FOR THE NEW REPORT ADDED=====================
		function insertNewDasboardRow(jsonData){
			var mngRpt_editRpt = $('#mngRpt_editRpt').val();
			var mngRpt_configureRptMsg = $('#mngRpt_configureRptMsg').val();
			var mngRpt_editActions = $('#mngRpt_editActions').val();
			var mngRpt_deleteRpt = $('#mngRpt_deleteRpt').val();
			var reportContent="";	
			$.each(jsonData, function () { 
		    
				//Fix for TD 78098 - By Joy
				reportContent += '<tr id='+this.reportId+'_'+this.reportId+'>'
								+'<th scope="row"><span class="reportName">'+this.reportName+'</span><br>'
								+'<small class="reportUrl">'+this.reportUrl+'</small></th>'
								+'<td class="vertical-center">'
								+'<input type="hidden" class="reportDescription" name="reportDescription" value="'+this.reportDescription+'">'
								+'<input type="hidden" class="reportType" name="reportType" id="reportType" value="'+this.reportType+'"> '
								+'<input type="hidden" class="allOrgNode" name="allOrgNode" id="allOrgNode" value="'+this.allOrgNode+'"> '
								+makeStatusDom(this.enabled)
								+'</td>'
								+'<td class="roleContainer vertical-center">'
								+makeRoleDom(this.roles)
								+'</td>'
								+'<td class="vertical-center">'
								+'<h5>'+this.menuName+'</h5>'
								+'</td>'
								+'<td class="vertical-center">'
								+'<span class="button-group compact">';
				
								if(mngRpt_editRpt == 'true'){
									reportContent += '<a href="#nogo" class="button icon-pencil edit-report with-tooltip" title="Edit" reportId="'+this.reportId+'"></a>';
								}
								if(mngRpt_configureRptMsg == 'true'){
									reportContent += '<a href="getReportMessageFilter.do?reportId='+this.reportId+'&reportName='+this.reportName+'&reportUrl='+this.reportUrl+'" class="button icon-chat configure-report-message with-tooltip" title="'+strings['msg.configureMassage']+'"></a>';
								}
								if(mngRpt_editActions == 'true'){
									if(this.reportType == 'API_LINK' || this.reportType == 'API_CUSTOM')
									reportContent += '<a href="#" reportId="'+this.reportId+'" reportName="'+this.reportName+'" class="button icon-swap with-tooltip confirm edit-actions" title="'+strings['msg.editActions']+'"></a>';
								}
								if(mngRpt_deleteRpt == 'true'){
									if(this.reportType != 'API_LINK' && this.reportType != 'API_CUSTOM')
									reportContent += '<a href="#" reportId="'+this.reportId+'" reportName="'+this.reportName+'" class="button icon-trash with-tooltip confirm delete-Report" title="'+strings['label.delete']+'"></a>';
								}	
								
								reportContent +='</span>'
												+'</td>'
												+'</tr>';
				});
			$("#reportDetails").append(reportContent);
			return true;
		}
		
		function makeStatusDom(status)
		{
			if (status==false)
			return '<small class="tag red-bg status">Disabled</small>'
			else
			return '<small class="tag green-bg status">Enabled</small>'
		}
		
		function makeRoleDom(rolesArray)
		{
			var reportRoleDom="";
			for(var i = 0; i < rolesArray.length; i++) {
				if (rolesArray[i]=="ROLE_CTB"){
					reportRoleDom +='<small class="tag green-bg role ROLE_CTB">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_SUPER"){
					reportRoleDom +='<small class="tag blue-bg role ROLE_SUPER">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_PARENT"){
					reportRoleDom +='<small class="tag red-bg role ROLE_PARENT">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_ADMIN"){
					reportRoleDom +='<small class="tag orange-bg role ROLE_ADMIN">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_GRW"){
					reportRoleDom +='<small class="tag grey-bg role ROLE_GRW">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_USER"){
					reportRoleDom +='<small class="tag black-bg role ROLE_USER">'+rolesArray[i]+'</small><br/>'
				}else {
					reportRoleDom +='<small class="tag red-bg role '+rolesArray[i]+'">'+rolesArray[i]+'</small><br/>'
				}
			}
			return reportRoleDom;
		}
		


function resetAddReportModal(modalFormIdObj, modalFormId, checkboxId) {
	$("input#reportName").removeClass("disabled");
	$("input#reportName").live("click", function(e) {
		$("input#reportName").focus();
	});
	$("#editReportType").removeClass("disabled");
	$("#editReportType").parent().removeClass("disabled");
	modalFormIdObj.find(".reset").val("");
	enableStaus(modalFormId, checkboxId);
}

/**
 * This js file is to manage report module
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {

	$('#searchMessage').live('click',function(){
		loadManageMessage();
	});
	
	$('#custProdId').live('change',function(){
		$('#reportMessage').empty();
		showHideCopyMessageDiv(0);
	}); 
	
	$('#editManageMessageButtonSave').live('click',function(){
		var paramObj = {};
		paramObj.custProdId = $('#custProdId').val();
		saveManageMessage(paramObj);
	});
	
	$('#copyMessage').live('click',function(){
		openCopyMessageModal();
	});
	
	// selecting left icon as current
	if($('.manage-message').length > 0) {
		// hiding right menu
		$('.clearfix').addClass('menu-hidden');
		
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-dashboard").parent().addClass("current");
	}

});
// *********** END DOCUMENT.READY ************

//===================================Manage Message Screen=====================
	function loadManageMessage(){
		var formObj = $('#reportMessageSearchForm').serialize();
		blockUI();
		$.ajax({
			type : "POST",
			url : 'ajaxJSP/loadManageMessage.do',
			data : formObj,
			dataType: 'html',
			cache:false,
			success : function(data) {
				$("#reportMessage").empty();
				$("#reportMessage").html(data);
				$('.manage-rpt-textarea').each(function(){
					CKEDITOR.inline( $(this).attr('id'), {
						allowedContent: true
					} );
				});
				showHideCopyMessageDiv(1);
				$(".switch.tiny").first().css("top", "32px");
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
	
	function saveManageMessage(paramObj){
		var reportId = $('#reportId').val();
		for(name in CKEDITOR.instances)	{
			var editorVal = CKEDITOR.instances[name].getData();
		    var manageMessageTOListNo = name.split("-")[2];
		    $('#hiddenEditor-'+reportId+'-'+manageMessageTOListNo).val(editorVal);
		}
		$('.custProdIdHidden').val(paramObj.custProdId);
		var formObj = $('#editManageMessageForm').serialize();
		
		blockUI();
		$.ajax({
			type : "POST",
			url : 'ajaxJSP/saveManageMessage.htm',
			data : formObj,
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				if(obj.status == "1"){
					$.modal.alert(strings['msg.savedSuccessfully']);
				}else{
					$.modal.alert(strings['script.common.error1']);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});
	}
	
	function openCopyMessageModal(){
		$("#copyMessageModal").modal({
			title: 'Copy Report Message',
			width:350,
			draggable: false,
			resizable: false,
			buttons: {
				'Cancel': {
					classes: 'glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						win.closeModal(); 
					}
				},
				'Save': {
					classes: 'blue-gradient glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						var paramObj = {};
						paramObj.custProdId = $('#custProdIdModal').val();
						saveManageMessage(paramObj);
					}
				}
			}
		});
	}
	
	function showHideCopyMessageDiv(param){
		if(param == 0){
			$('#copyMessageDiv').hide();
		}else if(param == 1){
			$('#copyMessageDiv').show();
		}
		
	}
	
	/**
	 * This js file is to manage user module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
$(window).load(function() {
	if($('#educationTab').val()!= "" && $('#educationTab').val()=="educationUserTab" ){
		$('.clearfix').addClass('menu-hidden');
   	    $("ul#shortcuts li").removeClass("current");
	    $(".shortcut-notes").parent().addClass("current");
		loadEduCenterUsers();
	}
});

$(document).ready(function() {
	if($('input#educationTab').val()!= null && $('input#educationTab').val()=="educationUserTab" ){
		$('.clearfix').addClass('menu-hidden');
   	    $("ul#shortcuts li").removeClass("current");
	    $(".shortcut-notes").parent().addClass("current");
	}
	
	$("#addNewUser").validationEngine({promptPosition : "centerRight", scroll: false});
	$("#editUser").validationEngine({promptPosition : "centerRight", scroll: false});
	 
	$('#treeViewForOrg').customScroll({
		horizontal : true,
		showOnHover : false,
		animate : false
	});

	$('.edit-User').live("click", function() {
		resetModalForm("editUser");
		resetModalForm("addNewUser");
		openUserModaltoEdit($(this).attr("id"),$(this).attr("tenantId"));
	});	
	$('#addUser').live("click", function() {
		
		if ($('#addUser').attr("orgLevel") == null || $('#addUser').attr("orgLevel") == "" ) {
			$.modal.alert(strings['script.user.adduser']);
		}
		else {
			resetModalForm("editUser");
			resetModalForm("addNewUser");
			if ($('#addUser').attr("orgLevel") == strings['user.not.added']) {
				$.modal.alert(strings['script.user.examinerUser']);
			}
			else {
				openUserModaltoAdd($('#addUser').attr("tenantId"),$('#addUser').attr("orgLevel") );
			}
		}
	});	
	$('.delete-User').live("click", function() {
	    var row = $(this);
		var userId = $(this).attr("id");
		var userName = $(this).attr("userName");
		$.modal.confirm(strings['confirm.deleteUser'],
			function () {
				deleteUserDetails(userId,userName, row);
				enableSorting(true);
			},function() {//this function closes the confirm modal on clicking cancel button
			} 
		);
		
	});
	
	if ($('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$(".login-as").live("click", function() {
			location.href = 'j_spring_security_switch_user?j_username=' + $(this).attr('param')+ '&isEdu=Y&theme=' + $(this).attr('themeName');
		});
	} else {
		$(".login-as").live("click", function() {
			location.href = 'j_spring_security_switch_user?j_username=' + $(this).attr('param')+ '&isEdu=N&theme=' + $(this).attr('themeName');
		});
	}
	
	
	//====================THIS METHOD POPULATES THE COUNTRY IN THE PROFILE PAGE=============
	$('#tabContactDetails').live("click",function(e){
		e.stopImmediatePropagation();
		var userCountry= $(".addressContainer #userCountry").val();
		$("#countryList option").each(function(){
			if($(this).val()==userCountry){
				$(this).addClass("selected");
				$(this).parent().siblings(".select-value").text(userCountry);
			}
					
		});
		
	});
	//====================THIS METHOD POPULATES THE COUNTRY IN THE FIRST TIME LOGIN PAGE (FOR CHANGE PASSWORD)=============
	var isfirst=true;
	$(".passwordContainer #password").live("focus",function(e){
			e.stopImmediatePropagation();
		var userCountry= $(".addressContainer #userCountry").val();
		if(isfirst){
						$("#countryList option").each(function(){
						if($(this).val()==userCountry){
							$(this).addClass("selected");
							$(this).parent().siblings(".select-value").text(userCountry);
						}
								
					});
					
					$("span.drop-down span").each(function(){
						if($(this).text()==userCountry){
							$(this).addClass("selected");
							}
					});
		         isfirst=false;
		}
		
		
	});
	//====================THIS METHOD POPULATES THE HIDDEN "countyrList" TEXT FIELD WITH THE VALUE OF THE COUNTRY SELECTED FROM THE DROPDOWN=============
	
	$("select#countryList").live("change",function(e){
			e.stopImmediatePropagation();
			$(".addressContainer #userCountry").val($(this).siblings(".select-value").text());	
          //alert($(".addressContainer #userCountry").val());			
	});
	
	$('#eduCenterId').live('change',function(){
		$("#eduCenterUsersDetails").empty();
		loadEduCenterUsers();
	}); 
});

	//Changed for TD 77443 - By Joy
	function loginAs(event,$obj){
		if(!$obj.hasClass('disabled')){
			$obj.addClass('disabled');
			location.href = 'j_spring_security_switch_user?j_username='+$obj.attr('param') + '&theme=' + $obj.attr('themeName');
		}
	}
	//===================================Education Center User Details Screen=====================
	function loadEduCenterUsers(){
		var eduCenterId = $('#eduCenterId').val();
		$("#addUser").hide(100);
		if(eduCenterId != null) {
			$("#addUser").show(100);
			var eduCenterName = $("#eduCenterId :selected").text();
			$('#addUser').attr('tenantId',eduCenterId);
			$('#addUser').attr('tenantName',eduCenterName);
			var dataUrl = 'eduCenterId='+eduCenterId+'&eduCenterName='+eduCenterName+'&searchParam=""&lastEduCenterId_username=""';
			blockUI();
			
			$.ajax({
				type : "GET",
				url : 'loadEduCenterUsers.do',
				data : dataUrl,
				dataType: 'json',
				cache:false,
				success : function(data) {
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
					} else {
						$(".pagination").hide(200);
					}
					getUserDetails(true, data);
					enableSorting(true);
					$("tbody#user_details").removeClass("loader big");				
					if (data != null && data.length >= moreCount){
						$("#moreUser").removeClass("disabled");
						if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
					} else {
						$("#moreUser").addClass("disabled");
						if($.browser.msie) $("#moreUser").addClass("disabled-ie");
					}
					unblockUI();
				},
				error : function(data) {
					$.modal.alert(strings['script.common.error']);
					unblockUI();
				}
			});
			enableSorting(true);
		}
	}
	
	function openUserModaltoAdd(tenantId,orgLevel) {	
	    $("#addUserRole").html("");
		$("input#tenantId").val(tenantId);
		$("input#orgLevel").val(orgLevel);
		if($("#purpose").val()=="eduCenterUsers"){
			$("#addNewUser").validationEngine({promptPosition : "centerRight", scroll: false});
		}
		manageIconIE('icon-star');
		blockUI();
		$.ajax({
			type : "GET",
			url : "getRoleOnAddUser.do",
			data : 'orgLevel='+orgLevel+'&purpose='+ $("#purpose").val(),
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				createRoleListOnAdd(data, orgLevel);
				$("#addUserModal").modal({
					title: 'Add User to ' + $('#addUser').attr("tenantName"),
					height: 410,
					width: 370,
					resizable: false,
					draggable: false,
					onOpen: CheckUserNameAndEnableStaus(),//CheckUserNameAvailability("#addNewUser #userId"),
					actions: {
						'Close' : {
							color: 'red',
							click: function(win) { 
								$('#addNewUser').validationEngine('hide');
								$('#imgHolder').empty();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								win.closeModal(); 
							}
						}
					},
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
										$('#addNewUser').validationEngine('hide');
										$('#imgHolder').empty();
										if($.browser.msie) setTimeout("hideMessage()", 300);
										win.closeModal(); 
									}
								},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);	
									 if($("#addNewUser").validationEngine('validate') && ($("#addNewUser #imgHolder > #validated").hasClass("validated"))){
										$('#addNewUser').validationEngine('hide');
										//$('#imgHolder').empty();
										if($("input[rel^='userStatusCheck']").closest("span").hasClass("checked")){
											if ($("input[rel^='userStatusCheck']").val()=='on')
											$("input[rel^='userStatus']").val($("input[rel^='userStatusCheck']").val());
											else
											$("input[rel^='userStatus']").val('on');
										}	else{
											$("input[rel^='userStatus']").val('off');
										}
										addUserDetails($(".add-User-form"), win);
										$("#moreUser").removeClass("disabled");
										if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
									 }										
									}
								}
							}
					});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
			}
		});
				
	}
	
	function hideMessage() {
		$('#addNewUser').validationEngine('hide');
	}
	
	function CheckUserNameAndEnableStaus()
	{
			enableStaus("addUserModal","userStatusCheck");
			$("input[rel^='userStatus']").val($("input[rel^='userStatusCheck']").val());
			CheckUserNameAvailability("input[rel^='userId']");		
			if($.template.ie7) {$("#imgHolderContainer").css("line-height", "25px");}
	}
	
	//======================VALIDATION OF THE USER NAME FIELD IN ADD USER MODAL ====================================
	function validateUserId(modalFormId,userNameId) {
		$("#"+modalFormId+" "+"#"+userNameId).blur(function() {
		      var username = "username=" + $(this).val();
			  if($(this).val() != "") {
				  showLoadingImage();
				  $.ajax({
						type : "GET",	
						url : "regn/checkusername.do",
						data : username,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data.available == "true") {
								removeLoadingImage();//This method is defined in parent.js
								showAvailability();//This method is defined in parent.js
							}
							else if (data.available == "false") {
								removeLoadingImage();//This method is defined in parent.js
								showUnAvailability();//This method is defined in parent.js
							}
						},
						error : function(data) {						
							$.modal.alert(strings['script.common.error1']);
						}
				  });
			}/*
			else {
				$("span#imgHolder").html('<!--<img src="themes/acsi/img/standard/loaders/loading16.gif">-->');
				$("span#imgHolder").html('<span class="icon-user icon-size2 icon-red" style="color: red;">Please provide valid Username</span>');
			}*/
		});	
		
	}

	//======================ENABLES THE CHECKBOX IN MODAL=====================================
	function enableStaus(modalId,checkboxId)
	{
		$("#"+modalId+" "+"#"+checkboxId).closest("span").addClass("checked");
	}
	
	//======================OPEN EDIT USER SCREEN==========================================
	function openUserModaltoEdit(userId,tenantId) {
	var row = $("#"+tenantId + '_' +userId);
	var userId = "userId=" + userId+'&purpose='+ $("#purpose").val();	
	if($("#purpose").val()=="eduCenterUsers"){
		$("#editUser").validationEngine({promptPosition : "centerRight", scroll: false});
	}
	manageIconIE('icon-star');
	blockUI();
	$.ajax({
			type : "GET",
			url : "getEditUserData.do",
			data : userId,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				createRoleListOnEdit(data);
				$("input#Id").val(data[0].userId);
				$("input#userName").val(data[0].userDisplayName);
				$("input#userId").val(data[0].userName);
				$("label#userid").text(data[0].userName);
				$("input#validation-email").val(data[0].emailId);
				var roles = data[0].availableRoleList;
				
				$("#userRole option").removeAttr('selected');
				
				if(typeof roles != "undefined") {
					$.each(roles, function(index, value) {
						//alert(roles[index].roleName);
						$("#userRole option").each(function() {
							var flag = isFoundInList($(this).val(), data[0].masterRoleList)
							// if($(this).val() == roles[index].roleName) { // TODO : check logic
							if (flag == true) {
								$(this).attr('selected', 'true');
							} 
							$(this).change();
							$(this).trigger('update-select-list');
						});
					});
				} else {
					$("#userRole option").change();
					$("#userRole option").trigger('update-select-list');
				}
				
				$('#userRole').trigger('update-select-list').change();
				
				//$("input#userStatus").removeAttr('checked');
				if(data[0].status == 'AC') {
					$("input#userStatus").attr('checked', true);
				} else {
					$("input#userStatus").removeAttr('checked');
				}
				$("input#userStatus").change();
				//$("input#userName").val(this.userName);
				$("#userModal").modal({
					title: strings['msg.editUser'],
					height: 370,
					width: 370,
					resizable: false,
					draggable: false,
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								$('#editUser').validationEngine('hide');
								if($.browser.msie) setTimeout("hideMessage()", 300);
								clickMe(e);
								win.closeModal(); 
								
								}
						},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
								if ($("#editUser").validationEngine('validate')) {
									if ($("input[rel^='editPwd']").val() == $("input[rel^='editConfPwd']").val()) {
										$('#editUser').validationEngine('hide');
										updateUserDetails($(".edit-User-form"), win, row);
									} else {
										$.modal.alert(strings['script.user.passwordMismatch']);
									}
								}
							}						
						}
					}
				});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
			}
		})	
	}

	//ROLE_GRW user cannot be created/edited from Manage User - By Joy
	//Fix for TD 77811 - By Joy
	//======================CREATE MASTER ROLE LIST ON EDIT USER==========================================	
	function createRoleListOnEdit(data) {
		
	    $("#userRole").find("option").remove();
		var availableRole = "";
		$.each(data[0].availableRoleList, function(index, value){
			if(data[0].availableRoleList[index].roleName != 'ROLE_GRW'){
				if(data[0].availableRoleList[index].roleDescription != 'Class Admin User'){
					availableRole += '<option value="'+ data[0].availableRoleList[index].roleName +'" >' +data[0].availableRoleList[index].roleDescription+'</option>';
				}
			}
		});
		$("#userRole").append(availableRole);		
		$("#userRole").change(function(){
			$("#userRole option[value='ROLE_USER']").attr('selected', true);
		});
		//alert(masterRole);
	}
	
	//ROLE_GRW user cannot be created/edited from Manage User - By Joy
	//Fix for TD 77811 - By Joy
	//======================CREATE MASTER ROLE LIST ON ADD USER==========================================	
	function createRoleListOnAdd(data,orgLevel) {
	    var masterRole = "";
		$.each(data, function(index, value){
			if(data[index].roleName != 'ROLE_GRW'){
				if(orgLevel == '4'){
					if(data[index].roleName != 'ROLE_ADMIN'){
						masterRole += '<option value="'+ data[index].roleName +'" '+data[index].defaultSelection+'>' +data[index].roleDescription+'</option>';
					}
				}else{
					masterRole += '<option value="'+ data[index].roleName +'" '+data[index].defaultSelection+'>' +data[index].roleDescription+'</option>';
				}
			}
		});
		//alert(masterRole);
		$("#addUserRole").empty().append(masterRole);
		$("#addUserRole").change(function(){
			$("#addUserRole option[value='ROLE_USER']").attr('selected', true);
		});
		$('#addUserRole').trigger('update-select-list');
	}	
	
	//=========================SAVE EDIT USER DETAILS========================================
	function updateUserDetails(form, win, row) {
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateUser.do',
		data : form.serialize(),
		dataType : 'html',
		cache : false,
		success : function(data) {
			unblockUI();
			var obj = jQuery.parseJSON(data);
			if (obj.status == 'equalsUserName') {
				$.modal.alert(strings['script.user.passwordLikeUsername']);
			} else if (obj.status == 'invalidPwd') {
				$.modal.alert(strings['script.user.passwordPolicy']);
			} else if(obj.status == 'invalidPwdHistory') {
				$.modal.alert(strings['script.user.passwordPolicyHistory']);
			} else if (obj.status == 'LDAP_ERROR') {
				$.modal.alert(obj.message);
			} else if (obj.status == 'Success') {
				win.closeModal();
				$.modal.alert(strings['script.user.updateSuccess']);
				var purpose = $("#purpose").val();
				if (purpose == 'eduCenterUsers') {
					loadEduCenterUsers();
				} else {
					//Uncommented updateRowValues(row) for TD 77428 - By Joy
					updateRowValues(row);
					//Added to load data after edit
					var id = $("#treeViewForOrg a").parent().attr("id");
					//fetchAllUsers(id,"getUserDetails.do");//Blocked as after search and edit the user should retain there
					//Change end here
				}
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError']);
			}
		});
	}
		
		//=======================DELETE USER DETAILS====================
	function deleteUserDetails(userId, userName, row) {
	
		$.ajax({
			type : "POST",
			url : 'deleteUser.do',
			data : 'Id='+userId+'&userName='+userName+'&purpose='+ $("#purpose").val()+"&CSRFToken=" + $( "input[name='CSRFToken']" ).val(),
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				//win.closeModal(); 
				if(obj.status == 'Success') {
					$.modal.alert(strings['script.user.deleteSuccess']);				
					deleteRowValues(row);
				} else {
					$.modal.alert(strings['script.user.deleteError']);
				}
			},
			error : function(data) {
				$.modal.alert(strings['script.user.deleteError']);
			}
		});
	}
	
	//-------------------------------------ADDING USER DETAILS--------------------------------
	function addUserDetails(form, win) {
		blockUI();
		var patt=/^(\d+.*|-\d+.*)/g;
		var username = $("input[rel^=userId]").val();
		var pwd = $("#password1").val();
		if(pwd.indexOf(username) != -1) {
			unblockUI();
			$.modal.alert(strings['script.user.passwordPartUsername']);
		} 
		/*else if (patt.test(username)){
			unblockUI();
			$.modal.alert(strings['script.user.useridStartNumber']);
		}*/ else {
			var custProdId = $('#AdminYear').val();
			if(custProdId == null || custProdId == "null"){
				custProdId = "0";
			}
			var dataUrl = form.serialize()+'&AdminYear='+custProdId+'&purpose='+ $("#purpose").val()+'&eduCenterId='+ $("#eduCenterId").val();
			//alert(dataUrl);
		$.ajax({
			type : "POST",
			url : 'addNewUser.do',
			data : dataUrl,
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				if (obj.status == 'equalsUserName')	{
					$.modal.alert(strings['script.user.passwordLikeUsername']);
					unblockUI();
				}
				else if(obj.status == 'invalidPwd'){
					$.modal.alert(strings['script.user.passwordPolicy']);
					unblockUI();
				}
				else if(obj.status == 'invalidPwdHistory') {
					$.modal.alert(strings['script.user.passwordPolicyHistory']);
				}
				else if(obj.status == 'LDAP_ERROR'){
					$.modal.alert(obj.message);
					unblockUI();
				}
				else if(obj.status != 'Faliure') {
					win.closeModal(); 
					var purpose = $("#purpose").val();
					if(purpose == 'eduCenterUsers'){
						loadEduCenterUsers();
					}else{
						$.ajax({
							type : "GET",
							url : "getUserDetails.do",
							data : form.serialize() + "&AdminYear=" + $("#AdminYear").val(),
							dataType : 'json',
							cache:false,
							success : function(data) {
								if (data != null && data.length >= moreCount){
									$(".pagination").show(200);
								} else {
									$(".pagination").hide(200);
								}
								getUserDetails(true, data);
								enableSorting(true);
								unblockUI();
							},
							error : function(data) {
								unblockUI();
							}
						});
					}
					$.modal.alert(strings['script.user.adduserSuccess']);
					unblockUI();
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
		}
	}
	
	//----------------------------UPDATE ROW IF UPDATE SUCCESS ---------------------
	
	// Modification done by Joy for TD 77428 - ISTEP Prism_Manage User_On editing an existing user the change is not getting reflected immediately.
	function updateRowValues(row) {
		var roletag = '<small class="tag _BGCOLOR_ _CLASS_ ">_VALUE_</small>'
		var completeRoleTagHtml = '';
	
		//var statusClass = 'tag';
		var statusClass = 'with-tooltip tooltip-left';
		var statusVal = '';
		//alert($("#userModal #userStatus").attr('checked'));
		if($("#userModal #userStatus").attr('checked') ==  'checked' || $("#userModal #userStatus").attr('checked') ==  true) {
			statusClass = statusClass + ' enable';
			statusVal = 'Enabled';
		} else {
			statusClass = statusClass + ' disable';
			statusVal = 'Disabled';
		}
		//alert(statusClass);
		//alert($("#userModal #userName").val());
		//row.find('th:eq(0)').html( $("#userModal #userId").val());
		//row.find('td:eq(0)').html( $("#userModal #userName").val());
		//row.find('td small:eq(0)').html( statusVal);
		row.find('th span:eq(0)').removeClass();
		row.find('th span:eq(0)').addClass(statusClass);
		row.find('th span:eq(0)').attr('title', statusVal)
		
		/*row.find('td:eq(1) small').removeClass();
		row.find('td:eq(1) small').addClass(statusClass);*/
		var hasAdminRole = false;
		$("#editUser #userRole option").each(function() {
			if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
				var tokens = $(this).val().split("_");
				for (var i = 0; i < tokens.length; i++) {
					if (tokens[i] == "ADMIN") {
						hasAdminRole = true;
						break;
					}
				}
			}
		});
		if(hasAdminRole == false) {
			$("#editUser #userRole option").each(function() {
				if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
				var roleClass = 'role' + ' ' + $(this).val();
				var roleTagTemp = roletag +'<br/>';
				roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
				roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).text());
				
				if($(this).val()=="ROLE_ACSI")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "blue-bg");
				else if ($(this).val()=="ROLE_CTB")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "green-bg");
				else if ($(this).val()=="ROLE_SCHOOL")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "anthracite-bg");
				else if ($(this).val()=="ROLE_CLASS")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "grey-bg");	
				else if ($(this).val()=="ROLE_PARENT")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "red-bg");	
				else if ($(this).val()=="ROLE_ADMIN")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
				else if ($(this).val()=="ROLE_EDU_ADMIN")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
				else if ($(this).val()=="ROLE_USER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "black-bg");
				
				completeRoleTagHtml = completeRoleTagHtml + roleTagTemp;
				
				}
			});
		} else {
			$("#editUser #userRole option").each(function() {
				if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
					if ($(this).val()=="ROLE_ADMIN" || $(this).val()=="ROLE_EDU_ADMIN") {
						var roleClass = 'role' + ' ' + $(this).val();
						var roleTagTemp = roletag +'<br/>';
						roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
						roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).text());
						roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
						completeRoleTagHtml = completeRoleTagHtml + roleTagTemp;
					}
				}
			});
		}
		row.find('.roleContainerForUsers').html(completeRoleTagHtml);
	  }
		
	//----------------------------DELETE THE ROW IF THE DATA IS DELETED SUCCESSFULLY ---------------------
	
	function deleteRowValues(row) {
		row.closest("tr").remove();
		enableSorting(true);
	}	
	//----------------------------Resetting Modal Form---------------------
	
	function resetModalForm(formId)
	{
		$("#"+formId).each (function() { this.reset(); });
		$("input#userStatus").removeAttr('checked');
		$("input#userStatus").change();
		$("#userRole option").removeAttr('selected');
		$("#userRole option").change();
		$("#userRole option").trigger('update-select-list');
		for(name in CKEDITOR.instances)	{
			CKEDITOR.instances[name].destroy(true);
		}	
	}
	
	
	//------ auto completing the search field
	$("#searchUser").autocomplete({
		source: function(request, response) {
	        $.ajax({
	            url: "searchUserAutoComplete.do",
	            cache:false,
	            dataType: "json",
	            data: {
	                term : request.term,
	                selectedOrg : $("a.jstree-clicked").parent().attr("id"),
	                AdminYear : $("#AdminYear").val(),
	                purpose : $('#purpose').val(),
	                eduCenterId : $('#eduCenterId').val()
	            },
	            success: function(data) {
	                response(data);
	            }
	        });
	    },
		minLength:3,
		position:{my:"right top",at:"right bottom"},
		open: function(event, ui) {
			$(".ui-autocomplete").css({"max-height":"150px", "width":"186px", "z-index":"1000000"});
			$(".ui-autocomplete").addClass("white-gradient");
			if($.template.ie7 || $.template.ie8) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"201px", "z-index":"1000000", "overflow" : "auto"});
			} else {
				$('.ui-autocomplete').customScroll({
					horizontal : true,
					showOnHover : false,
					width: 10
				});
			}
		},
		select:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchUser").val(searchString);
			}
			searchUser(searchString,'Y');
			event.preventDefault();
			event.stopPropagation();
			//$(".pagination").hide(200);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchUser").val(searchString);
			}
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	
	//------------binding the key up event with the search field , it will search the users when the enter key is pressed
	$("#searchUser").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchUser($("#searchUser").val(),'N');
		}
	});
	//------------binding the click event with the search field icon, it will search the users when search button is clicked
	$("a[param^='search_icon_user']").live("click", function(e) {
		if ( $("#searchUser").val()!="" && $("#searchUser").val()!="Search") {
			$(".ui-autocomplete").hide();
			searchUser($("#searchUser").val(),'N');
		}
	});

	//------------------This function searches the user with the typed user name and populates the user details in the table 
	function searchUser(searchString,isExactSearch) {
			blockUI();
			$.ajax({
			type : "GET",
			url : "searchUser.do",
			data : "username="+searchString+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&AdminYear="+$("#AdminYear").val()+"&isExactSearch="+isExactSearch+"&purpose="+$("#purpose").val()+"&eduCenterId="+$("#eduCenterId").val(),
			dataType : "json",
			cache:false,
			success : function(data){
				if (data != null && data.length >= moreCount){
					$(".pagination").show(200);
					$("#moreStudent").removeClass("disabled");
					if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
				} else {
					$(".pagination").hide(200);
				}
				if ( data != null && data != "") {
					getUserDetails(true, data); //this method is defined in usermodule.js
					enableSorting(true);
				}
				if ( data != null && data == "") {
					getUserDetails(true, data); //this method is defined in usermodule.js
				}
				unblockUI();
			},
			error : function(data){
				$.modal.alert(strings['script.user.search']);
				unblockUI();
			},
			complete: function(data){
				$("#moreUser").removeClass("disabled");
				if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
				}
		});
	}
	
	function clickMe(event) {
		event.stopImmediatePropagation();
		$(document).click();
	}
	
	function showHideMore(){
		$("#moreUser").addClass("disabled");
		if($.browser.msie) $("#moreUser").addClass("disabled-ie");
	}
	
	function isFoundInList(value, list){
		var isFound = false;
		for(var i=0; i<list.length; i++) {
			if(value == list[i].roleName) return true;
		}
		return isFound;
	}
	/**
	 * This js file is to manage user module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
	var flgStudentRedirect=true;
	var redirectLevel = 0;
	$(document).ready(function() {

		if($("#userTable").length > 0) {
			var allUsers = $(".users-list-all");
			if(allUsers != null && allUsers.length > 0) {
				$('#report-list').tablesorter({
					headers: {
						//Change for TD 77643 - By Joy
						4: { sorter: false }
					},
					sortList: [[0,1]]
				});
			}
			
				$('.clearfix').addClass('menu-hidden');
				$("ul#shortcuts li").removeClass("current");
				$(".shortcut-contacts").parent().addClass("current");
			
			var tempScrollTop, currentScrollTop = 0
			$("#moreUser").click(function() {
				currentScrollTop = $("#userTable").scrollTop();
				if(!$(this).hasClass('disabled')) {
					//scrolling down15
					var lastid = "";
					var callingAction = "";
					if($('#purpose').val() == 'eduCenterUsers'){
						var eduCenterId = $('#eduCenterId').val();
						var eduCenterName = $("#eduCenterId :selected").text();
						var lastEduCenterId_username = $('#last_user_tenant').val();
						lastid = 'eduCenterId='+eduCenterId+'&eduCenterName='+eduCenterName+'&searchParam='+$("#searchUser").val()+'&lastEduCenterId_username='+lastEduCenterId_username;
						callingAction = 'loadEduCenterUsers.do';
					}else{
						lastid = "tenantId=" + $('input#last_user_tenant').val() + "&AdminYear=" + $("#AdminYear").val() + "&searchParam="+$("#searchUser").val();
						callingAction = 'getUserDetails.do';
					}
					//showLoader(currentScrollTop);
					blockUI();
					$.ajax({
						type : "GET",
						url : callingAction,
						data : lastid,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data != null && data.length > 0){
								getUserDetails(false, data);
								enableSorting(true);
								retainUniqueValue();
								//setLastRowId ();
								unblockUI();
								$("#userTable").animate({scrollTop: currentScrollTop+600}, 500);
							} else {
								$("#moreUser").addClass("disabled");
								if($.browser.msie) $("#moreUser").addClass("disabled-ie");
								retainUniqueValue();
								unblockUI();
							}
							if (data != null && data.length < moreCount) {
								// check if this is the last set of result
								$("#moreUser").addClass("disabled");
								if($.browser.msie) $("#moreUser").addClass("disabled-ie");
							}
						},
						error : function(data) {
							unblockUI();
						}
					});
		
				} else {
					return false;
				}
				tempScrollTop = currentScrollTop;
			});
			
			var tempTree = $("#tempTree").val();
			var objectTree = "";
			try{
				objectTree = tempTree.split(",");
			}catch(e){
				
			}
			redirectLevel = objectTree.length;
				
		}
		
		$("#downloadUsers").live("click", function() {
			var tenantId = $('input#last_user_tenant').val().split("_");
			var adminYear = $("#AdminYear").val();
			//alert("tenantId="+tenantId[0]+"\nadminYear="+adminYear);
			var href = "downloadUsers.do?tenantId=" + tenantId[0] + "&adminYear=" + adminYear;
			$("#downloadUsers").attr("href", href);
		});
	});
	
	// *********** END DOCUMENT.READY ************
	
	//======================================CREATE  TREE STURCTURE================================
		var isOpeningFromRemote = false;
		var isOpeningComplete = false;
		var objectTree;
		var currObj = '';
		
		var objectTreeStack;
		var loadFullpath = false;
		
		var maxNodeLimit = 50;
		var tempNodes = new Array();
		var jsTreeJsonArr = new Array();
		var tempIndex = 0;
		var treeContainerObj =$("#treeViewForOrg");
		$(function (){
			$("#slide_menu_user").removeClass("navigable");
			$("#slide_menu_org").removeClass("navigable");
            $("#slide_menu_parent").removeClass("navigable");
            $("#slide_menu_student").removeClass("navigable");
			treeContainerObj.jstree( {                  
					"json_data" : {                       
						//"data":jsondata ,
						"maxNodeLimit" : maxNodeLimit,
						"clickedNode" : function (n) {
											return (n.attr ? n.attr("id") : treeContainerObj.attr("rootid"));
										},	
						"ajax" : { 
							"url" : "tenantHierarchy.do",
							"cache":false,
							"data" : function (n) {
								clickedNode = (n.attr ? n.attr("id") : treeContainerObj.attr("rootid"));
								return {
											"tenantId" : clickedNode,
											"AdminYear" : $("#AdminYear").val()
									   };
								},									
							"success": function(data, textStatus, XMLHttpRequest) {
								if(data != null && data.length > 0) {
									if(data.length > maxNodeLimit)
									{													
										//alert(data.length);
										tempNodes = data;
										tempIndex = clickedNode;
									}
								}								
							},
							"complete" : function() { 
									$("#treeViewForOrg ul:first").css("float", "left");
									$("#treeViewForOrg ul:first").css("min-width", "100%");
									$("#treeViewForOrg [id="+clickedNode+"]").removeClass("jstree-closed").addClass("jstree-open");
									if(tempNodes != null)
									{
										if(tempNodes.length > maxNodeLimit) {
											var moreIndex = parseInt($(".no-click").index($("[id="+clickedNode+"] .no-click")));
											//alert(moreIndex);
											var tempObj = new Object();													
											tempObj.indexCount = maxNodeLimit;
											tempObj.nodeJsonData = tempNodes; 
											jsTreeJsonArr[tempIndex] = new Array();
											jsTreeJsonArr[tempIndex][moreIndex] = tempObj;
											tempObj = null;
											tempNodes = null;
										}
									} 												
									onClickMoreLink("treeViewForOrg",tempIndex);
									
									// open tree on clicking # of users in org screen
									if($("#isRedirectedTree").val()=='Yes'){ 
										 var rootOrg = $("#treeViewForOrg > ul > li:first").attr("id");
											var tempTree = $("#tempTreeStudent").val();
											if(flgStudentRedirect){
											  redirectLevel= (tempTree.split(",")).length;
											  flgStudentRedirect=false;
											}
											  
											if(redirectLevel > 1 && tempTree != '' && tempTree != null) {
												isOpeningFromRemote = true;
												objectTree = tempTree.split(",");
												if(!loadFullpath) {
													objectTreeStack = tempTree.split(",");
													loadFullpath = true;
												}
												
												var count = 0;
												var newTempTree = '';
												currObj = objectTree[0];
												$.each(objectTree, function(index) {
													if(count > 0) {
														newTempTree = newTempTree + objectTree[index] + ',';
													}
													count = count +1;
												});
												newTempTree = newTempTree.substring(0, newTempTree.length - 1)
												//$("#"+currObj).find(".jstree-icon").click();
												if(objectTree.length > 1) {
													var currnode = $("#"+currObj);
													if(currnode != null && currnode.length > 0) {
														treeContainerObj.jstree('open_node', $("#"+currObj));
													} else {
														clickMoreForRedirect(false, currObj);
													}
												} else {
													var currnode = $("#"+currObj);
													if(currnode != null && currnode.length > 0) {
														treeContainerObj.jstree('select_node', $("#"+currObj));
														//$("#"+currObj).find(".jstree-icon")[0].click();
														//$("#"+currObj).find("a")[0].click();
														$("#"+currObj).find("a").click();
													} else {
														clickMoreForRedirect(true, currObj);
													}
												}
												$("#tempTreeStudent").val(newTempTree);
											} else {
												isOpeningComplete = true;
											}
											if(isOpeningFromRemote && isOpeningComplete) {
												treeContainerObj.jstree('select_node', $("#"+currObj));
												//$("#"+currObj).find(".jstree-icon")[0].click();
												//$("#"+currObj).find("a")[0].click();
												$("#"+currObj).find("a").click();
											} 
											if(redirectLevel == 1  && tempTree != '' && tempTree != null) {
												$("#"+currObj).find("a").click();
												$("#tempTreeStudent").val('');
												
											}
									}else{
										var rootOrg = $("#treeViewForOrg > ul > li:first").attr("id");
										var tempTree = $("#tempTree").val();
									
										if(redirectLevel > 1 && tempTree != '' && tempTree != null) {
											isOpeningFromRemote = true;
											objectTree = tempTree.split(",");
											if(!loadFullpath) {
												objectTreeStack = tempTree.split(",");
												loadFullpath = true;
											}
											
											var count = 0;
											var newTempTree = '';
											currObj = objectTree[0];
											$.each(objectTree, function(index) {
												if(count > 0) {
													newTempTree = newTempTree + objectTree[index] + ',';
												}
												count = count +1;
											});
											newTempTree = newTempTree.substring(0, newTempTree.length - 1)
											//$("#"+currObj).find(".jstree-icon").click();
											if(objectTree.length > 1) {
												var currnode = $("#"+currObj);
												if(currnode != null && currnode.length > 0) {
													treeContainerObj.jstree('open_node', $("#"+currObj));
												} else {
													clickMoreForRedirect(false, currObj);
													
												}
											} else {
												var currnode = $("#"+currObj);
												if(currnode != null && currnode.length > 0) {
													treeContainerObj.jstree('select_node', $("#"+currObj));
													//$("#"+currObj).find(".jstree-icon")[0].click();
													//$("#"+currObj).find("a")[0].click();
													$("#"+currObj).find("a").click();
												} else {
													clickMoreForRedirect(true, currObj);
													
												}
											}
											$("#tempTree").val(newTempTree);
										} else {
											isOpeningComplete = true;
										}
										if(isOpeningFromRemote && isOpeningComplete) {
											treeContainerObj.jstree('select_node', $("#"+currObj));
											//$("#"+currObj).find(".jstree-icon")[0].click();
											//$("#"+currObj).find("a")[0].click();
											$("#"+currObj).find("a").click();
										} 
										if(redirectLevel == 1  && tempTree != '' && tempTree != null) {
											$("#"+currObj).find("a").click();
											$("#tempTree").val('');
											
										}
								   }	
									// END : open tree on clicking # of users in org screen
									
									//Code to select the first node on load
									if( !isOpeningFromRemote && ((redirectLevel == 1 || redirectLevel == 0) || objectTree == null || tempTree == null || tempTree == '') ) {
										selectTheFirstNodeOfTree($("#treeViewForOrg > ul > li:first").attr("id"));
									}
									if($("#isRedirectedTree").val()=='Yes'){
										//$("#isRedirectedTree").val("No");
									}
																			
							}
						}
							
					},
					 
					"themes": { theme: "apple", dots: true },
					"plugins" : [ "themes", "json_data", "ui" ]		
					
				});
		});
		// =============== THIS FUNCTION CLICKS "more" LINK IN JS-TREE UNTILL THE NODE IS VISIBLE ===================
		function clickMoreForRedirect(lastnode, currSelectedNode) {
			var currObj;
			$.each(objectTreeStack, function(index) {
				if(currSelectedNode == objectTreeStack[index]) {
					currObj = objectTreeStack[index-1];
				}
			});
			$("span[treespanlevel^="+currObj+"]").click();
			var currnode = $("#"+currSelectedNode);
			if(lastnode) {
				if(currnode != null && currnode.length > 0) {
					treeContainerObj.jstree('select_node', $("#"+currSelectedNode));
					//$("#"+currSelectedNode).find("a")[0].click();
					$("#"+currSelectedNode).find("a").click();
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			} else {
				if(currnode != null && currnode.length > 0) {
					treeContainerObj.jstree('open_node', $("#"+currSelectedNode));
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			}
		}
		
		//Function to select the first node of the tree
		var isFirstTime=true;
		function selectTheFirstNodeOfTree(nodeId){
		 if (isFirstTime){
			$("#"+nodeId+" "+"a").click();
			  isFirstTime = false;
			}
		}
		
		function onClickMoreLink(divId,index)
		{
			$("#"+divId+" [treeSpanLevel="+index+"].jstree-more-link").click(function(e) {		//alert('more');			
				var nodeJsonData = new Array();			
				var jsTreeNodeLevel = $(this).attr("treeSpanLevel");
				var tempNode = $(this).parent("span.no-click");
				var moreIndex = $(".no-click").index(tempNode);
				var nodeTempArr = jsTreeJsonArr[jsTreeNodeLevel][moreIndex];

				var startLimit = parseInt(nodeTempArr.indexCount);	
				//alert(startLimit);				
				nodeJsonData = nodeTempArr.nodeJsonData.slice(startLimit,(startLimit+maxNodeLimit));		
				var node = $(tempNode).prev();
				var nodesHTML = getJstreeNodes(nodeJsonData,divId,$(tempNode).attr("treelevel"));
				//alert(nodesHTML);		
				$(node).after(nodesHTML);			
				if(nodeJsonData.length == maxNodeLimit && nodeTempArr.nodeJsonData.length != (startLimit+maxNodeLimit))
				{		
					jsTreeJsonArr[jsTreeNodeLevel][moreIndex].indexCount = (startLimit+maxNodeLimit);				
				}
				else {
					//$(node).addClass("jstree-last");
					$(tempNode).remove();
					$(node).nextAll(":last").addClass("jstree-last");							
				}		
			});											   
		}
		
		function getJstreeNodes(nodeJsonData,divId,linkIndex)
		{
			var html = "";
			var jsTreeClassName = "";
			var chkLen = $("#"+divId+" [treelevel="+linkIndex+"]").parent("ul").parent("li.jstree-checked").length;	
			for(var index=0; index<nodeJsonData.length; index++) {
				var nodeDetails = nodeJsonData[index];
				if(chkLen > 0) {		
					jsTreeClassName = "jstree-checked ";
				}
				else {
					jsTreeClassName = "jstree-unchecked ";
				}
				jsTreeClassName += ((nodeDetails.attr.className != undefined) ? nodeDetails.attr.className : "");
				if(nodeDetails.attr.className == "jstree-leaf") {		
					jsTreeClassName += " jstree-open";
				}
				else {		
					jsTreeClassName += " jstree-closed";
				}
				if(nodeDetails.attr.assignedtotest == 1) {		
					jsTreeClassName += " jstree-makebold";
				}
				//html += "<li "+((nodeDetails.attr.assignedtotest == 1)? "assignedtotest=1": "")+" "+((nodeDetails.attr.redoflag == 1)? "redoflag=1": "")+" id="+nodeDetails.attr.id+" label="+nodeDetails.attr.label+" level="+nodeDetails.attr.level+" title='"+nodeDetails.attr.title+"' class='"+jsTreeClassName+"'><ins class='jstree-icon'>&nbsp;</ins><a href='#' class=''><ins class='jstree-checkbox' "+((nodeDetails.attr.assignedtotest == 1)? "style='display:none;'" : "")+">&nbsp;</ins><ins class='jstree-icon'>&nbsp;</ins>"+nodeDetails.data+"</a></li>";
				html +='<li createdby="'+nodeDetails.attr.createdBy+'" udatedby="'+nodeDetails.attr.udatedBy+'" tenantid="'+nodeDetails.attr.tenantId+'" parenttenantid="'+nodeDetails.attr.parentTenantId+'" noofchildorgs="'+nodeDetails.attr.noOfChildOrgs+'" noofusers="'+nodeDetails.attr.noOfUsers+'" orglevel="'+nodeDetails.attr.orgLevel+'" id="'+nodeDetails.attr.id+'" class="jstree-closed jstree-last"><ins class="jstree-icon">&nbsp;</ins><a href="#" class=""><ins class="jstree-icon">&nbsp;</ins>'+nodeDetails.data+'</a></li>';
				jsTreeClassName = "";
			}
			return html;
		}

	//======================================CREATE TREE STURCTURE ENDS================================
	
	//============================= FETCH NEXT HIERARCHY IN THE SLIDING MENU =============================
	$("#slide_menu_user > #org_details li").live("click", function(e) {
	
		e.stopImmediatePropagation();
		e.stopImmediatePropagation();
	    if ($(".tenantHier").hasClass("blue-gradient")) {
			$(".tenantHier").removeClass("blue-gradient");
		}
		//$(this).addClass("blue-gradient");
		var id = $(this).attr("id");
		var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
		showLoading( $(this) );
		$.ajax({
			type : "GET",
			url : "tenantHierarchy.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				if (!$.isEmptyObject(data)) {
					getChildOrg(id, data);
					
					startSlide(id);
					hideLoading();
				}
				//getUser(id);								
			},
			error : function(data) {
				hideLoading();
				$.modal.alert(strings['script.org.error']);
			}
		});
	});
	
	// create child hierarchy DOM
	function getChildOrg(id, data) {
		var content = '';
		$.each(data, function() {
			content += '<li id=' + this.tenantId
					+ ' class="with-right-arrow grey-arrow tenantHier"><span id='
					+ this.tenantId + ' class="icon grey icon-size2 icon-folder"></span> <b class="glossy with-tooltip tracked" ' + 'id='
					+ this.tenantId + ' title = "' + this.tenantName + '" tenantName = "' + this.tenantName + '" orgLevel = ' + this.orgLevel + '>' + manageTenantName(this.tenantName) + '</b></li>';
		});
		$("#" + id).append('<ul class="files-list mini">' + content + '</ul>');
	}
	
	//============================= FETCH ALL USERS FOR SELECTED ORG FOR THE SLIDING MENU =============================
	$("#slide_menu_user > #org_details li b").live("click", function(e) {
		e.stopImmediatePropagation();
		
			var tenantName=$(this).attr("tenantName");
			var orgLevel = $(this).attr("orgLevel");
			var tenantId = $(this).attr("id");
			var id = $(this).attr("id"); 
			
	    if ($(".tenantHier").hasClass("blue-gradient")) {
			$(".tenantHier").removeClass("blue-gradient");
		     }
		$(this).parent().addClass("blue-gradient");
		updateOrgNameHeading(tenantName);
		updateAddUserHeading(tenantName,orgLevel,tenantId);
		fetchAllUsers(id);
		
	});
	
	
	//============================= FETCH ALL USERS FOR SELECTED ORG FOR THE TREE MENU =============================
	$("#treeViewForOrg a").live("click", function(e) {
			$("#searchUser").val("");
			if($.browser.msie) {
				$("#searchUser").focus();
				$("#searchUser").blur();
			}
			e.stopImmediatePropagation();
			var tenantName=$(this).text();
			var orgLevel = $(this).parent().attr("orglevel");
			var tenantId = $(this).parent().attr("id");
			var id = $(this).parent().attr("id");
			updateOrgNameHeading(tenantName);
			if($("#slide_menu_org").hasClass("organizations")){
				fetchAllOrgUsers(id,"orgChildren.do");// in manageOrganizations.js
			}
			else if($("#slide_menu_parent").hasClass("parents")){
				$("#searchParent").val("");
				if($.browser.msie) {
					$("#searchParent").focus();
					$("#searchParent").blur();
				}
				fetchAllParentUsers(id,"getParentDetailsOnScroll.do");// in manageParent.js
			}
			else if($("#slide_menu_student").hasClass("students")){
				$("#searchStudent").val("");
				if($.browser.msie) {
					$("#searchStudent").focus();
					$("#searchStudent").blur();
				}
				fetchAllStudents(id,$("#redirectedStudentBioId").val(),$("#isRedirectedTree").val(),"getStudentDetailsOnScroll.do");// in manageStudent.js
		    }
			else{
				updateAddUserHeading(tenantName,orgLevel,tenantId);
				fetchAllUsers(id,"getUserDetails.do");
			}
			
			
		
	});
	//================================SHOW THE SELECTED ORG NAME ON THE TOP OF THE TABLE =====================
	function updateOrgNameHeading (tenantName)
		{
			if($('#showOrgNameUser') != null) $('#showOrgNameUser').html("Users of " + tenantName);
			if($('#showOrgName') != null) $('#showOrgName').html("Organizations of " + tenantName);
			if(tenantName == "-1") {
				$('#showOrgNameUser').html(" ");
				$('#showOrgName').html(" ");
				$("span#showOrgNameParent").text(' ');
				$("span#showOrgNameStudent").text(' ');
			}
		}
		
		
	//================================SHOW THE SELECTED ORG NAME ON ADD USER MODAL =====================
	function updateAddUserHeading (tenantName,orgLevel,tenantId)
		{
			$('#addUser').attr( 'tenantName', tenantName);
			$('#addUser').attr( 'orgLevel', orgLevel);
			$('#addUser').attr( 'tenantId', tenantId);
			$('#orgLevel').val(orgLevel);
		}
	
	//====================================AJAX CALL TO FETCH ALL THE USERS FOR THE SELECTED ORG====================
	function fetchAllUsers(id,url)
		{
			blockUI();
			var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
			$("tbody#user_details").find("tr").remove();
			$("#report-list").trigger("update");
			$("tbody#user_details").addClass("loader big");
			$.ajax({
				type : "GET",
				url : url,
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if(data != null && data.length >= 1){
						showHideDataTable('show');
					}
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
					} else {
						$(".pagination").hide(200);
					}
					getUserDetails(true, data);
					enableSorting(true);
					$("tbody#user_details").removeClass("loader big");				
					if (data != null && data.length >= moreCount){
						$("#moreUser").removeClass("disabled");
						if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
					} else {
						$("#moreUser").addClass("disabled");
						if($.browser.msie) $("#moreUser").addClass("disabled-ie");
					}
					unblockUI();
				},
				error : function(data) {
					$("tbody#user_details").removeClass();	
					$.modal.alert(strings['script.org.error']);
					unblockUI();
				}
			});
		}
	//================================ CREATE USER ROW DOM ========================================
	// ********** This function is moved to init.jsp ***************** //
	/*function getUserDetails(checkFirstLoad, data) {
		var userContent = '';
		if (checkFirstLoad) {
			$("#user_details").find("tr").remove();
		}
		
		$.each(data, function () { 
		    
			userContent += '<tr id ='+ this.tenantId+'_'+this.userId+' scrollid= '+ this.loggedInOrgId+'_'+this.userName +' class="abc" >'
							+'<th scope="row">' + this.userName +'</th>'
							+'<td>' + this.userDisplayName +'</td>'
							+ createStatusTag(this.status)
							+'<td class="hide-on-tablet-portrait">'+this.tenantName+'</td>'
							//+ createUserTypeTag(this.userType)
							+ createUserRolesTag(this.availableRoleList)
							+'<td class="vertical-center">'
								+' <span class="button-group compact">' 
									+' <a id="'+ this.userId +'" tenantId ="' + this.tenantId + '" href="#" class="button icon-pencil with-tooltip edit-User" title="Edit"></a> '
									+' <a id="'+ this.userId +'" param="'+ this.userName +'" href="#" class="button icon-users icon with-tooltip login-as" title="Login as User"></a>'
									+' <a id="'+ this.userId +'" userName="'+ this.userName + '" parentId="' + this.parentId + '" tenantId ="' + this.tenantId +'" href="#" class=" button icon-trash with-tooltip confirm delete-User" title="Delete"></a>'  
								+' </span>'
							+'</td>'
						+'</tr>'
						
						 
		});
		$("#user_details").append(userContent);
		$("#report-list").trigger("update");
		$(".headerSortDown").removeClass("headerSortDown");
		$(".headerSortUp").removeClass("headerSortUp");
		setLastRowId ($("#last_user_tenant"));
	}*/
	
	//============================THIS FUNCTION CREATES THE ROLE TAGS FOR THE USER TABLE IN "MANAGE USERS" PAGE==========================
	function hasAdmin(availableRoles){
		var isAdmin = false;
		for (var j=0; j<availableRoles.length; j++){
			if (isAdminRole(availableRoles[j].roleName)){
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
	function isAdminRole(role){
		var isAdmin = false;
		var tokens = role.split("_");
		//alert(tokens);
		for (var i=0; i<tokens.length; i++){
			if (tokens[i] == "ADMIN"){
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
	function createUserRolesTag(availableRoles){
		var hasAdminRole = hasAdmin(availableRoles);
		var roleTag='<td class="roleContainerForUsers vertical-center">';
		//alert("roles=" + JSON.stringify(availableRoles));
		//alert(hasAdminRole);
		if(hasAdminRole == false) {
			$.each(availableRoles, function (){
				if(this.roleName== 'ROLE_ACSI'){
					roleTag +='<small class="tag blue-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_CTB'){
					roleTag +='<small class="tag green-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_SCHOOL'){
					roleTag +='<small class="tag anthracite-bg  role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_CLASS'){
					roleTag +='<small class="tag grey-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_PARENT'){
					roleTag +='<small class="tag red-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_ADMIN'){
					roleTag +='<small class="tag orange-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_USER'){
					roleTag +='<small class="tag black-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
			});
		} else {
			$.each(availableRoles, function (){
				if(this.roleName== 'ROLE_ADMIN'){
					roleTag +='<small class="tag orange-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_EDU_ADMIN'){
					roleTag +='<small class="tag orange-bg role '+ this.roleName+'">'+this.roleDescription+'</small><br/>';
				}
			});
		}
		roleTag +="</td>";
		return roleTag;
	}
	
	
	function createUserTypeTag(userType) {
		var typeTag = "";
		if(userType == 'ACSI USER') {
			typeTag = '<td><small class="tag blue-bg">'+userType+'</small></td>';	
		}
		if(userType == 'SCHOOL USER') {
			typeTag = '<td><small class="tag orange-bg">'+userType+'</small></td>';	
		}
		if(userType == 'TEACHER USER') {
			typeTag = '<td><small class="tag grey-bg">'+userType+'</small></td>';	
		}	
		return typeTag;
	}
	
	function setLastRowId (obj){
		obj.val($(".abc:last").attr("scrollid"));
	}
	
	
	function retainUniqueValue()
	{
			var seen = {};
			$('.abc').each(function() {
			var txt = $(this).attr("scrollid");
			if (seen[txt])
				$(this).remove();
			else
				seen[txt] = true;
			});

	}
	//========================================SHOWING AND HIDE LOADER=======================
	function showLoader(currentScrollTop)
	{
		$('div#last_msg_loader').css("top",currentScrollTop+400);
		$('div#last_msg_loader').html('<img src="themes/acsi/img/standard/loaders/loading64.gif">');"parentid=" + $(".abc:last").attr("id");
	}
	function hideLoader()
	{
		$('div#last_msg_loader').html('<!--<img src="themes/acsi/img/standard/loaders/loading64.gif">-->');
	}
	
	//============================= ENABLE TABLE SORTING =============================
	function enableSorting(flag) {
		if (flag) {
	
			var usersTable = $("#report-list");
			usersTable.trigger("update");
		}
	
	}
	
	function startSlide(id) {
	
		var animate = true;
		var clicked = id,
		// LI element
		li = $("#" + id).closest('li'),
	
		// Sub-menu
		submenu = li.children('ul:first'),
	
		// List of all ul above the current li
		allUL = li.parentsUntil('.navigable', 'ul'),
	
		// Current li ul
		parentUL = allUL.eq(0),
	
		// Main ul
		mainUL = allUL.eq(-1),
	
		// Root navigable element
		root = mainUL.closest('.navigable'),
	
		// Back button
		back = root.children('.back'),
	
		// Load indicator
		load = root.children('.load'),
	
		// Other vars
		current, url;
	
		if (submenu.length > 0) {
			// If not ready yet
			if (parentUL.outerHeight(true) == 0) {
				// Delay action
				setTimeout(function() {
					clicked.click();
	
				}, 40);
				return;
			}
	
			// Set as current
			root.data('navigableCurrent', submenu);
	
			// Hide previously open submenus
			parentUL.find('ul').hide();
	
			// Display parent menus
			allUL.show();
	
			// Display it
			submenu.show();
	
			// Correct position if needed
			submenu.add(allUL.not(':last')).each(
					function(i) {
						var menu = $(this), parent = menu.parent();
	
						if ($.inArray(parent.css('position'), [ 'relative',
								'absolute' ]) > -1) {
							menu.css('top', -parent.position().top + 'px');
						}
					});
	
			/*
			 * Animation
			 */
	
			// Set root element size according to target size
			root.stop(true).height(
					parentUL.outerHeight(true) + back.outerHeight(true))[animate ? 'animate'
					: 'css']({
				height : (submenu.outerHeight(true) + back.outerHeight()) + 'px'
			});
	
			// Move whole navigation to reveal target ul
			mainUL.stop(true)[animate ? 'animate' : 'css']({
				left : -(allUL.length * 100) + '%'
			});
	
			// Show back button
			back[animate ? 'animate' : 'css']({
				marginTop : 0
			});
	
			// Send open event
			li.trigger('navigable-open');
	
		}
	}
	
	function manageTenantName(tenantName) {
		if (tenantName.length >= 18 ) {
			tenantName = tenantName.substring(0,15) + "...";
		}
		return tenantName;
	} 
	
	function createStatusTag(status) {
		var statusTag = "";
		if (status == "AC" ) {
			//statusTag = '<td class="hide-on-tablet"><small class="tag green-bg">' + 'Enabled' + '</small></td>';
			//statusTag = '<td><small class="tag green-bg">' + 'Enabled' + '</small></td>';
			statusTag = '<span class="enable with-tooltip tooltip-left" title="Enabled"></span>';
		} else {
			//statusTag = '<td class="hide-on-tablet"><small class="tag red-bg">' + 'Disabled' + '</small></td>';
			//statusTag = '<td><small class="tag red-bg">' + 'Disabled' + '</small></td>';
			statusTag = '<span class="disable with-tooltip tooltip-left" title="Disabled"></span>';
		}
		return statusTag;
	}
	
	// ======================== RELOAD JS-TREE ON SELECTING ADMIN YEAR ====================== 
	function reloadOrgTree(selectedObj) {
		blockUI();
		// patch to remove tooltip
		$("#AdminYear").removeTooltip(true, true);
		$("#AdminYear").mouseenter();
		$("#AdminYear").mouseleave();
		
		$("#orgMode").removeTooltip(true, true);
		$("#orgMode").mouseenter();
		$("#orgMode").mouseleave();
		// END : patch to remove tooltip
		
		/* Fixed QC defect - Joy
		 * By changing TA/org mode, data table retains for non-public mode though logged in user is public and vice versa
		 */
		showHideDataTable('hide');
		//End Fixed QC defect - Joy
		
		//Fix for TD 78117 - By Joy
		//var adminYear = $(selectedObj).val();
		var adminYear = $('#AdminYear').val();
		
		var queryData = "AdminYear="+adminYear+"&orgMode="+$("#orgMode").val();
		$.ajax({
			type : "GET",
			url : "updateAdminYear.do",
			data : queryData,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				isFirstTime = true;
				treeContainerObj.jstree("refresh");
				resetPrismActions();
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.org.error']);
			}
		});
		blockUI();
		$.ajax({
			type : "GET",
			url : 'getUserDetails.do',
			//data : "tenantId=" + $('input#last_user_tenant').val() + "&AdminYear=" + $("#AdminYear").val() + "&searchParam="+$("#searchUser").val(),
			data : "tenantId=" + $("#CurrentOrg").val() + "&AdminYear=" + $("#AdminYear").val() + "&searchParam="+$("#searchUser").val(),
			dataType : 'json',
			cache:false,
			success : function(data) {
				if (data != null && data.length > 0){
					getUserDetails(false, data);
					enableSorting(true);
					retainUniqueValue();
					//setLastRowId ();
					unblockUI();
					//$("#userTable").animate({scrollTop: currentScrollTop+600}, 500);
				} else {
					$("#moreUser").addClass("disabled");
					if($.browser.msie) $("#moreUser").addClass("disabled-ie");
					retainUniqueValue();
					updateOrgNameHeading("-1");
					unblockUI();
				}
				if (data != null && data.length < moreCount) {
					// check if this is the last set of result
					$("#moreUser").addClass("disabled");
					if($.browser.msie) $("#moreUser").addClass("disabled-ie");
				}
			},
			error : function(data) {
				unblockUI();
			}
		});
	}
	
	/* Fixed QC defect - Joy
	 * By changing TA/org mode, data table retains for non-public mode though logged in user is public and vice versa
	 */
	function showHideDataTable(purpose){
		if(purpose === 'show'){
			$("#moreStudent").show();
			$("#moreUser").show();
			$("#moreOrg").show();
			$("#moreParent").show();

			$('#studentTable').show();
			$('#userTable').show();
			$('#orgTable').show();
			$('#parentTable').show();
		}else if(purpose === 'hide'){
			$("#moreStudent").hide();
			$("#moreUser").hide();
			$("#moreOrg").hide();
			$("#moreParent").hide();

			$('#studentTable').hide();
			$('#userTable').hide();
			$('#orgTable').hide();
			$('#parentTable').hide();
		}
	}
	
	
	/*This function used for SSO user to check class level*/
	function isClassUser(availableRoles){
		var isClass = false;		
		$.each(availableRoles, function (){
			if(this.label== 'Class'){
				isClass = true;
				return isClass;
			}			
		});
		return isClass;
	}
/**
 * This js file is for manage organizations screen
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
$(document).ready(function() {
	
	if($("#orgTable").length > 0) {
		
		$('#org-list').tablesorter({
			// pass the headers argument and assign an object
	        headers: { 
	            // assign the fourth column (we start counting zero)
	            3: { 
	                // disable it by setting the property sorter to false
	                sorter: false 
	            }
	        } 
		});
		// hide the right side menu
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-settings").parent().addClass("current");
		var orgCount = $("tbody#org_details").find("tr");
		if(orgCount != null && orgCount.length >= moreCount) {
			$(".pagination").show(200);
			$("#moreOrg").removeClass("disabled");
			if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
		} else {
			$(".pagination").hide(200);
		}
		if(orgCount == 0) {
			updateOrgNameHeading("-1");
		}
		//=======================SCROLL FOR THE ORGANISATION TABLE==============================
		var tempScrollTop, currentScrollTop = 0
		$("#moreOrg").click(function() {
			currentScrollTop = $("#orgTable").scrollTop();
			if(!$(this).hasClass('disabled')) {
				blockUI();
				var lastid ="tenantId=" + $('input#last_org_tenant').val()+"&AdminYear="+$("#AdminYear").val()+"&searchParam="+$("#searchOrg").val(); 
				//showLoader(currentScrollTop);//This method is defined in usermodule.js
				$.ajax({
					type : "GET",
					url : "orgChildren.do",
					data : lastid,
					dataType : 'json',
					cache:false,
					success : function(data) {
						unblockUI();
						if (data != null && data.length > 0){
							buildOrgDOM(false, data);
							enableSorting(true);
							retainUniqueValue();
							//setLastRowId ();
							//hideLoader();//This method is defined in usermodule.js
							$("#orgTable").animate({scrollTop: currentScrollTop+600}, 500);
						} else {
							$("#moreOrg").addClass("disabled");
							if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
						}
						if (data != null && data.length < moreCount) {
							// check if this is the last set of result
							$("#moreOrg").addClass("disabled");
							if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
						}
					
					},
					error : function(data) {
						unblockUI();
						$.modal.alert(strings['script.common.error']);
						//hideLoader();//This method is defined in usermodule.js
					}
				});
	
			}
			tempScrollTop = currentScrollTop;
		});
	}
	resetPrismActions();
});	

	 
		
		
		// update the organization tree on click on a Organization
		$(".org_list li").live("click", function(e) {
			e.stopImmediatePropagation();
			
			 if ($(".tenantHier").hasClass("blue-gradient")) {
					$(".tenantHier").removeClass("blue-gradient");
				}
			var id = $(this).attr("id");
			var nodeid = "tenantId=" + id + "&AdminYear="+$("#AdminYear").val();
			
			showLoading( $(this) );
			blockUI();
			$.ajax({
				type : "GET",
				url : "orgChildren.do",
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					unblockUI();
					if (!$.isEmptyObject(data)) {
						updateOrgTree(id, data);
						$(".org_list li").addClass("tenantHier");
						startSlide(id);
					}
					hideLoading();
				},
				error : function(data) {
					unblockUI();
					hideLoading();
					$.modal.alert(strings['script.org.error']);
				}
			});
		});
		
		// Update the organization details on click on a organization name
		/*$(".org_list li b").live("click", function(e) {
			e.stopImmediatePropagation();
			//var id = $(this).parent().attr("id");
			if ($(".tenantHier").hasClass("blue-gradient")) {
					$(".tenantHier").removeClass("blue-gradient");
				}
			$('#showOrgName b').text("");
			$('#showOrgName b').text("Child Organizations for " + $(this).text());
			$(this).parent().addClass("blue-gradient");
			$("tbody#org_details").find("tr").remove();
			$("tbody#org_details").addClass("loader big");
			//var id = $(this).attr("id");
			var nodeid = "tenantId=" + id;
			$.ajax({
				type : "GET",
				url : "orgChildren.do",
				data : nodeid,
				dataType : 'json',
				success : function(data) {
					if (!$.isEmptyObject(data)) {
						buildOrgDOM(true,data);
						
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data) {
					$("tbody#org_details").removeClass();
					$.modal.alert('Error occurred while populating organization children.');
				}
			});
		});*/
		
		
		//===========================Populates the table on click on a organization name========================
		function fetchAllOrgUsers(id,urlPath)
		{
			blockUI();
			$("#searchOrg").val("");
			if($.browser.msie) {
				$("#searchOrg").focus();
				$("#searchOrg").blur();
			}
			$("tbody#org_details").find("tr").remove();
			$("tbody#org_details").addClass("loader big");
			$("#org-list").trigger("update");
			var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
			$.ajax({
				type : "GET",
				url : urlPath,
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					unblockUI();
					if(data.length >= 1){
						showHideDataTable('show');
					}
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
						$("#moreOrg").removeClass("disabled");
						if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
					} else {
						$(".pagination").hide(200);
					}
					if (!$.isEmptyObject(data)) {
						buildOrgDOM(true,data);
						
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data) {
					unblockUI();
					$("tbody#org_details").removeClass();
					$.modal.alert(strings['script.org.error']);
				}
			});
		}
		
		// create child hierarchy DOM and update the sliding tree
		function updateOrgTree(id, data) {
			var content = '';
			if(data.length >= moreCount) {
				$("#moreOrg").removeClass("disabled");
				if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
			} else {
				$("#moreOrg").addClass("disabled");
				if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
			}
			$.each(data, function() {
				content += '<li id=' + this.tenantId
						+ ' class="with-right-arrow grey-arrow"><span id='
						+ this.tenantId + ' class="icon grey icon-size2 icon-folder"></span> <b id='
						+ this.tenantId + '>' + this.tenantName + '</b></li>';
			});
			$("#" + id).append('<ul class="files-list mini">' + content + '</ul>');
		}
		
		// update Organization details 
		// Create user row DOM             
		function updateOrgDetails( data) {
			var userContent = '';
			$("tbody#org_details").find("tr").remove();
			$("#org-list").trigger("update");
			$.each(data, function () { 
				userContent += '<tr id ='+ this.tenantId+' class="abc" >'
								+'<th scope="row">' + this.tenantId +'</th>'
								+'<td>' + this.tenantName +'</td>'
								+'<td>'+ this.noOfChildOrgs+'</td>'
								+'<td><a style="text-decoration:underline" href="redirectToUser.do?AdminYear='+$("#AdminYear").val()+'&nodeId='+this.tenantId+'&parentOrgId='+this.parentTenantId+'" >'+this.noOfUsers+'</a></td>'
								+'</tr>'
			});
			$("tbody#org_details").append(userContent);
		}
		
		// search organization............START
		
		$("#searchOrg").autocomplete({
			source: function(request, response) {
	            $.ajax({
	                url: "searchOrgAutoComplete.do",
	                dataType: "json",
	                cache:false,
	                data: {
	                    term : request.term,
	                    selectedOrg : $("a.jstree-clicked").parent().attr("id"),
	                    AdminYear : $("#AdminYear").val()
	                },
	                success: function(data) {
	                    response(data);
	                }
	            });
	        },
			minLength:3,
			position:{my:"right top",at:"right bottom"},
			open: function(event, ui) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"186px"});
				$(".ui-autocomplete").addClass("white-gradient");
				if($.template.ie7 || $.template.ie8) {
					$(".ui-autocomplete").css({"max-height":"150px", "width":"201px", "z-index":"1000000", "overflow" : "auto"});
				} else {
					$('.ui-autocomplete').customScroll({
						horizontal : true,
						showOnHover : false,
						width: 10
					});
				}
			},
			select:function(event, ui) {
				searchOrg(ui.item.value);
				$(".pagination").hide(200);
			},
			focus:function(event, ui) {
				var searchString = ui.item.value;
				$("#searchOrg").val(searchString);
				event.preventDefault();
				event.stopPropagation();
			}
		});
		//------------binding the click event with the search field icon, it will search the org when the enter key is pressed
		$("#searchOrg").live("keyup", function(e) {
			if ( e.keyCode == 13 && $(this).val()!="") {
				$(".ui-autocomplete").hide();
				searchOrg($("#searchOrg").val());
			}
		});
		//------------binding the click event with the search field icon, it will search the org when search button is clicked
		$("a[param^='search_icon_org']").live("click", function(e) {
			if ( $("#searchOrg").val()!="" && $("#searchOrg").val()!="Search") {
				$(".ui-autocomplete").hide();
				searchOrg($("#searchOrg").val());
			}
		});
		
		

		function searchOrg(searchString) {
			blockUI();
			$("tbody#org_details").find("tr").remove();
			$("#org-list").trigger("update");
			$("tbody#org_details").addClass("loader big");
			$.ajax({
				type : "GET",
				url : "searchOrganization.do",
				data : "searchString="+searchString+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&AdminYear="+$("#AdminYear").val(),
				dataType : "json",
				cache:false,
				success : function(data){
					unblockUI();
					//$(".pagination").hide(200);
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
						$("#moreStudent").removeClass("disabled");
						if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
					} else {
						$(".pagination").hide(200);
					}
					if ( data != null) {
						buildOrgDOM(true,data);
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data){
					unblockUI();
					$.modal.alert(strings['script.org.searchError']);
					$("tbody#org_details").removeClass();
				}
			});
		}

// ================================= SHOW LOADING IN MENU ===============================
function showLoading(obj) {
	var root = $(".navigable"),
		load = $('<div class="load orgLoad white-gradient" style="height:50px; background-color: #ecedf2;"></div>').appendTo(root),
		li = $(obj).closest('li'),
		allUL = li.parentsUntil('.navigable', 'ul');
	//$("#org_list").stop(true)['animate']({ left: -((allUL.length-1)*100+10)+'%' });
}
//================================= HIDE LOADING IN MENU ===============================
function hideLoading() {
	$('.orgLoad').remove();
	//$("#org_list").stop(true)['animate']({ right: -((allUL.length-1)*100+10)+'%' });
}

/*function resetPrismActions() {
	var adminYear = (typeof $('#AdminYear').val() !== 'undefined') ? $('#AdminYear').val() : 0;
	if(adminYear == 0){
		adminYear = (typeof $('#custProdIdManageContent').val() !== 'undefined') ? $('#custProdIdManageContent').val() : 0;
	}
	var queryData = "AdminYear="+adminYear;
	blockUI();
	$.ajax({
		type : "GET",
		url : "resetPrismActions.do",
		data : queryData,
		dataType : 'json',
		cache : false,
		async : false,
		success : function(data) {
			resetJspElements(data);
		},
		error : function(data) {
			//Temporary fix for TD 81027 - By Joy
			//$.modal.alert(strings['script.org.error']);
		}
	});
	unblockUI();
}*/

function resetPrismActions() {
	var adminYear = (typeof $('#AdminYear').val() !== 'undefined') ? $('#AdminYear').val() : 0;
	if(adminYear == 0){
		adminYear = (typeof $('#custProdIdManageContent').val() !== 'undefined') ? $('#custProdIdManageContent').val() : 0;
	}
	if(adminYear == 0 || adminYear == -1) {
		// no need to call reset
	} else {
		var queryData = "AdminYear="+adminYear;
		blockUI();
		$.ajax({
			type : "GET",
			url : "resetPrismActions.do",
			data : queryData,
			dataType : 'json',
			cache : false,
			async : false,
			success : function(data) {
				resetJspElements(data);
			},
			error : function(data) {
				//Temporary fix for TD 81027 - By Joy
				//$.modal.alert(strings['script.org.error']);
			}
		});
		unblockUI();
	}
}

function resetJspElements(data){
	if(strings['manage.orgs.usercount'] == data['Manage Organizations User Count']){
		$("#MANAGE_ORGS_USER_COUNT").val(data['Manage Organizations User Count']);
	}else{
		$("#MANAGE_ORGS_USER_COUNT").val("");
	}
	
	//Manage Education Center Users
	if(strings['manage.edu.center.users.edit.user'] == data['Manage Education Center Users Edit User']){
		$("#EDU_EDIT_USER").val(data['Manage Education Center Users Edit User']);
	}else{
		$("#EDU_EDIT_USER").val("");
	}
	if(strings['manage.edu.center.users.login.as.user'] == data['Manage Education Center Users Login As User']){
		$("#EDU_LOGIN_AS").val(data['Manage Education Center Users Login As User']);
	}else{
		$("#EDU_LOGIN_AS").val("");
	}
	if(strings['manage.edu.center.users.delete.user'] == data['Manage Education Center Users Delete User']){
		$("#EDU_DELETE_USER").val(data['Manage Education Center Users Delete User']);
	}else{
		$("#EDU_DELETE_USER").val("");
	}
	
	//Manage Users
	if(strings['manage.users.edit'] == data['Manage Users Edit User']){
		$("#MANAGE_USERS_EDIT").val(data['Manage Users Edit User']);
	}else{
		$("#MANAGE_USERS_EDIT").val("");
	}
	if(strings['manage.users.loginas'] == data['Manage Users Login As User']){
		$("#MANAGE_USERS_LOGIN_AS").val(data['Manage Users Login As User']);
	}else{
		$("#MANAGE_USERS_LOGIN_AS").val("");
	}
	if(strings['manage.users.delete'] == data['Manage Users Delete User']){
		$("#MANAGE_USERS_DELETE").val(data['Manage Users Delete User']);
	}else{
		$("#MANAGE_USERS_DELETE").val("");
	}
	
	//Manage Content
	if(strings['manage.content.add'] == data['Manage Content Add Content']){
		$("#MANAGE_CONTENT_ADD").val(data['Manage Content Add Content']);
	}else{
		$("#MANAGE_CONTENT_ADD").val("");
	}
	if(strings['manage.content.edit'] == data['Manage Content Edit Content']){
		$("#MANAGE_CONTENT_EDIT").val(data['Manage Content Edit Content']);
	}else{
		$("#MANAGE_CONTENT_EDIT").val("");
	}
	if(strings['manage.content.delete'] == data['Manage Content Delete Content']){
		$("#MANAGE_CONTENT_DELETE").val(data['Manage Content Delete Content']);
	}else{
		$("#MANAGE_CONTENT_DELETE").val("");
	}
	if(strings['manage.content.more'] == data['Manage Content More']){
		$("#MANAGE_CONTENT_MORE").val(data['Manage Content More']);
	}else{
		$("#MANAGE_CONTENT_MORE").val("");
	}
	if(strings['manage.content.standard'] == data['Manage Content Standard Description']){
		$("#MANAGE_CONTENT_STANDARD").val(data['Manage Content Standard Description']);
	}else{
		$("#MANAGE_CONTENT_STANDARD").val("");
	}
	if(strings['manage.content.rsc'] == data['Manage Content Resource Description']){
		$("#MANAGE_CONTENT_RSC").val(data['Manage Content Resource Description']);
	}else{
		$("#MANAGE_CONTENT_RSC").val("");
	}
	if(strings['manage.content.eda'] == data['Manage Content Everyday Activity Description']){
		$("#MANAGE_CONTENT_EDA").val(data['Manage Content Everyday Activity Description']);
	}else{
		$("#MANAGE_CONTENT_EDA").val("");
	}
	if(strings['manage.content.att'] == data['Manage Content About the Test Description']){
		$("#MANAGE_CONTENT_ATT").val(data['Manage Content About the Test Description']);
	}else{
		$("#MANAGE_CONTENT_ATT").val("");
	}
	if(strings['manage.content.rbs'] == data['Manage Content Result by Standard Description']){
		$("#MANAGE_CONTENT_RBS").val(data['Manage Content Result by Standard Description']);
	}else{
		$("#MANAGE_CONTENT_RBS").val("");
	}
	if(strings['manage.content.oar'] == data['Manage Content Overall Results Description']){
		$("#MANAGE_CONTENT_OAR").val(data['Manage Content Overall Results Description']);
	}else{
		$("#MANAGE_CONTENT_OAR").val("");
	}
	
	//Manage Parents
	if(strings['manage.parents.resetPwd'] == data['Manage Parents Reset Password']){
		$("#MANAGE_PARENTS_RESET_PWD").val(data['Manage Parents Reset Password']);
	}else{
		$("#MANAGE_PARENTS_RESET_PWD").val("");
	}
	if(strings['manage.parents.viewChildren'] == data['Manage Parents View Children']){
		$("#MANAGE_PARENTS_VIEW_CHILDREN").val(data['Manage Parents View Children']);
	}else{
		$("#MANAGE_PARENTS_VIEW_CHILDREN").val("");
	}
	
	//Manage Students
	if(strings['manage.students.assessment'] == data['Manage Students Assessment']){
		$("#MANAGE_STUDENTS_ASSESSMENT").val(data['Manage Students Assessment']);
	}else{
		$("#MANAGE_STUDENTS_ASSESSMENT").val("");
	}
	
	showHideJspElements();
}

function showHideJspElements(){
	if($("#MANAGE_ORGS_USER_COUNT").val() == strings['manage.orgs.usercount']) {
		$("#th_MANAGE_ORGS_USER_COUNT").show();
	} else {
		$("#th_MANAGE_ORGS_USER_COUNT").hide();
	}
	
	//Manage Education Center Users
	if ($("#EDU_EDIT_USER").val() != strings['manage.edu.center.users.edit.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('#manageEduCenterUsersDiv .icon-pencil').hide();
	} else {
		$('#manageEduCenterUsersDiv .icon-pencil').show();
	}
	if ($("#EDU_LOGIN_AS").val() != strings['manage.edu.center.users.login.as.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('#manageEduCenterUsersDiv .icon-users').hide();
	} else {
		$('#manageEduCenterUsersDiv .icon-users').show();
	}
	if ($("#EDU_DELETE_USER").val() != strings['manage.edu.center.users.delete.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('#manageEduCenterUsersDiv .icon-trash').hide();
	} else {
		$('#manageEduCenterUsersDiv .icon-trash').show();
	}
	
	//Manage Users
	if ($("#MANAGE_USERS_EDIT").val() == strings['manage.users.edit']) {
		$('#manageUsersDiv .icon-pencil').show();
	} else {
		$('#manageUsersDiv .icon-pencil').hide();
	}
	if ($("#MANAGE_USERS_LOGIN_AS").val() == strings['manage.users.loginas']) {
		$('#manageUsersDiv .icon-users').show();
	} else {
		$('#manageUsersDiv .icon-users').hide();
	}
	if ($("#MANAGE_USERS_DELETE").val() == strings['manage.users.delete']) {
		$('#manageUsersDiv .icon-trash').show();
	} else {
		$('#manageUsersDiv .icon-trash').hide();
	}
	
	//Manage Content
	if ($("#MANAGE_CONTENT_EDIT").val() == strings['manage.content.edit']) {
		$('.edit-content').show();
	} else {
		$('.edit-content').hide();
	}
	if ($("#MANAGE_CONTENT_DELETE").val() == strings['manage.content.delete']) {
		$('.delete-content').show();
	} else {
		$('.delete-content').hide();
	}
	if ($("#MANAGE_CONTENT_MORE").val() == strings['manage.content.more']) {
		$('#moreDiv').show();
	} else {
		$('#moreDiv').hide();
	}
	
	//Manage Parents
	if ($("#MANAGE_PARENTS_RESET_PWD").val() == strings['manage.parents.resetPwd']) {
		$('.reset-Password').show();
	} else {
		$('.reset-Password').hide();
	}
	if ($("#MANAGE_PARENTS_VIEW_CHILDREN").val() == strings['manage.parents.viewChildren']) {
		$('.view-Children').show();
	} else {
		$('.view-Children').hide();
	}
	
	//Manage Students
	if ($("#MANAGE_STUDENTS_ASSESSMENT").val() == strings['manage.students.assessment']) {
		$('.view-Assessment').show();
	} else {
		$('.view-Assessment').hide();
	}
}

//Create organization DOM table
function buildOrgDOM(checkFirstLoad, data) {
	var orgContent = '';
	if (checkFirstLoad) {
		$("tbody#org_details").find("tr").remove();
		$("#org-list").trigger("update");
	}
	
	$.each(data, function () {
	    
		orgContent += '<tr id ='+ this.parentTenantId+'_'+this.tenantId+' scrollid ='+ this.selectedOrgId+'_'+this.tenantId +' class="abc" >'
						+'<th scope="row">' + this.tenantId +'</th>'
						+'<td>' + this.tenantName +'</td>'
						+'<td>'+this.noOfChildOrgs+'</td>'
						//+ buildRedirectToUserLink(this.tenantId,this.parentTenantId,this.noOfUsers)
		if($("#MANAGE_ORGS_USER_COUNT").val() == strings['manage.orgs.usercount']) {
			orgContent += '<td><span class="button-group compact"><a tenantId="'+this.tenantId+'" id="count_'+this.tenantId+'" orgname="'+this.tenantName+'" parentTenantId="'+this.parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">' +strings['label.userCount']+'</a></span></td>';
		}
		showHideJspElements();
		orgContent += '</tr>';
	});
	$("tbody#org_details").append(orgContent);
	$("#org-list").trigger("update");
	$(".headerSortDown").removeClass("headerSortDown");
	$(".headerSortUp").removeClass("headerSortUp");
	setLastRowId ($("#last_org_tenant"));      //this method is defined in usermodule.js
}
 // click to view number of users in organisation screen
	$(".view-UserNumber").live("click",function(e){
		
		var tenantId=$(this).attr("tenantId");
		var parentTenantId=$(this).attr("parentTenantId");
		var orgname=$(this).attr("orgname");
		var adminYear=$("#AdminYear").val();
		$(this).closest('td').attr("id","count_"+tenantId);
		$(this).closest('td').html('<span class="loader"></span>');
		$.ajax({
			type : "GET",
			url : "getUserCount.do",
			data : "tenantId="+tenantId+"&adminYear="+adminYear,
			dataType : "json",
			cache:false,
			success : function(data){
				
				if (data != null){
				
					var count = buildRedirectToUserLink(tenantId,parentTenantId,data[0].noOfUsers);
					$("#count_"+tenantId).html(count);
					
					//=========OPEN THE MODAL========
					/*$("#organisationModal").modal({
						title: '# of Users for '+orgname,
						resize:true,
						draggable: false,
						onOpen:buildUserCountDom(data,tenantId,parentTenantId,orgname,"organisationModal","organisationModalContainer"),
						buttons: {
								'Close': {
									classes: 'glossy',						
									click: function(win) {
										win.closeModal();
									}
								}
						}
					});*/
					
				}
				else {
					$.modal.alert(strings['msg.err.userCount']);
					var buttonTag ='<span class="button-group compact"><a tenantId="'+tenantId+'" id="count_'+tenantId+'" orgname="'+orgname+'" parentTenantId="'+parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">'+strings['label.userCount']+'</a></span>'
					$("#count_"+tenantId).html(buttonTag);
				}
				
			},
			error : function(data){
				var buttonTag ='<span class="button-group compact"><a tenantId="'+tenantId+'" id="count_'+tenantId+'" orgname="'+orgname+'" parentTenantId="'+parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">'+strings['label.userCount']+'</a></span>'
				$("#count_"+tenantId).html(buttonTag);							
				$.modal.alert(strings['msg.err.userCount']);
				
			}
		});
		
	});

	function buildUserCountDom(data,tenantId,parentTenantId,orgname,modalId,modalContainerDivId){
		
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var content ='<div>'
						+'<p class="big-message" style="width:500px"><b>The # of users for</b>  <strong>'+orgname+'</strong>'+' <b>in</b>  '+'<b><strong>'+data[0].adminName+'</strong>  is </b> '
						+ buildRedirectToUserLink(tenantId,parentTenantId,data[0].noOfUsers)
						+'</p>';
				if (data[0].noOfUsers!="0"){
					 content +='<p class="message icon-speech blue-gradient glossy" style="width:500px">'
									+'<a class="close show-on-parent-hover" title="Hide message" href="#"></a>'	
									+strings['msg.clickRedirectManageUsers']
									+'</p>';
				}				
				content +='</div>';
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(content);				
		
	}

	function buildRedirectToUserLink(tenantId,parentTenantId,noOfUsers)
	{	
		if(noOfUsers !="0"){
			return	'<a style="text-decoration:underline" href="redirectToUser.do?AdminYear='+$("#AdminYear").val()+'&nodeId='+tenantId+'&parentOrgId='+parentTenantId+'" >'+noOfUsers+'</a>'
		}
		else{
			return noOfUsers
		}
	}
/**
 * This js file is to manage role module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */

$(document).ready(function() {
	 //alert('inside ready for mangae role js file');
	 // edit role in Manager Rele screen
	if($('.edit-role').length > 0) {
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-notes").parent().addClass("current");
		
		$('.edit-role').live("click", function(e) {
			//e.stopImmediatePropagation();
			viewRoleUsersDetails($(this).attr('roleId'));	
		});
		
		$('.associate-user').live("click", function() {
			if($('#searchUserId').val().replace(/ /g,'').length > 0){
				associateUserToRole($("#roleId").val(),$('#searchUserId').val());
			}else{
				$.modal.alert(strings['script.role.error']);
			}
		});
		
		$('.dissociate-user').live("click", function() {
			var userId = $(this).attr('userid');
			$.modal.confirm(strings['confirm.deleteUser'] ,
					function () {
						//alert($("#roleId").val());
						//alert(userId);
						dissociateUserFromRole($("#roleId").val(), userId);
					},function() {//this function closes the confirm modal on clicking cancel button
					} 
				);
			
		});
	}
// Arunava to implement more in Manage Role
	
	$("#moreRole").click(function() {
		currentScrollTop = $("#roleTable").scrollTop();
		if(!$(this).hasClass('disabled')) {
			var lastUserId = $("#roleTable tr:last").attr("id");
			var roleId = $("#roleId").val();
			callingAction = 'editRole.do?moreRole=true';
			var paramUrl = 'lastUserId='+lastUserId+'&roleId='+roleId;
			blockUI();
			$.ajax({
				type : "GET",
				url : callingAction,
				data : paramUrl,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if (data != null && data.length > 0){
						getRoleDetails(data);
						enableSorting(true);
						retainUniqueValue();
						unblockUI();
						$("#roleTable").animate({scrollTop: currentScrollTop+600}, 500);
					} else {
						//$("#moreRole").addClass("disabled");
						if($.browser.msie) $("#moreRole").addClass("disabled-ie");
						retainUniqueValue();
						unblockUI();
					}
					if (data != null && data.length < moreCount) {
						//$("#moreRole").addClass("disabled");
						if($.browser.msie) $("#moreRole").addClass("disabled-ie");
					}
				},
				error : function(data) {
					unblockUI();
				}
			});

		} else {
			return false;
		}
		tempScrollTop = currentScrollTop;
	});
	
	
});
// *********** END DOCUMENT.READY ************


//===================== Manage Report screen ===========================
function openModalRoleDetails(jsonData, roleId) {
	resetModalForm('editRoleForm');
	if($.browser.msie) {
		$('#search_icon').find('.font-icon').html('');
		$('#search_icon').find('.font-icon > .empty').hide();
	}
	var roleUserContent = '';
		$("#roleId").val(roleId);
		$.each(jsonData, function () {
			
			$("#roleId").val(this.roleId); 
			$("#roleName").val(this.roleName);
			$("#roleDescription").val(this.roleDescription);
			$("#userList").val(this.userList);
			
			$.each(this.userList, function (index, value) { 
				roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
									+'<td>' + value.userId +'</td>'
									+'<td>'+ value.userName +'</td>'
									+'<td class=""><div class="controls">'
										+'<span class="button-group compact children-tooltip">'
											+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="'+strings['title.removeFromAssociation']+'">'+strings['label.delete']+'</a>'
										+'</span>'
									+'</div></td>'
								+'</tr>';
			});
			$("#role_users_popup").html(roleUserContent);	
		});
		
	$("#roleModal").modal({
		title: 'Edit Role',
		resizable: false,
		draggable: false,
		height: 550,
		width: 550,
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
					clickMe(e);
					win.closeModal(); 
				}
			},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
					//updateRoleDetails($(this).attr('roleId'),$('#roleName').val(),$('#roleDescription').val());
					clickMe(e);
					updateRoleDetails($(".edit-role-form"), win);
				}
			}
		}
	});		
	unblockUI();
}

//----------------------------Update role details---------------------
function updateRoleDetails(form, win) {
	//var dataURL = 'roleId='+roleId+'&roleName='+roleName+'&roleDescription='+roleDescription;
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateRole.do',
		data : form.serialize(),
		dataType: 'html',
		cache:false,
		success : function(data) {
			var obj = jQuery.parseJSON(data);
			win.closeModal(); 
			if(obj.status == 'Success') {
				$.modal.alert(strings['script.role.update']);
				var roleId = $("#roleId").val();
				
				var newRow = '<tr id="'+roleId+'">\
								<th scope="row"><span class="reportName">'+$("#roleName").val()+'</span><br> </th>\
								<td class="vertical-center">'+$("#roleDescription").val()+'</td>\
								<td class="vertical-center">\
									<span class="button-group compact">\
										<a href="#"	class="button icon-pencil with-tooltip edit-role" title="Edit" roleId="'+roleId+'"></a>\
										<a href="#" roleId="'+roleId+'" roleName="'+$("#roleName").val()+'" class="button icon-trash with-tooltip confirm delete-Role"\
											title="Delete"></a>\
									</span>\
								</td>\
							</tr>';
				
				//$("#roleList").find("#"+roleId).remove();
				$("#"+roleId).replaceWith(newRow);
				//$("#roleList").append(newRow);
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
			unblockUI();
		},
		error : function(data) {
			$.modal.alert(strings['script.user.saveError']);
			unblockUI();
		}
	});
}
//----------------------------Update row if update success ---------------------

function viewRoleUsersDetails(roleId) {
	//alert($(form).serialize());
	blockUI();
	var dataURL = "roleId="+roleId;
	$.ajax({
		type : "GET",
		url : 'editRole.do',
		data : dataURL,
		dataType: 'json',
		cache:false,
		success : function(data) {
		//var obj = jQuery.parseJSON(data);
			//alert("data:"+data[0].roleName);
			openModalRoleDetails(data, roleId);
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError1']);
		}
		
	});
}

// ============================= Associate user from edit role =============================
	
function associateUserToRole(roleId, userName) {
		blockUI();
		var dataURL = 'roleId='+roleId+'&userName='+userName;
		$.ajax({
			type : "GET",
			url : 'associateUser.do',
			data : dataURL,
			dataType: 'json',
			cache:false,
			success : function(data) {
				//var obj = jQuery.parseJSON(data);
				//alert('Success, User Associated To Role*** opening modal window');
				//openModal2(data);
				if(data != null) {
					if(data != null && data.status == 'Fail') {
						unblockUI();
						$.modal.alert(strings['script.role.userNotFound']);
					} else if(data != null && data.status == 'RoleAlreadyTagged') {
							unblockUI();
							$.modal.alert(strings['script.role.userAlreadyTagged']);
					}else {
						var roleUserContent = '';
						
						$.each(data, function () {
								//$("#userList").val(this.userList);
										
							$.each(this.userList, function (index, value) { 
								roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
													+'<td>' + value.userId +'</td>'
													+'<td>'+ value.userName +'</td>'
													+'<td class=""><div class="controls">'
														+'<span class="button-group compact children-tooltip">'
															+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="'+strings['title.removeFromAssociation']+'">'+strings['label.delete']+'</a>'
														+'</span>'
													+'</div></td>'
												+'</tr>';				
							});
							$("#role_users_popup").html(roleUserContent);
							
						});	
						unblockUI();
					}
				} else {
					$.modal.alert(strings['script.role.userNotFound']);
					unblockUI();
				}
				
			},
			error : function(data) {
				$.modal.alert(strings['script.user.deleteError']);
				unblockUI();
			}
		});
	}

// ============================= Dissociate user from edit role =============================
	
function dissociateUserFromRole(roleId, userId) {
		blockUI();
		var dataURL = 'roleId='+roleId+'&userId='+userId;
		$.ajax({
			type : "GET",
			url : 'dissociateUser.do',
			data : dataURL,
			dataType: 'json',
			cache:false,
			success : function(data) {
				//alert('Success, User Dissociated From Role*** opening modal window');
				//openModal2(data);
				var roleUserContent = '';
				
				$.each(data, function () {
						//$("#userList").val(this.userList);
								
					$.each(this.userList, function (index, value) { 
						roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
											+'<td>' + value.userId +'</td>'
											+'<td>'+ value.userName +'</td>'
											+'<td class=""><div class="controls">'
												+'<span class="button-group compact children-tooltip">'
													+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="'+strings['title.removeFromAssociation']+'">'+strings['label.delete']+'</a>'
												+'</span>'
											+'</div></td>'
										+'</tr>';
					});
					$("#role_users_popup").html(roleUserContent);
					unblockUI();
				});
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});
	}
//=============================DELETE ROLES ON CLICK======================
$('.delete-Role').live("click", function() {

    var row = $(this);
	var roleId = $(this).attr("roleId");
	var roleName = $(this).attr("roleName");
	$.modal.confirm("Confirm "+$(this).attr("roleName")+" delete?" ,
		function () {
			deleteRoleDetails(roleId, roleName,row);
		},function() {//this function closes the confirm modal on clicking cancel button
		} 
	);
	
});	
//=============================AJAX CALL TO DELETE ROLES FROM DB TABLES=============================
	function deleteRoleDetails(roleId, roleName, row) {
			blockUI();
			$.ajax({
				type : "GET",
				url : 'deleteRole.do',
				data : "roleId="+roleId,
				dataType: 'html',
				cache:false,
				success : function(data) {
					var obj = jQuery.parseJSON(data);
					//win.closeModal(); 
					if(obj.status == 'Success') {
						$.modal.alert('Role '+roleName+' deleted successfully');				
						deleteRowValues(row);//this method is present in manageUser.js
					} else {
						$.modal.alert(strings['script.user.deleteError']);
					}
					unblockUI();
				},
				error : function(data) {
					$.modal.alert(strings['script.user.deleteError']);
					unblockUI();
				}
			});
		}
	
	function getRoleDetails(data) {
		var roleContent = '';
		
		$("#userList").val(this.userList);
		
		$.each(data, function () {
			
			$.each(this.userList, function (index, value) { 
				roleContent += '<tr id ='+ value.userId +' class="reportName" >'
									+'<td>' + value.userId +'</td>'
									+'<td>'+ value.userName +'</td>'
									+'<td class=""><div class="controls">'
										+'<span class="button-group compact children-tooltip">'
											+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="'+strings['title.removeFromAssociation']+'">'+strings['label.delete']+'</a>'
										+'</span>'
									+'</div></td>'
								+'</tr>';
			});
		    
	});
		$("#role_users_popup").append(roleContent);
		$("#report-list").trigger("update");
		$(".headerSortDown").removeClass("headerSortDown");
		$(".headerSortUp").removeClass("headerSortUp");
	}

	function enableSorting(flag) {
		if (flag) {
			var roleTable = $("#report-list");
			roleTable.trigger("update");
		}

	}
	
	


/**
 * This js file is to manage parent module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */

var isInvCoveValid = false;

//Added by Ravi for Claim New Invitation
var firstSubmit = true;
//End

$(document).ready(function() {
	//associateCustomFunction(step);
	$("#manageProfile").validationEngine();	
	$("#registrationForm").validationEngine();
	$("#claim-Invitation-Form").validationEngine();
	$("#changePasswordFrom").validationEngine();
	$("#mandatoryValidation").hide(500);
	
	// load children list at parent home page, after the page loading
	if ($(".children-list").size()>0){
		refreshChildrenList();
		$("ul#shortcuts li").removeClass("current");
		
		//Fix for PRISM-78 - By Joy
		//$(".shortcut-dashboard").parent().addClass("current");
		$(".shortcut-messages").parent().addClass("current");
	}
	
	// select manage shortcut in menu for my account
	if ($(".manageAccount").size()>0){
		if($.browser.msie) {
			$('.label').css('width', '200px');
		}
		
		//As refreshChildrenList() has been used for PN only so reduce unnecessary call for Admin/Teacher login - By Joy
		if($("#parentMenu").length){
			refreshChildrenList();
		}
		
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-stats").parent().addClass("current");
	}
	
	//==================== Format Phone number =================
	$('#formatPh').live("click", function() {
		var separator = '-';
		var mobileNo= $('#mobile').val();
		$('#mobile').val(mobileNo.replace( /[^\d]/g, '' ).replace( /(\d{3})(\d{3})(\d{4})/, '$1' + separator + '$2' + separator + '$3' ));
	});
	
	//=================== SAVE MY PROFILE =======================
	$('.save-Profile').live("click", function() {
		if($("#manageProfile").validationEngine('validate')) {
			blockUI();
			
			if($('#password').val() != "" && $('#password_again').val() == ""){
				unblockUI();
				$.modal.alert("verify password can not be empty");
			}  else{
				if( $("#firstName").val() != "" && $("#lastName").val() != "" && $("#verify_mail").val() != ""
					&& $("#ans1").val() != "" && $("#ans2").val() != "" && $("#ans3").val() != "" ) {
					$('#manageProfile').validationEngine('hide');
					$("#mandatoryValidation").hide(500);
					
					if ( ($("span.qsn1").children(".select-value").text()==$("span.qsn2").children(".select-value").text())
						||($("span.qsn1").children(".select-value").text()==$("span.qsn3").children(".select-value").text())||($("span.qsn2").children(".select-value").text()==$("span.qsn3").children(".select-value").text())
						){
						unblockUI();
						$.modal.alert(strings['script.parent.question']);
					}
					else{
						saveParentProfileInfo($(".manage-Profile-form"));
					}	
				}
				else{
					$("#mandatoryValidation").show();
					unblockUI();
				}				
			}			
					
		} else {
			$('#manageProfile').validationEngine('show');
		}
	});
	
	// -------------------------- reset invitation code -------------------------
	$("#invitationCode").keypress(function() {
		isInvCoveValid = false;
		$("#invalidICMsg").hide(500);
	});
	
	// -------------------------- code to align wizard -------------------------
	$("#completed").live('click', function() {
		$("#command").css('margin-top', '0px');
	});
	$("#wizard-previous").live('click', function() {
		$("#command").css('margin-top', '0px');
	});
	$("#wizard-next").live('click', function() {
		$("#command").css('margin-top', '0px');
	});
	
	//Fix for TD 78174 - By Joy
	$('.wizard-previous').live('click', function() {
		var step = $('#registrationForm ul.wizard-steps li.active span.wizard-step').html();
		if(step == "1"){
			isInvCoveValid = false;
		}
	});
	
	//Added by Ravi for Claim New Invitation
	/*$('.claim-Invitation').live("click", function() {
		$("input#invitationCode").val("");
		$("#invalidICMsg").hide();
		firstSubmit=true;
		openNewInvitationClaimModal();	
	});*/
	$('.claim-Invitation').click(function() {
		$("input#invitationCode").val("");
		$("#invalidICMsg").hide();
		firstSubmit=true;
		openNewInvitationClaimModal();	
	});
	//End
	
	//============================= OPEN clildren home page report on-clicking menu =============================
	$(".child_report_menu").live("click", function() {
		var loc = $(this).attr("href");
		location.href = loc;
	});
});
	// *********** END DOCUMENT.READY ************

	//============================= Function to fetch ChildrenList =============================
	function refreshChildrenList() {
		blockUI();
		$(".children-list").addClass("loader big");
		$.ajax({
			type:"GET",
			url:"getChildrenList.do",
			dataType:'json',
			cache:false,
			success:function(data){
				if(data != null) {
					var userContent = '';
					var menuContent = '';
					$.each(data, function () {
						var studentNameToDisplay = getStudentNameToDisplay(this.studentName);
						var gradeToDisplay = replaceString(this.grade, "Grade ", "");
						//Implement Joy: Before GRT loaded functionality - By Joy
						if(this.bioExists != 0){
							userContent += '<a href="getChildData.do?testElementId='+this.testElementId
											+'&studentBioId='+this.studentBioId
											+'&studentName='+studentNameToDisplay
											+'&studentGradeName='+this.grade
											+'&studentGradeId='+this.studentGradeId+'" style="color: #fff; font-weight: bold">'
											+ studentNameToDisplay + '</a><br/>'
											+ this.administration + ', Grade: ' + gradeToDisplay + '<br/><br/>';
				
							menuContent += '<li class="menu-third-level"><a class="child_report_menu" href="getChildData.do?testElementId='+this.testElementId
											+'&studentBioId='+this.studentBioId
											+'&studentName='+studentNameToDisplay
											+'&studentGradeName='+this.grade
											+'&studentGradeId='+this.studentGradeId+'">'
											+studentNameToDisplay+' (Grade: ' + gradeToDisplay + ')</a></li>';
						}else{
							userContent += '<span style="color: #fff; font-weight: bold">'
								+studentNameToDisplay+'</span></br>'
								+ this.administration + ', Grade: ' + gradeToDisplay + '<br/><br/>';
							
							menuContent += '<li class="menu-third-level"><span>'
								+studentNameToDisplay+' (Grade: '+ gradeToDisplay + ')</span></li>';
						}
						
					});
					$(".children-list").html(userContent);
					$(".children-list").removeClass("loader big");
					
					$("#child-holder").customScroll({
						showOnHover : false,
						animate : false
					});
					// update right menu
					$("#child_list").html(menuContent);
				}else{
					var userContent = '<div>No child is associated to your account.</div>';
					$(".children-list").html(userContent);
					$(".children-list").removeClass("loader big");
				}
				unblockUI();
			},
			error:function(data){
				unblockUI();
				$.modal.alert(strings['script.parent.getChildrenError']);
				$(".children-list").removeClass("loader big");
			}
			
		});
	}

	//============================= Registration next button click =============================
	function associateCustomFunction(step) {
		if(step == 0) {//1
			// * Moved to wizard.register.js
			//removePreviousChild();
			//getChildList();
		} 
		else if(step == 1) {//2
		
			CheckUserNameAvailability("input#username");
			//$("#registrationForm").validationEngine('hide');
			//removePromptOnBack();
		}
		else if(step == 2) {//3
			//$("#registrationForm").validationEngine('hide');
			//validatePwd($("#registrationForm input#password"),$("#registrationForm input#username"),$("#registrationForm"));
			//removePromptOnBack();
		}
		else if(step == 3) {//4
			//$("#registrationForm").validationEngine('hide');
			//removePromptOnBack();
			
		}
		else if(step == 4) {
			//$("#registrationForm").validationEngine('hide');
			//removePromptOnBack();
		}
		
		$("#command").css('margin-top', '0px');
	}
	//============================= Registration First Next button click =============================
	function removePreviousChild() {
		$("#ic_student_list").find("p").remove();
	}
	//============================= Get Child List for Invitation Code ================================
	function getChildList(fromLogin) {
		var ic = "invitationCode=" + $("input#invitationCode").val()+ "&fromLogin=" +fromLogin;
		var student_details = "";
		blockUI();
		$.ajax({
			type : "GET",
			url : "regn/validateCode.do",
			data : ic,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				if(data[0].errorMsg == "NA" ) {		
					$("#invalidICMsg").hide();
					isInvCoveValid = true;
					var studentList = data[0].studentToList;
					$.each(studentList, function(index, value) { 
						student_details += '<p class="margin-bottom" id="student_info">'
											+'<strong>'+ studentList[index].studentName+'</strong>'
											+'<br>'+studentList[index].administration
											+'<br>'+studentList[index].grade
											+'<br>'+studentList[index].schoolName
											+'<br>'+studentList[index].orgName
											+'</p>'; 
					});
					$("#ic_student_list").html(student_details);
					$("#displayChild").show();
					$("#displayInvitation").hide();
					firstSubmit=false;
					$.each( $('.wizard-next'), function(index, value) { 
						if(index == 0) $(this).click();
					});
				} else if (data[0].errorMsg == "IC_INVALID" ) {
					isInvCoveValid = false;
					$("#invalidICMsg").show(500);
					$("#invalidICMsg").text("The code you have entered is not valid. Make sure you have entered exactly the same code as you received in letter.");
					$("#displayChild").hide();
					$("#displayInvitation").show();
				} else if (data[0].errorMsg == "IC_NOTAVAILABLE" ) {
					isInvCoveValid = false;
					$("#invalidICMsg").show(500);
					$("#invalidICMsg").text("The code you have entered has reached its maximum number of claims.");
					$("#displayChild").hide();
					$("#displayInvitation").show();
				} else if (data[0].errorMsg == "IC_EXPIRED" ) {
					isInvCoveValid = false;
					$("#invalidICMsg").show(500);
					$("#invalidICMsg").text("The code you have entered is expired.");
					$("#displayChild").hide();
					$("#displayInvitation").show();
				} 
				
				//Fix for TD 78161 - By Joy
				else if (data[0].errorMsg == "IC_ALREADY_CLAIMED" ){
					isInvCoveValid = false;
					$("#invalidICMsg").show(500);
					$("#invalidICMsg").text("The code you have entered is already claimed by you.");
					$("#displayChild").hide();
					$("#displayInvitation").show();
				} 
				
				else {
					isInvCoveValid = false;
					$("#invalidICMsg").show(500);
					$("#invalidICMsg").text("The code you have entered is not valid. Make sure you have entered exactly same code as you received in letter.");
					$("#displayChild").hide();
					$("#displayInvitation").show();
				}
			},
			error : function(data) {
				unblockUI();
				isInvCoveValid = false;
				$("#displayChild").hide();
				$("#displayInvitation").show();
			}
		});
	}
	
	//============================= Return If Code validation is success ================================	
	function isInvCoveValid() {
		return isInvCoveValid;
	}
	
	//============================= Check Availability of the username on blur ================================	
	function CheckUserNameAvailability(fieldId) {
		//$("#imgHolder").find("span").remove();
		$(fieldId).blur(function(e) {
			e.stopImmediatePropagation();
			var pattern = /^[0-9a-zA-Z]+$/g;
		    var username = "username=" + $(fieldId).val();
			if($(fieldId).val().replace(/ /g,'').length > 2 && pattern.test($(fieldId).val())) {
				 showLoadingImage();
				  $.ajax({
						type : "GET",
						url : "regn/checkusername.do",
						data : username,
						dataType : 'json',
						cache:false,
						success : function(data) {
							unblockUI();
							//alert(data.available);
							if (data.available == "true") {
								removeLoadingImage();
								showAvailability();
								return true;
							}
							else if (data.available == "false") {
								removeLoadingImage();
								showUnAvailability();
								return false;
							}
						},
						error : function(data) {						
							unblockUI();
							showUnAvailability();
							return false;
						}
					});
				}/*
				else {
					$("span#imgHolder").html('<!--<img src="themes/acsi/img/standard/loaders/loading16.gif">-->');
					$("span#imgHolder").html('<span class="icon-user icon-size2 icon-red" style="color: red;">Please provide valid username</span>');
				}*/
		});	
		
	}
	//Show Loading image
	function showLoadingImage(){
	   $("span#imgHolder").html('<img src="themes/acsi/img/standard/loaders/loading16.gif">');	
	}
	//Remove Loading image	
	function removeLoadingImage(){
	   $("span#imgHolder").html('');
	}
	//Show if userName is available	
	function showAvailability(){
		$("span#imgHolder").html('<span id="validated" class="validated" style="color: green;">Username available.</span>');
		$("#addNewUser").validationEngine('validate');
	}
	//Show if userName is not available	
	function showUnAvailability(){
		$("span#imgHolder").html('<span class="" style="color: red;">Username already present. Please choose other.</span>');
		$("#addNewUser").validationEngine('validate');
	}
	//Remove the alert prompt of User Profile
	function removePromptOnBack()
	{
			$("button.wizard-previous").click(function(){
			$(".formErrorContent").remove();
			$(".formErrorArrow").remove();
		
		});
	}
	
	//Added by Ravi for Manage Profile
	function saveParentProfileInfo(form) {
		//alert(form.serialize());
		$.ajax({
			type : "POST",
			url : 'updateProfile.do',
			data : form.serialize(),
			dataType : 'html',
			cache:false,
			success : function(data){
				unblockUI();
				var obj = jQuery.parseJSON(data);
				if(obj.status == 'equalsUserName'){
					$.modal.alert(strings['script.user.passwordLikeUsername']);	
				}
				else if(obj.status == 'invalidPwd') {
					$.modal.alert(strings['script.user.passwordPolicy']);
				}
				else if(obj.status == 'invalidPwdHistory') {
					$.modal.alert(strings['script.user.passwordPolicyHistory']);
				}
				else if(obj.status == 'invalidPhone') {
					$.modal.alert(strings['script.user.phone']);
				}
				else if(obj.status == 'LDAP_ERROR') {
					$.modal.alert(obj.message);
				}
				else if(obj.status == 'Success') {
					$.modal.alert(strings['script.student.updateSuccess']);
				}
				else{
					$.modal.alert(strings['script.user.saveError']);
				}
				
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}
	
	//Added by Ravi for Claim New Invitation
	function openNewInvitationClaimModal(){
		$("#newICModal").modal({
			title: 'Enter Invitation Code',
			height: false,
			width: 670,
			resizable: false,
			draggable: false,
			buttons: {
				'Cancel': {
					classes: 'glossy',
					click: function(win) {
						$('#claim-Invitation-Form').validationEngine('hide');
						if($.browser.msie) setTimeout("hideICMessage()", 300);
						win.closeModal(); 
					}
				},
				'Submit': {
					classes: 'blue-gradient glossy',
					click: function(win) {
						// Submit clicked for the first time for validating IC
						if(firstSubmit){
							getChildList('N');
						}
						// Submit clicked for the second time for verify child info and insert an entry in invitation_code_claim table
						else{
							var invCode = $("input#invitationCode").val();
							verifyChildInfoAndSaveIC(invCode, win);
						}
	
					}
				}
			},
			onClose: function() {
				$('#claim-Invitation-Form').validationEngine('hide');
				if($.browser.msie) setTimeout("hideICMessage()", 300);
			}
		});		
		$("#displayChild").hide();
		$("#displayInvitation").show();
	}
	function hideICMessage() {
		$('#claim-Invitation-Form').validationEngine('hide');
	}
	//End
	
	//Added by Ravi for Claim New Invitation
	function verifyChildInfoAndSaveIC(invCode, win) {
		blockUI();
		//alert(form.serialize());
		var dataURL = "invitationCode="+invCode;
		$.ajax({
			type : "GET",
			url : 'claimInvitation.do',
			data : dataURL,
			dataType: 'html',
			cache:false,
			success : function(data) {
				unblockUI();
				var obj = jQuery.parseJSON(data);
				win.closeModal(); 
				if(obj.status == 'Success') {
					$.modal.alert(strings['script.parent.invitationCode']);
					
					//calling ajax to refresh children list
					refreshChildrenList();
					
				} else if(obj.status == 'Success') { 
					$.modal.alert('Multiple user session detected. Please logout from all user sessions and relogin with only one user at a time.');
				} else {
					$.modal.alert(strings['script.parent.alreadyClaimed']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.common.error1']);
			}
		});
	}
	//End
	
	

	//========================CHANGE PASSWORD FORM SUBMISSION=====================	
	$("#saveChangedPassword").click(function(e){
		e.stopImmediatePropagation();
		$("#loggedInUserName").val($("#changePasswordFrom #currUser").text());
		//alert($("#changePasswordFrom #currUser").text());
		validatePwd($("#changePasswordFrom input#password"),$("#changePasswordFrom input#loggedInUserName"),$("#changePasswordFrom"),$("#changePasswordFrom input#mobile"), '0');
		//$("#changePasswordFrom").submit();
	});	
//========================REGISTRATION FORM SUBMISSION=====================	
	$("#regSubmit").click(function(e){
		//Fix for TD 78521 - By Joy
		if($("#usernameDiv #imgHolder > #validated").hasClass("validated") || $("#usernameDiv #imgHolder").html() == ""){
			e.stopImmediatePropagation();
			validatePwd($("#registrationForm input#password"),$("#registrationForm input#username"),$("#registrationForm"),$("#registrationForm input#mobile"));
		}else{
			stepBack(2);
			e.stopImmediatePropagation();
		}
		
		//$("#registrationForm").submit();
	});
//=============================PREVENTING REGISTRATION FORM SUBMIT ON ENTER===========================	
	$('.noEnterSubmit').keypress(function(e){
        if ( e.which == 13 ) e.preventDefault();
		});
		
//=============================AJAX CALL TO VALIDATE PASSWORD===========================			
	function validatePwd(pwdObj,useNameObj,formObj,phObj, pwdPage)
	{
		if(formObj.validationEngine('validate')){
		 blockUI();
		}
		
		var password=pwdObj.val();
		var username=useNameObj.val();
	
		if(password.indexOf(username) != -1) {
			unblockUI();
			$.modal.alert(strings['script.user.passwordPartUsername']);
		} else {
			$.ajax({
				type : "POST",
				url : 'validatePwd.do',
				data : formObj.serialize(),
				dataType : 'html',
				cache:false,
				success : function(data) {
					
					var obj = jQuery.parseJSON(data);
				
					if (obj.status == 'equalsUserName')	{
						$.modal.alert(strings['script.user.passwordLikeUsername']);
						stepBack(2);
						if(pwdPage != null) stepBack(2);
						unblockUI();
					}
					else if(obj.status == 'invalidPwd'){
						$.modal.alert(strings['script.user.passwordPolicy']);
						stepBack(2);
						if(pwdPage != null) stepBack(2);
						unblockUI();
					}
					else if(obj.status == 'invalidPwdHistory') {
						$.modal.alert(strings['script.user.passwordPolicyHistory']);
						stepBack(2);
						if(pwdPage != null) stepBack(2);
						unblockUI();
					}
					else if(obj.status == 'LDAP_ERROR'){
						$.modal.alert(obj.message);
						stepBack(2);
						if(pwdPage != null) stepBack(2);
						unblockUI();
					}
					else if(obj.status == 'Success') {
						validatePh(phObj,formObj);
					   /*	if (validateSecurityQuestion()){
							formObj.submit();
						}else{
							$.modal.alert(strings['script.parent.question']);
							unblockUI();
						}*/
							
					} else {
						$.modal.alert(strings['script.user.saveError']);
						stepBack(2);
						if(pwdPage != null) stepBack(2);
						unblockUI();
					}
				},
				error : function(data){
					unblockUI();
					$.modal.alert(strings['script.common.error1']);
					
				}
			});
		}		
}

//=============================AJAX CALL TO VALIDATE PHONE===========================	
function validatePh (phObj,formObj)
{
	if(formObj.validationEngine('validate')){
	 blockUI();
	}
	var phone =phObj.val();
		$.ajax({
			type : "GET",
			url : 'validatePhone.do',
			data : 'phone='+phone,
			contentType: "application/json",
			dataType : 'html',
			cache:false,
			success : function(data) {
				
				var obj = jQuery.parseJSON(data);
			
				if(obj.status == 'invalidPhone'){
					$.modal.alert(strings['script.user.phone']);
					stepBack(1);					
					unblockUI();
				} else if(obj.status == 'Success') {
				   	if (validateSecurityQuestion()){
						formObj.submit();
					}else{
						$.modal.alert(strings['script.parent.question']);
						unblockUI();
					}
						
				} else {
					$.modal.alert(strings['script.user.saveError']);
					stepBack(1);
					unblockUI();
				}
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.common.error1']);
				
			}
		});
	
}
	
	
	function stepBack(step) {
		$.each( $('.wizard-previous'), function(index, value) { 
		if(index < step) {
			//alert("index="+index+",step="+step);
			$(this).click();			
		}
		});
		
	}
	//=======================DISABLES THE ERROR PROMTS WHILE CLICKING THE BACK BUTTON==============
	$("button.wizard-previous").live("click",function(){
		$("#registrationForm").validationEngine('hide');
		$("#changePasswordFrom").validationEngine('hide');
	});
	//=======================DISABLES THE ERROR PROMTS WHILE TRAVERSING BACK CLICKING THE WIZARD TABS==============
	$("ul.wizard-steps li.completed").live("click",function(e){
			//e.stopImmediatePropagation();
		$("#registrationForm").validationEngine('hide');
		$("#changePasswordFrom").validationEngine('hide');
	});
	//================================VALIDATES WHETHER ALL THE THREE DIFFERENT QUESTIONS ARE DIFFERENT============
	function validateSecurityQuestion(){
		if ( ($("span.qsn1").children(".select-value").text()==$("span.qsn2").children(".select-value").text())
				||($("span.qsn1").children(".select-value").text()==$("span.qsn3").children(".select-value").text())||($("span.qsn2").children(".select-value").text()==$("span.qsn3").children(".select-value").text())
				){ 
				 return false;
				 }
				 else {
				 return true;
				 }
	}
	function replaceString(str, old, now) {
		return str.replace(old, now);
	}
	function endsWith(str, suffix) {
	    return str.indexOf(suffix, str.length - suffix.length) !== -1;
	}
	function getStudentNameToDisplay(name){
		var studentNameToDisplay = "";
		if(endsWith(name, ", ") == true) {
			studentNameToDisplay = name.substring(0, name.length-2);
		} else if(endsWith(name, " ") == true) {
			studentNameToDisplay = name.substring(0, name.length-1);
		} else if(endsWith(name, ",") == true) {
			studentNameToDisplay = name.substring(0, name.length-1);
		} else {
			studentNameToDisplay = name;
		}
		return studentNameToDisplay;
	}
	/**
	 * This js file is to manage parent admin module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
	
	$(document).ready(function() {
		$('.view-Children').live("click", function() {
			resetModalForm("editParent");
			openParentModalToViewStudent($(this).attr("parentName"),$(this).attr("clickedTreeNode"),$(this));//,$(this).attr("tenantId")
		});
		$('.reset-Password').live("click", function() {
			resetModalForm("editParent");
			openResetPasswordModal($(this).attr("parentName"),$(this).attr("parentDisplayName"));
			
		});
		
		//$("#menuFindStudents").parent().addClass("current");
		$('.shortcut-stats').live("click", function(e) {
			e.stopImmediatePropagation();
			resetModalForm("editParent");
		/*	$('.clearfix').addClass('menu-hidden');
			$("ul#shortcuts li").removeClass("current");
			$('.shortcut-stats').parent().addClass("current");*/
		});
		
		//$('input#lastParentName').val(""); //clearing the value on load
		if($("#parentTable").length > 0) {
			var parents = $(".parent-list-result");
			if(parents != null && parents.length > 0) {
				$('#report-list').tablesorter({
					headers: {
						//Change for TD 77643 - By Joy
						3: { sorter: false }
					}/*,
					sortList: [[0,1]]*/
				});
			}
			if(parents != null && parents.length < moreCount) {
				// hide more button if the parent list is less than 15
				$(".pagination").hide(200);
			}
			$('.clearfix').addClass('menu-hidden');
			$("ul#shortcuts li").removeClass("current");
			$(".shortcut-medias").parent().addClass("current");
		//========================SCROLL FOR THE PARENT TABLE===============================	
			var tempScrollTop, currentScrollTop = 0
			//$("#parentTable").scroll(function() {
			$("#moreParent").click(function() {
				currentScrollTop = $("#parentTable").scrollTop();
				//if (tempScrollTop < currentScrollTop) {
				if(!$(this).hasClass('disabled')) {
					blockUI();
					//scrolling down15
					var lastid ="tenantId=" + $('input#lastParentName').val() + "&searchParam="+$("#searchParent").val(); 
					//showLoader(currentScrollTop);//This method is defined in usermodule.js
					$.ajax({
						type : "GET",
						url : "getParentDetailsOnScroll.do",
						data : lastid,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data != null && data.length > 0){
								if(getParentDetails(false,data)){
									enableSorting(true);
								}
								retainUniqueValue();
								//hideLoader();//This method is defined in usermodule.js
								unblockUI();
								$("#parentTable").animate({scrollTop: currentScrollTop+600}, 500);
							} else {
								$("#moreParent").addClass("disabled");
								if($.browser.msie) $("#moreParent").addClass("disabled-ie");
								unblockUI();
							}
							if (data != null && data.length < moreCount) {
								// check if this is the last set of result
								$("#moreParent").addClass("disabled");
								if($.browser.msie) $("#moreParent").addClass("disabled-ie");
							}
						},
						error : function(data) {
							$.modal.alert(strings['script.org.error']);
							//hideLoader();//This method is defined in usermodule.js
							unblockUI();
						}
					});
		
				} else {
					return false;
				}
				tempScrollTop = currentScrollTop;
			});
			
			/*Fixed for TD  77675 Start - By Joy
			 * Use notification instead of message in page
			 */
			notify('Notification!', 'Parent users are associated with <b>School</b> level (not with Class/Teacher level).', {
				system:				true,
				vPos:				'top',
				hPos:				'center',
				autoClose:			false,
				icon:				'themes/acsi/img/demo/icon.png',
				iconOutside:		true,
				closeButton:		true,
				showCloseOnHover:	true,
				groupSimilar:		true
			});
			//Fixed for TD  77675 End - Implementation change - By Joy
		}
	});
	
	//======================================== Create parent row DOM======================================
	function getParentDetails(checkFirstLoad,data) {
		var manageParents_resetPwd = $('#MANAGE_PARENTS_RESET_PWD').val();
		var manageParents_viewChildren = $('#MANAGE_PARENTS_VIEW_CHILDREN').val();
		var parentContent = '';	
		if (checkFirstLoad) {
			$("tbody#parent_details").find("tr").remove();
			$("#report-list").trigger("update");
		}
		
		$.each(data, function () { 
			parentContent += '<tr id ='+ this.userName +' class="abc" scrollid='+this.clikedOrgId+'_'+this.userName+'>'
								+'<th scope="row">' + createStatusTag(this.status) + this.userName +'</th>'
								+'<td>' + this.displayName +'</td>'
								+'<td>'+this.orgName+'</td>';
								
			if(manageParents_resetPwd == strings['manage.parents.resetPwd'] || manageParents_viewChildren == strings['manage.parents.viewChildren']){
				parentContent += '<td class="vertical-center">'
									+' <span class="button-group compact">';
				parentContent += '<a id="'+ this.userId + '" parentName="'+ this.userName +'" parentDisplayName="'+this.displayName
										+'" href="#" class="button icon-lock with-tooltip reset-Password" title="Reset Password"></a>';
				parentContent += '<a id="'+ this.userId + '" parentName="'+ this.userName +'" clickedTreeNode="'+  this.clikedOrgId 
										+'" orgId="'+  this.orgId +'" isPN="N" href="#" class="button icon-users icon with-tooltip view-Children" title="View Children"></a>';
				parentContent += 		'</span>'
								+'</td>';
			}
								
			parentContent +='</tr>' ;
						
						 
		});
		$("#parent_details").append(parentContent);
		setLastRowId ($("#lastParentName"));
		 
		// update table sorting 
		//$("#report-list").trigger("update");
		//$(".headerSortDown").removeClass("headerSortDown");
		//$(".headerSortUp").removeClass("headerSortUp");
		//var sorting = [[0, 1]];
		//$("#report-list").trigger("sorton", [sorting]);
		showHideJspElements();
		return true;
	}
	
	//======================================== CREATE STUDENT ROW DOM======================================
	function getStudentDetails(checkFirstLoad,data) {
		var MANAGE_STUDENTS_ASSESSMENT = $('#MANAGE_STUDENTS_ASSESSMENT').val();
		var studentContent = '';
		if (checkFirstLoad){
			$("tbody#student_details").find("tr").remove();
			$("#report-list").trigger("update");
		}
		$.each(data, function () { 
			if(this.grade == 'AD') {
				studentContent += '<tr id ="'+ this.studentName +'" scrollid="'+this.rowIndentifier+'|'+this.clikedOrgId+'" class="abc" >'
				+'<th scope="row">' + this.studentName +'</th>'
				+'<td>' + this.grade +'</td>'
				+'<td>'+checkForNull(this.orgName)+'</td>'
				+'</tr>' ;
			} else{
				studentContent += '<tr id ="'+ this.studentName +'" scrollid="'+this.rowIndentifier+'|'+this.clikedOrgId+'" class="abc" >'
				+'<th scope="row">' + this.studentName +'</th>'
				+'<td>' + this.grade +'</td>'
				+ createParentTag(this.parentAccount)
				+'<td>'+checkForNull(this.orgName)+'</td>';
				
				if(MANAGE_STUDENTS_ASSESSMENT == strings['manage.students.assessment']){
					studentContent += '<td class="vertical-center">'
					+' <span class="button-group compact">' 
					+' <a id="'+ this.studentBioId + '" testelementid="'+this.testElementId+'" parentName="'+ this.studentName + '" href="#" class="button with-tooltip view-Assessment" title="View Assessment"><span class="icon-pages"></span> Assessment</a>'  
					+' </span>'
					+'</td>';
				}
				
				studentContent += '</tr>' ;
			}			
						 
		});
		$("#student_details").append(studentContent);
		setLastRowId ($("#lastStudentId"));//This method is defined in usermoudle.js
		 
		// update table sorting 
		$("#report-list").trigger("update");
		$(".headerSortDown").removeClass("headerSortDown");
		$(".headerSortUp").removeClass("headerSortUp");
		//var sorting = [[0, 1]];
		//$("#report-list").trigger("sorton", [sorting]);
		showHideJspElements();
	}
	//====================================AJAX CALL TO FETCH ALL THE PARENTS FOR THE SELECTED ORG====================
	function fetchAllParentUsers(id,url)
	{
		blockUI();
		var nodeid = "tenantId=" + id;
		$("tbody#parent_details").find("tr").remove();
		$("#report-list").trigger("update");
		$("tbody#parent_details").addClass("loader big");
		$.ajax({
			type : "GET",
			url : url,
			data : nodeid,
			dataType : 'json',
			cache:false,
			async : false,
			success : function(data) {
				unblockUI();
				if(data.length >= 1){
					showHideDataTable('show');
				}
				if (data != null && data.length >= moreCount){
					$(".pagination").show(200);
					$("#moreParent").removeClass("disabled");
					if($.browser.msie) $("#moreParent").removeClass("disabled-ie");
				} else {
					$(".pagination").hide(200);
				}
				if (data != null && data.length == 0) {
					$("span#showOrgNameParent").text(' ');
				} else {
					$("span#showOrgNameParent").text('Parents of '+$("a.jstree-clicked").text());
				}
				getParentDetails(true, data);
				enableSorting(true);
				$("tbody#parent_details").removeClass("loader big");				
			},
			error : function(data) {
				unblockUI();
				$("tbody#parent_details").removeClass();	
				$.modal.alert(strings['script.org.error']);
			}
		});
	}
	
	
	//=========================================NULL CHECKING FOR UNDEFINED JSON KEY VALUE==================
	function checkForNull (value)
	{
		if(value != null)
		return value;
		else 
		return "";
		
	}
	
	//==========================THIS FUNCTION CREATES LIST OF PARENTS TO BE DISPALYED FOR EACH STUDENT============================
	function createParentTag(parentJson) {
	var parentContent = '<td>';
	if (parentJson) {
		var size = parentJson.length;
		$.each(parentJson, function(index) {
			if (!(index + 1 == size)) {
				parentContent += this.userName + ', ';// '<p>'+this.userName+'</p>';
			} else {
				parentContent += this.userName;
			}
		});
	}
	parentContent += '</td>';

	return parentContent;
}
	
	
	
	//======================OPEN VIEW CHILDREN  SCREEN==========================================
	function openParentModalToViewStudent(parentName,clickedTreeNode,$obj) {
		//var row = $("#"+parentName);
		var row = $("[id='"+parentName+"']");
		var nodeid = "parentName=" + parentName 
						+"&clickedTreeNode="+clickedTreeNode 
						+"&isPN="+$obj.attr('isPN')
						+"&orgId="+$obj.attr('orgId');	
		blockUI();
		$.ajax({
				type : "GET",
				url : "getChildrenList.do",
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if (data!= null) {
					//=========OPEN THE MODAL========
						$("#parentModal").modal({
							title: 'View Children for <b>'+ parentName + '</b>',
							height: false,
							width: 410,
							resize:false,
							draggable: false,
							onOpen:buildStudentTableDom(data,"parentModal","parentModalContainer"),
							buttons: {
									'Close': {
										classes: 'glossy',
										click: function(win) {
											win.closeModal();
										}
									}
							
							}
						});
					}
					else {
						$.modal.alert(strings['script.parent.noChildren']);
					}
					unblockUI();
				},
				error : function(data) {
					unblockUI();
					$.modal.alert(strings['script.common.error']);
				}
			})	
				
		}
	
											
	//==========================CREATES THE LIST STRUCTURE FOR CHILDREN VIEW=======================
	function createParentModalDivContent(jsonData,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId + ">" +"p.message").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("table").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("dl").remove();
		var makeViewStudentDom='<dl class="accordion same-height " style="width:340px">';
		$.each(jsonData, function () { 
			makeViewStudentDom +='<dt class="closed blue-gradient glossy"><span class="white"> '+ this.studentName+' </span></dt>'
										  +'<dd style="height: 150px; ">'
										  	+'<div class="with-padding">'
											  		+'<p class="button-height inline-label">'
											  				+'<label class="label" for="studentAdminSeason"> '+strings['label.AdminSeason']+' </label>'
											  					+'<label class="full-width newReportName" id="studentAdminSeason"> '+this.administration+' </label>'
											  		+'</p>'	
											  		+'<p class="button-height inline-label">'
											  				+'<label class="label" for="studentAdminGrade"> '+strings['label.Grade']+' </label>'
											  					+'<label class="full-width newReportName" id="studentAdminGrade"> '+this.grade+' </label>'
								  					+'</p>'	
								  			+'</div>'
								  		 +'</dd>';
							  	
					});
		makeViewStudentDom += '</dl>';
		makeViewStudentDom += '<p class="message" style="width:329px">'
								+'<span class="big-message-icon icon-speech left-side with-text blue"></span>'
								+'<span class="blue">'+strings['msg.clickStudent']+'</span>'
								+'</p>';
							
		
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(makeViewStudentDom);				
	}
	
	//Modified by Joy
	//==========================CREATES THE TABULAR STRUCTURE FOR CHILDREN VIEW=======================
	function buildStudentTableDom(jsonData,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId + ">" +"p.message").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("dl").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("table").remove();
		var makeViewStudentTableDom = '<table id="studentTable" class="table " style="width:400px">'
										+'<thead class ="table-header blue-gradient glossy ">'
										+'<tr >'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.studentName']+'</span></th>'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.adminSeason']+'</span></th>'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.grade']+'</span></th>'
										+'</tr>'
										+'</thead>'
										+'<tbody>';
		$.each(jsonData, function () { 
		
						makeViewStudentTableDom += '<tr>'
													+'<td>'
													+getStudentNameHTML(this)
													+'</td>'
													+'<td>'+this.administration +'</td>'
													+'<td>'+this.grade +'</td>'
													+'</tr>';
							});
							
		makeViewStudentTableDom += '</tbody></table>';
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(makeViewStudentTableDom);		
				
	}
	
	function getStudentNameHTML(obj){
		var studentNameHTML = "";
		if(obj.bioExists == 1){
			studentNameHTML += '<a href="redirectToStudent.do?AdminYear='
				+obj.adminid+'&studentBioId='+obj.testElementId+'&nodeId='+obj.clikedOrgId+'" >'
				+obj.studentName 
				+'</a>'
		}else{
			studentNameHTML += '<span class="with-tooltip tooltip-left" title="'+strings['msg.studentNotAvailable']+'">'
				+obj.studentName+'</span>';
		}
		return studentNameHTML;
	}
	
	
	
	
	
	
	//------ auto completing the search field
	$("#searchParent").autocomplete({
		source: function(request, response) {
            $.ajax({
                url: 'searchParentAutoComplete.do',
                dataType: "json",
                cache:false,
                data: {
                    term : request.term,
                    selectedOrg : $("a.jstree-clicked").parent().attr("id"),
                    AdminYear : $("#AdminYear").val()
                },
                success: function(data) {
                    response(data);
                }
            });
        },
		minLength:3,
		position:{my:"right top",at:"right bottom"},
		open: function(event, ui) {
			$(".ui-autocomplete").css({"max-height":"150px", "width":"186px"});
			$(".ui-autocomplete").addClass("white-gradient");
			if($.template.ie7 || $.template.ie8) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"201px", "z-index":"1000000", "overflow" : "auto"});
			} else {
				$('.ui-autocomplete').customScroll({
					horizontal : true,
					showOnHover : false,
					width: 10
				});
			}
		},
		select:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchParent").val(searchString);
			}
			searchParent(searchString,'other','Y');
			event.preventDefault();
			event.stopPropagation();
			//searchParent(ui.item.value);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchParent").val(searchString);
			}
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	
	//------------binding the key up event with the search field , it will search the users when the enter key is pressed
	$("#searchParent").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchParent($("#searchParent").val(),'other','N');
		}
	});
	//------------binding the click event with the search field icon, it will search the users when the enter key is pressed
	$("a[param^='search_icon_parent']").live("click", function(e) {
	   
		if ($("#searchParent").val()!="" && $("#searchParent").val()!="Search") {
			$(".ui-autocomplete").hide();
			searchParent($("#searchParent").val(),"other",'N');
		}/*else if($.template.ie7 || $.template.ie8 ){
			if ( $("#searchParent").val()=="Search" ) {
				$(".ui-autocomplete").hide();
				searchParent($("#searchParent").val(),"ie7");
				}
	   }*/
	});
	
	
	//===================This function searches the parent with the typed user name and populates the user details in the table ==========================
	function searchParent(searchString,browser,isExactSeacrh) {
		blockUI();	
		$.ajax({
			type : "GET",
			url : "searchParent.do",
			data : "parentName="+searchString+"&browser="+browser+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&isExactSeacrh="+isExactSeacrh+"&AdminYear="+$('#AdminYear').val(),
			dataType : "json",
			cache:false,
			success : function(data){
				unblockUI();
				if ( data != null || data != "") {
					if(data.length < moreCount) {
						// hide more button if the parent list is less than 15
						$(".pagination").hide(200);
					} else {
						$(".pagination").show(200);
						$("#moreParent").removeClass("disabled");
						if($.browser.msie) $("#moreParent").removeClass("disabled-ie");
					}
					getParentDetails(true,data); 
					//$(".pagination").hide(200);
				}
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.user.search']);
				
			}
		});
	}

	
	//------ auto completing the search field for auto complete
	$("#searchStudent").autocomplete({
		source:function(request, response) {
            $.ajax({
                url: 'searchStudentAutoComplete.do',
                dataType: "json",
                cache:false,
                data: {
                    term : request.term,
                    selectedOrg : $("a.jstree-clicked").parent().attr("id"),
                    AdminYear : $("#AdminYear").val()
                },
                success: function(data) {
                    response(data);
                }
            });
        },
		minLength:3,
			position:{my:"right top",at:"right bottom"},
			open: function(event, ui) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"186px"});
				$(".ui-autocomplete").addClass("white-gradient");
				if($.template.ie7 || $.template.ie8) {
					$(".ui-autocomplete").css({"max-height":"150px", "width":"201px", "z-index":"1000000", "overflow" : "auto"});
				} else {
					$('.ui-autocomplete').customScroll({
						horizontal : true,
						showOnHover : false,
						width: 10
					});
				}
			},
		select:function(event, ui) {
			searchStudent("searchStudent.do",ui.item.value);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			$("#searchStudent").val(searchString);
			event.preventDefault();
			event.stopPropagation();
		}
	});
	



//------------binding the key up event with the search field , it will search the students when the enter key is pressed
	$("#searchStudent").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchStudent("searchStudent.do",$("#searchStudent").val());
		}
	});
	//------------binding the click event with the search field icon, it will search the students when the search icon is clicked
	$("a[param^='search_icon_student']").live("click", function(e) {
	   
		  if ( $("#searchStudent").val()!="" && $("#searchStudent").val()!="Search" ) {
				$(".ui-autocomplete").hide();
				searchStudent("searchStudent.do",$("#searchStudent").val(),"other");
			}/*else if($.template.ie7 || $.template.ie8 ){
					if ( $("#searchStudent").val()=="Search by Student Name" ) {
						$(".ui-autocomplete").hide();
						searchStudent("searchStudent.do",$("#searchStudent").val(),"ie7");
						}
			   }*/
	});
	
	
	//===================This function searches the students with the typed student name and populates the student details in the table ==========================
	function searchStudent(methodurl,searchString,browser) {
		blockUI();
		$.ajax({
			type : "GET",
			url : methodurl,
			data : "studentName="+searchString+"&browser="+browser+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+ "&AdminYear=" + $("#AdminYear").val(),
			dataType : "json",
			cache:false,
			success : function(data){
				unblockUI();
				if ( data != null || data != "") {
					if(data.length < moreCount) {
						// hide more button if the parent list is less than 15
						$(".pagination").hide(200);
					} else {
						$(".pagination").show(200);
						$("#moreStudent").removeClass("disabled");
						if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
					}
					getStudentDetails(true,data); 
					//$(".pagination").hide(200);
				}
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.user.search']);
				
			}
		});
	}
	
	//======================OPEN RESET PASSWORD ==========================================
	function openResetPasswordModal(parentName,parentDisplayName) {
		$("#passwordModal").modal({
			title: '<b>Reset User Password</b>',
			width: 480,
			resizable: false,
			draggable: false,
			onOpen:showPasswordChangeWindow(parentName,parentDisplayName,"passwordModal","passwordModalContainer"),
			buttons: {
					'Submit': {
						classes: 'blue-gradient glossy',
						click: function(win) {
							//win.closeModal();//$(selector).setModalTitle(title)
							resetPassword(win,parentName,parentDisplayName);
							$(this).hide();
							$(this).siblings(".canelPwdReset").addClass("blue-gradient");
							$(this).siblings(".canelPwdReset").text("OK");
						}
					},
					'Cancel': {
						classes: 'glossy canelPwdReset',
						click: function(win) {
							//$(this).attr("id","canelPwdReset");
							win.closeModal();
						}
					}
			
			}
		});
				
		}	
   //=======================================AJAX CALL TO RESET THE PASSWORD IN THE DATABASE============
	function resetPassword(win,parentName,parentDisplayName)
	{
		var row = $("#"+parentName);
		var nodeid = "userName=" + parentName;	
		blockUI();
		$.ajax({
			type : "GET",
			url : "resetPassword.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				if (data!= null) {
				//=========REBUILD THE MODAL DOM========
					if( data.sendEmailFlag == "1") {
						win.setModalTitle(strings['msg.newParentPassword']);
						buildResetPasswordDom(parentDisplayName,"passwordModal","passwordModalContainer");
						$.modal.alert('New password is sent to user email. \nNew password is: '+data.password );
					} else {
						$.modal.alert('No email is associated with the user. Please notify password to user. \nNew password is: '+data.password );
					}	
				}
				else {
					$.modal.alert(strings['script.parent.passwordResetError']);
				}
				unblockUI();
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.parent.passwordResetError']);
			}
		});
	}
	
	
	
	//==========================CREATES THE STRUCTURE FOR RESET PASSWORD=======================
	function buildResetPasswordDom(parentDisplayName,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var newPassword ='<div id="newPassword">'
							 +'<p class="message blue-gradient" style="width:400px">'+strings['msg.passwordReset']+'</p>'
							 +'<p><b>Full Name:</b>&nbsp;'+parentDisplayName+'</p>'
							 +'<p class="big-message" style="width:400px">'+strings['msg.passwordSent']+'</p>'
							 +'</div>';
				
			$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(newPassword);		
				
	}
	
	//==========================CREATES THE STRUCTURE FOR THE POP UP FOR CONFIRMATION TO THE USER FOR  RESETTING THE  PASSWORD=======================
	function showPasswordChangeWindow (parentName,parentDisplayName,modalId,modalContainerDivId)
	{
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var content ='<div>'
						+'<p  class="message blue-gradient" style="width:350px">'+strings['msg.clickingSubmit']+'</p>'
						+'<p class="big-message" style="width:350px">'+strings['msg.confirmIdentity']+'<br/>'
						+'</p>'
						+'<br/>'
						+'<strong>'+strings['msg.userName']+'</strong>&nbsp;&nbsp;&nbsp;'
						+parentName
						+'<br/>'
						+'<strong>'+strings['msg.fullName']+'</strong>&nbsp;&nbsp;&nbsp;'
						+ parentDisplayName
						+'</div>'
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(content);					
					
	}
	
window.onerror = function(desc, page, line, chr) {
	/*
	 * alert('JavaScript error occurred! \n' +'\nError description: \t'+desc
	 * +'\nPage address: \t'+page +'\nLine number: \t'+line );
	 */
}

$(function() {
	$('a').focus(function() {
		this.blur();
	});
	SI.Files.stylizeAll();
	slider.init();

	$('input.text-default').each(function() {
		$(this).attr('default', $(this).val());
	}).focus(function() {
		if ($(this).val() == $(this).attr('default'))
			$(this).val('');
	}).blur(function() {
		if ($(this).val() == '')
			$(this).val($(this).attr('default'));
	});

	$('input.text,textarea.text').focus(function() {
		$(this).addClass('textfocus');
	}).blur(function() {
		$(this).removeClass('textfocus');
	});

	var popopenobj = 0, popopenaobj = null;
	$('a.popup').click(
			function() {
				var pid = $(this).attr('rel').split('|')[0], _os = parseInt($(
						this).attr('rel').split('|')[1]);
				var pobj = $('#' + pid);
				if (!pobj.length)
					return false;
				if (typeof popopenobj == 'object'
						&& popopenobj.attr('id') != pid) {
					popopenobj.hide(50);
					$(popopenaobj).parent().removeClass(
							popopenobj.attr('id').split('-')[1] + '-open');
					popopenobj = null;
				}
				return false;
			});
	$('p.images img').click(
			function() {
				var newbg = $(this).attr('src').split('bg/bg')[1]
						.split('-thumb')[0];
				$(document.body).css('backgroundImage',
						'url(' + _siteRoot + 'images/bg/bg' + newbg + '.jpg)');

				$(this).parent().find('img').removeClass('on');
				$(this).addClass('on');
				return false;
			});
	$(window).load(
			function() {
				try {
					$.each(css_ims, function() {
						(new Image()).src = _siteRoot + 'css/images/' + this;
					});
				} catch (err) {
				}
				try {
					$.each(css_cims, function() {
						var css_im = this;
						$.each([ 'blue', 'purple', 'pink', 'red', 'grey',
								'green', 'yellow', 'orange' ], function() {
							(new Image()).src = _siteRoot + 'css/' + this + '/'
									+ css_im;
						});
					});
				} catch (err) {
				}
			});
	$('div.sc-large div.img:has(div.tml)').each(
			function() {
				$('div.tml', this).hide();
				$(this).append('<a href="#" class="tml_open">&nbsp;</a>').find(
						'a').css({
					left : parseInt($(this).offset().left) + 864,
					top : parseInt($(this).offset().top) + 1
				}).click(function() {
					$(this).siblings('div.tml').slideToggle();
					return false;
				}).focus(function() {
					this.blur();
				});
			});
});
var slider = {
	num : -1,
	cur : 0,
	cr : [],
	al : null,
	at : 10 * 2000,
	ar : true,
	init : function() {
		if (!slider.data || !slider.data.length)
			return false;

		var d = slider.data;
		slider.num = d.length;
		var pos = Math.floor(Math.random() * 1);// slider.num);
		for ( var i = 0; i < slider.num; i++) {
			$('#' + d[i].id).css({
				left : ((i - pos) * 1000)
			});
			$('#slide-nav').append(
					'<a id="slide-link-' + i
							+ '" href="#" onclick="slider.slide(' + i
							+ ');return false;" onfocus="this.blur();">'
							+ (i + 1) + '</a>');
		}

		$('img,div#slide-controls', $('div#slide-holder')).fadeIn();
		slider.text(d[pos]);
		slider.on(pos);
		slider.cur = pos;
		window.setTimeout('slider.auto();', slider.at);
	},
	auto : function() {
		if (!slider.ar)
			return false;

		var next = slider.cur + 1;
		if (next >= slider.num)
			next = 0;
		slider.slide(next);
	},
	slide : function(pos) {
		if (pos < 0 || pos >= slider.num || pos == slider.cur)
			return;

		window.clearTimeout(slider.al);
		slider.al = window.setTimeout('slider.auto();', slider.at);

		var d = slider.data;
		for ( var i = 0; i < slider.num; i++)
			$('#' + d[i].id).stop().animate({
				left : ((i - pos) * 1000)
			}, 1000, 'swing');

		slider.on(pos);
		slider.text(d[pos]);
		slider.cur = pos;
	},
	on : function(pos) {
		$('#slide-nav a').removeClass('on');
		$('#slide-nav a#slide-link-' + pos).addClass('on');
	},
	text : function(di) {
		slider.cr['a'] = di.client;
		slider.cr['b'] = di.desc;
		slider.ticker('#slide-client span', di.client, 0, 'a');
		slider.ticker('#slide-desc', di.desc, 0, 'b');
	},
	ticker : function(el, text, pos, unique) {
		if (slider.cr[unique] != text)
			return false;

		ctext = text.substring(0, pos) + (pos % 2 ? '-' : '_');
		$(el).html(ctext);

		if (pos == text.length)
			$(el).html(text);
		else
			window.setTimeout('slider.ticker("' + el + '","' + text + '",'
					+ (pos + 1) + ',"' + unique + '");', 30);
	}
};
// STYLING FILE INPUTS 1.0 | Shaun Inman <http://www.shauninman.com/> |
// 2007-09-07
if (!window.SI) {
	var SI = {};
};
SI.Files = {
	htmlClass : 'SI-FILES-STYLIZED',
	fileClass : 'file',
	wrapClass : 'cabinet',

	fini : false,
	able : false,
	init : function() {
		this.fini = true;
	},
	stylize : function(elem) {
		if (!this.fini) {
			this.init();
		}
		;
		if (!this.able) {
			return;
		}
		;

		elem.parentNode.file = elem;
		elem.parentNode.onmousemove = function(e) {
			if (typeof e == 'undefined')
				e = window.event;
			if (typeof e.pageY == 'undefined' && typeof e.clientX == 'number'
					&& document.documentElement) {
				e.pageX = e.clientX + document.documentElement.scrollLeft;
				e.pageY = e.clientY + document.documentElement.scrollTop;
			}
			;
			var ox = oy = 0;
			var elem = this;
			if (elem.offsetParent) {
				ox = elem.offsetLeft;
				oy = elem.offsetTop;
				while (elem = elem.offsetParent) {
					ox += elem.offsetLeft;
					oy += elem.offsetTop;
				}
				;
			}
			;
		};
	},
	stylizeAll : function() {
		if (!this.fini) {
			this.init();
		}
		;
		if (!this.able) {
			return;
		}
		;
	}
};
	/**
	 * This js file is to Find Students
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
$(document).ready(function() {
		if($("#studentTable").length > 0) {
			$('#report-list').tablesorter({
				headers: {
					4: { sorter: false }
				},
				sortList: [[0,1]]
			});
			$("ul#shortcuts li").removeClass("current");
			$(".shortcut-agenda").parent().addClass("current");
			$('.clearfix').addClass('menu-hidden');
			$('.view-Assessment').live("click", function() {
				regenerateAC = false;
				openModalToViewAssessments($(this).attr("id"), $(this).attr("testelementid"));		
			});	
			//$('input#lastStudentId').val("");//clearing the value on load.
			
			//========================SCROLL FOR THE STUDENT TABLE===============================	
			var tempScrollTop, currentScrollTop = 0
			//$("#studentTable").scroll(function() {
			$("#moreStudent").click(function(e) {
				e.stopPropagation();
				currentScrollTop = $("#studentTable").scrollTop();
				//if (tempScrollTop < currentScrollTop) {
				if(!$(this).hasClass("disabled")) {
					blockUI();
					var lastid ="scrollId=" + $('input#lastStudentId').val() + "&AdminYear=" + $("#AdminYear").val()+"&searchParam="+$("#searchStudent").val(); 
					//showLoader(currentScrollTop);//This method is defined in usermodule.js
					$.ajax({
						type : "GET",
						url : "getStudentDetailsOnScroll.do",
						data : lastid,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data != null && data.length > 0){
								
								getStudentDetails(false,data);//This method is defined in manageParent.js
								retainUniqueValue();  //This method is defined in usermodule.js
								//hideLoader();//This method is defined in usermodule.js
								unblockUI();
								$("#studentTable").animate({scrollTop: currentScrollTop+600}, 500);
														
							} else {
								$("#moreStudent").addClass("disabled");
								if($.browser.msie) $("#moreStudent").addClass("disabled-ie");
								//$("#moreStudent").attr("disabled");
								unblockUI();
							}
							if (data != null && data.length < moreCount) {
								// check if this is the last set of result
								$("#moreStudent").addClass("disabled");
								if($.browser.msie) $("#moreStudent").addClass("disabled-ie");
							}
						},
						error : function(data) {
							$.modal.alert(strings['script.common.error1']);
							//hideLoader();//This method is defined in usermodule.js
							unblockUI();
							}
					});
		
				}
				tempScrollTop = currentScrollTop;
			});
			//========================SCROLL FOR THE STUDENT TABLE ENDS===============================
					
		}
	});
		
		
var regenerateAC=false;


function openModalToViewAssessments(studentBioId, testElementId) {
	
	
	//Fix for Amit Da's mail: RE: Latest code is deployed into DEV and QA (in manage student view assessment tab is not opening)
	//var nodeid = "studentBioId=" + studentBioId;	
	var nodeid = "testElementId=" + testElementId;
	
	blockUI();
	$.ajax({
			type : "GET",
			url : "getStudentAssessmentList.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				if(data.status != 'Blank') {
				//=========OPEN THE MODAL========
				$("#studentModal").modal({
					title: strings['msg.studentAssessmentDetails'],
					//height: 320,
					//width: 350,
					resize:true,
					draggable: false,
					onOpen:buildAssessmentTableDom(data,"studentModal","studentModalContainer"),
					buttons: {
							'Close': {
								classes: 'glossy',						
								click: function(win) {
									win.closeModal();
								}
							},
							'Re-set Invitation Code': {
								classes: 'orange-gradient glossy',						
								click: function(win) {
									
									//RND for TD 78246 - By Joy
									win.closeModal();
									confirmRecreationAC(1);
								}
							},
							'Create Letter':{
								classes: 'blue-gradient glossy createLetter',
								click: function(win) {
									var url = 'icDownload.do'+'?type=pdf'+'&token=0&reportUrl=/public/PN/Report/Invitation_pdf_files&drillDown=true&assessmentId=105_InvLetter&p_Student_Bio_Id='+testElementId;
									url = url + '&p_L3_Jasper_Org_Id=-1&p_AdminYear=-1'; // TODO : Remove hardcode 101
									window.open(url);
								}
							}
					}
				});
				} else {
					$.modal.alert(strings['script.student.blankAssessment']);
				}
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});
			
	}

	


//==========================CREATES THE TABULAR STRUCTURE FOR ASSESSMENT VIEW=======================
function buildAssessmentTableDom(jsonData,modalId,modalContainerDivId)
{	
	//RND for TD 78246 - By Joy
	var rowCounter=1;
	//$("#"+modalId +" > "+"#"+modalContainerDivId + ">" +"p.message").remove();
	//$("#"+modalId +" > "+"#"+modalContainerDivId ).find("table").remove();
	var makeViewAssessmentTableDom = '<table id="assessmentTable" class="table " style="width:940px">'
									+'<thead class ="table-header glossy ">'
									+'<tr >'
									+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['thead.assessment.availableAssessments']+'</span></th>'
									+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['thead.assessment.invitationCode']+'</span></th>'
									+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['thead.assessment.availableICClaims']+'</span></th>'
									+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['thead.groupDownloadFiles.expDate']+'</span></th>'
									+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['thead.action']+'</span></th>'
									+'</tr>'
									+'</thead>'
									+'<tbody>';
	$.each(jsonData, function () { 
	
		makeViewAssessmentTableDom += '<tr>'
												+'<td name="administration'+rowCounter+'" id="administration'+rowCounter+'" >'+this.administration +'</td>'
												+'<td name="invitationcode'+rowCounter+'" id="invitationcode'+rowCounter+'">'+this.invitationcode +'</td>'
												//+'<td name="icExpirationStatus'+rowCounter+'"  id="icExpirationStatus'+rowCounter+'">'+this.icExpirationStatus +'</td>'
												+'<td><span class="number input margin-right">'
												+'<button type="button" class="button number-down">-</button>'
												+'<input  type="text" size="8" name="totalAvailableClaim'+rowCounter+'" id="totalAvailableClaim'+rowCounter+'"  class="input-unstyled" value='+this.totalAvailableClaim +' data-number-options="{&quot;min&quot;:0,&quot;increment&quot;:1,&quot;shiftIncrement&quot;:1,&quot;precision&quot;:1}" >'
												+'<button type="button" class="button number-up">+</button>'
												+'</span></td>'
												+'<td><span class="input"><span class="icon-calendar"></span>'
												+'<input  type="text" name="expirationDate'+rowCounter+'"  id="expirationDate'+rowCounter+'" style="width:200px" class="input-unstyled datepicker" value="'+ new Date(this.expirationDate).toLocaleDateString()+'">'
												+'<input type="hidden" name="studentBioIdAss'+rowCounter+'"  id="studentBioIdAss'+rowCounter+'" value="'+this.studentBioId+'" />'
												
												//Fix for TD 78188 - By Joy
												+'<input type="hidden" name="testElementId'+rowCounter+'"  id="testElementId'+rowCounter+'" value="'+this.testElementId+'" />'
												
												+'</span></td>'
												+' <td><button class="button blue-gradient glossy mid-margin-left btnSaveViewAssessment" type="button" rowCounter="'+rowCounter+'">Save</button></td>'
												+'</tr>';
		
		     rowCounter	++;			
				
						});
	
	makeViewAssessmentTableDom += '</tbody></table>';
	//$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(makeViewAssessmentTableDom);	
	$("#"+modalId+ " > "+"#"+modalContainerDivId ).html(makeViewAssessmentTableDom);
		
	if(regenerateAC) {
		$("#invitationcode"+globalcounter).addClass("orange-bg");
		$("#invitationcode"+globalcounter).css('box-shadow', '0 0 30px orange');
		$(".createLetter").css('box-shadow', '0 0 15px blue');
		notify('Invitation Code Refreshed', 'The old invitation code will no longer be linked to that student\'s results. Parents and family will no longer be able to view results, although the account is still active. <br/><br/>Please refresh \'Manage Student\' screen to view updated \'Parent User ID\' column.', {
			autoClose: true,
			closeDelay:10000,
			delay: 100,
			showCloseOnHover:false,
			textOneSimilar:'Invitation Code Regenerated!',
			textSeveralSimilars:'Invitation Code Regenerated!',
			icon: 'themes/acsi/img/demo/icon.png',
			onDisplay: function() {
				$(".createLetter").css('box-shadow', '0 0 15px blue');
			}
		});
	}	

}

//======================================= REGERATE Activation Code =========================
var globalcounter = 0;
function confirmRecreationAC(rowcounter)
{
	globalcounter = rowcounter;
	$.modal.confirm(strings['msg.confirm.resetIc'], function()
	{
		blockUI();
		var studentBioId = $("#studentBioIdAss"+globalcounter).val();
		var adminYear = $("#administration"+globalcounter).text();
		var invitationCode = $("#invitationcode"+globalcounter).text();
		
		//Fix for TD 78188 - By Joy
		var testElementId = $('#testElementId'+globalcounter).val();
		var data = "studentBioId="+studentBioId+"&adminYear="+adminYear+"&invitationCode="+invitationCode+"&testElementId="+testElementId;
		
		$.ajax({
			type : "GET",
			url : "regenerateActivationCode.do",
			data : data,
			dataType : 'json',
			cache:false,
			success : function(data) {
				
				//RND for TD 78246 - By Joy
				if(data.status == 'Success'){
					//$("#studentModal").closeModal();
					regenerateAC=true;
					
					//Fix for TD 78188 - By Joy
					openModalToViewAssessments(studentBioId,testElementId);
				}else{
					$.modal.alert(strings['script.common.error1']);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});

	}, function()
	{
		//RND for TD 78246 - By Joy
		var studentBioId = $("#studentBioIdAss"+globalcounter).val();
		var testElementId = $('#testElementId'+globalcounter).val();
		regenerateAC = false;
		openModalToViewAssessments(studentBioId,testElementId);
		
	});
};

//=======================================ENABLING THE SAVE ACTION IN THE VIEW ASSESSMENT RFOW WISE=========================
	$(".btnSaveViewAssessment").live("click",function(event){
		event.stopImmediatePropagation();
		var rowCounter=$(this).attr("rowCounter");
		var date = new Date ($("#expirationDate"+rowCounter).val());
		var today = new Date();
		if(validateDate(date) != "NotValid")
		{
			 var mmddyyyy= validateDate(date);
			 saveAssessmentDetails($("#studentBioIdAss"+rowCounter).val(),$("#administration"+rowCounter).text(),$("#invitationcode"+rowCounter).text(),$("#icExpirationStatus"+rowCounter).text(),$("#totalAvailableClaim"+rowCounter).val(),mmddyyyy);
		}
		else
		{
			$.modal.alert(strings['script.student.expirationDate']);
		}
		
	});

//======================================================DATE VALIDATION FOR ASSESSMENT===================================
	function validateDate(dateObj)
	{	
		var today = new Date();
		var mmddyyyy= "";
		if(dateObj >= today)
		{
			 if( ((dateObj.getMonth()+1)%10 ) == (dateObj.getMonth()+1))
			 {
				 mmddyyyy += '0'+(dateObj.getMonth()+1)+'/';
			 }
			 else 
			 {
				 mmddyyyy += (dateObj.getMonth()+1)+'/';
			 }
			
			if( (dateObj.getDate() %10 )== dateObj.getDate())
				{
					mmddyyyy += '0'+ dateObj.getDate()+'/';
				}
			else
				{
					mmddyyyy += dateObj.getDate()+'/';
				}
				mmddyyyy += dateObj.getFullYear();
			return 	mmddyyyy;	
		}
		
		else
		{
			return "NotValid";
		}
	}
//============================================ ENABLING DATE PICKER ================================================
		
		$('.datepicker').live("focus",function ()	{
			showSelectedDate('.datepicker',$(this).attr("id"),$(this).val());
			});
			
			
			
			
			function showSelectedDate(classSelector,id,dateObjString)
			{
					$("#"+id).glDatePicker({ 
					zIndex: 100,
					startDate:new Date(dateObjString),
					selectedDate:dateObjString,
					onChange:function()
								{	
														
									$("#"+id).val(new Date($("#"+id).val()).toLocaleDateString());
									
								}				
					});	
			}
			
			
			/*	$('.gldp-days').live("click",function ()	{
			datePick(".datepicker",$('.datepicker').val());
				});
		
			function datePick(classSelector,dateObjString)
			{
				alert("hii");
				$(classSelector).glDatePicker({ 
					startDate:new Date(),
					selectedDate:dateObjString,
					allowOld:false
					});
			}*/
			
//======================================================UPDATES THE ASSESSMENT DETAILS IN THE DATABASE==========================
			
			function saveAssessmentDetails(studentBioId,administration,invitationcode,icExpirationStatus,totalAvailableClaim,expirationDate)
			{
				blockUI();		
					$.ajax({
							type : "GET",
							url : 'updateAssessmentDetails.do',
							data : "&studentBioId="+studentBioId+"&administration="+administration+"&invitationcode="+invitationcode+"&icExpirationStatus="+icExpirationStatus+"&totalAvailableClaim="+totalAvailableClaim+"&expirationDate="+expirationDate,
							dataType: 'html',
							cache:false,
							success : function(data) {
								var obj = jQuery.parseJSON(data);
								if(obj.status == 'Success') {
									$.modal.alert(strings['script.student.updateSuccess']);				
									
								} else {
									$.modal.alert(strings['script.user.saveError']);
								}
								unblockUI();
							},
							error : function(data) {
								unblockUI();
								$.modal.alert(strings['script.user.saveError']);
							}
						});

			}
			
			//====================================AJAX CALL TO FETCH ALL THE STUDENTS FOR THE SELECTED ORG====================
			function fetchAllStudents(id,studentBioId,isRedirectedTree,url)
			{
				blockUI();
				var nodeid = "scrollId="+id+"&studentBioId="+studentBioId+"&isRedirectedTree="+isRedirectedTree+ "&AdminYear=" + $("#AdminYear").val();
				$("tbody#student_details").find("tr").remove();
				$("#report-list").trigger("update");
				$("tbody#student_details").addClass("loader big");
				$.ajax({
					type : "GET",
					url : url,
					data : nodeid,
					dataType : 'json',
					cache:false,
					success : function(data) {
						if(data.length >= 1){
							showHideDataTable('show');
						}
						if (data != null && data.length >= moreCount){
							$(".pagination").show(200);
							$("#moreStudent").removeClass("disabled");
							if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
						} else {
							$(".pagination").hide(200);
						}
						
						if (data != null && data.length == 0) {
							$("span#showOrgNameStudent").text(' ');
						} else {
							$("span#showOrgNameStudent").text('Students of '+$("a.jstree-clicked").text());
						}
						getStudentDetails(true, data);
						enableSorting(true);
						$("#isRedirectedTree").val("No");
						$("tbody#student_details").removeClass("loader big");				
						if (data != null && data.length >= moreCount){
							$("#moreParent").removeClass("disabled");
							if($.browser.msie) $("#moreParent").removeClass("disabled-ie");
						} else {
							$("#moreParent").addClass("disabled");
							if($.browser.msie) $("#moreParent").addClass("disabled-ie");
						}
						unblockUI();
					},
					error : function(data) {
						unblockUI();
						$("tbody#student_details").removeClass();	
						$.modal.alert(strings['script.org.error']);
					}
				});
			}

/**
 * This js file is to manage content module - Start
 * Author: Joy
 * Version: 1
 */
var ANIMATION_TIME = 200;

$(document).ready(function() {
	
	if($("#contentTable").length > 0) {
		var allContents = $(".content-list-all");
		if(allContents != null && allContents.length > 0) {
			$('#report-list').tablesorter({
				headers: {
					3: { sorter: false }
				},
				sortList: [[0,1]]
			});
		}
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-content").parent().addClass("current");
	}
	
	
	$('#custProdIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#gradeIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#subtestIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateGrade();
		resetPrismActions();
	}); 
	
	$('#gradeIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#subtestIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateSubtest();
	});
	
	$('#subtestIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateObjective();
	});
	
	$('#objectiveIdManageContent').live('change',function(){
		hideContentElements();
		showContentElements();
	});
	
	$('#contentTypeIdManageContent').live('change',function(){
		hideContentElements();
		showContentElements();
	});
	
	$('#addContent').live("click", function() {
		if ($('#objectiveIdManageContent').val() == null || $('#objectiveIdManageContent').val() == "" || $('#objectiveIdManageContent').val() == "-1" ) {
			$.modal.alert(strings['script.content.addContent']);
		}else {
			resetModalForm("addNewContent");
			resetModalForm("editContent");
			//resetModalForm("modifyStandardForm");
			resetModalForm("modifyGenericForm");
			openContentModalToAdd();
		}
	});
	
	$('#modifyStandardButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('STD');
	});
	
	$('#modifyRscButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('RSC');
	});
	
	$('#modifyEdaButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('EDA');
	});
	
	$('#modifyAttButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('ATT');
	});
	
	$('#modifyRbsButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('RBS');
	});
	
	$('#modifyOarButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('OAR');
	});
	
	$('#modifySplButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('SPL');
	});
	
	$('#modifyOplButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('OPL');
	});
	
	$('#refresh-content').live('click',function(){
		loadManageContentList();
	});
	
	var tempScrollTop, currentScrollTop = 0
	$("#moreContents").click(function() {
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		var lastid = $("#contentTable tr:last").attr("value");
		var checkFirstLoad = false;
		var paramUrl = 'custProdId='+custProdId+'&subtestId='+subtestId
						+'&objectiveId='+objectiveId+'&contentTypeId='+contentTypeId
						+'&lastid='+lastid+'&checkFirstLoad='+checkFirstLoad;
		
		currentScrollTop = $("#contentTable").scrollTop();
		if(!$(this).hasClass('disabled')) {
			var callingAction = "";
			var lastid = $('#lastid').val();
				callingAction = 'loadManageContent.do';
			blockUI();
			$.ajax({
				type : "GET",
				url : callingAction,
				data : paramUrl,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if (data != null && data.length > 0){
						getContentDetails(false,data);
						enableSorting(true);
						retainUniqueValue();
						unblockUI();
						$("#contentTable").animate({scrollTop: currentScrollTop+600}, 500);
					} else {
						$("#moreContents").addClass("disabled");
						if($.browser.msie) $("#moreContents").addClass("disabled-ie");
						retainUniqueValue();
						unblockUI();
					}
					if (data != null && data.length < moreCount) {
						// check if this is the last set of result
						$("#moreContents").addClass("disabled");
						if($.browser.msie) $("#moreContents").addClass("disabled-ie");
					}
				},
				error : function(data) {
					unblockUI();
				}
			});

		} else {
			return false;
		}
		tempScrollTop = currentScrollTop;
	});
	
	$('.edit-content').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		openContentModalToEdit($(this).attr("contentId"));
	});	
	
	$('.delete-content').live("click", function() {
	    var row = $(this);
		var contentId = $(this).attr("contentId");
		$.modal.confirm(strings['msg.confirm.deleteContent'],
			function () {
				deleteContentDetails(contentId, row);
				enableSorting(true);
			},function() {//this function closes the confirm modal on clicking cancel button
			} 
		);
	});
	
	$('.mandatoryDescription').live('click',function(){
		$('.mandatoryDescription').hide(ANIMATION_TIME);
	});
	
});
//=====document.ready End=========================================

//============Open Modal to Edit Description of Resource, Everyday Activity and About the Test ===============
function openModifyGenericModalToEdit(type) {
	blockUI();
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = 0;
	var objectiveId = 0;
	var performanceLevelId = 0;
	var statusCodeId = 0;
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	
	if(type == 'RSC' || type == 'STD' || type == 'RBS' || type == 'OAR' || type == 'SPL' || type == 'OPL' ){
		subtestId = $('#subtestIdManageContent').val();
		performanceLevelId = $('#performanceLevelIdManageContent').val();
		statusCodeId = $('#statusCodeIdManageContent').val();
	}
	if(type == 'STD' || type == 'OPL' ){
		objectiveId = $('#objectiveIdManageContent').val();
	}
	if(type == 'OPL' ){
		performanceLevelId = $('#performanceLevelIdManageContentOpl').val();
	}
	
	var dataUrl = 'custProdId='+custProdId+'&gradeId='+gradeId
					+'&subtestId='+subtestId+'&objectiveId='+objectiveId
					+'&type='+type+'&performanceLevelId='+performanceLevelId
					+ '&statusCodeId='+statusCodeId;
	
	$.ajax({
			type : "GET",
			url : "modifyGenericForEdit.do",
			data : dataUrl,
			dataType : 'json',
			cache:false,
			success : function(data) {
				var custProdName = $('#custProdIdManageContent :selected').text();
				var gradeName = $('#gradeIdManageContent :selected').text();
				var subtestName = $('#subtestIdManageContent :selected').text();
				var performanceLevelName = $('#performanceLevelIdManageContent :selected').text();
				var statusCodeName = $('#statusCodeIdManageContent :selected').text();
				
				//As we need to show Objective description instead of objective name - By Joy 
				//var objectiveName = $('#objectiveIdManageContent :selected').text();
				var objectiveName = "";
				if(data != null && data.objectiveDesc != ""){
					objectiveName = data.objectiveDesc;
				}
				
				var $modifyGenericModal = $('#modifyGenericModal');
				$modifyGenericModal.find('#testAdministrationText').text(custProdName);
				$modifyGenericModal.find('#gradeText').text(gradeName);
				
				$('#p_subtest').hide();
				$('#p_objective').hide();
				$('#p_performanceLevel').hide();
				$('#p_statusCode').hide();
				
				if(type == 'RSC'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
				}else if(type == 'STD'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
					$('#p_objective').show();
					$modifyGenericModal.find('#objectiveText').text(objectiveName);
				}else if(type == 'OAR' || type == 'RBS'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
					$('#p_performanceLevel').show();
					$modifyGenericModal.find('#performanceLevelText').text(performanceLevelName);
					$('#p_statusCode').show();
					$modifyGenericModal.find('#statusCodeText').text(statusCodeName);
				}else if(type == 'SPL'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
					$('#p_performanceLevel').show();
					$modifyGenericModal.find('#performanceLevelText').text(performanceLevelName);
				}else if(type == 'OPL'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
					objectiveName = $('#objectiveIdManageContent :selected').text();
					$('#p_objective').show();
					$modifyGenericModal.find('#objectiveText').text(objectiveName);
					performanceLevelName = $('#performanceLevelIdManageContentOpl :selected').text();
					$('#p_performanceLevel').show();
					$modifyGenericModal.find('#performanceLevelText').text(performanceLevelName);
				}
				
				if(data != null && data.contentDescription != ""){
					$modifyGenericModal.find('#genericDescriptionEditor').val(data.contentDescription);
				}
				
				var modalTitle = '';
				if(type == 'STD'){
					modalTitle+='Modify Standard Description';
				}else if(type == 'RSC'){
					modalTitle+='Modify Resource Description';
				}else if(type == 'EDA'){
					modalTitle+='Modify Everyday Activity Description';
				}else if(type == 'ATT'){
					modalTitle+='Modify About the Test Description';
				}else if(type == 'RBS'){
					modalTitle+='Modify Result by Standard Description';
				}else if(type == 'OAR'){
					modalTitle+='Modify Overall Result Description';
				}else if(type == 'SPL'){
					modalTitle+='Modify Subtest Performance Level Description';
				}else if(type == 'OPL'){
					modalTitle+='Modify Objective Performance Level Description';
				}
				
				$("#modifyGenericModal").modal({
					title: modalTitle,
					height: 500,
					width: 780,
					resizable: false,
					draggable: false,
					onOpen: setCKEditor('modifyGeneric'),
					actions: {
						'Close' : {
							color: 'red',
							click: function(win) { 
								//Fix for TD 77905 - By Joy
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								win.closeModal(); 
							}
						},
						'Maximize' : {
							color: 'blue',
							click: function(win) { 
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								$.modal.current.setModalContentSize($(window).width(), $(window).height()).centerModal();
							}
						}/*,
						'Restore' : {
							color: 'green',
							click: function(win) { 
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								$.modal.current.setModalContentSize(780,500).centerModal();
							}
						}*/
					},
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);
										$('.mandatoryDescription').hide();
										if($.browser.msie) setTimeout("hideMessage()", 300);
										win.closeModal(); 
									}
								},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);	
										modifyGeneric($("#modifyGenericForm"), win);
									}
								}
							}
					});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		}); 
}

//============Insert/Update Description of Resource/Everyday Activity/About the Test ===============
function modifyGeneric(form, win) {
	blockUI();
	var descriptionFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		editorVal = editorVal.trim();
		if(editorVal == "" || editorVal.indexOf('Click here to add text') > 0){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			descriptionFlag = false;
			break;
		}
	    $('#modifyGenericModal #contentDescription').val(editorVal);
	}
	
	if(descriptionFlag == true){
		var performanceLevelName = "";
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		var performanceLevelId = (contentTypeId == 'OAR' || contentTypeId == 'RBS' || contentTypeId == 'SPL') ?  $('#performanceLevelIdManageContent').val() : "";
		if(contentTypeId == 'OPL') {
			performanceLevelId = $('#performanceLevelIdManageContentOpl').val();
			performanceLevelName = $('#performanceLevelIdManageContentOpl').text();
		}
		var statusCodeId = (contentTypeId == 'OAR' || contentTypeId == 'RBS') ?  $('#statusCodeIdManageContent').val() : "";
		
		var $modifyGenericModal = $('#modifyGenericModal');
		$modifyGenericModal.find('#custProdId').val(custProdId);
		$modifyGenericModal.find('#gradeId').val(gradeId);
		$modifyGenericModal.find('#subtestId').val(subtestId);
		$modifyGenericModal.find('#objectiveId').val(objectiveId);
		$modifyGenericModal.find('#contentType').val(contentTypeId);
		$modifyGenericModal.find('#contentTypeName').val(contentTypeName);
		$modifyGenericModal.find('#performanceLevel').val(performanceLevelId);
		if(contentTypeId == 'OPL') {
			$modifyGenericModal.find('#objectiveDesc').val(performanceLevelName);
		}
		$modifyGenericModal.find('#statusCode').val(statusCodeId);
		
		var formObj = $('#modifyGenericForm').serialize();
		$.ajax({
			type : "POST",
			url : 'addNewContent.htm',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					unblockUI();
					$('.mandatoryDescription').hide();
					$.modal.alert(strings['script.content.addSuccess']);
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		 unblockUI();
	}
}

function buildPerformanceLevelDOM($container,performanceLevel){
	
	var option = "";
	if(performanceLevel == 'P' || performanceLevel == ''){
		option += "<option selected value='P'>Pass+</option>";
		option += "<option value='A'>Pass</option>";
		option += "<option value='B'>Did Not Pass</option>";
		option += "<option value='U'>Undefined</option>";	
		option += "<option value='N'>DNR</option>";		
	}else if(performanceLevel == 'B'){
		option += "<option value='P'>Pass+</option>";
		option += "<option value='A'>Pass</option>";
		option += "<option selected value='B'>Did Not Pass</option>";
		option += "<option value='U'>Undefined</option>";	
		option += "<option value='N'>DNR</option>";		
	}else if(performanceLevel == 'U'){
		option += "<option value='P'>Pass+</option>";
		option += "<option value='A'>Pass</option>";
		option += "<option value='B'>Did Not Pass</option>";
		option += "<option selected value='U'>Undefined</option>";	
		option += "<option value='N'>DNR</option>";		
	}else if(performanceLevel == 'N'){
		option += "<option value='P'>Pass+</option>";
		option += "<option value='A'>Pass</option>";
		option += "<option value='B'>Did Not Pass</option>";
		option += "<option value='U'>Undefined</option>";	
		option += "<option selected value='N'>DNR</option>";		
	}else if(performanceLevel == 'A'){
		option += "<option value='P'>Pass+</option>";
		option += "<option selected value='A'>Pass</option>";
		option += "<option value='B'>Did Not Pass</option>";
		option += "<option value='U'>Undefined</option>";	
		option += "<option value='N'>DNR</option>";		
	}
	$container.find('#performanceLevel').html(option);
	$container.find('#performanceLevel').change();
	$container.find('#performanceLevel').trigger('update-select-list');						
	
}


function buildStatusCodeDOM($container,statusCode){
	
	var option = "";
	if(statusCode == 'Blank' || statusCode == ''){
		option += "<option selected value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option value='9'>"+strings['option.statusCode.9']+"</option>";
	}else if(performanceLevel == '3'){
		option += "<option value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option selected value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option value='9'>"+strings['option.statusCode.9']+"</option>";
	}else if(performanceLevel == '7'){
		option += "<option value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option selected value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option value='9'>"+strings['option.statusCode.9']+"</option>";
	}else if(performanceLevel == '5'){
		option += "<option value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option selected value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option value='9'>"+strings['option.statusCode.9']+"</option>";
	}else if(performanceLevel == '8'){
		option += "<option value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option selected value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option value='9'>"+strings['option.statusCode.9']+"</option>";
	}else if(performanceLevel == '9'){
		option += "<option value='Blank'>"+strings['option.statusCode.0']+"</option>";
		option += "<option value='3'>"+strings['option.statusCode.3']+"</option>";
		option += "<option value='5'>"+strings['option.statusCode.5']+"</option>";		
		option += "<option value='7'>"+strings['option.statusCode.7']+"</option>";	
		option += "<option value='8'>"+strings['option.statusCode.8']+"</option>";
		option += "<option selected value='9'>"+strings['option.statusCode.9']+"</option>";
	}
	$container.find('#statusCode').html(option);
	$container.find('#statusCode').change();
	$container.find('#statusCode').trigger('update-select-list');						
	
}


//============Open Modal to Edit Content ===============
function openContentModalToEdit(contentId) {
	blockUI();
	$("#editContent").validationEngine({promptPosition : "centerRight", scroll: false});
	manageIconIE('icon-star');
	var dataUrl = 'contentId='+contentId;
	$.ajax({
			type : "GET",
			url : "getContentForEdit.do",
			data : dataUrl,
			dataType : 'json',
			cache:false,
			success : function(data) {
				var $editContentModal = $('#editContentModal');
				$editContentModal.find('#contentId').val(data.contentId);
				$editContentModal.find('#contentName').val(data.contentName);
				$editContentModal.find('#subHeader').val(data.subHeader);
				$editContentModal.find('#contentDescriptionEditorEdit').val(data.contentDescription);
				
				$("#editContentModal").modal({
					title: 'Edit Content',
					height: 430,
					width: 780,
					resizable: false,
					draggable: false,
					onOpen: setCKEditor('edit'),
					actions: {
						'Close' : {
							color: 'red',
							click: function(win) { 
								//Fix for TD 77905 - By Joy
								$('#editContent').validationEngine('hide');
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								win.closeModal(); 
							}
						},
						'Maximize' : {
							color: 'blue',
							click: function(win) { 
								$('#editContent').validationEngine('hide');
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								$.modal.current.setModalContentSize($(window).width(), $(window).height()).centerModal();
							}
						}/*,
						'Restore' : {
							color: 'green',
							click: function(win) { 
								$('#editContent').validationEngine('hide');
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								$.modal.current.setModalContentSize(780,430).centerModal();
							}
						}*/
					},
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								$('#editContent').validationEngine('hide');
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								clickMe(e);
								win.closeModal(); 
							}
						},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
								if($("#editContent").validationEngine('validate')){
									$('#editContent').validationEngine('hide');
									updateContent($("#editContent"), win);
								 }
							}
						}
					}
				});					
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.common.error1']);
			}
		});	
	unblockUI();
}

//============Update Content ===============
function updateContent(form, win) {
	blockUI();
	var updateContentFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			updateContentFlag = false;
			break;
		}
	    $('#editContentModal #contentDescription').val(editorVal);
	}
	
	if(updateContentFlag == true){
		var formObj = $('#editContent').serialize();
		$.ajax({
			type : "POST",
			url : 'updateContent.htm',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					loadManageContentList();
					$.modal.alert(strings['script.content.editSuccess']);
					$('.mandatoryDescription').hide();
					unblockUI();
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		unblockUI();
	}
	
}


//=======================Delete Content Details====================
function deleteContentDetails(contentId, row) {
	blockUI();
	$.ajax({
		type : "GET",
		url : 'deleteContent.do',
		data : 'contentId='+contentId,
		dataType: 'json',
		cache:false,
		success : function(data) {
			if(data.value >= 1) {
				unblockUI();
				$.modal.alert(strings['script.content.deleteSuccess']);				
				deleteRowValues(row);
			} else {
				unblockUI();
				$.modal.alert(strings['script.user.deleteError']);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.deleteError']);
		}
	});
}

//============Open Modal to Add Content ===============
function openContentModalToAdd() {
    $("#addNewContent").validationEngine({promptPosition : "centerRight", scroll: false});
	manageIconIE('icon-star');
	
	$("#addContentModal").modal({
		title: 'Add Content',
		height: 430,
		width: 780,
		resizable: false,
		draggable: false,
		onOpen: setCKEditor('add'),
		actions: {
			'Close' : {
				color: 'red',
				click: function(win) { 
					//Fix for TD 77905 - By Joy
					$('#addNewContent').validationEngine('hide');
					$('.mandatoryDescription').hide();
					if($.browser.msie) setTimeout("hideMessage()", 300);
					win.closeModal(); 
				}
			},
			'Maximize' : {
				color: 'blue',
				click: function(win) { 
					$('#addNewContent').validationEngine('hide');
					$('.mandatoryDescription').hide();
					if($.browser.msie) setTimeout("hideMessage()", 300);
					$.modal.current.setModalContentSize($(window).width(), $(window).height()).centerModal();
				}
			}/*,
			'Restore' : {
				color: 'green',
				click: function(win) { 
					$('#addNewContent').validationEngine('hide');
					$('.mandatoryDescription').hide();
					if($.browser.msie) setTimeout("hideMessage()", 300);
					$.modal.current.setModalContentSize(780,430).centerModal();
				}
			}*/
		},
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);
							$('#addNewContent').validationEngine('hide');
							$('.mandatoryDescription').hide();
							if($.browser.msie) setTimeout("hideMessage()", 300);
							win.closeModal(); 
						}
					},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);	
							 if($("#addNewContent").validationEngine('validate')){
								$('#addNewContent').validationEngine('hide');
								addNewContent($("#addNewContent"), win);
							 }
						}
					}
				}
		});

}

function setCKEditor(purpose){
	var $objTextArea;
	if(purpose == 'add'){
		$objTextArea = $('#contentDescriptionEditor');
	}else if(purpose == 'edit'){
		$objTextArea = $('#contentDescriptionEditorEdit');
	}/*else if(purpose == 'modifyStandard'){
		$objTextArea = $('#objectiveDescriptionEditor');
	}*/else if(purpose == 'modifyGeneric'){
		$objTextArea = $('#genericDescriptionEditor');
	}
	
	if(CKEDITOR.instances[$objTextArea.attr('id') ] == undefined){
		CKEDITOR.inline($objTextArea.attr('id') );
	}else{
		for(name in CKEDITOR.instances)	{
			CKEDITOR.instances[name].destroy(true);
		}	
		CKEDITOR.inline($objTextArea.attr('id') );
		//CKEDITOR.replace($objTextArea.attr('id') );
	}
	unblockUI();
}

//============Insert Content ===============
function addNewContent(form, win) {
	blockUI();
	var addNewContentFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			addNewContentFlag = false;
			break;
		}
	    $('#addContentModal #contentDescription').val(editorVal);
	}
	
	if(addNewContentFlag == true){
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		
		var $addContentModal = $('#addContentModal');
		$addContentModal.find('#custProdId').val(custProdId);
		$addContentModal.find('#gradeId').val(gradeId);
		$addContentModal.find('#subtestId').val(subtestId);
		$addContentModal.find('#objectiveId').val(objectiveId);
		$addContentModal.find('#contentType').val(contentTypeId);
		$addContentModal.find('#contentTypeName').val(contentTypeName);
		
		var formObj = $('#addNewContent').serialize();
		$.ajax({
			type : "POST",
			url : 'addNewContent.htm',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					loadManageContentList();
					unblockUI();
					$('.mandatoryDescription').hide();
					$.modal.alert(strings['script.content.addSuccess']);
					
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		 unblockUI();
	}
}

//============Load grade id, name depending upon custProdId ===============
function populateGrade(){
	var custProdId = $('#custProdIdManageContent').val();
	if(custProdId != -1){
		var dataUrl = 'custProdId='+custProdId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateGrade.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#gradeIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

//============Load subtest id, name depending upon gradeId ===============
function populateSubtest(){
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	if(gradeId != -1){
		var dataUrl = 'gradeId='+gradeId+'&custProdId='+custProdId;;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateSubtest.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#subtestIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

//============Load Objective id, name depending upon subtestId ===============
function populateObjective(){
	var subtestId = $('#subtestIdManageContent').val();
	if(subtestId != -1){
		var gradeId = $('#gradeIdManageContent').val();
		var dataUrl = 'subtestId='+subtestId+'&gradeId='+gradeId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateObjective.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#objectiveIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

//============To populate any drop down ===============
function populateDropdownByJson(elementObject,jsonDataValueName,plsSelectFlag,clearFlag){
	elementObject.empty();
	var option = "";
	if((typeof plsSelectFlag !== 'undefined') && (plsSelectFlag == 1)){
		option += "<option value='-1'>"+strings['msg.pleaseSelect']+"</option>";
	}
	
	if((typeof clearFlag === 'undefined')){
		if(jsonDataValueName != null){
			if(jsonDataValueName != ""){
				$.each(jsonDataValueName, function(index, data) {
					option += '<option value='+data.value+'>'+data.name+'</option>';
			    });
			}else{
				$.modal.alert(strings['script.common.empty']);
			}
		}
	}
	
	elementObject.html(option);
	elementObject.change();
	elementObject.trigger('update-select-list');
}

//=============================AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA START=============================

function loadManageContentList() {
	
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var objectiveId = $('#objectiveIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	
	var checkFirstLoad=true;
	var paramUrl = 'custProdId='+custProdId+'&subtestId='+subtestId+'&objectiveId='+objectiveId+'&contentTypeId='+contentTypeId+'&checkFirstLoad='+checkFirstLoad;
	
		blockUI();
		$.ajax({
			type : "GET",
			url : 'loadManageContent.do',
			data : paramUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				$('#contentTableDiv').show();
				if (data != null && data != "" && data.length >= moreCount){
					$("#moreDiv").show(ANIMATION_TIME);
					$("#moreContents").removeClass("disabled");
					if($.browser.msie) $("#moreContents").removeClass("disabled-ie");
				} else {
					$("#moreDiv").hide(ANIMATION_TIME);
					$("#moreContents").addClass("disabled");
					if($.browser.msie) $("#moreContents").addClass("disabled-ie");
				}
				if ( data != null || data != ""){
					getContentDetails(checkFirstLoad,data);
					enableSorting(true);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
		enableSorting(true)
	}

function getContentDetails(checkFirstLoad,data) {
	var manageContent = '';
	if (checkFirstLoad) {
		$("#content_details").find("tr").remove();
	}
	$.each(data, function () { 
		var subHeaderVar = "";
	    if(this.subHeader != undefined){
	    	subHeaderVar = this.subHeader;
	    }
	    /* As per requirement, Performance level does not depend upon content - By Joy */
	    /*
		manageContent += '<tr name="contentIdRow" id="contentIdRow" value="'+this.contentId+'">'
			         	+'<th scope="row"><h5>' + this.contentName +'</h5></th>'
						+'<th scope="row">' + subHeaderVar +'</th>'
						+'<th scope="row">' + this.gradeName +'</th>'
						+'<th scope="row">' + getPerformanceLevelName(this.performanceLevel) +'</th>'
						+'<td class="vertical-center" nowrap>'
						+'<span class="button-group compact" width="50px">'
						+'<a href="#" class="button icon-pencil edit-content with-tooltip" contentId="'+this.contentId+'" title="Edit"></a>'
						+'<a href="#" class="button icon-trash with-tooltip confirm delete-content" contentId="'+this.contentId+'" title="Delete"></a>'
						+'</span>'
						+'</td>'
					+'</tr>';
					*/
	    manageContent += '<tr name="contentIdRow" id="contentIdRow" value="'+this.contentId+'">'
					     	+'<th scope="row"><h5>' + this.contentName +'</h5></th>'
							+'<th scope="row">' + subHeaderVar +'</th>'
							+'<th scope="row">' + this.gradeName +'</th>'
							+'<td class="vertical-center" nowrap>'
							+'<span class="button-group compact" width="50px">'
							+'<a href="#" class="button icon-pencil edit-content with-tooltip" contentId="'+this.contentId+'" title="Edit"></a>'
							+'<a href="#" class="button icon-trash with-tooltip confirm delete-content" contentId="'+this.contentId+'" title="Delete"></a>'
							+'</span>'
							+'</td>'
						+'</tr>';
	});
	$("#content_details").append(manageContent);
	$("#report-list").trigger("update");
	$(".headerSortDown").removeClass("headerSortDown");
	$(".headerSortUp").removeClass("headerSortUp");
	showHideJspElements();
}

function enableContentSorting(flag) {
	if (flag) {
		var ContentTable = $("#report-list");
		ContentTable.trigger("update");
	}

}


//============================= AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA END=============================

//==To hide add,search buttons and table==========
function hideContentElements(){
	$('#refresh-content').hide();
	$('#addContentDiv').hide();
	$('#modifyStandardDiv').hide();
	$('#modifyRscDiv').hide();
	$('#modifyEdaDiv').hide();
	$('#modifyAttDiv').hide();
	$('#modifyRbsDiv').hide();
	$('#modifyOarDiv').hide();
	$('#modifySplDiv').hide();
	$('#modifyOplDiv').hide();
	$('#div_performanceLevel').hide();
	$('#div_statusCode').hide();
	$('#contentTableDiv').hide();
}

//==To show add,search,description buttons==========
function showContentElements(){
	var objectiveId = $('#objectiveIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	
	if(gradeId != -1){
		if((contentTypeId == 'EDA') && ($("#MANAGE_CONTENT_EDA").val() == strings['manage.content.eda'])){
			$('#modifyEdaDiv').show();
		}else if((contentTypeId == 'ATT') && ($("#MANAGE_CONTENT_ATT").val() == strings['manage.content.att'])){
			$('#modifyAttDiv').show();
		}
		
		if(subtestId != -1){
			if((contentTypeId == 'RSC') && ($("#MANAGE_CONTENT_RSC").val() == strings['manage.content.rsc'])){
				$('#modifyRscDiv').show();
			}else if((contentTypeId == 'RBS') && ($("#MANAGE_CONTENT_RBS").val() == strings['manage.content.rbs'])){
				$('#div_performanceLevel').show();
				$('#div_statusCode').show();
				$('#modifyRbsDiv').show();
			}else if((contentTypeId == 'OAR') && ($("#MANAGE_CONTENT_OAR").val() == strings['manage.content.oar'])){
				$('#div_performanceLevel').show();
				$('#div_statusCode').show();
				$('#modifyOarDiv').show();
			}else if((contentTypeId == 'SPL')) {
				$('#div_performanceLevel').show();
				$('#div_performanceLevel_opl').hide();
				$('#modifySplDiv').show();
			}
			
			if(objectiveId != -1){
				if((contentTypeId == 'STD') && ($("#MANAGE_CONTENT_STANDARD").val() == strings['manage.content.standard'])){
					$('#modifyStandardDiv').show();
				}else if((contentTypeId == 'ACT' || contentTypeId == 'IND') && ($("#MANAGE_CONTENT_ADD").val() == strings['manage.content.add'])){
					$('#refresh-content').show();
					$('#addContentDiv').show();
				}else if((contentTypeId == 'OPL')) {
					$('#div_performanceLevel_opl').show();
					$('#div_performanceLevel').hide();
					$('#modifyOplDiv').show();
				}
			}
		}
	}
	
}

//==Get Performance level name==========
function getPerformanceLevelName(performanceLevel){
	var performanceLevelName = "";
	if(performanceLevel == 'A'){
		performanceLevelName = 'Pass'; 
	}else if(performanceLevel == 'B'){
		performanceLevelName = 'Did Not Pass';
	}else if(performanceLevel == 'U'){
		performanceLevelName = 'Undefined';
	}else if(performanceLevel == 'N'){
		performanceLevelName = 'DNR';
	}else if(performanceLevel == 'P'){
		performanceLevelName = 'Pass+';
	}
	return performanceLevelName;
}

//=============== get inors home page message =====================
function openInorsHomePage() {
	//Fixed for TD 77263 -  By Joy
	/*var iframeObj = $('#report-iframe-0').contents();
	var inorsHomeObj = iframeObj.find('#inorsHome');
	var taContentObj = iframeObj.find('#taContent');
	var blockDivObj = iframeObj.find('#blockDiv');
	if(inorsHomeObj.length) {
		blockUI();
		$.ajax({
			type : "GET",
			url : "loadHomePageMsg.do",
			data : null,
			dataType : 'json',
			cache: false,
			success : function(data) {
				unblockUI();
				blockDivObj.hide();
				taContentObj.val(data.value);
				inorsHomeObj.html(taContentObj.val());	
				
			},
			error : function(data) {
			if (data.status == "200") {
				unblockUI();
				blockDivObj.hide();
				taContentObj.val(data.responseText);
				inorsHomeObj.html(taContentObj.val());		
			} else {
				inorsHomeObj.html("<p class='big-message icon-warning red-gradient'>Error getting home page content. Please try later.</p>");
			}				
		  }
		});
	}*/
	
	if($('#report-iframe-0').contents().find('#inorsHome').length) {
		blockUI();
		$.ajax({
			type : "GET",
			url : "loadHomePageMsg.do",
			data : null,
			dataType : 'json',
			cache: false,
			success : function(data) {
				unblockUI();
				$('#report-iframe-0').contents().find('#blockDiv').hide();
				$('#report-iframe-0').contents().find('#taContent').val(data.value);
				$('#report-iframe-0').contents().find('#inorsHome').html($('#report-iframe-0').contents().find('#taContent').val());
			},
			error : function(data) {
			if (data.status == "200") {
				unblockUI();
				$('#report-iframe-0').contents().find('#blockDiv').hide();
				$('#report-iframe-0').contents().find('#taContent').val(data.responseText);
				$('#report-iframe-0').contents().find('#inorsHome').html($('#report-iframe-0').contents().find('#taContent').val());
			} else {
				inorsHomeObj.html("<p class='big-message icon-warning red-gradient'>"+strings['msg.err.homePageContent']+"</p>");
			}				
		  }
		});
	}
	
}

/**
 * This js file is to manage content module - End
 * Author: Joy
 * Version: 1
 */
// ================ Create Tree ==========================
var isOpeningFromRemote = false;
var isOpeningComplete = false;
var objectTree;
var currObj = '';

var objectTreeStack;
var loadFullpath = false;

var maxNodeLimit = 50;
var tempNodes = new Array();
var jsTreeJsonArr = new Array();
var tempIndex = 0;

var chkTreeContainerObj;
var filteredRow;

$(document).ready(function() {
	
	$(".download-instructions").live("click", function(event) {
		$(".formError").trigger("click");
	});
	
	$(".customRefresh").live("click", function(event) {
		event.stopImmediatePropagation();
		$(this).removeTooltip();
		$(document).click();
		var count = $(this).attr("count");
		var tabCount = $(this).attr("tabCount");
		blockUI('new-tab'+tabCount+'');
		var apiUrl = $(this).attr("apiurl");
		var reportUrl = $(this).attr("param");
		var reportName = $(this).attr("reportName");
		var reportId = $(this).attr("reportId");
		var formObj = $('.report-form-'+count);
		var obj = $('.report-frame-'+count);
		
		var dataURL = reportUrl + '&reportId='+reportId + '&reportName='+reportName+'&filter=true&' + $(formObj).serialize();
		$(obj).attr('src', apiUrl + '?reportUrl='+dataURL);
	});
	
	$("#downloadBulkPdf").on("click", function() {
		downloadBulkPdf('M', 'GD');
	});
	$("#downloadBulkPdfSeperate").on("click", function() {
		downloadBulkPdf('S', 'GD');
	});
	
	$("#downloadBulkICPdf").on("click", function() {
		downloadBulkPdf('M', 'IC');
	});
	$("#downloadBulkICPdfSeperate").on("click", function() {
		downloadBulkPdf('S', 'IC');
	});
	
	$("#downloadCandidateReport").on("click", function() {
		downloadBulkPdf('CR', 'CR');
	});
	
	
	$(".accordion-header").live("click", function() {
		$(".accordion-body").slideToggle(500);
		$(".icon-plus-round").toggle();
		$(".icon-minus-round").toggle();
	});
	
	showHideDownloadButtons();
	// this is to retain group download files field values
	$("#groupFile").on("change", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		retainDownloadValues();
		showHideDownloadButtons();
	});
	
	$("#collationHierarchy").on("change", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		retainDownloadValues();
	});
	
	$("#fileName").on("blur", function(event) {
		retainDownloadValues();
	});
	
	$("#email").on("blur", function(event) {
		retainDownloadValues();
	});
	
	$("#downloadStudentFileXML").on("click", function() {
		/*var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=XML&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileXML").attr("href", href);*/
		$(".success-message").hide(100);
		$(".error-message").hide(100);
		$(".error-message2").hide(100);
		var formObj = $('#downloadStudentFile');
		$(formObj).validationEngine();
		if(formObj.validationEngine('validate')) {
			if($("#p_Date_Type").val() == 'PD') {
				if(($("#p_Start_Date").val() != '' && $("#p_End_Date").val() != '')
						|| ($("#p_Start_Date").val() == '' && $("#p_End_Date").val() == '')) {
						var diff = new Date($("#p_End_Date").val()) - new Date($("#p_Start_Date").val());
						diff = diff/1000/60/60/24;
						if(diff > 30) {
							$(".error-message2").show(200);
						} else if(diff < 0) { 
							$.modal.alert('End date should be greater than start date.');
						} else {
							downloadStudentDataFile('XML');
						}
					} else {
						if($("#p_Start_Date").val() == '') $.modal.alert('Please provide start date.');
						if($("#p_End_Date").val() == '') $.modal.alert('Please provide end date.');
					}
			} else {
				$.modal.alert('For XML extract Date type should be Last Updated Date.');
			}		
		}
	});
	
	$("#downloadStudentFileCSV").on("click", function() {
		var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=CSV&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileCSV").attr("href", href);
	});
	
	$("#downloadStudentFileDAT").live("click", function() {
		$(".success-message").hide(100);
		$(".error-message").hide(100);
		$(".error-message2").hide(100);
		var formObj = $('#downloadStudentFile');
		$(formObj).validationEngine();
		if(formObj.validationEngine('validate')) {
			if(($("#p_Start_Date").val() != '' && $("#p_End_Date").val() != '')
				|| ($("#p_Start_Date").val() == '' && $("#p_End_Date").val() == '')) {
				var diff = new Date($("#p_End_Date").val()) - new Date($("#p_Start_Date").val());
				diff = diff/1000/60/60/24;
				if(diff > 30) {
					$(".error-message2").show(200);
				} else if(diff < 0) { 
					$.modal.alert('End date should be greater than start date.');
				} else {
					downloadStudentDataFile('DAT');
				}
			} else {
				if($("#p_Start_Date").val() == '') $.modal.alert('Please provide start date.');
				if($("#p_End_Date").val() == '') $.modal.alert('Please provide end date.');
			}
		}
	});
	
	/*$(".jqdatepicker").glDatePicker({
		onClick: function(target, cell, date, data) {
			var shortMonths = new Array("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
			target.val(date.getDate() + '-' + shortMonths[date.getMonth()] + '-' + date.getYear());
			if(data != null) {
				alert(data.message + '\n' + date);
			}
		}            
	});*/
	
	$('.jqdatepicker').datepicker({
		changeMonth: true,
	    changeYear: true
    });

	/* Extra action call in happening - Commented the function  */
	// GRT/IC File Download
	$("#p_test_administration").on("change", function(event) {
		var testAdm = $("#p_test_administration").val();
	});
	$("#p_test_program").on("change", function(event) {
	});
	$('#p_corpdiocese').on('change',function(){
	});
	$("#p_school").live("change", function(event) {
		var testAdministrationVal = $("#p_testAdministrationVal").val();
		var testProgram = $("#p_testProgram").val();
		var corpDiocese = $("#p_corpDiocese").val();
		var school = $("#p_school").val();
		if(school) {
			if (school == "-1") { // All
				var schoolCount = $('#p_school > option').length;
				$("#schoolCount").val(schoolCount);
			} else if (school == "-2") { // None Available
				$("#schoolCount").val("0");
			} else { // One Selected
				$("#schoolCount").val("1");
			}
		}
		var href = "downloadGRTInvitationCodeFiles.do?type=GRT&testAdministrationVal=" + testAdministrationVal + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		// $(".customRefresh").click();
	});
	$("#p_class").on("change", function(event) {
		var klassOptionsString = "";
		$("#p_class option").each(function() {
			klassOptionsString = klassOptionsString + $(this).val() + ",";
		});
		$("#klassOptionsString").val(klassOptionsString);
		var klass = $("#q_klass").val();
		if(klass){
			if (klass == "-1") { // All
				var klassCount = $('#p_class > option').length;
				$("#classCount").val(klassCount);
			} else if (klass == "-2") { // None Available
				$("#classCount").val("0");
			} else { // One Selected
				$("#classCount").val("1");
			}
		}
		// $(".customRefresh").click();
	});
	$("#downloadGRTFile").on("click", function() {
		var testAdministrationVal = $("#q_testAdministrationVal").val();
		var testProgram = $("#q_testProgram").val();
		var corpDiocese = $("#q_corpDiocese").val();
		var school = $("#q_school").val();
		var href = "downloadGRTInvitationCodeFiles.do?type=GRT&testAdministrationVal=" + testAdministrationVal + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadGRTFile").attr("href", href);
	});
	$("#downloadICFile").on("click", function() {
		var testAdministrationVal = $("#q_testAdministrationVal").val();
		var testProgram = $("#q_testProgram").val();
		var corpDiocese = $("#q_corpDiocese").val();
		var school = $("#q_school").val();
		var href = "downloadGRTInvitationCodeFiles.do?type=IC&2013&testAdministrationVal=" + testAdministrationVal + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadICFile").attr("href", href);
	});

	// Group Download
	$("#studentTableGDSelect").on("change", function(event) {
		var num = $("#studentTableGDSelect").val();
		$("#studentTableGDSelectedVal").html(num);
	});
	// Asynchronous : Submit to Group Download Files
	$("#downloadSeparatePdfsGD").on("click", function(event) {
		if(!$(this).hasClass('disabled')) {
			event.preventDefault();
			event.stopPropagation();
			$(document).click();
			groupDownloadSubmit('SP');
		}
	});
	// Asynchronous : Submit to Group Download Files
	$("#downloadCombinedPdfsGD").on("click", function(event) {
		if(!$(this).hasClass('disabled')) {
			event.preventDefault();
			event.stopPropagation();
			$(document).click();
			groupDownloadSubmit('CP');
		}
	});
	// Synchronous : Immediate download
	// $("#downloadSinglePdfsGD").on("click", function() {
	// groupDownloadSubmit('SS');
	//	});
	
	// Asynchronous : Submit to Group Download Files
	$("#downloadSeparatePdfsMAP").on("click", function(event) {
		if(!$(this).hasClass('disabled')) {
			event.preventDefault();
			event.stopPropagation();
			$(document).click();
			downloadMapCombined('SP');
		}
	});
	// Asynchronous : Submit to Group Download Files
	$("#downloadCombinedPdfsMAP").on("click", function(event) {
		if(!$(this).hasClass('disabled')) {
			event.preventDefault();
			event.stopPropagation();
			$(document).click();
			downloadMapCombined('CP');
		}
	});
	
	//Submit to GRF Download
	$("#downloadGrf").on("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		downloadGrf();
	});

// ==================== STUDENT DATATABLE IN GROUP DOWNLOAD ===========================
	$("#studentTableGD").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 0, 3, 4 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			//Fix for TD 77864 - By Joy
			/*$(".paginate_button").on("click", function() {
				refreshCheckBoxesFromTextBoxes();
			});*/
			filteredRow = this.$('tr', {"filter": "applied"} );
			refreshCheckBoxesFromTextBoxes();
			calculateAndChangeCheckAll();
		}
	});

	$( "#studentTableGD_length > label" ).css( "cursor", "default" );
	$( "#studentTableGD_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );

	/**
	 * Toggele self check status. Set all text box values.
	 * Refresh check boxes from text boxes.
	 */
	$("#checkAllImg").click(function() {
		selectAllFilteredRows();
	});
	/*$("#check-all").on("click", function() {
		var value = toggleACheckBox($('#check-all'));
		setAllTextBoxValues(value);
		refreshCheckBoxesFromTextBoxes();
	});*/

	/**
	 * Toggele self check status. Set the corresponding text box
	 * value. Calculate and change the colour of check all check
	 * box.
	 */
	$("input[id^=check-student-]").live("click", function() {
		toggleACheckBox($(this));
		var id = this.id;
		var studentId = id.substring(14);
		var textBox = $('#check-status-' + studentId);
		setATextBoxValue(textBox, this.value);
		calculateAndChangeCheckAll();
	});

	/**
	 * Refresh check boxes from text boxes.
	 */
	$('[name="studentTableGD_length"]').change(function() {
		//refreshCheckBoxesFromTextBoxes();
		//Fix for TD 77926 - By Joy
		$(".formError").trigger("click");
	});
	
	/*$(".sorting").live("click", function() {
		//refreshCheckBoxesFromTextBoxes();
	});
	
	$(".sorting_asc").live("click", function() {
		//refreshCheckBoxesFromTextBoxes();
	});
	
	$(".sorting_desc").live("click", function() {
		//refreshCheckBoxesFromTextBoxes();
	});*/
	
	//Fix for TD 77880 - By Joy
	$('#studentTableGD_filter input[type="text"]').live("keydown", function() {
		//$('#checkAllImg').prop('src', 'themes/acsi/img/empty.bmp');
		//$('#checkAllVal').val("0");
		setAllTextBoxValues("0");
		refreshCheckBoxesFromTextBoxes();
		calculateAndChangeCheckAll();
	});

	$('#groupDownload').validationEngine();
	
	clickTheRefreshButton();
	
	$('#userSearchRP').focus();
	
	$("#userSearchRP").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			if ($("#userSearchRP").val() != "" && $("#userSearchRP").val() != "Search") {
				$("#passwordResetStatusRP").attr("class", "wizard-fieldset fields-list hidden");
				getUserForManagePassword($("#userSearchRP").val());
			}
		}
	});

	$("a[id='userSearchIconRP']").live("click", function(e) {
		if ($("#userSearchRP").val() != "" && $("#userSearchRP").val() != "Search") {
			$("#passwordResetStatusRP").attr("class", "wizard-fieldset fields-list hidden");
			getUserForManagePassword($("#userSearchRP").val());
		}
	});

	$(".reset-pwd").click(function() {
		resetUserPwd($("#userSearchRpHidden").val());
	});

	$(".reset-pwd-search").click(function() {
		clearResetPasswordSearchResult();
	});
	
	if($('#userIdRP').length > 0) {
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-reset").parent().addClass("current");
	}
	
	$(".view-FileSize").live("click",function(e){
		var row =  $(this);
		var jobId = row.attr("jobId");
		var filePath = row.attr("filePath");
		var fileName = row.attr("fileName");
		row.closest('td').attr("id","count_"+jobId);
		row.closest('td').html('<span class="loader"></span>');
		$.ajax({
			type : "GET",
			url : "getFileSize.do",
			data : "jobId="+jobId+"&fileName="+fileName+"&filePath="+filePath,
			dataType : "json",
			cache:false,
			success : function(data){
				if (data != null){
					var count = data[0].fileSize; // buildRedirectToUserLink(jobId,data[0].fileSize);
					$("#count_"+jobId).html(count);
				} else {
					$.modal.alert("Error in retrieving file size");
					var buttonTag ='<span class="button-group compact"><a jobId="'+jobId+'" id="count_'+jobId+'" fileName="'+fileName+'" filePath="'+filePath+'" href="#nogo" class="button green-gradient disabled  with-tooltip view-FileSize" title="File not found" style="cursor: pointer;">Size</a></span>'
					$("#count_"+jobId).html(buttonTag);
				}
			},
			error : function(data){
				var buttonTag ='<span class="button-group compact"><a jobId="'+jobId+'" id="count_'+jobId+'" fileName="'+fileName+'" filePath="'+filePath+'" href="#nogo" class="button green-gradient disabled with-tooltip view-FileSize" title="File not found" style="cursor: pointer;">Size</a></span>'
				$("#count_"+jobId).html(buttonTag);							
				$.modal.alert("Error in retrieving file size");
			}
		});
		
	});
});

/**
 * This method removes the values of all text boxes and labels and hides the details.
 */
function clearResetPasswordSearchResult() {
	// Values removed from all text boxes and labels
	$("#userSearchRP").attr('readonly', false);
	$("#userIdRP").val("0");
	$("#userSearchRP").val("");
	$("#userSearchRpHidden").val("");

	$("#firstNameRP").text("");
	$("#middleNameRP").text("");
	$("#lastNameRP").text("");
	$("#emailRP").text("");
	//Fix for TD 80117
	$("#contactNumberRP").text("");
	$("#streetRP").text("");
	$("#cityRP").text("");
	$("#stateRP").text("");
	$("#zipRP").text("");
	$("#countryRP").text("");

	$("#question1RP").text("");
	$("#answer1RP").text("");
	$("#question2RP").text("");
	$("#answer2RP").text("");
	$("#question3RP").text("");
	$("#answer3RP").text("");

	$("#passwordResetStatusMsgRP").html("");
	$("#statusUsernameRP").text("");
	$("#statusPasswordRP").text("");

	// Legends are made hidden
	$("#passwordResetStatusRP").attr("class", "wizard-fieldset fields-list hidden");
	$("#userDetailsRP").attr("class", "wizard-fieldset fields-list hidden");
	$("#securityQuestionsRP").attr("class", "wizard-fieldset fields-list hidden");
}

/**
 * This method resets the user password and sends the password to the user via email.
 * 
 * @param username
 */
function resetUserPwd(username) {
	var userId = $("#userIdRP").val();
	var email = $("#emailRP").text();
	if (userId == "0") {
		$.modal.alert(strings['script.trySearch']);
	} else {
		$.modal.confirm(strings['msg.rp.confirm'] + username + "?", function() {
			blockUI();
			$.ajax({
				type : "GET",
				url : "resetUserPassword.do",
				data : "username=" + username + "&email=" + email,
				dataType : 'json',
				cache : false,
				success : function(data) {
					unblockUI();
					if (data != null && data.resetPwdFlag == "1") {
						$("#passwordResetStatusMsgRP").html("<span>"+strings['msg.rp.success']+"</span>");
						$("#statusUsernameRP").text(username);
						if (data.sendEmailFlag == "1") {
							$("#statusEmailRP").html("<span style=\"color: green\">"+strings['msg.rp.email.success']+"</span>");
						} else {
							$("#statusEmailRP").html("<span style=\"color: red\">"+strings['msg.rp.email.failure']+"</span>");
						}
						$("#passwordResetStatusRP").attr("class", "wizard-fieldset fields-list");
					} else {
						$("#passwordResetStatusRP").attr("class", "wizard-fieldset fields-list hidden");
						$.modal.alert(strings['script.parent.passwordResetError']);
					}
					$('#userSearchRP').val(username);
				},
				error : function(data) {
					unblockUI();
					$.modal.alert(strings['script.parent.passwordResetError']);
				}
			});
		}, function() {
			// this function closes the confirm modal on clicking cancel button
		});
	}
}

/**
 * This method retrievs the user details from the database based on the username. It uses exact username search.
 * 
 * @param username
 */
function getUserForManagePassword(username) {
	blockUI();
	$.ajax({
		type : "GET",
		url : "getUserForResetPassword.do",
		data : "username=" + username,
		dataType : "json",
		cache : false,
		success : function(data) {
			unblockUI();
			if (data) {
				$("#userSearchRpHidden").val(username);
				$("#userIdRP").val(data.userId);
				$('#userSearchRP').blur(); 

				$("#firstNameRP").text(data.firstName);
				$("#middleNameRP").text(data.middleName);
				$("#lastNameRP").text(data.lastName);
				$("#emailRP").text(data.emailId);
				$("#contactNumberRP").text(data.phoneNumber);
				$("#streetRP").text(data.street);
				$("#cityRP").text(data.city);
				$("#stateRP").text(data.state);
				$("#zipRP").text(data.zip);
				$("#countryRP").text(data.country);

				if (data.pwdHintList[0]) {
					$("#question1RP").text(data.pwdHintList[0].questionValue);
					$("#answer1RP").text(data.pwdHintList[0].answerValue);
				}
				if (data.pwdHintList[1]) {
					$("#question2RP").text(data.pwdHintList[1].questionValue);
					$("#answer2RP").text(data.pwdHintList[1].answerValue);
				}
				if (data.pwdHintList[2]) {
					$("#question3RP").text(data.pwdHintList[2].questionValue);
					$("#answer3RP").text(data.pwdHintList[2].answerValue);
				}
				if (data.userId == 0) {
					$.modal.alert(strings['script.noUserFound']);
					$("#userDetailsRP").attr("class", "wizard-fieldset fields-list hidden");
					$("#securityQuestionsRP").attr("class", "wizard-fieldset fields-list hidden");
					$("#userSearchRP").attr('readonly', false);
				} else {
					$("#userSearchRP").attr('readonly', true);
					$("#userDetailsRP").attr("class", "wizard-fieldset fields-list");
					$("#securityQuestionsRP").attr("class", "wizard-fieldset fields-list");
				}
			} else {
				$.modal.alert(strings['script.noUserFound']);
				$("#userDetailsRP").attr("class", "wizard-fieldset fields-list hidden");
				$("#securityQuestionsRP").attr("class", "wizard-fieldset fields-list hidden");
				$("#userSearchRP").attr('readonly', false);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.search']);
		},
		complete : function(data) {
		}
	});
}

function selectAllFilteredRows(){
	var val = $('#checkAllVal').val();
	if (val == "0") {
		$('#checkAllImg').prop('src', 'themes/acsi/img/selected.bmp');
		$('#checkAllVal').val("1");
		setFilteredTextBoxValues("1");
	} else if ((val == "1") || (val == "-1")) {
		$('#checkAllImg').prop('src', 'themes/acsi/img/empty.bmp');
		$('#checkAllVal').val("0");
		setFilteredTextBoxValues("0");
	}
	refreshCheckBoxesFromTextBoxes();
}

/**
 * Programatically click the Refresh button.
 */
var refreshUrls = new Array("/public/INORS/Report/Report1_files", "/public/INORS/Report/Report2_files");
function clickTheRefreshButton() {
	var reportUrl = $("#reportUrl").val();
	for (var i = 0; i < refreshUrls.length; i++) {
		if (reportUrl == refreshUrls[i]) {
			$(".customRefresh").click();
			break;
		}
	}
}

function calculateAndChangeCheckAll() {
	var totalStudents = getTotalStudentCount();
	var checkedStudents = getCheckedStudentCount();
	if (checkedStudents == 0) {
		$('#checkAllImg').prop('src', 'themes/acsi/img/empty.bmp');
		$('#checkAllVal').val("0");
	} else if (checkedStudents == totalStudents) {
		$('#checkAllImg').prop('src', 'themes/acsi/img/selected.bmp');
		$('#checkAllVal').val("1");
	} else {
		$('#checkAllImg').prop('src', 'themes/acsi/img/tristate.bmp');
		$('#checkAllVal').val("-1");
	}
}

function getTotalStudentCount(){
	var count = 0;
	/*$("input[id^=check-status-]").each(function() {
		count = count + 1;
	});*/
	count = filteredRow.length;
	return count;
}

function getCheckedStudentCount(){
	var count = 0;
	$("input[id^=check-status-]").each(function() {
		var value = this.value;
		if(value == "1") {
			count = count + 1;
		}
	});
	return count;
}

function setAllTextBoxValues(value){
	$("input[id^=check-status-]").each(function() {
		setATextBoxValue($(this), value);
	});
}

function setFilteredTextBoxValues(value){
	for (var i = 0; i < filteredRow.length; i++) {
		var dtRowIndex = filteredRow[i]._DT_RowIndex;
		var textBox = $("input[name=check-status-" + dtRowIndex + "]");
		setATextBoxValue(textBox, value);
	}
}

function setATextBoxValue(textBox, value){
	textBox.val(value);
}

/**
 * Toggle text boxes. Refresh check boxes from text boxes.
 * 
 * @param value
 */
function toggleAllCheckBoxes(value) {
	$("input[id^=check-status-]").each(function() {
		toggleACheckBox($(this));
	});
}

function toggleACheckBox(checkBox) {
	var value = checkBox.val();
	if (value == "1") {
		checkBox.removeAttr('checked');
		checkBox.val("0");
		return "0";
	} else if (value == "0") {
		checkBox.attr('checked', 'checked');
		checkBox.val("1");
		return "1";
	}
}

function refreshCheckBoxesFromTextBoxes() {
	$("input[id^=check-student-]").each(function() {
		refreshACheckBoxFromATextBox($(this));
	});
}

function refreshACheckBoxFromATextBox(checkBox){
	var id = checkBox.attr("id");
	var studentId = id.substring(14);
	var checkStatus = $('#check-status-' + studentId).val();
	setACheckBox(checkBox, checkStatus);
}

function setACheckBox(checkBox, value) {
	if (value == "1") {
		checkBox.attr('checked', 'checked');
		checkBox.val("1");
	} else if (value == "0") {
		checkBox.removeAttr('checked');
		checkBox.val("0");
	}
}

// =============== retain group download files field values =====================
function retainDownloadValues() {
	var formObj = $('#groupDownload');
	$.ajax({
		type : "GET",
		url : 'retainDownloadValues.do',
		data : $(formObj).serialize(),
		dataType : 'json',
		cache : false,
		success : function(data) {
			// do nothing
		},
		error : function(data) {
			// do nothing
		}
	});
}

function clearGDCache() {
	var formObj = $('#groupDownload');
	$.ajax({
		type : "GET",
		url : 'clearGDCache.do',
		data : $(formObj).serialize(),
		dataType : 'json',
		cache : false,
		success : function(data) {
			// do nothing
		},
		error : function(data) {
			// do nothing
		}
	});
}

// ================== Download Bulk PDFs =====================
function downloadBulkPdf(type, mode) {
	$("li.active").click();
	$(".error-message").hide(200);
	$(".success-message").hide(200);
	blockUI();
	
	if(type != 'CR' && $('.jstree-checked').length == 0 && chkTreeContainerObj) {
		// check default school
		chkTreeContainerObj.jstree("check_all");
	}
	var formObj = $('#groupDownload');
	$(formObj).validationEngine();
	if(formObj.validationEngine('validate')) {
		var url = 'downloadBulkPdf.do';
		if(type != 'CR') {
			var checked_ids = []; 
			$("#chkTreeViewForOrg").jstree("get_checked",null,true).each(function () { 
				checked_ids.push(this.id + '|' + $(this).attr("orglevel")); 
			}); 
			$("#selectedNodes").val(checked_ids.join(","));
		} else {
			url = 'downloadCandicateReport.do';
		}
		$.ajax({
			type : "GET",
			url : url,
			data : $(formObj).serialize()+"&fileType="+type+"&mode="+mode,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if (data.status == 'Success')	{
					$(".success-message").show(200);
				} else {
					$(".error-message").show(200);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
		
		
	}
	event.stopPropagation();
	event.preventDefault();
}

//======================================CREATE  TREE STURCTURE================================
	/*function createTree() {	
		chkTreeContainerObj =$("#chkTreeViewForOrg");
		$(function (){
			$("#slide_menu_user").removeClass("navigable");
			$("#slide_menu_org").removeClass("navigable");
            $("#slide_menu_parent").removeClass("navigable");
            $("#slide_menu_student").removeClass("navigable");
			chkTreeContainerObj.jstree( {                  
					"json_data" : {                       
						//"data":jsondata ,
						"maxNodeLimit" : maxNodeLimit,
						"clickedNode" : function (n) {
											return (n.attr ? n.attr("id") : chkTreeContainerObj.attr("rootid"));
										},	
						"ajax" : { 
							"url" : "groupDownloadHierarchy.do",
							"cache":false,
							"data" : function (n) {
								nodeLevel = (n.attr ? n.attr("orglevel") : "-99");
								clickedNode = (n.attr ? n.attr("id") : chkTreeContainerObj.attr("rootid"));
								return {
											"tenantId" : clickedNode,
											"reportUrl": chkTreeContainerObj.attr("reportUrl"),
											"nodeLevel": nodeLevel
									   };
								},									
							"success": function(data, textStatus, XMLHttpRequest) {										 
								if(data != null && data != "") {
									if(data.length > maxNodeLimit)
									{													
										//alert(data.length);
										tempNodes = data;
										tempIndex = clickedNode;
									}
								}								
							},
							"complete" : function() { 
									$("#chkTreeViewForOrg ul:first").css("float", "left");
									$("#chkTreeViewForOrg ul:first").css("min-width", "100%");
									$("#chkTreeViewForOrg [id="+clickedNode+"]").removeClass("jstree-closed").addClass("jstree-open");
									if(tempNodes != null)
									{
										if(tempNodes.length > maxNodeLimit) {
											var moreIndex = parseInt($(".no-click").index($("[id="+clickedNode+"] .no-click")));
											//alert(moreIndex);
											var tempObj = new Object();													
											tempObj.indexCount = maxNodeLimit;
											tempObj.nodeJsonData = tempNodes; 
											jsTreeJsonArr[tempIndex] = new Array();
											jsTreeJsonArr[tempIndex][moreIndex] = tempObj;
											tempObj = null;
											tempNodes = null;
										}
									} 												
									onClickMoreLink("chkTreeViewForOrg",tempIndex);
									
									var rootOrg = $("#chkTreeViewForOrg > ul > li:first").attr("id");
									var tempTree = $("#tempTree").val();
								
									unblockUI();
																			
							}
						}
							
					},
					 
					"themes": { theme: "apple", dots: true },
					"plugins" : [ "themes", "json_data", "ui" ]		
					
				});
		});
		
		getNodeCount();
		
	}
	
	// =============== find selected node count ======================
	function getNodeCount() {
		chkTreeContainerObj.bind("open_node.jstree close_node.jstree check_node.jstree uncheck_node.jstree", function(e) {
			var stud = 0, cls = 0, sch = 0;
			$('.jstree-checked').each(function() {
				if($(this).attr('orglevel') == -1) stud++;
				if($(this).attr('orglevel') == 4) cls++;
				if($(this).attr('orglevel') == 3) sch++;
			});
			$('.jstree-undetermined').each(function() {
				if($(this).attr('orglevel') == 4) cls++;
				if($(this).attr('orglevel') == 3) sch++;
			});
			$("#studCount").html(stud);
			$("#classCount").html(cls);
			$("#schoolCount").html(sch);
			console.log('stud:'+stud+' cls:'+cls+' sch:'+sch);
		});
	}
		
		// =============== THIS FUNCTION CLICKS "more" LINK IN JS-TREE UNTILL THE NODE IS VISIBLE ===================
		function clickMoreForRedirect(lastnode, currSelectedNode) {
			var currObj;
			$.each(objectTreeStack, function(index) {
				if(currSelectedNode == objectTreeStack[index]) {
					currObj = objectTreeStack[index-1];
				}
			});
			$("span[treespanlevel^="+currObj+"]").click();
			var currnode = $("#"+currSelectedNode);
			if(lastnode) {
				if(currnode != null && currnode.length > 0) {
					chkTreeContainerObj.jstree('select_node', $("#"+currSelectedNode));
					//$("#"+currSelectedNode).find("a")[0].click();
					$("#"+currSelectedNode).find("a").click();
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			} else {
				if(currnode != null && currnode.length > 0) {
					chkTreeContainerObj.jstree('open_node', $("#"+currSelectedNode));
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			}
		}
		
		//Function to select the first node of the tree
		var isFirstTime=true;
		function selectTheFirstNodeOfTree(nodeId){
		 if (isFirstTime){
			$("#"+nodeId+" "+"a").click();
			  isFirstTime = false;
			}
		}
		
		function onClickMoreLink(divId,index)
		{
			$("#"+divId+" [treeSpanLevel="+index+"].jstree-more-link").click(function(e) {		//alert('more');			
				var nodeJsonData = new Array();			
				var jsTreeNodeLevel = $(this).attr("treeSpanLevel");
				var tempNode = $(this).parent("span.no-click");
				var moreIndex = $(".no-click").index(tempNode);
				var nodeTempArr = jsTreeJsonArr[jsTreeNodeLevel][moreIndex];

				var startLimit = parseInt(nodeTempArr.indexCount);	
				//alert(startLimit);				
				nodeJsonData = nodeTempArr.nodeJsonData.slice(startLimit,(startLimit+maxNodeLimit));		
				var node = $(tempNode).prev();
				var nodesHTML = getJstreeNodes(nodeJsonData,divId,$(tempNode).attr("treelevel"));
				//alert(nodesHTML);		
				$(node).after(nodesHTML);			
				if(nodeJsonData.length == maxNodeLimit && nodeTempArr.nodeJsonData.length != (startLimit+maxNodeLimit))
				{		
					jsTreeJsonArr[jsTreeNodeLevel][moreIndex].indexCount = (startLimit+maxNodeLimit);				
				}
				else {
					//$(node).addClass("jstree-last");
					$(tempNode).remove();
					$(node).nextAll(":last").addClass("jstree-last");							
				}		
			});											   
		}
		
		function getJstreeNodes(nodeJsonData,divId,linkIndex)
		{
			var html = "";
			var jsTreeClassName = "";
			var chkLen = $("#"+divId+" [treelevel="+linkIndex+"]").parent("ul").parent("li.jstree-checked").length;	
			for(var index=0; index<nodeJsonData.length; index++) {
				var nodeDetails = nodeJsonData[index];
				if(chkLen > 0) {		
					jsTreeClassName = "jstree-checked ";
				}
				else {
					jsTreeClassName = "jstree-unchecked ";
				}
				//jsTreeClassName += ((nodeDetails.attr.className != undefined) ? nodeDetails.attr.className : "");
				if(nodeDetails.attr.className == "jstree-leaf") {		
					jsTreeClassName += " jstree-leaf";
				}
				else {		
					jsTreeClassName += " jstree-closed";
				}
				if(nodeDetails.attr.assignedtotest == 1) {		
					jsTreeClassName += " jstree-makebold";
				}
				//html += "<li "+((nodeDetails.attr.assignedtotest == 1)? "assignedtotest=1": "")+" "+((nodeDetails.attr.redoflag == 1)? "redoflag=1": "")+" id="+nodeDetails.attr.id+" label="+nodeDetails.attr.label+" level="+nodeDetails.attr.level+" title='"+nodeDetails.attr.title+"' class='"+jsTreeClassName+"'><ins class='jstree-icon'>&nbsp;</ins><a href='#' class=''><ins class='jstree-checkbox' "+((nodeDetails.attr.assignedtotest == 1)? "style='display:none;'" : "")+">&nbsp;</ins><ins class='jstree-icon'>&nbsp;</ins>"+nodeDetails.data+"</a></li>";
				html +='<li createdby="'+nodeDetails.attr.createdBy+'" udatedby="'+nodeDetails.attr.udatedBy+'" tenantid="'+nodeDetails.attr.tenantId+'" parenttenantid="'+nodeDetails.attr.parentTenantId+'" noofchildorgs="'+nodeDetails.attr.noOfChildOrgs+'" noofusers="'+nodeDetails.attr.noOfUsers+'" orglevel="'+nodeDetails.attr.orgLevel+'" id="'+nodeDetails.attr.id+'" class="'+jsTreeClassName+'"><ins class="jstree-icon">&nbsp;</ins><a href="#" class=""><ins class="jstree-checkbox">&nbsp;</ins><ins class="jstree-icon">&nbsp;</ins>'+nodeDetails.data+'</a></li>';
				jsTreeClassName = "";
			}
			return html;
		}*/
		//=============================DELETE Group Files ON CLICK======================
		$('.delete-GroupFiles').on("click", function() {
		    var row = $(this);
			var jobId = $(this).attr("jobId");
			$.modal.confirm(strings['msg.confirm.delete'],
				function () {
				deleteGroupFilesDetails(jobId,row);
				},function() {//this function closes the confirm modal on clicking cancel button
				} 
			);
			
		});	
		
		//=============================Download Group Files ON CLICK======================
		
		function eraseCache(){
			  window.location = window.location.href+'?eraseCache=true';
			}
		
		$('.download-GroupFiles').on("click", function() {
			var row =  $(this);
			var jobId = row.attr("jobId");
			var filePath = row.attr("filePath");
			var fileName = row.attr("fileName");
			var requestType = row.attr("requestType");
			var orgLevel = row.attr("orgLevel");
			var availability = downloadGroupFilesDetails(row,jobId,filePath,fileName);
			if(availability == true) {
				var href = "downloadGroupDownloadFiles.do?"+'jobId='+jobId+'&fileName='+fileName + '&requestType=' + requestType + '&orgLevel=' + orgLevel +'&filePath='+filePath;
				$(".download-GroupFiles").attr("href", href);
			} else {
				if ((requestType == "SDF") && (orgLevel == "1")) {
					if ((fileName.match(".DAT$")) || (fileName.match(".dat$"))) {
						filePath = filePath.replace(".DAT",".zip"); 
						filePath = filePath.replace(".dat",".zip");
						fileName = fileName.replace(".DAT",".zip"); 
						fileName = fileName.replace(".dat",".zip");
						var href = "downloadGroupDownloadFiles.do?" + 'jobId=' + jobId + '&fileName=' + fileName + '&requestType=' + requestType + '&orgLevel=' + orgLevel + '&filePath=' + filePath;
						$(".download-GroupFiles").attr("href", href);
					} else {
						var href = "downloadGroupDownloadFiles.do?" + 'jobId=' + jobId + '&fileName=' + fileName + '&requestType=' + requestType + '&orgLevel=' + orgLevel + '&filePath=' + filePath;
						$(".download-GroupFiles").attr("href", href);
					}
				} else {
					$(".download-GroupFiles").attr("href", "#");
					$.modal.alert(strings['msg.fnf']);
				}
			} 
		});
		
		function downloadGroupFilesDetails(row,jobId,filePath,fileName) {
			var availability = false;
				$.ajax({
					type : "GET",
					url : 'checkFileAvailability.do',
					data : "filePath="+filePath,
					dataType: 'html',
					async : false,
					success : function(data) {
						var obj = jQuery.parseJSON(data);
						if(obj.status == 'Success') {
							availability = true;
						}
					}
				});
				return availability;
			}
		
		$('.view-requestdetails').on("click", function() {
			var row = $(this);
			var jobId = $(this).attr("jobId");
			viewrequestDetails(jobId);
		});
		
		function viewrequestDetails(jobId)
		{
				    manageIconIE('icon-star');
				    var param = "jobId=" + jobId;	
				    blockUI();
				    $.ajax({
						type : "GET",
						url : "getRequestDetailViewData.do", 
						data : param,
						dataType : 'json',
						cache:false,
						success : function(data) {
							unblockUI();
							/*$("textarea#requestSummary").val(data[0].requestSummary);
							$("#viewRequestDetail").modal({
								title: 'View Request Details',
								height: 350,
								width: 600,
								resizable: true,
								draggable: true,
							});*/
							var requestDetails = data[0].requestSummary;
							requestDetails = requestDetails.replace(/\n/g, '<br />');
							$("#requestDetailsContainerGD").html(requestDetails);
							$("#requestDetailsContainerGD").modal({
								title: 'View Request Details',
								buttonsAlign: 'center'
							});
						},
						error : function(data) {
							$.modal.alert(strings['script.common.error1']);
						}
					})	
		}
		
		//=============================AJAX CALL TO DELETE Group Files FROM DB TABLES=============================
			function deleteGroupFilesDetails(jobId,row) {
					blockUI();
					$.ajax({
						type : "GET",
						url : 'deleteGroupDownloadFiles.do',
						data : "jobId="+jobId,
						dataType: 'html',
						cache:false,
						success : function(data) {
							var obj = jQuery.parseJSON(data);
							if(obj.status == 'Success') {
								$.modal.alert(strings['msg.jobDeleted']);
								unblockUI();
								//deleteRowValues(row);//this method is present in manageUser.js
								row.closest("tr").remove();
							} else {
								$.modal.alert(strings['msg.fileDeleteError']);
								unblockUI();
							}
							unblockUI();
						},
						error : function(data) {
							$.modal.alert(strings['msg.fileDeleteError']);
							unblockUI();
						}
					});
				}
			
			// ================ Remove duplicate icons in IE ============================
			function manageIconIE(icon) {
				if($.browser.msie) {
					$('.'+icon).html('');
					$('.'+icon+' > .empty').hide();
				}
			}

/**
 * Custom Ajax call. Returns a json from the server.
 * 
 * @param requestType
 *            http request type: "GET", "POST", "PUT" or "DELETE"
 * @param requestUrl
 *            utl to the request. Example: populateDistrictGrt.do
 * @param inputData
 *            data to be sent to the server
 * @param outputDataType
 *            type of the response data
 * @param browserCache
 *            boolean. Whether the output will be cached by the browser.
 * @param asyncRequest
 *            boolean. Whether the request is asynchronous.
 * @param errMsg
 *            meaasage on $.ajax error. Example: "Server responds in Error"
 * @returns json server response data
 * @author <a href="mailto:amitabha.roy@tcs.com">Amitabha Roy</a>
 */
function customAjaxCall(requestType, requestUrl, inputData, outputDataType, browserCache, asyncRequest, errMsg) {
	var json = {};
	if (asyncRequest == false)
		blockUI();
	$.ajax({
		type : requestType,
		url : requestUrl,
		data : inputData,
		dataType : outputDataType,
		cache : browserCache,
		async : asyncRequest,
		success : function(data) {
			json = data;
			if (asyncRequest == false)
				unblockUI();
		},
		error : function(data) {
			if (asyncRequest == false)
				unblockUI();
			$.modal.alert(errMsg);
		}
	});
	return json;
}

/**
 * Populates a Dropdown. Written to be used in pages:
 * <ul>
 * <li>GRT/IC File Download</li>
 * <li>Group Download</li>
 * </ul>
 * 
 * @param element
 *            html dropdown element.
 * @param json
 *            the json data from which the dropdown will be populated
 * @param selectValue
 *            value of the selectText
 * @param selectText
 *            optional "Please Select" option in the dropdown
 * @param showId
 *            boolean. Whether to show the value along with the text in the
 *            dropdown
 */
function populateDropdownByIdWithJson(element, json, selectValue, selectText, showId) {
	element.empty();
	var option = "";
	if (selectValue && selectText){
		option += '<option value=' + selectValue + '>' + selectText + '</option>';
	}
	if ((json != null) && (json != "") && (json.length > 0)) {
		$.each(json, function(index, data) {
			if (showId && showId == true) {
				option += '<option value='+data.value+'>'+data.name+' ('+data.value+')</option>';
			} else {
				option += '<option value=' + data.value + '>' + data.name + '</option>';
			}
		});
	}
	element.html(option);
	element.change();
	element.trigger('update-select-list');
}


/**
 * Populates the Student Table
 * @Impotrant
 */
function populateStudentTableGD() {
	$(".error-message").hide(200);
	var formEle = $("#groupDownload");
	if(formEle) {
		var orgNodeId = $('#q_klass').val();
		if (orgNodeId != "-2") {
			var transferObject = getGroupDownloadTO();
			blockUI();
			$.ajax({
				type : "GET",
				url : 'populateStudentTableGD.do',
				data : transferObject,
				dataType: 'json',
				cache:false,
				success : function(data) {
					if(data && data.length && data.length > 0) {
						populateStudentTableByJson(data);
						//$("#studentTableGD").removeClass('hidden');
						//$("#studentTableGD").show();
					} else {
						$(".error-message").html(strings['msg.studentNotFound']);
						$(".error-message").show(200);
						//$("#studentTableGD").hide();
					}
					unblockUI();
				},
				error : function(data) {
					$(".error-message").html(strings['msg.studentNotFound']);
					$(".error-message").show(200);
					unblockUI();
				}
			});
		}
	}
}

/**
 * 
 * @param json
 */
function populateStudentTableByJson(json) {
	var elementObject = $('#studentListGD');
	elementObject.empty();
	var rows = "";
	var count = 0;
	if (json != null) {
		if ((json != "") && (json.length > 0)) {
			$.each(json, function(index, data) {
				count = count + 1;
				if (count%2 == 0)
					rows += '<tr class="even">';
				else
					rows += '<tr class="odd">';
				rows += '<th scope="row" class="checkbox-cell  sorting_1"><input name="checked[]" id="check-student-' + data.value+ '" value="'+data.value+'" type="checkbox" /></th>';
				rows += '<td>' + data.name + '</td>';
				rows += '</tr>';
			});
		} else {
			$.modal.alert(strings['script.common.empty']);
		}
	}
	elementObject.html(rows);
}

/**
 * GroupDownloadTO Constructor
 */
function GroupDownloadTO(button, testAdministrationVal, testAdministrationText,
		testProgram, district, school, klass, grade, students, groupFile,
		collationHierarchy, fileName, email, userId, userName, adminId,
		customerId, orgNodeLevel, extractStartDate, gdfExpiryTime,
		requestDetails, jobLog, jobStatus, fileSize, jobId) {
	this.button = button;
	this.testAdministrationVal = testAdministrationVal;
	this.testAdministrationText = testAdministrationText;
	this.testProgram = testProgram;
	this.district = district;
	this.school = school;
	this.klass = klass;
	this.grade = grade;
	this.students = students;
	this.groupFile = groupFile;
	this.collationHierarchy = collationHierarchy;
	this.fileName = fileName;
	this.email = email;
	this.userId = userId;
	this.userName = userName;
	this.adminId = adminId;
	this.customerId = customerId;
	this.orgNodeLevel = orgNodeLevel;

	this.extractStartDate = extractStartDate;
	this.gdfExpiryTime = gdfExpiryTime;
	this.requestDetails = requestDetails;
	this.jobLog = jobLog;
	this.jobStatus = jobStatus;
	this.fileSize = fileSize;
	this.jobId = jobId;
}

/**
 * 
 * @returns {GroupDownloadTO}
 */
function getGroupDownloadTO() {
	var button = $("#buttonGD").val();
	var testAdministrationVal = $("#q_testAdministrationVal").val();
	var testAdministrationText = $("#q_testAdministrationText").val();
	var testProgram = $("#q_testProgram").val();
	var district = $("#q_corpDiocese").val();
	var school = $("#q_school").val();
	var klass = $("#q_klass").val();
	var grade = $("#q_grade").val();
	var students = getSelectedStudentIdsAsCommaString();
	var groupFile = $("#q_groupFile").val();
	var collationHierarchy = $("#q_collationHierarchy").val();
	var fileName = $("#fileName").val();
	var email = $("#email").val();
	var userId = "";
	var userName = "";
	var adminId = "";
	var customerId = "";
	var orgNodeLevel = "";

	var extractStartDate = "";
	var gdfExpiryTime = "";
	var requestDetails = "";
	var jobLog = "";
	var jobStatus = "";
	var fileSize = "";
	var jobId = "";
	var to = new GroupDownloadTO(button, testAdministrationVal,
			testAdministrationText, testProgram, district, school, klass,
			grade, students, groupFile, collationHierarchy, fileName, email,
			userId, userName, adminId, customerId, orgNodeLevel,
			extractStartDate, gdfExpiryTime, requestDetails, jobLog, jobStatus,
			fileSize, jobId);
	return to;
}

function getSelectedStudentIdsAsCommaString() {
	var students = "";
	$("input[id^=check-status-]").each(function() {
		if (this.value == "1") {
			var id = this.id;
			var studentId = id.substring(13);
			students = students + "," + studentId;
		}
	});
	if (students.length > 0) {
		students = students.substring(1);
	}
	return students;
}

/**
 * Download MAP GLA ISR PDF based on selected student and subtest
 * @param studentId
 */
function downloadMapIsr(studentId) {
	blockUI();
	var subtest = $("#p_subtest", window.parent.document); 
	var tab = $("li.active", window.parent.document).attr("param");
	tab = tab.replace(/new-tab/g, "report-form-");
	var formObj = $('.'+tab, window.parent.document);
	var dataUrl = $(formObj).serialize() + '&studentId=' + studentId;
	
	location.href = 'downloadMapIsr.do?' + dataUrl;
	unblockUI();
}

/**
 * Download MAP GLA ISR PDF based on multi-select student and subtest
 * @param mode (SP or CP)
 */
var fileName = "";
function downloadMapCombined(mode) {
	displayGroupDownloadStatus(undefined);
	if ($("#groupDownload").validationEngine('validate')) {
		blockUI();
		
		var nameChanged = false;
		
		if(fileName == "") {
			fileName = $("#fileName").val();
			nameChanged = true;
		} else {
			// check if user changed file name
			if(fileName == $("#fileName").val()) {
				nameChanged = false;
				unblockUI();
				$.modal.alert('Seems like you have just requested a download with same "file name (Name of Generated File)". Please change the file name and try again.');
			} else {
				fileName = $("#fileName").val();
				nameChanged = true;
			}
		}
		
		if(nameChanged) {
			var status = false;
			var subtest = $("#p_subtest", window.parent.document); 
			var tab = $("li.active", window.parent.document).attr("param");
			tab = tab.replace(/new-tab/g, "report-form-");
			var formObj = $('.'+tab, window.parent.document);
			var studentId = getSelectedStudentIdsAsCommaString();
			if(studentId == "") {
				unblockUI();
				fileName = "";
				$.modal.alert('Please select student(s)');
			} else {
				var dataUrl = $(formObj).serialize() + '&mode=' + mode + '&studentId=' + studentId + '&fileName=' + $("#fileName").val() + '&email=' + $("#email").val();
				
				$.ajax({
					type : "POST",
					url : 'groupDownloadMapIsr.do',
					data : dataUrl,
					dataType : 'json',
					cache : false,
					async : false,
					success : function(data) {
						// show success notification
						/*var serverResponseData = groupDownloadFunction(json);
						if (serverResponseData) {
							if (serverResponseData.handler == "success") {
								status = true;
							} else {
								status = false;
							}
						}*/ 
						if(data.status == 'Success'){
							status = true;
						}
						else {
							status = false;
							$.modal.alert(strings['msg.isr']);
						}
						displayGroupDownloadStatus(status);
						unblockUI();
					},
					error : function(data) {
						if (data.status == "200") {
							//jsonOutputData = data;
						} else {
							$.modal.alert(strings['msg.nff']);
						}
						unblockUI();
					}
				});
			}
		}
	}
}

/**
 * @author Joykumar Pal
 * Download GRF based on multi-select student and subtest
 */
function downloadGrf() {
	displayGroupDownloadStatus(undefined);
	if ($("#grfDownload").validationEngine('validate')) {
		blockUI();
		var status = false;
		var subtest = $("#p_subtest", window.parent.document); 
		var tab = $("li.active", window.parent.document).attr("param");
		tab = tab.replace(/new-tab/g, "report-form-");
		var formObj = $('.'+tab, window.parent.document);
		var dataUrl = $(formObj).serialize() + '&fileName=' + $("#fileName").val() + '&email=' + $("#email").val();
			
		$.ajax({
			type : "POST",
			url : 'grfDownload.do',
			data : dataUrl,
			dataType : 'json',
			cache : false,
			async : false,
			success : function(data) {
				if(data.status == 'Success'){
					status = true;
				}
				else {
					status = false;
					$.modal.alert(strings['msg.isr']);
				}
				displayGroupDownloadStatus(status);
				unblockUI();
			},
			error : function(data) {
				if (data.status == "200") {
				} else {
					$.modal.alert(strings['msg.nff']);
				}
				unblockUI();
			}
		});
	}
}


/**
 * Does not actually submit the form, but it feels alike.
 * 
 * @param button
 */
function groupDownloadSubmit(button) {
	displayGroupDownloadStatus(undefined);
	if ($("#groupDownload").validationEngine('validate')) {
		
		//Fix TD 77926 - By Joy
		window.parent.$('html, body').animate({scrollTop:0}, 'slow');
		
		$("#buttonGD").val(button);
		var status = false;
		var json = getGroupDownloadTO();
		if ((button == "SP") || (button == "CP") || (button == "SS")) {
			var errMsg = validateGroupDownloadForm(button, json);
			if (errMsg == "") {
				$.modal.confirm(strings['msg.duplexPrintConfirm'],
					function() {
						// Ajax Call
						var serverResponseData = groupDownloadFunction(json);
						// {
						// "handler": "success/failure",
						// "type": "sync/async",
						// "downloadFileName": "download-file-name",
						// "jobTrackingId": "job-tracking-id"
						// }
						if (serverResponseData) {
							if (serverResponseData.handler == "success") {
								status = true;
								if (serverResponseData.type == "sync") {
									// Synchronous : Immediate download
									// - only for Single
									// Student
									status = undefined;
									// var href =
									// "downloadSingleStudentPdf.do?fileName="
									// + json.fileName + "&email=" +
									// json.email;
									// Href Call
									// $("#downloadSinglePdfsGD").attr("href",
									// href);
								} else {
									// Asynchronous : No action needed
									// $("#downloadSinglePdfsGD").attr("href",
									// "#");
								}
							} else {
								status = false;
							}
							displayGroupDownloadStatus(status);
						} else {
							$.modal.alert(strings['msg.isr']);
						}
					}, function() {
						// this function closes the confirm modal on
						// clicking
						// cancel button
					}
				);
			} else {
				if (errMsg == strings['msg.selectStudent']) {
					// clearGDCache();
					// location.reload();
				}
				$.modal.alert(errMsg);
			}
		} else {
			$.modal.alert(strings['msg.urt']);
		}
	}
}

/**
 * Show or hide the status message divs.
 * 
 * @param status
 */
function displayGroupDownloadStatus(status){
	if (status == true) { // Success
		$(".success-message").show(200);
		$(".error-message").hide(200);
	} else if (status == false) { // Error
		$(".success-message").hide(200);
		$(".error-message").show(200);
	} else { // No Message
		$(".success-message").hide(200);
		$(".error-message").hide(200);
	}
}

/**
 * 
 * @param jsonInputData
 * @returns {"handler" : "success/failure", "type" ; "sync/async", "downloadFileName" : "file-name", "jobTrackingId" : "tracking-id"}
 */
function groupDownloadFunction(jsonInputData) {
	
	/*var button = $("#buttonGD").val();
	var testAdministrationVal = $("#q_testAdministrationVal").val();
	var testAdministrationText = $("#q_testAdministrationText").val();
	var testProgram = $("#q_testProgram").val();
	var district = $("#q_corpDiocese").val();
	var school = $("#q_school").val();
	var klass = $("#q_klass").val();
	var grade = $("#q_grade").val();
	var students = getSelectedStudentIdsAsCommaString();
	var groupFile = $("#q_groupFile").val();
	var collationHierarchy = $("#q_collationHierarchy").val();
	var fileName = $("#fileName").val();
	var email = $("#email").val();*/
	
	var dataUrl = $("#groupDownload").serialize()+'&json='+JSON.stringify(jsonInputData);
	/*dataUrl = dataUrl + '&button='+button+'&testAdministrationVal='+testAdministrationVal;
	dataUrl = dataUrl + '&button='+button+'&testAdministrationVal='+testAdministrationVal;
	dataUrl = dataUrl + '&testAdministrationText='+testAdministrationText+'&testProgram='+testProgram;
	dataUrl = dataUrl + '&district='+district+'&school='+school;
	dataUrl = dataUrl + '&klass='+klass+'&grade='+grade;
	dataUrl = dataUrl + '&students='+students+'&groupFile='+groupFile;
	dataUrl = dataUrl + '&collationHierarchy='+collationHierarchy+'&fileName='+fileName+'&email='+email;*/
	//alert(dataUrl);
	
	var jsonOutputData = "";
	blockUI();
	$.ajax({
		type : "POST",
		url : 'groupDownloadFunction.do',
		data : dataUrl,
		dataType : 'json',
		cache : false,
		async : false,
		success : function(data) {
			if (data) {
				jsonOutputData = data;
			} else {
				$.modal.alert(strings['msg.nff']);
			}
			unblockUI();
		},
		error : function(data) {
			if (data.status == "200") {
				jsonOutputData = data;
			} else {
				$.modal.alert(strings['msg.nff']);
			}
			unblockUI();
		}
	});
	return jsonOutputData;
}

function showHideDownloadButtons() {
	var groupFile = $("#q_groupFile").val();
	if (groupFile) {
		if (groupFile == "5") { // Student PDF's
			$("#downloadSeparatePdfsGD").hide();
			$("#downloadCombinedPdfsGD").hide();
			// $("#downloadSinglePdfsGD").hide(); // TODO : Delete button
			$("#nameMailDiv").hide();
		} else if ((groupFile == "1") || (groupFile == "2") || (groupFile == "3")) {
			$("#downloadSeparatePdfsGD").show();
			$("#downloadCombinedPdfsGD").show();
			// $("#downloadSinglePdfsGD").hide();
			$("#nameMailDiv").show();
		} else {
			$("#downloadSeparatePdfsGD").show();
			$("#downloadCombinedPdfsGD").show();
			// $("#downloadSinglePdfsGD").hide();
			$("#nameMailDiv").show();
		}
	}
}

/**
 * Form Validation for Group Download.
 * 
 * @param button
 * @param json
 * @returns {String}
 */
function validateGroupDownloadForm(button, json) {
	var errMsg = "";

	// File Name
	var fileName = $("#fileName").val();
	if (fileName) {
		if (fileName.length == 0) {
			errMsg = strings['msg.validFileName'];
			return errMsg;
		}
	} else {
		errMsg = strings['msg.validFileName'];
		return errMsg;
	}
	// Email
	/*var email = $("#email").val();
	var isValidEmail = validateEmail(email);
	if (isValidEmail == false) {
		errMsg = strings['msg.validEmail'];
		return errMsg;
	} else {
		if (email.length == 0) {
			errMsg = strings['msg.validEmail'];
			return errMsg;
		}
	}*/

	// Student
	var students = json.students;
	var studentIds = students.split(',');
	if (students.length == 0) {
		errMsg = strings['msg.selectStudent'];
		return errMsg;
	}

	// Button
	if (button == "SS") {
		if (studentIds.length > 1) {
			errMsg = strings['msg.oneStudent'];
			return errMsg;
		}
	}
	return errMsg;
}

/**
 * Email Validator.
 * 
 * @param $email
 * @returns {Boolean}
 */
function validateEmail($email) {
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	if (!emailReg.test($email)) {
		return false;
	} else {
		return true;
	}
}

//=============== Download Student Data File =====================
function downloadStudentDataFile(fileType) {
	var startDate = $("#p_Start_Date").val();
	var endDate = $("#p_End_Date").val();
	var dateType = $("#p_Date_Type").val();
	var href = "type=" + fileType + "&dateType=" + dateType + "&startDate=" + startDate + "&endDate=" + endDate;
	blockUI();
	$.ajax({
		type : "GET",
		url : "downloadStudentFile.do",
		data : href,
		dataType: 'html',
		cache:false,
		success : function(data) {
			unblockUI();
			var obj = jQuery.parseJSON(data);
			if (obj.status == 'Success') {
				$(".success-message").show(200);
			} else {
				$(".error-message").show(200);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.common.error']);
		}
	});
}
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
