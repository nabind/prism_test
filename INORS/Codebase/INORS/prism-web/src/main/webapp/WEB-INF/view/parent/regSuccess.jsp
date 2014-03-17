<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<form:form method="post" action="" class="block wizard same-height top-registration margin-buttom with-padding">

	<h2>Registration successful</h2>
	<p class="big-message">
		<br/><br/>
		<b>Thank you !!</b>
		<br/><br/>
		You have successfully registered with <spring:message code="pnlogin.page.welcome"/> for Parents. <a href="userlogin.do"><small class="tag blue-bg">Click here</small></a> to login using the credential you have chosen. 
		<br/><br/>
	</p>		

</form:form>

<div style="margin-top:130px" ></div>
