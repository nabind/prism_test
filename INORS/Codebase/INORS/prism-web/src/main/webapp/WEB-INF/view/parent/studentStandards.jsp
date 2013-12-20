<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	<c:if test="${studentName}==0}">
		<div id="standardHeader" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			<h2>Overview of Action Plan</h2>		
		</div>
	</c:if>
	<c:if test="${studentName}==1}">
		<div id="standardHeader" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			<h2>Overview of Action Plan for ${studentName}</h2>		
		</div>
	</c:if>
</div>


<div>	
<table>
		<tr>
			   <td>	
					<div id="standardDiv" class="left-column big-message blue-gradient" 
						style="color: #fff; height: auto; width:22%" >
						<dl>
							<c:forEach var="loopstudentStandartList"
								items="${studentStandartList.standard}"
								varStatus="loopstudentStandartList">
									<dd style="min-height: 50px;">
										
										
										
									</dd>
							</c:forEach>
						</dl>
					</div>
				</td>
				 <td>	
					<div id="indicatorDiv" class="left-column big-message blue-gradient" 
						style="color: #fff; height: auto; width:22%" >
						<dl>
							<c:forEach var="loopstudentStandartList"
								items="${studentStandartList.indicators}"
								varStatus="loopstudentStandartList">
									<dd style="min-height: 50px;">
										
										
										
									</dd>
							</c:forEach>
						</dl>
					</div>
				</td>
		</tr>
</table>
</div>
