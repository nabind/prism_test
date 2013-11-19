/**
 * This js file is to manage role module
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */

$(document).ready(function() {
	 //alert('inside ready for mangae role js file');
	 // edit role in Manager Rele screen
	if($('.edit-role').length > 0) {
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-notes").parent().addClass("current");
		
		$('.edit-role').live("click", function(e) {
			//e.stopImmediatePropagation();
			viewRoleUsersDetails($(this).attr('roleId'));	
		});
		
		$('.associate-user').live("click", function() {
			if($('#searchUserId').val().replace(/ /g,'').length > 0){
				associateUserToRole($("#roleId").val(),$('#searchUserId').val());
			}else{
				$.modal.alert(strings['script.role.error']);
			}
		});
		
		$('.dissociate-user').live("click", function() {
			var userId = $(this).attr('userid');
			$.modal.confirm("Do you want to delete this user?" ,
					function () {
						//alert($("#roleId").val());
						//alert(userId);
						dissociateUserFromRole($("#roleId").val(), userId);
					},function() {//this function closes the confirm modal on clicking cancel button
					} 
				);
			
		});
	}
		//searchUserOnEditPopup()
});
// *********** END DOCUMENT.READY ************


//===================== Manage Report screen ===========================
function openModalRoleDetails(jsonData, roleId) {
	resetModalForm('editRoleForm');
	if($.browser.msie) {
		$('#search_icon').find('.font-icon').html('');
		$('#search_icon').find('.font-icon > .empty').hide();
	}
	var roleUserContent = '';
		$("#roleId").val(roleId);
		$.each(jsonData, function () {
			
			$("#roleId").val(this.roleId); 
			$("#roleName").val(this.roleName);
			$("#roleDescription").val(this.roleDescription);
			$("#userList").val(this.userList);
			
			$.each(this.userList, function (index, value) { 
				roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
									+'<td>' + value.userId +'</td>'
									+'<td>'+ value.userName +'</td>'
									+'<td class=""><div class="controls">'
										+'<span class="button-group compact children-tooltip">'
											+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="Remove from association">Delete</a>'
										+'</span>'
									+'</div></td>'
								+'</tr>';
			});
			$("#role_users_popup").html(roleUserContent);	
		});
		
	$("#roleModal").modal({
		title: 'Edit Role',
		resizable: false,
		draggable: false,
		height: 350,
		width: 550,
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
					//updateRoleDetails($(this).attr('roleId'),$('#roleName').val(),$('#roleDescription').val());
					clickMe(e);
					updateRoleDetails($(".edit-role-form"), win);
				}
			}
		}
	});		
	unblockUI();
}

//----------------------------Update role details---------------------
function updateRoleDetails(form, win) {
	//var dataURL = 'roleId='+roleId+'&roleName='+roleName+'&roleDescription='+roleDescription;
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateRole.do',
		data : form.serialize(),
		dataType: 'html',
		cache:false,
		success : function(data) {
			var obj = jQuery.parseJSON(data);
			win.closeModal(); 
			if(obj.status == 'Success') {
				$.modal.alert(strings['script.role.update']);
				var roleId = $("#roleId").val();
				
				var newRow = '<tr id="'+roleId+'">\
								<th scope="row"><span class="reportName">'+$("#roleName").val()+'</span><br> </th>\
								<td class="vertical-center">'+$("#roleDescription").val()+'</td>\
								<td class="vertical-center">\
									<span class="button-group compact">\
										<a href="#"	class="button icon-pencil with-tooltip edit-role" title="Edit" roleId="'+roleId+'"></a>\
										<a href="#" roleId="'+roleId+'" roleName="'+$("#roleName").val()+'" class="button icon-trash with-tooltip confirm delete-Role"\
											title="Delete"></a>\
									</span>\
								</td>\
							</tr>';
				
				//$("#roleList").find("#"+roleId).remove();
				$("#"+roleId).replaceWith(newRow);
				//$("#roleList").append(newRow);
			} else {
				$.modal.alert(strings['script.user.saveError']);
			}
			unblockUI();
		},
		error : function(data) {
			$.modal.alert(strings['script.user.saveError']);
			unblockUI();
		}
	});
}
//----------------------------Update row if update success ---------------------

function viewRoleUsersDetails(roleId) {
	//alert($(form).serialize());
	blockUI();
	var dataURL = "roleId="+roleId;
	$.ajax({
		type : "GET",
		url : 'editRole.do',
		data : dataURL,
		dataType: 'json',
		cache:false,
		success : function(data) {
		//var obj = jQuery.parseJSON(data);
			//alert("data:"+data[0].roleName);
			openModalRoleDetails(data, roleId);
		},
		error : function(data) {
			unblockUI();
			$.modal.alert(strings['script.user.saveError1']);
		}
		
	});
}

// ============================= Associate user from edit role =============================
	
function associateUserToRole(roleId, userName) {
		blockUI();
		var dataURL = 'roleId='+roleId+'&userName='+userName;
		$.ajax({
			type : "GET",
			url : 'associateUser.do',
			data : dataURL,
			dataType: 'json',
			cache:false,
			success : function(data) {
				//var obj = jQuery.parseJSON(data);
				//alert('Success, User Associated To Role*** opening modal window');
				//openModal2(data);
				if(data != null) {
					if(data != null && data.status == 'Fail') {
						unblockUI();
						$.modal.alert(strings['script.role.userNotFound']);
					} else if(data != null && data.status == 'RoleAlreadyTagged') {
							unblockUI();
							$.modal.alert(strings['script.role.userAlreadyTagged']);
					}else {
						var roleUserContent = '';
						
						$.each(data, function () {
								//$("#userList").val(this.userList);
										
							$.each(this.userList, function (index, value) { 
								roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
													+'<td>' + value.userId +'</td>'
													+'<td>'+ value.userName +'</td>'
													+'<td class=""><div class="controls">'
														+'<span class="button-group compact children-tooltip">'
															+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="Remove from association">Delete</a>'
														+'</span>'
													+'</div></td>'
												+'</tr>';				
							});
							$("#role_users_popup").html(roleUserContent);
							
						});	
						unblockUI();
					}
				} else {
					$.modal.alert(strings['script.role.userNotFound']);
					unblockUI();
				}
				
			},
			error : function(data) {
				$.modal.alert(strings['script.user.deleteError']);
				unblockUI();
			}
		});
	}

// ============================= Dissociate user from edit role =============================
	
function dissociateUserFromRole(roleId, userId) {
		blockUI();
		var dataURL = 'roleId='+roleId+'&userId='+userId;
		$.ajax({
			type : "GET",
			url : 'dissociateUser.do',
			data : dataURL,
			dataType: 'json',
			cache:false,
			success : function(data) {
				//alert('Success, User Dissociated From Role*** opening modal window');
				//openModal2(data);
				var roleUserContent = '';
				
				$.each(data, function () {
						//$("#userList").val(this.userList);
								
					$.each(this.userList, function (index, value) { 
						roleUserContent += '<tr id ='+ value.userId +' class="reportName" >'
											+'<td>' + value.userId +'</td>'
											+'<td>'+ value.userName +'</td>'
											+'<td class=""><div class="controls">'
												+'<span class="button-group compact children-tooltip">'
													+'<a href="#" roleid="'+this.roleId+'" userid="'+value.userId+'" class="button dissociate-user icon-delete" title="Remove from association">Delete</a>'
												+'</span>'
											+'</div></td>'
										+'</tr>';
					});
					$("#role_users_popup").html(roleUserContent);
					unblockUI();
				});
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
				unblockUI();
			}
		});
	}
//=============================DELETE ROLES ON CLICK======================
$('.delete-Role').live("click", function() {

    var row = $(this);
	var roleId = $(this).attr("roleId");
	var roleName = $(this).attr("roleName");
	$.modal.confirm("Confirm "+$(this).attr("roleName")+" delete?" ,
		function () {
			deleteRoleDetails(roleId, roleName,row);
		},function() {//this function closes the confirm modal on clicking cancel button
		} 
	);
	
});	
//=============================AJAX CALL TO DELETE ROLES FROM DB TABLES=============================
	function deleteRoleDetails(roleId, roleName, row) {
			blockUI();
			$.ajax({
				type : "GET",
				url : 'deleteRole.do',
				data : "roleId="+roleId,
				dataType: 'html',
				cache:false,
				success : function(data) {
					var obj = jQuery.parseJSON(data);
					//win.closeModal(); 
					if(obj.status == 'Success') {
						$.modal.alert('Role '+roleName+' deleted successfully');				
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

