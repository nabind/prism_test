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
				<label class="label"><b><spring:message code="label.username" /></b></label>
				<span id="statusUsernameRP" class="input-unstyled"></span>
			</div>
			<div class="field-block button-height">
				<label class="label"><b><spring:message code="label.emailSendingStatus" /></b></label>
				<span id="statusEmailRP" class="input-unstyled"></span>
			</div>
		</fieldset>
		<fieldset id="userDetailsRP" class="wizard-fieldset fields-list hidden" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.userDetails" /></legend>
			<div class="field-block button-height">
				<label for="firstNameRP" class="label"><b><spring:message code="label.firstName" /></b></label>
				<span id="firstNameRP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="middleNameRP" class="label"><b><spring:message code="label.middleName" /></b></label>
				<span id="middleNameRP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="lastNameRP" class="label"><b><spring:message code="label.lastName" /></b></label>
				<span id="lastNameRP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="emailRP" class="label"><b><spring:message code="label.email" /></b></label>
				<span id="emailRP" class="input-unstyled" style="width: 300px; "></span>
			</div>
			<div class="field-block button-height">
				<label for="contactNumberRP" class="label"><spring:message code="label.contactNumber" /></label>
				<span id="contactNumberRP" class="input-unstyled"></span>
			</div>
			<div class="field-block button-height addressContainer">
				<label class="label"><spring:message code="label.address" /></label>
				<p class="button-height">
					<span class="">
						<label for="streetRP" class="blue"><spring:message code="label.street" />:</label>
						<span id="streetRP" class="input-unstyled"></span>
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span>

					<span class="">
						<label for="cityRP" class="blue"><spring:message code="label.city" />:</label>
						<span id="cityRP" class="input-unstyled"></span>
					</span>
				</p>
				<p class="button-height">
					<span class="">
						<label for="stateRP" class="blue"><spring:message code="label.state" />:</label>
						<span id="stateRP" class="input-unstyled"></span>
					</span>
					<span class="label">&nbsp;</span>
					<span class="">
						<label for="zipRP" class="blue"><spring:message code="label.zip" />:</label>
						<span id="zipRP" class="input-unstyled"></span>
					</span>
				</p>
				<p class="button-height">
					<span id="countryRP" class="input-unstyled"></span>
				</p>
			</div>
		</fieldset>
		<fieldset id="securityQuestionsRP" class="wizard-fieldset fields-list hidden" style="border-bottom: 1px solid #e0e0e0;">
			<legend class="legend"><spring:message code="label.securityQuestions" /></legend>
			<div class="field-block button-height">
				<label for="question1RP" class="label"><b><spring:message code="label.question1" /></b></label>
				<span id="question1RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="answer1RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<span id="answer1RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="question2RP" class="label"><b><spring:message code="label.question2" /></b></label>
				<span id="question2RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="answer2RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<span id="answer2RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="question3RP" class="label"><b><spring:message code="label.question3" /></b></label>
				<span id="question3RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
			<div class="field-block button-height">
				<label for="answer3RP" class="label"><b><spring:message code="label.answer" /></b></label>
				<span id="answer3RP" class="input-unstyled" style="width: 300px;"></span>
			</div>
		</fieldset>
		<input type="hidden" id="userIdRP" value="0"/>
	</div>