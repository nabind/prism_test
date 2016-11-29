

function topChange() {
    var paramList = arguments;
	for(i = 0; i < paramList.length; i++) {
            document.getElementById("toproot" + paramList[i]).className = 'on';
		}
}