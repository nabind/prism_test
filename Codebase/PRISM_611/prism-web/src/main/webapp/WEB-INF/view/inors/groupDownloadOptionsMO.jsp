<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:message var="combinedPdf" key="group.downloads.combined" />
<fmt:message var="separatePdf" key="group.downloads.separate" />
		<div id="sorting-advanced_wrapper" class="dataTables_wrapper" role="grid" style="margin-top: 10px; margin-bottom: 15px;">
			<table aria-describedby="sorting-advanced_info" class="table responsive-table responsive-table-on dataTable" id="studentTableGD">
				<thead>
					<tr role="row">
						<th aria-label="" style="width: 13px;" colspan="1" rowspan="1" role="columnheader" class="sorting_disabled" scope="col">
							<img id="checkAllImg" src="themes/acsi/img/empty.bmp" /><input type="hidden" id="checkAllVal" value="0" /><br />
						</th>
						<th aria-label="Text: activate to sort column ascending" style="width: 350px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="msg.thead.groupDownload.student" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 150px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="msg.thead.groupDownload.school" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 130px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="msg.thead.groupDownload.grade" /></th>
						<th aria-label="Text: activate to sort column ascending" style="width: 120px;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting" scope="col"><spring:message code="msg.thead.groupDownload.pdf" /></th>
					</tr>
				</thead>
				<tbody aria-relevant="all" aria-live="polite" role="alert" id="studentListGD">
					<c:forEach var="student" items="${studentList}">
						<tr id="">
						    <td scope="row" class="checkbox-cell  sorting_1 vertical-center"><input name="checked[]" id="check-student-${student.id}" value="0" type="checkbox" /></td>
						    <td class="vertical-center">${student.name}</td>
						    <td class="vertical-center">${student.school}</td>
						    <td class="vertical-center">${student.grade}</td>
						    <td aria-label="Text: activate to sort column ascending" style="width: 25px; text-align: center; cursor: default;" colspan="1" rowspan="1" aria-controls="sorting-advanced" tabindex="0" role="columnheader" class="sorting vertical-center" scope="col">
						    	<span class="button-group compact">
						    		<a class="button compact icon-download blue glossy with-tooltip" href="#nogo" title="Individual Student Report" onClick="downloadMapIsr('${student.id}')"></a>
								</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<p class="success-message message small-margin-bottom green-gradient" style="display:none"><spring:message code="msg.success.fileDownload" /></p>
		<p class="error-message message small-margin-bottom red-gradient" style="display:none"><spring:message code="msg.error.groupDownload" /></p>
		<div class="columns accordion with-padding" id="nameMailDiv">
			<p class="inline-label">
				<label for="fileName" class="label" style="width: 345px !important; float: left;"><spring:message code="msg.label.groupDownload.fileName" /> </label>
				<input name="fileName" type="text" value="${fileName}" id="fileName" class="input validate[required, custom[fileNameGD, maxSize[50]]" maxlength="50" style="width:200px;">
				<span class="info-spot">
					<span class="icon-info-round"></span>
					<span class="info-bubble" style="width: 123px;">
						<spring:message code="msg.label.groupDownload.span" />
					</span>
				</span>
			</p>
			<p class="inline-label">
				<label for="email" class="label" style="width: 345px !important; float: left;"><spring:message code="msg.label.groupDownload.email" /> </label>
				<input name="email" type="text" value="${email}" id="email" class="input validate[required,custom[email]]" maxlength="100" style="width:200px;">
			</p>
		</div>
		<div class="columns accordion with-padding" style="margin-bottom:0">
			<c:if test="${not empty actionMap[separatePdf]}">
			<a href="#nogo" class="downloadBulkPdf button" id="downloadSeparatePdfsMAP" style="margin-left: 0px; float: left;">
				<span class="button-icon icon-download blue-gradient report-btn"></span>
				<spring:message code="msg.label.groupDownload.downloadSeparatePdfsGD" />
			</a>
			</c:if>
			<c:if test="${not empty actionMap[combinedPdf]}">
			<a href="#nogo" class="downloadBulkPdf button" id="downloadCombinedPdfsMAP" style="margin-left: 10px; float: right;">
				<span class="button-icon icon-download blue-gradient report-btn"></span>
				<spring:message code="msg.label.groupDownload.downloadCombinedPdfsGD" />
			</a>
			</c:if>
			<input type="hidden" name="buttonGD" id="buttonGD" />
			<input type="hidden" name="startIndex" id="startIndex" />
			<input type="hidden" name="endIndex" id="endIndex" />
			<input type="hidden" name="length" id="length" />
		</div>