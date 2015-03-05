<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
			
								<select id="educationCenterDropDown" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list auto-open data-tooltip-options="{"classes":["black-gradient","glossy"]}">
									<c:forEach var="educationCenter" items="${userList}" >
										<option value="${educationCenter.id}">${educationCenter.eduCenterCode}</option>
									</c:forEach>
								</select>
