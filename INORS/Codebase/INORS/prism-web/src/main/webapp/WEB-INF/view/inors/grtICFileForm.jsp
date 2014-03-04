<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<style>
	li.jstree-open > ul {
		padding-left: 29% !important;
	}
</style>

<div class="content-panel" style="padding-left:0px; padding-right: 10px; border: none">
	<form:form method="POST" id="grtDownload" modelAttribute="grtDownload">
	<p class="success-message message small-margin-bottom green-gradient" style="display:none">PDF File Generation has been requested.<br/>Click on 'Group Download Files' for Status of request(s).</p>
	<p class="error-message message small-margin-bottom red-gradient" style="display:none">Error submitting download request. Please try later.</p>
	<input type="hidden" value="/public/INORS/Report/Report1_files" name="reportUrl" >
	<c:if test="${not empty reportMessages}">
		<c:forEach var="reportMessage" items="${reportMessages}">
			<c:if test="${reportMessage.displayFlag=='Y'}">
				<fieldset class="fieldset">
					<legend class="legend">${ reportMessage.messageName }</legend>
					<p class="inline-label">${ reportMessage.message }</p>
				</fieldset>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${showGrtDiv=='Y'}">
	<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
		<c:choose>
			<c:when test="${fn:endsWith(testAdministrationText, '2010')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/IMAST S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2011')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/IMAST S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2012')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/IMAST S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IREAD')}">
					<c:if test="${fn:contains(testAdministrationText, 'Spring')}">
						<a class="button" id="grt2010" href="/inors/staticfiles/IREAD-3 S2011-12 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					</c:if>
					<c:if test="${fn:contains(testAdministrationText, 'Summer')}">
						<a class="button" id="grt2010" href="/inors/staticfiles/IREAD-3 R2011-12 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					</c:if>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2013')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/ISTEP S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<a class="button" id="grt2010" href="/inors/staticfiles/IMAST S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IREAD')}">
					<c:if test="${fn:contains(testAdministrationText, 'Spring')}">
						<a class="button" id="grt2010" href="/inors/staticfiles/IREAD-3 S2012-13 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					</c:if>
					<c:if test="${fn:contains(testAdministrationText, 'Summer')}">
						<a class="button" id="grt2010" href="/inors/staticfiles/IREAD-3 R2012-13 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					</c:if>
				</c:if>
			</c:when>
			<c:otherwise>
				<!-- Code When No Layout Available -->
			</c:otherwise>
		</c:choose>
		<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
	</div>
	</c:if>
	<c:if test="${showIcDiv=='Y'}">
	<div id="icDiv" class="columns accordion with-padding" style="margin-bottom: 0">
		<c:choose>
			<c:when test="${(fn:startsWith(testAdministrationText, 'ISTEP+')) && (fn:endsWith(testAdministrationText, '2013'))}">
				<a class="button" id="ic2013" href="/inors/staticfiles/S2012-13 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 Invitation Code File Record Layout</a>
			</c:when>
			<c:otherwise>
				<!-- Code When No Layout Available -->
			</c:otherwise>
		</c:choose>
		<a class="button float-right" id="downloadICFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File</a>
	</div>
	</c:if>
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
</div>
