<c:forEach var="rescoreItemTO"
	items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
	<div class="item-div-normal-${rescoreItemTO.studentBioId}">
		<c:choose>
			<c:when test="${rescoreItemTO.requestedDate == '-1'}">
				<small class="item-tag tag align-row grey-bg">
					${rescoreItemTO.itemNumber}-${rescoreItemTO.itemPart}-${rescoreItemTO.itemScore}-${rescoreItemTO.pointPossible}
				</small>
				<br>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${rescoreItemTO.isRequested=='N'}">
						<a class="item-link-dnp align-row" action="submitRescoreRequest"
							rrfId="${rescoreItemTO.rrfId}"
							id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.rrfId}"
							href="#nogo"> <small class="item-tag tag">
								${rescoreItemTO.itemNumber}-${rescoreItemTO.itemPart}-${rescoreItemTO.itemScore}-${rescoreItemTO.pointPossible}
						</small><br>
						</a>
					</c:when>
					<c:when test="${rescoreItemTO.isRequested=='Y'}">
						<a class="item-link-dnp align-row" action="submitRescoreRequest"
							rrfId="${rescoreItemTO.rrfId}"
							id="item_${rescoreItemTO.studentBioId}_${rescoreSubtestTO.subtestId}_${rescoreSessionTO.sessionId}_${rescoreItemTO.rrfId}"
							href="#nogo"> <small class="item-tag tag red-bg">
								${rescoreItemTO.itemNumber}-${rescoreItemTO.itemPart}-${rescoreItemTO.itemScore}-${rescoreItemTO.pointPossible}
						</small><br>
						</a>
					</c:when>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="item-div-inact-${rescoreItemTO.studentBioId}"
		style="display: none;">
		<c:if test="${rescoreItemTO.itemNumber != 0}">
			<small class="item-tag tag align-row grey-bg">
				${rescoreItemTO.itemNumber}-${rescoreItemTO.itemPart}-${rescoreItemTO.itemScore}-${rescoreItemTO.pointPossible}
			</small>
		</c:if>
	</div>
</c:forEach>
