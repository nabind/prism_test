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
					<p><spring:message code="p.profile.1" /></p>
					<ul class="bullet-list">
						<li><spring:message code="li.profile.1" /></li>
						<li><spring:message code="li.profile.2" /></li>
						<li><spring:message code="li.profile.3" /></li>
					</ul>
				</div>
			</div>
			<div class="right-column">
				<p id="mandatoryValidation" class="message red-gradient red-color display-none">
						<span class="message-icon icon-warning with-text bold"><spring:message code="msg.allRequired" /></span>
				</p>
				<div class="standard-tabs margin-bottom" id="add-tabs">
					<ul class="tabs myaccount">
						<li class="active"><a href="#tab-1"><spring:message code="label.basicInformation" /></a></li>
						<li><a href="#tab-2" id="tabContactDetails" ><spring:message code="label.contactDetails" /></a></li>
						<li><a href="#tab-3"><spring:message code="label.securityQuestions" /></a></li>
					</ul>
					<div id="manageProfile" class="tabs-content">
						<div id="tab-1" class="with-padding margin-top margin-bottom-medium">
							<div class="field-block button-height" style="margin-left:0px; margin-right:0px">
								<label for="username" class="label"><b><spring:message code="label.username" /></b></label>
								${parentAccountDetail.userName}
								<input type="hidden" name="username" id="username" value="${parentAccountDetail.userName}" class="input">
								<!--UserName-->
							</div>
							<div class="field-block button-height" style="margin-left:0px; margin-right:0px">
								<label for="displayName" class="label"><b><spring:message code="table.label.displayName" /></b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="displayName" id="displayName" value="${parentAccountDetail.displayName}" class="input validate[required, minSize[3], maxSize[10]]">
							</div>
							<div class="field-block button-height">
								<label for="password" class="label"><b><spring:message code="label.changePassword" /></b></label>
								<input type="password" name="password" id="password" value="" class="input validate[minSize[8], maxSize[15], custom[validatePassword]]" autocomplete="off">
								<span class="info-spot">
									<span class="icon-info-round"></span>
									<span class="info-bubble" style="width:250px">
										<spring:message code="info.password" />
									</span>
								</span>
							</div>
							<div id ="verify_password"  class="field-block button-height">
								<label for="password_again" class="label"><b><spring:message code="table.label.verifyPassword" /></b></label>
								<input type="password" name="password_again" id="password_again" value="" class="input validate[equalsPassword[password] ]"> <!--funcCall[checkPwd]  Password -->
							</div>
							<div class="field-block button-height">
								<label for="firstName" class="label"><b><spring:message code="label.firstName" /></b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="firstName" id="firstName" value="${parentAccountDetail.firstName}" class="input validate[required,maxSize[30]]">
							</div>
							<div class="field-block button-height">
								<label for="lastName" class="label"><b><spring:message code="label.lastName" /></b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="lastName" id="lastName" value="${parentAccountDetail.lastName}" class="input validate[required,maxSize[30]]">
							</div>
						</div>
						<div id="tab-2" class="with-padding margin-top margin-bottom-medium">
							<div class="field-block button-height">
								<label for="verify_mail" class="label"><b><spring:message code="label.emailAddress" /></b><span class="icon-star icon-size1 red"></span></label>
								<input type="text" name="verify_mail" id="verify_mail" value="${parentAccountDetail.mail}" maxlength="100" class="input validate[required, custom[email]]" >
								<span class="info-spot">
									<span class="icon-info-round"></span>
									<span class="info-bubble" style="width:250px">
										<spring:message code="info.email" />
									</span>
								</span>	
							</div>
							<div class="field-block button-height">
								<label for="mobile" class="label"><spring:message code="label.contactNumber" /></label>
								<input type="text" name="mobile" id="mobile" value="${parentAccountDetail.mobile}" class="input validate[maxSize[15],custom[phone]]">
							</div>
							<div class="field-block button-height addressContainer" style="min-height:213px">
								<span class="label"><spring:message code="label.address" /></span>
								<input type="hidden" name="userCountry" id="userCountry" value="${parentAccountDetail.country}">
								<p class="button-height">
									<span class="input">
										<label for="street" class="blue"><spring:message code="label.street" />:</label>
										<input type="text" name="street" id="street" class="input-unstyled validate[maxSize[50]]"  value="<c:if test='${parentAccountDetail.street != null && not empty parentAccountDetail.street }'>${parentAccountDetail.street}</c:if>" >
									</span>
								</p>
								<p class="button-height">
								 <span class="label">&nbsp;</span>
									<span class="input">
										<label for="street" class="blue"><spring:message code="label.city" />:</label>
										<input type="text" name="city" id="city" class="input-unstyled validate[maxSize[50]]"  value="<c:if test='${parentAccountDetail.city != null && not empty parentAccountDetail.city }'>${parentAccountDetail.city}</c:if>" >
									</span>
								</p>
								<p class="button-height">
									<span class="input">
										<label for="city" class="blue"><spring:message code="label.state" />:</label>
										<input type="text" name="state" id="state" class="input-unstyled validate[maxSize[40]]" value="<c:if test='${parentAccountDetail.state != null && not empty parentAccountDetail.state }'>${parentAccountDetail.state}</c:if>">
									</span>
									<span class="label">&nbsp;</span><span class="input">
										<label for="zip_code" class="blue"><spring:message code="label.zip" />:</label>
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
						<spring:message code="button.cancel" />
					</a>
					<a href="#nogo" class="button margin-left glossy with-tooltip save-Profile" title="Save all changes" >
						<span class="button-icon blue-gradient manage-btn"><span class="icon-tick"></span></span>
						<spring:message code="button.save" />
					</a>
				</div>
				<%@ include file="../common/required.jsp" %>
				<div style="clear:both;line-height:0px;height:0px;">
				</div>
			</div>
		</div>
	</div>
</form:form>
