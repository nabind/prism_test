	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<ul class="big-menu blue-gradient collapsible">
	
		<c:forEach var="assessments" items="${assessmentList}" varStatus="loop">
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
										<span onclick="addReportTab('${report.reportUrl}', '${report.reportId}', '${report.reportName}', '${assessments.assessmentId}', '', '${report.reportType}', '${report.customUrl}')">${report.reportName}</span>
									</c:otherwise>
								</c:choose>
							</li>
						</c:if>
					</sec:authorize>
				</c:forEach>
			</ul>
		</li>
		</c:forEach>
	</ul>
