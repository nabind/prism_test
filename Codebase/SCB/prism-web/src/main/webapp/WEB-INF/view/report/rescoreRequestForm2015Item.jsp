<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}">
	<div class="item-div-normal-${studentBioId}">
		<c:choose>
			<c:when test="${rescoreItemTO.requestedDate == '-1'}">
				<big class="new-row item-tag button compact grey-gradient" style="margin-bottom: 2px;width: 125px;">
					${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
				</big>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${rescoreItemTO.isRequested=='N'}">
						<a class="item-link-2015 align-row" 
							action="submitRescoreRequest" 
							rrfId="${rescoreItemTO.rrfId}" 
							studentBioId="${rescoreItemTO.studentBioId}" 
							id="item_${rescoreItemTO.rrfId}"
							href="#nogo">
								<big class="new-row item-tag button compact blue-gradient" style="margin-bottom: 2px;width: 125px;">
									${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
								</big>
						</a>
						<br/>
					</c:when>
					<c:when test="${rescoreItemTO.isRequested=='Y'}">
						<a class="item-link-2015 align-row" 
							action="submitRescoreRequest" 
							rrfId="${rescoreItemTO.rrfId}"
							studentBioId="${rescoreItemTO.studentBioId}"  
							id="item_${rescoreItemTO.rrfId}"
							href="#nogo">
								<big class="new-row item-tag button compact red-gradient" style="margin-bottom: 2px;width: 125px;">
									${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
								</big>
						</a>
						<br/>
					</c:when>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="item-div-act-${studentBioId}" style="display: none;">
		<c:choose>
			<c:when test="${rescoreItemTO.isRequested=='N'}">
				<a class="item-link-2015 align-row" 
					action="submitRescoreRequest" 
					rrfId="${rescoreItemTO.rrfId}"
					studentBioId="${rescoreItemTO.studentBioId}"  
					id="item_${rescoreItemTO.rrfId}"
					href="#nogo">
						<big class="new-row item-tag button compact blue-gradient" style="margin-bottom: 2px;width: 125px;">
							${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
						</big>
				</a>
				<br/>
			</c:when>
			<c:when test="${rescoreItemTO.isRequested=='Y'}">
				<a class="item-link-2015 align-row" 
					action="submitRescoreRequest" 
					rrfId="${rescoreItemTO.rrfId}" 
					studentBioId="${rescoreItemTO.studentBioId}"  
					id="item_${rescoreItemTO.rrfId}"
					href="#nogo">
						<big class="new-row item-tag button compact red-gradient" style="margin-bottom: 2px;width: 125px;">
							${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
						</big>
				</a>
				<br/>
			</c:when>
		</c:choose>
	</div>
	<div class="item-div-inact-${studentBioId}" style="display: none;">
		<big class="new-row item-tag button compact grey-gradient" style="margin-bottom: 2px;width: 125px;">
			${rescoreItemTO.itemNumber} - ${rescoreItemTO.itemPart} - ${rescoreItemTO.itemScore} - ${rescoreItemTO.pointPossible}
		</big>
	</div>
</c:forEach>									 