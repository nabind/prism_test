	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	<!-- Main content -->
	<section role="main" id="main" style="margin-top:69px" >
		<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
		<div class="with-padding" style="background-color:#FFF">
			<div class="right-column">
					<div class="standard-tabs margin-bottom" id="add-tabs" style="margin: 0 0px 10px !important">
						<ul class="tabs">
							<li class="active"><a href="#tab-1"><spring:message code="li.body.1" /></a></li>
							<li><a href="#tab-2"><spring:message code="li.body.2" /></a></li>
							<li><a href="#tab-3"><spring:message code="li.body.3" /></a></li>
							<li class="disabled"><a href="#tab-4"><spring:message code="li.body.4" /></a></li>
							<li><spring:message code="li.body.5" /></li>
						</ul>
						<div class="tabs-content">
							<div id="tab-1" class="with-padding" style="padding:0px !important">
								<div class="content-panel margin-bottom" style="padding-left:0px; border: none">
									<div class="panel-content linen">
										<div class="panel-control align-right">
											<span class="progress thin" style="width: 40px">
												<span class="progress-bar green-gradient" style="width: 35%"></span>
											</span>
											<spring:message code="msg.body.35" />
											<a href="#" class="button icon-cloud-upload margin-left"><spring:message code="label.addFile" /></a>
										</div>
										<div class="panel-load-target scrollable with-padding" style="height:450px;margin: 10px;" >
											<details class="details margin-bottom" >
												<summary><spring:message code="label.detailSummary" /></summary>
												<div class="with-padding">
													<p><spring:message code="p.body.1" /></p>
													<p><spring:message code="p.body.2" /></p>
													<a href="javascript:void(0)" class="button blue-gradient"><spring:message code="label.blue" /></a>
												</div>
											</details>
											<p class="message icon-info-round white-gradient">
												<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
												<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
											</p>
										</div>
									</div>
								</div>
							</div>
							<div id="tab-2" class="with-padding">
								<spring:message code="label.alternateTab.1" />
							</div>
							<div id="tab-3" class="with-padding">
								<spring:message code="label.alternateTab.2" />
							</div>
							<div id="tab-4" class="with-padding">
								<spring:message code="li.body.4" />
							</div>
						</div>
					</div>
			</div>
			<p class="wrapped left-icon icon-info-round"><spring:message code="p.body.3" /></p>
		</div>
	</section>
	<!-- End main content -->