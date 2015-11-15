<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:message var="grtFileButtom" key="downloads.grt.button" />
<fmt:message var="icFileButtom" key="downloads.ic.button" />
		<c:if test="${showGrtDiv=='Y'}">
		<c:if test="${not empty actionMap[grtFileButtom]}">
			<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<fmt:message var="MANAGE_USERS_SEARCH" key="manage.users.search"  />
				<a class="button" id="grt${selectedYear}" href="downloadAssest.do?assetPath=${grtFileLayoutHref}"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>${grtFileLayoutDisplayName}</a>
				<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> <spring:message code="label.generateGRTFile" /></a>
			</div>
		</c:if>
		</c:if>
		<c:if test="${showIcDiv=='Y'}">
		<c:if test="${not empty actionMap[icFileButtom]}">
			<div id="icDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<a class="button" id="ic${selectedYear}" href="downloadAssest.do?assetPath=${icFileLayoutHref}"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>${icFileLayoutDisplayName}</a>
				<a class="button float-right" id="downloadICFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> <spring:message code="label.generateICFile" /></a>
			</div>
		</c:if>
		</c:if>