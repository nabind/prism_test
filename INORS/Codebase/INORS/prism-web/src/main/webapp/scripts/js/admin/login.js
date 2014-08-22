
$(document).ready(function() {
	/**
	 * Commented the call - By Joy
	 * Every content of login page is configurable and DB driven
	 * */
	//getLoginMessage();
	
	$(".login-password-help").keypress(function(evt){
			if (evt.keyCode  == 13) { 
				return false;
			}					
		});
		
	$(".login-username-help").keypress(function(evt){
		if (evt.keyCode  == 13) { 
			return false;
		}					
	});	
	//========================Code for Forgot Password================================
	$(".login-password-help").click(function(e){
		var cont='<p class="button-height inline-label">'
					+'<label class="label isIE" style="width: 70px;" for="username">Username</label>'
					+'<input type="text" name="f_username" id="f_username" style="width:200px;margin:3px" class="input" />'
					+'</p>'
					+'<p style="width:306px;margin:3px" class="message red-gradient margin-top display-none" id="invalidUsernameMsg">The Username entered is not valid. Make sure you have entered correct username.</p>';
		
		$("#forgotPasswordContainer").html(cont);
		$("#f_username").val("");
		$("#invalidUsernameMsg").hide();
		$("#forgotPassword").modal({
			title: 'Forgot Password?',
			resize:false,
			draggable: false,
			height: false,
			onOpen:populateUsername(),
			buttons: {
				'Close': {
					classes: 'glossy',						
					click: function(win) {
						win.closeModal();
					}
				},
				'Next': {
					classes: 'blue-gradient glossy mid-margin-left next',
					click: function(win,e) {
						var userName=$("#f_username").val();
						if (!(($("#f_username").val()).replace(/\s/g,"").length==0)){
						blockUI();
						$.ajax({
							type : "GET",	
							url : "regn/checkActiveUser.do",
							data : "username="+userName,
							dataType : 'json',
							cache:false,
							success : function(data) {
								unblockUI();
								if (data.available == "true") {
									$("#invalidUsernameMsg").show(500);
									$("#invalidUsernameMsg").text("The username entered is not valid.");											
								}
								else if (data.available == "false") {
									$("#invalidUsernameMsg").hide();
									getSecurityQuestions(userName);
								}
							},
							error : function(data) {
								unblockUI();												
								$.modal.alert("Could not retrieve user details.");
							}
						});
						}else{
							$("#invalidUsernameMsg").show(500);
							$("#invalidUsernameMsg").text("Please enter an username to proceed.");											
						}
					}
				},
				'Submit': {
						classes: 'blue-gradient glossy mid-margin-left display-none submit',						
						click: function(win,e) {
							var userName=$("input#username").val();
							if(	(($("#ans1").val()).replace(/\s/g,"").length==0)||(($("#ans2").val()).replace(/\s/g,"").length==0)||(($("#ans3").val()).replace(/\s/g,"").length==0)){
								$("#invalidUserAnsMsg").text("Please enter all the answers");
								$("#invalidUserAnsMsg").show(500);
							}
							else{
								$("#invalidUserAnsMsg").hide();
								var obj = 'ans1='+$("#ans1").val()
										+ '&ans2='+$("#ans2").val()
										+ '&ans3='+$("#ans3").val()
										+ '&questionId1='+$("#questionId1").val()
										+ '&questionId2='+$("#questionId2").val()
										+ '&questionId3='+$("#questionId3").val()
										+ '&username='+$("#username").val();
																					
								checkAnswers(obj,userName);
							}
						}
					}
				}
			});
		});
	
	//========================Code for Forgot Username================================
		$(".login-username-help").click(function(e){
			var cont='<p class="button-height inline-label">'
					+'<label class="label isIE" style="width: 90px;" for="email">Enter Email Id</label>'
					+'<input type="text" name="f_email" id="f_email" style="width:200px;margin:3px" class="input"/>'
				   +'</p>'
				   +'<p style="width:306px;margin:3px" class="message red-gradient margin-top display-none" id="invalidEmailMsg">The Email Id entered is not valid. Make sure you have entered correct Email Id.</p>'
				   +'<p style="width:306px;margin:3px" class="message green-gradient margin-top display-none" id="mailSentNotofication"/>' 
				   ;
			$("#forgotUserNameContainer").html(cont);			
			$("#f_email").val("");
			$("#invalidEmailMsg").hide();
			$("#mailSentNotofication").hide();
			$("#forgotUserName").modal({
				title: 'Forgot Username?',
				resize:true,
				draggable: true,
				onOpen:ieModifyTag(),
				buttons: {
					/*'Close': {
						classes: 'glossy',						
						click: function(win) {
							win.closeModal();
						}
					},*/
					'Submit': {
						classes: 'blue-gradient glossy mid-margin-left  submitUser',						
						click: function(win,e) {
								var patt=/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i ;
								if ($("button.submitUser").text()!="OK"){
									if(	(($("#f_email").val()).replace(/\s/g,"").length==0) || (!patt.test($("#f_email").val())) ){
										$("#invalidEmailMsg").text("The email id entered is not valid.");
										$("#invalidEmailMsg").show(500);
										$("#mailSentNotofication").hide();
										
									}
									else{
										$("#invalidEmailMsg").hide();
										$("#mailSentNotofication").hide();
										getUserNames($("#f_email").val());
									}
								}
								else {
									/*var selectedUser=$('input[name=radioUser]:radio:checked').val();
									if ( (selectedUser != null) || (selectedUser != undefined)){
										$("#j_username").val(selectedUser);
										$("#j_username").focus();
										$("#j_username").select();
										win.closeModal();
									}*/
									win.closeModal();
								}
						}
					}
				}
			});
		});	
	}); // End document ready

/*
 * By Joy
 * Get teacher/parent login message after loading the login page 
 */
	function getLoginMessage(){
		if(typeof $('#userLogin').val() !== 'undefined'){
			blockUI('.nine-columns');
			$.ajax({
				type : "GET",
				url : "getLoginMessage.do",
				data : null,
				dataType : 'json',
				cache: true,
				success : function(data) {
					$('#taContent').val(data.value);
					$('#contentDescription').html($('#taContent').val());
					unblockUI('.nine-columns');
				},
				error : function(data) {
					if (data.status == "200") {
						$('#taContent').val(data.responseText);
						$('#contentDescription').html($('#taContent').val());
					} else {
						$("#contentDescription").html("<p class='big-message icon-warning red-gradient'>Error getting login page content. Please try later.</p>");
					}	
					unblockUI('.nine-columns');
			  }
			});
		}
	}
	
//========================Code for Forgot Password================================
	function populateUsername(){
		if ($("#j_username").val()=="Username"){
			$("#f_username").val("");
		} else{
			$("#f_username").val($("#j_username").val());
		}
		ieModifyTag();
	}
	
	function getSecurityQuestions(userName){
		blockUI();
		$.ajax({
			type : "GET",	
			url : "regn/securityQuestionForUser.do",
			data : "username="+userName,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				if (data != null && data.length > 0) {
					//build question dom	
					var isCreated=createQuestionDom(data,userName);
					ieModifyTag();	
					if (isCreated){
						$("button.next").hide();
						$("button.submit").removeClass("display-none");
					}
					$("#forgotPassword").setModalPosition('auto', 20, false);
				}
				else {
					$.modal.alert("Could not retrieve user's security questions.");
				}
			},
			error : function(data) {		
				unblockUI();
				$.modal.alert("Could not retrieve user's security questions.");
			}
		});
	}
	
	function createQuestionDom(data,userName){
		var content='';
		var counter=1;
		$.each(data, function() {
			content += '<div class="block margin-bottom padding-bottom" style="width:400px;margin:3px">'
						+'<h4 class="block-title" style="font-size: 16px;line-height: 6px;">Question '+counter+'</h4>'
						+'<div class="padding-left margin-bottom"><p class=""><b>'+this.question+'</b></p>'
						+'<div class="button-height inline-small-label ">'
						+'<label class="label isIE" for="ans'+counter+'">Answer</label>'
						+'<input type="text" name="ans'+counter+'" id="ans'+counter+'" style="width:200px;margin:3px" class="input" maxlength="200"/></div>'
						+'</div></div>'
		+'<input type="hidden" name="questionId'+counter+'" id="questionId'+counter+'" value="'+this.questionId+'"/>'
		
				counter++;	
			});
		content +='<input type="hidden" name="username" id="username" value="'+userName+'"/><p style="width:384px;margin:3px" class="message red-gradient margin-top display-none" id="invalidUserAnsMsg">Please enter all the answers</p>'
		$("#forgotPasswordContainer").html(content);
		
		return true;
	}
	
	function checkAnswers(formObj,userName){
		blockUI();
		 $.ajax({
				type : "GET",	
				url : "regn/checkAnswers.do",
				data : formObj,
				dataType : 'html',
				cache:false,
				success : function(data) {
				unblockUI();
				var obj = jQuery.parseJSON(data);
					if(obj.status == 'Success') {
						generateNewPassword(userName);											
					} else if (obj.status != 'Success') {
						$("#invalidUserAnsMsg").show(500);
						$("#invalidUserAnsMsg").text("One or more answers are not matching with system. Please try again.");
					}
				},
				error : function(data) {
					unblockUI();					
					$.modal.alert("Some error occured while submitting answers. Please try later.");
				}
		  });
	}
	
	function generateNewPassword(userName){
		blockUI();
		$.ajax({
			type : "GET",	
			url : "regn/generateTempPwd.do",
			data : "username="+userName,
			dataType : 'json',
			cache:false,
			success : function(data) {
			unblockUI();
			if(data != null) {
				if( data.sendEmailFlag == "1") {
					$("#forgotPasswordContainer").html("The password for the user has been reset.<br/>Temporary password will be sent to user email.");
					$("button.submit").hide();					
				} else {
					$.modal.alert("Could not reset password. Please try later.");
				}					
			} else{
					$.modal.alert("Could not reset password. Please try later.");
				}
			},
			error : function(data) {	
				unblockUI();							
				$.modal.alert("Could not reset password. Please try later.");
			}
		});
	}
	
	function buildPasswordDom(jsonData,userName){
		if (jsonData.password != null) {
			var pwdContent='<div id="newPassword">'
							 +'<p class="message blue-gradient" style="width:400px">The password for the user has been reset:</p>'
							 +'<p><b>Username:</b>&nbsp;'+userName+'</p>'
							 +'<p><b>Temporary Password:</b>&nbsp;'+jsonData.password+'</p>'
							 +'<p class="big-message" style="width:400px">The user will need to change the password after log in.</p>'
							 +'</div>';
				
			$("#forgotPasswordContainer").html(pwdContent);
	
			return true;
		} else 
			return false;
	}
	
//========================Code for Forgot Username================================		

	function getUserNames(emailId){
		blockUI();
		$.ajax({
			type : "GET",	
			url : "regn/getUserNames.do",
			data : "emailId="+emailId,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				if (data != null ) {
					if( data.sendEmailFlag == "1") {
						$("#invalidEmailMsg").hide();
						$("#mailSentNotofication").show(500);
						$("#mailSentNotofication").html("<span style=\"color: green\">UserName has been sent Successfully!!</span>");
						/*var iscrearted = makeuserDom(data);	
						if (iscrearted){
							$("button.submitUser").text("OK");
						}*/
						
						} else {
							$("#mailSentNotofication").hide();
							$("#invalidEmailMsg").show(500);	
							$("#invalidEmailMsg").text("There are no users present with this email id. Please retry with the correct email id.");
						}					
													
				} else {
					$.modal.alert("Some error occurred while fetching username.");											
				}
			},
			error : function(data) {	
			   unblockUI();					
			   $.modal.alert("Some error occurred while fetching username.");
			}
		});
	}

	function makeuserDom(data){
		var content='';
		var count=1;
		var numberOfuser=data.length;
		if (numberOfuser==1){
			content = '<p style="width:389px" class="message blue-gradient with-small-padding" id="UsernameMsg">The following user is associated with the given email id. Please click "OK" to continue.</p>';
		} else if (numberOfuser>1){
			content = '<p style="width:389px" class="message blue-gradient with-small-padding" id="UsernameMsg">Following '+numberOfuser+' users are associated with the given email id. Please identify correct username and click "OK" to continue.</p>';
		}
		var selected = 'selected=true';
		$.each(data, function() {
			if(count > 1) {
				selected = '';
			} 
			content +='<div class="block margin-bottom with-small-padding" style="width:384px;margin:3px">'
				   //+'<input id="radio-'+count+'" type="radio" value="'+this.userName+'" name="radioUser" '+selected+'>&nbsp;'
				   +'<span  class="label" for="radio-'+count+'"><b>Username:&nbsp;</b>'+this.userName+' </span><br/>'
					+'<small class="input-info margin-left">First Name: '+this.firstName+' </small><br/>'
					+'<small class="input-info margin-left">Last Name: '+this.lastName+' </small>'
					+'</div>';
				count++;
		});
		$("#forgotUserNameContainer").html(content);
		
		if(count > 3) {
			$("#forgotUserName").setModalPosition('auto', 20, false);
		}
		return true;
	}
	
	//patch for IE	
	function ieModifyTag(){
		if($.browser.msie) {
			$("label.isIE").removeClass("label");
			$("label.isIE").css('font-weight', 'bold');
			$("label.isIE").append('<span class="label padding-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>');
		}
	}	