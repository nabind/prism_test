<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="java.util.ArrayList" %>

	<link rel="stylesheet" href="css/highlight.css" type="text/css">
		<link rel="stylesheet" href="css/demo.css" type="text/css">
		<!-- jquery core -->
		
		<!-- jquery plugins -->
		<script type="text/javascript" src="css/jquery.cookie.min.js"></script>
		<script type="text/javascript" src="css/jquery.accordion.js"></script>
		<script type="text/javascript" src="css/highlight.pack.js"></script>
		<script type="text/javascript" src="css/jquery.ui.dialog.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
	
			//syntax highlighter
			hljs.tabReplace = '    ';
			hljs.initHighlightingOnLoad();
	
			//accordion
			$('h3.accordion').accordion({
				defaultOpen: 'section1',
				cookieName: 'accordion_nav',
				speed: 'slow',
				animateOpen: function (elem, opts) { //replace the standard slideUp with custom function
					elem.next().slideFadeToggle(opts.speed);
				},
				animateClose: function (elem, opts) { //replace the standard slideDown with custom function
					elem.next().slideFadeToggle(opts.speed);
				}
			});
	
			$('h3.accordion2').accordion({
				defaultOpen: 'sample-1',
				cookieName: 'accordion2_nav',
				speed: 'slow',
				cssClose: 'accordion2-close', //class you want to assign to a closed accordion header
				cssOpen: 'accordion2-open'
				
			});
	
			//custom animation for open/close
			$.fn.slideFadeToggle = function(speed, easing, callback) {
				return this.animate({opacity: 'toggle', height: 'toggle'}, speed, easing, callback);
			};
	
		});
	</script>
	
<div id="heromaskarticle">
	<div id="articlecontent">
		<h1>Current Process 111</h1>
		
		<div id="accordion">
				<!-- panel -->
				<% 
				java.util.List<OrgProcess> allProcess = (ArrayList) request.getSession().getAttribute("allProcess");
				int count=0;
				for(OrgProcess process : allProcess) {
					count++;
				%>
				
				<h3 class="accordion accordion-close" id="body-section<%=count %>">
					Structure Element #: <%=process.getStructElement() %>
					<span>Process Status: <%=process.getProcessStatus() %></span>
				</h3>
				<div class="container" style="display: none; ">
					<div class="content">
						<p>
							Process Id: <%=process.getProcessid() %>
						</p>
						<p>	
							Created Date: <%=process.getCreatedDate() %>
						</p>
						<p>
							Updated Date: <%=process.getUpdatedDate() %>
						</p>
						<p>
							Data Status: <%=process.getDataStatus() %>
						</p>
						<p>
							PDF Status: <%=process.getPdfStatus() %>
						</p>
						<p>
							Target Status: <%=process.getTargetStatus() %>
						</p>
						<p>
							Target Email Status: <%=process.getTargerEmailStatus() %>
						</p>
						<p>
							Process Status: <%=process.getProcessStatus() %>
						</p>
						
						
					</div>
					<div >
						<script>
							$(document).ready(function(){
								$("#dialog<%=count%>").dialog({bgiframe: true, autoOpen: false, modal: false, minHeight: 50, minWidth: 400, closeOnEscape: true, resizable: true});
							});
						</script>
						<% String log = process.getProcessLog(); log = log.replaceAll("\n", "<br/>"); %>
						<div id='dialog<%=count%>' title='Log for Process # <%=process.getProcessid() %>' style='display:none; font-size:10px'><%=process.getProcessLog() %></div>
						<p>
							Process log: <a href='#note' class='noteLink' style='color:#00329B;' onclick='jQuery("#dialog<%=count%>").dialog("open"); return false'>View Log</a>
						</p>
					</div>
				</div>
				<%} %>
				<!-- end panel -->
				
				
			</div>

	</div>
</div>
</div>