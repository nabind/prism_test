$(document).ready(function(){
	var rowgroup = "";
	var disag = "0";
	var lastvalue = "";
	var org_nodeid = "736002,736003";
	var transpose = "0";
	//alert('Joy - Before');
	setTimeout(function(){
		//alert('Joy - After');
		tableRefresh();
	}, 2000);
	// $("#reportContainer").load(function(){
		// alert('Joy - After');
		// //tableRefresh();
	// });
	// $(document).on('load', '#reportContainer .jrxtrowheader span', function() {
		 // alert('Joy - After');
	// });
	
});

function tableRefresh(){
	$("#reportContainer .jrxtrowheader span").each(function(i, e){
		var actualText = $(e).text(); 
		//console.log(actualText);
		var displayText = actualText.split("|");
		$(e).text(displayText[0]);
		$(e).attr("last_val",displayText[1]);
	});
	reportCallBack();
}

function reportCallBack(){
	$(".jrxtrowheader span").mousedown(function() {
		disag = 1;
		lastvalue = $(this).attr("last_val");
		//org_nodeid = "";
	});
	appendMenu();
}

function appendMenu(){
	var updateObj = [{
		name: 'create',
		title: 'create button',
		fun: function () {
			alert('i am add button');
		}
	},{
		name: 'update',
		title: 'update button',
		subMenu: [{
			name: 'merge',
			title: 'It will merge row',
			fun: function () {
				alert('It will merge row')
			}
			},{
			name: 'replace',
			title: 'It will replace row',
			subMenu: [{
				name: 'replace top 100',
				fun:function(){
					alert('It will replace top 100 rows');
				}
				},{
				name: 'replace all',
				fun:function(){
					alert('It will replace all rows');
				}
			}]
		}]
	}];
	//$(".jrxtrowheader").wrapInner("<div class='containerDiv'><span class='containerSpan'></span></div>");
	$('.jrxtrowheader').contextMenu('open',updateObj,{triggerOn:'click',mouseClick:'right'});
	
}