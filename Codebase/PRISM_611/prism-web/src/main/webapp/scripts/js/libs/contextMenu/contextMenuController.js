/*
var actualText = "{"
					+"'displayName':'English/Language Arts',"
					+"'value':'p_Org_Id~605205,Gender~501',"
					+"'hiddenColumns':'',"
					+"'disag':'1',"
					+"'transpose':'0',"
					+"'rowgroup':'Gender',"
					+"'contextMenu':["  
						+"{"  
							+"'name':'Hide this Content Area',"
							+"'title':'Hide this Content Area',"
							+"'value':'2001'"  
						+"}"
					+"]"
				+"}";
*/				
			
/*				
var actualText = "{"
					+"'displayName':'SCHOOL000000100',"
					+"'value':'p_Org_Id~605205',"
					+"'hiddenColumns':'',"
					+"'disag':'1',"
					+"'transpose':'0',"
					+"'rowgroup':'Gender',"
					+"'contextMenu':["  
						+"{"  
							+"'name':'Drill To Roster',"
							+"'title':'Drill To Roster',"
							+"'value':'-99',"  
							+"'subMenu':["  
								+"{"  
									+"'name':'D/2',"
									+"'title':'D/2',"
									+"'value':'-99'"
								+"}"
							+"]"
						+"},"
						+"{"
						+"'name':'Disaggregate By',"
						+"'title':'Disaggregate By',"
						+"'value':'-99',"
						+"'subMenu':[  "
							+"{  "
								+"'name':'Ethnicity',"
								+"'title':'Ethnicity',"
								+"'value':'-99'"
							+"},"
							+"{  "
								+"'name':'Gender',"
								+"'title':'Gender',"
								+"'value':'-99'"
							+"},"
							+"{  "
								+"'name':'Grade',"
								+"'title':'Grade',"
								+"'value':'-99'"
							+"},"
							+"{  "
								+"'name':'Level',"
								+"'title':'Level',"
								+"'value':'-99'"
							+"}"
						 +"]"
					  +"}"
				   +"]"
				+"}";				
*/							

$(document).on('mousedown', '.jrxtrowheader, td[class^="header_"]', function(){
	if(typeof($(this).attr("id")) !== 'undefined'){
			openMenu($(this));
		}
} );


function openMenu(elmObj){
	var actualText = elmObj.attr("id"); 
	console.log(actualText);
	var rawJson = actualText.replace(/'/g, '"');
	console.log(rawJson);
	var jsonObj = JSON.parse(rawJson);
	$('#contextMenuDiv').attr('lastValue',jsonObj.value);
	$('#contextMenuDiv').attr('hiddenColumns',jsonObj.hiddenColumns);
	$('#contextMenuDiv').attr('disag',jsonObj.disag);
	$('#contextMenuDiv').attr('transpose',jsonObj.transpose);
	$('#contextMenuDiv').attr('rowgroup',jsonObj.rowgroup);
	var contextMenuDom = buildList(jsonObj.contextMenu);
	$('#contextMenuDiv').html(contextMenuDom);
	$(elmObj).contextMenu('menu',$('#contextMenuDiv ul:first'),{triggerOn:'click',mouseClick:'right'});
	
}

function buildList(data){
    var html = '<ul>';
    for(var item in data){
        html += '<li title="'+data[item].title+'" value="'+data[item].value+'" onclick="conMenuFunc($(this))">';
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
	var p_lastvalue = $('#contextMenuDiv').attr('lastValue');
	var p_hidden_columns = $('#contextMenuDiv').attr('hiddenColumns');
	var p_disag = $('#contextMenuDiv').attr('disag');
	var p_transpose = $('#contextMenuDiv').attr('transpose');
	var p_rowgroup = $('#contextMenuDiv').attr('rowgroup');
	if(liObj.attr('value') == -99){
		p_rowgroup = liObj.text();
	}else{
		p_hidden_columns += liObj.attr('value');
	}
	var reportUrl = window.parent.$('#reportUrl').attr('value');
	var reportName = window.parent.$('#reportName').attr('value');
	var reportId = window.parent.$('#reportId').attr('value');
	var scheme = window.parent.$("#scheme").attr("value");
	var serverName = window.parent.$("#serverName").attr("value");
	var serverPort = window.parent.$("#serverPort").attr("value");
	var contextPath = window.parent.$("#contextPath").attr("value");
	var fullReportURL = scheme+"://"+serverName+":"+serverPort+contextPath;
	fullReportURL += "/openReportHtmlApi.do?reportUrl="+reportUrl+"&reportId="+reportId+"&reportName="+reportName+"&filter=true&p_rowgroup="+p_rowgroup+"&p_disag="+p_disag+"&p_lastvalue="+p_lastvalue+"&p_org_nodeid=&p_transpose="+p_transpose+"&p_hidden_columns="+p_hidden_columns;
	console.log("fullReportURL: "+fullReportURL);
	location.href = fullReportURL;
}