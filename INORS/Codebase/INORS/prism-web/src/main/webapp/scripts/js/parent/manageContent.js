/**
 * This js file is to manage content module - Start
 * Author: Joy
 * Version: 1
 */
var ANIMATION_TIME = 200;

$(document).ready(function() {
	
	if($("#contentTable").length > 0) {
		var allContents = $(".content-list-all");
		if(allContents != null && allContents.length > 0) {
			$('#report-list').tablesorter({
				headers: {
					3: { sorter: false }
				},
				sortList: [[0,1]]
			});
		}
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-content").parent().addClass("current");
	}
	
	
	$('#custProdIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#gradeIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#subtestIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateGrade();
	}); 
	
	$('#gradeIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#subtestIdManageContent'),null,1,'clear');
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateSubtest();
	});
	
	$('#subtestIdManageContent').live('change',function(){
		hideContentElements();
		populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
		populateObjective();
	});
	
	$('#objectiveIdManageContent').live('change',function(){
		hideContentElements();
		showContentElements();
	});
	
	$('#contentTypeIdManageContent').live('change',function(){
		hideContentElements();
		showContentElements();
	});
	
	$('#addContent').live("click", function() {
		if ($('#objectiveIdManageContent').val() == null || $('#objectiveIdManageContent').val() == "" || $('#objectiveIdManageContent').val() == "-1" ) {
			$.modal.alert(strings['script.content.addContent']);
		}else {
			resetModalForm("addNewContent");
			resetModalForm("editContent");
			//resetModalForm("modifyStandardForm");
			resetModalForm("modifyGenericForm");
			openContentModalToAdd();
		}
	});
	
	$('#modifyStandardButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		//openModifyStandardModalToEdit();
		openModifyGenericModalToEdit('STD');
	});
	
	$('#modifyRscDiv').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('RSC');
	});
	
	$('#modifyEdaButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('EDA');
	});
	
	$('#modifyAttButton').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		resetModalForm("modifyStandardForm");
		resetModalForm("modifyGenericForm");
		openModifyGenericModalToEdit('ATT');
	});
	
	$('#refresh-content').live('click',function(){
		loadManageContentList();
	});
	
	var tempScrollTop, currentScrollTop = 0
	$("#moreContents").click(function() {
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		var lastid = $("#contentTable tr:last").attr("value");
		var checkFirstLoad = false;
		var paramUrl = 'custProdId='+custProdId+'&subtestId='+subtestId
						+'&objectiveId='+objectiveId+'&contentTypeId='+contentTypeId
						+'&lastid='+lastid+'&checkFirstLoad='+checkFirstLoad;
		
		currentScrollTop = $("#contentTable").scrollTop();
		if(!$(this).hasClass('disabled')) {
			var callingAction = "";
			var lastid = $('#lastid').val();
				callingAction = 'loadManageContent.do';
			blockUI();
			$.ajax({
				type : "GET",
				url : callingAction,
				data : paramUrl,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if (data != null && data.length > 0){
						getContentDetails(false,data);
						enableSorting(true);
						retainUniqueValue();
						unblockUI();
						$("#contentTable").animate({scrollTop: currentScrollTop+600}, 500);
					} else {
						$("#moreContents").addClass("disabled");
						if($.browser.msie) $("#moreContents").addClass("disabled-ie");
						retainUniqueValue();
						unblockUI();
					}
					if (data != null && data.length < 14) {
						// check if this is the last set of result
						$("#moreContents").addClass("disabled");
						if($.browser.msie) $("#moreContents").addClass("disabled-ie");
					}
				},
				error : function(data) {
					unblockUI();
				}
			});

		} else {
			return false;
		}
		tempScrollTop = currentScrollTop;
	});
	
	$('.edit-content').live("click", function() {
		resetModalForm("addNewContent");
		resetModalForm("editContent");
		openContentModalToEdit($(this).attr("contentId"));
	});	
	
	$('.delete-content').live("click", function() {
	    var row = $(this);
		var contentId = $(this).attr("contentId");
		$.modal.confirm("Do you want to delete this content?" ,
			function () {
				deleteContentDetails(contentId, row);
				enableSorting(true);
			},function() {//this function closes the confirm modal on clicking cancel button
			} 
		);
	});
	
	$('.mandatoryDescription').live('click',function(){
		$('.mandatoryDescription').hide(ANIMATION_TIME);
	});
	
});
//=====document.ready End=========================================

//============Open Modal to Edit Description of Resource, Everyday Activity and About the Test ===============
function openModifyGenericModalToEdit(type) {
	blockUI();
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = 0;
	var objectiveId = 0;
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	
	if(type == 'RSC' || type == 'STD'){
		subtestId = $('#subtestIdManageContent').val();
	}
	if(type == 'STD'){
		objectiveId = $('#objectiveIdManageContent').val();
	}
	
	var dataUrl = 'custProdId='+custProdId+'&gradeId='+gradeId
					+'&subtestId='+subtestId+'&objectiveId='+objectiveId
					+'&type='+type;
	
	$.ajax({
			type : "GET",
			url : "modifyGenericForEdit.do",
			data : dataUrl,
			dataType : 'json',
			cache:false,
			success : function(data) {
				var custProdName = $('#custProdIdManageContent :selected').text();
				var gradeName = $('#gradeIdManageContent :selected').text();
				var subtestName = $('#subtestIdManageContent :selected').text();
				var objectiveName = $('#objectiveIdManageContent :selected').text();
				
				var $modifyGenericModal = $('#modifyGenericModal');
				$modifyGenericModal.find('#testAdministrationText').text(custProdName);
				$modifyGenericModal.find('#gradeText').text(gradeName);
				if(type == 'RSC'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
				}else{
					$('#p_subtest').hide();
				}
				
				if(type == 'STD'){
					$('#p_subtest').show();
					$modifyGenericModal.find('#subtestText').text(subtestName);
					$('#p_objective').show();
					$modifyGenericModal.find('#objectiveText').text(objectiveName);
				}else{
					$('#p_subtest').hide();
					$('#p_objective').hide();
				}
				
				
				if(data != null && data.contentDescription != ""){
					$modifyGenericModal.find('#genericDescriptionEditor').val(data.contentDescription);
				}
				
				var modalTitle = '';
				if(type == 'STD'){
					modalTitle+='Modify Standard Description';
				}else if(type == 'RSC'){
					modalTitle+='Modify Resource Description';
				}else if(type == 'EDA'){
					modalTitle+='Modify Everyday Activity Description';
				}else if(type == 'ATT'){
					modalTitle+='Modify About the Test  Description';
				} 
				
				$("#modifyGenericModal").modal({
					title: modalTitle,
					height: 500,
					width: 780,
					resizable: false,
					draggable: false,
					onOpen: setCKEditor('modifyGeneric'),
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);
										$('.mandatoryDescription').hide();
										if($.browser.msie) setTimeout("hideMessage()", 300);
										win.closeModal(); 
									}
								},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);	
										modifyGeneric($("#modifyGenericForm"), win);
									}
								}
							}
					});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		}); 
}

//============Insert/Update Description of Resource/Everyday Activity/About the Test ===============
function modifyGeneric(form, win) {
	blockUI();
	var descriptionFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			descriptionFlag = false;
			break;
		}
	    $('#modifyGenericModal #contentDescription').val(editorVal);
	}
	
	if(descriptionFlag == true){
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		
		var $modifyGenericModal = $('#modifyGenericModal');
		$modifyGenericModal.find('#custProdId').val(custProdId);
		$modifyGenericModal.find('#gradeId').val(gradeId);
		$modifyGenericModal.find('#subtestId').val(subtestId);
		$modifyGenericModal.find('#objectiveId').val(objectiveId);
		$modifyGenericModal.find('#contentType').val(contentTypeId);
		$modifyGenericModal.find('#contentTypeName').val(contentTypeName);
		
		var formObj = $('#modifyGenericForm').serialize();
		$.ajax({
			type : "POST",
			url : 'addNewContent.do',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					unblockUI();
					$('.mandatoryDescription').hide();
					$.modal.alert(strings['script.content.addSuccess']);
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		 unblockUI();
	}
}

/* As Standard/Objective is dependent upon Test Administration, so the code is blocked by Joy */
//============Open Modal to Edit Description of Standard ===============
/*
function openModifyStandardModalToEdit() {
	blockUI();
	var objectiveId = $('#objectiveIdManageContent').val();
	var dataUrl = 'objectiveId='+objectiveId;
	$.ajax({
			type : "GET",
			url : "modifyStandardForEdit.do",
			data : dataUrl,
			dataType : 'json',
			cache:false,
			success : function(data) {
				var custProdName = $('#custProdIdManageContent :selected').text();
				var gradeName = $('#gradeIdManageContent :selected').text();
				var subtestName = $('#subtestIdManageContent :selected').text();
				var objectiveName = $('#objectiveIdManageContent :selected').text();
				var contentTypeId = $('#contentTypeIdManageContent').val();
				var contentTypeName = $('#contentTypeIdManageContent :selected').text();
				
				var $modifyStandardModal = $('#modifyStandardModal');
				$modifyStandardModal.find('#testAdministrationText').text(custProdName);
				$modifyStandardModal.find('#gradeText').text(gradeName);
				$modifyStandardModal.find('#subtestText').text(subtestName);
				$modifyStandardModal.find('#objectiveText').text(objectiveName);
				$modifyStandardModal.find('#objectiveId').val(objectiveId);
				if(data != null && data.contentDescription != ""){
					$modifyStandardModal.find('#objectiveDescriptionEditor').val(data.contentDescription);
				}
				
				$("#modifyStandardModal").modal({
					title: 'Modify Standard Description',
					height: 500,
					width: 780,
					resizable: false,
					draggable: false,
					onOpen: setCKEditor('modifyStandard'),
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);
										$('.mandatoryDescription').hide();
										if($.browser.msie) setTimeout("hideMessage()", 300);
										win.closeModal(); 
									}
								},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
										clickMe(e);	
										modifyStandard($("#modifyStandardForm"), win);
									}
								}
							}
					});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		}); 
}


//============Insert/Update Standard Description ===============
function modifyStandard(form, win) {
	blockUI();
	var descriptionFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			descriptionFlag = false;
			break;
		}
	    $('#modifyStandardModal #contentDescription').val(editorVal);
	}
	
	if(descriptionFlag == true){
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		var modifyStandardModal = $('#modifyStandardModal');
		modifyStandardModal.find('#contentType').val(contentTypeId);
		modifyStandardModal.find('#contentTypeName').val(contentTypeName);
		var formObj = $('#modifyStandardForm').serialize();
		$.ajax({
			type : "POST",
			url : 'addNewContent.do',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					unblockUI();
					$('.mandatoryDescription').hide();
					$.modal.alert(strings['script.content.addSuccess']);
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		 unblockUI();
	}
}
*/


//============Open Modal to Edit Content ===============
function openContentModalToEdit(contentId) {
	blockUI();
	$("#editContent").validationEngine({promptPosition : "centerRight", scroll: false});
	manageIconIE('icon-star');
	var dataUrl = 'contentId='+contentId;
	$.ajax({
			type : "GET",
			url : "getContentForEdit.do",
			data : dataUrl,
			dataType : 'json',
			cache:false,
			success : function(data) {
				var $editContentModal = $('#editContentModal');
				$editContentModal.find('#contentId').val(data.contentId);
				$editContentModal.find('#contentName').val(data.contentName);
				$editContentModal.find('#subHeader').val(data.subHeader);
				$editContentModal.find('#contentDescriptionEditorEdit').val(data.contentDescription);
				
				var performanceLevel = data.performanceLevel;
				$editContentModal.find('#performanceLevel option').removeAttr('selected');
				var option = "";
				if(data.performanceLevel == 'A'){
					option += "<option selected value='A'>Pass</option>";
					option += "<option value='B'>Did Not Pass</option>";
					option += "<option value='U'>Undefined</option>";	
					option += "<option value='N'>DNR</option>";		
					option += "<option value='P'>Pass+</option>";
				}else if(data.performanceLevel == 'B'){
					option += "<option value='A'>Pass</option>";
					option += "<option selected value='B'>Did Not Pass</option>";
					option += "<option value='U'>Undefined</option>";	
					option += "<option value='N'>DNR</option>";		
					option += "<option value='P'>Pass+</option>";
				}else if(data.performanceLevel == 'U'){
					option += "<option value='A'>Pass</option>";
					option += "<option value='B'>Did Not Pass</option>";
					option += "<option selected value='U'>Undefined</option>";	
					option += "<option value='N'>DNR</option>";		
					option += "<option value='P'>Pass+</option>";
				}else if(data.performanceLevel == 'N'){
					option += "<option value='A'>Pass</option>";
					option += "<option value='B'>Did Not Pass</option>";
					option += "<option value='U'>Undefined</option>";	
					option += "<option selected value='N'>DNR</option>";		
					option += "<option value='P'>Pass+</option>";
				}else if(data.performanceLevel == 'P'){
					option += "<option value='A'>Pass</option>";
					option += "<option value='B'>Did Not Pass</option>";
					option += "<option value='U'>Undefined</option>";	
					option += "<option value='N'>DNR</option>";		
					option += "<option selected value='P'>Pass+</option>";
				}
				$editContentModal.find('#performanceLevel').html(option);
				$editContentModal.find('#performanceLevel').change();
				$editContentModal.find('#performanceLevel').trigger('update-select-list');
				
				$("#editContentModal").modal({
					title: 'Edit Content',
					height: 430,
					width: 780,
					resizable: false,
					draggable: false,
					onOpen: setCKEditor('edit'),
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								$('#editContent').validationEngine('hide');
								$('.mandatoryDescription').hide();
								if($.browser.msie) setTimeout("hideMessage()", 300);
								clickMe(e);
								win.closeModal(); 
							}
						},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
								if($("#editContent").validationEngine('validate')){
									$('#editContent').validationEngine('hide');
									updateContent($("#editContent"), win);
								 }
							}
						}
					}
				});					
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.common.error1']);
			}
		});	
	unblockUI();
}

//============Update Content ===============
function updateContent(form, win) {
	blockUI();
	var updateContentFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			updateContentFlag = false;
			break;
		}
	    $('#editContentModal #contentDescription').val(editorVal);
	}
	
	if(updateContentFlag == true){
		var formObj = $('#editContent').serialize();
		$.ajax({
			type : "POST",
			url : 'updateContent.do',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					loadManageContentList();
					$.modal.alert(strings['script.content.editSuccess']);
					$('.mandatoryDescription').hide();
					unblockUI();
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		unblockUI();
	}
	
}


//=======================Delete Content Details====================
function deleteContentDetails(contentId, row) {
	blockUI();
	$.ajax({
		type : "GET",
		url : 'deleteContent.do',
		data : 'contentId='+contentId,
		dataType: 'json',
		cache:false,
		success : function(data) {
			if(data.value >= 1) {
				unblockUI();
				$.modal.alert(strings['script.content.deleteSuccess']);				
				deleteRowValues(row);
			} else {
				unblockUI();
				$.modal.alert(strings['script.user.deleteError']);
			}
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.deleteError']);
		}
	});
}

//============Open Modal to Add Content ===============
function openContentModalToAdd() {
    $("#addNewContent").validationEngine({promptPosition : "centerRight", scroll: false});
	manageIconIE('icon-star');
	
	$("#addContentModal").modal({
		title: 'Add Content',
		height: 430,
		width: 780,
		resizable: false,
		draggable: false,
		onOpen: setCKEditor('add'),
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);
							$('#addNewContent').validationEngine('hide');
							$('.mandatoryDescription').hide();
							if($.browser.msie) setTimeout("hideMessage()", 300);
							win.closeModal(); 
						}
					},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);	
							 if($("#addNewContent").validationEngine('validate')){
								$('#addNewContent').validationEngine('hide');
								addNewContent($("#addNewContent"), win);
							 }
						}
					}
				}
		});

}

function setCKEditor(purpose){
	var $objTextArea;
	if(purpose == 'add'){
		$objTextArea = $('#contentDescriptionEditor');
	}else if(purpose == 'edit'){
		$objTextArea = $('#contentDescriptionEditorEdit');
	}/*else if(purpose == 'modifyStandard'){
		$objTextArea = $('#objectiveDescriptionEditor');
	}*/else if(purpose == 'modifyGeneric'){
		$objTextArea = $('#genericDescriptionEditor');
	}
	
	if(CKEDITOR.instances[$objTextArea.attr('id') ] == undefined){
		CKEDITOR.inline($objTextArea.attr('id') );
	}else{
		for(name in CKEDITOR.instances)	{
			CKEDITOR.instances[name].destroy(true);
		}	
		CKEDITOR.inline($objTextArea.attr('id') );
		//CKEDITOR.replace($objTextArea.attr('id') );
	}
	unblockUI();
}

//============Insert Content ===============
function addNewContent(form, win) {
	blockUI();
	var addNewContentFlag = true;
	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
		if(editorVal == ""){
			$('.mandatoryDescription').show(ANIMATION_TIME);
			addNewContentFlag = false;
			break;
		}
	    $('#addContentModal #contentDescription').val(editorVal);
	}
	
	if(addNewContentFlag == true){
		var custProdId = $('#custProdIdManageContent').val();
		var gradeId = $('#gradeIdManageContent').val();
		var subtestId = $('#subtestIdManageContent').val();
		var objectiveId = $('#objectiveIdManageContent').val();
		var contentTypeId = $('#contentTypeIdManageContent').val();
		var contentTypeName = $('#contentTypeIdManageContent :selected').text();
		
		var $addContentModal = $('#addContentModal');
		$addContentModal.find('#custProdId').val(custProdId);
		$addContentModal.find('#gradeId').val(gradeId);
		$addContentModal.find('#subtestId').val(subtestId);
		$addContentModal.find('#objectiveId').val(objectiveId);
		$addContentModal.find('#contentType').val(contentTypeId);
		$addContentModal.find('#contentTypeName').val(contentTypeName);
		
		var formObj = $('#addNewContent').serialize();
		$.ajax({
			type : "POST",
			url : 'addNewContent.do',
			data : formObj,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.value >= 1) {
					win.closeModal(); 
					loadManageContentList();
					unblockUI();
					$('.mandatoryDescription').hide();
					$.modal.alert(strings['script.content.addSuccess']);
					
				} else {
					unblockUI();
					$.modal.alert(strings['script.user.saveError']);
				}
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.user.saveError']);
			}
		});
	}else{
		 unblockUI();
	}
}

//============Load grade id, name depending upon custProdId ===============
function populateGrade(){
	var custProdId = $('#custProdIdManageContent').val();
	if(custProdId != -1){
		var dataUrl = 'custProdId='+custProdId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateGrade.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#gradeIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

//============Load subtest id, name depending upon gradeId ===============
function populateSubtest(){
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	if(gradeId != -1){
		var dataUrl = 'gradeId='+gradeId+'&custProdId='+custProdId;;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateSubtest.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#subtestIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

//============Load Objective id, name depending upon subtestId ===============
function populateObjective(){
	var subtestId = $('#subtestIdManageContent').val();
	if(subtestId != -1){
		var gradeId = $('#gradeIdManageContent').val();
		var dataUrl = 'subtestId='+subtestId+'&gradeId='+gradeId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateObjective.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				populateDropdownByJson($('#objectiveIdManageContent'),data,1);
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}



//----------------------------Resetting Modal Form---------------------

/*function resetModalForm(formId)
{
	$("#"+formId).each (function() { this.reset(); });
	$("input#userStatus").removeAttr('checked');
	$("input#userStatus").change();
	$("#userRole option").removeAttr('selected');
	$("#userRole option").change();
	$("#userRole option").trigger('update-select-list');
}*/

//============To populate any drop down ===============
function populateDropdownByJson(elementObject,jsonDataValueName,plsSelectFlag,clearFlag){
	elementObject.empty();
	var option = "";
	if((typeof plsSelectFlag !== 'undefined') && (plsSelectFlag == 1)){
		option += "<option value='-1'>Please Select</option>";
	}
	
	if((typeof clearFlag === 'undefined')){
		if(jsonDataValueName != null){
			if(jsonDataValueName != ""){
				$.each(jsonDataValueName, function(index, data) {
					option += '<option value='+data.value+'>'+data.name+'</option>';
			    });
			}else{
				$.modal.alert(strings['script.common.empty']);
			}
		}
	}
	
	elementObject.html(option);
	elementObject.change();
	elementObject.trigger('update-select-list');
}

//=============================AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA START=============================

function loadManageContentList() {
	
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var objectiveId = $('#objectiveIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	
	var checkFirstLoad=true;
	var paramUrl = 'custProdId='+custProdId+'&subtestId='+subtestId+'&objectiveId='+objectiveId+'&contentTypeId='+contentTypeId+'&checkFirstLoad='+checkFirstLoad;
	
		blockUI();
		$.ajax({
			type : "GET",
			url : 'loadManageContent.do',
			data : paramUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				$('#contentTableDiv').show();
				if (data != null && data != "" && data.length > 14){
					$("#moreDiv").show(ANIMATION_TIME);
					$("#moreContents").removeClass("disabled");
					if($.browser.msie) $("#moreContents").removeClass("disabled-ie");
				} else {
					$("#moreDiv").hide(ANIMATION_TIME);
					$("#moreContents").addClass("disabled");
					if($.browser.msie) $("#moreContents").addClass("disabled-ie");
				}
				if ( data != null || data != ""){
					getContentDetails(checkFirstLoad,data);
					enableSorting(true);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
		enableSorting(true)
	}

function getContentDetails(checkFirstLoad,data) {
	var manageContent = '';
	if (checkFirstLoad) {
		$("#content_details").find("tr").remove();
	}
	$.each(data, function () { 
		var subHeaderVar = "";
	    if(this.subHeader != undefined){
	    	subHeaderVar = this.subHeader;
	    }
	    /* As per requirement, Performance level does not depend upon content */
	    /*
		manageContent += '<tr name="contentIdRow" id="contentIdRow" value="'+this.contentId+'">'
			         	+'<th scope="row"><h5>' + this.contentName +'</h5></th>'
						+'<th scope="row">' + subHeaderVar +'</th>'
						+'<th scope="row">' + this.gradeName +'</th>'
						+'<th scope="row">' + getPerformanceLevelName(this.performanceLevel) +'</th>'
						+'<td class="vertical-center" nowrap>'
						+'<span class="button-group compact" width="50px">'
						+'<a href="#" class="button icon-pencil edit-content with-tooltip" contentId="'+this.contentId+'" title="Edit"></a>'
						+'<a href="#" class="button icon-trash with-tooltip confirm delete-content" contentId="'+this.contentId+'" title="Delete"></a>'
						+'</span>'
						+'</td>'
					+'</tr>';
					*/
	    manageContent += '<tr name="contentIdRow" id="contentIdRow" value="'+this.contentId+'">'
					     	+'<th scope="row"><h5>' + this.contentName +'</h5></th>'
							+'<th scope="row">' + subHeaderVar +'</th>'
							+'<th scope="row">' + this.gradeName +'</th>'
							+'<td class="vertical-center" nowrap>'
							+'<span class="button-group compact" width="50px">'
							+'<a href="#" class="button icon-pencil edit-content with-tooltip" contentId="'+this.contentId+'" title="Edit"></a>'
							+'<a href="#" class="button icon-trash with-tooltip confirm delete-content" contentId="'+this.contentId+'" title="Delete"></a>'
							+'</span>'
							+'</td>'
						+'</tr>';
	});
	$("#content_details").append(manageContent);
	$("#report-list").trigger("update");
	$(".headerSortDown").removeClass("headerSortDown");
	$(".headerSortUp").removeClass("headerSortUp");
}

function enableContentSorting(flag) {
	if (flag) {
		var ContentTable = $("#report-list");
		ContentTable.trigger("update");
	}

}
//============================= AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA END=============================

//==To hide add,search buttons and table==========
function hideContentElements(){
	$('#refresh-content').hide();
	$('#addContentDiv').hide();
	$('#modifyStandardDiv').hide();
	$('#modifyRscDiv').hide();
	$('#modifyEdaDiv').hide();
	$('#modifyAttDiv').hide();
	$('#contentTableDiv').hide();
}

//==To show add,search, *description buttons==========
function showContentElements(){
	var objectiveId = $('#objectiveIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	
	if(gradeId != -1){
		if(contentTypeId == 'EDA'){
			$('#modifyEdaDiv').show();
		}else if(contentTypeId == 'ATT'){
			$('#modifyAttDiv').show();
		}
		
		if(subtestId != -1){
			if(contentTypeId == 'RSC'){
				$('#modifyRscDiv').show();
			}
			
			if(objectiveId != -1){
				if(contentTypeId == 'STD'){
					$('#modifyStandardDiv').show();
				}else if(contentTypeId == 'ACT' || contentTypeId == 'IND'){
					$('#refresh-content').show();
					$('#addContentDiv').show();
				}
			}
		}
	}
	
}

//==Get Performance level name==========
function getPerformanceLevelName(performanceLevel){
	var performanceLevelName = "";
	if(performanceLevel == 'A'){
		performanceLevelName = 'Pass'; 
	}else if(performanceLevel == 'B'){
		performanceLevelName = 'Did Not Pass';
	}else if(performanceLevel == 'U'){
		performanceLevelName = 'Undefined';
	}else if(performanceLevel == 'N'){
		performanceLevelName = 'DNR';
	}else if(performanceLevel == 'P'){
		performanceLevelName = 'Pass+';
	}
	return performanceLevelName;
}

/**
 * This js file is to manage content module - End
 * Author: Joy
 * Version: 1
 */