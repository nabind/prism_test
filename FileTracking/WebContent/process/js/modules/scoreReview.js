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
	var message = "";
	var dataString = "studentBioId="+studentBioId+"&subtestId="+subtestId;
	oTable = $('#scoreReviewTable').DataTable({bJQueryUI : true});	
	oTable.destroy();
	jQuery("#reviewDialog").dialog({
		title: 'Review pending scores for Student: '+ studentName + ' Subtest: '+ subtestName,
		width: 900,
		height: "auto",
		draggable: false,
		  buttons: {
			  'Reset' : function() {
	              //$(this).dialog('close');
				  $('input:radio').checkbox();
	          },
	          'Cancel' : function() {
	              $(this).dialog('close');
	          },
	          'Save': function(){
	        	  message = saveReviewScore(studentBioId,subtestId);
	          }
			}
	});
	
	$.ajax({
		type: "POST",
	    url: "getReviewResult.htm",
	    data: dataString,
	    success: function(data) {
	    	oTable = $('#scoreReviewTable').dataTable({
	    	//$('#scoreReviewTable').dataTable({
				bJQueryUI : true,
				bPaginate : false,
				bProcessing: true,
				bFilter : false,
				bInfo : false,
				bSort : false,
				fnDrawCallback: function () {
		        	dataTableCallBack();
		        }, 
				data : data.data,
		        columns: [
		            { data: "form" },
		            { data: "nc" },
		            { data: "ss" },
		            { data: "hse" },
		            { data: "date" },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV'){
		            			html = "<input type='text' name='comment' class='rv_comment' value='"+row.comment+"'"
										+ " scr_id='"+row.scr+"'"
										+ " />";
		            		}else{
		            			html = "<input type='text' name='comment' class='' value='"+row.comment+"'"
				            			+ " scr_id='"+row.scr+"'"
										+ " readonly/>";
		            		}
		        			return html;
						}
		            },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV'){
		            			html = "<input type='radio' name='radioBtn' class='' "
									+ " scr_id='"+row.scr+"'"
									+ "/>";
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
}

function saveReviewScore(studentBioId,subtestId){
	var message = "";
	
	var messageEmptyFlag = false;
	$(oTable.$('.rv_comment')).each(function (index, value){
		var tempComment = $(this).val();
		tempComment = tempComment.trim();
		if(tempComment.length == 0){
			messageEmptyFlag = true;
		}
	});
	if(messageEmptyFlag == true){
		message = "Please enter comment";
		$("#errorLog").show();
    	$("#errorLog").css("color","red");
    	$("#errorLog").text(message);
	}else{
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
		    	$("#errorLog").show();
		    	$("#errorLog").css("color","green");
		    	$("#errorLog").text(message);
		    },
		    error: function(data) {
		    	//alert('Failed to save Scoring Data');
		    	message = 'Failed to save Scoring Data';
		    	$("#errorLog").show();
		    	$("#errorLog").css("color","red");
		    	$("#errorLog").text(message);
			}
		});
	}
	return message;
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



