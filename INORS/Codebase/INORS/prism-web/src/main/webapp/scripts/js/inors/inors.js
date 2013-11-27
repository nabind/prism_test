
// ================ Create Tree ==========================
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

var chkTreeContainerObj;

$(document).ready(function() {
	createTree();
	
	$(".customRefresh").live("click", function(event) {
		event.stopImmediatePropagation();
		$(document).click();
		var count = $(this).attr("count");
		var tabCount = $(this).attr("tabCount");
		blockUI('new-tab'+tabCount+'');
		var reportUrl = $(this).attr("param");
		var reportName = $(this).attr("reportName");
		var reportId = $(this).attr("reportId");
		var formObj = $('.report-form-'+count);
		var obj = $('.report-frame-'+count);
		
		var dataURL = reportUrl + '&reportId='+reportId + '&reportName='+reportName+'&filter=true&' + $(formObj).serialize();
		$(obj).attr('src','groupDownloads.do?reportUrl='+dataURL);
	});
	
	$("#downloadBulkPdf").live("click", function() {
		downloadBulkPdf('M', 'GD');
	});
	$("#downloadBulkPdfSeperate").live("click", function() {
		downloadBulkPdf('S', 'GD');
	});
	
	$("#downloadBulkICPdf").live("click", function() {
		downloadBulkPdf('M', 'IC');
	});
	$("#downloadBulkICPdfSeperate").live("click", function() {
		downloadBulkPdf('S', 'IC');
	});
	
	$("#downloadCandidateReport").live("click", function() {
		downloadBulkPdf('CR', 'CR');
	});
	
	
	$(".download-instructions").live("click", function() {
		$(".accordion-body").slideToggle(500);
		$(".icon-plus-round").toggle();
		$(".icon-minus-round").toggle();
	});
	
	// this is to retain group download files field values
	$("#groupFile").live("change", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		retainDownloadValues();
	});
	$("#collationHierarchy").live("change", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$(document).click();
		retainDownloadValues();
	});
	$("#fileName").live("blur", function(event) {
		retainDownloadValues();
	});
	$("#email").live("blur", function(event) {
		retainDownloadValues();
	});
	
	$("#downloadStudentFileXML").live("click", function() {
		var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=XML&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileXML").attr("href", href);
	});
	$("#downloadStudentFileCSV").live("click", function() {
		var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=CSV&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileCSV").attr("href", href);
	});
	$("#downloadStudentFileDAT").live("click", function() {
		var startDate = $("#p_Start_Date").val();
		var endDate = $("#p_End_Date").val();
		var href = "downloadStudentFile.do?type=DAT&startDate=" + startDate + "&endDate=" + endDate;
		$("#downloadStudentFileDAT").attr("href", href);
	});
	/*$(".jqdatepicker").glDatePicker({
		onClick: function(target, cell, date, data) {
			var shortMonths = new Array("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
			target.val(date.getDate() + '-' + shortMonths[date.getMonth()] + '-' + date.getYear());
			if(data != null) {
				alert(data.message + '\n' + date);
			}
		}            
	});*/
	$('.jqdatepicker').datepicker({
		changeMonth: true,
	    changeYear: true
    });
	
	$('#corpDiocese').live('change',function(){
		populateSchool();
	});
	
	$("#downloadGRTFile").live("click", function() {
		var testAdministration = $("#testAdministration").val();
		var testProgram = $("#testProgram").val();
		var corpDiocese = $("#corpDiocese").val();
		var school = $("#school").val();
		//alert("testAdministration="+testAdministration+"\ntestProgram="+testProgram+"\ncorpDiocese="+corpDiocese+"\nschool="+school);
		var href = "downloadGRTInvitationCodeFiles.do?type=GRT&testAdministration=" + testAdministration + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadGRTFile").attr("href", href);
	});
	$("#downloadICFile").live("click", function() {
		var testAdministration = $("#testAdministration").val();
		var testProgram = $("#testProgram").val();
		var corpDiocese = $("#corpDiocese").val();
		var school = $("#school").val();
		//alert("testAdministration="+testAdministration+"\ntestProgram="+testProgram+"\ncorpDiocese="+corpDiocese+"\nschool="+school);
		var href = "downloadGRTInvitationCodeFiles.do?type=IC&2013&testAdministration=" + testAdministration + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadICFile").attr("href", href);
	});
});

// =============== retain group download files field values =====================
function retainDownloadValues() {
	var formObj = $('#groupDownload');
	$.ajax({
		type : "GET",
		url : 'retainDownloadValues.do',
		data : $(formObj).serialize(),
		dataType: 'json',
		cache:false,
		success : function(data) {
			// do nothing
		},
		error : function(data) {
			// do nothing
		}
	});
}

// ================== Download Bulk PDFs =====================
function downloadBulkPdf(type, mode) {
	$("li.active").click();
	$(".error-message").hide(200);
	$(".success-message").hide(200);
	blockUI();
	
	if(type != 'CR' && $('.jstree-checked').length == 0) {
		// check default school
		chkTreeContainerObj.jstree("check_all");
	}
	var formObj = $('#groupDownload');
	$(formObj).validationEngine();
	if(formObj.validationEngine('validate')) {
		var url = 'downloadBulkPdf.do';
		if(type != 'CR') {
			var checked_ids = []; 
			$("#chkTreeViewForOrg").jstree("get_checked",null,true).each(function () { 
				checked_ids.push(this.id + '|' + $(this).attr("orglevel")); 
			}); 
			$("#selectedNodes").val(checked_ids.join(","));
		} else {
			url = 'downloadCandicateReport.do';
		}
		$.ajax({
			type : "GET",
			url : url,
			data : $(formObj).serialize()+"&fileType="+type+"&mode="+mode,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if (data.status == 'Success')	{
					$(".success-message").show(200);
				} else {
					$(".error-message").show(200);
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
		
		
	}
	event.stopPropagation();
	event.preventDefault();
}

//======================================CREATE  TREE STURCTURE================================
	function createTree() {	
		chkTreeContainerObj =$("#chkTreeViewForOrg");
		$(function (){
			$("#slide_menu_user").removeClass("navigable");
			$("#slide_menu_org").removeClass("navigable");
            $("#slide_menu_parent").removeClass("navigable");
            $("#slide_menu_student").removeClass("navigable");
			chkTreeContainerObj.jstree( {                  
					"json_data" : {                       
						//"data":jsondata ,
						"maxNodeLimit" : maxNodeLimit,
						"clickedNode" : function (n) {
											return (n.attr ? n.attr("id") : chkTreeContainerObj.attr("rootid"));
										},	
						"ajax" : { 
							"url" : "groupDownloadHierarchy.do",
							"cache":false,
							"data" : function (n) {
								nodeLevel = (n.attr ? n.attr("orglevel") : "-99");
								clickedNode = (n.attr ? n.attr("id") : chkTreeContainerObj.attr("rootid"));
								return {
											"tenantId" : clickedNode,
											"reportUrl": chkTreeContainerObj.attr("reportUrl"),
											"nodeLevel": nodeLevel
									   };
								},									
							"success": function(data, textStatus, XMLHttpRequest) {										 
								if(data != null && data != "") {
									if(data.length > maxNodeLimit)
									{													
										//alert(data.length);
										tempNodes = data;
										tempIndex = clickedNode;
									}
								}								
							},
							"complete" : function() { 
									$("#chkTreeViewForOrg ul:first").css("float", "left");
									$("#chkTreeViewForOrg ul:first").css("min-width", "100%");
									$("#chkTreeViewForOrg [id="+clickedNode+"]").removeClass("jstree-closed").addClass("jstree-open");
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
									onClickMoreLink("chkTreeViewForOrg",tempIndex);
									
									var rootOrg = $("#chkTreeViewForOrg > ul > li:first").attr("id");
									var tempTree = $("#tempTree").val();
								
									unblockUI();
																			
							}
						}
							
					},
					 
					"themes": { theme: "apple", dots: true },
					"plugins" : [ "themes", "json_data", "checkbox", "ui" ]		
					
				});
		});
		
		getNodeCount();
		
	}
	
	// =============== find selected node count ======================
	function getNodeCount() {
		chkTreeContainerObj.bind("open_node.jstree close_node.jstree check_node.jstree uncheck_node.jstree", function(e) {
			var stud = 0, cls = 0, sch = 0;
			$('.jstree-checked').each(function() {
				if($(this).attr('orglevel') == -1) stud++;
				if($(this).attr('orglevel') == 4) cls++;
				if($(this).attr('orglevel') == 3) sch++;
			});
			$('.jstree-undetermined').each(function() {
				if($(this).attr('orglevel') == 4) cls++;
				if($(this).attr('orglevel') == 3) sch++;
			});
			$("#studCount").html(stud);
			$("#classCount").html(cls);
			$("#schoolCount").html(sch);
			console.log('stud:'+stud+' cls:'+cls+' sch:'+sch);
		});
	}
		
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
					chkTreeContainerObj.jstree('select_node', $("#"+currSelectedNode));
					//$("#"+currSelectedNode).find("a")[0].click();
					$("#"+currSelectedNode).find("a").click();
				} else {
					clickMoreForRedirect(lastnode, currSelectedNode);
				}
			} else {
				if(currnode != null && currnode.length > 0) {
					chkTreeContainerObj.jstree('open_node', $("#"+currSelectedNode));
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
				//jsTreeClassName += ((nodeDetails.attr.className != undefined) ? nodeDetails.attr.className : "");
				if(nodeDetails.attr.className == "jstree-leaf") {		
					jsTreeClassName += " jstree-leaf";
				}
				else {		
					jsTreeClassName += " jstree-closed";
				}
				if(nodeDetails.attr.assignedtotest == 1) {		
					jsTreeClassName += " jstree-makebold";
				}
				//html += "<li "+((nodeDetails.attr.assignedtotest == 1)? "assignedtotest=1": "")+" "+((nodeDetails.attr.redoflag == 1)? "redoflag=1": "")+" id="+nodeDetails.attr.id+" label="+nodeDetails.attr.label+" level="+nodeDetails.attr.level+" title='"+nodeDetails.attr.title+"' class='"+jsTreeClassName+"'><ins class='jstree-icon'>&nbsp;</ins><a href='#' class=''><ins class='jstree-checkbox' "+((nodeDetails.attr.assignedtotest == 1)? "style='display:none;'" : "")+">&nbsp;</ins><ins class='jstree-icon'>&nbsp;</ins>"+nodeDetails.data+"</a></li>";
				html +='<li createdby="'+nodeDetails.attr.createdBy+'" udatedby="'+nodeDetails.attr.udatedBy+'" tenantid="'+nodeDetails.attr.tenantId+'" parenttenantid="'+nodeDetails.attr.parentTenantId+'" noofchildorgs="'+nodeDetails.attr.noOfChildOrgs+'" noofusers="'+nodeDetails.attr.noOfUsers+'" orglevel="'+nodeDetails.attr.orgLevel+'" id="'+nodeDetails.attr.id+'" class="'+jsTreeClassName+'"><ins class="jstree-icon">&nbsp;</ins><a href="#" class=""><ins class="jstree-checkbox">&nbsp;</ins><ins class="jstree-icon">&nbsp;</ins>'+nodeDetails.data+'</a></li>';
				jsTreeClassName = "";
			}
			return html;
		}
		//=============================DELETE Group Files ON CLICK======================
		$('.delete-GroupFiles').live("click", function() {
		    var row = $(this);
			var jobId = $(this).attr("jobId");
			$.modal.confirm("Confirm delete?" ,
				function () {
				deleteGroupFilesDetails(jobId,row);
				},function() {//this function closes the confirm modal on clicking cancel button
				} 
			);
			
		});	
		
		//=============================AJAX CALL TO DELETE Group Files FROM DB TABLES=============================
			function deleteGroupFilesDetails(jobId,row) {
					blockUI();
					$.ajax({
						type : "GET",
						url : 'deleteGroupDownloadFiles.do',
						data : "jobId="+jobId,
						dataType: 'html',
						cache:false,
						success : function(data) {
							var obj = jQuery.parseJSON(data);
							if(obj.status == 'Success') {
								$.modal.alert('Report deleted successfully.');
								unblockUI();
								deleteRowValues(row);//this method is present in manageUser.js
							} else {
								$.modal.alert('Error while deleting the file');
								unblockUI();
							}
							unblockUI();
						},
						error : function(data) {
							$.modal.alert('Error while deleting the file');
							unblockUI();
						}
					});
				}

function populateSchool(){
	var parentOrgNodeId = $('#corpDiocese').val();
	if (parentOrgNodeId != "-1") {
		var dataUrl = 'parentOrgNodeId=' + parentOrgNodeId;
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateSchool.do',
			data : dataUrl,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.length > 0) {
					populateSchoolDropdownByJson($('#school'),data,1);
				} else {
					$.modal.alert("No School Found for this Corp/Diocese");
				}
				unblockUI();
			},
			error : function(data) {
				$.modal.alert(strings['script.common.error']);
				unblockUI();
			}
		});
	}
}

function populateSchoolDropdownByJson(elementObject,jsonDataValueName,plsSelectFlag,clearFlag){
	elementObject.empty();
	var option = "";
	if((typeof plsSelectFlag !== 'undefined') && (plsSelectFlag == 1)){
		option += "<option value='-1'>Please Select</option>";
	}
	
	if((typeof clearFlag === 'undefined')) {
		if(jsonDataValueName != null) {
			if ((jsonDataValueName != "") && (jsonDataValueName.length > 0)) {
				$.each(jsonDataValueName, function(index, data) {
					option += '<option value='+data.value+'>'+data.name+'</option>';
			    });
			}else{
				alert("here");
				$.modal.alert(strings['script.common.empty']);
			}
		}
	}
	
	elementObject.html(option);
	elementObject.change();
	elementObject.trigger('update-select-list');
}