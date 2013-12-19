<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<div class="margin-bottom-medium">
	<c:if test="${not empty childDataMap}">
		<textarea id="taContent" style="display:none;">
			${childDataMap.studentOverviewMessage}
		</textarea>
		<div id="studentOverviewMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">			
		</div>
		<div id="subtestDiv">
			<dl>
			<c:forEach var="subtestTO"
				items="${childDataMap.studentSubtest}"
				varStatus="loopSubtestTO">
					<dd>
						<a href="javascript:void(0)" 
							id="subtest-${loopSubtestTO.count}" 
							subtestId="${subtestTO.value}">
								${subtestTO.value}
						</a>
					</dd>
			</c:forEach>
			</dl>
		</div>
	</c:if>
</div>
