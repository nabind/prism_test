
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
	
	// get home page message - after page is loaded
	openInorsHomePage();
	
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

	/* Extra action call in happening - Commented the function  */
	// GRT/IC File Download
	//populateGrtDropdownsOnLoad();
	
	
	$("#testAdministration").live("change", function(event) {
		showHideDivs();
	});
	$("#testProgram").live("change", function(event) {
		var testProgram = $("#testProgram").val();
		populateDistrictGrt(testProgram)
	});
	$('#corpDiocese').live('change',function(){
		var testProgram = $("#testProgram").val();
		var corpDiocese = $("#corpDiocese").val();
		populateSchoolGrt(testProgram, corpDiocese);
	});
	$("#school").live("change", function(event) {
		showHideDivs();
	});
	$("#downloadGRTFile").live("click", function() {
		var testAdministration = $("#testAdministration").val();
		var testProgram = $("#testProgram").val();
		var corpDiocese = $("#corpDiocese").val();
		var school = $("#school").val();
		var href = "downloadGRTInvitationCodeFiles.do?type=GRT&testAdministration=" + testAdministration + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadGRTFile").attr("href", href);
	});
	$("#downloadICFile").live("click", function() {
		var testAdministration = $("#testAdministration").val();
		var testProgram = $("#testProgram").val();
		var corpDiocese = $("#corpDiocese").val();
		var school = $("#school").val();
		var href = "downloadGRTInvitationCodeFiles.do?type=IC&2013&testAdministration=" + testAdministration + "&testProgram=" + testProgram + "&corpDiocese=" + corpDiocese + "&school=" + school;
		$("#downloadICFile").attr("href", href);
	});

	/* Extra action call in happening - Commented the function  */
	// Group Download
	//populateGDDropdownsOnLoad();
	
	$("#testAdministrationGD").live("change", function(event) {
	});
	
	$("#testProgramGD").live("change", function(event) {
		var testProgram = $("#testProgramGD").val();
		if (testProgram && testProgram != "-1") { // then populate Corp/Diocese
			populateDistrictGD(testProgram)
		}
	});
	
	$('#corpDioceseGD').live('change', function() {
		var corpDiocese = $("#corpDioceseGD").val();
		if (corpDiocese && corpDiocese != "-1") { // then populate school
			populateSchoolGD(corpDiocese);
		}
	});
	
	$('#schoolGD').live('change', function() {
		var schoolId = $("#schoolGD").val();
		if (schoolId && schoolId != "-1") { // then populate class
			populateClassGD(schoolId);
		}
	});
	
	$('#classGD').live('change', function() {
		populateStudentTableGD();
	});
	
	/* Extra action call in happening - Commented the function  */
	//populateGradeGD();
	
	$('#check-all').change(function() {
		var checkboxes = $(this).closest('form').find(':checkbox');
		if ($(this).is(':checked')) {
			checkboxes.attr('checked', 'checked');
		} else {
			checkboxes.removeAttr('checked');
		}
	});
	
	$("#downloadSeparatePdfsGD").live("click", function() {
		$("#buttonGD").val("P");
		groupDownloadFunction('P');
	});
	
	$("#downloadCombinedPdfsGD").live("click", function() {
		$("#buttonGD").val("C");
		groupDownloadFunction('C');
	});
	
	$("#downloadSinglePdfsGD").live("click", function() {
		$("#buttonGD").val("S");
		groupDownloadFunction('S');
	});
});

// =============== get inors home page message =====================
function openInorsHomePage() {
	if($("#inorsHome").length > 0) {
		$.ajax({
			type : "GET",
			url : "loadHomePage.do",
			data : null,
			dataType : 'html',
			cache:false,
			success : function(data) {
				$("#inorsHome").html(data);						
			},
			error : function(data) {
				$("#inorsHome").html("<p class='big-message icon-warning red-gradient'>Error getting home page content. Please try later.</p>");
			}
		});
	}
}

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
		
		//=============================Download Group Files ON CLICK======================
		
		function eraseCache(){
			  window.location = window.location.href+'?eraseCache=true';
			}
		
		$('.download-GroupFiles').live("click", function() {
			var row =  $(this);
			var jobId = row.attr("jobId");
			var filePath = row.attr("filePath");
			var fileName = row.attr("fileName");
			var availability = downloadGroupFilesDetails(row,jobId,filePath,fileName);
			if(availability == true) {
				var href = "downloadGroupDownloadFiles.do?"+'jobId='+jobId+'&fileName='+fileName+'&filePath='+filePath;
				$(".download-GroupFiles").attr("href", href);
			} else {
				$(".download-GroupFiles").attr("href", "#");
				$.modal.alert('File not found in the filepath mentioned');
			}
		});
		
		function downloadGroupFilesDetails(row,jobId,filePath,fileName) {
			var availability = false;
				$.ajax({
					type : "GET",
					url : 'checkFileAvailability.do',
					data : "filePath="+filePath,
					dataType: 'html',
					async : false,
					success : function(data) {
						var obj = jQuery.parseJSON(data);
						if(obj.status == 'Success') {
							availability = true;
						}
					}
				});
				return availability;
			}
		
		$('.view-requestdetails').live("click", function() {
			var row = $(this);
			var jobId = $(this).attr("jobId");
			viewrequestDetails(jobId);
		});
		
		function viewrequestDetails(jobId)
		{
				    manageIconIE('icon-star');
				    var param = "jobId=" + jobId;	
				    blockUI();
				    $.ajax({
						type : "GET",
						url : "getRequestDetailViewData.do", 
						data : param,
						dataType : 'json',
						cache:false,
						success : function(data) {
							unblockUI();
							$("textarea#requestSummary").val(data[0].requestSummary);
							$("#viewRequestDetail").modal({
								title: 'View Request Details',
								height: 350,
								width: 600,
								resizable: true,
								draggable: true,
							});		
								
						},
						error : function(data) {
							$.modal.alert(strings['script.common.error1']);
						}
					})	
		}
		
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
								$.modal.alert('Job deleted successfully.');
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
			function deleteRowValues(row) {
				row.closest("tr").remove();
			}
			
			// ================ Remove duplicate icons in IE ============================
			function manageIconIE(icon) {
				if($.browser.msie) {
					$('.'+icon).html('');
					$('.'+icon+' > .empty').hide();
				}
			}

/**
 * Custom Ajax call. Returns a json from the server.
 * 
 * @param requestType
 *            http request type: "GET", "POST", "PUT" or "DELETE"
 * @param requestUrl
 *            utl to the request. Example: populateDistrictGrt.do
 * @param inputData
 *            data to be sent to the server
 * @param outputDataType
 *            type of the response data
 * @param browserCache
 *            boolean. Whether the output will be cached by the browser.
 * @param asyncRequest
 *            boolean. Whether the request is asynchronous.
 * @param errMsg
 *            meaasage on $.ajax error. Example: "Server responds in Error"
 * @returns json server response data
 * @author <a href="mailto:amitabha.roy@tcs.com">Amitabha Roy</a>
 */
function customAjaxCall(requestType, requestUrl, inputData, outputDataType, browserCache, asyncRequest, errMsg) {
	var json = {};
	if (asyncRequest == false)
		blockUI();
	$.ajax({
		type : requestType,
		url : requestUrl,
		data : inputData,
		dataType : outputDataType,
		cache : browserCache,
		async : asyncRequest,
		success : function(data) {
			json = data;
			if (asyncRequest == false)
				unblockUI();
		},
		error : function(data) {
			if (asyncRequest == false)
				unblockUI();
			$.modal.alert(errMsg);
		}
	});
	return json;
}

/**
 * Populates a Dropdown. Written to be used in pages:
 * <ul>
 * <li>GRT/IC File Download</li>
 * <li>Group Download</li>
 * </ul>
 * 
 * @param element
 *            html dropdown element.
 * @param json
 *            the json data from which the dropdown will be populated
 * @param selectValue
 *            value of the selectText
 * @param selectText
 *            optional "Please Select" option in the dropdown
 * @param showId
 *            boolean. Whether to show the value along with the text in the
 *            dropdown
 */
function populateDropdownByIdWithJson(element, json, selectValue, selectText, showId) {
	element.empty();
	var option = "";
	if (selectValue && selectText){
		option += '<option value=' + selectValue + '>' + selectText + '</option>';
	}
	if ((json != null) && (json != "") && (json.length > 0)) {
		$.each(json, function(index, data) {
			if (showId && showId == true) {
				option += '<option value='+data.value+'>'+data.name+' ('+data.value+')</option>';
			} else {
				option += '<option value=' + data.value + '>' + data.name + '</option>';
			}
		});
	}
	element.html(option);
	element.change();
	element.trigger('update-select-list');
}

/**
 * This function loads the values for all dropdowns in "GRT/IC File Download"
 * page in case of page onLoad handler.
 */
function populateGrtDropdownsOnLoad() {
	var testAdministration = $("#testAdministration").val();
	if (testAdministration && testAdministration != "-1") { // then populate test program
		populateTestProgramGrt();
	}
	var testProgram = $("#testProgram").val();
	if (testProgram && testProgram != "-1") { // then populate district
		populateDistrictGrt(testProgram);
	}
	var corpDiocese = $("#corpDiocese").val();
	if (corpDiocese && corpDiocese != "-1") { // then populate school
		populateSchoolGrt(testProgram, corpDiocese);
	}
	var school = $("#school").val();
	if (school) { // then display download links
		// No code here
		// It will be handled by the onChange handler of $("#school")
	}
}

function populateTestProgramGrt() {
	// As of now hard coded, but we can call customAjaxCall() to hit the database.
	var json = [ {
		"value" : "1",
		"name" : "Public Schools"
	}, {
		"value" : "0",
		"name" : "Non Public Schools"
	} ];
	populateDropdownByIdWithJson($("#testProgram"), json);
}

/**
 * Populates the "Corp/Diocese" drop down in "GRT/IC File Download" page
 * 
 * @param testProgram
 */
function populateDistrictGrt(testProgram) {
	if (testProgram == "-1") {
		populateDropdownByIdWithJson($("#corpDiocese"), null, "-1", "Please Select Test Program");
	} else {
		var dataUrl = "testProgram=" + testProgram;
		var json = customAjaxCall("GET", "populateDistrictGrt.do", dataUrl, "json", false, false, "Server responds in Error");
		if ((json != null) && (json.length > 0)) {
			populateDropdownByIdWithJson($("#corpDiocese"), json, "-1", "Please Select");
		} else {
			$.modal.alert("No Corp/Diocese Found for Test Program");
			populateDropdownByIdWithJson($("#corpDiocese"), null, "-1", "Please Select Test Program");
		}
	}
}

/**
 * Populates the "School" drop down in "GRT/IC File Download" page
 * 
 * @param testProgram
 * @param districtId
 */
function populateSchoolGrt(testProgram, districtId) {
	if (districtId == "-1") {
		populateDropdownByIdWithJson($("#school"), null, "-1", "Please Select Corp/Diocese");
	} else {
		var dataUrl = 'testProgram=' + testProgram + '&districtId=' + districtId;
		var json = customAjaxCall("GET", "populateSchoolGrt.do", dataUrl, "json", false, false, "Server responds in Error");
		if ((json != null) && (json.length > 0)) {
			populateDropdownByIdWithJson($("#school"), json, "-1", "Please Select");
		} else {
			$.modal.alert("No Corp/Diocese Found for Test Program");
			populateDropdownByIdWithJson($("#school"), null, "-1", "Please Select Corp/Diocese");
		}
	}
}

/**
 * This function loads the values for all dropdowns in "Group Download" page in
 * case of page onLoad handler.
 */
function populateGDDropdownsOnLoad() {
	populateTestAdministrationGD(); // Default
	var testAdministration = $("#testAdministrationGD").val();
	//alert(testAdministration);
	if (testAdministration && testAdministration != "-1") { // then populate test program
		populateTestProgramGD();
	}
	var testProgram = $("#testProgramGD").val();
	if (testProgram && testProgram != "-1") { // then populate district
		populateDistrictGD(testProgram);
	}
	var corpDiocese = $("#corpDioceseGD").val();
	if (corpDiocese && corpDiocese != "-1") { // then populate school
		populateSchoolGD(corpDiocese);
	}
	var school = $("#schoolGD").val();
	if (school) { // then display class
		populateClassGD(school);
	}
	var klass = $("#classGD").val();
	if (klass) { // then display student table
		populateStudentTableGD();
	}
}

/**
 * Populates the Test Administration dropdown
 */
function populateTestAdministrationGD() {
	var json = customAjaxCall("GET", "populateTestAdministrationGD.do", "", "json", false, false, "Server responds in Error");
	if ((json != null) && (json.length > 0)) {
		populateDropdownByIdWithJson($("#testAdministrationGD"), json);
	} else {
		$.modal.alert("No Test Administration Found");
		populateDropdownByIdWithJson($("#testAdministrationGD"), null, "-1", "Please Select");
	}

}

/**
 * Populates the Test Program dropdown
 */
function populateTestProgramGD() {
	// As of now hard coded, but we can call customAjaxCall() to hit the database.
	var json = [ {
		"value" : "1",
		"name" : "Public Schools"
	}, {
		"value" : "0",
		"name" : "Non Public Schools"
	} ];
	populateDropdownByIdWithJson($("#testProgramGD"), json);
}

/**
 * Populates the "Corp/Diocese" drop down in "Group Download" page
 * 
 * @param testProgram
 *            value of the "Test Program" dropdown
 */
function populateDistrictGD(testProgram) {
	populateDropdownGD(testProgram, "-1", "Please Select", "-1",
			"Please Select Test Program", $("#corpDioceseGD"), "GET",
			"populateDistrictGD.do", "json", false, false,
			"Server responds in Error", "No Corp/Diocese Found for Test Program", false);
}

/**
 * 
 * @param parentVal
 *            value of parent dropdown
 * @param selectValue
 *            value for selectText = -1
 * @param selectText
 *            Example: "Please Select"
 * @param selectNullValue
 *            value for selectNullText = -1
 * @param selectNullText
 *            Example: "Please Select Test Program"
 * @param element
 *            html dropdown element. Example: $("#corpDioceseGD")
 * @param requestType
 *            Example:
 * @param requestUrl
 *            Example: "populateSchoolGD.do"
 * @param outputDataType
 *            Example: "json"
 * @param browserCache
 *            boolean
 * @param asyncRequest
 *            boolean
 * @param errMsg
 *            meaasage on $.ajax error. Example: "Server responds in Error"
 * @param emptyMsg
 *            Example: "No Corp/Diocese Found for Test Program"
 * @param showId
 *            boolean. Whether to show the value along with the text in the
 *            dropdown
 * @author <a href="mailto:amitabha.roy@tcs.com">Amitabha Roy</a>
 */
function populateDropdownGD(parentVal, selectValue, selectText,
		selectNullValue, selectNullText, element, requestType, requestUrl,
		outputDataType, browserCache, asyncRequest, errMsg, emptyMsg, showId) {
	if (parentVal == selectNullValue) {
		populateDropdownByIdWithJson(element, null, selectNullValue, selectNullText, showId);
	} else {
		var transferObject = getGroupDownloadTO();
		var responseJson = customAjaxCall(requestType, requestUrl,
				transferObject, outputDataType, browserCache, asyncRequest,
				errMsg);
		if ((responseJson != null) && (responseJson.length > 0)) {
			populateDropdownByIdWithJson(element, responseJson, selectValue, selectText, showId);
		} else {
			$.modal.alert(emptyMsg);
			populateDropdownByIdWithJson(element, null, selectNullValue, selectNullText, showId);
		}
	}
}

/**
 * Populates the School dropdown
 * 
 * @param districtId
 */
function populateSchoolGD(districtId) {
	populateDropdownGD(districtId, "-1", "Please Select", "-1",
			"Please Select Corp/Diocese", $("#schoolGD"), "GET",
			"populateSchoolGD.do", "json", false, false,
			"Server responds in Error", "No School Found for this Corp/Diocese", false);
}

/**
 * Populates the Class dropdown
 * 
 * @param schoolId
 */
function populateClassGD(schoolId) {
	populateDropdownGD(schoolId, "-1", "Please Select", "-1",
			"Please Select School", $("#classGD"), "GET",
			"populateClassGD.do", "json", false, false,
			"Server responds in Error", "No Class Found for this School", true);
}

/**
 * Populates the Grade dropdown
 */
function populateGradeGD() {
	populateDropdownGD("", "-1", "Please Select", "-1",
			"Please Select", $("#gradeGD"), "GET",
			"populateGradeGD.do", "json", false, false,
			"Server responds in Error", "No Data Found", false);
}

/**
 * Populates the Student Table
 */
function populateStudentTableGD(){
	var orgNodeId = $('#classGD').val();
	if (orgNodeId != "-1") {
		var transferObject = getGroupDownloadTO();
		blockUI();
		$.ajax({
			type : "GET",
			url : 'populateStudentTableGD.do',
			data : transferObject,
			dataType: 'json',
			cache:false,
			success : function(data) {
				if(data.length > 0) {
					populateStudentTableByJson(data);
					$("#studentTableGD").removeClass('hidden');
					$("#studentTableGD").show();
				} else {
					$.modal.alert("No Student Found for this Class");
					$("#studentTableGD").hide();
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

/**
 * 
 * @param json
 */
function populateStudentTableByJson(json) {
	var elementObject = $('#studentListGD');
	elementObject.empty();
	var rows = "";
	var count = 0;
	if (json != null) {
		if ((json != "") && (json.length > 0)) {
			$.each(json, function(index, data) {
				count = count + 1;
				if (count%2 == 0)
					rows += '<tr class="even">';
				else
					rows += '<tr class="odd">';
				rows += '<th scope="row" class="checkbox-cell  sorting_1"><input name="checked[]" id="check-student-' + data.value+ '" value="'+data.value+'" type="checkbox" /></th>';
				rows += '<td>' + data.name + '</td>';
				rows += '</tr>';
			});
		} else {
			$.modal.alert(strings['script.common.empty']);
		}
	}
	elementObject.html(rows);
}

/**
 * Show/Hide GRT/IC Layout/Download Links/Buttons
 */
function showHideDivs() {
	var schoolId = $("#school").val();
	if((schoolId) && schoolId != '-1') {
		$("#icDiv").removeClass('hidden');
		$("#grtDiv").removeClass('hidden');
		$("#icDiv").show();
		$("#grtDiv").show();
		var testAdministration = $("#testAdministration").val();
		if (testAdministration) {
			var tokens = testAdministration.split("~");
			if ("2010" == tokens[2]) {
				$("#icLinks").html('');
				$("#grtLinks").html('<a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>');
			} else if ("2011" == tokens[2]) {
				$("#icLinks").html('');
				$("#grtLinks").html('<a class="button" id="grt2011" href="/inors/staticfiles/ISTEP S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2010-11 GRT File Record Layout</a><br />');
			} else if ("2012" == tokens[2]) {
				$("#icLinks").html('');
				$("#grtLinks").html('<a class="button" id="grt2012" href="/inors/staticfiles/ISTEP S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a><br />');
			} else if ("2013" == tokens[2]) {
				if("ISTEP" == tokens[0] && "Spring" == tokens[1]) {
					$("#icLinks").html('<a class="button" id="ic2013" href="/inors/staticfiles/S2012-13 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 Invitation Code File Record Layout</a><br />');
				} else {
					$("#icLinks").html('');
				}
				$("#grtLinks").html('<a class="button" id="grt2013" href="/inors/staticfiles/ISTEP S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a><br />');
			} else {
				$("#icDiv").hide();
				$("#grtDiv").hide();
				$.modal.alert('Unknown Test Administration');
			}
		}
	} else {
		$("#icDiv").hide();
		$("#grtDiv").hide();
	}
}

/**
 * GroupDownloadTO Constructor
 */
function GroupDownloadTO(button, testAdministration, testProgram, district, school, klass, grade, students, groupFile, collationHierarchy, fileName, email) {
	this.button = button;
	this.testAdministration = testAdministration;
	this.testProgram = testProgram;
	this.district = district;
	this.school = school;
	this.klass = klass;
	this.grade = grade;
	this.students = students;
	this.groupFile = groupFile;
	this.collationHierarchy = collationHierarchy;
	this.fileName = fileName;
	this.email = email;
}

/**
 * 
 * @returns {GroupDownloadTO}
 */
function getGroupDownloadTO() {
	var button = $("#buttonGD").val();
	var testAdministration = $("#testAdministrationGD").val();
	var testProgram = $("#testProgramGD").val();
	var district = $("#corpDioceseGD").val();
	var school = $("#schoolGD").val();
	var klass = $("#classGD").val();
	var grade = $("#gradeGD").val();
	var students = $("input[id^=check-student-]:checked").map(
			function() {
				return this.value;
			}
		).get().join(",");
	var groupFile = $("#groupFile").val();
	var collationHierarchy = $("#collationHierarchy").val();
	var fileName = $("#fileName").val();
	var email = $("#email").val();
	var to = new GroupDownloadTO(button, testAdministration, testProgram, district, school, klass, grade, students, groupFile, collationHierarchy, fileName, email);
	return to;
}

/**
 * 
 * @param button
 */
function groupDownloadFunction(button) {
	var json = getGroupDownloadTO();
	blockUI();
	$.ajax({
		type : "GET",
		url : 'groupDownloadFunction.do',
		data : json,
		dataType : 'json',
		cache : false,
		success : function(data) {
			if (data.length > 0) {
				alert(JSON.stringify(data));
			} else {
				$.modal.alert("No File Found");
				$("#studentTableGD").hide();
			}
			unblockUI();
		},
		error : function(data) {
			$.modal.alert(strings['script.common.error']);
			unblockUI();
		}
	});
}