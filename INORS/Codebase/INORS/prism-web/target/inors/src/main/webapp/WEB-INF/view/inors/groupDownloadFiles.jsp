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
											<th scope="col" width="25%">Expiration Date</th>
											<th scope="col" width="15%">File Type</th>
											<th scope="col" width="25%">Generated File</th>
											<th scope="col" width="20%">Size</th>
											<th scope="col" width="15%">Status</th>
											<th scope="col" width="15%">Actions</th>
										</tr>
									</thead>

									<tbody id ="deleteGroupFiles">
										<c:forEach var="group" items="${groupList}">
											<tr id="${group.jobId}">
												<td scope="row">${group.expirationDate}<br>
											    </td>
											    <td scope="row">${group.fileType}<br>
											    </td>
											    <td scope="row"><br>
											    <a href="#">${group.generatedFile}</a> 
											    </td>
											    <td scope="row">${group.size}<br>
											    </td>
											    <td scope="row">${group.download}<br>
											    </td>
											    <td scope="row"><br>
												<a href="#"	jobId="${group.jobId}" class="button icon-trash with-tooltip confirm delete-GroupFiles"
															title="Delete"></a>
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
