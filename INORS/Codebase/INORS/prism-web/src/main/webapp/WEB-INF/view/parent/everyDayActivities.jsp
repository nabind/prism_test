<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
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
