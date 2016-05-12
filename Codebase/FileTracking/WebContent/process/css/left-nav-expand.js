function flipNav(obj) {
	var shownav = document.getElementById("navtop" + arguments[0]);
	if ( shownav.className != "leftnav-hide" ) {
		shownav.className = 'leftnav-hide';
	}
	else {
		shownav.className = 'leftnav-show';
	}
	var titlenav = document.getElementById("navroot" + arguments[0]);
	if ( titlenav.className != "root-off" ) {
		titlenav.className = 'root-off';
	}
	else {
		titlenav.className = 'root-on';
	}
}
