<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin">
<p class="big-message">This section includes files for the ISTEP+, IMAST and IREAD-3 Student Reports, and for ISTEP+ only the Image Prints and Invitation Code Letters. The File Type column in the section below will indicate IC when the file contains Invitation Code Letters. Other file types will be blank.
<br/><br/>If the Image Print document (Applied Skills) is not available for a student, please contact your School or Corporation Test Coordinator.<br/>If the ISR (Individual Student Report) is not available for a student, please contact the CTB/Indiana Help Desk.</p>
<p class="red">
	<span class="tag red-gradient">Please Note:</span> The length of time to create your file depends upon the volume of requests made by all INORS users. Thanks for your patience!
</p>
<p class="red">Files will be deleted automatically after the expiration date/time. Click <b>Refresh</b> to get the current job status.</p>
<a href="javascript:location.reload();" class="button with-tooltip tooltip-right" title="Click to get the current status.">
	<span class="button-icon"><span class="icon-refresh"></span></span>
	Refresh
</a>

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
											<th scope="col" width="30%">Request File Name</th>
											<th scope="col" width="20%">Created Date</th>
											<th scope="col" width="20%">Expiry Date</th>
											<th scope="col" width="12%">Job Status</th>
											<th scope="col" width="10%">File Size</th>
											<th scope="col" width="8%">Actions</th>
										</tr>
									</thead>

									<tbody id ="deleteGroupFiles">
										<c:forEach var="group" items="${groupList}">
											<tr id="${group.jobId}">
											    <td scope="row">
											    	<a href="#"	 jobId="${group.jobId}" class="view-requestdetails" title="View Request Detail">${group.requestFilename}</a>
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
		                                                	<a jobId="${group.jobId}" fileName="${group.requestFilename}" filePath="${group.filePath}" class="blue-gradient button icon-download icon-size2 with-tooltip confirm download-GroupFiles" title="Download File" style="cursor: pointer;"></a>
		    											</c:when>
														<c:otherwise>
															 <span class="loader working" title="In Progress"></span>
		      											</c:otherwise>
		                                            </c:choose>
	                                            </td>
	                                            <td scope="row">${group.fileSize}<br>
	                                            <td>
												<a href="#"	jobId="${group.jobId}" class="button icon-trash with-tooltip confirm delete-GroupFiles"
															title="Delete"></a>
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
							    <label class="label" for="requestSummary"><Strong>Request Summary</Strong></label><br>
							    <p align="left">
								<textarea type="text" name="requestSummary"  id="requestSummary" align="left"  style="width: 500px; height: 250px;" class="textarea full-width wrapped relative white-gradient"  disabled="disabled"></textarea>
								</p>
							</p>
						</form:form>
				</div>
			
	</div>
