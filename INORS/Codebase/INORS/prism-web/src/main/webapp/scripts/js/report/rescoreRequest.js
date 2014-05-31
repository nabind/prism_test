/**
 * This js file is to manage Rescore Request - Start
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */
var ANIMATION_TIME = 200;
var ERROR_MESSAGE = "Error Occured";
var DATE_VALIDATION_ERROR_MESSAGE = "Enter valid date";
var oTable = "";

$(document).ready(function() {
	
	$("#studentTableRRF").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [1]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			$('.item-link-dnp').off().on('click', function(){
				submitRescoreRequest('#studentTableRRF',$(this));
			});
			$('.performance-level-dnp').off().on('click', function(){
				showHideItems('#studentTableRRF',$(this));
			});
			$('.rescore-date-dnp').off().focusout(function(){
				activeInactiveItems('#studentTableRRF',$(this));
				resetRequestedDate($(this));
		   });
		}
	});
	$( "#studentTableRRF_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );
	
	oTable = $("#studentTableRRF_2").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [0,2]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			
			$('.item-link').off().on('click', function(){
				submitRescoreRequest('#studentTableRRF_2',$(this));
			});
			
			$('.performance-level').off().on('click', function(){
				showHideItems('#studentTableRRF_2',$(this));
			});
			
			$('.rescore-date').off().focusout(function(){
				activeInactiveItems('#studentTableRRF_2',$(this));
				resetRequestedDate($(this));
			});
			
			$('.remove-student').off().on('click', function(){
				removeStudent($(this));
			});
		}
	});
	$( "#studentTableRRF_2_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_2_filter > label" ).css( "cursor", "default" );
	
	$('.addStudent').live('click', function(){
		addStudent();
	});
});
//=====document.ready End=========================================

function resetRequestedDate(obj){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var urlData = 'studentBioId='+studentBioId+'&requestedDate='+$(obj).val();
	
	blockUI();
	$.ajax({
		type : 'GET',
		url : 'resetItemDate.do',
		data : urlData,
		dataType : 'json',
		cache: false,
		success : function(data) {
			unblockUI();
			if(data.value >= 1){
				
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
	    	$(containerId+' .item-div-inact-'+studentBioId).show();
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
	
	$('.item-div-'+studentBioId+'-'+subtestId).toggle();
	if($(containerId+' .item-div-'+studentBioId+'-'+subtestId).is(':hidden')){
		//$(containerId+' .item-div-'+studentBioId+'-'+subtestId).show();
	}else{
		//$(containerId+' .item-div-'+studentBioId+'-'+subtestId).hide();
		var requestedDate =  $(containerId+' #rescoreDate_'+studentBioId).val();
		if(requestedDate == 'undefined' || requestedDate == null) {
			requestedDate = $('#rescoreDate_'+studentBioId).val();
		}
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
						$(containerId+' .item-div-'+studentBioId+'-'+subtestId+' .item-tag').removeClass('red-bg');
						//$('.item-div-'+studentBioId+'-'+subtestId).toggle();
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
	var target_row = $(obj).closest("tr").get(0);
    var aPos = oTable.fnGetPosition(target_row); 
    oTable.fnDeleteRow(aPos);
	
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var studentFullName = (typeof $(obj).attr('studentFullName') !== 'undefined') ? $(obj).attr('studentFullName') : 0;
	$('#addStudent').removeClass('disabled');
	$('#addStudent').addClass('addStudent');
	var option = $('#selectStudentRRF').html();
	option += "<option selected value='"+studentBioId+"'>"+studentFullName+"</option>";
	$('#selectStudentRRF').html(option);
	$('#selectStudentRRF').change();
	$('#selectStudentRRF').trigger('update-select-list');
}

function addStudent(){
	var studentBioId = $('#selectStudentRRF').val();
	var grade = $('#q_grade').val();
	var urlData = 'studentBioId='+studentBioId
					+'&grade='+grade;;
	blockUI();
	$.ajax({
		type : 'GET',
		url : 'addStudent.do',
		data : urlData,
		dataType : 'json',
		cache: false,
		success : function(data) {
			unblockUI();
			if(data != null){
				var removeButton = '<a class="button blue-gradient glossy float-left margin-left remove-student"'
							+' studentBioId="'+data.studentBioId+'"'
							+' studentFullName="'+data.studentFullName+'"'
							+' href="#nogo">Remove</a>';
				
				var student = data.studentFullName;
				
				var parentRescoreDate = '';
				if(data.requestedDate =='-1'){
					parentRescoreDate ='<input type="text" class="rescore-date"'
							+' studentBioId="'+data.studentBioId+'"' 
							+' id="rescoreDate_'+data.studentBioId+'"'
							+' value="" />';
				}else{
					parentRescoreDate ='<input type="text" class="rescore-date"'
						+' studentBioId="'+data.studentBioId+'"' 
						+' id="rescoreDate_'+data.studentBioId+'"'
						+' value="'+data.requestedDate+'" />';
				}
				
				var jsonSubtestList = data.rescoreSubtestTOList;
				var ela = '';
				var math = '';
				var scOrss = '';
				var elaSession2 = '';
				var elaSession3 = '';
				var mathSession1 = '';
				var scOrssSession4 = '';
				$.each(jsonSubtestList, function (subtestIndex,jsonSubtestTO){
					var jsonSubtest = '';
					if(jsonSubtestTO.performanceLevel=='Pass' || jsonSubtestTO.performanceLevel=='Pass+'){
						jsonSubtest = '<a class="performance-level"'
								+' subtestId="'+jsonSubtestTO.subtestId+'"'
								+' studentBioId="'+studentBioId+'"'
								+' href="#nogo">'+jsonSubtestTO.performanceLevel+'</a>';
					}else{
						jsonSubtest = jsonSubtestTO.performanceLevel;
					}
					
					var jsonSessionList = jsonSubtestTO.rescoreSessionTOList;
					$.each(jsonSessionList, function (sessionIndex,jsonSessionTO){
						//item-div start
						var jsonSession = '';
						if(jsonSubtestTO.performanceLevel=='Pass' || jsonSubtestTO.performanceLevel=='Pass+'){
							jsonSession = '<div class="item-div-'+studentBioId+'-'+jsonSubtestTO.subtestId+'" style="display: none;">';
						}else if(this.performanceLevel=='DNP'){
							jsonSession = '<div class="item-div-'+studentBioId+'-'+jsonSubtestTO.subtestId+'" >';
						}
						
						var jsonItemList = jsonSessionTO.rescoreItemTOList;
						$.each(jsonItemList, function (itemIndex,jsonItemTO){
							jsonSession += '<div class="item-div-normal-'+studentBioId+'">';
							if(jsonItemTO.requestedDate == '-1'){
								jsonSession += '<small class="item-tag tag align-row grey-bg">'+jsonItemTO.itemNumber+'</small>';
							}else{
								var itemLink = '';
								if(jsonItemTO.isRequested=='N'){
									itemLink = '<a class="item-link align-row"'
										+' action="submitRescoreRequest" itemNumber="'+jsonItemTO.itemNumber+'"'
										+' subtestId="'+jsonSubtestTO.subtestId+'"'
										+' sessionId="'+jsonSessionTO.sessionId+'"'
										+' moduleId="'+jsonSessionTO.moduleId+'"'
										+' studentBioId="'+studentBioId+'"'
										+' id="item_'+studentBioId+'_'+jsonSubtestTO.subtestId+'_'+jsonSessionTO.sessionId+'_'+jsonItemTO.itemNumber+'"'
										+' href="#nogo"> <small class="item-tag tag">'+jsonItemTO.itemNumber+'</small> </a>';
								}else if (jsonItemTO.isRequested=='Y'){
									itemLink = '<a class="item-link align-row"'
										+' action="submitRescoreRequest" itemNumber="'+jsonItemTO.itemNumber+'"'
										+' subtestId="'+jsonSubtestTO.subtestId+'"'
										+' sessionId="'+jsonSessionTO.sessionId+'"'
										+' moduleId="'+jsonSessionTO.moduleId+'"'
										+' studentBioId="'+studentBioId+'"'
										+' id="item_'+studentBioId+'_'+jsonSubtestTO.subtestId+'_'+jsonSessionTO.sessionId+'_'+jsonItemTO.itemNumber+'"'
										+' href="#nogo"> <small class="item-tag tag red-bg">'+jsonItemTO.itemNumber+'</small> </a>';
								}
								jsonSession += itemLink;
							}
							jsonSession += '</div>';
							
							jsonSession += '<div class="item-div-act-'+studentBioId+'" style="display: none;">';
							var itemLink = '';
							if(jsonItemTO.isRequested=='N'){
								itemLink = '<a class="item-link align-row"'
									+' action="submitRescoreRequest" itemNumber="'+jsonItemTO.itemNumber+'"'
									+' subtestId="'+jsonSubtestTO.subtestId+'"'
									+' sessionId="'+jsonSessionTO.sessionId+'"'
									+' moduleId="'+jsonSessionTO.moduleId+'"'
									+' studentBioId="'+studentBioId+'"'
									+' id="item_'+studentBioId+'_'+jsonSubtestTO.subtestId+'_'+jsonSessionTO.sessionId+'_'+jsonItemTO.itemNumber+'"'
									+' href="#nogo"> <small class="item-tag tag">'+jsonItemTO.itemNumber+'</small> </a>';
							}else if (jsonItemTO.isRequested=='Y'){
								itemLink = '<a class="item-link align-row"'
									+' action="submitRescoreRequest" itemNumber="'+jsonItemTO.itemNumber+'"'
									+' subtestId="'+jsonSubtestTO.subtestId+'"'
									+' sessionId="'+jsonSessionTO.sessionId+'"'
									+' moduleId="'+jsonSessionTO.moduleId+'"'
									+' studentBioId="'+studentBioId+'"'
									+' id="item_'+studentBioId+'_'+jsonSubtestTO.subtestId+'_'+jsonSessionTO.sessionId+'_'+jsonItemTO.itemNumber+'"'
									+' href="#nogo"> <small class="item-tag tag red-bg">'+jsonItemTO.itemNumber+'</small> </a>';
							}
							jsonSession += itemLink;
							jsonSession += '</div>';
							
							jsonSession += '<div class="item-div-inact-'+studentBioId+'" style="display: none;">';
							if(jsonItemTO.itemNumber != 0){
								jsonSession += 		'<small class="item-tag tag align-row grey-bg">'+jsonItemTO.itemNumber+'</small>'
							}
							jsonSession += '</div>';
						});
						jsonSession += '</div>';
						//item-div end
						
						if(jsonSessionTO.sessionId == '2'){
							elaSession2 = jsonSession;
						}else if(jsonSessionTO.sessionId == '3'){
							elaSession3 = jsonSession;
						}else if(jsonSessionTO.sessionId == '1'){
							mathSession1 = jsonSession;
						}else if(jsonSessionTO.sessionId == '4'){
							scOrssSession4 = jsonSession;
						}
						
					});
					
					
					if(subtestIndex==0){
						ela = jsonSubtest;
					}else if(subtestIndex==1){
						math = jsonSubtest;
					}else if(subtestIndex==2){
						scOrss = jsonSubtest;
					}
				});
				
				if(grade == 10001 || grade == 10006){
					oTable.fnAddData( [
					                   /*removeButton,*/
					                   student,
					                   parentRescoreDate,
					                   ela,
					                   elaSession2,
					                   elaSession3,
					                   math,
					                   mathSession1 ] );
				}else{
					oTable.fnAddData( [
					                   /*removeButton,*/
					                   student,
					                   parentRescoreDate,
					                   ela,
					                   elaSession2,
					                   elaSession3,
					                   math,
					                   mathSession1,
					                   scOrss,
					                   scOrssSession4] );
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
	
	$("#selectStudentRRF option[value='"+studentBioId+"']").each(function() {
	    $(this).remove();
	});
	
	//Fix for TD 79220 - By Joy
	var option = $.trim($('#selectStudentRRF').val());
	if(option == ""){
		$('#addStudent').addClass('disabled');
		$('#addStudent').removeClass('addStudent');
	}
	$('#selectStudentRRF').change();
	$('#selectStudentRRF').trigger('update-select-list');
}


function isDate(txtDate){
    var currVal = txtDate;
    if(currVal == '')
        return false;
    
    //Declare Regex - Fix for TD 79122 - By Joy
    //var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; 
    var rxDatePattern = /^(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})$/;
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