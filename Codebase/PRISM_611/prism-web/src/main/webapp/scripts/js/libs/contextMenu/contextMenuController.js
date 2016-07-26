var org_nodeid = "736002,736003";
/*var actualText = "{"
					+"'displayName':'SCHOOL000000100',"
					+"'value':'p_Org_Id~736002',"
					+"'contextMenu':["  
						+"{"  
							+"'name':'Drill To Roster',"
							+"'title':'Drill To Roster',"
							+"'subMenu':["  
								+"{"  
									+"'name':'D/2',"
									+"'title':'D/2'"
								+"}"
							+"]"
						+"},"
						+"{"
						+"'name':'Disaggregate By',"
						+"'title':'Disaggregate By',"
						+"'subMenu':[  "
							+"{  "
								+"'name':'Ethnicity',"
								+"'title':'Ethnicity'"
							+"},"
							+"{  "
								+"'name':'Gender',"
								+"'title':'Gender'"
							+"},"
							+"{  "
								+"'name':'Grade',"
								+"'title':'Grade'"
							+"},"
							+"{  "
								+"'name':'Level',"
								+"'title':'Level'"
							+"}"
						 +"]"
					  +"}"
				   +"]"
				+"}";*/

$(document).on('mousedown', '.jrxtrowheader', function(){
	if(typeof $(this).attr("id") !== 'undefined'){
			openMenu($(this));
		}
} );

function openMenu(elmObj){
	var actualText = elmObj.attr("id"); 
	var rawJson = actualText.replace(/'/g, '"');
	console.log(rawJson);
	var jsonObj = JSON.parse(rawJson)
	var displayText = jsonObj.displayName;
	var lastVal = jsonObj.value;
	$('#contextMenuDiv').attr('p_lastvalue',lastVal);
	var contextMenuDom = buildList(jsonObj.contextMenu);
	$('#contextMenuDiv').html(contextMenuDom);
	$('.jrxtrowheader').contextMenu('menu',$('#contextMenuDiv ul:first'),{triggerOn:'click',mouseClick:'right'});
	
}

function buildList(data){
    var html = '<ul>';
    for(var item in data){
        html += '<li title="'+data[item].title+'" onclick="conMenuFunc($(this))">';
        if(typeof(data[item].subMenu) === 'object'){
            html += data[item].name;
            html += buildList(data[item].subMenu);
        } else {
            html += data[item].name;
        }
        html += '</li>';
    }
    html += '</ul>';
	return html;
}

function conMenuFunc(liObj){
	var p_rowgroup = liObj.text();
	var p_disag = 1;
	var p_lastvalue = $('#contextMenuDiv').attr('p_lastvalue');
	var p_transpose = "";
	var reportUrl = window.parent.$('#reportUrl').attr('value');
	var reportName = window.parent.$('#reportName').attr('value');
	var reportId = window.parent.$('#reportId').attr('value');
	var applicationName = window.parent.$("#applicationName").attr("value");
	var fullReportURL = applicationName+"/openReportHtmlApi.do?reportUrl="+reportUrl+"&reportId="+reportId+"&reportName="+reportName+"&filter=true&p_rowgroup="+p_rowgroup+"&p_disag="+p_disag+"&p_lastvalue="+p_lastvalue+"&p_transpose="+p_transpose;
	console.log("fullReportURL: "+fullReportURL);
	location.href = fullReportURL;
}