<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<hgroup id="main-title" class="thin h1-title">
<h1>Manage Students</h1>
</hgroup>

<!--<div class="with-padding">-->
<spring:theme code="theme.name" var="themeName"/>
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
				
				<fmt:message var="manageStudents_search" key="manage.students.search"  />
				<c:if test="${not empty actionMap[manageStudents_search]}">
					<span class="input search-input">
						<input type="text" name="searchStudent" id="searchStudent" class="input-unstyled with-tooltip" title="Search Student by <br/>Last Name OR First Name" placeholder="Search">
						<a href="javascript:void(0)" class="button icon-search compact" id="search_icon" param="search_icon_student"></a>
					</span>
				</c:if>
			</div>
		
			<div class="panel-load-target with-padding margin10 height-mid padding-none">
				
				<fmt:message var="manageStudents_more" key="manage.students.more"  />
				<c:if test="${not empty actionMap[manageStudents_more]}">
					<div class="pagination panel-control margin-bottom-small rounded-border">
						<a href="#nogo" id="moreStudent" class="page_next paginate button compact icon-forward grey-gradient glossy with-tooltip" title="Display more students">More</a>
					</div>
				</c:if>
				
				
				<div id="studentTable"
					class="report-container tabs-content padding-small"
					style="height: 450px">
					<div id="last_msg_loader" height="140" style="position:relative;left:0px;z-index:1"></div>	
					<table class="simple-table responsive-table" id="report-list">
						<input type="text"  style="display:none"  name="lastStudentId" id="lastStudentId" value="" />
						<c:choose>
							<c:when test="${themeName == 'tasc'}">
								<thead>
									<tr class="abc">
										<th scope="col" width="20%">Student Name</th>
										<th scope="col" width="15%">Grade</th>
										<th scope="col" width="25%">${schoolOrgLevel}</th>
									</tr>
								</thead>
								<tbody id="student_details">
									<tr class="abc">
										<th scope="col" width="20%"></th>
										<th scope="col" width="15%"></th>
										<th scope="col" width="25%"></th>
									</tr>
								</tbody>
							</c:when>
							<c:otherwise>
								<thead>
									<tr class="abc">
										<th scope="col" width="25%">Student Name</th>
										<th scope="col" width="13%">Grade</th>
										<th scope="col" width="20%">Parent User ID</th>													
										<th scope="col" width="22%">Class Name</th>
										
										<fmt:message var="MANAGE_STUDENTS_ASSESSMENT" key="manage.students.assessment"  />
										<input type="hidden" id="MANAGE_STUDENTS_ASSESSMENT" value="${actionMap[MANAGE_STUDENTS_ASSESSMENT]}" />
										
										<c:if test="${not empty actionMap[MANAGE_STUDENTS_ASSESSMENT]}">
											<th scope="col" width="20%" class="">Actions</th>
										</c:if>
										
									</tr>
								</thead>
								<tbody id="student_details">
									<tr class="abc">
										<th scope="col" width="25%"></th>
										<th scope="col" width="13%"></th>
										<th scope="col" width="20%"></th>
										<th scope="col" width="22%"></th>
										
										<c:if test="${not empty actionMap[MANAGE_STUDENTS_ASSESSMENT]}">
											<th scope="col" width="20%" class=""></th>
										</c:if>
										
									</tr>
								</tbody>
							</c:otherwise>
						</c:choose>
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