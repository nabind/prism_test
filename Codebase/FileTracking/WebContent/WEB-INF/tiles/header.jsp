<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.web.UserController" %>

<script>
function refreshList(adminid, url) {
	location.href = url+"?adminid="+adminid;
}
</script>

<div id="center">


<div id="contents">
<div id="global" class="clear">
<div style="float: right;">
<% if(UserController.checkLogin(request)) { 
		String userName = (String) request.getSession().getAttribute("userName");
%>
	<p><span style="font-weight: bold; margin-right: 15px;">Welcome: &nbsp;<%=userName %> User</span>
	<span class="h15-pipe">|</span><a href="logout.htm"> Logout</a></p>
	<% } %>
</div>
</div>
<div id="search" class="clear">
<div id="sitelogo"><a
	href="#nogo"><img
	src="css/logoCTB.png"
	alt="css/logoCTB.png"
	border="0" height="50%"></a></div>
<div id="searchbox">
	<div class="msviLSBsform" id="msviLSBsform" style="font-size: 17px;padding-top: 30px;">PRISM Online Process Tracking System</div>
</div>
</div>
<div id="siteoutline" class="clear">
<div id="topnav" class="clear">
	<div id="topnavcontrol" class="clear">
		<% if(UserController.checkLogin(request)) { %>
			<%if("Admin".equals(request.getSession().getAttribute("userName"))) { %>
			<span><a id="toproot2" class="off" href="view.htm">&nbsp;Search ACSI Process&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="process.htm">&nbsp; ACSI Processes &nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot2" class="off" target="_blank" href="css/external/help.pdf">&nbsp;Help &nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<% } else if("TascAdmin".equals(request.getSession().getAttribute("userName"))) { %>
			<span><a id="toproot2" class="off" href="tascSearch.htm">&nbsp;Search TASC Process&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="tascSearchErNew.htm">&nbsp; Search TASC Processes ER&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<!-- <span><a id="toproot1" class="off" href="searchTascGraph.htm">&nbsp; Request Count&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
						 -->
			<span><a id="toproot1" class="off" href="combined.htm">&nbsp; Check single student&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="completenessCheckSearch.htm">&nbsp; PBT Completeness Check&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="scoreReview.htm">&nbsp; Score Change Review&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="searchError.htm">&nbsp; Search Error&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<% } else if("MapAdmin".equals(request.getSession().getAttribute("userName"))) { %>
			<span><a id="toproot2" class="off" href="mapSearch.htm">&nbsp;Search MAP Process&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<span><a id="toproot1" class="off" href="mapProcess.htm">&nbsp; All MAP Processes for today&nbsp;</a></span>
			<img src="css/nav-divider.gif" alt="" class="divider" border="0" height="29" width="3">
			
			<% } %>
		<% } %>
	</div>
</div>
<div id="hero" class="clear">
<div id="herocontents"
	style=""></div>
</div>
<div id="main" class="clear">