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
        	dataTableCallBack();
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
					var html = "<a href='#note' class='noteLink moreInfoWin' style='color:#00329B;text-decoration:underline'";
					html = html + " imagingId='"+row.imagingId+"' orgTpName='"+row.orgTpName+"'"
								+ " lastName='"+row.lastName+"' firstName='"+row.firstName+"'"
								+ " middleInitial='"+row.middleInitial+"' lithoCode='"+row.lithoCode+"'"
								+ " scanStack='"+row.scanStack+"' scanSequence='"+row.scanSequence+"'"
								+ " winsDocId='"+row.winsDocId+"' comodityCode='"+row.comodityCode+"'"
								+ " winStatus='"+row.winStatus+"' prismProcessStatusDesc='"+row.prismProcessStatusDesc+"'"
								+ " imageFilePath='"+row.imageFilePath+"' imageFileName='"+row.imageFileName+"'"
								+ ">";
					html += "More Info</a>";
					return html;
				}
			}
        ]
    });
	
	
	
});

function getMoreInfoWin(htmlElement) {
	jQuery("#moreInfoDialog").dialog({
		title: 'More Info: ',
		width: 675,
		height: 410,
		draggable: false
	});
	$("#imagingId").html(htmlElement.attr('imagingId'));
	$("#orgTpName").html(htmlElement.attr('orgTpName'));
	$("#lastName").html(htmlElement.attr('lastName'));
	$("#firstName").html(htmlElement.attr('firstName'));
	$("#middleInitial").html(htmlElement.attr('middleInitial'));
	$("#lithoCode").html(htmlElement.attr('lithoCode'));
	$("#scanStack").html(htmlElement.attr('scanStack'));
	$("#scanSequence").html(htmlElement.attr('scanSequence'));
	$("#winsDocId").html(htmlElement.attr('winsDocId'));
	$("#comodityCode").html(htmlElement.attr('comodityCode'));
	$("#winStatus").html(htmlElement.attr('winStatus'));
	$("#prismProcessStatusDesc").html(htmlElement.attr('prismProcessStatusDesc'));
	$("#imageFilePath").html(htmlElement.attr('imageFilePath'));
	$("#imageFileName").html(htmlElement.attr('imageFileName'));
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