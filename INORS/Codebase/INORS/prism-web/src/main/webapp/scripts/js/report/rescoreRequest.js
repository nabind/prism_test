/**
 * This js file is to manage Rescore Request - Start
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */
var ANIMATION_TIME = 200;
var ERROR_MESSAGE = "Error Occured";
var DATE_VALIDATION_ERROR_MESSAGE = "Enter valid date";
var DATE_RANGE_ERROR_MESSAGE = "Entered date is greater than today's date.";
var oTable = "";
var oldDate = "";
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
			runDatePickerScripts($(".rescore-date-dnp"));
			$(".rescore-date-dnp").on("change", function(event) {
				activeInactiveItems('#studentTableRRF',$(this));
				$(".rescore-date-dnp").attr("style", "width: 70px; float: right; cursor:default;");
		    });
			$(".rescore-date-dnp").on("focus", function(event) {
				$(this).blur();
		    });
		}
	});
	$( "#studentTableRRF_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );
	
	oTable = $("#studentTableRRF_2").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			//'aTargets' : [0,2]
			'aTargets' : [1]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			
			$('.item-link').off().on('click', function(){
				submitRescoreRequest('#studentTableRRF_2',$(this));
			});
			
			$('.performance-level').off().on('click', function(){
				showHideItems('#studentTableRRF_2',$(this));
			});
			runDatePickerScripts($(".rescore-date"));
			$(".rescore-date").on("change", function(event) {
				activeInactiveItems('#studentTableRRF_2',$(this));
				$(".rescore-date").attr("style", "width: 70px; float: right; cursor:default;");
			});
			$(".rescore-date").on("focus", function(event) {
				$(this).blur();
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

function resetRequestedDate(obj,callback){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	var urlData = 'studentBioId='+studentBioId+'&requestedDate='+$(obj).val();
	var returnVal = 0;
	blockUI();
	$.ajax({
		type : 'GET',
		url : 'resetItemDate.do',
		data : urlData,
		dataType : 'json',
		async: false,
		cache: false,
		success : function(data) {
			unblockUI();
			if(data.value >= 1){
				returnVal = 1;
				if(typeof callback === "function"){
					callback(returnVal);
				}
			}else{
				//Transaction failure portion
				//$.modal.alert(ERROR_MESSAGE);
			}
		},
		error : function(data) {	
			//$.modal.alert(ERROR_MESSAGE);
			unblockUI();
		}
	});	
}

function activeInactiveItems(containerId,obj){
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	$(containerId+' .item-div-normal-'+studentBioId).hide();
	$(containerId+' .item-div-act-'+studentBioId).hide();
	$(containerId+' .item-div-inact-'+studentBioId).hide();
	var activeFlag = 0;
	var requestedDate =  $(obj).val();
	if(requestedDate.length > 0){
		if(isDate(requestedDate)){
	    	//$(obj).css('background-color','white');
	    	//$(obj).css('color','#666666');
			$(obj).closest("span").attr("class", "input");
	    	$(obj).val();
	    	if(oldDate != requestedDate) {
	    		resetRequestedDate(obj,function(returnVal){
					if(returnVal == 1){
						activeFlag = 1;
					}
				});
	    	}else{
	    		activeFlag = 1;
	    	}
	    	if(activeFlag == 1){
	    		$(containerId+' .item-div-act-'+studentBioId).show();
	    	}else{
	    		$(containerId+' .item-div-inact-'+studentBioId).show();
	    	}
	    }else{
	    	//$(obj).css('background-color','red');
	    	$(obj).val(DATE_VALIDATION_ERROR_MESSAGE);
	    	//$(obj).css('color','white');
	    	$(obj).closest("span").attr("class", "input red-bg");
	    	$(containerId+' .item-div-inact-'+studentBioId).show();
	    }
	}else{
		if(oldDate != requestedDate) {
			resetRequestedDate(obj,function(returnVal){
				if(returnVal == 1){
					//Commented the code as updation of date should not impact on is_requested state.
					//$(containerId+' .item-div-act-'+studentBioId).show();
					//$(containerId+' .item-div-act-'+studentBioId +' .item-tag').removeClass('red-bg');
					//$(containerId+' .item-div-act-'+studentBioId).hide();
				}else{
					//Transaction failure portion - no risk because the items will be inactivated irrespective of transaction state.
				}
			});
			//$(obj).css('background-color','white');
			//$(obj).css('color','#666666');
			$(obj).closest("span").attr("class", "input");
			$(containerId+' .item-div-inact-'+studentBioId).show();
		} else {
			$(containerId+' .item-div-inact-'+studentBioId).show();
		}
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
    	//$(containerId+' #rescoreDate_'+studentBioId).css('background-color','red');
    	$(containerId+' #rescoreDate_'+studentBioId).val(DATE_VALIDATION_ERROR_MESSAGE);
    	//$(containerId+' #rescoreDate_'+studentBioId).css('color','white');
    	$(containerId+' #rescoreDate_'+studentBioId).closest("span").attr("class", "input red-bg");
    }	
}

function showHideItems(containerId,obj){
	
	var subtestId = (typeof $(obj).attr('subtestId') !== 'undefined') ? $(obj).attr('subtestId') : 0;
	var studentBioId = (typeof $(obj).attr('studentBioId') !== 'undefined') ? $(obj).attr('studentBioId') : 0;
	
	if($(containerId+' .item-div-'+studentBioId+'-'+subtestId).is(':hidden')){
		$(containerId+' .item-div-'+studentBioId+'-'+subtestId).show();
	}else{
		$(containerId+' .item-div-'+studentBioId+'-'+subtestId).hide();
		var requestedDate =  $(containerId+' #rescoreDate_'+studentBioId).val();
		if(requestedDate == 'undefined' || requestedDate == null) {
			requestedDate = $('#rescoreDate_'+studentBioId).val();
		}
		//if((requestedDate.length > 0) && isDate(requestedDate)){
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
					}else{
						$.modal.alert(ERROR_MESSAGE);
					}
				},
				error : function(data) {	
					$.modal.alert(ERROR_MESSAGE);
					unblockUI();
				}
			});	
		//} 			
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
					parentRescoreDate ='<span class="input" style="width: 100px;"><input type="text" class="rescore-date input-unstyled" readonly="true"'
							+' studentBioId="'+data.studentBioId+'"' 
							+' id="rescoreDate_'+data.studentBioId+'"'
							+' value="" /></span>';
				}else{
					parentRescoreDate ='<span class="input" style="width: 100px;"><input type="text" class="rescore-date input-unstyled" readonly="true"'
						+' studentBioId="'+data.studentBioId+'"' 
						+' id="rescoreDate_'+data.studentBioId+'"'
						+' value="'+data.requestedDate+'" /></span>';
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
						if(jsonSubtestTO.performanceLevel != 'UND'){
							if(jsonSubtestTO.performanceLevel=='Pass' || jsonSubtestTO.performanceLevel=='Pass+'){
								
								var isVisible = 'N';
								$.each(jsonSessionList, function (innerSessionIndex,innerJsonSessionTO){
									var innerJsonItemList = innerJsonSessionTO.rescoreItemTOList;
									$.each(innerJsonItemList, function (innerItemIndex,innerJsonItemTO){
										if (innerJsonItemTO.isRequested=='Y'){
											isVisible = 'Y';
										}
									});
								});
								if(isVisible == 'Y'){
									jsonSession = '<div class="item-div-'+studentBioId+'-'+jsonSubtestTO.subtestId+'" >';
								}else{
									jsonSession = '<div class="item-div-'+studentBioId+'-'+jsonSubtestTO.subtestId+'" style="display: none;">';
								}
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
						}
					
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
			runDatePickerScripts($(".rescore-date"));
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

function runDatePickerScripts(e) {
	e.datepicker({
		showButtonPanel : true,
		beforeShow : function(input) {
			oldDate = $(input).val();
			if(oldDate == DATE_VALIDATION_ERROR_MESSAGE) {
				$.datepicker._clearDate(input);
			}
			setTimeout(function() {
				var buttonPane = $(input).datepicker("widget").find(".ui-datepicker-buttonpane");
				buttonPane.empty();
				var btn = $('<button class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" type="button" style="margin-left: 85px;">Clear</button>');
				btn.unbind("click").bind("click", function() {
					$.datepicker._clearDate(input);
				});
				btn.appendTo(buttonPane);
			}, 1);
		},
		onChangeMonthYear : function(year, month, instance) {
			/*oldDate = $(instance).val();
			if(oldDate == DATE_VALIDATION_ERROR_MESSAGE) {
				$.datepicker._clearDate(input);
			}*/
			setTimeout(function() {
				var buttonPane = $(instance).datepicker("widget").find(".ui-datepicker-buttonpane");
				buttonPane.empty();
				var btn = $('<button class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" type="button" style="margin-left: 85px;">Clear</button>');
				btn.unbind("click").bind("click", function() {
					$.datepicker._clearDate(instance.input);
				});
				btn.appendTo(buttonPane);
			}, 1);
		},
		showOn: "button",
		buttonImage: "themes/acsi/img/calendar.png",
		buttonImageOnly: true,
		buttonText: "Click here to select Parent-Rescore Date"
	});
	e.attr("style", "width: 70px; float: right; cursor:default;");
	$(".ui-datepicker-trigger").attr("style", "margin: 7px 10px 0px 0px; width: 16px; height: 16px; cursor: pointer;");
}

function isDate(txtDate){
    var currVal = txtDate;
    if(currVal == '')
        return false;
    
    //Declare Regex - Fix for TD 79122 - By Joy
    //var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; 
    var rxDatePattern = /^(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})$/;
    //var rxDatePattern = /^(\d{2})(\/)(\d{2})(\/)(\d{4})$/;
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
    
    // check if date is greater than current date
    var myDate = new Date(dtYear, dtMonth - 1, dtDay);
    var today = new Date();
    if (myDate > today) {
    	$.modal.alert(DATE_RANGE_ERROR_MESSAGE);
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