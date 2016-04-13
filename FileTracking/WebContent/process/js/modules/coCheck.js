$(document).ready(function(){
	
	/*$('#coCheckDateFrom').datepicker({maxDate: '0',onSelect: function(date) {
		date = $(this).datepicker('getDate');
        var maxDate = new Date(date.getTime());
        maxDate.setDate(maxDate.getDate() + 365);
        $('#coCheckDateTo').datepicker('option', {minDate: date, maxDate: maxDate});
    }});*/
	$('#coCheckDateFrom').datepicker();
	$('#coCheckDateTo').datepicker();
	
	$('#coCheckButton').on("click", function(){
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
	
	$("#coCheckResultTable").on( 'error.dt', function ( e, settings, techNote, message ) {
		alert( 'Error has occurred, please contact system administrator');
    	}).dataTable( {
		"bJQueryUI": true,
		"sPaginationType": "full_numbers",
		"order": [[ 12, "desc" ]],
        "bProcessing": true,
        "bServerSide": true,
        "sort": "position",
        "bStateSave": false,
        "iDisplayLength": 10,
        "iDisplayStart": 0,
        "fnDrawCallback": function () {
        	update_rows();
        },         
        "sAjaxSource": "coCheckResult.htm",
        "aoColumnDefs": [ 
						  { 'bSortable': false, 'aTargets': [ 0 ]},
						  { 'bSortable': false, 'aTargets': [ 13 ]}
						],
        "aoColumns": [
			{ 
				"mData": "prismProcessStatus",
				"mRender": function (oObj) {
					if(oObj == 'CO'){
                		return "<span class='completed' title='Completed'></span>";
                	}else if(oObj == 'ER'){
                		return "<span class='error' title='Error'></span>";
                	}else if(oObj == 'NR'){
                		return "<span class='progress' title='Record not received by Prism'></span>";
                	}else if(oObj == 'IN'){
                		return "<span class='invalidated' title='Invalidated'></span>";
                	}
                }		
			},	                      
			{ "mData": "scanBatch" },
			{ "mData": "districtNumber" },
			{ "mData": "schoolNumber" },
			{ "mData": "uuid" },
			{ "mData": "barcode" },
			{ "mData": "form"},
			{ "mData": "braille" },
			{ "mData": "largePrint"},
			{ "mData": "dateTestTaken"},
			{ "mData": "loginDate"},
			{ "mData": "scanDate"},
			{ "mData": "winsExportDate"},
			{
				"mRender": function ( data, type, row ) {
					var html = "<a href='#note' class='noteLink' style='color:#00329B;text-decoration:underline' onclick='";
					html = html + "getMoreInfo(" +row+ ");'>";
					html += "More Info</a>";
					return html;
				}
			}
        ]
    });
	
});

function update_rows(){
    $(".process_details tr:even").css("background-color", "#fff");
    $(".process_details tr:odd").css("background-color", "#eee");
}

function validateForm(){
	//TODO validation if needed
	$('#coCheckForm').submit();
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


function getMoreInfo(rowObj) {
	alert(rowObj.imagingId);
	clearMoreInfoTableRows();
	var dataString = "erExcdId="+erExcdId;
	$("#moreInfo").html( '<img src="css/ajax-loader.gif"></img>' );
	jQuery("#moreInfoDialog").dialog({
		title: 'More Info: ',
		width: 675,
		height: 410,
		draggable: false
	});
	$.ajax({
	      type: "POST",
	      url: "getMoreInfo.htm",
	      data: dataString,
	      success: function(data) {
	    	  if(data == "Error") {
	    		  clearMoreInfoTableWhenError('Failed to get Data');
	    	  } else if(data.length == 2) {
	    		  clearMoreInfoTableWhenError('Data Not Found');
	          } else {
	    		  $("#moreInfo").html('');
		    	  var obj = JSON.parse(data);
		    	  $("#imagingId").html( obj.TESTING_SITE_CODE );
		    	  $("#testCenterName_mi").html( obj.TESTING_SITE_NAME );
		    	  $("#testLanguage_mi").html( obj.TEST_LANGUAGE );
		    	  $("#lithoCode_mi").html( obj.LITHOCODE );
		    	  $("#scoringDate_mi").html( obj.SCORING_DATE );
		    	  $("#scannedDate_mi").html( obj.SCANNED_DATE );
		    	  $("#studentName_mi").html( obj.LAST_NAME );
		    	  $("#numberCorrect_mi").html( obj.NCR_SCORE );
		    	  $("#statusCode_mi").html( obj.CONTENT_STATUS_CODE );
		    	  $("#scanBatch_mi").html( obj.SCAN_BATCH );
		    	  $("#scanStack_mi").html( obj.SCAN_STACK );
		    	  $("#scanSequence_mi").html( obj.SCAN_SEQUENCE );
		    	  $("#bioImages_mi").html( obj.BIO_IMAGES );
		     }
	      },
		  error: function(data) {
			  clearMoreInfoTableWhenError('Failed to get Data');
		  }
    });
    return false;
}

function getErrorLog(erExcdId) {
	var dataString = "erExcdId="+erExcdId;
	$("#errorLog").html( '<img src="css/ajax-loader.gif"></img>' );
	jQuery("#errorLogDialog").dialog({
		title: 'Error Log',
		width: 510,
		height: 448,
		draggable: false
	});
	$.ajax({
	      type: "POST",
	      url: "getErrorLog.htm",
	      data: dataString,
	      success: function(data) {
	    	  $("#errorLog").html( data );
	      },
		  error: function(data) {
			  $("#errorLog").html( ' Failed to get Error Log' );
		  }
    });
    return false;
}