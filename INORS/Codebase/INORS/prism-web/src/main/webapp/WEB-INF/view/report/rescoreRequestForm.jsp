<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="content-panel" style="padding-left: 0px; padding-right: 10px; border: none">
	<form:form method="POST" id="rescoreRequestForm" modelAttribute="rescoreRequestForm">
		<p class="success-message message small-margin-bottom green-gradient" style="display: none">Success</p>
		<p class="error-message message small-margin-bottom red-gradient" style="display: none">Error</p>
		<input type="hidden" value="/public/INORS/Report/Report2_files" name="reportUrl">
		<div id="" class="columns accordion with-padding" style="margin-bottom: 0">
			<span id="ctl00_MainPageContent_instructionVideo">
				<a href="https://ctb-mcgraw-hill.webex.com/ctb-mcgraw-hill/ldr.php?AT=pb&SP=MC&rID=67284132&rKey=1df53fff97245310" id="ctl00_MainPageContent_linkUrl" target="_blank" style="color:Blue; font-size: small; font-weight: bolder;">Link to Instruction Video</a>
			</span>
			<span id="">
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
		</div>
	</form:form>
	<input type="hidden" id="q_testAdministrationVal" value="${testAdministrationVal}" />
	<input type="hidden" id="q_testAdministrationText" value="${testAdministrationText}" />
	<input type="hidden" id="q_testProgram" value="${testProgram}" />
	<input type="hidden" id="q_corpDiocese" value="${corpDiocese}" />
	<input type="hidden" id="q_school" value="${school}" />
</div>
