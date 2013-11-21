/**
 * This js file is to manage content module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
	
$(document).ready(function() {
	
	if($("#contentTable").length > 0) {
		var allContents = $(".content-list-all");
		if(allContents != null && allContents.length > 0) {
			$('#report-list').tablesorter({
				headers: {
					5: { sorter: false }
				},
				sortList: [[0,1]]
			});
		}
	}
	
	$('#custProdIdManageContent').live('change',function(){
		refreshContent();
		populateGrade();
	}); 
	
	$('#gradeIdManageContent').live('change',function(){
		refreshContent();
		populateSubtest();
	});
	
	$('#subtestIdManageContent').live('change',function(){
		refreshContent();
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

//==To remove button and table==========
function refreshContent(){
	$('#refresh-content').hide();
	$('#contentTableDiv').hide();
}

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
							 addNewContent($("#addNewContent"), win);
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
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	
	var $addContentModal = $('#addContentModal');
	$addContentModal.find('#custProdId').val(custProdId);
	$addContentModal.find('#gradeId').val(gradeId);
	$addContentModal.find('#subtestId').val(subtestId);
	$addContentModal.find('#objectiveId').val(objectiveId);
	$addContentModal.find('#contentType').val(contentTypeId);
	$addContentModal.find('#contentTypeName').val(contentTypeName);

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
				loadManageContentList();
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
				$('#refresh-content').show();
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

//=============================AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA START=============================


$('#refresh-content').live('click',function(){
	loadManageContentList();
});

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
				if (data != null && data.length > 14){
					$(".pagination").show(200);
				} else {
					$(".pagination").hide(200);
				}
				getContentDetails(checkFirstLoad,data);
				enableSorting(true);
				if (data != null && data.length > 14){
					$("#moreContents").removeClass("disabled");
					if($.browser.msie) $("#moreContents").removeClass("disabled-ie");
				} else {
					$("#moreContents").addClass("disabled");
					if($.browser.msie) $("#moreContents").addClass("disabled-ie");
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert('Error while searching the content');
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
	    
		manageContent += '<tr name="articleId" id="articleId" value="'+this.articleId+'">'
			            +'<input type="hidden" class="contentId" name="contentId" value="'+this.contentId+'">'
			            +'<input type="hidden" class="articleId" name="articleId" value="'+this.articleId+'">'
						+'<th scope="row"><h5>' + this.contentName +'</h5></th>'
						+'<th scope="row">' + this.subHeader +'</th>'
						+'<th scope="row">' + this.gradeName +'</th>'
						+'<th scope="row">' + this.profLevel +'</th>'
						+'<td class="vertical-center" nowrap>'
						+'<span class="button-group compact" width="50px">'
						+'<a href="#" class="button icon-pencil edit-content with-tooltip" title="Edit"></a>'
						+'<a href="#" class="button icon-trash with-tooltip confirm delete-Content" title="Delete"></a>'
						+'</span>'
						+'</td>'
					+'</tr>'
	});
	$('#contentTableDiv').show();
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

var tempScrollTop, currentScrollTop = 0
$("#moreContents").click(function() {
	var custProdId = $('#custProdIdManageContent').val();
	var gradeId = $('#gradeIdManageContent').val();
	var subtestId = $('#subtestIdManageContent').val();
	var objectiveId = $('#objectiveIdManageContent').val();
	var contentTypeId = $('#contentTypeIdManageContent').val();
	var contentTypeName = $('#contentTypeIdManageContent :selected').text();
	var lastid = $("#contentTable tr:last").attr("value");
	var checkFirstLoad=false;
	var param = 'custProdId='+custProdId+'&subtestId='+subtestId+'&objectiveId='+objectiveId+'&contentTypeId='+contentTypeId+'&lastid='+lastid+'&checkFirstLoad='+checkFirstLoad;
	currentScrollTop = $("#contentTable").scrollTop();
	if(!$(this).hasClass('disabled')) {
		var callingAction = "";
		var lastid = $('#lastid').val();
			callingAction = 'loadManageContent.do';
		blockUI();
		$.ajax({
			type : "GET",
			url : callingAction,
			data : param,
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
//============================= AJAX CALL TO TO POPULATE CONTENTS FROM DB TABLES THROUGH PACKAGE BY ARUNAVA END=============================



