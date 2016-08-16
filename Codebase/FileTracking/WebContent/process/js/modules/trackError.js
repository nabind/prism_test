$(document).ready(function(){
	
	/*$('#coCheckDateFrom').datepicker({maxDate: '0',onSelect: function(date) {
		date = $(this).datepicker('getDate');
        var maxDate = new Date(date.getTime());
        maxDate.setDate(maxDate.getDate() + 365);
        $('#coCheckDateTo').datepicker('option', {minDate: date, maxDate: maxDate});
    }});*/
	$('#errorDateFrom').datepicker();
	$('#errorDateTo').datepicker();
	
	$('#errorSearchButton').on("click", function(){
		validateForm();
	});
	
	$.fn.dataTable.ext.errMode = 'none';
	
	$("#errorResultTable").on( 'error.dt', function ( e, settings, techNote, message ) {
		alert( 'Error has occurred, please contact system administrator');
    	}).dataTable( {
		"bJQueryUI": true,
		"sPaginationType": "full_numbers",
		"order": [[ 12, "desc" ]],
        "bProcessing": true,
        "bServerSide": true,
        "sort": "position",
        "scrollX": true,
        "bStateSave": false,
        "iDisplayLength": 10,
        "iDisplayStart": 0,
        "fnDrawCallback": function () {
        	dataTableCallBack();
        },         
        "sAjaxSource": "errorResult.htm",
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
			    	}
			    }		
			},
			{ "mData": "recordId" },
			{ "mData": "fileName" },
			{ "mData": "fileGenDateTime" },
			{ "mData": "OrgIDTP" },
			{ "mData": "drcStudentID" },
			{ "mData": "stateCode"},
			{ "mData": "examineeID" },
			{ "mData": "errCodeErrDesc"},
			{ "mData": "studentName"},
			{ "mData": "dob"},
			{ "mData": "gender"},
			{ "mData": "procesDate"},
			{ "mData": "orgCodePath" },
			{ "mData": "testCenterCode" },
			{ "mData": "testCenterName" },
			{ "mData": "documentID" },
			{ "mData": "scheduleID" },
			{ "mData": "tcaScheduleDate" },
			{ "mData": "imagingID" },
			{ "mData": "lithoCode"},
			{ "mData": "testMode" },
			{ "mData": "testLanguage"},
			{ "mData": "contentName" },
			{ "mData": "form"},
			{ "mData": "dateTestTaken"},
			{ "mData": "barcodeID"},
			{ "mData": "contentScore" },
			{ "mData": "contentTestCode" },
			{ "mData": "scaleScore" },
			{ "mData": "scannedProcessDate"},
			{ "mData": "statusCodeContentArea" }
        ]
    });
	
	
	
});

function dataTableCallBack(){
	update_rows();
}

function update_rows(){
    $(".process_details tr:even").css("background-color", "#fff");
    $(".process_details tr:odd").css("background-color", "#eee");
}

function validateForm(){
	//TODO validation if needed
	$('#trackErrorForm').submit();
}
