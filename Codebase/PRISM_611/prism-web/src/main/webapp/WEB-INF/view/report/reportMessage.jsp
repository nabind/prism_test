<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
	.switch.tiny {
		right: 216px; top: 20px;
	}
</style>
<c:if test="${not empty serviceMapManageMessage}">
	<div class="with-padding">
		<form:form
			id="editManageMessageForm"
			method="post" action="ajaxJSP/saveManageMessage.htm"
			modelAttribute="manageMessageForm">
			<div class="float-right">
				<a href="javascript:void(0)" id="editManageMessageButtonSave" 
							class="button blue-gradient glossy" reportId="${serviceMapManageMessage.reportId}"><spring:message code="button.save" /></a>
				<c:choose>
	                <c:when test="${serviceMapManageMessage.reportName!='Generic System Configuration'}">			
						<a href="javascript:void(0);" class="button blue-gradient glossy" id="copyMessage"> <spring:message code="button.copyMessage" /> </a>
					</c:when>
				</c:choose>
				<a href="manageReports.do" id="editManageMessageButtonCancel" 
							class="button white-gradient glossy"><spring:message code="button.cancel" /></a>
			</div>
			<div> 
			<c:forEach var="manageMessageTO"
				items="${serviceMapManageMessage.manageMessageTOList}"
				varStatus="loopManageMessageTO">
				<div class="inline-large-label">
					<input type="hidden" 
						name="manageMessageTOList[${loopManageMessageTO.index}].custProdIdHidden" 
						id="custProdIdHidden-${manageMessageTO.reportId}-${loopManageMessageTO.count}"
						class="custProdIdHidden"/>
					<input type="hidden"
						name="manageMessageTOList[${loopManageMessageTO.index}].reportId"
						value="${manageMessageTO.reportId}" />
					<input type="hidden"
						name="manageMessageTOList[${loopManageMessageTO.index}].messageTypeId"
						value="${manageMessageTO.messageTypeId}" />
					<c:if test="${manageMessageTO.activationStatus == 'AC'}">
						<input type="checkbox" title="Activate/Inactivate"
							name="manageMessageTOList[${loopManageMessageTO.index}].activationStatus"
							id="manage-rpt-chkbox-${manageMessageTO.reportId}-${loopManageMessageTO.count}"
							class="switch tiny with-tooltip" checked>
					</c:if>
					<c:if test="${manageMessageTO.activationStatus == 'IN'}">
						<input type="checkbox" title="Activate/Inactivate"
							name="manageMessageTOList[${loopManageMessageTO.index}].activationStatus"
							id="manage-rpt-chkbox-${manageMessageTO.reportId}-${loopManageMessageTO.count}"
							class="switch tiny with-tooltip">
					</c:if>
				</div>
				
				<div>
				<fieldset class="fieldset">
					<legend class="legend" style="padding-left:50px">${manageMessageTO.messageTypeName}</legend>
					<input type="hidden"
						id="hiddenMessageTypeName-${manageMessageTO.reportId}-${loopManageMessageTO.index}"
						name="manageMessageTOList[${loopManageMessageTO.index}].messageTypeName" value="${manageMessageTO.messageTypeName}" />
					<input type="hidden"
						id="hiddenEditor-${manageMessageTO.reportId}-${loopManageMessageTO.index}"
						name="manageMessageTOList[${loopManageMessageTO.index}].message" />
					<div class="new-row">
						<textarea
							id="editors-${manageMessageTO.reportId}-${loopManageMessageTO.index}"
							class="manage-rpt-textarea">${manageMessageTO.message}
						</textarea>
					</div>
				</fieldset>
				</div>
			</c:forEach>
			</div>
		</form:form>
	</div>
</c:if>
