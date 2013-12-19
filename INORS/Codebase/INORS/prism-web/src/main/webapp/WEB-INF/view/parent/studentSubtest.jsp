<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<div class="margin-bottom-medium">
	<c:if test="${not empty childDataMap}">
		<textarea id="taContent" style="display:none;">
			${childDataMap.studentOverviewMessage}
		</textarea>
		<div id="somHeader" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			<h2>Overview of Action Plan for ${childDataMap.studentName}</h2>		
		</div>
		<div id="studentOverviewMessage" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">			
		</div>
		<div id="subtestDiv" class="left-column big-message blue-gradient" 
			style="color: #fff; height: auto; width:22%" >
			<dl>
				<c:forEach var="subtestTO"
					items="${childDataMap.studentSubtest}"
					varStatus="loopSubtestTO">
						<dd style="min-height: 50px;">
							- <a href="javascript:void(0)" 
								style="color: #fff; font-weight: bold"
								id="subtest-${loopSubtestTO.count}" 
								subtestId="${subtestTO.value}">
									${subtestTO.name}
							</a>
						</dd>
				</c:forEach>
			</dl>
		</div>
	</c:if>
</div>
