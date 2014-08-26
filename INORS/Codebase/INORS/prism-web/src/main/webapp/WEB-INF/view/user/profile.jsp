<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="manageProfile" name="manageProfile" class="manage-Profile-form">
	<input type="hidden" name="userId" id="userId" value="${parentAccountDetail.userId}" />
	<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1>My Account</h1>
	</hgroup>

	<style>
		.field-block {margin: 0;}
	</style>


	<div class="manageAccount" style="min-width:765px">

		<div class="left-column-200px">

			<div class="left-column">
				<div class="boxed rounded-border">
					<p>Using this page you can change your basic information along with your contact details and even user password. Some important tips for your account:</p>
					<ul class="bullet-list">
						<li>Never disclose your password to anyone. It is a good practice to change your password once in a month.</li>
						<li>Your email may be used to communicate important announcements about your account and help you locate a lost password.</li>
						<li>If you ever lose your password, using combination of security questions you can reset your password.</li>
					</ul>
				</div>
			</div>

			<div class="right-column">
				<p id="mandatoryValidation" class="message red-gradient red-color display-none">
						<span class="message-icon icon-warning with-text bold">Please complete all required fields.</span>
				</p>
				<div class="standard-tabs margin-bottom" id="add-tabs">
					<ul class="tabs myaccount">
						<li class="active"><a href="#tab-1">Basic Information</a></li>
						<li><a href="#tab-2" id="tabContactDetails" >Contact Details</a></li>
						<li><a href="#tab-3">Security Questions</a></li>
					</ul>

					<div id="manageProfile" class="tabs-content">

						<div id="tab-1" class="with-padding margin-top margin-bottom-medium">
							<div class="field-block button-height" style="margin-left:0px; margin-right:0px">
								<label for="username" class="label"><b>Username</b></label>
								${parentAccountDetail.userName}
								<input type="hidden" name="username" id="username" value="${parentAccountDetail.userName}" class="input">
								<!--UserName-->
							</div>
							<div class="field-block button-height" style="margin-left:0px; margin-right:0px">
								<label for="displayName" class="label"><b>Display Name</b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="displayName" id="displayName" value="${parentAccountDetail.displayName}" class="input validate[required, minSize[3], maxSize[10]]">
							</div>
							<div class="field-block button-height">
								<label for="password" class="label"><b>Change Password</b></label>
								<input type="password" name="password" id="password" value="" class="input validate[minSize[8], maxSize[15], custom[validatePassword]]" autocomplete="off">
								<span class="info-spot">
									<span class="icon-info-round"></span>
									<span class="info-bubble" style="width:250px">
										Passwords are case-sensitive. They must be at least 8 characters long and include at least one number, one uppercase letter, and one lowercase letter.
									</span>
								</span>
							</div>
							
							<div id ="verify_password"  class="field-block button-height">
								<label for="password_again" class="label"><b>Verify Password</b></label>
								<input type="password" name="password_again" id="password_again" value="" class="input validate[equalsPassword[password] ]"> <!--funcCall[checkPwd]  Password -->
							</div>
				
							<div class="field-block button-height">
								<label for="firstName" class="label"><b>First Name</b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="firstName" id="firstName" value="${parentAccountDetail.firstName}" class="input validate[required,maxSize[30]]">
							</div>
				
							<div class="field-block button-height">
								<label for="lastName" class="label"><b>Last Name</b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="lastName" id="lastName" value="${parentAccountDetail.lastName}" class="input validate[required,maxSize[30]]">
							</div>
						</div>

						<div id="tab-2" class="with-padding margin-top margin-bottom-medium">
							<div class="field-block button-height">
								<label for="verify_mail" class="label"><b>Email Address</b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="verify_mail" id="verify_mail" value="${parentAccountDetail.mail}" maxlength="100" class="input validate[required, custom[email]]" >
								<span class="info-spot">
									<span class="icon-info-round"></span>
									<span class="info-bubble" style="width:250px">
										Your email may be used to communicate important announcements about your account and help you locate a lost password. We will never distribute your information to third parties. For more details, please refer to the Security and Privacy Policy.
									</span>
								</span>	
							</div>
				
							<div class="field-block button-height">
								<label for="mobile" class="label">Contact Number</label>
								<input type="text" name="mobile" id="mobile" value="${parentAccountDetail.mobile}" class="input validate[maxSize[15],custom[phone]]">
							</div>
				
							<div class="field-block button-height addressContainer" style="min-height:213px">
								<span class="label">Address</span>
								<input type="hidden" name="userCountry" id="userCountry" value="${parentAccountDetail.country}">
								<p class="button-height">
									<span class="input">
										<label for="street" class="blue">Street:</label>
										<input type="text" name="street" id="street" class="input-unstyled validate[maxSize[50]]"  value="<c:if test='${parentAccountDetail.street != null && not empty parentAccountDetail.street }'>${parentAccountDetail.street}</c:if>" >
									</span>
								</p>
								<p class="button-height">
								 <span class="label">&nbsp;</span>
									<span class="input">
										<label for="street" class="blue">City:</label>
										<input type="text" name="city" id="city" class="input-unstyled validate[maxSize[50]]"  value="<c:if test='${parentAccountDetail.city != null && not empty parentAccountDetail.city }'>${parentAccountDetail.city}</c:if>" >
									</span>
								</p>
								<p class="button-height">
									<span class="input">
										<label for="city" class="blue">State:</label>
										<input type="text" name="state" id="state" class="input-unstyled validate[maxSize[40]]" value="<c:if test='${parentAccountDetail.state != null && not empty parentAccountDetail.state }'>${parentAccountDetail.state}</c:if>">
									</span>
									<span class="label">&nbsp;</span><span class="input">
										<label for="zip_code" class="blue">Zip:</label>
										<input type="text" name="zip_code" id="zip_code" value="<c:if test='${parentAccountDetail.zipCode != null && not empty parentAccountDetail.zipCode }'>${parentAccountDetail.zipCode}</c:if>" class="input-unstyled validate[maxSize[8]]"  size="3">
									</span>
								</p>
								<p class="button-height">
									<span class="label">&nbsp;</span><%@ include file="../common/country.jsp" %>
								</p>
								
							</div>
						</div>

						<div id="tab-3" class="with-padding margin-top margin-bottom-medium">
						
							<%@ include file="../parent/securityQuestion.jsp" %>

						</div>

					</div>

				</div>
				<div class="float-right"> 
					<a href="myAccount.do" class="button margin-left glossy with-tooltip" title="Undo changes" >
						<span class="button-icon black-gradient manage-btn"><span class="icon-undo"></span></span>
						Cancel
					</a>
					<a href="#nogo" class="button margin-left glossy with-tooltip save-Profile" title="Save all changes" >
						<span class="button-icon blue-gradient manage-btn"><span class="icon-tick"></span></span>
						Save
					</a>
				</div>
				<%@ include file="../common/required.jsp" %>
				<div style="clear:both;line-height:0px;height:0px;">
				</div>

			</div>
			
			<!-- Fix for TD 78524,78525(Block the inclusion) -->
			<!-- @ include file="../parent/claimNewInvitation.jsp" -->

		</div>
	
	
	</div>
</form:form>
