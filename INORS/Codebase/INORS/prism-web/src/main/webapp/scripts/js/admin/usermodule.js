	/**
	 * This js file is to manage user module
	 * Author: Tata Consultancy Services Ltd.
	 * Version: 1
	 */
	var flgStudentRedirect=true;
	var redirectLevel = 0;
	$(document).ready(function() {

		if($("#userTable").length > 0) {
			var allUsers = $(".users-list-all");
			if(allUsers != null && allUsers.length > 0) {
				$('#report-list').tablesorter({
					headers: {
						//Change for TD 77643 - By Joy
						4: { sorter: false }
					},
					sortList: [[0,1]]
				});
			}
			
				$('.clearfix').addClass('menu-hidden');
				$("ul#shortcuts li").removeClass("current");
				$(".shortcut-contacts").parent().addClass("current");
			
			var tempScrollTop, currentScrollTop = 0
			$("#moreUser").click(function() {
				currentScrollTop = $("#userTable").scrollTop();
				if(!$(this).hasClass('disabled')) {
					//scrolling down15
					var lastid = "";
					var callingAction = "";
					if($('#purpose').val() == 'eduCenterUsers'){
						var eduCenterId = $('#eduCenterId').val();
						var eduCenterName = $("#eduCenterId :selected").text();
						var lastEduCenterId_username = $('#last_user_tenant').val();
						lastid = 'eduCenterId='+eduCenterId+'&eduCenterName='+eduCenterName+'&searchParam='+$("#searchUser").val()+'&lastEduCenterId_username='+lastEduCenterId_username;
						callingAction = 'loadEduCenterUsers.do';
					}else{
						lastid = "tenantId=" + $('input#last_user_tenant').val() + "&AdminYear=" + $("#AdminYear").val() + "&searchParam="+$("#searchUser").val();
						callingAction = 'getUserDetails.do';
					}
					//showLoader(currentScrollTop);
					blockUI();
					$.ajax({
						type : "GET",
						url : callingAction,
						data : lastid,
						dataType : 'json',
						cache:false,
						success : function(data) {
							if (data != null && data.length > 0){
								getUserDetails(false, data);
								enableSorting(true);
								retainUniqueValue();
								//setLastRowId ();
								unblockUI();
								$("#userTable").animate({scrollTop: currentScrollTop+600}, 500);
							} else {
								$("#moreUser").addClass("disabled");
								if($.browser.msie) $("#moreUser").addClass("disabled-ie");
								retainUniqueValue();
								unblockUI();
							}
							if (data != null && data.length < 14) {
								// check if this is the last set of result
								$("#moreUser").addClass("disabled");
								if($.browser.msie) $("#moreUser").addClass("disabled-ie");
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
			
			var tempTree = $("#tempTree").val();
			var objectTree = "";
			try{
				objectTree = tempTree.split(",");
			}catch(e){
				
			}
			redirectLevel = objectTree.length;
				
		}
		
		$("#downloadUsers").live("click", function() {
			var tenantId = $('input#last_user_tenant').val().split("_");
			var adminYear = $("#AdminYear").val();
			//alert("tenantId="+tenantId[0]+"\nadminYear="+adminYear);
			var href = "downloadUsers.do?tenantId=" + tenantId[0] + "&adminYear=" + adminYear;
			$("#downloadUsers").attr("href", href);
		});
	});
	
	// *********** END DOCUMENT.READY ************
	
	//======================================CREATE  TREE STURCTURE================================
		var isOpeningFromRemote = false;
		var isOpeningComplete = false;
		var objectTree;
		var currObj = '';
		
		var objectTreeStack;
		var loadFullpath = false;
		
		var maxNodeLimit = 50;
		var tempNodes = new Array();
		var jsTreeJsonArr = new Array();
		var tempIndex = 0;
		var treeContainerObj =$("#treeViewForOrg");
		$(function (){
			$("#slide_menu_user").removeClass("navigable");
			$("#slide_menu_org").removeClass("navigable");
            $("#slide_menu_parent").removeClass("navigable");
            $("#slide_menu_student").removeClass("navigable");
			treeContainerObj.jstree( {                  
					"json_data" : {                       
						//"data":jsondata ,
						"maxNodeLimit" : maxNodeLimit,
						"clickedNode" : function (n) {
											return (n.attr ? n.attr("id") : treeContainerObj.attr("rootid"));
										},	
						"ajax" : { 
							"url" : "tenantHierarchy.do",
							"cache":false,
							"data" : function (n) {
								clickedNode = (n.attr ? n.attr("id") : treeContainerObj.attr("rootid"));
								return {
											"tenantId" : clickedNode,
											"AdminYear" : $("#AdminYear").val()
									   };
								},									
							"success": function(data, textStatus, XMLHttpRequest) {
								if(data != null && data.length > 0) {
									if(data.length > maxNodeLimit)
									{													
										//alert(data.length);
										tempNodes = data;
										tempIndex = clickedNode;
									}
								}								
							},
							"complete" : function() { 
									$("#treeViewForOrg ul:first").css("float", "left");
									$("#treeViewForOrg ul:first").css("min-width", "100%");
									$("#treeViewForOrg [id="+clickedNode+"]").removeClass("jstree-closed").addClass("jstree-open");
									if(tempNodes != null)
									{
										if(tempNodes.length > maxNodeLimit) {
											var moreIndex = parseInt($(".no-click").index($("[id="+clickedNode+"] .no-click")));
											//alert(moreIndex);
											var tempObj = new Object();													
											tempObj.indexCount = maxNodeLimit;
											tempObj.nodeJsonData = tempNodes; 
											jsTreeJsonArr[tempIndex] = new Array();
											jsTreeJsonArr[tempIndex][moreIndex] = tempObj;
											tempObj = null;
											tempNodes = null;
										}
									} 												
									onClickMoreLink("treeViewForOrg",tempIndex);
									
									// open tree on clicking # of users in org screen
									if($("#isRedirectedTree").val()=='Yes'){ 
										 var rootOrg = $("#treeViewForOrg > ul > li:first").attr("id");
											var tempTree = $("#tempTreeStudent").val();
											if(flgStudentRedirect){
											  redirectLevel= (tempTree.split(",")).length;
											  flgStudentRedirect=false;
											}
											  
											if(redirectLevel > 1 && tempTree != '' && tempTree != null) {
												isOpeningFromRemote = true;
												objectTree = tempTree.split(",");
												
												var count = 0;
												var newTempTree = '';
												currObj = objectTree[0];
												$.each(objectTree, function(index) {
													if(count > 0) {
														newTempTree = newTempTree + objectTree[index] + ',';
													}
													count = count +1;
												});
												newTempTree = newTempTree.substring(0, newTempTree.length - 1)
												//$("#"+currObj).find(".jstree-icon").click();
												if(objectTree.length > 1) {
													var currnode = $("#"+currObj);
													if(currnode != null && currnode.length > 0) {
														treeContainerObj.jstree('open_node', $("#"+currObj));
													} else {
														clickMoreForRedirect(false, currObj);
													}
												} else {
													var currnode = $("#"+currObj);
													if(currnode != null && currnode.length > 0) {
														treeContainerObj.jstree('select_node', $("#"+currObj));
														//$("#"+currObj).find(".jstree-icon")[0].click();
														//$("#"+currObj).find("a")[0].click();
														$("#"+currObj).find("a").click();
													} else {
														clickMoreForRedirect(true, currObj);
													}
												}
												$("#tempTreeStudent").val(newTempTree);
											} else {
												isOpeningComplete = true;
											}
											if(isOpeningFromRemote && isOpeningComplete) {
												treeContainerObj.jstree('select_node', $("#"+currObj));
												//$("#"+currObj).find(".jstree-icon")[0].click();
												//$("#"+currObj).find("a")[0].click();
												$("#"+currObj).find("a").click();
											} 
											if(redirectLevel == 1  && tempTree != '' && tempTree != null) {
												$("#"+currObj).find("a").click();
												$("#tempTreeStudent").val('');
												
											}
									}else{
										var rootOrg = $("#treeViewForOrg > ul > li:first").attr("id");
										var tempTree = $("#tempTree").val();
									
										if(redirectLevel > 1 && tempTree != '' && tempTree != null) {
											isOpeningFromRemote = true;
											objectTree = tempTree.split(",");
											if(!loadFullpath) {
												objectTreeStack = tempTree.split(",");
												loadFullpath = true;
											}
											
											var count = 0;
											var newTempTree = '';
											currObj = objectTree[0];
											$.each(objectTree, function(index) {
												if(count > 0) {
													newTempTree = newTempTree + objectTree[index] + ',';
												}
												count = count +1;
											});
											newTempTree = newTempTree.substring(0, newTempTree.length - 1)
											//$("#"+currObj).find(".jstree-icon").click();
											if(objectTree.length > 1) {
												var currnode = $("#"+currObj);
												if(currnode != null && currnode.length > 0) {
													treeContainerObj.jstree('open_node', $("#"+currObj));
												} else {
													clickMoreForRedirect(false, currObj);
													
												}
											} else {
												var currnode = $("#"+currObj);
												if(currnode != null && currnode.length > 0) {
													treeContainerObj.jstree('select_node', $("#"+currObj));
													//$("#"+currObj).find(".jstree-icon")[0].click();
													//$("#"+currObj).find("a")[0].click();
													$("#"+currObj).find("a").click();
												} else {
													clickMoreForRedirect(true, currObj);
													
												}
											}
											$("#tempTree").val(newTempTree);
										} else {
											isOpeningComplete = true;
										}
										if(isOpeningFromRemote && isOpeningComplete) {
											treeContainerObj.jstree('select_node', $("#"+currObj));
											//$("#"+currObj).find(".jstree-icon")[0].click();
											//$("#"+currObj).find("a")[0].click();
											$("#"+currObj).find("a").click();
										} 
										if(redirectLevel == 1  && tempTree != '' && tempTree != null) {
											$("#"+currObj).find("a").click();
											$("#tempTree").val('');
											
										}
								   }	
									// END : open tree on clicking # of users in org screen
									
									//Code to select the first node on load
									if((redirectLevel == 1 || redirectLevel == 0) || objectTree == null) {
										selectTheFirstNodeOfTree($("#treeViewForOrg > ul > li:first").attr("id"));
									}
									if($("#isRedirectedTree").val()=='Yes'){
										//$("#isRedirectedTree").val("No");
									}
																			
							}
						}
							
					},
					 
					"themes": { theme: "apple", dots: true },
					"plugins" : [ "themes", "json_data", "ui" ]		
					
				});
		});
		// =============== THIS FUNCTION CLICKS "more" LINK IN JS-TREE UNTILL THE NODE IS VISIBLE ===================
		function clickMoreForRedirect(lastnode, currSelectedNode) {
			var currObj;
			$.each(objectTreeStack, function(index) {
				if(currSelectedNode == objectTreeStack[index]) {
					currObj = objectTreeStack[index-1];
				}
			});
			$("span[treespanlevel^="+currObj+"]").click();
			var currnode = $("#"+currSelectedNode);
			if(lastnode) {
				if(currnode != null && currnode.length > 0) {
					treeContainerObj.jstree('select_node', $("#"+currSelectedNode));
					//$("#"+currSelectedNode).find("a")[0].click();
					$("#"+currSelectedNode).find("a").click();
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			} else {
				if(currnode != null && currnode.length > 0) {
					treeContainerObj.jstree('open_node', $("#"+currSelectedNode));
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			}
		}
		
		//Function to select the first node of the tree
		var isFirstTime=true;
		function selectTheFirstNodeOfTree(nodeId){
		 if (isFirstTime){
			$("#"+nodeId+" "+"a").click();
			  isFirstTime = false;
			}
		}
		
		function onClickMoreLink(divId,index)
		{
			$("#"+divId+" [treeSpanLevel="+index+"].jstree-more-link").click(function(e) {		//alert('more');			
				var nodeJsonData = new Array();			
				var jsTreeNodeLevel = $(this).attr("treeSpanLevel");
				var tempNode = $(this).parent("span.no-click");
				var moreIndex = $(".no-click").index(tempNode);
				var nodeTempArr = jsTreeJsonArr[jsTreeNodeLevel][moreIndex];

				var startLimit = parseInt(nodeTempArr.indexCount);	
				//alert(startLimit);				
				nodeJsonData = nodeTempArr.nodeJsonData.slice(startLimit,(startLimit+maxNodeLimit));		
				var node = $(tempNode).prev();
				var nodesHTML = getJstreeNodes(nodeJsonData,divId,$(tempNode).attr("treelevel"));
				//alert(nodesHTML);		
				$(node).after(nodesHTML);			
				if(nodeJsonData.length == maxNodeLimit && nodeTempArr.nodeJsonData.length != (startLimit+maxNodeLimit))
				{		
					jsTreeJsonArr[jsTreeNodeLevel][moreIndex].indexCount = (startLimit+maxNodeLimit);				
				}
				else {
					//$(node).addClass("jstree-last");
					$(tempNode).remove();
					$(node).nextAll(":last").addClass("jstree-last");							
				}		
			});											   
		}
		
		function getJstreeNodes(nodeJsonData,divId,linkIndex)
		{
			var html = "";
			var jsTreeClassName = "";
			var chkLen = $("#"+divId+" [treelevel="+linkIndex+"]").parent("ul").parent("li.jstree-checked").length;	
			for(var index=0; index<nodeJsonData.length; index++) {
				var nodeDetails = nodeJsonData[index];
				if(chkLen > 0) {		
					jsTreeClassName = "jstree-checked ";
				}
				else {
					jsTreeClassName = "jstree-unchecked ";
				}
				jsTreeClassName += ((nodeDetails.attr.className != undefined) ? nodeDetails.attr.className : "");
				if(nodeDetails.attr.className == "jstree-leaf") {		
					jsTreeClassName += " jstree-open";
				}
				else {		
					jsTreeClassName += " jstree-closed";
				}
				if(nodeDetails.attr.assignedtotest == 1) {		
					jsTreeClassName += " jstree-makebold";
				}
				//html += "<li "+((nodeDetails.attr.assignedtotest == 1)? "assignedtotest=1": "")+" "+((nodeDetails.attr.redoflag == 1)? "redoflag=1": "")+" id="+nodeDetails.attr.id+" label="+nodeDetails.attr.label+" level="+nodeDetails.attr.level+" title='"+nodeDetails.attr.title+"' class='"+jsTreeClassName+"'><ins class='jstree-icon'>&nbsp;</ins><a href='#' class=''><ins class='jstree-checkbox' "+((nodeDetails.attr.assignedtotest == 1)? "style='display:none;'" : "")+">&nbsp;</ins><ins class='jstree-icon'>&nbsp;</ins>"+nodeDetails.data+"</a></li>";
				html +='<li createdby="'+nodeDetails.attr.createdBy+'" udatedby="'+nodeDetails.attr.udatedBy+'" tenantid="'+nodeDetails.attr.tenantId+'" parenttenantid="'+nodeDetails.attr.parentTenantId+'" noofchildorgs="'+nodeDetails.attr.noOfChildOrgs+'" noofusers="'+nodeDetails.attr.noOfUsers+'" orglevel="'+nodeDetails.attr.orgLevel+'" id="'+nodeDetails.attr.id+'" class="jstree-closed jstree-last"><ins class="jstree-icon">&nbsp;</ins><a href="#" class=""><ins class="jstree-icon">&nbsp;</ins>'+nodeDetails.data+'</a></li>';
				jsTreeClassName = "";
			}
			return html;
		}

	//======================================CREATE TREE STURCTURE ENDS================================
	
	//============================= FETCH NEXT HIERARCHY IN THE SLIDING MENU =============================
	$("#slide_menu_user > #org_details li").live("click", function(e) {
	
		e.stopImmediatePropagation();
		e.stopImmediatePropagation();
	    if ($(".tenantHier").hasClass("blue-gradient")) {
			$(".tenantHier").removeClass("blue-gradient");
		}
		//$(this).addClass("blue-gradient");
		var id = $(this).attr("id");
		var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
		showLoading( $(this) );
		$.ajax({
			type : "GET",
			url : "tenantHierarchy.do",
			data : nodeid,
			dataType : 'json',
			cache:false,
			success : function(data) {
				if (!$.isEmptyObject(data)) {
					getChildOrg(id, data);
					
					startSlide(id);
					hideLoading();
				}
				//getUser(id);								
			},
			error : function(data) {
				hideLoading();
				$.modal.alert(strings['script.org.error']);
			}
		});
	});
	
	// create child hierarchy DOM
	function getChildOrg(id, data) {
		var content = '';
		$.each(data, function() {
			content += '<li id=' + this.tenantId
					+ ' class="with-right-arrow grey-arrow tenantHier"><span id='
					+ this.tenantId + ' class="icon grey icon-size2 icon-folder"></span> <b class="glossy with-tooltip tracked" ' + 'id='
					+ this.tenantId + ' title = "' + this.tenantName + '" tenantName = "' + this.tenantName + '" orgLevel = ' + this.orgLevel + '>' + manageTenantName(this.tenantName) + '</b></li>';
		});
		$("#" + id).append('<ul class="files-list mini">' + content + '</ul>');
	}
	
	//============================= FETCH ALL USERS FOR SELECTED ORG FOR THE SLIDING MENU =============================
	$("#slide_menu_user > #org_details li b").live("click", function(e) {
		e.stopImmediatePropagation();
		
			var tenantName=$(this).attr("tenantName");
			var orgLevel = $(this).attr("orgLevel");
			var tenantId = $(this).attr("id");
			var id = $(this).attr("id"); 
			
	    if ($(".tenantHier").hasClass("blue-gradient")) {
			$(".tenantHier").removeClass("blue-gradient");
		     }
		$(this).parent().addClass("blue-gradient");
		updateOrgNameHeading(tenantName);
		updateAddUserHeading(tenantName,orgLevel,tenantId);
		fetchAllUsers(id);
		
	});
	
	
	//============================= FETCH ALL USERS FOR SELECTED ORG FOR THE TREE MENU =============================
	$("#treeViewForOrg a").live("click", function(e) {
			$("#searchUser").val("");
			if($.browser.msie) {
				$("#searchUser").focus();
				$("#searchUser").blur();
			}
			e.stopImmediatePropagation();
			var tenantName=$(this).text();
			var orgLevel = $(this).parent().attr("orglevel");
			var tenantId = $(this).parent().attr("id");
			var id = $(this).parent().attr("id");
			updateOrgNameHeading(tenantName);
			if($("#slide_menu_org").hasClass("organizations")){
				fetchAllOrgUsers(id,"orgChildren.do");// in manageOrganizations.js
			}
			else if($("#slide_menu_parent").hasClass("parents")){
				$("#searchParent").val("");
				if($.browser.msie) {
					$("#searchParent").focus();
					$("#searchParent").blur();
				}
				fetchAllParentUsers(id,"getParentDetailsOnScroll.do");// in manageParent.js
			}
			else if($("#slide_menu_student").hasClass("students")){
				$("#searchStudent").val("");
				if($.browser.msie) {
					$("#searchStudent").focus();
					$("#searchStudent").blur();
				}
				fetchAllStudents(id,$("#redirectedStudentBioId").val(),$("#isRedirectedTree").val(),"getStudentDetailsOnScroll.do");// in manageStudent.js
		    }
			else{
				updateAddUserHeading(tenantName,orgLevel,tenantId);
				fetchAllUsers(id,"getUserDetails.do");
			}
			
			
		
	});
	//================================SHOW THE SELECTED ORG NAME ON THE TOP OF THE TABLE =====================
	function updateOrgNameHeading (tenantName)
		{
			if($('#showOrgNameUser') != null) $('#showOrgNameUser').html("Users of " + tenantName);
			if($('#showOrgName') != null) $('#showOrgName').html("Organizations of " + tenantName);
		}
		
		
	//================================SHOW THE SELECTED ORG NAME ON ADD USER MODAL =====================
	function updateAddUserHeading (tenantName,orgLevel,tenantId)
		{
			$('#addUser').attr( 'tenantName', tenantName);
			$('#addUser').attr( 'orgLevel', orgLevel);
			$('#addUser').attr( 'tenantId', tenantId);
			$('#orgLevel').val(orgLevel);
		}
	
	//====================================AJAX CALL TO FETCH ALL THE USERS FOR THE SELECTED ORG====================
	function fetchAllUsers(id,url)
		{
			blockUI();
			var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
			$("tbody#user_details").find("tr").remove();
			$("#report-list").trigger("update");
			$("tbody#user_details").addClass("loader big");
			$.ajax({
				type : "GET",
				url : url,
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					if(data != null && data.length >= 1){
						showHideDataTable('show');
					}
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
					$("tbody#user_details").removeClass();	
					$.modal.alert(strings['script.org.error']);
					unblockUI();
				}
			});
		}
	//================================ CREATE USER ROW DOM ========================================
	// ********** This function is moved to init.jsp ***************** //
	/*function getUserDetails(checkFirstLoad, data) {
		var userContent = '';
		if (checkFirstLoad) {
			$("#user_details").find("tr").remove();
		}
		
		$.each(data, function () { 
		    
			userContent += '<tr id ='+ this.tenantId+'_'+this.userId+' scrollid= '+ this.loggedInOrgId+'_'+this.userName +' class="abc" >'
							+'<th scope="row">' + this.userName +'</th>'
							+'<td>' + this.userDisplayName +'</td>'
							+ createStatusTag(this.status)
							+'<td class="hide-on-tablet-portrait">'+this.tenantName+'</td>'
							//+ createUserTypeTag(this.userType)
							+ createUserRolesTag(this.availableRoleList)
							+'<td class="vertical-center">'
								+' <span class="button-group compact">' 
									+' <a id="'+ this.userId +'" tenantId ="' + this.tenantId + '" href="#" class="button icon-pencil with-tooltip edit-User" title="Edit"></a> '
									+' <a id="'+ this.userId +'" param="'+ this.userName +'" href="#" class="button icon-users icon with-tooltip login-as" title="Login as User"></a>'
									+' <a id="'+ this.userId +'" userName="'+ this.userName + '" parentId="' + this.parentId + '" tenantId ="' + this.tenantId +'" href="#" class=" button icon-trash with-tooltip confirm delete-User" title="Delete"></a>'  
								+' </span>'
							+'</td>'
						+'</tr>'
						
						 
		});
		$("#user_details").append(userContent);
		$("#report-list").trigger("update");
		$(".headerSortDown").removeClass("headerSortDown");
		$(".headerSortUp").removeClass("headerSortUp");
		setLastRowId ($("#last_user_tenant"));
	}*/
	
	//============================THIS FUNCTION CREATES THE ROLE TAGS FOR THE USER TABLE IN "MANAGE USERS" PAGE==========================
	function hasAdmin(availableRoles){
		var isAdmin = false;
		for (var j=0; j<availableRoles.length; j++){
			var tokens = availableRoles[j].roleName.split("_");
			//alert(tokens);
			//for (var i=0; i<tokens.length; i++){
				if (tokens[1] == "ADMIN"){
					return true;
				}
			//}
		};
		return isAdmin;
	}
	function createUserRolesTag(availableRoles){
		var hasAdminRole = hasAdmin(availableRoles);
		var roleTag='<td class="roleContainerForUsers vertical-center">';
		//alert("roles=" + JSON.stringify(availableRoles));
		//alert(hasAdminRole);
		if(hasAdminRole == false) {
			$.each(availableRoles, function (){
				if(this.roleName== 'ROLE_ACSI'){
					roleTag +='<small class="tag blue-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_CTB'){
					roleTag +='<small class="tag green-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_SCHOOL'){
					roleTag +='<small class="tag anthracite-bg  role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_CLASS'){
					roleTag +='<small class="tag grey-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_PARENT'){
					roleTag +='<small class="tag red-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_ADMIN'){
					roleTag +='<small class="tag orange-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
				if(this.roleName== 'ROLE_USER'){
					roleTag +='<small class="tag black-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
			});
		} else {
			$.each(availableRoles, function (){
				if(this.roleName== 'ROLE_ADMIN'){
					roleTag +='<small class="tag orange-bg role '+ this.roleName+'">'+this.label + ' ' +this.roleDescription+'</small><br/>';
				}
			});
		}
		roleTag +="</td>";
		return roleTag;
	}
	
	
	function createUserTypeTag(userType) {
		var typeTag = "";
		if(userType == 'ACSI USER') {
			typeTag = '<td><small class="tag blue-bg">'+userType+'</small></td>';	
		}
		if(userType == 'SCHOOL USER') {
			typeTag = '<td><small class="tag orange-bg">'+userType+'</small></td>';	
		}
		if(userType == 'TEACHER USER') {
			typeTag = '<td><small class="tag grey-bg">'+userType+'</small></td>';	
		}	
		return typeTag;
	}
	
	function setLastRowId (obj){
		obj.val($(".abc:last").attr("scrollid"));
	}
	
	
	function retainUniqueValue()
	{
			var seen = {};
			$('.abc').each(function() {
			var txt = $(this).attr("scrollid");
			if (seen[txt])
				$(this).remove();
			else
				seen[txt] = true;
			});

	}
	//========================================SHOWING AND HIDE LOADER=======================
	function showLoader(currentScrollTop)
	{
		$('div#last_msg_loader').css("top",currentScrollTop+400);
		$('div#last_msg_loader').html('<img src="themes/acsi/img/standard/loaders/loading64.gif">');"parentid=" + $(".abc:last").attr("id");
	}
	function hideLoader()
	{
		$('div#last_msg_loader').html('<!--<img src="themes/acsi/img/standard/loaders/loading64.gif">-->');
	}
	
	//============================= ENABLE TABLE SORTING =============================
	function enableSorting(flag) {
		if (flag) {
	
			var usersTable = $("#report-list");
			usersTable.trigger("update");
		}
	
	}
	
	function startSlide(id) {
	
		var animate = true;
		var clicked = id,
		// LI element
		li = $("#" + id).closest('li'),
	
		// Sub-menu
		submenu = li.children('ul:first'),
	
		// List of all ul above the current li
		allUL = li.parentsUntil('.navigable', 'ul'),
	
		// Current li ul
		parentUL = allUL.eq(0),
	
		// Main ul
		mainUL = allUL.eq(-1),
	
		// Root navigable element
		root = mainUL.closest('.navigable'),
	
		// Back button
		back = root.children('.back'),
	
		// Load indicator
		load = root.children('.load'),
	
		// Other vars
		current, url;
	
		if (submenu.length > 0) {
			// If not ready yet
			if (parentUL.outerHeight(true) == 0) {
				// Delay action
				setTimeout(function() {
					clicked.click();
	
				}, 40);
				return;
			}
	
			// Set as current
			root.data('navigableCurrent', submenu);
	
			// Hide previously open submenus
			parentUL.find('ul').hide();
	
			// Display parent menus
			allUL.show();
	
			// Display it
			submenu.show();
	
			// Correct position if needed
			submenu.add(allUL.not(':last')).each(
					function(i) {
						var menu = $(this), parent = menu.parent();
	
						if ($.inArray(parent.css('position'), [ 'relative',
								'absolute' ]) > -1) {
							menu.css('top', -parent.position().top + 'px');
						}
					});
	
			/*
			 * Animation
			 */
	
			// Set root element size according to target size
			root.stop(true).height(
					parentUL.outerHeight(true) + back.outerHeight(true))[animate ? 'animate'
					: 'css']({
				height : (submenu.outerHeight(true) + back.outerHeight()) + 'px'
			});
	
			// Move whole navigation to reveal target ul
			mainUL.stop(true)[animate ? 'animate' : 'css']({
				left : -(allUL.length * 100) + '%'
			});
	
			// Show back button
			back[animate ? 'animate' : 'css']({
				marginTop : 0
			});
	
			// Send open event
			li.trigger('navigable-open');
	
		}
	}
	
	function manageTenantName(tenantName) {
		if (tenantName.length >= 18 ) {
			tenantName = tenantName.substring(0,15) + "...";
		}
		return tenantName;
	} 
	
	function createStatusTag(status) {
		var statusTag = "";
		if (status == "AC" ) {
			//statusTag = '<td class="hide-on-tablet"><small class="tag green-bg">' + 'Enabled' + '</small></td>';
			//statusTag = '<td><small class="tag green-bg">' + 'Enabled' + '</small></td>';
			statusTag = '<span class="enable with-tooltip tooltip-left" title="Enabled"></span>';
		} else {
			//statusTag = '<td class="hide-on-tablet"><small class="tag red-bg">' + 'Disabled' + '</small></td>';
			//statusTag = '<td><small class="tag red-bg">' + 'Disabled' + '</small></td>';
			statusTag = '<span class="disable with-tooltip tooltip-left" title="Disabled"></span>';
		}
		return statusTag;
	}
	
	// ======================== RELOAD JS-TREE ON SELECTING ADMIN YEAR ====================== 
	function reloadOrgTree(selectedObj) {
		blockUI();
		// patch to remove tooltip
		$("#AdminYear").removeTooltip(true, true);
		$("#AdminYear").mouseenter();
		$("#AdminYear").mouseleave();
		
		$("#orgMode").removeTooltip(true, true);
		$("#orgMode").mouseenter();
		$("#orgMode").mouseleave();
		// END : patch to remove tooltip
		
		/* Fixed QC defect - Joy
		 * By changing TA/org mode, data table retains for non-public mode though logged in user is public and vice versa
		 */
		showHideDataTable('hide');
		//End Fixed QC defect - Joy
		
		var adminYear = $(selectedObj).val();
		var queryData = "AdminYear="+adminYear+"&orgMode="+$("#orgMode").val();
		$.ajax({
			type : "GET",
			url : "updateAdminYear.do",
			data : queryData,
			dataType : 'json',
			cache:false,
			success : function(data) {
				unblockUI();
				isFirstTime = true;
				treeContainerObj.jstree("refresh");
			},
			error : function(data) {
				unblockUI();
				$.modal.alert(strings['script.org.error']);
			}
		});
	}
	
	/* Fixed QC defect - Joy
	 * By changing TA/org mode, data table retains for non-public mode though logged in user is public and vice versa
	 */
	function showHideDataTable(purpose){
		if(purpose === 'show'){
			$("#moreStudent").show();
			$("#moreUser").show();
			$("#moreOrg").show();
			$("#moreParent").show();

			$('#studentTable').show();
			$('#userTable').show();
			$('#orgTable').show();
			$('#parentTable').show();
		}else if(purpose === 'hide'){
			$("#moreStudent").hide();
			$("#moreUser").hide();
			$("#moreOrg").hide();
			$("#moreParent").hide();

			$('#studentTable').hide();
			$('#userTable').hide();
			$('#orgTable').hide();
			$('#parentTable').hide();
		}
	}
	
	
	/*This function used for SSO user to check class level*/
	function hasClass(availableRoles){
		var isClass = false;		
		$.each(availableRoles, function (){
			if(this.label== 'CLASS'){
				isClass = true;
				return isClass;
			}			
		});
		return isClass;
	}