<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>

<div class="margin-bottom-medium">
		<div id="standardHeader" class="wrapped relative white-gradient thin"
			style="height: auto; text-align: justify">	
			<h1>Overview of Action Plan for ${studentName}</h1>		
		</div>
</div>


<div>	
<table>
		<tr>
			   <td>	
					<div id="standardDiv" class="left-column big-message blue-gradient" 
						style="color: #fff; height: auto; width:22%" >
						<dl>
							<c:forEach var="loopstudentStandartList"
								items="${articleTypeDetailsList.standard}"
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
