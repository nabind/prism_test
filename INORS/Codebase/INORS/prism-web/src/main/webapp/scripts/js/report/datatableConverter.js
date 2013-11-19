var pressed = false,start = undefined,startX, startWidth,oTable,asInitVals = new Array();
var tableHeaderBg = '';
$(document).ready(function(){
	if($.template.ie7) { 
		$('span').addClass('ie7span'); 
	} else {
		$('span').addClass('otherspan'); 
	}
	
	if($(".jrtableframe").length > 0) {
		
		// for advanced filter
		var txt = "";
		for(var index=0, len=$(".columnHeader").length; index<len; index++) {
			txt += "<div style='display:none' id='filterDialog_"+index+"' class='filter-dialog'><div><label><input type='radio' class='radio' checked='true' name='filterType_"+index+"' data-type='all-row'/>Show all rows</label><label><input class='radio' type='radio' name='filterType_"+index+"' data-type='single-row'/>Show only rows where</label></div><div><select style='height:30px' class='filterOption' id='rowFilterType_"+index+"'></select><span class='small-margin-left' id='filter"+index+"'></span></div></div>";
			//txt += "<div style='display:none' id='filterDialog_"+index+"' class='filter-dialog'><div><label><input type='radio' class='radio' checked='true' name='filterType_"+index+"' data-type='all-row'/>Show all rows</label><label><input class='radio' type='radio' name='filterType_"+index+"' data-type='single-row'/>Show only rows where</label></div><div><select class='select' id='rowFilterType_"+index+"'></select><span class='small-margin-left' id='filter"+index+"'></span></div></div>";
		}
		$("body").append(txt);
		
		var rowFilterType = ["Equals","Greater than","Less than","Is between"];
		for(var i=0; i<$(".columnHeader").length; i++) {
			txt = "";
			var title = 'Filter by column : ' + $(".columnHeader").children().get(i).innerText;
			for(var index=0, len=rowFilterType.length; index<len; index++) {
				txt += "<option value='"+index+"'>"+rowFilterType[index]+"</option>";
			}	
			$("#rowFilterType_"+i).append(txt);
			/*$("#filterDialog"+i).dialog({
				modal:true,
				autoOpen: false,
				title: title,
				width:500,
				buttons: [],
				position: ['center',20]
			});	*/
			
		}
		
		// adjust iframe height
		var height = '1000px';
		$("select[name^=jsTable_length]").live("change","select",function() {
			var parentBody = window.parent.document.body;
			var height = '1000px';
			if(this.value == 20) height = '1000px';
			if(this.value == 30) height = '1350px';
			if(this.value == 40) height = '1700px';
			$(".report-frame:visible", parentBody).css({height: height, minHeight: height});
		});
		
		$(".filter-dialog").on("change","select",function() {
			var $this = $(this);
			var val = $this.val(), index = $this.attr("id").split("_")[1];
			var ele = $("#filter"+index).find(":text");
			for(var i=0, len = ele.length; i<len; i++) {
				ele.eq(i).val("");
			}
			if(val == 3) {
				$("#jsTable_range_from_"+index).attr("disabled",false);
				$("#jsTable_range_to_"+index).attr("disabled",false);
				$("#jsTable_range_from_"+index).animate({width:'100px'}, 500);
				$("#jsTable_range_to_"+index).animate({width:'100px'}, 500);
			} else if(val == 0 || val == 2) {
				$("#jsTable_range_from_"+index).attr("disabled",true);
				$("#jsTable_range_to_"+index).attr("disabled",false);
				$("#jsTable_range_from_"+index).animate({width:'0px'}, 500);
				$("#jsTable_range_to_"+index).animate({width:'200px'}, 500);
			} else if(val == 1) {
				$("#jsTable_range_from_"+index).attr("disabled",false);
				$("#jsTable_range_to_"+index).attr("disabled",true);
				$("#jsTable_range_from_"+index).animate({width:'200px'}, 500);
				$("#jsTable_range_to_"+index).animate({width:'0px'}, 500);
			}
		});

		$(".filter-dialog").on("keyup",":text",function() {
			var $this = $(this);
			var id = $this.attr("id");
			if(id != null) {
				var index = id.split("_")[3];
				if($("#rowFilterType_"+index).val() == "0" && id.indexOf("to") > -1) {
					var val = $this.val();
					$("#jsTable_range_from_"+index).val(val);
					$("#jsTable_range_from_"+index).trigger("keyup");
				}
			}
		});

		$(".filter-dialog").on("change",":radio",function() {
			var $this = $(this);
			var index = $this.attr("name").split("_")[1];
			var isDisabled = ($this.attr("data-type") == "all-row") ? true : false;
			if(tableHeaderBg == '') {
				tableHeaderBg = $(".columnheader-"+index).css('background');
				// patch for IE
				if(!tableHeaderBg) tableHeaderBg = 'url(themes/acsi/img/old-browsers/colors/bg_button.png) #d6dadf repeat-x';
			}
			if(isDisabled) {
				$(".columnheader-"+index).css('background', tableHeaderBg);
			} else {
				$(".columnheader-"+index).css('background', '#FFBB2B');
			}
			var onlyText = isDisabled;
			if($this.attr("data-type") != "all-row") {
				if(index == 1 || index == 3) {
					isDisabled = false;
					onlyText = false;
				} else {
					isDisabled = true;
					onlyText = true;
				}
			}
			
			$("#rowFilterType_"+index).val(0).attr("disabled",isDisabled);
			$("#rowFilterType_"+index).change();
			$("#rowFilterType_"+index).trigger('update-select-list');
			var input = $("#filter"+index).find(":text");
			for(var i=0, len=input.length; i<len; i++) {
				input.eq(i).val("").attr("disabled",isDisabled);
				if(index == 1 || index == 3) {
					   $("#jsTable_range_from_"+index).trigger("keyup");
				} else {
					   $("input").trigger("keyup");	
				}

				//$("#jsTable_range_to_"+index).trigger("keyup");
			}
			if(isDisabled) {			
				//oTable.fnFilterClear();
			} else {
				$("#jsTable_range_to_"+index).attr("disabled",false);
				$("#jsTable_range_from_"+index).attr("disabled",true);
			}
			
			if($this.attr("data-type") != "all-row" && onlyText) $("#filter"+index).find("input").attr("disabled", false);
		});
		
		makeTable();
	}
	// end : for advanced filter	
	
	$("#jsTable").on("click",".header-menu", function(e) {
		e.preventDefault();
		e.stopPropagation();
		e.stopImmediatePropagation();
		//var index = $(e.target).parent().parent().index();
		var index = $(this).attr("row");
		//$("#filterDialog"+index).dialog("open");
		$("#filterDialog_"+index).modal({
			title: 'Filter by : '+$(this).parent().get(0).innerText,
			resizable: false,
			blockerVisible: false,
			width: 412,
			buttons: {
				'Close': {
					classes: 'glossy',						
					click: function(win) {
						win.closeModal();
					}
				}
			},
			onOpen : function(e, ui) {
				$(this).css('top', '90px');
				var id = index;//$(this).attr("id").split("_")[1];
				if($(this).find(":radio:checked").attr("data-type") == "all-row") {
					var ele = $("#rowFilterType_"+id+", #jsTable_range_from_"+id+", #jsTable_range_to_"+id);
					for(var j=0, l=ele.length; j<l; j++) {
						ele.eq(j).attr("disabled",true);
						$("#filter"+id).find("input").attr("disabled", true);
					}
				}
			},
			onClose : function() {
				manageFilter(index);
			}
		});	
		
	 });
	
	$("tfoot input").keyup( function () {
		/* Filter on the column (the index) of this element */
		oTable.fnFilter( this.value, $("tfoot input").index(this) );
	} );
	
	$("tfoot input").each( function (i) {
	/* Input value pushed into array */
		asInitVals[i] = this.value;
	} );
	
	$("#jsTable").on("click",".columnheader, input", function(e) {
		/* Searchbox appears on clicking the table header and indicator is shown on sorting of rows*/
		var count,w;
		e.stopPropagation();
		if($(this).is("input")){
			count = $(this).parent().index();
		}else{
			count = $(this).index();
			//$("#jsTable thead th").find(".DataTables_sort_icon").addClass("css_both");
			//$("#jsTable thead th:eq("+count+")").find(".DataTables_sort_icon").removeClass("css_both");
			//var findClass = $("#jsTable thead th:eq("+count+")").find(".DataTables_sort_icon");
			/*if(findClass.attr("class").indexOf("css_both") < 0){
				if(findClass.attr("class").indexOf("css_down") < 0){
					findClass.removeClass("css_up");
					findClass.addClass("css_down");
				}else{
					findClass.removeClass("css_down");
					findClass.addClass("css_up");
				}
			}else{
				findClass.addClass("css_down");
				findClass.removeClass("css_both");
				
			}*/
		}
		if($.browser.msie){
			w = $("#jsTable thead th:eq("+count+")").width()-9;	
		}else{
			w = $("#jsTable thead th:eq("+count+")").width();
		}
		$("#jsTable tfoot th input").css("visibility","hidden");
		$("#jsTable tfoot th input:eq("+count+")").css({
			"visibility":"visible",
			"width": w-2
		});
	});
	
	/* Calculates the width of search boxes on load*/
	var headerWidth,headerWidth1;
	for(var index=0;index<$(".columnheader").length;index++){
		headerWidth = $("#jsTable thead th:eq("+index+")").css("width");
		$("#jsTable tfoot th input:eq("+index+")").css("width", parseInt(headerWidth,10)-4+"px");
	}
	
	/*Recalculates the position of search field for IE*/
	if($.browser.msie){
		$("table tfoot .searchRow").css("top","-75px");
	}	 
	
	// ============== CODES for HIGHCHART OPTIONS ======================== //
	$(".setting").live("click",function(){
		chartId = $(this).parent().attr("id");//.split("Highc_")[1];
		$( "#dialog-modal" ).modal({
			title: 'Change chart type',
			resizable: false,
			blockerVisible: false,
			buttons: {
				'Close': {
					classes: 'glossy',						
					click: function(win) {
						win.closeModal();
					}
				}
			},
			onOpen : function(e, ui) {
				$(this).css('top', '90px');
			}
		});
	});
	$(".highcharts_parent_container").mouseenter(function(){
		$(this).prepend("<div id = 'setting' class = 'setting'></div>");
	});
	$(".highcharts_parent_container").mouseleave(function(){
		$(this).find("#setting").remove();
	});
	$("div.icon").click(function(){
		var chartType = $(this).attr("id").split("_")[0],stacking;
		var plotOptions = $(this).attr("class").split(" ")[2];
		var isInverted = $(this).attr("class").split(" ")[3];
		if(plotOptions == "basic"){
			stacking = "";
		}else{
			stacking = plotOptions;
		}
		var chart = $("#"+chartId).highcharts();
		
		var oldSeries = chart.series;
		var oldOption = chart.options;
		
		oldOption.chart.inverted = (isInverted === 'inverted')? true : false;
		if(oldOption.chart.defaultSeriesType == 'bar') {
			oldOption.chart.defaultSeriesType = 'column';
		}
		
		chart.destroy();
		chart = new Highcharts.Chart(oldOption);
		chart.addSeries(oldSeries);
		
		// if single series present
		if(oldSeries.length == 1 && (chartType === 'bar' || chartType === 'column')) {
			stacking = (stacking == '')? 'normal' : stacking;
		}
		$.each(chart.series, function(index){
			chart.series[index].update({
				type: chartType,
				stacking: stacking
			});
		});
		$("div#dialog-modal").dialog("close");
	});
	// ============== END CODE for HIGHCHART OPTIONS ======================== //
});
/*
$.fn.dataTableExt.oApi.fnFilterClear  = function ( oSettings ) {   
    oSettings.oPreviousSearch.sSearch = "";
    if ( typeof oSettings.aanFeatures.f != 'undefined' ){
        var n = oSettings.aanFeatures.f;
        for ( var i=0, iLen=n.length ; i<iLen ; i++ ) {
            $('input', n[i]).val( '' );
        }
    }
    for ( var i=0, iLen=oSettings.aoPreSearchCols.length ; i<iLen ; i++ ) {
        oSettings.aoPreSearchCols[i].sSearch = "";
    }
    oSettings.oApi._fnReDraw( oSettings );
};
*/
/*datatable is created*/
function makeTable(){
	
	
	var count=0; header = $(".columnHeader"), data = $(".column"),html="<table id = 'jsTable' class='table'><thead><tr>"
		for(var count1=0; count1<header.length; count1++){
			html+="<th class='columnheader columnheader-"+count1+"'><div class='header-menu' row='"+count1+"'><span class='icon-hourglass with-tooltip' title='Advanced Filter'></span></div><div class='DataTables_sort_wrapper'><span class = 'spantext'>"+header.eq(count1).find(".otherspan")[2].innerHTML+"</span></div></th></th>";
		}
		html+="</tr></thead><tbody>";
		
		for(var count2=0;count2<(data.length/header.length);count2++){
			html+="<tr>";
			for(var count3=0;count3<header.length;count3++){
				if(count<data.length){
					html+= "<td>"+data.eq(count).children().text()+"</td>";
					count++;
				}
			}
			html+= "</tr>";
		}
		html+="</tbody><tfoot><tr>";
		
		var indx = ((data.length/header.length)-1)*header.length;
		for(var count1=0; count1<header.length; count1++){
			html+= "<td>&nbsp;</td>";
		}	

		html+="</tr></tfoot></table>";
		
		var allDiv = $(".jrPage").children('div');
		$(allDiv).each(function( index ) {
			if(index == 0 || index == 1) {
				$(this).css("left","1px");
				$("body").append(this);
			}
		});
		
		
		$("body").append(html);
		$(".jrtableframe").hide();
		oTable = $("#jsTable").dataTable({
			"oLanguage": {
				"sSearch": "Search all columns:"
			},
			"iDisplayLength": 30,
 			"aLengthMenu": [[20, 30, 40], [20, 30, 40]],
 			"sPaginationType": "full_numbers"
			/*,
			"bStateSave": true*/
		}).columnFilter({aoColumns:[
			{ sSelector: "#filter0", type:"text" },
			{ sSelector: "#filter1", type:"number-range" },
			{ sSelector: "#filter2", type:"text" },
			{ sSelector: "#filter3", type:"number-range" },
			{ sSelector: "#filter4", type:"text" },
			{ sSelector: "#filter5", type:"text" },
			{ sSelector: "#filter6", type:"number-range" },
			{ sSelector: "#filter7", type:"number-range" }
		]});
		
		$("#jsTable").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging",
			minWidth: 50
		});
		//$(".dataTables_length, .dataTables_filter, .dataTables_info, .dataTables_paginate").css("display","none");
		$("#jsTable tfoot").css("display","none");
		$("#jsTable tfoot, #jsTable thead").disableSelection();
		
		//$(".jrPage").parents("table").css("display", "none");
		$(".jrPage").css("display", "none");
		
		setTimeout(function() {
			adjustSize();
		}, 500);
}

/* Manage filterted header color */
function manageFilter(index) {
	var isDisabled = true;
	var input = $("#filter"+index).find(":text");
	for(var i=0, len=input.length; i<len; i++) {
		if(input.eq(i).val() != "") {
			isDisabled = false;
		}
	}
	if(isDisabled) {
		$(".columnheader-"+index).css('background', tableHeaderBg);
	} else {
		$(".columnheader-"+index).css('background', '#FFBB2B');
	}
}

/* Adjust ifreme height after loading datatable */
function adjustSize(){
	$("select[name^=jsTable_length]").live("change","select",function() {
		var parentBody = window.parent.document.body;
		var height = '1000px';
		if(this.value == 20) height = '1000px';
		if(this.value == 30) height = '1350px';
		if(this.value == 40) height = '1700px';
		$(".report-frame:visible", parentBody).css({height: height, minHeight: height});
	});
	$("select[name^=jsTable_length]").val("20").change();
}
	
