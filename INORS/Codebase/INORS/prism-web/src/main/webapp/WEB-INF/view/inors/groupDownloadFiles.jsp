<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<hgroup id="main-title" class="thin">
<p>This section includes files for the Bulk Candidate Report and Student Data File. The File Type column in the section below will indicate IC when the file contains Invitation Code Letters. Other file types will be blank.</p>
<p class="big-message">
	<span class="orange big-message-icon icon-speech"></span>
	Please note: The length of time to create your file depends upon the volume of requests made by all INORS users. Thanks for your patience!</p>
<p class="red">File will be deleted automatically after expiration date.</p>
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
											<th scope="col" width="12%">Request File Name</th>
											<th scope="col" width="24%">Created Date</th>
											<th scope="col" width="24%">Expiry Date</th>
											<th scope="col" width="15%">Job Status</th>
											<th scope="col" width="12%">File Size</th>
											<th scope="col" width="13%">Actions</th>
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
		    											<c:when test="${group.requestType=='GDF'}">
		    												<span class="info-spot">
																<span class="icon-info-round"></span>
																<span class="info-bubble" style="width: 350px;">
																	<%-- ${group.jobLog} --%>
																	Generated File Name: ${group.filePath}<br />
																	Date of File Generation Request: ${group.createdDateTime}<br />
																	Test Administration: <br />
																	Test Program: <br />
																	Corp/Diocese: <br />
																	School: <br />
																	Grage: <br />
																	Class: <br /><br /><br />
																	1 Student(s) have been selected.<br /><br /><br />
																	File Type: <br />
																	Request Type: 
																</span>
															</span>
		    											</c:when>
		    											<c:otherwise>
															 <span class=" title="">&nbsp;&nbsp;&nbsp;&nbsp;</span>
		      											</c:otherwise>
		                                            </c:choose>
		                                            <c:choose>
		    											<c:when test="${group.jobStatus=='ER'}">
		    												<span class="icon-cross-round icon-size2 red" title="Error"></span>
		    											</c:when>
		    											<c:when test="${group.jobStatus=='CO'}">
		                                                	<a jobId="${group.jobId}" fileName="${group.requestFilename}" filePath="${group.filePath}" class="icon-download icon-size2 with-tooltip confirm download-GroupFiles" title="Download File" style="cursor: pointer;"></a>
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
