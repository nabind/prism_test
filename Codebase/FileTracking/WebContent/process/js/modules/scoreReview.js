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
	var reviewDialog;
	var bCount =0;
	
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
		        	$("input:radio[class=accepted]").click(function(){
		        		var selectedBcount = $(this).attr('bCount');
		        		var notSelectedId = 'radioBtnRJ'+selectedBcount;
		        		
		        		$('input[type="radio"]:not(:checked)').each(function (index, value){
		        			if($(this).attr('id')!=notSelectedId){
		        				if($(this).attr('class')=='reject'){
		        					$(this).prop("checked", true);
		        				}
		        			}
		        		});
		        		
		        	}); 
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
		            			html = "<input type='text' maxlength='100' name='comment' class='rv_comment' value='"+row.comment+"'"
										+ " scr_id='"+row.scr+"'"
										+ " />";		            			
		            		}else{
		            			html = "<span class='table-span'>"+row.comment+"</span>";		            			
		            		}
		        			return html;
						}
		            },
		            { 
		            	"mRender": function ( data, type, row ) {
		            		var html = "";
		            		if(row.status == 'RV' || row.status == 'AE'){
		            			html = "<div calss= 'row' >" 
		            				+ " <div class= 'button-group' id='"+ bCount +"'>"
		            				//+ " <label class='btn btn-default'>"
		            				+ " <input data-size='mini' type='radio' bCount="+bCount+"  name='radioBtn"+ bCount + "'" +"id='radioBtnAP"+ bCount + "'"+" value='Approved' class='accepted' "
									+ " scr_id='"+row.scr+"' opr_ss='"+row.opr_ss+"'"
									+ " opr_ncr='"+row.opr_ncr+"' opr_hse='"+row.opr_hse+"'"
									+ " ss='"+row.ss+"' hse='"+row.hse+"'"
									+ " ncr='"+row.ncr+"'"
									+ " />" 
									+ " <img class='approveImg' id= 'approveImg' src='./Approve.png' height='13' width='13' style='display:none;'>"
									
									+ " <input data-size='mini' type='radio' bCount="+bCount+" name='radioBtn"+ bCount + "'" +"id='radioBtnRJ"+ bCount +"'"+" value='Reject' class='reject' "
									+ " scr_id='"+row.scr+"' opr_ss='"+row.opr_ss+"'"
									+ " opr_ncr='"+row.opr_ncr+"' opr_hse='"+row.opr_hse+"'"
									+ " ss='"+row.ss+"' hse='"+row.hse+"'"
									+ " ncr='"+row.ncr+"'"
									+ " />"
									+ " <img class='rejectImg' id= 'rejectImg' src='./Reject.png' height='15' width='15' style='display:none;'>"
									//+ " </label>"	
									+ " </div>"
									+ " </div>";
		            			
		            			bCount++;
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
		            		
		            		
		            		if(bCount > 0){
		            			reviewDialog = jQuery("#reviewDialog").dialog({
		            				title: 'Review pending scores for Student: '+ studentName + ' Subtest: '+ subtestName,
		            				width: 900,
		            				height: "auto",
		            				draggable: false,	
		            				modal: true,
		            				buttons: {
		            					  /*'Reset' : function() {
		            						  $('input:radio').bootstrapSwitch('destroy', true);
		            						  $('input:radio').bootstrapSwitch({
		            							  'state': null
		            						  }); 
		            						  $(oTable.$('.rv_comment')).val("");
		            			          },*/
		            			          'Cancel' : function() {
		            			              $(this).dialog('close');
		            			          },
		            			          'Save': function(){
		            			        	  validateReviewScore(studentBioId,subtestId,function(){ 
		            			        		  reviewDialog.dialog('close'); 
		            			        	  });
		            			          }
		            				}
		            			});
		            		} else {
		            			reviewDialog = jQuery("#reviewDialog").dialog({
		            				title: 'Review pending scores for Student: '+ studentName + ' Subtest: '+ subtestName,
		            				width: 900,
		            				height: "auto",
		            				draggable: false,	
		            				modal: true,
		            				buttons: {				 
		            			          'Cancel' : function() {
		            			              $(this).dialog('close');
		            			          }
		            				}
		            			});
		            		}
		            		
							return html;
						}
		            }
		        ]
			});
	    	//$('input:radio');

	      },
	      error: function(data) {
			  alert('Failed to get Student Details');
		  }
	});	
}

function validateReviewScore(studentBioId,subtestId,callbackFunction){
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
		if($(this).is(':checked')) {
			if($(this).val()== 'Approved'){
				var ss = $(this).attr('ss')==''?0:$(this).attr('ss');
				var ncr = $(this).attr('ncr')==''?0:$(this).attr('ncr');
				var hse = $(this).attr('hse')==''?0:$(this).attr('hse');
				var opr_ss = $(this).attr('opr_ss')==''?0:$(this).attr('opr_ss');
				var opr_ncr = $(this).attr('opr_ncr')==''?0:$(this).attr('opr_ncr');
				var opr_hse = $(this).attr('opr_hse')==''?0:$(this).attr('opr_hse');
				if(opr_ss>ss || opr_ncr>ncr || opr_hse>hse ){
					scoreFlag = true;
				}
			}
		}
		
	});
	
	
	if(messageEmptyFlag == true){
		message = "Please enter comment";
		$("#errorLog").show();
    	$("#errorLog").css("color","red");
    	$("#errorLog").text(message);
	}else if(scoreFlag == true){
		$("#confirmDialog").dialog({
			title: 'The selected score(s) are lower than other available scores.',
			width: 513,
			height: 134,
			resizable: false,
			draggable: false,	
			modal: true,
			closeOnEscape: false,
			buttons : {
				"Proceed" : function(){
					saveReviewScore(studentBioId,subtestId,callbackFunction);
					$(this).dialog("close");
				},
				"Cancel" : function() {
					$(this).dialog("close");
				}
			}
		});		
	}else{
		saveReviewScore(studentBioId,subtestId,callbackFunction);
	}
}

function saveReviewScore(studentBioId,subtestId,callbackFunction){

	var commentStr = ""; 
	$(oTable.$('input:text')).each(function (index, value){
		var tempStr = $(this).attr('scr_id')+'~'+$(this).val();
		commentStr = commentStr+','+tempStr;
	}); 
	commentStr = commentStr.substring(1);
	
	var statusStr = ""; 
	$(oTable.$('input:radio')).each(function (index, value){
		var tempStr = $(this).attr('scr_id')+'~';
		if($(this).is(':checked')) {
			if($(this).val()== 'Approved'){
				tempStr = tempStr + 'AP';
			}else{
				tempStr = tempStr + 'RJ';
			}
			statusStr = statusStr+','+tempStr;
		}
		
		
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
	    	$("#messageDialog").dialog({
				title: 'Message',
				width: 'auto',
				height: 'auto',
				resizable: false,
				draggable: false,	
				modal: true,
				closeOnEscape: false,
				open: function() {
				      $(this).html(message);
				    },
				buttons : {
					"Okay" : function() {
						$(this).dialog("close");
						callbackFunction();
					}
				}
			});
	    },
	    error: function(data) {
	    	message = 'Failed to save Scoring Data';
	    	$("#errorLog").show();
	    	$("#errorLog").css("color","red");
	    	$("#errorLog").text(message);
		}
	});
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



