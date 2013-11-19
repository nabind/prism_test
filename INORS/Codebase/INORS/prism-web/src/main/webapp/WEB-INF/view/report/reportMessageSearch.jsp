<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="content-panel" style="padding-left: 0px; border: 0px">
	<hgroup id="main-title" class="thin">
		<h1>Configure Report Message</h1>
	</hgroup>
	<div class="right-column">
				<div class="content-panel" style="padding-left: 0px; border-radius: 0px 8px 8px 8px">
					<div class="report-panel-content panel-content linen">
					
	<div class="scrollable manage-message" style="min-height: 585px; color: #666">
		<div
			class="panel-control panel-control-report align-right padding-right"></div>
		<div
			class="panel-load-target scrollable with-padding report-layout padding-none">
			<form:form method="post" class="report-form"
				id="reportMessageSearchForm">
				<div class="mid-margin-bottom">
					<div class="reportFilterCriteria panel-control rounded-border">
						<span id="filter-icon" class="icon-leaf icon-size2"></span> 
						<b>Configure message for: ${serviceMapReportMessageFilter.reportName}</b>
					</div>
					<div class="cyan-gradient icholder rounded-border-bottom"
						style="border-bottom: 1px solid #CCC;">
						<div class="refresh-report"
							style="position: absolute; top: 5px; right: 20px;">
							<a href="javascript:void(0)" id="searchMessage"
								class="button blue-gradient glossy">Search Message</a> <input
								type="hidden" name="reportId" id="reportId"
								value="${serviceMapReportMessageFilter.reportId}" /> <input
								type="hidden" name="reportName" id="reportName"
								value="${serviceMapReportMessageFilter.reportName}" /> <input
								type="hidden" name="reportUrl" id="reportUrl"
								value="${serviceMapReportMessageFilter.reportUrl}" />
						</div>
						<div class="with-mid-padding mid-margin-bottom icholderinner"
							style="min-width: 200px; overflow-x: auto">
							<div
								class="columns margin-bottom-medium margin-bottom-medium-ve inputControlContailer inputControlContailer"
								style="height: 10px; min-width: 720px">
								<div class="three-columns report-inputs">
									<h6 class="margin-bottom-small">Test Administration</h6>
									<div class="float-left margin-right margin-bottom">
										<p class="button-height">
											<select id="custProdId" name="custProdId"
												class="select multiple-as-single easy-multiple-selection navy-gradient check-list expandable-list"
												style="width: 150px;">
												<c:forEach var="customerProductTO"
													items="${serviceMapReportMessageFilter.customerProductList}">
													<option value="${customerProductTO.value}">${customerProductTO.name}</option>
												</c:forEach>
											</select>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form:form>
		</div>
		<div id="reportMessage"></div>
		<div id="copyMessageModal" class="display-none">
			<p class="button-height inline-label">
				<label class="label" for="reportType" style="width:150px">Test Administration</label> 
				<select
					id="custProdIdModal" name="custProdIdModal"
					class="select multiple-as-single easy-multiple-selection check-list expandable-list">
					<c:forEach var="customerProductTO"
						items="${serviceMapReportMessageFilter.customerProductList}">
						<option value="${customerProductTO.value}">${customerProductTO.name}</option>
					</c:forEach>
				</select>
			</p>
			<div id="imgHolder"></div>
		</div>
	</div>
</div>
</div>
	</div>
</div>
