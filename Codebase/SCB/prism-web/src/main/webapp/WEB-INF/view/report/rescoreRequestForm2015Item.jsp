<%@ taglib prefix="ftld" uri="http://www.prism.com/customTlds"%>

<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}">
	<div class="item-div-normal-${studentBioId}">
		<c:choose>
			<c:when test="${rescoreItemTO.requestedDate == '-1'}">
				<big class="new-row item-tag message itemBox itemBoxGrey compact align-center grey-gradient" >
					${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
				</big>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${rescoreItemTO.isRequested=='N'}">
						<c:choose>
							<c:when test="${rescoreItemTO.itemScore == 'A'}">
								<big class="new-row item-tag message itemBox itemBoxGrey compact align-center grey-gradient with-tooltip" title="Omitted Item" data-tooltip-options='{"position":"right"}'>
									${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
								</big>
							</c:when>
							<c:when test="${ftld:isNumeric(rescoreItemTO.itemScore) && rescoreItemTO.itemScore==rescoreItemTO.pointPossible}">
								<big class="new-row item-tag message itemBox itemBoxGrey compact align-center grey-gradient with-tooltip" title="Max score achieved" data-tooltip-options='{"position":"right"}'>
									${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
								</big>
							</c:when>
							<c:otherwise>
								<a class="item-link-2015 align-row" 
								action="submitRescoreRequest" 
								rrfId="${rescoreItemTO.rrfId}" 
								studentBioId="${rescoreItemTO.studentBioId}" 
								id="item_${rescoreItemTO.rrfId}"
								href="#nogo">
									<big class="new-row item-tag message itemBox compact align-center blue-gradient" >
										${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
									</big>
								</a>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${rescoreItemTO.isRequested=='Y'}">
						<a class="item-link-2015 align-row" 
							action="submitRescoreRequest" 
							rrfId="${rescoreItemTO.rrfId}"
							studentBioId="${rescoreItemTO.studentBioId}"  
							id="item_${rescoreItemTO.rrfId}"
							href="#nogo">
								<big class="new-row item-tag message itemBox compact align-center red-gradient" >
									${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
								</big>
						</a>
					</c:when>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="item-div-act-${studentBioId}" style="display: none;">
		<c:choose>
			<c:when test="${rescoreItemTO.isRequested=='N'}">
				<c:if test="${rescoreItemTO.itemScore == 'A'}">
					<big class="new-row item-tag message itemBox itemBoxGrey compact align-center grey-gradient with-tooltip" title="Omitted Item" data-tooltip-options='{"position":"right"}'>
						${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
					</big>
				</c:if>
				<c:if test="${rescoreItemTO.itemScore != 'A'}">
				<a class="item-link-2015 align-row" 
					action="submitRescoreRequest" 
					rrfId="${rescoreItemTO.rrfId}"
					studentBioId="${rescoreItemTO.studentBioId}"  
					id="item_${rescoreItemTO.rrfId}"
					href="#nogo">
						<big class="new-row item-tag message itemBox compact align-center blue-gradient" >
							${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
						</big>
				</a>
				</c:if>
			</c:when>
			<c:when test="${rescoreItemTO.isRequested=='Y'}">
				<a class="item-link-2015 align-row" 
					action="submitRescoreRequest" 
					rrfId="${rescoreItemTO.rrfId}" 
					studentBioId="${rescoreItemTO.studentBioId}"  
					id="item_${rescoreItemTO.rrfId}"
					href="#nogo">
						<big class="new-row item-tag message itemBox compact align-center red-gradient" >
							${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
						</big>
				</a>
			</c:when>
		</c:choose>
	</div>
	<div class="item-div-inact-${studentBioId}" style="display: none;">
		<big class="new-row item-tag message itemBox compact align-center grey-gradient" >
			${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
		</big>
	</div>
</c:forEach>