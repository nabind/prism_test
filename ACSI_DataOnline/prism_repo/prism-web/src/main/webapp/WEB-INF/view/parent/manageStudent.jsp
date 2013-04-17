<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<hgroup id="main-title" class="thin h1-title">
<h1>Manage Students</h1>
</hgroup>

<!--<div class="with-padding">-->
<div class="" style="background-color: #FFF">
		<input type="hidden" value="<c:if test='${not empty isRedirected}'>${isRedirected}</c:if><c:if test='${empty isRedirected}'></c:if>" id="isRedirectedTree" name="isRedirectedTree">
		<input type="hidden" value="<c:if test='${not empty studentBioId}'>${studentBioId}</c:if><c:if test='${empty studentBioId}'></c:if>" id="redirectedStudentBioId" name="redirectedStudentBioId">
        <input type="hidden" value="<c:if test='${not empty hierarchialOrgIds}'>${hierarchialOrgIds}</c:if><c:if test='${empty hierarchialOrgIds}'></c:if>" id="tempTreeStudent" name="tempTreeStudent">
		<div class="content-panel margin-bottom" style="min-width: 725px;">
		<div class="panel-navigation silver-gradient">

			<%@ include file="../admin/multiAdminYear.jsp" %>

			<div class="panel-load-target">
				<div id="slide_menu_student" class="navigable students">
					
						<%@ include file="../admin/orgHierarchy.jsp" %>
					
				</div>
			</div>			

		</div>

		<div class="panel-content linen">

			<div class="panel-control align-right">
				<div class = "align-left" style="float:left"><b>
				<a href="#nogo" id="showHierarchy" class="button icon-forward with-tooltip" style="display:none" title="Show Hierarchy"></a> <span id="showOrgNameStudent"></span></b></div>
				<span class="input search-input">
					<input type="text" name="searchStudent" id="searchStudent" class="input-unstyled with-tooltip" title="Search Student by <br/>Last Name OR First Name" placeholder="Search">
					<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_student"></a>
				</span>

			</div>
		
			<div
				class="panel-load-target with-padding margin10 height-mid padding-none">
				<div class="pagination panel-control margin-bottom-small rounded-border">
					<a href="#nogo" id="moreStudent" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more students">More</a>
				</div>
				<div id="studentTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
					<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>	
					<table class="simple-table responsive-table" id="report-list">
						<input type="text"  style="display:none"  name="lastStudentId" id="lastStudentId" value="" />
						<thead>
							<tr class="abc">
								<th scope="col" width="20%">Student Name</th>
								<th scope="col" width="15%">Grade</th>
								<!--th scope="col" width="16%">Student Test Number</th-->
								<th scope="col" width="20%">Parent User ID</th>
								<th scope="col" width="25%">School Name</th>
								<th scope="col" width="20%" class="">Actions</th>
							</tr>
						</thead>

						<tbody id="student_details">
							<tr class="abc">
								<th scope="col" width="20%"></th>
								<th scope="col" width="15%"></th>
								<!--th scope="col" width="16%">Student Test Number</th-->
								<th scope="col" width="20%"></th>
								<th scope="col" width="25%"></th>
								<th scope="col" width="20%" class=""></th>
							</tr>
							<%-- <c:forEach var="student" items="${studentList}" varStatus="loopCount" >
									<c:if test="${loopCount.last}">
										<input type="text"  style="display:none"  name="lastStudentId" id="lastStudentId" value="${student.rowIndentifier}|${student.clikedOrgId}" />
									</c:if>
								<tr class="abc" id = "${student.studentBioId}" scrollid ="${student.rowIndentifier}|${student.clikedOrgId}" >
									<th scope="row">${student.studentName}</th>
									<td>${student.grade}</td>
									<!--td>${student.invitationcode}</td-->
									<td>
									<c:forEach var="parentAccount" items="${student.parentAccount}" varStatus="loopCount" >
										${parentAccount.userName}<c:if test="${not loopCount.last}">, </c:if>
									</c:forEach>
									</td>
									<td>${student.orgName}</td>
									<td class="vertical-center">
										<span class="button-group compact"> 
											<a id="${student.studentBioId}"  parentName="${student.studentName}" href="#" class="button with-tooltip view-Assessment" title="View Assessment"><span class="icon-pages icon"></span>Assessment</a>
										</span>
									</td>
								</tr>
							</c:forEach>	 --%>						
						</tbody>

					</table>

				</div>
				
			</div>

		</div>
</div>
	
	
	<div class="margin-top"></div>

	<div id="studentModal" class="display-none">
			<div id ="studentModalContainer" class="six-columns six-columns-tablet twelve-columns-mobile">
			</div>
	</div>

 </div>