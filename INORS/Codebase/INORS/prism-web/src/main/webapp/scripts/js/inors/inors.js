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
	
	
	$(".accordion-header").on("click", function() {
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
		var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=XML&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileXML").attr("href", href);
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
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		groupDownloadSubmit('SP');
	});
	// Asynchronous : Submit to Group Download Files
	$("#downloadCombinedPdfsGD").on("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		groupDownloadSubmit('CP');
	});
	// Synchronous : Immediate download
	// $("#downloadSinglePdfsGD").on("click", function() {
	// groupDownloadSubmit('SS');
	//	});
	

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