		<c:if test="${showGrtDiv=='Y'}">
			<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<a class="button" id="grt${selectedYear}" href="downloadAssest.do?assetPath=Static_Files/${grtFileLayoutHref}"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>${grtFileLayoutDisplayName}</a>
				<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> <spring:message code="label.generateICFile" /></a>
			</div>
		</c:if>
		<c:if test="${showIcDiv=='Y'}">
			<div id="icDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<a class="button" id="ic${selectedYear}" href="downloadAssest.do?assetPath=Static_Files/${icFileLayoutHref}"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>${icFileLayoutDisplayName}</a>
				<a class="button float-right" id="downloadICFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> <spring:message code="label.generateICFile" /></a>
			</div>
		</c:if>