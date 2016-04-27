$(document).ready(function(){
	
	/*$('#coCheckDateFrom').datepicker({maxDate: '0',onSelect: function(date) {
		date = $(this).datepicker('getDate');
        var maxDate = new Date(date.getTime());
        maxDate.setDate(maxDate.getDate() + 365);
        $('#coCheckDateTo').datepicker('option', {minDate: date, maxDate: maxDate});
    }});*/
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

function getMoreInfoWin(studentBioId, subtestId) {
	jQuery("#searchScoreReview").dialog({
		title: 'Review pending scores: ',
		width: 900,
		height: 500,
		draggable: false
	});
	var dataString = "studentBioId="+studentBioId+"subtestId="+subtestId;
	$.ajax({
	      type: "POST",
	      url: "getReviewResult.htm",
	      data: dataString,
	      success: function(data) {
	    	  if(data == "Error") {
	    		  clearStudentDetailsTableWhenError('Failed to get review records');
	    	  } else {
	    		  $("#_stu_log").html( '' );
		    	  var obj = JSON.parse(data);
		    	  
	    	  }
	      },
		  error: function(data) {
			  clearStudentDetailsTableWhenError('Failed to get Student Details');
		  }
	});
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
	$("#imagingId").html('');
	$("#orgTpName").html('');
	$("#lastName").html('');
	$("#firstName").html('');
	$("#middleInitial").html('');
	$("#lithoCode").html('');
	$("#scanStack").html('');
	$("#scanSequence").html('');
	$("#winsDocId").html('');
	$("#comodityCode").html('');
	$("#winStatus").html('');
	$("#prismProcessStatus").html('');
	$("#imageFilePath").html('');
	$("#imageFileName").html('');
}

