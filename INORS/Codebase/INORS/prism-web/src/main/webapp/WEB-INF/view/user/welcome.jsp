		<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
		<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
		<div class="right-column">
				<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
					<ul class="tabs reporttabs">
						<li class="active"><a assessment="${homeReport.assessmentName}" href="#new-tab0" id="new-tab0_new-tab0">${homeReport.reportName}</a></li>
					</ul>
					<div class="tabs-content">
						<div id="new-tab0" class="with-padding relative">
							<%@ include file="../report/report.jsp"%>
						</div>
					</div>
				</div>
		</div>
