$(document).ready(function(){
	
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

function getMoreInfoWin(studentBioId, subtestId, studentName, subtestName) {
	var dataStringToCont = null;
	var dataString = "studentBioId="+studentBioId+"&subtestId="+subtestId;
	var table = $('#scoreReviewTable').DataTable({bJQueryUI : true});	
	
	jQuery("#reviewDialog").dialog({
		title: 'Review pending scores for Student: '+ studentName + ' Subtest: '+ subtestName,
		width: 900,
		height: "auto",
		draggable: false,
		  buttons: {
	          'Cancel' : function() {
	              $(this).dialog('close');
	          },
	          'Save': function(){
	        	  saveReviewScore(table);
	        	  $(this).dialog('close');
				}
			}
	});
	

	$.ajax({
		type: "POST",
	    url: "getReviewResult.htm",
	    data: dataString,
	    success: function(data) {
	    	table.destroy();
	    	table = $('#scoreReviewTable').dataTable({
				bJQueryUI : true,
				bPaginate : false,
				bProcessing: true,
				bFilter : false,
				bInfo : false,
				bSort : false,
				data : data.data,
		        columns: [
		            { data: "form" },
		            { data: "nc" },
		            { data: "ss" },
		            { data: "hse" },
		            { data: "date" },
		            { data: "comments" },
		            { data: "" }
		        ],
				"columnDefs" : [ {
					"targets" : -1,
					"data" : null,					
					"defaultContent" : "<input type='radio' name='radioBtn' value='1'  />"
					
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

function saveReviewScore(table){
	if(document.getElementsByName('radioBtn').checked) {
	    alert('checked');
	} else {
		alert('not checked');
	}
}

function dataTableCallBack(){
	update_rows();
	$('.moreInfoWin').on("click", function(){
		getMoreInfoWin($(this));
	});
}

function update_rows(){
    $(".process_details tr:even").css("background-color", "#fff");
    $(".process_details tr:odd").css("background-color", "#eee");
}

function validateForm(){
	//TODO validation if needed
	$('#scoreReviewForm').submit();
}

function clearMoreInfoTableRows() {
	$("#studentTestId").html('');
	$("#formName").html('');
	$("#newNC").html('');
	$("#newSS").html('');
	$("#newHSE").html('');
	$("#processedDate").html('');
	
}

function studentDetails(studentTestEventId,subTestName){
	clearMoreInfoTableRows();
	var dataString = "studentTestEventId="+studentTestEventId+"&subTestName="+subTestName;
	$("#review").html( '<img src="css/ajax-loader.gif"></img>' );
	jQuery("#reviewDialog").dialog({
		title: 'Student Score Info: ',
		width: 675,
		height: 410,
		draggable: false
	});
	$.ajax({
	      type: "POST",
	      url: "getStudentScoreInfo.htm",
	      cache: false,
	      data: dataString,
	      success: function(data) {
	    	  if(data == "Error") {
	    		  clearMoreInfoTableWhenError('Failed to get Data');
	    	  } else if(data.length == 2) {
	    		  clearMoreInfoTableWhenError('Data Not Found');
	          } else {
	    		  $("#review").html('');
		    	  var obj = JSON.parse(data);
		    	  $("#studentTestId").html( obj.TEST_ELEMENT_ID );
		    	  $("#formName").html( obj.FORM_NAME );
		    	  $("#newNC").html( obj.NCE );
		    	  $("#newSS").html( obj.SS );
		    	  $("#newHSE").html( obj.HSE );
		    	  $("#processedDate").html(obj.UPDATED_DATE_TIME);
		     }
	      },
		  error: function(data) {
			  clearMoreInfoTableWhenError('Failed to get Data');
		  }
    });

}

function rejectOther(id){
	var inputs = document.getElementsByName("isApprove");
	for(var i=0;i<inputs.length;i++){
		var input = inputs[i];
		input.onclick = function (evt) {
		    if (this.checked) {
		    disableInputs(this, inputs);
		    }
		    else {
		    enableInputs(this, inputs);
		    }
		    return true;
		    };
	}
	
function disableInputs (input, inputs) {
	    for (var i = 0; i < inputs.length; i++) {
	    var currentInput = inputs[i];
	    if (currentInput != input) {
	    currentInput.disabled = true;
	    }
	    }
	    }

function enableInputs (input, inputs) {
	    for (var i = 0; i < inputs.length; i++) {
	    var currentInput = inputs[i];
	    if (currentInput != input) {
	    currentInput.disabled = false;
	    }
	    }
	    }

}
