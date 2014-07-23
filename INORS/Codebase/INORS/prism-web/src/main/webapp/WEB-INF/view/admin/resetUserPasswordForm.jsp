<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="com.ctb.prism.core.constant.IApplicationConstants" %>
<style>
.field-block, .button-height {
margin-top: 0px !important;
padding-top: 0px !important;
}
</style>
<div class="main-title same-height" style="min-height: 613px;">
		<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1><spring:message code="heading.resetPassword" /></h1>
	</hgroup>
		<fieldset class="wizard-fieldset fields-list" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.searchUser" /></legend>
			<div class="field-block button-height">
				<label for="first_name" class="label"><b><spring:message code="label.username" /></b></label>
				<span class="input search-input">
					<input type="text" name="userSearchRP" id="userSearchRP" class="input-unstyled with-tooltip" title="<spring:message code="title.searchUser" />" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="userSearchIconRP"></a>
				</span>
				<a class="button margin-left glossy with-tooltip reset-pwd-search" title="<spring:message code="button.clearSearch" />" href="javascript:void(0)">
					<span class="button-icon black-gradient">
						<span class="icon-undo"></span>
					</span>
					<spring:message code="button.clearSearch" />
				</a>
				<a class="button margin-left glossy with-tooltip reset-pwd" title="<spring:message code="heading.resetPassword" />" href="javascript:void(0)">
					<span class="button-icon blue-gradient">
						<span class="icon-tick"></span>
					</span>
					<spring:message code="heading.resetPassword" />
				</a>
			</div>
		</fieldset>
		<fieldset id="passwordResetStatusRP" class="wizard-fieldset fields-list hidden" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.passwordResetStatus" /></legend>
			<div class="button-height">
				<p class="message icon-speech green-gradient small-margin-bottom">
						<span class="block-arrow bottom"><span></span></span>
						<span class="white" id="passwordResetStatusMsgRP">&nbsp;</span>
					</p>
			</div>
			<div class="field-block button-height">
				<label for="statusUsernameRP" class="label"><b><spring:message code="label.username" /></b></label>
				<input name="statusUsernameRP" id="statusUsernameRP" class="input-unstyled">
			</div>
			<div class="field-block button-height">
				<label for="statusPasswordRP" class="label"><b><spring:message code="label.newPassword" /></b></label>
				<input name="statusPasswordRP" id="statusPasswordRP" class="input-unstyled" style="font-size: 16px; font-weight: bold;font-family: monospace;">
			</div>
			<div class="field-block button-height">
				<label for="statusEmailRP" class="label"><b><spring:message code="label.emailSendingStatus" /></b></label>
				<span name="statusEmailRP" id="statusEmailRP" class="input-unstyled"></span>
			</div>
		</fieldset>
		<fieldset id="userDetailsRP" class="wizard-fieldset fields-list hidden" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.userDetails" /></legend>
			<div class="field-block button-height">
				<label for="firstNameRP" class="label"><b><spring:message code="label.firstName" /></b></label>
				<input type="text" name="firstNameRP" id="firstNameRP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="middleNameRP" class="label"><b><spring:message code="label.middleName" /></b></label>
				<input name="middleNameRP" id="middleNameRP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="lastNameRP" class="label"><b><spring:message code="label.lastName" /></b></label>
				<input name="lastNameRP" id="lastNameRP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="emailRP" class="label"><b><spring:message code="label.email" /></b></label>
				<input name="emailRP" id="emailRP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="contactNumberRP" class="label"><spring:message code="label.contactNumber" /></label>
				<input name="contactNumberRP" id="contactNumberRP" class="input-unstyled">
			</div>
			<div class="field-block button-height addressContainer">
				<label class="label"><spring:message code="label.address" /></label>
				<p class="button-height">
					<span class="">
						<label for="streetRP" class="blue"><spring:message code="label.street" />:</label>
						<input name="streetRP" id="streetRP" value="" class="input-unstyled">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span>

					<span class="">
						<label for="cityRP" class="blue"><spring:message code="label.city" />:</label>
						<input name="cityRP" id="cityRP" value="" class="input-unstyled">
					</span>
				</p>
				<p class="button-height">
					<span class="">
						<label for="stateRP" class="blue"><spring:message code="label.state" />:</label>
						<input name="stateRP" id="stateRP" value="" class="input-unstyled">
					</span>
					<span class="label">&nbsp;</span>
					<span class="">
						<label for="zipRP" class="blue"><spring:message code="label.zip" />:</label>
						<input name="zipRP" id="zipRP" value="" class="input-unstyled">
					</span>
				</p>
				<p class="button-height">
					<input name="countryRP" id="countryRP" value="" class="input-unstyled">
				</p>
			</div>
		</fieldset>
		<fieldset id="securityQuestionsRP" class="wizard-fieldset fields-list hidden" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.securityQuestions" /></legend>
			<div class="field-block button-height">
				<label for="question1RP" class="label"><b><spring:message code="label.question1" /></b></label>
				<input name="question1RP" id="question1RP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="answer1RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<input name="answer1RP" id="answer1RP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="question2RP" class="label"><b><spring:message code="label.question2" /></b></label>
				<input name="question2RP" id="question2RP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="answer2RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<input name="answer2RP" id="answer2RP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="question3RP" class="label"><b><spring:message code="label.question3" /></b></label>
				<input name="question3RP" id="question3RP" class="input-unstyled" style="width: 300px;">
			</div>
			<div class="field-block button-height">
				<label for="answer3RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<input name="answer3RP" id="answer3RP" class="input-unstyled" style="width: 300px;">
			</div>
		</fieldset>
		<input type="hidden" id="userIdRP" value="0"/>
	</div>