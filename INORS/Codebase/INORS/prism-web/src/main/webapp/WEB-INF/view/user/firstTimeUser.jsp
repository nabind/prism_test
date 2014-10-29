<%@ page import="com.ctb.prism.core.constant.IApplicationConstants, javax.servlet.http.HttpServletRequest"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
		<h3 class="block-title"><spring:message code="label.changePassword" /></h3>
		<fieldset class="wizard-fieldset fields-list">
			<legend class="legend"><spring:message code="label.password" /></legend>
			<div class="margin-bottom">
				<p class="message blue-gradient"><spring:message code="info.changePassword" /></p>
			</div>
			<div class="field-block button-height">
				<%@ include file="../common/required.jsp" %>
				<label for="username" class="label"><b><spring:message code="label.username" /></b></label>
				<span id="currUser"><%=request.getSession().getAttribute(IApplicationConstants.CURRUSER) %></span>
				<input type="hidden" name="loggedInUserName" id="loggedInUserName" value="">
			</div>
			<div class="field-block button-height passwordContainer">
				<label for="password" class="label"><b><spring:message code="label.createPassword" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password" id="password" value="" class="input validate[required,custom[validatePassword]]">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						<spring:message code="info.password" />
					</span>
				</span>
			</div>
			<div id ="verify_password"  class="field-block button-height">
				<label for="password_again" class="label"><b><spring:message code="table.label.verifyPassword" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="password" name="password_again" id="password_again" value="" class="input validate[required ,equalsPassword[password] ]"> 
			</div>
		</fieldset>
		<c:if test="${not empty parentDetails}">
		<fieldset class="wizard-fieldset fields-list">
			<legend class="legend"><spring:message code="label.profile" /></legend>
			<div class="field-block button-height">
				<%@ include file="../common/required.jsp" %>
				<label for="firstName" class="label"><b><spring:message code="label.firstName" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="firstName" id="firstName" value="${parentDetails.firstName}" class="input validate[required, maxSize[30]]">
			</div>
			<div class="field-block button-height">
				<label for="lastName" class="label"><b><spring:message code="label.lastName" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="lastName" id="lastName" value="${parentDetails.lastName}" class="input validate[required, maxSize[30]]">
			</div>
			<div class="field-block button-height">
				<label for="mail" class="label"><b><spring:message code="label.emailAddress" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="mail" id="mail" value="${parentDetails.mail}" maxlength="100" class="input validate[required,custom[email]]">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width:300px">
						<spring:message code="info.email" />
					</span>
				</span>		
			</div>
			<div class="field-block button-height">
				<label for="verify_mail" class="label"><b><spring:message code="label.verifyEmail" /></b><span class="icon-star icon-size1 red"></span></label>
				<input type="text" name="verify_mail" id="verify_mail" value="${parentDetails.mail}" maxlength="100" class="input validate[required,custom[email] ,equalsEmail[mail]]" >
			</div>
			<div class="field-block button-height">
				<label for="mobile" class="label"><spring:message code="label.contactNumber" /></label>
				<input type="text" name="mobile" id="mobile" value="${parentDetails.mobile}" class="input validate[maxSize[15],custom[phone]]">
				<label for="pseudo-input-2" class="button orange-gradient with-tooltip" title="Click to format phone number(xxx-xxx-xxxx)" id="formatPh"><span class="icon-phone small-margin-right"></span>
				</label>
			</div>
			<div class="field-block button-height addressContainer">
				<span class="label"><spring:message code="label.address" /></span>
				<input type="hidden" name="userCountry" id="userCountry" value="${parentDetails.country}">
				<p class="button-height">
					<span class="input">
						<label for="street" class="blue"><spring:message code="label.street" />:</label>
						<input type="text" name="street" id="street" value="${parentDetails.street}" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="label">&nbsp;</span>
					<span class="input">
						<label for="street" class="blue"><spring:message code="label.city" />:</label>
						<input type="text" name="city" id="city" value="${parentDetails.city}" class="input-unstyled validate[maxSize[50]]">
					</span>
				</p>
				<p class="button-height">
					<span class="input">
						<label for="city" class="blue"><spring:message code="label.state" />:</label>
						<input type="text" name="state" id="state" value="${parentDetails.state}" class="input-unstyled validate[maxSize[40]]">
					</span>
					<span class="label">&nbsp;</span><span class="input">
						<label for="zip_code" class="blue"><spring:message code="label.zip" />:</label>
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
					<legend class="legend"><spring:message code="label.profile" /></legend>
					<div class="field-block button-height">
						<%@ include file="../common/required.jsp" %><br/>
						<label for="firstName" class="label"><b><spring:message code="label.firstName" /></b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="firstName" id="firstName" value="" class="input validate[required,maxSize[30]]">
					</div>
					<div class="field-block button-height">
						<label for="lastName" class="label"><b><spring:message code="label.lastName" /></b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="lastName" id="lastName" value="" class="input validate[required,maxSize[30]]">
					</div>
					<div class="field-block button-height">
						<label for="mail" class="label"><b><spring:message code="label.emailAddress" /></b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="mail" id="mail" value="" maxlength="100" class="input validate[required,custom[email]]">
						<span class="info-spot">
							<span class="icon-info-round"></span>
							<span class="info-bubble" style="width:300px">
								<spring:message code="info.email" />
							</span>
						</span>		
					</div>
					<div class="field-block button-height">
						<label for="verify_mail" class="label"><b><spring:message code="label.verifyEmail" /></b><span class="icon-star icon-size1 red"></span></label>
						<input type="text" name="verify_mail" id="verify_mail" value="" maxlength="100" class="input validate[required,custom[email] ,equalsEmail[mail]]" >
					</div>
					<div class="field-block button-height">
						<label for="mobile" class="label"><spring:message code="label.contactNumber" /></label>
						<input type="text" name="mobile" id="mobile" value="" class="input validate[maxSize[15],custom[phone]]">
					</div>
					<div class="field-block button-height addressContainer">
						<span class="label"><spring:message code="label.address" /></span>
						<input type="hidden" name="userCountry" id="userCountry" value="">						
						<p class="button-height">
							<span class="input">
								<label for="street" class="blue"><spring:message code="label.street" />:</label>
								<input type="text" name="street" id="street" value="" class="input-unstyled validate[maxSize[50]]">
							</span>
						</p>
						<p class="button-height">
							<span class="label">&nbsp;</span>
							<span class="input">
								<label for="street" class="blue"><spring:message code="label.city" />:</label>
								<input type="text" name="city" id="city" value="" class="input-unstyled validate[maxSize[50]]">
							</span>
						</p>
						<p class="button-height">
							<span class="input">
								<label for="city" class="blue"><spring:message code="label.state" />:</label>
								<input type="text" name="state" id="state" value="" class="input-unstyled validate[maxSize[40]]">
							</span>
							<span class="label">&nbsp;</span><span class="input">
								<label for="zip_code" class="blue"><spring:message code="label.zip" />:</label>
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
			<legend class="legend"><spring:message code="label.securityQuestions" /></legend>
			<div class="field-block">
				<%@ include file="../common/required.jsp" %>
				<h4><spring:message code="h4.firstTimeUser" /></h4>
				<p><spring:message code="info.password.recover" /></p>
			</div>
			<%@ include file="../parent/securityQuestion.jsp" %>
			<div class="field-block button-height wizard-controls align-right">
				<button id="saveChangedPassword" type="button" class="button glossy mid-margin-right">
					<span class="button-icon"><span class="icon-tick"></span></span>
					<spring:message code="button.save" />
				</button>
			</div>
		</fieldset>
	</form:form>
<% if(isIe) { %>
	</div>
<%}%>	
<div style="margin-top:130px" ></div>
