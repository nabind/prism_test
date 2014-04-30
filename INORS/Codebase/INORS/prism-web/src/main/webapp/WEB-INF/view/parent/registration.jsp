<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String agent = request.getHeader("User-Agent");
	boolean isIe = false;
	if(agent != null && agent.indexOf("MSIE") != -1) isIe = true;
%>
<% if(isIe) { %>
	<div id="registrationFormContainer">
<%}%>
	<form:form method="post" action="register.do" id="registrationForm" class="block wizard same-height top-registration margin-buttom registration">
		<h3 class="block-title">Registration</h3>

		<fieldset class="wizard-fieldset fields-list noEnterSubmit">

			<legend class="legend">Invitation Code</legend>

			<div class="margin-bottom">
				<p class="message blue-gradient">Enter the 16-digit Invitation Code supplied by your child's school here. Please type in the Invitation Code exactly as it appears in the letter you received from your child's school (use capital letters and include hyphens).
				<br/><br/>If you have previously entered the Invitation Code for one child, and are attempting to enter Invitation Codes for additional children, please return to the main screen, log in and under "Manage Account", click "Enter new Invitation Code".</p>
			</div>

			<div class="field-block button-height">
				
				<label for="invitationCode" class="label"><b>Invitation Code</b></label>&nbsp;
				<input type="text" name="invitationCode" id="invitationCode" value="" class="input validate[required]"/>
				<p class="message red-gradient margin-top display-none" id="invalidICMsg"></p>
				
			</div>
			
		</fieldset>
		
		<fieldset class="wizard-fieldset fields-list noEnterSubmit">

			<legend class="legend">Verify your child's information</legend>

			<div class="diaplay-none margin-top margin-bottom">
				<p class="big-message">
					<span class="big-message-icon icon-speech"></span>
					By clicking "Next", you are acknowledging that you are authorized to view the following child's information.</p>
					<div id = "ic_student_list" class="field-block">

					</div>
				<p class="message blue-gradient margin-top">
					<span class="message-icon icon-warning"></span>
					<b>Note:</b> Please contact your child's school if you have questions regarding the invitation code.
				</p>
			</div>

		</fieldset>

		<fieldset class="wizard-fieldset fields-list noEnterSubmit">

			<legend class="legend">Profile</legend>

			<div class="field-block button-height" id="usernameDiv">
				<%@ include file="../common/required.jsp" %>
				<label for="username" class="label"><b>Choose a Username</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="username" id="username" value="" class="input validate[required,custom[onlyLetterNumber],maxSize[30],minSize[3]]" autocomplete="off">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						Usernames must be at least 3 characters. Choose something that will be easy to remember, but be careful - once you choose a username it may not be changed. Choose a username that you have not tried to use previously.
					</span>
				</span>
				<span id="imgHolder"></span>
			</div>
			<div class="field-block button-height">	
				<small class="input-info">This is the name that will be displayed on profile page</small>
				<label for="displayName" class="label"><b>Display Name</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="displayName" id="displayName" value="" class="input validate[required, minSize[3], maxSize[10]]" autocomplete="off">
			</div>
			
			<div class="field-block button-height">
				<label for="password" class="label"><b>Create a Password</b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password" id="password" value="" class="input validate[required,maxSize[15],minSize[8]]" autocomplete="off">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						Passwords are case-sensitive. They must be at least 8 characters long and include at least one number, one uppercase letter, and one lowercase letter.
					</span>
				</span>
				
			</div>
			
			<div id ="verify_password"  class="field-block button-height">
				<label for="password_again" class="label"><b>Verify Password</b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password_again" id="password_again" value="" class="input validate[required ,equalsPassword[password] ]" autocomplete="off"> <!--funcCall[checkPwd]  Password -->
			</div>

			<div class="field-block button-height">
				<label for="firstName" class="label"><b>First Name</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="firstName" id="firstName" value="" class="input validate[required,maxSize[30]]">
			</div>

			<div class="field-block button-height">
				<label for="lastName" class="label"><b>Last Name</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="lastName" id="lastName" value="" class="input validate[required,maxSize[30]]">
			</div>

		</fieldset>

		<fieldset class="wizard-fieldset fields-list noEnterSubmit">

			<legend class="legend">Contacts</legend>
			<div class="field-block button-height">
				<%@ include file="../common/required.jsp" %><br/>
				<label for="mail" class="label"><b>Email Address</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="mail" id="mail" value="" maxlength="100" class="input validate[required,custom[email]]">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						Your email may be used to communicate important announcements about your account and help you locate a lost password. We will never distribute your information to third parties. For more details, please refer to the Security and Privacy Policy.
					</span>
				</span>		
			</div>
			
			<div class="field-block button-height">
				<label for="verify_mail" class="label"><b>Verify Email</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="verify_mail" id="verify_mail" value="" maxlength="100" class="input validate[required,custom[email] ,equalsEmail[mail]]" >
			</div>

			<div class="field-block button-height">
				<label for="mobile" class="label">Contact Number</label>
				<input type="text" name="mobile" id="mobile" value="" class="input validate[maxSize[15],custom[phone]]">
			</div>

			<div class="field-block button-height addressContainer">
				<span class="label">Address</span>
				<!--  <input type="hidden" name="userCountry" id="userCountry" value="">-->
				<p class="button-height">
					<span class="input">
						<label for="street" class="blue">Street:</label>
						<input type="text" name="street" id="street" value="" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span>
					<span class="input">
						<label for="street" class="blue">City:</label>
						<input type="text" name="city" id="city" value="" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="input">
						<label for="city" class="blue">State:</label>
						<input type="text" name="state" id="state" value="" class="input-unstyled validate[maxSize[40]]">
					</span>
					<span class="label">&nbsp;</span><span class="input">
						<label for="zip_code" class="blue">Zip:</label>
						<input type="text" name="zip_code" id="zip_code" value="" class="input-unstyled validate[maxSize[8]]" size="3">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span><%@ include file="../common/country.jsp" %>
				</p>
				
			</div>

		</fieldset>

		<fieldset class="wizard-fieldset fields-list ">

			<legend class="legend noEnterSubmit">Security Questions</legend>
			<div class="field-block noEnterSubmit">
				<%@ include file="../common/required.jsp" %>
				<h4>Set up your Security Questions</h4>
				<p>If you ever lose your password, you will be able to reset your password. You will first be required to answer your choice of security questions correctly. Choose three different questions from the list below and provide your own answers. Try to pick questions that no one else could answer for you.</p>
			</div>
			<%@ include file="../parent/securityQuestion.jsp" %>

			<div class="field-block button-height wizard-controls align-right">

				<button type="button" id="regSubmit" class="button glossy mid-margin-right" >
					<span class="button-icon"><span class="icon-tick"></span></span>
					Save
				</button>

			</div>

		</fieldset>
	</form:form>
<% if(isIe) { %>
	</div>
<%}%>

<div style="margin-top:130px" ></div>
