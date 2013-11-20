/**
 * This js file is to manage content module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
	
$(document).ready(function() {
	$('#custProdIdManageContent').live('change',function(){
		populateGrade();
	}); 
	
	$('#gradeIdManageContent').live('change',function(){
		populateSubtest();
	});
	
	$('#subtestIdManageContent').live('change',function(){
		populateObjective();
	}); 
	
	$('#addContent').live("click", function() {
		if ($('#objectiveIdManageContent').val() == null || $('#objectiveIdManageContent').val() == "" || $('#objectiveIdManageContent').val() == "0" ) {
			$.modal.alert(strings['script.content.addContent']);
		}
		else {
			resetModalForm("addNewContent");
			//resetModalForm("addNewUser");
			openContentModalToAdd();
		}
	});
	
});

//============Open Modal to Add Content ===============
function openContentModalToAdd() {
    $("#addNewContent").validationEngine({promptPosition : "centerRight", scroll: false});
	manageIconIE('icon-star');
	
	$("#addContentModal").modal({
		title: 'Add Content',
		height: 500,
		width: 780,
		resizable: true,
		draggable: false,
		onOpen: setCKEditor(),
		buttons: {
			'Cancel': {
				classes: 'glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);
							$('#addNewContent').validationEngine('hide');
							if($.browser.msie) setTimeout("hideMessage()", 300);
							win.closeModal(); 
						}
					},
			'Save': {
				classes: 'blue-gradient glossy mid-margin-left',
				click: function(win,e) {
							clickMe(e);	
							 if($("#addNewContent").validationEngine('validate') 
									 && ($("#addNewContent #imgHolder > #validated").hasClass("validated"))){
								$('#addNewContent').validationEngine('hide');
								addNewContent($("#addNewContent"), win);
							 }		
							//addNewContent($("#addNewContent"), win);
						}
					}
				}
		});

}

function setCKEditor(){
	$('.manage-content-textarea').each(function(){
		//CKEDITOR.instances[$(this).attr('id')].destroy(true);
		if(CKEDITOR.instances[$(this).attr('id') ] == undefined) {
			CKEDITOR.inline( $(this).attr('id') );
			/*CKEDITOR.replace($(this).attr('id'),{
				fullPage:true
			});*/
		}
		
	});
}

//============Open Modal to Add Content ===============
function addNewContent(form, win) {
	blockUI();
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var objectiveId = $('#objectiveIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentType = $('#contentTypeIdManageContent :selected').text();
	
	//$('#addContentModal').find('#custProdId')
	var $addContentModal = $('#addContentModal');
	$addContentModal.find('#custProdId').val(custProdId);
	$addContentModal.find('#gradeId').val(gradeId);
	$addContentModal.find('#subtestId').val(subtestId);
	$addContentModal.find('#objectiveId').val(objectiveId);
	$addContentModal.find('#contentTypeId').val(contentTypeId);
	$addContentModal.find('#contentType').val(contentType);

	for(name in CKEDITOR.instances)	{
		var editorVal = CKEDITOR.instances[name].getData();
	    $('#addContentModal #contentDescription').val(editorVal);
	}
	var formObj = $('#addNewContent').serialize();
	
	$.ajax({
		type : "POST",
		url : 'addNewContent.do',
		data : formObj,
		dataType: 'json',
		cache:false,
		success : function(data) {
			if(data.value == 1) {
				win.closeModal(); 
				//TODO loadContentDetails();
				$.modal.alert(strings['script.content.addSuccess']);
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
}

//============Load grade id, name depending upon custProdId ===============
function populateGrade(){
	var custProdId = $('#custProdIdManageContent').val();
	if(custProdId != 0){
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
	populateDropdownByJson($('#subObjMapIdManageContent'),null,1,'clear');
	populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
}

//============Load subtest id, name depending upon gradeId ===============
function populateSubtest(){
	var gradeId = $('#gradeIdManageContent').val();
	if(gradeId != 0){
		var dataUrl = 'gradeId='+gradeId;
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
	populateDropdownByJson($('#objectiveIdManageContent'),null,1,'clear');
}

//============Load Objective id, name depending upon subtestId ===============
function populateObjective(){
	var subtestId = $('#subtestIdManageContent').val();
	if(subtestId != 0){
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
		option += "<option value='0'>Please Select</option>";
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