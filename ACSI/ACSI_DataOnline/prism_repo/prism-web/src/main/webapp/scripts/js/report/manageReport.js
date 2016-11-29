/**
 * This js file is to manage report module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */

$(document).ready(function() {

	 //enabling the validation of the report addition modal
	 $("#addNewReport").validationEngine({promptPosition : "centerRight", scroll: false});
	 $("#editReportForm").validationEngine({promptPosition : "centerRight", scroll: false});
	 
	 // edit report in Manager Reports screen
	if($('.edit-report').length > 0) {
		$('#report-list').tablesorter({
			headers: {
				4: { sorter: false }
			},
			sortList: [[0,1]]
		});
		
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-dashboard").parent().addClass("current");
		
		$('.edit-report').live("click", function() {
			openModal($(this).attr('reportid'));
		});
		
		$('#addDashboard').live("click", function() {
		resetAddReportModal($("#addNewReport"),"addNewReport","reportStatusCheck");
		openAddReportModal($("#addNewReport"));
		});
	}

});
// *********** END DOCUMENT.READY ************


//===================== Manage Report screen ===========================
//----------------------Edit Report-------------------------------
function openModal(reportId) {
	var row = $("#"+reportId);
	var reportName = row.find('.reportName').html();
	var reportUrl = row.find('.reportUrl').html();
	var status = row.find('.status').html();
	var roles = row.find('.role');
	
	
		clearRolesOption(roles);
		
	$("#reportId").val(reportId);
	$("#editReportName").val(reportName);
	$("#editReportUrl").val(reportUrl);
	if(status == 'Enabled') {
		$("#editReportStatus").attr('checked', true);
	} else {
		$("#editReportStatus").removeAttr('checked');
	}
	$("#editReportStatus").change();
	
	$.each(roles, function(index, value) { 
		$("#editUserRole option").each(function() {
			if($(this).val() == roles[index].innerHTML) {
				$(this).attr('selected', 'true');
			} /*else {
				$(this).removeAttr('selected');
			}*/
			$(this).change();
			$(this).trigger('update-select-list');
		});
	});
	
	
	
	manageIconIE('icon-star');
	$("#editRole").modal({
		title: 'Edit Report',
		height:200,
		width:400,
		draggable: false,
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
					$("#editReportForm").validationEngine('hide');
					clickMe(e);
					win.closeModal(); 
				}
			},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
					clickMe(e);
					if ($("#editReportForm").validationEngine('validate')){
							$("#editReportForm").validationEngine('hide');
					  updateReportDetails($(".edit-report-form"), win, row);
					}
				}
			}
		}
	});		
}

//==================================CLEAN THE ROLES DROPDOWN ON CLICK====================
	function clearRolesOption(roles)
	{
	    //$.each(roles, function(index, value) { 
			$("#editUserRole option").each(function() {
				
				$(this).removeAttr('selected');
				
				$(this).change();
				$(this).trigger('update-select-list');
			});
			$("#addUserRole option").each(function() {
				
				$(this).removeAttr('selected');
				
				$(this).change();
				$(this).trigger('update-select-list');
			});
			
		//});
	}
//==================================CLEAN THE  DROPDOWN AND TEXT FIELDS WHILE OPENING====================
			function resetAllFields(addFormObj){
			
			addFormObj.find("#reportType option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			 });
			 addFormObj.find("#assessmentType option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			 });
			 
			}	

//----------------------------Update report details---------------------
function updateReportDetails(form, win, row) {
	//alert($(form).serialize());
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateReport.do',
		data : form.serialize(),
		dataType: 'html',
		cache:false,
		success : function(data) {
			var obj = jQuery.parseJSON(data);
			win.closeModal(); 
			if(obj.status == 'Success') {
				$.modal.alert(strings['script.manageReport.update']);
				updateRowValuesForRole(row);
				enableSorting(true);
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
			unblockUI();
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError']);
		}
	});
}
//----------------------------Update row if update success ---------------------
var tag = '<small class="tag _BGCOLOR_ _CLASS_ ">_VALUE_</small>';
function updateRowValuesForRole(row) {
	var statusTag = tag;
	var roleTag = '';
	var statusClass = 'status';
	var statusVal = '';
	
	if($("#editReportStatus").attr('checked') ==  true || $("#editReportStatus").attr('checked') == 'checked' ) {
		statusClass = statusClass + ' green-bg';
		statusVal = 'Enabled';
	} else {
		statusClass = statusClass + ' red-bg';
		statusVal = 'Disabled';
	}
	var statusTag = statusTag.replace(/_CLASS_/g, statusClass);
	statusTag = statusTag.replace(/_VALUE_/g, statusVal);
	
	$("#editUserRole option").each(function() {
		if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
			var roleClass = 'role' + ' ' + $(this).val();
			var roleTagTemp = tag +'<br/>';
			roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
			roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).val());
			
			if($(this).val()=="ROLE_ACSI")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "blue-bg");
			else if ($(this).val()=="ROLE_CTB")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "green-bg");
			else if ($(this).val()=="ROLE_SCHOOL")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
			else if ($(this).val()=="ROLE_CLASS")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "grey-bg");	
			else ($(this).val()=="ROLE_PARENT")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "red-bg");	
				
			roleTag = roleTag + roleTagTemp;
			//alert(roleTag);
		}
	});
	
	row.find('.reportName').html( $("#editReportName").val() );
	//alert($("#reportName").val());
	row.find('.reportUrl').html( $("#editReportUrl").val() );
	row.find('.status').parent().html(statusTag);
	row.find('.roleContainer').html(roleTag);
	
}

//=============================DELETE REPORT ON CLICK======================
$('.delete-Report').live("click", function() {

    var row = $(this);
	var reportId = $(this).attr("reportId");
	var reportName = $(this).attr("reportName");
	$.modal.confirm("Confirm delete?" ,
		function () {
			deleteReportDetails(reportId, reportName,row);
		},function() {//this function closes the confirm modal on clicking cancel button
		} 
	);
	
});	
//=============================AJAX CALL TO DELETE REPORTS FROM DB TABLES=============================
	function deleteReportDetails(reportId, reportName,row) {
			blockUI();
			$.ajax({
				type : "GET",
				url : 'deleteReport.do',
				data : "reportId="+reportId,
				dataType: 'html',
				cache:false,
				success : function(data) {
					var obj = jQuery.parseJSON(data);
					//win.closeModal(); 
					if(obj.status == 'Success') {
						$.modal.alert('Report deleted successfully.');				
						deleteRowValues(row);//this method is present in manageUser.js
					} else {
						$.modal.alert(strings['script.user.deleteError']);
					}
					unblockUI();
				},
				error : function(data) {
					$.modal.alert(strings['script.user.deleteError']);
					unblockUI();
				}
			});
		}
//====================================OPEN REPORT MODAL==========================
	function openAddReportModal(addFormObj){
		manageIconIE('icon-star');
		clearRolesOption();
		resetAllFields(addFormObj);
		$("#reportStatusCheck").attr('checked', true);
		$("#reportStatusCheck").change();
		$("#addReport").modal({
			title: 'Add Report',
			height:350,
			width:400,
			draggable: false,
			buttons: {
				'Cancel': {
					classes: 'glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						addFormObj.validationEngine('hide');
						win.closeModal(); 
					}
				},
				'Save': {
					classes: 'blue-gradient glossy mid-margin-left',
					click: function(win,e) {
						clickMe(e);
						if (addFormObj.validationEngine('validate')){
							addFormObj.validationEngine('hide');
							if(addFormObj.find("#reportStatusCheck").attr("checked")=="checked" || addFormObj.find("#reportStatusCheck").attr("checked")==true){
								addFormObj.find("input#reportStatus").val(addFormObj.find("#reportStatusCheck").val());
							}
						//addFormObj.find("input#reportStatus").val(addFormObj.find("#reportStatusCheck").val());
							addDashboard(addFormObj,win);
						}
					}
				}
			}
		});	
	}
	/*var obj = jQuery.parseJSON(data);
					
					if(obj.status == 'Success') {
						$.modal.alert('Report added successfully');				
						insertNewDasboardRow(row);
					} else {
						$.modal.alert('Some error occured while deleting. Please try after some time.');
					}*/
	function addDashboard(addFormObj,win)
	{
		blockUI();
		$.ajax({
				type : "POST",
				url : 'addDashboard.do',
				data :addFormObj.serialize(),
				dataType: 'json',
				cache:false,
				success : function(data) {
					if (data!=null){
					
						if(data.status =="Faliure"){
							$.modal.alert(strings['script.manageReport.exists']);
						}
						else {
								win.closeModal();
								$.modal.alert(strings['script.manageReport.addSuccess']);
								if (insertNewDasboardRow(data)){
									enableSorting(true);
								}
						}					
					}
					unblockUI();
					
				},
				error : function(data) {
					$.modal.alert(strings['script.user.deleteError']);
					unblockUI();
				}
			});
	}
	
	
	
	//===================================CREATES THE ROW FOR THE NEW REPORT ADDED=====================
		function insertNewDasboardRow(jsonData)
		{
			var reportContent="";	
			$.each(jsonData, function () { 
		    
				reportContent += '<tr id='+this.reportId+'>'
								+'<th scope="row"><span class="reportName">'+this.reportName+'</span><br>'
								+'<small class="reportUrl">'+this.reportUrl+'</small></th>'
								+'<td class="vertical-center">'
								+makeStatusDom(this.enabled)
								+'</td>'
								+'<td class="roleContainer vertical-center">'
								+makeRoleDom(this.roles)
								+'</td>'
								+'<td class="vertical-center">'
								+'<small>'+this.assessmentName+'</small>'
								+'</td>'
								+'<td class="vertical-center">'
								+'<span class="button-group compact">'
								+'<a href="#"	class="button icon-pencil edit-report" reportId="'+this.reportId+'"></a>'
								+'<a href="#" reportId="'+this.reportId+'" reportName="'+this.reportName+'" class="button icon-trash with-tooltip confirm delete-Report" title="Delete"></a>'
								+'</span>'
								+'</td>'
								+'</tr>';
				});
			$("#reportDetails").append(reportContent);
			return true;
		}
		
		function makeStatusDom(status)
		{
			if (status==false)
			return '<small class="tag red-bg status">Disabled</small>'
			else
			return '<small class="tag green-bg status">Enabled</small>'
		}
		
		function makeRoleDom(rolesArray)
		{
			var reportRoleDom="";
			for(var i = 0; i < rolesArray.length; i++) {
				if(rolesArray[i]=="ROLE_ACSI"){
					reportRoleDom +='<small class="tag blue-bg role ROLE_ACSI">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_CTB"){
					reportRoleDom +='<small class="tag green-bg role ROLE_CTB">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_SCHOOL"){
					reportRoleDom +='<small class="tag orange-bg role ROLE_SCHOOL">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_CLASS"){
					reportRoleDom +='<small class="tag grey-bg role ROLE_CLASS">'+rolesArray[i]+'</small><br/>'
				}else {
					reportRoleDom +='<small class="tag red-bg role ROLE_PARENT">'+rolesArray[i]+'</small><br/>'
				}
			}
			return reportRoleDom;
		}
		
		function resetAddReportModal(modalFormIdObj,modalFormId,checkboxId)
		{
			modalFormIdObj.find(".reset").val("");
			enableStaus(modalFormId,checkboxId);
			
		}
		
	