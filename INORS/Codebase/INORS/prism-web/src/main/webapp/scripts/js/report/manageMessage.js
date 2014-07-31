/**
 * This js file is to manage report module
 * Author: Joy
 * Version: 1
 */
$(document).ready(function() {

	$('#searchMessage').live('click',function(){
		loadManageMessage();
	});
	
	$('#custProdId').live('change',function(){
		$('#reportMessage').empty();
		showHideCopyMessageDiv(0);
	}); 
	
	$('#editManageMessageButtonSave').live('click',function(){
		var paramObj = {};
		paramObj.custProdId = $('#custProdId').val();
		saveManageMessage(paramObj);
	});
	
	$('#copyMessage').live('click',function(){
		openCopyMessageModal();
	});
	
	// selecting left icon as current
	if($('.manage-message').length > 0) {
		// hiding right menu
		$('.clearfix').addClass('menu-hidden');
		
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-dashboard").parent().addClass("current");
	}

});
// *********** END DOCUMENT.READY ************

//===================================Manage Message Screen=====================
	function loadManageMessage(){
		var formObj = $('#reportMessageSearchForm').serialize();
		blockUI();
		$.ajax({
			type : "POST",
			url : 'ajaxJSP/loadManageMessage.do',
			data : formObj,
			dataType: 'html',
			cache:false,
			success : function(data) {
				$("#reportMessage").empty();
				$("#reportMessage").html(data);
				$('.manage-rpt-textarea').each(function(){
					CKEDITOR.inline( $(this).attr('id') );
				});
				showHideCopyMessageDiv(1);
				$(".switch.tiny").first().css("top", "32px");
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
	
	function saveManageMessage(paramObj){
		var reportId = $('#reportId').val();
		for(name in CKEDITOR.instances)	{
			var editorVal = CKEDITOR.instances[name].getData();
		    var manageMessageTOListNo = name.split("-")[2];
		    $('#hiddenEditor-'+reportId+'-'+manageMessageTOListNo).val(editorVal);
		}
		$('.custProdIdHidden').val(paramObj.custProdId);
		var formObj = $('#editManageMessageForm').serialize();
		
		blockUI();
		$.ajax({
			type : "POST",
			url : 'ajaxJSP/saveManageMessage.htm',
			data : formObj,
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				if(obj.status == "1"){
					$.modal.alert("Saved Successfully");
				}else{
					$.modal.alert(strings['script.common.error1']);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});
	}
	
	function openCopyMessageModal(){
		$("#copyMessageModal").modal({
			title: 'Copy Report Message',
			width:350,
			draggable: false,
			resizable: false,
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
						var paramObj = {};
						paramObj.custProdId = $('#custProdIdModal').val();
						saveManageMessage(paramObj);
					}
				}
			}
		});
	}
	
	function showHideCopyMessageDiv(param){
		if(param == 0){
			$('#copyMessageDiv').hide();
		}else if(param == 1){
			$('#copyMessageDiv').show();
		}
		
	}
	