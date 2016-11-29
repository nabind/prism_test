// Copyright (c) 2005-2010 Enquisite Software Inc. All rights reserved.
// $Id$
// <![CDATA[


// customer accessible functions
function enq_get_pc(){
	return enq_rc('enqp');
}

function enq_get_sc(){
	return enq_rc('enqs');
}

function enq_set_pc(cookie){
	enq_wc('enqp',cookie,3650);
}

function enq_set_sc(cookie){
	enq_wc('enqs',cookie);
}

function enq_write_event(location,referrer,siteid){
	enq_pl['location']=location;
	enq_pl['referrer']=referrer;
	if(typeof(siteid)!='undefined' && siteid != '') {
		enq_pl['id']=siteid;
	}
	var enq_scr = document.createElement("script");
	enq_scr.src = enq_uri(enq_pl);
	enq_scr.type="text/javascript";
	document.getElementsByTagName("head")[0].appendChild(enq_scr);
}
// end of customer accessible functions

function enq_wc(name,value,days){
	var expires='';
	if(days){
		var date=new Date();
		date.setTime(date.getTime()+(days*86400000));
		var expires="; expires="+date.toUTCString();
	}
	var domain='';
	if(enq_pl['domain']){
		domain = "; domain="+enq_pl['domain'];
	}
	document.cookie=name+"="+value+expires+domain+"; path=/";
}

function enq_rc(name){
	var nameEQ=name+"=";
	var ca=document.cookie.split(';');
	for(var i=0;i<ca.length;i++){
		var c=ca[i];
		while(c.charAt(0)==' ')c=c.substring(1,c.length);
		if(c.indexOf(nameEQ)==0) {
			return c.substring(nameEQ.length,c.length);
		}
	}
	return null;
}

function enq_ec(name){enq_wc(name,"",-1);}

function enq_ch(){
	var chars = "0123456789abcdefghiklmnopqrstuvwxyz";
	var cookiestring = '';
	for (var i=0; i<32; i++) {
		var rnum = Math.floor(Math.random() * chars.length);
		cookiestring += chars.substring(rnum,rnum+1);
	}
	return cookiestring;
}

function enq_uri(enq_pl){
	var enq_url;
	var enq_loc;
	if(enq_pl['location']){
		enq_loc=enq_pl['location'];
		enq_url=(enq_loc.substring(0,5)=='https')?'https://logssl.enquisite.com':'http://log.enquisite.com';
	}
	else {
		enq_loc=(enq_pl['frames'])?top.location:window.location;
		enq_url=(enq_loc.href.substring(0,5)=='https')?'https://logssl.enquisite.com':'http://log.enquisite.com';
	}
	var enq_ref=(enq_pl['referrer'])
		?enq_pl['referrer']:
		(enq_pl['frames'])?top.document.referrer:'';


	if(enq_ref.match(/^(|unknown|undefined|\[unknown origin\])$/))
		enq_ref=parent.document.referrer;
	if(enq_ref.match(/^(|unknown|undefined|\[unknown origin\])$/))
		if(document['referrer']!=null)enq_ref=document['referrer'];
	if(enq_ref.match(/^(|unknown|undefined|\[unknown origin\])$/))
		enq_ref='';

	var enq_pc=enq_rc('enqp');
	if(enq_pc==null){enq_pc=enq_ch();enq_wc('enqp',enq_pc,3650);}
	var enq_sc=enq_rc('enqs');
	if(enq_sc==null){enq_sc=enq_ch();enq_wc('enqs',enq_sc);}

	var enq_str='';
	enq_str+=enq_url+'/d.js?id='+escape(enq_pl['id']);
	enq_str+="&referrer="+escape(enq_ref)+"&location="+escape(enq_loc);
	enq_str+="&ua="+escape(navigator.userAgent);
	enq_str+="&pc="+escape(enq_pc)+"&sc="+escape(enq_sc);
	if(enq_pl['value']){
		enq_str+="&value="+enq_pl['value'];
	}
	else if(enq_pl['valuevar'] && typeof(window[enq_pl['valuevar']]) != "undefined"){
		enq_str+="&value="+window[enq_pl['valuevar']];
	}
	if(enq_pl['transactionid']){
		enq_str+="&transactionid="+enq_pl['transactionid'];
	}
	else if(enq_pl['transactionidvar'] && typeof(window[enq_pl['transactionidvar']]) != "undefined"){
		enq_str+="&transactionid="+window[enq_pl['transactionidvar']];
	}
	enq_str+="&r="+Math.random();
	return enq_str;
}

function enq_fp(enq_pl){
	var enq_sl=document.getElementsByTagName("script");
	var enq_q='';
	for(var i=0;i<enq_sl.length;i++){
		var enq_s=enq_sl[i];
		if(enq_s.src&&enq_s.src.match("log(ssl)?.enquisite.com/log\.js\?")){
			var enq_sm=enq_s.src.match(/\?([^#]*)(#.*)?/);
			if(enq_sm){	
				var enq_qpl=enq_sm[1].split('&');
				for(var j=0;j<enq_qpl.length;j++){
					var enq_kvl=enq_qpl[j].split('=');
					enq_pl[enq_kvl[0]]=enq_kvl[1];
				}
				if(enq_pl['id'] && typeof(window['enq_DupeJS']) != 'undefined' && window['enq_DupeJS'][enq_pl['id']] != 1){
					break;
				}
			}
		}
	}

	if(typeof enq_pl['f'] != 'undefined'){
		enq_pl['frames'] = 1;
	}
}

var enq_oe=window.onerror;
window.onerror=null;
var enq_pl=new Array();
if(typeof(window['enq_DupeJS'])=='undefined'){
	window['enq_DupeJS'] = new Array();
}
enq_fp(enq_pl);
if(enq_pl['id'] && window['enq_DupeJS'][enq_pl['id']] != 1){
	var enq_str='';
	enq_str+="<script "+'src="';
	enq_str+=enq_uri(enq_pl);
	enq_str+='" type="text/javascript"></script>';
	var enq_w='w'+'r'+'i'+'t'+'e';
	document[enq_w](enq_str);
	window['enq_DupeJS'][enq_pl['id']]=1;
}
window.onerror=enq_oe;

// ]]>
