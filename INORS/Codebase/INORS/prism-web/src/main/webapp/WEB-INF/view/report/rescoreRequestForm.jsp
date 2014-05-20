<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none">Success</p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none">Error</p>
		<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl">
		<c:if test="${not empty reportMessages}">
			<c:forEach var="reportMessage" items="${reportMessages}">
				<c:if test="${reportMessage.displayFlag=='Y'}">
					<c:if test="${reportMessage.messageType=='DM'}">
						<div class="big-message">
							<%-- <span class="big-message-icon icon-warning with-text color" style="margin-top: -15px;">Important</span> --%>
							<%-- <strong>${ reportMessage.messageName }</strong><br> --%>
							${ reportMessage.message }
						</div>
						<p>&nbsp;</p>
					</c:if>
					<c:if test="${reportMessage.messageType!='DM'}">
						<fieldset class="fieldset">
							<legend class="legend">${ reportMessage.messageName }</legend>
							<p class="inline-label">${ reportMessage.message }</p>
						</fieldset>
					</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		<!-- <div class="columns accordion with-padding big-message">
			<span>Rescore requests will be accepted for the ISTEP+ Spring 2013 test administration from 09-09-2013 to 09-30-2013 for English/Language Arts, Mathematics and Social Studies and from 10-24-2013 to 11-15-2013 for Science.</span>
		</div> -->
		<!-- <div class="columns accordion with-padding">
			<span>Only parents can initiate a rescore request. The school will complete the Rescore Request Form per the parents' instructions. A Parent-Request Date is required in order to have the item(s) rescored. Enter the month and day of the request in the proper column.</span>
		</div> -->
		<!-- <div class="columns accordion with-padding">
			<span id="ctl00_MainPageContent_instructionVideo" class="tag">
				<a href="https://ctb-mcgraw-hill.webex.com/ctb-mcgraw-hill/ldr.php?AT=pb&SP=MC&rID=67284132&rKey=1df53fff97245310" id="ctl00_MainPageContent_linkUrl" target="_blank" style="color:White; font-size: small;">Link to Instruction Video</a>
			</span>
			<br />
			<span>
				<strong>Instructions:</strong>
				<ul>
					<li>Only the students that received DNP in at least one content area are listed in this table.</li>
					<li>If a student received DNP and did not receive full credit, items eligible for rescore are automatically displayed for a specific subject.</li>
					<li>If a student received Pass or Pass+ in a subject, items are not automatically displayed for the subject. Click Pass or Pass+ to display items eligible for rescore in a specific subject. </li>
					<li>Enter the date the parent requested a rescore. Select the item to be rescored by clicking on the item. The item will be highlighted to indicate it has been selected for rescore.</li>
					<li>Deselect an item to be rescored by clicking on the highlighted item. The highlighting will be removed to indicate the item has been deselected.</li>
					<li>By clicking Pass or Pass+ a second time, all items will disappear and be deselected in a specific subject.</li>
					<li>To submit a rescore request for Undetermined students call the CTB/Indiana Help Desk Toll Free at 800-282-1132.</li>
				</ul>
			</span>
		</div> -->
		<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Parent-Rescore Date<br />(MM/DD)</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">E/la</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Math<br />Session 1</th>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF">
					<c:forEach var="rescoreRequestStudentTO" items="${rescoreStudentList}">
						<tr>
						    <td class="vertical-center">${rescoreRequestStudentTO.studentFullName}</td>
						    <td class="vertical-center">
						    	<c:choose>
						    		<c:when test="${rescoreRequestStudentTO.requestedDate =='-1'}">
						    			<input type="text" id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" value="" /> 
						    		</c:when>
						    		<c:otherwise>
						    			<input type="text" id="rescoreDate_${rescoreRequestStudentTO.studentBioId}" value="${rescoreRequestStudentTO.requestedDate}" /> 
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						    
						    <c:forEach var="rescoreSubtestTO" items="${rescoreRequestStudentTO.rescoreSubtestTOList}">
						    	<td class="vertical-center">
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='Pass'}">
						    			<a class="performance-level" 
						    				subtestId="${rescoreSubtestTO.subtestId}"
											href="#nogo">
												${rescoreSubtestTO.performanceLevel}
										</a>
						    		</c:if>
						    		<c:if test="${rescoreSubtestTO.performanceLevel=='DNP'}">
						    			${rescoreSubtestTO.performanceLevel}
						    		</c:if>
						    	</td>
						    	
						    	<c:forEach var="rescoreSessionTO" items="${rescoreSubtestTO.rescoreSessionTOList}">
						    		<td class="vertical-center">
							    		<c:forEach var="rescoreItemTO" items="${rescoreSessionTO.rescoreItemTOList}" varStatus="theCount">
							    			<div class="item-div">
							    				<c:choose>
							    					<c:when test="${rescoreItemTO.requestedDate == '-1'}">
							    						<%-- <div class="item-state_${rescoreSubtestTO.subtestId}"> --%>
															<small class="item-tag tag align-row orange-bg">${rescoreItemTO.itemNumber}</small>
														<!-- </div> -->
								    				</c:when>
								    				<c:otherwise>
								    					<c:choose>
								    						<c:when test="${rescoreItemTO.isRequested=='N'}">
									    						<%-- <div class="item-state_${rescoreSubtestTO.subtestId}"> --%>
									    							<a class="item-link align-row" 
									    								action="submitRescoreRequest" 
																		itemsetId="${rescoreItemTO.itemsetId}"
												    					rrfId="${rescoreItemTO.rrfId}" 
												    					userId="${rescoreItemTO.userId}"
												    					userName="${rescoreItemTO.userName}"
												    					studentBioId="${rescoreItemTO.studentBioId}"
																		href="#nogo" 
																		id="item_${rescoreItemTO.itemsetId}">
																			<small class="item-tag tag">${rescoreItemTO.itemNumber}</small>	
																	</a>
									    						<!-- </div> -->
									    					</c:when>
									    					<c:when test="${rescoreItemTO.isRequested=='Y'}">
									    						<!-- <div class="item-state_${rescoreSubtestTO.subtestId}"> -->
									    							<a class="item-link align-row" 
									    								action="submitRescoreRequest" 
																		itemsetId = "${rescoreItemTO.itemsetId}"
												    					rrfId = "${rescoreItemTO.rrfId}" 
												    					userId = "${rescoreItemTO.userId}"
												    					userName = "${rescoreItemTO.userName}"
												    					studentBioId = "${rescoreItemTO.studentBioId}"
																		href="#nogo" 
																		id="item_${rescoreItemTO.itemsetId}">
																			<small class="item-tag tag red-bg">${rescoreItemTO.itemNumber}</small>
																	</a>		
									    						<!-- </div> -->
									    					</c:when>
								    					</c:choose>		
								    				</c:otherwise>
								    			</c:choose>
							    			</div>
							    		</c:forEach>
						    		</td>
						    	</c:forEach>
						    </c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<p><span>
			UND: Undetermined - No score due to invalid or omitted items. DNP: Did Not Pass.<br />
			*To submit a rescore request for Undetermined students please call the CTB/Indiana Help Desk Toll Free at 800-282-1132.
		</span></p>
		<div class="columns accordion" style="text-align: center; border: none;">
			<a class="button blue-gradient glossy" id="reviewRRF" href="#">Review </a>
		</div>
		<div class="columns accordion with-padding">
			<strong>Instructions</strong>
			<ul>
				<li>To request rescore items for students that received Pass, Pass+ or UND  in all content areas, select student name from the Pass/Pass+/UND dropdown list and click ADD.</li>
				<li>Once the student has been added to the table below, click Pass or Pass+ to display items eligible for rescore in a specific subject.</li>
				<li>Enter the date the parent requested a rescore. Select the item to be rescored by clicking on the item. The item will be highlighted to indicate it has been selected for rescore.</li>
				<li>Deselect an item to be rescored by clicking on the highlighted item. The highlighting will be removed to indicate the item has been deselected.</li>
				<li>By clicking Pass or Pass+ a second time, all items will disappear and be deselected in a specific subject.</li>
				<li>To submit a rescore request for Undetermined students call the CTB/Indiana Help Desk Toll Free at 800-282-1132.</li>
			</ul>
		</div>
		<div class="columns accordion with-padding">
			<p class="button-height">
				Students Pass/Pass+/UND  in all content areas:<br />
				<select id="selectStudentRRF" name="" class="select navy-gradient expandable-list float-left" style="width: 250px;">
					
				</select>
				<a class="button blue-gradient glossy float-left margin-left disabled" id="addRRF" href="#">Add </a>
			</p>
		</div>
		<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableRRF_2">
				<thead>
					<tr role="row">
						<th aria-label="Text: activate to sort column ascending" style="width: 160px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col"></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 230px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Student</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">E/la</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 2</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">E/la<br />Session 3</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 50px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col">Math</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="" scope="col">Math<br />Session 1</th>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListRRF_2">
					<c:forEach var="student" items="${rescoreStudentList}">
						<tr>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						    <td class="vertical-center"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<p><span>
			* To submit a rescore request for Undetermined students please call the CTB/Indiana Help Desk Toll Free at 800-282-1132.<br />
			You do not have to submit this form. This form submits automatically.
		</span></p>
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
</div>
