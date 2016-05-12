var oTable;

$(document).ready(function(){
	
	$(document).ajaxStart(function(){
        $("#wait").css("display", "block");
    });
    $(document).ajaxComplete(function(){
        $("#wait").css("display", "none");
    });
	
	$('#dateFrom').datepicker();
	$('#dateTo').datepicker();
	
	$('#searchScoreReview').on("click", function(){
		validateForm();
	});
	
	$("#coCheckErrorDialog").dialog({
		bgiframe: true, 
		autoOpen: false, 
		modal: true, 
		height: 100, 
		minWidth: 450, 
		closeOnEscape: true, 
		resizable: true
	});
	
	$.fn.dataTable.ext.errMode = 'none';
	
	$("#scoreResultTable").dataTable( {
		"bJQueryUI": true,
		"sPaginationType": "full_numbers",
	});
	
});

function getReviewInfo(studentBioId, subtestId, studentName, subtestName) {
	$("#errorLog").hide();
	var dataString = "studentBioId="+studentBioId+"&subtestId="+subtestId;
	oTable = $('#scoreReviewTable').DataTable({bJQueryUI : true});	
	oTable.destroy();
	
	$.ajax({
		type: "POST",
	    url: "getReviewResult.htm",
	    data: dataString,
	    success: function(data) {
	    	oTable = $('#scoreReviewTable').dataTable({
	    		bJQueryUI : true,
				bPaginate : false,
				bProcessing: true,
				bFilter : false,
				bInfo : false,
				bSort : false,
				fnDrawCallback: function () {
		        	dataTableCallBack();
		        }, 
		        fnRowCallback: function( nRow, aData) {
                    if ( aData.isActive == "N" ){
                        $('td', nRow).css('background-color', 'rgba(173, 172, 172, 0.7)');
                    }
                },
				data : data.data,
		        columns: [
		            { data: "form" },
		            { data: "ncr" },
		            { data: "ss" },
		            { data: "hse" },
		            { data: "date" },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV'){
		            			html = "<span>Review</span>";
		            		}else if(row.status == 'AP'){
	            				html = "<span>Approved</span>";
	            			}else if(row.status == 'RJ'){
	            				html = "<span>Rejected</span>";
	            			}else if(row.status == 'AE'){
	            				html = "<span>Approved with Error</span>";
	            			}else if(row.status == 'PR'){
	            				html = "<span>Processed</span>";
	            			}else if(row.status == 'IN'){
	            				html = "<span>Invalidated by System</span>";
	            			}
		            		
							return html;
						}
		            },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV' || row.status == 'AE'){
		            			html = "<input type='text' name='comment' class='rv_comment' value='"+row.comment+"'"
										+ " scr_id='"+row.scr+"'"
										+ " />";
		            		}else{
		            			html = "<span>"+row.comment+"</span>";
		            		}
		        			return html;
						}
		            },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV' || row.status == 'AE'){
		            			html = "<input type='radio' name='radioBtn' class='' "
									+ " scr_id='"+row.scr+"' opr_ss='"+row.opr_ss+"'"
									+ " opr_ncr='"+row.opr_ncr+"' opr_hse='"+row.opr_hse+"'"
									+ " ss='"+row.ss+"' hse='"+row.hse+"'"
									+ " ncr='"+row.ncr+"'"
									+ " />";
		            		}else{
		            			if(row.status == 'AP'){
		            				html = "<span>Approved</span>";
		            			}else if(row.status == 'RJ'){
		            				html = "<span>Rejected</span>";
		            			}else if(row.status == 'AE'){
		            				html = "<span>Approved with Error</span>";
		            			}else if(row.status == 'PR'){
		            				html = "<span>Processed</span>";
		            			}else if(row.status == 'IN'){
		            				html = "<span>Invalidated by System</span>";
		            			}
		            		}
							return html;
						}
		            }
		        ]
			});
	    	$('input:radio').checkbox();
	      },
	      error: function(data) {
			  alert('Failed to get Student Details');
		  }
	});
	
	var reviewDialog = jQuery("#reviewDialog").dialog({
		title: 'Review pending scores for Student: '+ studentName + ' Subtest: '+ subtestName,
		width: 900,
		height: "auto",
		draggable: false,
		  buttons: {
			  'Reset' : function() {
				  $('input:radio').checkbox().removeAttr('checked');
				  $(oTable.$('.rv_comment')).val("");
	          },
	          'Cancel' : function() {
	              $(this).dialog('close');
	          },
	          'Save': function(){
	        	  saveReviewScore(studentBioId,subtestId,function(){ 
	        		  reviewDialog.dialog('close'); 
	        	  });
	          }
			}
	});
}

function saveReviewScore(studentBioId,subtestId,callbackFunction){
	var message = "";
	
	var messageEmptyFlag = false;
	$(oTable.$('.rv_comment')).each(function (index, value){
		var tempComment = $(this).val();
		tempComment = tempComment.trim();
		if(tempComment.length == 0){
			messageEmptyFlag = true;
		}
	});
	
	var scoreFlag = false;
	$(oTable.$('input:radio')).each(function (index, value){
		if($(this).is(":checked")){
			var ss = $(this).attr('ss');
			var ncr = $(this).attr('ncr');
			var hse = $(this).attr('hse');
			var opr_ss = $(this).attr('opr_ss');
			var opr_ncr = $(this).attr('opr_ncr');
			var opr_hse = $(this).attr('opr_hse');
			if(opr_ss>ss || opr_ncr>ncr || opr_hse>hse ){
				scoreFlag = true;
			}
		}
	});
	
	
	if(messageEmptyFlag == true){
		message = "Please enter comment";
		$("#errorLog").show();
    	$("#errorLog").css("color","red");
    	$("#errorLog").text(message);
	}else if(scoreFlag == true){
		message = "The selected score(s) are lower than other available scores.";
		$.confirm({
			title: 'The selected score(s) are lower than other available scores.!',
		    content: 'Do you want to continue?',
		    confirm: function(){
		        $.alert('Confirmed!');
		    },
		    cancel: function(){
		        $.alert('Canceled!');
		    }
		});
	}
	
	
	
	else{
		var commentStr = ""; 
		$(oTable.$('input:text')).each(function (index, value){
			var tempStr = $(this).attr('scr_id')+'~'+$(this).val();
			commentStr = commentStr+','+tempStr;
		}); 
		commentStr = commentStr.substring(1);
		
		var statusStr = ""; 
		$(oTable.$('input:radio')).each(function (index, value){
			var tempStr = $(this).attr('scr_id')+'~';
			if($(this).is(":checked")){
				tempStr = tempStr + 'AP';
			}else{
				tempStr = tempStr + 'RJ';
			}
			statusStr = statusStr+','+tempStr;
		}); 
		statusStr = statusStr.substring(1);
		
		var dataString = "studentBioId="+studentBioId+"&subtestId="+subtestId
							+"&commentStr="+commentStr+"&statusStr="+statusStr;
		$.ajax({
			type: "POST",
		    url: "saveReviewScore.htm",
		    data: dataString,
		    success: function(data) {
		    	message = data;
		    	alert(message);
		    	callbackFunction();
		    },
		    error: function(data) {
		    	message = 'Failed to save Scoring Data';
		    	$("#errorLog").show();
		    	$("#errorLog").css("color","red");
		    	$("#errorLog").text(message);
			}
		});
	}
}

function dataTableCallBack(){
	update_rows();
}

function update_rows(){
    $(".process_details tr:even").css("background-color", "#fff");
    $(".process_details tr:odd").css("background-color", "#eee");
}

function validateForm(){
	$('#scoreReviewForm').submit();
}



