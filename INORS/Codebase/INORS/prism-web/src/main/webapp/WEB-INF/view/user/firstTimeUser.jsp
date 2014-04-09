<%@page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>

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
<form:form method="post" id="changePasswordFrom" action="welcome.do" class="block wizard same-height top-registration margin-buttom">

		<h3 class="block-title">Change Password</h3>

		<fieldset class="wizard-fieldset fields-list">

			<legend class="legend">Password</legend>

			<div class="margin-bottom">
				<p class="message blue-gradient">You should change your password on first login. Please use the fields below to change your password.</p>
			</div>

			<div class="field-block button-height">
				<%@ include file="../common/required.jsp" %>
				<label for="username" class="label"><b>Username</b></label>
				<span id="currUser"><%=request.getSession().getAttribute(IApplicationConstants.CURRUSER) %></span>
				<input type="hidden" name="loggedInUserName" id="loggedInUserName" value="">
			</div>
			
			<div class="field-block button-height passwordContainer">
				<label for="password" class="label"><b>Create Password</b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password" id="password" value="" class="input validate[required]">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						Passwords are case-sensitive. They must be at least 8 characters long and include at least one number, one uppercase letter, and one lowercase letter.
					</span>
				</span>
				
			</div>
			
			<div id ="verify_password"  class="field-block button-height">
				<label for="password_again" class="label"><b>Verify Password</b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password_again" id="password_again" value="" class="input validate[required ,equalsPassword[password] ]"> <!--funcCall[checkPwd]  Password -->
			</div>
			
		</fieldset>
		<c:if test="${not empty parentDetails}">
		<fieldset class="wizard-fieldset fields-list">
			<legend class="legend">Profile</legend>
			<div class="field-block button-height">
				<%@ include file="../common/required.jsp" %>
				<label for="firstName" class="label"><b>First Name</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="firstName" id="firstName" value="${parentDetails.firstName}" class="input validate[required, maxSize[30]]">
			</div>

			<div class="field-block button-height">
				<label for="lastName" class="label"><b>Last Name</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="lastName" id="lastName" value="${parentDetails.lastName}" class="input validate[required, maxSize[30]]">
			</div>
			<div class="field-block button-height">
				<label for="mail" class="label"><b>Email Address</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="mail" id="mail" value="${parentDetails.mail}" maxlength="100" class="input validate[required,custom[email]]">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						Your email may be used to communicate important announcements about your account and help you locate a lost password. We will never distribute your information to third parties. For more details, please refer to the Security and Privacy Policy.
					</span>
				</span>		
			</div>
			
			<div class="field-block button-height">
				<label for="verify_mail" class="label"><b>Verify Email</b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="verify_mail" id="verify_mail" value="${parentDetails.mail}" maxlength="100" class="input validate[required,custom[email] ,equalsEmail[mail]]" >
			</div>

			<div class="field-block button-height">
				<label for="mobile" class="label">Contact Number</label>
				<input type="text" name="mobile" id="mobile" value="${parentDetails.mobile}" class="input validate[maxSize[15],custom[phone]]">
			</div>

			<div class="field-block button-height addressContainer">
				<span class="label">Address</span>
				<input type="hidden" name="userCountry" id="userCountry" value="${parentDetails.country}">
				<p class="button-height">
					<span class="input">
						<label for="street" class="blue">Street:</label>
						<input type="text" name="street" id="street" value="${parentDetails.street}" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span>
					<span class="input">
						<label for="street" class="blue">City:</label>
						<input type="text" name="city" id="city" value="${parentDetails.city}" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="input">
						<label for="city" class="blue">State:</label>
						<input type="text" name="state" id="state" value="${parentDetails.state}" class="input-unstyled validate[maxSize[40]]">
					</span>
					<span class="label">&nbsp;</span><span class="input">
						<label for="zip_code" class="blue">Zip:</label>
						<input type="text" name="zip_code" id="zip_code" value="${parentDetails.zipCode}" class="input-unstyled validate[maxSize[8]]" size="3">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span><%@ include file="../common/country.jsp" %>
				</p>
				
			</div>
		</fieldset>
		</c:if>
		<c:if test="${empty parentDetails}">
				<fieldset class="wizard-fieldset fields-list">
					<legend class="legend">Profile</legend>
										
					<div class="field-block button-height">
						<%@ include file="../common/required.jsp" %><br/>
						<label for="firstName" class="label"><b>First Name</b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="firstName" id="firstName" value="" class="input validate[required,maxSize[30]]">
					</div>

					<div class="field-block button-height">
						<label for="lastName" class="label"><b>Last Name</b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="lastName" id="lastName" value="" class="input validate[required,maxSize[30]]">
					</div>
					<div class="field-block button-height">
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
						<input type="hidden" name="userCountry" id="userCountry" value="">						
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
		</c:if>
		<fieldset class="wizard-fieldset fields-list">

			<legend class="legend">Security Questions</legend>
			<div class="field-block">
				<%@ include file="../common/required.jsp" %>
				<h4>Set up your Security Questions</h4>
				<p>If you ever lose your password, we will be able to send you a new one to the email address you provided. You will first be required to answer your choice of security questions correctly. Choose three different questions from the list below and provide your own answers. Try to pick questions that no one else could answer for you.</p>
			</div>
			<%@ include file="../parent/securityQuestion.jsp" %>
			
			<div class="field-block button-height wizard-controls align-right">

				<button id="saveChangedPassword" type="button" class="button glossy mid-margin-right">
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
