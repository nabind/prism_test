var oTable;

$(document).ready(function(){
	
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
		"order": [[ 26, "desc" ]],
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
						  {'bSortable': false, 'aTargets': [0]},
						  { "width": "30%", "aTargets":  [22] }
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
			{ "mData": "stateCode"},
			{ "mData": "testMode" },
			{ "mData": "studentName"},
			{ "mData": "examineeID" },
			{
				"mRender": function ( data, type, row ) {
					var html = "<a href='#note' class='noteLink historyGhi' style='color:#00329B;text-decoration:underline'";
					html = html + " drcStudentID='"+row.drcStudentID+"' studentName='"+row.studentName+"'"
								+ ">";
					html += row.drcStudentID;
					html += "</a>";
					return html;
				}
			},
			{ "mData": "prismProcessStatus" },
			{ "mData": "barcodeID"},
			{ "mData": "scheduleID" },
			{ "mData": "tcaScheduleDate" },
			{ "mData": "dateTestTaken"},
			{ "mData": "form"},
			{ "mData": "contentName" },
			{ "mData": "contentTestCode" },
			{ "mData": "testLanguage"},
			{ "mData": "lithoCode"},
			{ "mData": "scaleScore" },
			{ "mData": "contentScore" },
			{ "mData": "statusCodeContentArea" },
			{ "mData": "testCenterCode" },
			{ "mData": "testCenterName" },
			{ "mData": "errCodeErrDesc"},
			{ "mData": "testEventUpdateDate"},
			{ "mData": "scannedProcessDate"},
			{ "mData": "orgCodePath" },
			{ "mData": "procesDate"},
			{ "mData": "documentID" },
			{ "mData": "fileName" },
			{ "mData": "fileGenDateTime" }	
        ]
    });
});

function getHistoryGhi(obj) {
	jQuery("#historyDialog").dialog({
		title: 'History for Student(DRC Student ID): '+obj.attr('drcStudentID')+" |  Student Name: "+obj.attr('studentName'),
		width: 920,
		height: 400,
		draggable: true,
		resizable: true,
		modal: true
	});
	
	var dataString = "drcStudentID="+obj.attr('drcStudentID');
	oTable = $('#historyTable').DataTable({bJQueryUI : true});	
	oTable.destroy();
	var reviewDialog;
	var bCount =0;
	
	$.ajax({
		type: "POST",
	    url: "getHistoryResult.htm",
	    data: dataString,
	    success: function(data) {
	    	oTable = $('#historyTable').dataTable({
	    		bJQueryUI : true,
	    		"sPaginationType": "full_numbers",
				"sort": "position",
				"order": [[ 4, "desc" ]],
				"bFilter": true,
				"bInfo": false,
				"bAutoWidth": false,
				"bProcessing": true,
				"aoColumnDefs": [ 
								  { 'bSortable': false, 'aTargets': [ 0 ]}
								],
				fnDrawCallback: function () {
		        	dataTableCallBack();
		        },
				data : data.data,
		        columns: [
					{ 
						"data": "prismProcessStatus",
						"mRender": function (oObj) {
							if(oObj == 'CO'){
					    		return "<span class='completed' title='Completed'></span>";
					    	}else if(oObj == 'ER'){
					    		return "<span class='error' title='Error'></span>";
					    	}
					    }		
					},
		            { data: "recordId" },
		            { data: "documentID" },
		            { data: "errCodeErrDesc" },
		            { data: "procesDate" }
		        ]
			});
	      },
	      error: function(data) {
			  alert('Failed to get History Data');
		  }
	});	
}

function dataTableCallBack(){
	update_rows();
	$('.historyGhi').on("click", function(){
		getHistoryGhi($(this));
	});
}

function update_rows(){
    $(".process_details tr:even").css("background-color", "#fff");
    $(".process_details tr:odd").css("background-color", "#eee");
}

function validateForm(){
	//TODO validation if needed
	$("#errorResultTable").destroy();
	$('#trackErrorForm').submit();
}
