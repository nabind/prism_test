

		<div id="newICModal" class="display-none">
			<div class="">
				<form:form id="claim-Invitation-Form" method="post" class="claim-Invitation-Form">
					<input type="hidden" name="roleId" id="roleId"/>
				
			<div id="displayInvitation">			
				<h3>Invitation code</h3>
				<div class="margin-bottom">
					<p class="message blue-gradient">Enter the 16-digit Invitation Code supplied by your child's school here. Please type in the Invitation Code exactly as it appears in the letter you received from your child's school (use capital letters and include hyphens).
					<br/><br/>If you do not have a letter, please contact your child's school.</p>
				</div>
				<div class="field-block button-height">
					
					<br><label for="invitationCode" class="label"><b>Invitation Code</b></label>&nbsp;
					<input type="text" name="invitationCode" id="invitationCode" value="" class="input validate[required]"/>
					<p class="message red-gradient margin-top display-none" id="invalidICMsg">The code you have entered is not valid. Make sure you have entered exactly same code as you received in letter.</p>
				</div>
				<!--<div class="margin-bottom">
					<br/><br/><p class="message grey-gradient">NOTE: Please type in the Invitation Code exactly as it appears in the letter you received from your student's school (use capital letters and include hyphens).</p>
				</div>-->
			</div>
			
			
			<div id="displayChild">
			<h3>Verify your child's information</h3>
			<div class="diaplay-none margin-top margin-bottom">
				<p class="big-message">
					<span class="big-message-icon icon-speech"></span>
					By clicking "Submit," you are acknowledging that you are authorized to view the following child's information.</p>
					<div id = "ic_student_list" class="field-block">

					</div>
				<p class="message blue-gradient margin-top">
					<span class="message-icon icon-warning"></span>
					<b>Note:</b> Please contact your child's school if you have questions regarding the Invitation Code. If you are not the parent or guardian authorized to view this student information, or if you believe you have entered the wrong Invitation Code, you must press "Cancel" and contact your child's school.
				</p>
			</div>
			</div>
					
				</form:form>
			</div>
		</div>




