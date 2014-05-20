/**
 * This js file is to manage Rescore Request - Start
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */
var ANIMATION_TIME = 200;

$(document).ready(function() {
	
	$("#studentTableRRF").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 1 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			//filteredRow = this.$('tr', {"filter": "applied"} );
			$(".item-link").on("click", function(){
				submitRescoreRequest($(this));
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

function submitRescoreRequest(obj){
	blockUI();
	
	var actionUrl = $(obj).attr('action');
	actionUrl = actionUrl + '.do';
	
	var itemsetId = (typeof $(obj).attr('itemsetId') !== 'undefined') ? $(obj).attr('itemsetId') : 0;
	var rrfId = (typeof $(obj).attr('rrfId') !== 'undefined') ? $(obj).attr('rrfId') : 0;
	var userId = (typeof $(obj).attr('userId') !== 'undefined') ? $(obj).attr('userId') : 0;
	var elementId = (typeof $(obj).attr('id') !== 'undefined') ? $(obj).attr('id') : 0;
	
	var requestedStatus = "Y";
	var isRequested = $('#'+elementId +' .item-tag').hasClass('red-bg');
	if(isRequested){
		requestedStatus = "N";
	}
	
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var requestedDate =  $('#rescoreDate_'+studentBioId).val();
	
	var urlData = 'itemsetId='+itemsetId
					+'&rrfId='+rrfId
					+'&userId='+userId
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
				//$.modal.alert("Request submitted successfully");
				if(requestedStatus == "Y"){
					$('#'+elementId +' .item-tag').addClass('red-bg');
				}else{
					$('#'+elementId +' .item-tag').removeClass('red-bg');
				}
			}else{
				$.modal.alert("Error occured");
			}
		},
		error : function(data) {	
			$.modal.alert("Error occured");
			unblockUI();
		}
	});
}

/**
 * This js file is to manage Rescore Request - End
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */