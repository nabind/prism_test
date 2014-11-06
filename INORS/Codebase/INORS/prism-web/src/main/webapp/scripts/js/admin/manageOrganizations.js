/**
 * This js file is for manage organizations screen
 * Author: Tata Consultancy Services Ltd.
 * Version: 1
 */
$(document).ready(function() {
	
	if($("#orgTable").length > 0) {
		
		$('#org-list').tablesorter({
			// pass the headers argument and assign an object
	        headers: { 
	            // assign the fourth column (we start counting zero)
	            3: { 
	                // disable it by setting the property sorter to false
	                sorter: false 
	            }
	        } 
		});
		// hide the right side menu
		$('.clearfix').addClass('menu-hidden');
		$("ul#shortcuts li").removeClass("current");
		$(".shortcut-settings").parent().addClass("current");
		var orgCount = $("tbody#org_details").find("tr");
		if(orgCount != null && orgCount.length >= moreCount) {
			$(".pagination").show(200);
			$("#moreOrg").removeClass("disabled");
			if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
		} else {
			$(".pagination").hide(200);
		}
		if(orgCount == 0) {
			updateOrgNameHeading("-1");
		}
		//=======================SCROLL FOR THE ORGANISATION TABLE==============================
		var tempScrollTop, currentScrollTop = 0
		$("#moreOrg").click(function() {
			currentScrollTop = $("#orgTable").scrollTop();
			if(!$(this).hasClass('disabled')) {
				blockUI();
				var lastid ="tenantId=" + $('input#last_org_tenant').val()+"&AdminYear="+$("#AdminYear").val()+"&searchParam="+$("#searchOrg").val(); 
				//showLoader(currentScrollTop);//This method is defined in usermodule.js
				$.ajax({
					type : "GET",
					url : "orgChildren.do",
					data : lastid,
					dataType : 'json',
					cache:false,
					success : function(data) {
						unblockUI();
						if (data != null && data.length > 0){
							buildOrgDOM(false, data);
							enableSorting(true);
							retainUniqueValue();
							//setLastRowId ();
							//hideLoader();//This method is defined in usermodule.js
							$("#orgTable").animate({scrollTop: currentScrollTop+600}, 500);
						} else {
							$("#moreOrg").addClass("disabled");
							if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
						}
						if (data != null && data.length < moreCount) {
							// check if this is the last set of result
							$("#moreOrg").addClass("disabled");
							if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
						}
					
					},
					error : function(data) {
						unblockUI();
						$.modal.alert(strings['script.common.error']);
						//hideLoader();//This method is defined in usermodule.js
					}
				});
	
			}
			tempScrollTop = currentScrollTop;
		});
	}
	resetPrismActions();
});	

	 
		
		
		// update the organization tree on click on a Organization
		$(".org_list li").live("click", function(e) {
			e.stopImmediatePropagation();
			
			 if ($(".tenantHier").hasClass("blue-gradient")) {
					$(".tenantHier").removeClass("blue-gradient");
				}
			var id = $(this).attr("id");
			var nodeid = "tenantId=" + id + "&AdminYear="+$("#AdminYear").val();
			
			showLoading( $(this) );
			blockUI();
			$.ajax({
				type : "GET",
				url : "orgChildren.do",
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					unblockUI();
					if (!$.isEmptyObject(data)) {
						updateOrgTree(id, data);
						$(".org_list li").addClass("tenantHier");
						startSlide(id);
					}
					hideLoading();
				},
				error : function(data) {
					unblockUI();
					hideLoading();
					$.modal.alert(strings['script.org.error']);
				}
			});
		});
		
		// Update the organization details on click on a organization name
		/*$(".org_list li b").live("click", function(e) {
			e.stopImmediatePropagation();
			//var id = $(this).parent().attr("id");
			if ($(".tenantHier").hasClass("blue-gradient")) {
					$(".tenantHier").removeClass("blue-gradient");
				}
			$('#showOrgName b').text("");
			$('#showOrgName b').text("Child Organizations for " + $(this).text());
			$(this).parent().addClass("blue-gradient");
			$("tbody#org_details").find("tr").remove();
			$("tbody#org_details").addClass("loader big");
			//var id = $(this).attr("id");
			var nodeid = "tenantId=" + id;
			$.ajax({
				type : "GET",
				url : "orgChildren.do",
				data : nodeid,
				dataType : 'json',
				success : function(data) {
					if (!$.isEmptyObject(data)) {
						buildOrgDOM(true,data);
						
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data) {
					$("tbody#org_details").removeClass();
					$.modal.alert('Error occurred while populating organization children.');
				}
			});
		});*/
		
		
		//===========================Populates the table on click on a organization name========================
		function fetchAllOrgUsers(id,urlPath)
		{
			blockUI();
			$("#searchOrg").val("");
			if($.browser.msie) {
				$("#searchOrg").focus();
				$("#searchOrg").blur();
			}
			$("tbody#org_details").find("tr").remove();
			$("tbody#org_details").addClass("loader big");
			$("#org-list").trigger("update");
			var nodeid = "tenantId=" + id + "&AdminYear=" + $("#AdminYear").val();
			$.ajax({
				type : "GET",
				url : urlPath,
				data : nodeid,
				dataType : 'json',
				cache:false,
				success : function(data) {
					unblockUI();
					if(data.length >= 1){
						showHideDataTable('show');
					}
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
						$("#moreOrg").removeClass("disabled");
						if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
					} else {
						$(".pagination").hide(200);
					}
					if (!$.isEmptyObject(data)) {
						buildOrgDOM(true,data);
						
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data) {
					unblockUI();
					$("tbody#org_details").removeClass();
					$.modal.alert(strings['script.org.error']);
				}
			});
		}
		
		// create child hierarchy DOM and update the sliding tree
		function updateOrgTree(id, data) {
			var content = '';
			if(data.length >= moreCount) {
				$("#moreOrg").removeClass("disabled");
				if($.browser.msie) $("#moreOrg").removeClass("disabled-ie");
			} else {
				$("#moreOrg").addClass("disabled");
				if($.browser.msie) $("#moreOrg").addClass("disabled-ie");
			}
			$.each(data, function() {
				content += '<li id=' + this.tenantId
						+ ' class="with-right-arrow grey-arrow"><span id='
						+ this.tenantId + ' class="icon grey icon-size2 icon-folder"></span> <b id='
						+ this.tenantId + '>' + this.tenantName + '</b></li>';
			});
			$("#" + id).append('<ul class="files-list mini">' + content + '</ul>');
		}
		
		// update Organization details 
		// Create user row DOM             
		function updateOrgDetails( data) {
			var userContent = '';
			$("tbody#org_details").find("tr").remove();
			$("#org-list").trigger("update");
			$.each(data, function () { 
				userContent += '<tr id ='+ this.tenantId+' class="abc" >'
								+'<th scope="row">' + this.tenantId +'</th>'
								+'<td>' + this.tenantName +'</td>'
								+'<td>'+ this.noOfChildOrgs+'</td>'
								+'<td><a style="text-decoration:underline" href="redirectToUser.do?AdminYear='+$("#AdminYear").val()+'&nodeId='+this.tenantId+'&parentOrgId='+this.parentTenantId+'" >'+this.noOfUsers+'</a></td>'
								+'</tr>'
			});
			$("tbody#org_details").append(userContent);
		}
		
		// search organization............START
		
		$("#searchOrg").autocomplete({
			source: function(request, response) {
	            $.ajax({
	                url: "searchOrgAutoComplete.do",
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
				searchOrg(ui.item.value);
				$(".pagination").hide(200);
			},
			focus:function(event, ui) {
				var searchString = ui.item.value;
				$("#searchOrg").val(searchString);
				event.preventDefault();
				event.stopPropagation();
			}
		});
		//------------binding the click event with the search field icon, it will search the org when the enter key is pressed
		$("#searchOrg").live("keyup", function(e) {
			if ( e.keyCode == 13 && $(this).val()!="") {
				$(".ui-autocomplete").hide();
				searchOrg($("#searchOrg").val());
			}
		});
		//------------binding the click event with the search field icon, it will search the org when search button is clicked
		$("a[param^='search_icon_org']").live("click", function(e) {
			if ( $("#searchOrg").val()!="" && $("#searchOrg").val()!="Search") {
				$(".ui-autocomplete").hide();
				searchOrg($("#searchOrg").val());
			}
		});
		
		

		function searchOrg(searchString) {
			blockUI();
			$("tbody#org_details").find("tr").remove();
			$("#org-list").trigger("update");
			$("tbody#org_details").addClass("loader big");
			$.ajax({
				type : "GET",
				url : "searchOrganization.do",
				data : "searchString="+searchString+"&selectedOrg="+$("a.jstree-clicked").parent().attr("id")+"&AdminYear="+$("#AdminYear").val(),
				dataType : "json",
				cache:false,
				success : function(data){
					unblockUI();
					//$(".pagination").hide(200);
					if (data != null && data.length >= moreCount){
						$(".pagination").show(200);
						$("#moreStudent").removeClass("disabled");
						if($.browser.msie) $("#moreStudent").removeClass("disabled-ie");
					} else {
						$(".pagination").hide(200);
					}
					if ( data != null) {
						buildOrgDOM(true,data);
					}
					$("tbody#org_details").removeClass();
				},
				error : function(data){
					unblockUI();
					$.modal.alert(strings['script.org.searchError']);
					$("tbody#org_details").removeClass();
				}
			});
		}

// ================================= SHOW LOADING IN MENU ===============================
function showLoading(obj) {
	var root = $(".navigable"),
		load = $('<div class="load orgLoad white-gradient" style="height:50px; background-color: #ecedf2;"></div>').appendTo(root),
		li = $(obj).closest('li'),
		allUL = li.parentsUntil('.navigable', 'ul');
	//$("#org_list").stop(true)['animate']({ left: -((allUL.length-1)*100+10)+'%' });
}
//================================= HIDE LOADING IN MENU ===============================
function hideLoading() {
	$('.orgLoad').remove();
	//$("#org_list").stop(true)['animate']({ right: -((allUL.length-1)*100+10)+'%' });
}

function resetPrismActions() {
	var adminYear = (typeof $('#AdminYear').val() !== 'undefined') ? $('#AdminYear').val() : 0;
	var queryData = "AdminYear="+adminYear;
	blockUI();
	$.ajax({
		type : "GET",
		url : "resetPrismActions.do",
		data : queryData,
		dataType : 'json',
		cache : false,
		async : false,
		success : function(data) {
			resetJspElements(data);
		},
		error : function(data) {
			$.modal.alert(strings['script.org.error']);
		}
	});
	unblockUI();
}

function resetJspElements(data){
	if(strings['manage.orgs.usercount'] == data['Manage Organizations User Count']){
		$("#MANAGE_ORGS_USER_COUNT").val(data['Manage Organizations User Count']);
	}else{
		$("#MANAGE_ORGS_USER_COUNT").val("");
	}
	if(strings['manage.edu.center.users.edit.user'] == data['Manage Education Center Users Edit User']){
		$("#EDU_EDIT_USER").val(data['Manage Education Center Users Edit User']);
	}else{
		$("#EDU_EDIT_USER").val("");
	}
	if(strings['manage.edu.center.users.login.as.user'] == data['Manage Education Center Users Login As User']){
		$("#EDU_LOGIN_AS").val(data['Manage Education Center Users Login As User']);
	}else{
		$("#EDU_LOGIN_AS").val("");
	}
	if(strings['manage.edu.center.users.delete.user'] == data['Manage Education Center Users Delete User']){
		$("#EDU_DELETE_USER").val(data['Manage Education Center Users Delete User']);
	}else{
		$("#EDU_DELETE_USER").val("");
	}
	showHideJspElements();
}

function showHideJspElements(){
	if($("#MANAGE_ORGS_USER_COUNT").val() == strings['manage.orgs.usercount']) {
		$("#th_MANAGE_ORGS_USER_COUNT").show();
	} else {
		$("#th_MANAGE_ORGS_USER_COUNT").hide();
	}
	if ($("#EDU_EDIT_USER").val() != strings['manage.edu.center.users.edit.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('.icon-pencil').hide();
	} else {
		$('.icon-pencil').show();
	}
	if ($("#EDU_LOGIN_AS").val() != strings['manage.edu.center.users.login.as.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('.icon-users').hide();
	} else {
		$('.icon-users').show();
	}
	if ($("#EDU_DELETE_USER").val() != strings['manage.edu.center.users.delete.user'] && $('#educationTab').val() != "" && $('#educationTab').val() == "educationUserTab") {
		$('.icon-trash').hide();
	} else {
		$('.icon-trash').show();
	}
}

//Create organization DOM table
function buildOrgDOM(checkFirstLoad, data) {
	var orgContent = '';
	if (checkFirstLoad) {
		$("tbody#org_details").find("tr").remove();
		$("#org-list").trigger("update");
	}
	
	$.each(data, function () {
	    
		orgContent += '<tr id ='+ this.parentTenantId+'_'+this.tenantId+' scrollid ='+ this.selectedOrgId+'_'+this.tenantId +' class="abc" >'
						+'<th scope="row">' + this.tenantId +'</th>'
						+'<td>' + this.tenantName +'</td>'
						+'<td>'+this.noOfChildOrgs+'</td>'
						//+ buildRedirectToUserLink(this.tenantId,this.parentTenantId,this.noOfUsers)
		if($("#MANAGE_ORGS_USER_COUNT").val() == strings['manage.orgs.usercount']) {
			orgContent += '<td><span class="button-group compact"><a tenantId="'+this.tenantId+'" id="count_'+this.tenantId+'" orgname="'+this.tenantName+'" parentTenantId="'+this.parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">' +strings['label.userCount']+'</a></span></td>';
		}
		showHideJspElements();
		orgContent += '</tr>';
	});
	$("tbody#org_details").append(orgContent);
	$("#org-list").trigger("update");
	$(".headerSortDown").removeClass("headerSortDown");
	$(".headerSortUp").removeClass("headerSortUp");
	setLastRowId ($("#last_org_tenant"));      //this method is defined in usermodule.js
}
 // click to view number of users in organisation screen
	$(".view-UserNumber").live("click",function(e){
		
		var tenantId=$(this).attr("tenantId");
		var parentTenantId=$(this).attr("parentTenantId");
		var orgname=$(this).attr("orgname");
		var adminYear=$("#AdminYear").val();
		$(this).closest('td').attr("id","count_"+tenantId);
		$(this).closest('td').html('<span class="loader"></span>');
		$.ajax({
			type : "GET",
			url : "getUserCount.do",
			data : "tenantId="+tenantId+"&adminYear="+adminYear,
			dataType : "json",
			cache:false,
			success : function(data){
				
				if (data != null){
				
					var count = buildRedirectToUserLink(tenantId,parentTenantId,data[0].noOfUsers);
					$("#count_"+tenantId).html(count);
					
					//=========OPEN THE MODAL========
					/*$("#organisationModal").modal({
						title: '# of Users for '+orgname,
						resize:true,
						draggable: false,
						onOpen:buildUserCountDom(data,tenantId,parentTenantId,orgname,"organisationModal","organisationModalContainer"),
						buttons: {
								'Close': {
									classes: 'glossy',						
									click: function(win) {
										win.closeModal();
									}
								}
						}
					});*/
					
				}
				else {
					$.modal.alert(strings['msg.err.userCount']);
					var buttonTag ='<span class="button-group compact"><a tenantId="'+tenantId+'" id="count_'+tenantId+'" orgname="'+orgname+'" parentTenantId="'+parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">'+strings['label.userCount']+'</a></span>'
					$("#count_"+tenantId).html(buttonTag);
				}
				
			},
			error : function(data){
				var buttonTag ='<span class="button-group compact"><a tenantId="'+tenantId+'" id="count_'+tenantId+'" orgname="'+orgname+'" parentTenantId="'+parentTenantId+'" href="#nogo" class="button with-tooltip view-UserNumber" title="'+strings['title.viewUserNumber']+'">'+strings['label.userCount']+'</a></span>'
				$("#count_"+tenantId).html(buttonTag);							
				$.modal.alert(strings['msg.err.userCount']);
				
			}
		});
		
	});

	function buildUserCountDom(data,tenantId,parentTenantId,orgname,modalId,modalContainerDivId){
		
		$("#"+modalId +" > "+"#"+modalContainerDivId ).find("div").remove();
		var content ='<div>'
						+'<p class="big-message" style="width:500px"><b>The # of users for</b>  <strong>'+orgname+'</strong>'+' <b>in</b>  '+'<b><strong>'+data[0].adminName+'</strong>  is </b> '
						+ buildRedirectToUserLink(tenantId,parentTenantId,data[0].noOfUsers)
						+'</p>';
				if (data[0].noOfUsers!="0"){
					 content +='<p class="message icon-speech blue-gradient glossy" style="width:500px">'
									+'<a class="close show-on-parent-hover" title="Hide message" href="#"></a>'	
									+strings['msg.clickRedirectManageUsers']
									+'</p>';
				}				
				content +='</div>';
		$("#"+modalId+ " > "+"#"+modalContainerDivId ).append(content);				
		
	}

	function buildRedirectToUserLink(tenantId,parentTenantId,noOfUsers)
	{	
		if(noOfUsers !="0"){
			return	'<a style="text-decoration:underline" href="redirectToUser.do?AdminYear='+$("#AdminYear").val()+'&nodeId='+tenantId+'&parentOrgId='+parentTenantId+'" >'+noOfUsers+'</a>'
		}
		else{
			return noOfUsers
		}
	}