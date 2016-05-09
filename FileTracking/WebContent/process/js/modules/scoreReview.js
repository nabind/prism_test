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
	          },
	          'Cancel' : function() {
	              $(this).dialog('close');
	          },
	          'Save': function(){
	        	  saveReviewScore(studentBioId,subtestId);
	        	  $(this).dialog('close');
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
							var html = "<input type='text' name='comment' class='' value='"+row.comment+"'"
										+ " scr_id='"+row.scr+"'"
										+ "/>";
							return html;
						}
		            },
		            { 
		            	"mRender": function ( data, type, row ) {
							var html = "<input type='radio' name='radioBtn' class='' "
										+ " scr_id='"+row.scr+"'"
										+ "/>";
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
	    	alert('Data Saved');
	    },
	    error: function(data) {
	    	alert('Failed to save Scoring Data');
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



