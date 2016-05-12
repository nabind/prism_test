
function searchCtrl_KeyPress(e)
{var e=e||window.event;var keyCode=e.which||e.keyCode;if(keyCode==13)
{var x=document.getElementById(searchCtrlButtonId);if(x)
{x.click();return false;}}
return true;}
