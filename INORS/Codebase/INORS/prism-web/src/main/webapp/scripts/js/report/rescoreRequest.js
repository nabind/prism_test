/**
 * This js file is to manage Rescore Request - Start
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */
var ANIMATION_TIME = 200;
var ERROR_MESSAGE = "Error Occured";
var DATE_VALIDATION_ERROR_MESSAGE = "Enter valid date";

$(document).ready(function() {
	
	$("#reviewRRF").live("click", function(event) {
		var disableLinks = $('#q_dataloadMessage').val();
		if(disableLinks == 'Y') {
		} else if(disableLinks == 'N') {
			$.modal({
				title: 'Modal window',
				content: $("#sorting-advanced_wrapper_").html(),
				height: 500
			});
		} else {
		}
	});
	
	$("#studentTableRRF").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 1 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			// disableLinks();
			//filteredRow = this.$('tr', {"filter": "applied"} );
			$('.item-link-dnp').on('click', function(){
				submitRescoreRequest('#studentTableRRF',$(this));
			});
			$('.performance-level-dnp').on('click', function(){
				showHideItems('#studentTableRRF',$(this));
			});
			$('.rescore-date-dnp').focusout(function(){
				activeInactiveItems('#studentTableRRF',$(this));
		   });
		}
	});
	$( "#studentTableRRF_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );
	
	var filteredRow_2;
	$("#studentTableRRF_2").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [0, 1 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			//filteredRow_2 = this.$('tr', {"filter": "applied"} );
			$('.item-link').on('click', function(){
				submitRescoreRequest('#studentTableRRF_2',$(this));
			});
			$('.performance-level').on('click', function(){
				showHideItems('#studentTableRRF_2',$(this));
			});
			$('.rescore-date').focusout(function(){
				activeInactiveItems('#studentTableRRF_2',$(this));
			});
			$('.remove-student').on('click', function(){
				removeStudent($(this));
			});
		}
	});
	$( "#studentTableRRF_2_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_2_filter > label" ).css( "cursor", "default" );
	
	$('#addStudent').on('click', function(){
		addStudent();
	});
});
//=====document.ready End=========================================
function disableLinks() {
	var disableLinks = $('#q_dataloadMessage').val();
	if(disableLinks == 'Y') {
		disableElement($('#studentListRRF a'));
		disableElement($('#reviewRRF'));
	} else if(disableLinks == 'N') {
	} else {
	}
}

function disableElement(e) {
	e.removeAttr("href");
	e.addClass('disabled');
	e.addClass('silver-bg');
}

function getReviewFormContent() {
	var reviewFormContent;
	var testAdministrationVal = $("#q_testAdministrationVal").val();
	var testProgram = $("#q_testProgram").val();
	var corpDiocese = $("#q_corpDiocese").val();
	var school = $("#q_school").val();
	var grade = $("#q_grade").val();
	var reportUrl = $("#reportUrl").val();
	var requestUrl = 'rescoreRequestReviewData.do?testAdministrationVal=' + testAdministrationVal;
	requestUrl = requestUrl + "&testProgram=" + testProgram;
	requestUrl = requestUrl + "&corpDiocese=" + corpDiocese;
	requestUrl = requestUrl + "&school=" + school;
	requestUrl = requestUrl + "&grade=" + grade;
	requestUrl = requestUrl + "&reportUrl=" + reportUrl;
	alert(requestUrl);
	blockUI();
	$.ajax({
		type : 'GET',
		url : requestUrl,
		//data : urlData,
		dataType : 'json',
		cache: false,
		success : function(data) {
			alert(data);
			alert(JSON.stringify(data));
			unblockUI();
			/*if(data.value >= 1){
				$(containerId+' .item-div-'+subtestId+' .item-tag').removeClass('red-bg');
			}else{
				$.modal.alert(ERROR_MESSAGE);
			}*/
			reviewFormContent = data;
		},
		error : function(data) {	
			$.modal.alert(ERROR_MESSAGE);
			unblockUI();
		}
	});
	return reviewFormContent;
}

function activeInactiveItems(containerId,obj){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	$(containerId+' .item-div-normal-'+studentBioId).hide();
	$(containerId+' .item-div-act-'+studentBioId).hide();
	$(containerId+' .item-div-inact-'+studentBioId).hide();
	
	var requestedDate =  $(obj).val();
	if(requestedDate.length > 0){
		if(isDate(requestedDate)){
	    	$(obj).css('background-color','white');
	    	$(obj).css('color','black');
	    	$(obj).val();
	    	$(containerId+' .item-div-act-'+studentBioId).show();
	    }else{
	    	$(obj).css('background-color','red');
	    	$(obj).val(DATE_VALIDATION_ERROR_MESSAGE);
	    	$(obj).css('color','white');
	    	$(containerId+' .item-div-normal-'+studentBioId).show();
	    }
	}else{
		$(obj).css('background-color','white');
		$(obj).css('color','black');
		$(containerId+' .item-div-inact-'+studentBioId).show();
	}
}

function submitRescoreRequest(containerId,obj){
	var itemNumber = (typeof $(obj).attr('itemNumber') !== 'undefined') ? $(obj).attr('itemNumber') : 0;
	var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
	var sessionId = (typeof $(obj).attr('sessionId') !== 'undefined') ? $(obj).attr('sessionId') : 0;
	var moduleId = (typeof $(obj).attr('moduleId') !== 'undefined') ? $(obj).attr('moduleId') : 0;
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var elementId = (typeof $(obj).attr('id') !== 'undefined') ? $(obj).attr('id') : 0;
	
	var requestedStatus = "Y";
	var isRequested = $(containerId+' #'+elementId +' .item-tag').hasClass('red-bg');
	if(isRequested){
		requestedStatus = "N";
	}
	
	var requestedDate =  $(containerId+' #rescoreDate_'+studentBioId).val();
	
	if((requestedDate.length > 0) && isDate(requestedDate)){
		blockUI();
		var actionUrl = $(obj).attr('action');
		actionUrl = actionUrl + '.do';
		var urlData = 'itemNumber='+itemNumber
						+'&subtestId='+subtestId
						+'&sessionId='+sessionId
						+'&moduleId='+moduleId
						+'&studentBioId='+studentBioId
						+'&requestedStatus='+requestedStatus
						+'&requestedDate='+requestedDate;
	
		$.ajax({
			type : "GET",
			url : actionUrl,
			data : urlData,
			dataType : 'json',
			cache: false,
			success : function(data) {
				unblockUI();
				if(data.value >= 1){
					if(requestedStatus == "Y"){
						$(containerId+' #'+elementId +' .item-tag').addClass('red-bg');
					}else{
						$(containerId+' #'+elementId +' .item-tag').removeClass('red-bg');
					}
				}else{
					$.modal.alert(ERROR_MESSAGE);
				}
			},
			error : function(data) {	
			$.modal.alert(ERROR_MESSAGE);
			unblockUI();
			}
		});
    }else{
    	$(containerId+' #rescoreDate_'+studentBioId).css('background-color','red');
    	$(containerId+' #rescoreDate_'+studentBioId).val(DATE_VALIDATION_ERROR_MESSAGE);
    	$(containerId+' #rescoreDate_'+studentBioId).css('color','white');
    }	
}

function showHideItems(containerId,obj){
	
	var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	
	if($(containerId+' .item-div-'+subtestId).is(':hidden')){
		$(containerId+' .item-div-'+subtestId).show();
	}else{
		$(containerId+' .item-div-'+subtestId).hide();
		var requestedDate =  $(containerId+' #rescoreDate_'+studentBioId).val();
		if((requestedDate.length > 0) && isDate(requestedDate)){
			var urlData = 'subtestId='+subtestId
							+'&studentBioId='+studentBioId;
			blockUI();
			$.ajax({
				type : 'GET',
				url : 'resetItemState.do',
				data : urlData,
				dataType : 'json',
				cache: false,
				success : function(data) {
					unblockUI();
					if(data.value >= 1){
						$(containerId+' .item-div-'+subtestId+' .item-tag').removeClass('red-bg');
					}else{
						$.modal.alert(ERROR_MESSAGE);
					}
				},
				error : function(data) {	
					$.modal.alert(ERROR_MESSAGE);
					unblockUI();
				}
			});	
		}				
	}
}

function removeStudent(obj){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var studentFullName = (typeof $(obj).attr('studentFullName') !== 'undefined') ? $(obj).attr('studentFullName') : 0;
	$('#studentTableRRF_2 #row_'+studentBioId).hide();
	$('#addStudent').removeClass('disabled');
	var option = $('#selectStudentRRF').html();
	option += "<option selected value='"+studentBioId+"'>"+studentFullName+"</option>";
	$('#selectStudentRRF').html(option);
	$('#selectStudentRRF').change();
	$('#selectStudentRRF').trigger('update-select-list');
}

function addStudent(){
	var studentBioId = $('#selectStudentRRF').val();
	$('#studentTableRRF_2 #row_'+studentBioId).show();
}

function isDate(txtDate){
    var currVal = txtDate;
    if(currVal == '')
        return false;
    
    var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; //Declare Regex
    var dtArray = currVal.match(rxDatePattern); // is format OK?
    
    if (dtArray == null) 
        return false;
    
    //Checks for mm/dd/yyyy format.
    dtMonth = dtArray[1];
    dtDay= dtArray[3];
    dtYear = dtArray[5];        
    
    if (dtMonth < 1 || dtMonth > 12) 
        return false;
    else if (dtDay < 1 || dtDay> 31) 
        return false;
    else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) 
        return false;
    else if (dtMonth == 2) 
    {
        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
        if (dtDay> 29 || (dtDay ==29 && !isleap)) 
                return false;
    }
    return true;
}

/**
 * This js file is to manage Rescore Request - End
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */