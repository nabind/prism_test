var rowgroup = "";
var disag = "0";
var lastvalue = "";
var org_nodeid = "736002,736003";
var transpose = "0";

$(document).ready(function(){
	console.log('Page Ready')
	$(".jrxtrowheader").mousedown(function() {
		openMenu($(this));
	});
});

function openMenu(elmObj){
	var actualText = elmObj.attr("id"); 
	var rawJson = actualText.replace(/'/g, '"');
	console.log(rawJson);
	var jsonObj = JSON.parse(rawJson)
	var displayText = jsonObj.displayName;
	var lastVal = jsonObj.value;
	//console.log(lastVal);
	var contextMenuDom = buildList(jsonObj.contextMenu);
	$('#contextMenuDiv').html(contextMenuDom);
	$('.jrxtrowheader').contextMenu('menu',$('#contextMenuDiv ul:first'),{triggerOn:'click',mouseClick:'right'});
	
}

function buildList(data){
    var html = '<ul>';
    for(var item in data){
        html += '<li>';
        if(typeof(data[item].subMenu) === 'object'){
            html += data[item].name;
            html += buildList(data[item].subMenu);
        } else {
            html += data[item].name;
        }
        html += '</li>';
    }
    html += '</ul>';
	//console.log('before return: '+html);
    return html;
}