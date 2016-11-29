function toggleExpando(obj) {
	var expando = document.getElementById("expando" + arguments[0]);
	if ( expando.className != "expando-hide" ) {
		expando.className = 'expando-hide';
	}
	else {
		expando.className = 'expando-show';
	}
	var expando = document.getElementById("expandohead" + arguments[0]);
	if ( expando.className != "expando-off" ) {
		expando.className = 'expando-off';
	}
	else {
		expando.className = 'expando-on';
	}
}



function showPageTools(obj) {
	var pagetools = document.getElementById("share-this-page");
	if ( pagetools.className != "dynSBM-hide" ) {
		pagetools.className = 'dynSBM-hide';
	}
	else {
		pagetools.className = 'dynSBM-show';
	}
}