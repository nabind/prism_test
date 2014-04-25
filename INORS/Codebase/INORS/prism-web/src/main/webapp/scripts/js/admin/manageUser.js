	/**
	 * This js file is to manage user module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
$(window).load(function() {
	if($('#educationTab').val()!= "" && $('#educationTab').val()=="educationUserTab" ){
		$('.clearfix').addClass('menu-hidden');
   	    $("ul#shortcuts li").removeClass("current");
	    $(".shortcut-medias").parent().addClass("current");
		loadEduCenterUsers();
	}
});

$(document).ready(function() {
	if($('input#educationTab').val()!= null && $('input#educationTab').val()=="educationUserTab" ){
		$('.clearfix').addClass('menu-hidden');
   	    $("ul#shortcuts li").removeClass("current");
	    $(".shortcut-medias").parent().addClass("current");
	}
	
	$("#addNewUser").validationEngine({promptPosition : "centerRight", scroll: false});
	$("#editUser").validationEngine({promptPosition : "centerRight", scroll: false});
	 
	$('#treeViewForOrg').customScroll({
		horizontal : true,
		showOnHover : false,
		animate : false
	});

	$('.edit-User').live("click", function() {
		resetModalForm("editUser");
		resetModalForm("addNewUser");
		openUserModaltoEdit($(this).attr("id"),$(this).attr("tenantId"));
	});	
	$('#addUser').live("click", function() {
		
		if ($('#addUser').attr("orgLevel") == null || $('#addUser').attr("orgLevel") == "" ) {
			$.modal.alert(strings['script.user.adduser']);
		}
		else {
			resetModalForm("editUser");
			resetModalForm("addNewUser");
			if ($('#addUser').attr("orgLevel") == strings['user.not.added']) {
				$.modal.alert(strings['script.user.examinerUser']);
			}
			else {
				openUserModaltoAdd($('#addUser').attr("tenantId"),$('#addUser').attr("orgLevel") );
			}
		}
	});	
	$('.delete-User').live("click", function() {
	    var row = $(this);
		var userId = $(this).attr("id");
		var userName = $(this).attr("userName");
		$.modal.confirm("Do you want to delete this user?" ,
			function () {
				deleteUserDetails(userId,userName, row);
				enableSorting(true);
			},function() {//this function closes the confirm modal on clicking cancel button
			} 
		);
		
	});
	
	//Changed for TD 77443 - By Joy
	if($('#educationTab').val()!= "" && $('#educationTab').val()=="educationUserTab" ){
		$(".login-as").live("click", function(e) {
			loginAs(e,$(this));
		});	
	}
	else
		{
		$(".login-as").live("click", function(e) {
			loginAs(e,$(this));
		});	
	}
	
	
	//====================THIS METHOD POPULATES THE COUNTRY IN THE PROFILE PAGE=============
	$('#tabContactDetails').live("click",function(e){
		e.stopImmediatePropagation();
		var userCountry= $(".addressContainer #userCountry").val();
		$("#countryList option").each(function(){
			if($(this).val()==userCountry){
				$(this).addClass("selected");
				$(this).parent().siblings(".select-value").text(userCountry);
			}
					
		});
		
	});
	//====================THIS METHOD POPULATES THE COUNTRY IN THE FIRST TIME LOGIN PAGE (FOR CHANGE PASSWORD)=============
	var isfirst=true;
	$(".passwordContainer #password").live("focus",function(e){
			e.stopImmediatePropagation();
		var userCountry= $(".addressContainer #userCountry").val();
		if(isfirst){
						$("#countryList option").each(function(){
						if($(this).val()==userCountry){
							$(this).addClass("selected");
							$(this).parent().siblings(".select-value").text(userCountry);
						}
								
					});
					
					$("span.drop-down span").each(function(){
						if($(this).text()==userCountry){
							$(this).addClass("selected");
							}
					});
		         isfirst=false;
		}
		
		
	});
	//====================THIS METHOD POPULATES THE HIDDEN "countyrList" TEXT FIELD WITH THE VALUE OF THE COUNTRY SELECTED FROM THE DROPDOWN=============
	
	$("select#countryList").live("change",function(e){
			e.stopImmediatePropagation();
			$(".addressContainer #userCountry").val($(this).siblings(".select-value").text());	
          //alert($(".addressContainer #userCountry").val());			
	});
	
	$('#eduCenterId').live('change',function(){
		$("#eduCenterUsersDetails").empty();
		loadEduCenterUsers();
	}); 
});

	//Changed for TD 77443 - By Joy
	function loginAs(event,$obj){
		if(!$obj.hasClass('disabled')){
			$obj.addClass('disabled');
			location.href = 'j_spring_security_switch_user?j_username='+$obj.attr('param');
		}
	}
	//===================================Education Center User Details Screen=====================
	function loadEduCenterUsers(){
		var eduCenterId = $('#eduCenterId').val();
		var eduCenterName = $("#eduCenterId :selected").text();
		$('#addUser').attr('tenantId',eduCenterId);
		$('#addUser').attr('tenantName',eduCenterName);
		var dataUrl = 'eduCenterId='+eduCenterId+'&eduCenterName='+eduCenterName+'&searchParam=""&lastEduCenterId_username=""';
		blockUI();
		
		$.ajax({
			type : "GET",
			url : 'loadEduCenterUsers.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if (data != null && data.length > 14){
					$(".pagination").show(200);
				} else {
					$(".pagination").hide(200);
				}
				getUserDetails(true, data);
				enableSorting(true);
				$("tbody#user_details").removeClass("loader big");				
				if (data != null && data.length > 14){
					$("#moreUser").removeClass("disabled");
					if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
				} else {
					$("#moreUser").addClass("disabled");
					if($.browser.msie) $("#moreUser").addClass("disabled-ie");
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
	
	function openUserModaltoAdd(tenantId,orgLevel) {	
	    $("#addUserRole").html("");
		$("input#tenantId").val(tenantId);
		$("input#orgLevel").val(orgLevel);
		if($("#purpose").val()=="eduCenterUsers"){
			$("#addNewUser").validationEngine({promptPosition : "centerRight", scroll: false});
		}
		manageIconIE('icon-star');
		blockUI();
		$.ajax({
			type : "GET",
			url : "getRoleOnAddUser.do",
			data : 'orgLevel='+orgLevel+'&purpose='+ $("#purpose").val(),
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				createRoleListOnAdd(data, orgLevel);
				$("#addUserModal").modal({
					title: 'Add User to ' + $('#addUser').attr("tenantName"),
					height: 410,
					width: 370,
					resizable: false,
					draggable: false,
					onOpen: CheckUserNameAndEnableStaus(),//CheckUserNameAvailability("#addNewUser #userId"),
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
										$('#addNewUser').validationEngine('hide');
										if($.browser.msie) setTimeout("hideMessage()", 300);
										win.closeModal(); 
									}
								},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);	
									 if($("#addNewUser").validationEngine('validate') 
											 && ($("#addNewUser #imgHolder > #validated").hasClass("validated"))){
										$('#addNewUser').validationEngine('hide');
										if($("input[rel^='userStatusCheck']").closest("span").hasClass("checked")){
											if ($("input[rel^='userStatusCheck']").val()=='on')
											$("input[rel^='userStatus']").val($("input[rel^='userStatusCheck']").val());
											else
											$("input[rel^='userStatus']").val('on');
										}	else{
											$("input[rel^='userStatus']").val('off');
										}
										addUserDetails($(".add-User-form"), win);
										$("#moreUser").removeClass("disabled");
										if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
									 }										
									}
								}
							}
					});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
			}
		});
				
	}
	
	function hideMessage() {
		$('#addNewUser').validationEngine('hide');
	}
	
	function CheckUserNameAndEnableStaus()
	{
			enableStaus("addUserModal","userStatusCheck");
			$("input[rel^='userStatus']").val($("input[rel^='userStatusCheck']").val());
			CheckUserNameAvailability("input[rel^='userId']");		
			if($.template.ie7) {$("#imgHolderContainer").css("line-height", "25px");}
	}
	
	//======================VALIDATION OF THE USER NAME FIELD IN ADD USER MODAL ====================================
	function validateUserId(modalFormId,userNameId) {
		$("#"+modalFormId+" "+"#"+userNameId).blur(function() {
		      var username = "username=" + $(this).val();
			  if($(this).val() != "") {
				  showLoadingImage();
				  $.ajax({
						type : "GET",	
						url : "regn/checkusername.do",
						data : username,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data.available == "true") {
								removeLoadingImage();//This method is defined in parent.js
								showAvailability();//This method is defined in parent.js
							}
							else if (data.available == "false") {
								removeLoadingImage();//This method is defined in parent.js
								showUnAvailability();//This method is defined in parent.js
							}
						},
						error : function(data) {						
							$.modal.alert(strings['script.common.error1']);
						}
				  });
			}/*
			else {
				$("span#imgHolder").html('<!--<img src="themes/acsi/img/standard/loaders/loading16.gif">-->');
				$("span#imgHolder").html('<span class="icon-user icon-size2 icon-red" style="color: red;">Please provide valid Username</span>');
			}*/
		});	
		
	}

	//======================ENABLES THE CHECKBOX IN MODAL=====================================
	function enableStaus(modalId,checkboxId)
	{
		$("#"+modalId+" "+"#"+checkboxId).closest("span").addClass("checked");
	}
	
	//======================OPEN EDIT USER SCREEN==========================================
	function openUserModaltoEdit(userId,tenantId) {
	var row = $("#"+tenantId + '_' +userId);
	var nodeid = "tenantId=" + userId+'&purpose='+ $("#purpose").val();	
	if($("#purpose").val()=="eduCenterUsers"){
		$("#editUser").validationEngine({promptPosition : "centerRight", scroll: false});
	}
	manageIconIE('icon-star');
	blockUI();
	$.ajax({
			type : "GET",
			url : "getEditUserData.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				createRoleListOnEdit(data);
				$("input#Id").val(data[0].userId);
				$("input#userName").val(data[0].userDisplayName);
				$("input#userId").val(data[0].userName);
				$("label#userid").text(data[0].userName);
				$("input#validation-email").val(data[0].emailId);
				var roles = data[0].availableRoleList;
				
				$("#userRole option").removeAttr('selected');
				
				if(typeof roles != "undefined") {
					$.each(roles, function(index, value) {
						//alert(roles[index].roleName);
						$("#userRole option").each(function() {
							var flag = isFoundInList($(this).val(), data[0].masterRoleList)
							// if($(this).val() == roles[index].roleName) { // TODO : check logic
							if (flag == true) {
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
				
				$('#userRole').trigger('update-select-list').change();
				
				//$("input#userStatus").removeAttr('checked');
				if(data[0].status == 'AC') {
					$("input#userStatus").attr('checked', true);
				} else {
					$("input#userStatus").removeAttr('checked');
				}
				$("input#userStatus").change();
				//$("input#userName").val(this.userName);
				$("#userModal").modal({
					title: 'Edit User',
					height: 370,
					width: 370,
					resizable: false,
					draggable: false,
					buttons: {
						'Cancel': {
							classes: 'glossy mid-margin-left',
							click: function(win,e) {
								$('#editUser').validationEngine('hide');
								if($.browser.msie) setTimeout("hideMessage()", 300);
								clickMe(e);
								win.closeModal(); 
								
								}
						},
						'Save': {
							classes: 'blue-gradient glossy mid-margin-left',
							click: function(win,e) {
								clickMe(e);
								if ($("#editUser").validationEngine('validate')) {
									if ($("input[rel^='editPwd']").val() == $("input[rel^='editConfPwd']").val()) {
										$('#editUser').validationEngine('hide');
										updateUserDetails($(".edit-User-form"), win, row);
									} else {
										$.modal.alert(strings['script.user.passwordMismatch']);
									}
								}
							}						
						}
					}
				});					
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error1']);
			}
		})	
	}

	//Fix for TD 77811 - By Joy
	//======================CREATE MASTER ROLE LIST ON EDIT USER==========================================	
	function createRoleListOnEdit(data) {
		
	    $("#userRole").find("option").remove();
		var availableRole = "";
		$.each(data[0].availableRoleList, function(index, value){
			if(data[0].availableRoleList[index].roleDescription != 'Class Admin user'){
				availableRole += '<option value="'+ data[0].availableRoleList[index].roleName +'" >' +data[0].availableRoleList[index].roleDescription+'</option>';
			}
		});
		$("#userRole").append(availableRole);		
		$("#userRole").change(function(){
			$("#userRole option[value='ROLE_USER']").attr('selected', true);
		});
		//alert(masterRole);
	}
	
	//Fix for TD 77811 - By Joy
	//======================CREATE MASTER ROLE LIST ON ADD USER==========================================	
	function createRoleListOnAdd(data,orgLevel) {
	    var masterRole = "";
		$.each(data, function(index, value){
			if(orgLevel == '4'){
				if(data[index].roleDescription != 'Admin user'){
					masterRole += '<option value="'+ data[index].roleName +'" '+data[index].defaultSelection+'>' +data[index].roleDescription+'</option>';
				}
			}else{
				masterRole += '<option value="'+ data[index].roleName +'" '+data[index].defaultSelection+'>' +data[index].roleDescription+'</option>';
			}
			
		});
		//alert(masterRole);
		$("#addUserRole").empty().append(masterRole);
		$("#addUserRole").change(function(){
			$("#addUserRole option[value='ROLE_USER']").attr('selected', true);
		});
		$('#addUserRole').trigger('update-select-list');
	}	
	
	//=========================SAVE EDIT USER DETAILS========================================
	function updateUserDetails(form, win, row) {
	blockUI();
	$.ajax({
		type : "POST",
		url : 'updateUser.do',
		data : form.serialize(),
		dataType : 'html',
		cache : false,
		success : function(data) {
			unblockUI();
			var obj = jQuery.parseJSON(data);
			if (obj.status == 'equalsUserName') {
				$.modal.alert(strings['script.user.passwordLikeUsername']);
			} else if (obj.status == 'invalidPwd') {
				$.modal.alert(strings['script.user.passwordPolicy']);
			} else if (obj.status == 'LDAP_ERROR') {
				$.modal.alert(obj.message);
			} else if (obj.status == 'Success') {
				win.closeModal();
				$.modal.alert(strings['script.user.updateSuccess']);
				var purpose = $("#purpose").val();
				if (purpose == 'eduCenterUsers') {
					loadEduCenterUsers();
				} else {
					//Uncommented updateRowValues(row) for TD 77428 - By Joy
					updateRowValues(row);
					//Added to load data after edit
					var id = $("#treeViewForOrg a").parent().attr("id");
					//fetchAllUsers(id,"getUserDetails.do");//Blocked as after search and edit the user should retain there
					//Change end here
				}
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
		
		//=======================DELETE USER DETAILS====================
	function deleteUserDetails(userId, userName, row) {
	
		$.ajax({
			type : "GET",
			url : 'deleteUser.do',
			data : 'Id='+userId+'&userName='+userName+'&purpose='+ $("#purpose").val(),
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				//win.closeModal(); 
				if(obj.status == 'Success') {
					$.modal.alert(strings['script.user.deleteSuccess']);				
					deleteRowValues(row);
				} else {
					$.modal.alert(strings['script.user.deleteError']);
				}
			},
			error : function(data) {
				$.modal.alert(strings['script.user.deleteError']);
			}
		});
	}
	
	//-------------------------------------ADDING USER DETAILS--------------------------------
	function addUserDetails(form, win) {
		blockUI();
		var patt=/^(\d+.*|-\d+.*)/g;
		var username = $("input[rel^=userId]").val();
		var pwd = $("#password1").val();
		if(pwd.indexOf(username) != -1) {
			unblockUI();
			$.modal.alert(strings['script.user.passwordPartUsername']);
		} 
		/*else if (patt.test(username)){
			unblockUI();
			$.modal.alert(strings['script.user.useridStartNumber']);
		}*/ else {
		var dataUrl = form.serialize()+'&AdminYear='+$("#AdminYear").val()+'&purpose='+ $("#purpose").val()+'&eduCenterId='+ $("#eduCenterId").val();
		$.ajax({
			type : "POST",
			url : 'addNewUser.do',
			data : dataUrl,
			dataType: 'html',
			cache:false,
			success : function(data) {
				var obj = jQuery.parseJSON(data);
				if (obj.status == 'equalsUserName')	{
					$.modal.alert(strings['script.user.passwordLikeUsername']);
					unblockUI();
				}
				else if(obj.status == 'invalidPwd'){
					$.modal.alert(strings['script.user.passwordPolicy']);
					unblockUI();
				}
				else if(obj.status == 'LDAP_ERROR'){
					$.modal.alert(obj.message);
					unblockUI();
				}
				else if(obj.status != 'Faliure') {
					win.closeModal(); 
					var purpose = $("#purpose").val();
					if(purpose == 'eduCenterUsers'){
						loadEduCenterUsers();
					}else{
						$.ajax({
							type : "GET",
							url : "getUserDetails.do",
							data : form.serialize() + "&AdminYear=" + $("#AdminYear").val(),
							dataType : 'json',
							cache:false,
							success : function(data) {
								if (data != null && data.length > 14){
									$(".pagination").show(200);
								} else {
									$(".pagination").hide(200);
								}
								getUserDetails(true, data);
								enableSorting(true);
								unblockUI();
							},
							error : function(data) {
								unblockUI();
							}
						});
					}
					$.modal.alert(strings['script.user.adduserSuccess']);
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
	}
	
	//----------------------------UPDATE ROW IF UPDATE SUCCESS ---------------------
	
	// Modification done by Joy for TD 77428 - ISTEP Prism_Manage User_On editing an existing user the change is not getting reflected immediately.
	function updateRowValues(row) {
		var roletag = '<small class="tag _BGCOLOR_ _CLASS_ ">_VALUE_</small>'
		var completeRoleTagHtml = '';
	
		//var statusClass = 'tag';
		var statusClass = 'with-tooltip tooltip-left';
		var statusVal = '';
		//alert($("#userModal #userStatus").attr('checked'));
		if($("#userModal #userStatus").attr('checked') ==  'checked' || $("#userModal #userStatus").attr('checked') ==  true) {
			statusClass = statusClass + ' enable';
			statusVal = 'Enabled';
		} else {
			statusClass = statusClass + ' disable';
			statusVal = 'Disabled';
		}
		//alert(statusClass);
		//alert($("#userModal #userName").val());
		//row.find('th:eq(0)').html( $("#userModal #userId").val());
		//row.find('td:eq(0)').html( $("#userModal #userName").val());
		//row.find('td small:eq(0)').html( statusVal);
		row.find('th span:eq(0)').removeClass();
		row.find('th span:eq(0)').addClass(statusClass);
		row.find('th span:eq(0)').attr('title', statusVal)
		
		/*row.find('td:eq(1) small').removeClass();
		row.find('td:eq(1) small').addClass(statusClass);*/
		var hasAdminRole = false;
		$("#editUser #userRole option").each(function() {
			if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
				var tokens = $(this).val().split("_");
				if (tokens[1] == "ADMIN") {
					hasAdminRole = true;
				}
			}
		});
		if(hasAdminRole == false) {
			$("#editUser #userRole option").each(function() {
				if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
				var roleClass = 'role' + ' ' + $(this).val();
				var roleTagTemp = roletag +'<br/>';
				roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
				roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).text());
				
				if($(this).val()=="ROLE_ACSI")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "blue-bg");
				else if ($(this).val()=="ROLE_CTB")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "green-bg");
				else if ($(this).val()=="ROLE_SCHOOL")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "anthracite-bg");
				else if ($(this).val()=="ROLE_CLASS")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "grey-bg");	
				else if ($(this).val()=="ROLE_PARENT")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "red-bg");	
				else if ($(this).val()=="ROLE_ADMIN")
					roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
				else if ($(this).val()=="ROLE_USER")
				roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "black-bg");
				
				completeRoleTagHtml = completeRoleTagHtml + roleTagTemp;
				
				}
			});
		} else {
			$("#editUser #userRole option").each(function() {
				if($(this).attr('selected') == true ||$(this).attr('selected')=="selected") {
					if ($(this).val()=="ROLE_ADMIN") {
						var roleClass = 'role' + ' ' + $(this).val();
						var roleTagTemp = roletag +'<br/>';
						roleTagTemp = roleTagTemp.replace(/_CLASS_/g, roleClass);
						roleTagTemp = roleTagTemp.replace(/_VALUE_/g, $(this).text());
						roleTagTemp = roleTagTemp.replace(/_BGCOLOR_/g, "orange-bg");
						completeRoleTagHtml = completeRoleTagHtml + roleTagTemp;
					}
				}
			});
		}
		row.find('.roleContainerForUsers').html(completeRoleTagHtml);
	  }
		
	//----------------------------DELETE THE ROW IF THE DATA IS DELETED SUCCESSFULLY ---------------------
	
	function deleteRowValues(row) {
		row.closest("tr").remove();
		enableSorting(true);
	}	
	//----------------------------Resetting Modal Form---------------------
	
	function resetModalForm(formId)
	{
		$("#"+formId).each (function() { this.reset(); });
		$("input#userStatus").removeAttr('checked');
		$("input#userStatus").change();
		$("#userRole option").removeAttr('selected');
		$("#userRole option").change();
		$("#userRole option").trigger('update-select-list');
		for(name in CKEDITOR.instances)	{
			CKEDITOR.instances[name].destroy(true);
		}	
	}
	
	
	//------ auto completing the search field
	$("#searchUser").autocomplete({
		source: function(request, response) {
	        $.ajax({
	            url: "searchUserAutoComplete.do",
	            cache:false,
	            dataType: "json",
	            data: {
	                term : request.term,
	                selectedOrg : $("a.jstree-clicked").parent().attr("id"),
	                AdminYear : $("#AdminYear").val(),
	                purpose : $('#purpose').val(),
	                eduCenterId : $('#eduCenterId').val()
	            },
	            success: function(data) {
	                response(data);
	            }
	        });
	    },
		minLength:3,
		position:{my:"right top",at:"right bottom"},
		open: function(event, ui) {
			$(".ui-autocomplete").css({"max-height":"150px", "width":"186px", "z-index":"1000000"});
			$(".ui-autocomplete").addClass("white-gradient");
			if($.template.ie7 || $.template.ie8) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"201px", "z-index":"1000000", "overflow" : "auto"});
			} else {
				$('.ui-autocomplete').customScroll({
					horizontal : true,
					showOnHover : false,
					width: 10
				});
			}
		},
		select:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchUser").val(searchString);
			}
			searchUser(searchString,'Y');
			event.preventDefault();
			event.stopPropagation();
			//$(".pagination").hide(200);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchUser").val(searchString);
			}
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	
	//------------binding the key up event with the search field , it will search the users when the enter key is pressed
	$("#searchUser").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchUser($("#searchUser").val(),'N');
		}
	});
	//------------binding the click event with the search field icon, it will search the users when search button is clicked
	$("a[param^='search_icon_user']").live("click", function(e) {
		if ( $("#searchUser").val()!="" && $("#searchUser").val()!="Search") {
			$(".ui-autocomplete").hide();
			searchUser($("#searchUser").val(),'N');
		}
	});

	//------------------This function searches the user with the typed user name and populates the user details in the table 
	function searchUser(searchString,isExactSearch) {
			blockUI();
			$.ajax({
			type : "GET",
			url : "searchUser.do",
			data : "username="+searchString+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&AdminYear="+$("#AdminYear").val()+"&isExactSearch="+isExactSearch+"&purpose="+$("#purpose").val()+"&eduCenterId="+$("#eduCenterId").val(),
			dataType : "json",
			cache:false,
			success : function(data){
				if (data != null && data.length > 14){
					$(".pagination").show(200);
					$("#moreStudent").removeClass("disabled");
					if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
				} else {
					$(".pagination").hide(200);
				}
				if ( data != null && data != "") {
					getUserDetails(true, data); //this method is defined in usermodule.js
					enableSorting(true);
				}
				if ( data != null && data == "") {
					getUserDetails(true, data); //this method is defined in usermodule.js
				}
				unblockUI();
			},
			error : function(data){
				$.modal.alert(strings['script.user.search']);
				unblockUI();
			},
			complete: function(data){
				$("#moreUser").removeClass("disabled");
				if($.browser.msie) $("#moreUser").removeClass("disabled-ie");
				}
		});
	}
	
	function clickMe(event) {
		event.stopImmediatePropagation();
		$(document).click();
	}
	
	function showHideMore(){
		$("#moreUser").addClass("disabled");
		if($.browser.msie) $("#moreUser").addClass("disabled-ie");
	}
	
	function isFoundInList(value, list){
		var isFound = false;
		for(var i=0; i<list.length; i++) {
			if(value == list[i].roleName) return true;
		}
		return isFound;
	}