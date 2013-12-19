<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
/* You can alter this CSS in order to give Smooth Div Scroll your own look'n'feel */

			/* Invisible left hotspot */
			div.scrollingHotSpotLeft
			{
				/* The hotspots have a minimum width of 100 pixels and if there is room the will grow
				and occupy 15% of the scrollable area (30% combined). Adjust it to your own taste. */
				/*min-width: 75px;*/
				width: 2%;
				height: 100%;
				/* There is a big background image and it's used to solve some problems I experienced
				in Internet Explorer 6. */
				background-image: url(themes/acsi/img/sdscroll/big_transparent.gif);
				background-repeat: repeat;
				background-position: center center;
				position: absolute;
				z-index: 200;
				left: 0;
				/*  The first url is for Firefox and other browsers, the second is for Internet Explorer */
				cursor: url(themes/acsi/img/sdscroll/cursors/cursor_arrow_left.png), url(themes/acsi/img/sdscroll/cursors/cursor_arrow_left.cur),w-resize;
			}

			/* Visible left hotspot */
			div.scrollingHotSpotLeftVisible
			{
				background-image: url(themes/acsi/img/sdscroll/arrow_left.gif);				
				background-color: #ccc;
				background-repeat: no-repeat;
				opacity: 0.35; /* Standard CSS3 opacity setting */
				-moz-opacity: 0.35; /* Opacity for really old versions of Mozilla Firefox (0.9 or older) */
				filter: alpha(opacity = 35); /* Opacity for Internet Explorer. */
				zoom: 1; /* Trigger "hasLayout" in Internet Explorer 6 or older versions */
			}

			/* Invisible right hotspot */
			div.scrollingHotSpotRight
			{
				/*min-width: 75px;*/
				width: 5%;
				height: 100%;
				background-image: url(themes/acsi/img/sdscroll/big_transparent.gif);
				background-repeat: repeat;
				background-position: center center;
				position: absolute;
				z-index: 200;
				right: 0;
				cursor: url(themes/acsi/img/sdscroll/cursors/cursor_arrow_right.png), url(themes/acsi/img/sdscroll/cursors/cursor_arrow_right.cur),e-resize;
			}

			/* Visible right hotspot */
			div.scrollingHotSpotRightVisible
			{
				background-image: url(themes/acsi/img/sdscroll/arrow_right.gif);
				background-color: #ccc;
				background-repeat: no-repeat;
				opacity: 0.35;
				filter: alpha(opacity = 35);
				-moz-opacity: 0.35;
				zoom: 1;
			}

			/* The scroll wrapper is always the same width and height as the containing element (div).
			   Overflow is hidden because you don't want to show all of the scrollable area.
			*/
			div.scrollWrapper
			{
				position: relative;
				overflow: hidden;
				width: 100%;
				height: 100%;
			}

			div.scrollableArea
			{
				position: relative;
				width: auto;
				height: 100%;
			}
</style>

<link rel="stylesheet" href="scripts/js/libs/theme/jquery-ui-redmond.css">
<div class="content-panel" style="padding-left:0px; border: none">
	<div class="report-panel-content linen linen-custom">
		
		<div class="panel-control panel-control-report align-right padding-right"></div>
		
		<div class="panel-load-target with-padding report-layout padding-none">
			<c:if test="${homeReport.refreshButtonClass == 'customRefresh'}">
				<c:set var="name" scope="page" value=""/>
			</c:if>	
			<c:if test="${homeReport.refreshButtonClass != 'customRefresh'}">
				<c:set var="name" scope="page" value="Report"/>
			</c:if>	
			<c:if test="${homeReport.studentBioId == null}">
			<c:if test="${homeReport.hideFilter != 'hide'}">
			<form:form method="GET" class="report-form report-form-${homeReport.tabCount}" tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}">
				<div class="mid-margin-bottom" id="filterHolder-${homeReport.tabCount}" style="position:relative">
					<div class="reportFilterCriteria panel-control rounded-border report-filter report-filter-${homeReport.tabCount}" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}" tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}" assessment="${homeReport.assessmentName}">
						<span id="filter-icon" class="icon-leaf icon-size2"></span> <b>${name} Filter Options</b>
						<c:if test="${homeReport.refreshButtonClass != 'customRefresh'}">
						<div class="download-button download-button-${homeReport.tabCount}">
							<a href="#nogo" class="button download-button-pdf margin-left glossy compact with-tooltip" 
								title="Download report as PDF" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}" assessment="${homeReport.assessmentName}">
								<span class="button-icon blue-gradient report-btn"><span class="icon-page-list"></span></span>
								PDF
							</a>
							<a href="#nogo" class="button download-button-xls margin-left glossy compact with-tooltip" 
								title="Download report as Excel" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}" assessment="${homeReport.assessmentName}">
								<span class="button-icon blue-gradient report-btn"><span class="icon-page-list"></span></span>
								Excel
							</a>							
						</div>
						</c:if>	
					</div>
					
					<div class="cyan-gradient icholder icholder-${homeReport.tabCount} rounded-border-bottom display-none" style="border-bottom: 1px solid #CCC;">
						<div class="refresh-report" style="position: absolute; top: 3px; right: 15px;">
							<a href="javascript:void(0)" class="button blue-gradient glossy ${homeReport.refreshButtonClass}" reportName="${homeReport.reportName}" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}" assessment="${homeReport.assessmentName}" apiUrl="${homeReport.reportApiUrl}">Refresh ${name}</a>
						</div>
						<div class="with-mid-padding mid-margin-bottom icholderinner icholderinner-${homeReport.tabCount}" style="min-width:200px;overflow-x:auto !important">
							<div class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer-${homeReport.tabCount}" style="line-height:6px;min-width:720px">
								<div style="width:100%; text-align: center;"><img src="themes/acsi/img/standard/loaders/loading64.gif" /></div>
							</div>
						</div>
					</div>
				</div>
			</form:form>
			</c:if>
			</c:if>
			<c:if test="${homeReport.reportApiUrl != 'openReportHtmlApi'}">
				<div class="pagination pagination-${homeReport.tabCount} align-right panel-control panel-control-pagination margin-bottom-small rounded-border display-none"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}">
					<span style="left:10px;position:absolute">Page <span class="pageCurrent">1</span> of <span class="pageEnd">1</span></span>
					<a href="#nogo" class="page_first paginate button compact icon-previous grey-gradient glossy with-tooltip disabled" title="First" page="0" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}"></a>
					<a href="#nogo" class="page_prev paginate button compact icon-backward grey-gradient glossy with-tooltip disabled" title="Prev" page="" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}"></a>
					<a href="#nogo" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Next" page="" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}"></a>
					<a href="#nogo" class="page_last paginate button compact icon-next grey-gradient glossy with-tooltip" title="Last" page="" reportid="${homeReport.reportId}" param="${homeReport.reportUrl}"  tabCount="${homeReport.currentTabNumber}" count="${homeReport.tabCount}"></a>
				</div>
			</c:if>			
			<div id="reportContainer" class="report-container-${homeReport.tabCount} tabs-content padding-small">
				<div class="reportLoading" id="loading${homeReport.tabCount}">
					<div style="width:100%; text-align: center;"></div>
				</div>
				<div class="iframeWrapper iframeWrapper-${homeReport.tabCount}">
					<iframe id='report-iframe-${homeReport.tabCount}' class="report-frame report-frame-${homeReport.tabCount}" onLoad="closeProgress('${homeReport.reportUrl}', '${homeReport.tabCount}')"
						src="${homeReport.reportApiUrl}?assessmentId=${homeReport.assessmentName}&reportId=${homeReport.reportId}&reportUrl=${homeReport.reportUrl}&reportName=${homeReport.reportName}&studentId=${homeReport.studentBioId}&reportType=${homeReport.reportType}" 
						frameborder="0"
						marginwidth="0"
						marginheight="0"
						scrolling="${homeReport.scrolling}">
					</iframe>
				</div>
			</div>
		</div>
		
	</div>
	
	<div id='homeTab' class='display-none'>
		<input type="hidden" name="reportUrl" id="reportUrl" value="${homeReport.reportUrl}">
		<input type="hidden" name="assessment" id="assessment" value="${homeReport.assessmentName}">
		<input type="hidden" name="reportName" id="reportName" value="${homeReport.reportName}">
		<input type="hidden" name="reportId" id="reportId" value="${homeReport.reportId}">
		<input type="hidden" name="studentId" class="studentIdForTab" value="${homeReport.studentBioId}">
	</div>
</div>
