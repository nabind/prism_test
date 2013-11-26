<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin">
<h1>Group Download Files</h1>
</hgroup>
<style>
	.label {width: 160px !important}
</style>
<div class="" style="background-color: #FFF">

	<div class="side-tabs same-height margin-bottom" id="manageReportParentTab">
		<div class="tabs-content">
<div class="panel-load-target with-padding margin10 padding-none height-mid">
							<div class="report-container1 tabs-content height-mid manage-report-container">
								<div><table class="simple-table responsive-table">

									<thead>
										<tr>
											<th scope="col" width="20%">Request File Name</th>
											<th scope="col" width="20%">Created Date</th>
											<th scope="col" width="20%">Completed Date</th>
											<th scope="col" width="20%">Request Type</th>
											<th scope="col" width="20%">Job Status</th>
										</tr>
									</thead>

									<tbody id ="deleteGroupFiles">
										<c:forEach var="group" items="${groupList}">
											<tr id="${group.jobId}">
											    <td scope="row">${group.requestFilename}<br>
											    </td>
												<td scope="row">${group.createdDateTime}<br>
											    </td>
												<td scope="row">${group.updatedDateTime}<br>
											    </td>
											    <td scope="row">${group.requestType}<br>
											    <td>
											    <c:choose>
                                                <c:when test="${group.jobStatus=='IP'}">
                                                <span class="loader working"></span>
    											</c:when>
    											<c:when test="${group.jobStatus=='CO'}">
                                                <input type="button" name="downloadbutton" id="downloadbutton" value="Download">
    											</c:when>
												<c:otherwise>
												${group.jobStatus}
      											</c:otherwise>
	                                            </c:choose>
	                                            </td>
											</tr>
										</c:forEach>
									</tbody>
								</table></div>
							</div>
						</div>
					</div>
				</div>
			
	</div>
