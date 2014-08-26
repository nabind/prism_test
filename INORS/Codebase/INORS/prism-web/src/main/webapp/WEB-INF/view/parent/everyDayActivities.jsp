<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<div class="margin-bottom-medium">

		<div id="everyDayActivityGradeListId" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			
			<h3>Everyday Activities: Choose a grade</h3><br>
            
            <p>Select one of the grades below to see additional Everyday Activities you can do with your child.</p><br>
			
			<c:forEach var="loopGradeList" items="${everyDayActivityGradeList}">
	              	<a href="getEveryDayActivitiesDetails.do?gradeId=${everyDayActivityGradeList.gradeId}&gradeName=${everyDayActivityGradeList.gradeName}" >
	              	everyDayActivityGradeList.gradeName</a>
			</c:forEach>
			
		</div>
		
</div>
