	/**
	 * This js file is to manage parent admin module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
	
	$(document).ready(function() {
		$('.view-Children').live("click", function() {
			resetModalForm("editParent");
			openParentModalToViewStudent($(this).attr("parentName"),$(this).attr("clickedTreeNode"),$(this));//,$(this).attr("tenantId")
		});
		$('.reset-Password').live("click", function() {
			resetModalForm("editParent");
			openResetPasswordModal($(this).attr("parentName"),$(this).attr("parentDisplayName"));
			
		});
		
		//$("#menuFindStudents").parent().addClass("current");
		$('.shortcut-stats').live("click", function(e) {
			e.stopImmediatePropagation();
			resetModalForm("editParent");
		/*	$('.clearfix').addClass('menu-hidden');
			$("ul#shortcuts li").removeClass("current");
			$('.shortcut-stats').parent().addClass("current");*/
		});
		
		//$('input#lastParentName').val(""); //clearing the value on load
		if($("#parentTable").length > 0) {
			var parents = $(".parent-list-result");
			if(parents != null && parents.length > 0) {
				$('#report-list').tablesorter({
					headers: {
						//Change for TD 77643 - By Joy
						3: { sorter: false }
					}/*,
					sortList: [[0,1]]*/
				});
			}
			if(parents != null && parents.length < moreCount) {
				// hide more button if the parent list is less than 15
				$(".pagination").hide(200);
			}
			$('.clearfix').addClass('menu-hidden');
			$("ul#shortcuts li").removeClass("current");
			$(".shortcut-medias").parent().addClass("current");
		//========================SCROLL FOR THE PARENT TABLE===============================	
			var tempScrollTop, currentScrollTop = 0
			//$("#parentTable").scroll(function() {
			$("#moreParent").click(function() {
				currentScrollTop = $("#parentTable").scrollTop();
				//if (tempScrollTop < currentScrollTop) {
				if(!$(this).hasClass('disabled')) {
					blockUI();
					//scrolling down15
					var lastid ="tenantId=" + $('input#lastParentName').val() + "&searchParam="+$("#searchParent").val(); 
					//showLoader(currentScrollTop);//This method is defined in usermodule.js
					$.ajax({
						type : "GET",
						url : "getParentDetailsOnScroll.do",
						data : lastid,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data != null && data.length > 0){
								if(getParentDetails(false,data)){
									enableSorting(true);
								}
								retainUniqueValue();
								//hideLoader();//This method is defined in usermodule.js
								unblockUI();
								$("#parentTable").animate({scrollTop: currentScrollTop+600}, 500);
							} else {
								$("#moreParent").addClass("disabled");
								if($.browser.msie) $("#moreParent").addClass("disabled-ie");
								unblockUI();
							}
							if (data != null && data.length < moreCount) {
								// check if this is the last set of result
								$("#moreParent").addClass("disabled");
								if($.browser.msie) $("#moreParent").addClass("disabled-ie");
							}
						},
						error : function(data) {
							$.modal.alert(strings['script.org.error']);
							//hideLoader();//This method is defined in usermodule.js
							unblockUI();
						}
					});
		
				} else {
					return false;
				}
				tempScrollTop = currentScrollTop;
			});
			
			/*Fixed for TD  77675 Start - By Joy
			 * Use notification instead of message in page
			 */
			notify('Notification!', 'Parent users are associated with <b>School</b> level (not with Class/Teacher level).', {
				system:				true,
				vPos:				'top',
				hPos:				'center',
				autoClose:			false,
				icon:				'themes/acsi/img/demo/icon.png',
				iconOutside:		true,
				closeButton:		true,
				showCloseOnHover:	true,
				groupSimilar:		true
			});
			//Fixed for TD  77675 End - Implementation change - By Joy
		}
	});
	
	//======================================== Create parent row DOM======================================
	function getParentDetails(checkFirstLoad,data) {
		var manageParents_resetPwd = $('#manageParents_resetPwd').val();
		var manageParents_viewChildren = $('#manageParents_viewChildren').val();
		var parentContent = '';	
		if (checkFirstLoad) {
			$("tbody#parent_details").find("tr").remove();
			$("#report-list").trigger("update");
		}
		
		$.each(data, function () { 
			parentContent += '<tr id ='+ this.userName +' class="abc" scrollid='+this.clikedOrgId+'_'+this.userName+'>'
								+'<th scope="row">' + createStatusTag(this.status) + this.userName +'</th>'
								+'<td>' + this.displayName +'</td>'
								+'<td>'+this.orgName+'</td>';
								
			if(manageParents_resetPwd == 'true' || manageParents_viewChildren == 'true'){
				parentContent += '<td class="vertical-center">'
									+' <span class="button-group compact">';
				if(manageParents_resetPwd == 'true'){
					parentContent += '<a id="'+ this.userId + '" parentName="'+ this.userName +'" parentDisplayName="'+this.displayName
										+'" href="#" class="button icon-lock with-tooltip reset-Password" title="Reset Password"></a>';
				}
				if(manageParents_viewChildren == 'true'){
					parentContent += '<a id="'+ this.userId + '" parentName="'+ this.userName +'" clickedTreeNode="'+  this.clikedOrgId 
										+'" orgId="'+  this.orgId +'" isPN="N" href="#" class="button icon-users icon with-tooltip view-Children" title="View Children"></a>';
				}
				parentContent += 		'</span>'
								+'</td>';
			}
								
			parentContent +='</tr>' ;
						
						 
		});
		$("#parent_details").append(parentContent);
		 setLastRowId ($("#lastParentName"));
		 
		// update table sorting 
		//$("#report-list").trigger("update");
		//$(".headerSortDown").removeClass("headerSortDown");
		//$(".headerSortUp").removeClass("headerSortUp");
		//var sorting = [[0, 1]];
		//$("#report-list").trigger("sorton", [sorting]);
		
		return true;
	}
	
	//======================================== CREATE STUDENT ROW DOM======================================
	function getStudentDetails(checkFirstLoad,data) {
		var manageStudents_assessment = $('#manageStudents_assessment').val();
		var studentContent = '';
		if (checkFirstLoad){
			$("tbody#student_details").find("tr").remove();
			$("#report-list").trigger("update");
		}
		$.each(data, function () { 
			if(this.grade == 'AD') {
				studentContent += '<tr id ="'+ this.studentName +'" scrollid="'+this.rowIndentifier+'|'+this.clikedOrgId+'" class="abc" >'
				+'<th scope="row">' + this.studentName +'</th>'
				+'<td>' + this.grade +'</td>'
				+'<td>'+checkForNull(this.orgName)+'</td>'
				+'</tr>' ;
			} else{
				studentContent += '<tr id ="'+ this.studentName +'" scrollid="'+this.rowIndentifier+'|'+this.clikedOrgId+'" class="abc" >'
				+'<th scope="row">' + this.studentName +'</th>'
				+'<td>' + this.grade +'</td>'
				+ createParentTag(this.parentAccount)
				+'<td>'+checkForNull(this.orgName)+'</td>';
				
				if(manageStudents_assessment == 'true'){
					studentContent += '<td class="vertical-center">'
					+' <span class="button-group compact">' 
					+' <a id="'+ this.studentBioId + '" testelementid="'+this.testElementId+'" parentName="'+ this.studentName + '" href="#" class="button with-tooltip view-Assessment" title="View Assessment"><span class="icon-pages"></span> Assessment</a>'  
					+' </span>'
					+'</td>';
				}
				
				studentContent += '</tr>' ;
			}			
						 
		});
		$("#student_details").append(studentContent);
		setLastRowId ($("#lastStudentId"));//This method is defined in usermoudle.js
		 
		// update table sorting 
		$("#report-list").trigger("update");
		$(".headerSortDown").removeClass("headerSortDown");
		$(".headerSortUp").removeClass("headerSortUp");
		//var sorting = [[0, 1]];
		//$("#report-list").trigger("sorton", [sorting]);
	}
	//====================================AJAX CALL TO FETCH ALL THE PARENTS FOR THE SELECTED ORG====================
	function fetchAllParentUsers(id,url)
	{
		blockUI();
		var nodeid = "tenantId=" + id;
		$("tbody#parent_details").find("tr").remove();
		$("#report-list").trigger("update");
		$("tbody#parent_details").addClass("loader big");
		$.ajax({
			type : "GET",
			url : url,
			data : nodeid,
			dataType : 'json',
			cache:false,
			async : false,
			success : function(data) {
				unblockUI();
				if(data.length >= 1){
					showHideDataTable('show');
				}
				if (data != null && data.length >= moreCount){
					$(".pagination").show(200);
					$("#moreParent").removeClass("disabled");
					if($.browser.msie) $("#moreParent").removeClass("disabled-ie");
				} else {
					$(".pagination").hide(200);
				}
				if (data != null && data.length == 0) {
					$("span#showOrgNameParent").text(' ');
				} else {
					$("span#showOrgNameParent").text('Parents of '+$("a.jstree-clicked").text());
				}
				getParentDetails(true, data);
				enableSorting(true);
				$("tbody#parent_details").removeClass("loader big");				
			},
			error : function(data) {
				unblockUI();
				$("tbody#parent_details").removeClass();	
				$.modal.alert(strings['script.org.error']);
			}
		});
	}
	
	
	//=========================================NULL CHECKING FOR UNDEFINED JSON KEY VALUE==================
	function checkForNull (value)
	{
		if(value != null)
		return value;
		else 
		return "";
		
	}
	
	//==========================THIS FUNCTION CREATES LIST OF PARENTS TO BE DISPALYED FOR EACH STUDENT============================
	function createParentTag(parentJson) {
	var parentContent = '<td>';
	if (parentJson) {
		var size = parentJson.length;
		$.each(parentJson, function(index) {
			if (!(index + 1 == size)) {
				parentContent += this.userName + ', ';// '<p>'+this.userName+'</p>';
			} else {
				parentContent += this.userName;
			}
		});
	}
	parentContent += '</td>';

	return parentContent;
}
	
	
	
	//======================OPEN VIEW CHILDREN  SCREEN==========================================
	function openParentModalToViewStudent(parentName,clickedTreeNode,$obj) {
		//var row = $("#"+parentName);
		var row = $("[id='"+parentName+"']");
		var nodeid = "parentName=" + parentName 
						+"&clickedTreeNode="+clickedTreeNode 
						+"&isPN="+$obj.attr('isPN')
						+"&orgId="+$obj.attr('orgId');	
		blockUI();
		$.ajax({
				type : "GET",
				url : "getChildrenList.do",
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if (data!= null) {
					//=========OPEN THE MODAL========
						$("#parentModal").modal({
							title: 'View Children for <b>'+ parentName + '</b>',
							height: false,
							width: 410,
							resize:false,
							draggable: false,
							onOpen:buildStudentTableDom(data,"parentModal","parentModalContainer"),
							buttons: {
									'Close': {
										classes: 'glossy',
										click: function(win) {
											win.closeModal();
										}
									}
							
							}
						});
					}
					else {
						$.modal.alert(strings['script.parent.noChildren']);
					}
					unblockUI();
				},
				error : function(data) {
					unblockUI();
					$.modal.alert(strings['script.common.error']);
				}
			})	
				
		}
	
											
	//==========================CREATES THE LIST STRUCTURE FOR CHILDREN VIEW=======================
	function createParentModalDivContent(jsonData,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId + ">" +"p.message").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("table").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("dl").remove();
		var makeViewStudentDom='<dl class="accordion same-height " style="width:340px">';
		$.each(jsonData, function () { 
			makeViewStudentDom +='<dt class="closed blue-gradient glossy"><span class="white"> '+ this.studentName+' </span></dt>'
										  +'<dd style="height: 150px; ">'
										  	+'<div class="with-padding">'
											  		+'<p class="button-height inline-label">'
											  				+'<label class="label" for="studentAdminSeason"> '+strings['label.AdminSeason']+' </label>'
											  					+'<label class="full-width newReportName" id="studentAdminSeason"> '+this.administration+' </label>'
											  		+'</p>'	
											  		+'<p class="button-height inline-label">'
											  				+'<label class="label" for="studentAdminGrade"> '+strings['label.Grade']+' </label>'
											  					+'<label class="full-width newReportName" id="studentAdminGrade"> '+this.grade+' </label>'
								  					+'</p>'	
								  			+'</div>'
								  		 +'</dd>';
							  	
					});
		makeViewStudentDom += '</dl>';
		makeViewStudentDom += '<p class="message" style="width:329px">'
								+'<span class="big-message-icon icon-speech left-side with-text blue"></span>'
								+'<span class="blue">'+strings['msg.clickStudent']+'</span>'
								+'</p>';
							
		
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(makeViewStudentDom);				
	}
	
	//Modified by Joy
	//==========================CREATES THE TABULAR STRUCTURE FOR CHILDREN VIEW=======================
	function buildStudentTableDom(jsonData,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId + ">" +"p.message").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("dl").remove();
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("table").remove();
		var makeViewStudentTableDom = '<table id="studentTable" class="table " style="width:400px">'
										+'<thead class ="table-header blue-gradient glossy ">'
										+'<tr >'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.studentName']+'</span></th>'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.adminSeason']+'</span></th>'
										+'<th scope="col" class="blue-gradient glossy"><span class="white">'+strings['th.studentTable.grade']+'</span></th>'
										+'</tr>'
										+'</thead>'
										+'<tbody>';
		$.each(jsonData, function () { 
		
						makeViewStudentTableDom += '<tr>'
													+'<td>'
													+getStudentNameHTML(this)
													+'</td>'
													+'<td>'+this.administration +'</td>'
													+'<td>'+this.grade +'</td>'
													+'</tr>';
							});
							
		makeViewStudentTableDom += '</tbody></table>';
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(makeViewStudentTableDom);		
				
	}
	
	function getStudentNameHTML(obj){
		var studentNameHTML = "";
		if(obj.bioExists == 1){
			studentNameHTML += '<a href="redirectToStudent.do?AdminYear='
				+obj.adminid+'&studentBioId='+obj.testElementId+'&nodeId='+obj.clikedOrgId+'" >'
				+obj.studentName 
				+'</a>'
		}else{
			studentNameHTML += '<span class="with-tooltip tooltip-left" title="'+strings['msg.studentNotAvailable']+'">'
				+obj.studentName+'</span>';
		}
		return studentNameHTML;
	}
	
	
	
	
	
	
	//------ auto completing the search field
	$("#searchParent").autocomplete({
		source: function(request, response) {
            $.ajax({
                url: 'searchParentAutoComplete.do',
                dataType: "json",
                cache:false,
                data: {
                    term : request.term,
                    selectedOrg : $("a.jstree-clicked").parent().attr("id"),
                    AdminYear : $("#AdminYear").val()
                },
                success: function(data) {
                    response(data);
                }
            });
        },
		minLength:3,
		position:{my:"right top",at:"right bottom"},
		open: function(event, ui) {
			$(".ui-autocomplete").css({"max-height":"150px", "width":"186px"});
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
				$("#searchParent").val(searchString);
			}
			searchParent(searchString,'other','Y');
			event.preventDefault();
			event.stopPropagation();
			//searchParent(ui.item.value);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			if(searchString.indexOf('<br/>') != -1) {
				searchString = searchString.substring(0, searchString.indexOf('<br/>'));
				$("#searchParent").val(searchString);
			}
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	
	//------------binding the key up event with the search field , it will search the users when the enter key is pressed
	$("#searchParent").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchParent($("#searchParent").val(),'other','N');
		}
	});
	//------------binding the click event with the search field icon, it will search the users when the enter key is pressed
	$("a[param^='search_icon_parent']").live("click", function(e) {
	   
		if ($("#searchParent").val()!="" && $("#searchParent").val()!="Search") {
			$(".ui-autocomplete").hide();
			searchParent($("#searchParent").val(),"other",'N');
		}/*else if($.template.ie7 || $.template.ie8 ){
			if ( $("#searchParent").val()=="Search" ) {
				$(".ui-autocomplete").hide();
				searchParent($("#searchParent").val(),"ie7");
				}
	   }*/
	});
	
	
	//===================This function searches the parent with the typed user name and populates the user details in the table ==========================
	function searchParent(searchString,browser,isExactSeacrh) {
		blockUI();	
		$.ajax({
			type : "GET",
			url : "searchParent.do",
			data : "parentName="+searchString+"&browser="+browser+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&isExactSeacrh="+isExactSeacrh+"&AdminYear="+$('#AdminYear').val(),
			dataType : "json",
			cache:false,
			success : function(data){
				unblockUI();
				if ( data != null || data != "") {
					if(data.length < moreCount) {
						// hide more button if the parent list is less than 15
						$(".pagination").hide(200);
					} else {
						$(".pagination").show(200);
						$("#moreParent").removeClass("disabled");
						if($.browser.msie) $("#moreParent").removeClass("disabled-ie");
					}
					getParentDetails(true,data); 
					//$(".pagination").hide(200);
				}
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.user.search']);
				
			}
		});
	}

	
	//------ auto completing the search field for auto complete
	$("#searchStudent").autocomplete({
		source:function(request, response) {
            $.ajax({
                url: 'searchStudentAutoComplete.do',
                dataType: "json",
                cache:false,
                data: {
                    term : request.term,
                    selectedOrg : $("a.jstree-clicked").parent().attr("id"),
                    AdminYear : $("#AdminYear").val()
                },
                success: function(data) {
                    response(data);
                }
            });
        },
		minLength:3,
			position:{my:"right top",at:"right bottom"},
			open: function(event, ui) {
				$(".ui-autocomplete").css({"max-height":"150px", "width":"186px"});
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
			searchStudent("searchStudent.do",ui.item.value);
		},
		focus:function(event, ui) {
			var searchString = ui.item.value;
			$("#searchStudent").val(searchString);
			event.preventDefault();
			event.stopPropagation();
		}
	});
	



//------------binding the key up event with the search field , it will search the students when the enter key is pressed
	$("#searchStudent").live("keyup", function(e) {
		if ( e.keyCode == 13 && $(this).val()!="") {
			$(".ui-autocomplete").hide();
			searchStudent("searchStudent.do",$("#searchStudent").val());
		}
	});
	//------------binding the click event with the search field icon, it will search the students when the search icon is clicked
	$("a[param^='search_icon_student']").live("click", function(e) {
	   
		  if ( $("#searchStudent").val()!="" && $("#searchStudent").val()!="Search" ) {
				$(".ui-autocomplete").hide();
				searchStudent("searchStudent.do",$("#searchStudent").val(),"other");
			}/*else if($.template.ie7 || $.template.ie8 ){
					if ( $("#searchStudent").val()=="Search by Student Name" ) {
						$(".ui-autocomplete").hide();
						searchStudent("searchStudent.do",$("#searchStudent").val(),"ie7");
						}
			   }*/
	});
	
	
	//===================This function searches the students with the typed student name and populates the student details in the table ==========================
	function searchStudent(methodurl,searchString,browser) {
		blockUI();
		$.ajax({
			type : "GET",
			url : methodurl,
			data : "studentName="+searchString+"&browser="+browser+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+ "&AdminYear=" + $("#AdminYear").val(),
			dataType : "json",
			cache:false,
			success : function(data){
				unblockUI();
				if ( data != null || data != "") {
					if(data.length < moreCount) {
						// hide more button if the parent list is less than 15
						$(".pagination").hide(200);
					} else {
						$(".pagination").show(200);
						$("#moreStudent").removeClass("disabled");
						if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
					}
					getStudentDetails(true,data); 
					//$(".pagination").hide(200);
				}
			},
			error : function(data){
				unblockUI();
				$.modal.alert(strings['script.user.search']);
				
			}
		});
	}
	
	//======================OPEN RESET PASSWORD ==========================================
	function openResetPasswordModal(parentName,parentDisplayName) {
		$("#passwordModal").modal({
			title: '<b>Reset User Password</b>',
			width: 480,
			resizable: false,
			draggable: false,
			onOpen:showPasswordChangeWindow(parentName,parentDisplayName,"passwordModal","passwordModalContainer"),
			buttons: {
					'Submit': {
						classes: 'blue-gradient glossy',
						click: function(win) {
							//win.closeModal();//$(selector).setModalTitle(title)
							resetPassword(win,parentName,parentDisplayName);
							$(this).hide();
							$(this).siblings(".canelPwdReset").addClass("blue-gradient");
							$(this).siblings(".canelPwdReset").text("OK");
						}
					},
					'Cancel': {
						classes: 'glossy canelPwdReset',
						click: function(win) {
							//$(this).attr("id","canelPwdReset");
							win.closeModal();
						}
					}
			
			}
		});
				
		}	
   //=======================================AJAX CALL TO RESET THE PASSWORD IN THE DATABASE============
	function resetPassword(win,parentName,parentDisplayName)
	{
		var row = $("#"+parentName);
		var nodeid = "userName=" + parentName;	
		blockUI();
		$.ajax({
			type : "GET",
			url : "resetPassword.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				if (data!= null) {
				//=========REBUILD THE MODAL DOM========
					if( data.sendEmailFlag == "1") {
						win.setModalTitle(strings['msg.newParentPassword']);
						buildResetPasswordDom(parentDisplayName,"passwordModal","passwordModalContainer");				
					} else {
						$.modal.alert(strings['script.parent.passwordResetError']);
					}	
				}
				else {
					$.modal.alert(strings['script.parent.passwordResetError']);
				}
				unblockUI();
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.parent.passwordResetError']);
			}
		});
	}
	
	
	
	//==========================CREATES THE STRUCTURE FOR RESET PASSWORD=======================
	function buildResetPasswordDom(parentDisplayName,modalId,modalContainerDivId)
	{	
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var newPassword ='<div id="newPassword">'
							 +'<p class="message blue-gradient" style="width:400px">'+strings['msg.passwordReset']+'</p>'
							 +'<p><b>Full Name:</b>&nbsp;'+parentDisplayName+'</p>'
							 +'<p class="big-message" style="width:400px">'+strings['msg.passwordSent']+'</p>'
							 +'</div>';
				
			$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(newPassword);		
				
	}
	
	//==========================CREATES THE STRUCTURE FOR THE POP UP FOR CONFIRMATION TO THE USER FOR  RESETTING THE  PASSWORD=======================
	function showPasswordChangeWindow (parentName,parentDisplayName,modalId,modalContainerDivId)
	{
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var content ='<div>'
						+'<p  class="message blue-gradient" style="width:350px">'+strings['msg.clickingSubmit']+'</p>'
						+'<p class="big-message" style="width:350px">'+strings['msg.confirmIdentity']+'<br/>'
						+'</p>'
						+'<br/>'
						+'<strong>'+strings['msg.userName']+'</strong>&nbsp;&nbsp;&nbsp;'
						+parentName
						+'<br/>'
						+'<strong>'+strings['msg.fullName']+'</strong>&nbsp;&nbsp;&nbsp;'
						+ parentDisplayName
						+'</div>'
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(content);					
					
	}
	