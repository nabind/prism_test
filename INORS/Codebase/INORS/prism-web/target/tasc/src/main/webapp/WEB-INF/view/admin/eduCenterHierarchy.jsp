<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
			
								<select id="educationCenterDropDown" name="userRole" style="width:200px" class="select multiple-as-single easy-multiple-selection check-list auto-open data-tooltip-options="{"classes":["black-gradient","glossy"]}">
									<c:forEach var="educationCenter" items="${userList}" >
										<option value="${educationCenter.id}">${educationCenter.eduCenterCode}</option>
									</c:forEach>
								</select>
								
<%-- <c:if test="${treeSturcture != 'Yes'}"> --%>
<!-- 	<ul id="org_details" class="files-list mini open-on-panel-content org_list"> -->
<%-- 			<c:forEach var="educationCenter" items="${userList}"> --%>
<!-- 				<li id=${educationCenter.id} class="with-right-arrow grey-arrow tenantHier"> -->
<%-- 					<span id=${educationCenter.id} class="icon grey icon-size2 icon-folder"></span>  --%>
<%-- 						<b class="glossy with-tooltip tracked"  data-tooltip-options="{"classes":["black-gradient","glossy"]}" 	id="${educationCenter.id}"  --%>
<%-- 							title = ${} tenantName = "${educationCenter.eduCenterCode}" orgLevel = "${educationCenter.id}" ><s:eval expression="T(com.ctb.prism.core.util.CustomStringUtil).manageString(educationCenter.eduCenterCode)"  /> --%>
<!-- 						</b> -->
<!-- 				</li> -->
<%-- 			</c:forEach> --%>
<!-- 		</ul> -->
<%-- </c:if>	 --%>
<%-- <c:if test="${treeSturcture == 'Yes'}"> --%>
<%-- 		<div id="treeViewForOrg" style="height: 510px;" class="jstree jstree-0 jstree-focused jstree-apple" rootid="${educationCenter.id}_${educationCenter.eduCenterCode}" > --%>
			
<!-- 		</div> -->
<%-- </c:if> --%>

										
		
		