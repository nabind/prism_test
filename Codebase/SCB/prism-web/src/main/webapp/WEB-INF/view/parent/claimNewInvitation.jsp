		<div id="newICModal" class="display-none">
			<div class="">
				<form:form id="claim-Invitation-Form" method="post" class="claim-Invitation-Form">
					<input type="hidden" name="roleId" id="roleId"/>
					<div id="displayInvitation">			
						<h3><spring:message code="label.invitationCode" /></h3>
						<div class="margin-bottom">
							<p class="message blue-gradient"><spring:message code="p.claimNewInvitation.1" /></p>
						</div>
						<div class="field-block button-height">
							<br><label for="invitationCode" class="label"><b><spring:message code="thead.assessment.invitationCode" /></b></label>&nbsp;
							<input type="text" name="invitationCode" id="invitationCode" value="" class="input validate[required]"/>
							<p class="message red-gradient margin-top display-none" id="invalidICMsg"><spring:message code="p.claimNewInvitation.2" /></p>
						</div>
					</div>
					<div id="displayChild">
					<h3><spring:message code="label.verifyChildInformation" /></h3>
					<div class="diaplay-none margin-top margin-bottom">
						<p class="big-message">
							<span class="big-message-icon icon-speech"></span>
							<spring:message code="p.claimNewInvitation.3" /></p>
							<div id = "ic_student_list" class="field-block">
							</div>
						<p class="message blue-gradient margin-top">
							<span class="message-icon icon-warning"></span>
							<b><spring:message code="label.note" />:</b> <spring:message code="p.claimNewInvitation.4" />
						</p>
					</div>
					</div>
				</form:form>
			</div>
		</div>
