	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<c:set var="singleQuote" value="&#39;" />
	<c:set var="escapeSingleQuote" value="" />
	<ul class="big-menu blue-gradient collapsible">
	
		<c:forEach var="assessments" items="${assessmentList}" varStatus="loop">
			<c:set var="count" value="0" />
			<c:forEach var="report" items="${assessments.reports}" varStatus="innerloop">
				<sec:authorize ifAnyGranted="${report.allRoles}">
					<c:set var="currLevel" value="<%=request.getSession().getAttribute(com.ctb.prism.core.constant.IApplicationConstants.CURRORGLVL)%>" />
					<c:if test="${report.orgLevel == currLevel}">
						<c:set var="count" value="${count + 1}" />
					</c:if>
				</sec:authorize>
			</c:forEach>
			<c:if test="${count > 0}">
			<li class="with-right-arrow" id="menu_${assessments.assessmentName}">
				<span>${assessments.assessmentName}</span>
			
			<ul class="big-menu white-gradient">
				<c:forEach var="report" items="${assessments.reports}" varStatus="innerloop">
					<sec:authorize ifAnyGranted="${report.allRoles}">
					<c:set var="currLevel" value="<%=request.getSession().getAttribute(com.ctb.prism.core.constant.IApplicationConstants.CURRORGLVL)%>" />
					<c:if test="${report.orgLevel == currLevel}">
						<li style="padding: 8px 25px !important; font-size: 12px !important; font-weight: normal !important; text-shadow: none !important" id="select-tooltip-${loop.count}">
							<c:choose>
								<c:when test="${report.reportType == 'API_LINK'}">
									<a href="${report.reportOriginalUrl}" title="${report.reportName}">${report.reportName}</a>
								</c:when>
								<c:otherwise>
									<span onclick='addReportTab("${report.reportUrl}", "${report.reportId}",  "<c:out value="${report.reportName}" escapeXml="true"/>", "${assessments.assessmentId}", "", "${report.reportType}", "${report.customUrl}")'>${report.reportName}</span>
								</c:otherwise>
							</c:choose>
						</li>
					</c:if>
					</sec:authorize>
				</c:forEach>
			</ul>
		</li>
		</c:if>
		</c:forEach>
	</ul>
