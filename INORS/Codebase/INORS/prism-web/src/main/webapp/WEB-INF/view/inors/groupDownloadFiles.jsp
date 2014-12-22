<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript"/></noscript>

<hgroup id="main-title" class="thin">
<c:if test="${not empty gdfMessages['GDF Header Message']}">${gdfMessages['GDF Header Message']}</c:if>
<c:if test="${not empty gdfMessages['GDF Notice']}">${gdfMessages['GDF Notice']}</c:if>

<fmt:message var="groupDownloadsRefresh" key="group.downloads.refresh" />
<c:if test="${not empty actionMap[groupDownloadsRefresh]}">
<a href="javascript:location.reload();" class="button with-tooltip tooltip-right" title="Click to get the current status.">
	<span class="button-icon"><span class="icon-refresh"></span></span>
	<spring:message code="button.refresh"/>
</a>
</c:if>

<hr/>
</hgroup>
<style>
	.label {width: 160px !important}
	.button.full-width {text-align: center;width: 70%}
	div.scrollingHotSpotLeft, div.scrollingHotSpotRight {display: none}
</style>
<div class="" style="background-color: #FFF">
	<div class="side-tabs1 same-height margin-bottom" id="manageReportParentTab">
		<div class="tabs-content">
			<div class="panel-load-target with-padding margin10 padding-none height-mid">
							<div class="report-container1 tabs-content height-mid manage-report-container">
								<div><table class="simple-table responsive-table">
									<thead>
										<tr>
											<th scope="col" width="30%"><spring:message code="thead.groupDownloadFiles.fileName"/></th>
											<th scope="col" width="20%"><spring:message code="thead.groupDownloadFiles.createdDate"/></th>
											<th scope="col" width="20%"><spring:message code="thead.groupDownloadFiles.expDate"/></th>
											<th scope="col" width="12%"><spring:message code="thead.groupDownloadFiles.jobStatus"/></th>
											<th scope="col" width="10%"><spring:message code="thead.groupDownloadFiles.fileSize"/></th>
											<th scope="col" width="8%"><spring:message code="thead.groupDownloadFiles.actions"/></th>
										</tr>
									</thead>
									<tbody id ="deleteGroupFiles">
										<c:forEach var="group" items="${groupList}">
											<tr id="${group.jobId}">
											    <td scope="row">
											    	<a href="#"	 jobId="${group.jobId}" class="view-requestdetails" title="View Request Detail">${group.displayFilename}</a>
											    </td>
												<td scope="row">${group.createdDateTime}<br>
											    </td>
												<td scope="row">${group.updatedDateTime}<br>
											    </td>
											    <td scope="row">
		                                            <c:choose>
		    											<c:when test="${group.jobStatus=='ER'}">
		    												<span class="button icon-cross-round icon-size2 red with-tooltip" title="Error in file generation"></span>
		    											</c:when>
		    											<c:when test="${group.jobStatus=='NA'}">
		    												<span class="button icon-forbidden icon-size2 orange with-tooltip" title="No files available for the selected criteria"></span>
		    											</c:when>
		    											<c:when test="${group.jobStatus=='CO'}">
		    												<fmt:message var="groupDownloadsDownload" key="group.downloads.download" />
		    												<c:if test="${not empty actionMap[groupDownloadsDownload]}">
		                                                	<a href="downloadGroupDownloadFiles.do?filePath=${group.filePath}&jobId=${group.jobId}&requestType=${group.requestType}&orgLevel=${group.orgLevel}" class="blue-gradient button icon-download icon-size2 with-tooltip confirm" title="Download File" style="cursor: pointer;"></a>
		                                                	</c:if>
		    											</c:when>
														<c:otherwise>
															 <span class="loader working" title="In Progress"></span>
		      											</c:otherwise>
		                                            </c:choose>
	                                            </td>
	                                           <%--  <td scope="row">${group.fileSize}<br> --%>
	                                            <td>
	                                           		 <c:choose>
	                                           		 	<c:when test="${group.jobStatus=='CO'}">
	                                           		 		<fmt:message var="groupDownloadsFileSize" key="group.downloads.fileSize" />
	                                           		 		<c:if test="${not empty actionMap[groupDownloadsFileSize]}">
				                                            <span class="button-group compact"><a jobId="${group.jobId}" id="count_${group.jobId}" 
				                                            	fileName="${group.requestFilename}" filePath="${group.filePath}" href="#nogo" 
				                                            	class="button green-gradient with-tooltip view-FileSize" title="View file size" 
				                                            	style="cursor: pointer;">Size</a></span>
				                                            </c:if>
			                                            </c:when>
			                                            <c:when test="${group.jobStatus=='ER'}">
			                                            	<span class="icon-cross-round icon-size2 red" title="Error"></span>
			                                            </c:when>
			                                            <c:otherwise>
												 			<span class="icon-hourglass icon-size2" title="In Progress"></span>
      												   </c:otherwise>	
	                                            	</c:choose>
	                                            </td>
	                                            <fmt:message var="groupDownloadsDelete" key="group.downloads.delete"  />
	                                            <td><c:if test="${not empty actionMap[groupDownloadsDelete]}"><a href="#"	jobId="${group.jobId}" class="button icon-trash with-tooltip confirm delete-GroupFiles" title="Delete"></a></c:if></td>
											</tr>
										</c:forEach>
									</tbody>
								</table></div>
							</div>
						</div>
					</div>
				</div>
				<div class="viewRequestDetailDivId" id="viewRequestDetailDivId" style="display:none">
						<form:form id="viewRequestDetail" method="post">
							<br>
							<p class="button-height inline-label">
							    <label class="label" for="requestSummary"><Strong><spring:message code="label.groupDownloadFiles.summary"/></Strong></label><br>
							    <p align="left">
								<textarea type="text" name="requestSummary"  id="requestSummary" align="left"  style="width: 500px; height: 250px;" class="textarea full-width wrapped relative white-gradient"  disabled="disabled"></textarea>
								</p>
							</p>
						</form:form>
				</div>
				<div id="requestDetailsContainerGD" style="display:none">
					<div id="requestDetailsContentGD"></div>
				</div>
	</div>
