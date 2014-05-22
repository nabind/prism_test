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
			alert('disabled');
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
			disableLinks();
			//filteredRow = this.$('tr', {"filter": "applied"} );
			$('.item-link').on('click', function(){
				submitRescoreRequest($(this));
			});
			$('.performance-level').on('click', function(){
				showHideItems($(this));
			});
			$('.rescore-date').focusout(function(){
				activeInactiveItems($(this));
		   });
		}
	});
	$( "#studentTableRRF_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );
	
	/*var filteredRow_2;
	$("#studentTableRRF_2").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 0, 3, 4, 6 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			filteredRow_2 = this.$('tr', {"filter": "applied"} );
		}
	});
	$( "#studentTableRRF_2_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_2_filter > label" ).css( "cursor", "default" );*/
	
	
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

function activeInactiveItems(obj){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	$('.item-div-normal-'+studentBioId).hide();
	$('.item-div-act-'+studentBioId).hide();
	$('.item-div-inact-'+studentBioId).hide();
	
	var requestedDate =  $(obj).val();
	if(requestedDate.length > 0){
		if(isDate(requestedDate)){
	    	$(obj).css('background-color','white');
	    	$(obj).css('color','black');
	    	$(obj).val();
	    	$('.item-div-act-'+studentBioId).show();
	    }else{
	    	$(obj).css('background-color','red');
	    	$(obj).val(DATE_VALIDATION_ERROR_MESSAGE);
	    	$(obj).css('color','white');
	    	$('.item-div-normal-'+studentBioId).show();
	    }
	}else{
		$(obj).css('background-color','white');
		$(obj).css('color','black');
		$('.item-div-inact-'+studentBioId).show();
	}
}

function submitRescoreRequest(obj){
	var itemsetId = (typeof $(obj).attr('itemsetId') !== 'undefined') ? $(obj).attr('itemsetId') : 0;
	var rrfId = (typeof $(obj).attr('rrfId') !== 'undefined') ? $(obj).attr('rrfId') : 0;
	var elementId = (typeof $(obj).attr('id') !== 'undefined') ? $(obj).attr('id') : 0;
	
	var requestedStatus = "Y";
	var isRequested = $('#'+elementId +' .item-tag').hasClass('red-bg');
	if(isRequested){
		requestedStatus = "N";
	}
	
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var requestedDate =  $('#rescoreDate_'+studentBioId).val();
	
	if((requestedDate.length > 0) && isDate(requestedDate)){
		blockUI();
		var actionUrl = $(obj).attr('action');
		actionUrl = actionUrl + '.do';
		var urlData = 'itemsetId='+itemsetId
						+'&rrfId='+rrfId
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
					// $.modal.alert("Request submitted successfully");
					if(requestedStatus == "Y"){
						$('#'+elementId +' .item-tag').addClass('red-bg');
					}else{
						$('#'+elementId +' .item-tag').removeClass('red-bg');
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
    	$('#rescoreDate_'+studentBioId).css('background-color','red');
    	$('#rescoreDate_'+studentBioId).val(DATE_VALIDATION_ERROR_MESSAGE);
    	$('#rescoreDate_'+studentBioId).css('color','white');
    }	
}

function showHideItems(obj){
	var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	
	if($('.item-div-'+subtestId).is(':hidden')){
		$('.item-div-'+subtestId).show();
	}else{
		$('.item-div-'+subtestId).hide();
		var requestedDate =  $('#rescoreDate_'+studentBioId).val();
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
						$('.item-div-'+subtestId+' .item-tag').removeClass('red-bg');
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