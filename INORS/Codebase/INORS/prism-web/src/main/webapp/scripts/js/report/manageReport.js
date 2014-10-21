/**
 * This js file is to manage report module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
$(window).load(function() {
	$("#blockDiv").remove();
});
$(document).ready(function() {

	 //enabling the validation of the report addition modal
	 $("#addNewReport").validationEngine({promptPosition : "centerRight", scroll: false});
	 $("#editReportForm").validationEngine({promptPosition : "centerRight", scroll: false});
	 
	// CKEditor
	 //This block effects other text area throughout the application - give js error
	 /*
	 $('textarea').each(function(){
		CKEDITOR.inline( $(this).attr('id') );
	});*/
	
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
	
	if($('.edit-actions').length > 0) {
		$('.edit-actions').live("click", function() {
			openModalForEditActions($(this).attr('reportid'));
		});
	}
	
	$("#productForAction").on("change", function(event) {
		var reportId = $("#reportIdForAction").val();
		drawActionDropdownForEditActions(reportId)
	});

});
// *********** END DOCUMENT.READY ************

function populateAllSelectedOptionsFromObjectValueTO(dropdownId, list) {
	var innerHtml = "";
	if(typeof list != "undefined") {
		$.each(list, function(index, value) {
			innerHtml = innerHtml + '<option value="' + value.name + '" selected="true">' + value.value + '</option>';
		});
	}
	$("#"+dropdownId).html(innerHtml);
	$("#"+dropdownId+" option").change();
	$("#"+dropdownId+" option").trigger('update-select-list');
}

function populateActionsFromObjectValueList(dropdownId, actionList) {
	var innerHtml = "";
	if(typeof actionList != "undefined") {
		$.each(actionList, function(index, value) {
			innerHtml = innerHtml + '<option value="' + value.id + '"';
			if(value.status == "AC") innerHtml = innerHtml + '" selected="true"';
			innerHtml = innerHtml + ">" + value.name + '</option>';
		});
	}
	//alert("innerHtml=." + innerHtml+".");
	if(innerHtml == "") {
		//alert("Error");
		// $(".error-message").html(strings['script.report.noActionFound']);
		//$(".error-message").removeClass('hidden');
		$(".error-message").attr('class', 'error-message message small-margin-bottom red-gradient');
	} else {
		//alert("Ok");
		//$(".error-message").addClass('hidden');
		$(".error-message").attr('class', 'error-message message small-margin-bottom red-gradient hidden');
	}
	$("#"+dropdownId).html(innerHtml);
	$("#"+dropdownId+" option").change();
	$("#"+dropdownId+" option").trigger('update-select-list');
}

function openModalForEditActions(reportId) {
	$(".error-message").attr('class', 'error-message message small-margin-bottom red-gradient');
    manageIconIE('icon-star');
    var param = "reportId=" + reportId;	
    blockUI();
    $.ajax({
		type : "GET",
		url : "getReportDataForEditActions.do",
		data : param,
		dataType : 'json',
		cache:false,
		success : function(data) {
			unblockUI();
			$("input#reportIdForAction").val(data.id);
			$("input#reportNameForAction").val(data.name);

			var products = data.productList;
			populateAllSelectedOptionsFromObjectValueTO("productForAction", products);
			
			var roles = data.roleList;
			populateAllSelectedOptionsFromObjectValueTO("roleForAction", roles);
			
			var levels = data.orgLevelList;
			populateAllSelectedOptionsFromObjectValueTO("levelForAction", levels);
			
			/*var actions = data[1];
			populateActionsFromObjectValueList("newAction", actions);*/
			$("#newAction").html('');
			
			drawActionDropdownForEditActions(reportId);
			
			drawModalForEditActions();	
				
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	})	
}

function drawActionDropdownForEditActions(reportId) {
	var productForAction = $("#productForAction").val();
	// alert("productForAction=" + productForAction);
    var param = "reportId=" + reportId + "&custProdId=" + productForAction;	
    blockUI();
    $.ajax({
		type : "GET",
		url : "getActionDataForEditActions.do",
		data : param,
		dataType : 'json',
		cache:false,
		success : function(data) {
			unblockUI();
			var actions = data;
			populateActionsFromObjectValueList("newAction", actions);
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	})	
}

function drawModalForEditActions() {
	$("#editActionsForm").modal({
		title: 'Edit Actions',
		height: 265,
		width: 400,
		resizable: false,
		draggable: false,
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
					clickMe(e);
					win.closeModal(); 
				}
			},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
					clickMe(e);
					updateActionsDetails($(".edit-actions-form"), win);
				}
			}
		},
		onOpen: function(){
			$("input#userRole").change();
			$("input#userRole").trigger('update-select-list');		
		}
	});
}

function updateActionsDetails(form, win) {
	blockUI();
	$.ajax({
		type : "GET",
		url : 'updateActions.do',
		data : form.serialize(),
		dataType: 'json',
		cache:false,
		success : function(data) {
			unblockUI();
			win.closeModal(); 
			if(data.status == 'Success') {
				$.modal.alert(strings['script.report.actionsSavedSuccessfully']);
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError']);
		}
	});
}

//===================== Manage Report screen ===========================
//----------------------Edit Report-------------------------------
 
function openModal(reportId) {
var row = $("#"+reportId + '_' +reportId);
    manageIconIE('icon-star');
    var param = "reportId=" + reportId;	
    blockUI();
    $.ajax({
		type : "GET",
		url : "getEditDataForReport.do",
		data : param,
		dataType : 'json',
		cache:false,
		success : function(data) {
			unblockUI();
			$("input#reportId").val(data[0].reportId);
			$("input#reportName").val(data[0].reportName);
			$("input#reportDescription").val(data[0].reportDescription);
			$("input#reportUrl").val(data[0].reportOriginalUrl);
			$("input#menuType").val(data[0].menuType);
			$("input#reportType").val(data[0].reportType);
			$("input#reportSequence").val(data[0].reportSequence);
			$("input#customerType").val(data[0].customerType);
			$("input#reportStatus").val(data[0].reportStatus);
			//$("input#userRole").val(data[0].userRole);
			if(data[0].enabled == true) {
				$("input#editReportStatus").attr('checked', true);
			} else {
				$("input#editReportStatus").removeAttr('checked');
			}
			$("input#editReportStatus").change();
			var menuType = data[0].menuId;
			if(typeof menuType != "undefined") {
				$.each(menuType, function(index, value) {
					$("#editMenuType option").each(function() {				
						if($(this).val() == menuType) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			}
			var reportType = data[0].reportType;
			if(typeof reportType != "undefined") {
				$.each(reportType, function(index, value) {
					$("#editReportType option").each(function() {				
						if($(this).val() == reportType) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			}
			var customerProductArr = data[0].customerProductArr;
			if(typeof customerProductArr != "undefined") {
				$.each(customerProductArr, function(index, value) {
					$("#editCustomerType option").each(function() {				
						if($(this).val() == customerProductArr[index]) {
							$(this).attr('selected', 'true');
						}
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			}
			var roles = data[0].roles;
			if(typeof roles != "undefined") {
				$.each(roles, function(index, value) {
					$("#userRole option").each(function() {				
						if($(this).val() == roles[index]) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#userRole option").change();
				$("#userRole option").trigger('update-select-list');
			}
			$("#orgNodeLevel option").each(function() {
				$(this).removeAttr('selected');
				$(this).change();
				$(this).trigger('update-select-list');
			});
			var nodeLevels = data[0].orgNodeLevelArr;
			if(typeof nodeLevels != "undefined") {
				$.each(nodeLevels, function(index, value) {
					$("#orgNodeLevel option").each(function() {
						if(nodeLevels[index].match("^" + $(this).text())) {
							$(this).attr('selected', 'true');
						} 
						$(this).change();
						$(this).trigger('update-select-list');
					});
				});
			} else {
				$("#orgNodeLevel option").change();
				$("#orgNodeLevel option").trigger('update-select-list');
			}
			
			$("#editReportForm").modal({
				title: strings['msg.editReport'],
				height: 470,
				width: 400,
				resizable: false,
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
				},
				onOpen: function(){
					$("input#userRole").change();
					$("input#userRole").trigger('update-select-list');		
				}
			});		
				
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error1']);
		}
	})	
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
	
//==================CLEAN THE ORG LEVEL OPTION WHIOLE OPENING======================
	function clearOrgLevelOption(){
		$('#addReport #allOrgNode').val([]);
	}

	function clearReportTypeOption(){		
		$('#addReport #reportType').val([]);
	}
	
	function clearCustomerProdOption(){
		$('#addReport #customerType').val([]);
	}
	
	function clearMenuOption(){
		$('#addReport #menuType').val([]);
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
			 setDefaultValuesForAddReportModal();
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
	
	$("#userRole option").each(function() {
		if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
			var roleClass = 'role' + ' ' + $(this).val();
			var roleTagTemp = tag +'<br/>';
			roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
			roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).val());
			
			if($(this).val()=="ROLE_USER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "black-bg");
			else if ($(this).val()=="ROLE_CTB")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "green-bg");
			else if ($(this).val()=="ROLE_SUPER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "blue-bg");
			else if ($(this).val()=="ROLE_ADMIN")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
			else if ($(this).val()=="ROLE_GRW")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "grey-bg");
			else ($(this).val()=="ROLE_PARENT")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "red-bg");	
			
				
			roleTag = roleTag + roleTagTemp;
			//alert(roleTag);
		}
	});
	
	/*
	 * Fix for TD 79043 - By Joy
	 * As the method updateRowValuesForRole() called only from updateReportDetails() so
	 * we use $("#editReportForm #reportName").val() to get edited report name
	 * */
	row.find('.reportName').html( $("#editReportForm #reportName").val() );
	
	row.find('.reportUrl').html( $("#reportUrl").val() );
	row.find('.status').parent().html(statusTag);
	row.find('.roleContainer').html(roleTag);
	row.find('.menu_name').html($("#editMenuType option:selected'").html());
	row.find('.menuid').val($("#editMenuType").val());
}

//=============================DELETE REPORT ON CLICK======================
$('.delete-Report').live("click", function() {

    var row = $(this);
	var reportId = $(this).attr("reportId");
	var reportName = $(this).attr("reportName");
	$.modal.confirm(strings['msg.confirm.delete'],
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
					enableSorting(true);
					var obj = jQuery.parseJSON(data);
					//win.closeModal(); 
					if(obj.status == 'Success') {
						$.modal.alert(strings['msg.reportDeletedSuccessfully']);				
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
		clearOrgLevelOption();
		clearReportTypeOption();
		clearCustomerProdOption();
		clearMenuOption();
		resetAllFields(addFormObj);
		$("#reportStatusCheck").attr('checked', true);
		$("#reportStatusCheck").change();
		$("#addReport").modal({
			title: strings['msg.addReport'],
			height:470,
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
	function setDefaultValuesForAddReportModal() {
		$('#reportType option:first-child').attr("selected", "selected");
		$('#menuType option:first-child').attr("selected", "selected");
		$('#customerType option:first-child').attr("selected", "selected");
		$("#addUserRole option[value='ROLE_USER']").attr("selected", "selected");
		$('#allOrgNode option:first-child').attr("selected", "selected");
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
								unblockUI();
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
		function insertNewDasboardRow(jsonData){
			var mngRpt_editRpt = $('#mngRpt_editRpt').val();
			var mngRpt_configureRptMsg = $('#mngRpt_configureRptMsg').val();
			var mngRpt_deleteRpt = $('#mngRpt_deleteRpt').val();
			var reportContent="";	
			$.each(jsonData, function () { 
		    
				//Fix for TD 78098 - By Joy
				reportContent += '<tr id='+this.reportId+'_'+this.reportId+'>'
								+'<th scope="row"><span class="reportName">'+this.reportName+'</span><br>'
								+'<small class="reportUrl">'+this.reportUrl+'</small></th>'
								+'<td class="vertical-center">'
								+'<input type="hidden" class="reportDescription" name="reportDescription" value="'+this.reportDescription+'">'
								+'<input type="hidden" class="reportType" name="reportType" id="reportType" value="'+this.reportType+'"> '
								+'<input type="hidden" class="linkName" name="linkName" id="linkName" value="'+this.linkName+'"> '
								+'<input type="hidden" class="allOrgNode" name="allOrgNode" id="allOrgNode" value="'+this.allOrgNode+'"> '
								+makeStatusDom(this.enabled)
								+'</td>'
								+'<td class="roleContainer vertical-center">'
								+makeRoleDom(this.roles)
								+'</td>'
								+'<td class="vertical-center">'
								+'<h5>'+this.menuName+'</h5>'
								+'</td>'
								+'<td class="vertical-center">'
								+'<span class="button-group compact">';
				
								if(mngRpt_editRpt == 'true'){
									reportContent += '<a href="#nogo" class="button icon-pencil edit-report with-tooltip" title="Edit" reportId="'+this.reportId+'"></a>';
								}
								if(mngRpt_configureRptMsg == 'true'){
									reportContent += '<a href="getReportMessageFilter.do?reportId='+this.reportId+'&reportName='+this.reportName+'&reportUrl='+this.reportUrl+'" class="button icon-chat configure-report-message with-tooltip" title="'+strings['msg.configureMassage']+'"></a>';
								}
								if(mngRpt_deleteRpt == 'true'){
									reportContent += '<a href="#nogo" reportId="'+this.reportId+'" reportName="'+this.reportName+'" class="button icon-trash with-tooltip confirm delete-Report" title="'+strings['label.delete']+'"></a>';
								}	
								
								reportContent +='</span>'
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
				if (rolesArray[i]=="ROLE_CTB"){
					reportRoleDom +='<small class="tag green-bg role ROLE_CTB">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_SUPER"){
					reportRoleDom +='<small class="tag blue-bg role ROLE_SUPER">'+rolesArray[i]+'</small><br/>'
				}else if (rolesArray[i]=="ROLE_PARENT"){
					reportRoleDom +='<small class="tag red-bg role ROLE_PARENT">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_ADMIN"){
					reportRoleDom +='<small class="tag orange-bg role ROLE_ADMIN">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_GRW"){
					reportRoleDom +='<small class="tag grey-bg role ROLE_GRW">'+rolesArray[i]+'</small><br/>'
				} else if (rolesArray[i]=="ROLE_USER"){
					reportRoleDom +='<small class="tag black-bg role ROLE_USER">'+rolesArray[i]+'</small><br/>'
				}else {
					reportRoleDom +='<small class="tag red-bg role '+rolesArray[i]+'">'+rolesArray[i]+'</small><br/>'
				}
			}
			return reportRoleDom;
		}
		
		function resetAddReportModal(modalFormIdObj,modalFormId,checkboxId)
		{
			modalFormIdObj.find(".reset").val("");
			enableStaus(modalFormId,checkboxId);
			
		}
		