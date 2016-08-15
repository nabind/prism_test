package com.vaannila.TO;

import java.io.Serializable;

public class SearchProcess implements Serializable, Comparable<SearchProcess>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262460751035212841L;
	private long processid = 0;
	private String structElement = "";
	private String orgLevel = "";
	private String caseCount = "";
	private String dataStatus = "";
	private String pdfStatus = "";
	private String targetStatus = "";
	private String targerEmailStatus = "";
	private String processStatus = "";
	private String processStatusDesc = "";
	private String processLog = "";
	private String sourceFileName = "";
	private String createdDate = "";
	private String updatedDate = "";
	private String elementName = "";
	private String customarCode = "";
	private String processedDateFrom = "";
	private String processedDateTo = "";
	private String uuid = "";
	private String recordId = "";
	private String lastName = "";
	private String exceptionCode = "";
	private String subjectCa = "";
	private String sourceSystem = "";
	private String stateCode = "";
	private String form = "";
	private String testElementId = "";
	private String barcode = "";
	private String processId = "";
	private String sourceSystemDesc = "";
	private String district = "";
	private String subtest = "";
	private String processIdStr = "";
	private String grade = "";
	private long fromRowNum = 0;
	private long toRowNum = 0;
	private long pageNumber = 0;
	private long pageDisplayLength = 0;
	private String searchParam = "";
	private String sortCol = "";
	private String sortDir = "";
	private String mode = "";
	private String imagingId = "";
	private String status;
	private String studentBioId;
	private String subtestId;
	private String DRCStudentId;
	private String DRCDocumentId;
	private String errorDateFrom;
	private String errorDateTo;
	private String trackErrorStatus;
	
	public String getDRCStudentId() {
		return DRCStudentId;
	}
	public void setDRCStudentId(String dRCStudentId) {
		DRCStudentId = dRCStudentId;
	}
	public String getDRCDocumentId() {
		return DRCDocumentId;
	}
	public void setDRCDocumentId(String dRCDocumentId) {
		DRCDocumentId = dRCDocumentId;
	}
	public String getErrorDateFrom() {
		return errorDateFrom;
	}
	public void setErrorDateFrom(String errorDateFrom) {
		this.errorDateFrom = errorDateFrom;
	}
	public String getErrorDateTo() {
		return errorDateTo;
	}
	public void setErrorDateTo(String errorDateTo) {
		this.errorDateTo = errorDateTo;
	}
	public String getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(String studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(String subtestId) {
		this.subtestId = subtestId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getSubtest() {
		return subtest;
	}
	public void setSubtest(String subtest) {
		this.subtest = subtest;
	}
	public String getProcessIdStr() {
		return processIdStr;
	}
	public void setProcessIdStr(String processIdStr) {
		this.processIdStr = processIdStr;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getCustomarCode() {
		return customarCode;
	}
	public void setCustomarCode(String customarCode) {
		this.customarCode = customarCode;
	}
	public long getProcessid() {
		return processid;
	}
	public void setProcessid(long processid) {
		this.processid = processid;
	}
	public String getStructElement() {
		return structElement;
	}
	public void setStructElement(String structElement) {
		this.structElement = structElement;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getCaseCount() {
		return caseCount;
	}
	public void setCaseCount(String caseCount) {
		this.caseCount = caseCount;
	}
	public String getDataStatus() {
		return (dataStatus);
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getPdfStatus() {
		return (pdfStatus);
	}
	public void setPdfStatus(String pdfStatus) {
		this.pdfStatus = pdfStatus;
	}
	public String getTargetStatus() {
		return (targetStatus);
	}
	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}
	public String getTargerEmailStatus() {
		return (targerEmailStatus);
	}
	public void setTargerEmailStatus(String targerEmailStatus) {
		this.targerEmailStatus = targerEmailStatus;
	}
	public String getProcessStatus() {
		return (processStatus);
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getProcessStatusDesc() {
		return processStatusDesc;
	}
	public void setProcessStatusDesc(String processStatusDesc) {
		this.processStatusDesc = processStatusDesc;
	}
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public String getSourceFileName() {
		return sourceFileName;
	}
	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getProcessedDateFrom() {
		return processedDateFrom;
	}
	public void setProcessedDateFrom(String processedDateFrom) {
		this.processedDateFrom = processedDateFrom;
	}
	public String getProcessedDateTo() {
		return processedDateTo;
	}
	public void setProcessedDateTo(String processedDateTo) {
		this.processedDateTo = processedDateTo;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getSubjectCa() {
		return subjectCa;
	}
	public void setSubjectCa(String subjectCa) {
		this.subjectCa = subjectCa;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getTestElementId() {
		return testElementId;
	}
	public void setTestElementId(String testElementId) {
		this.testElementId = testElementId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getSourceSystemDesc() {
		return sourceSystemDesc;
	}
	public void setSourceSystemDesc(String sourceSystemDesc) {
		this.sourceSystemDesc = sourceSystemDesc;
	}
	public long getFromRowNum() {
		this.fromRowNum = ((this.pageNumber - 1) * this.pageDisplayLength) + 1;
		return fromRowNum;
	}
	public void setFromRowNum(long fromRowNum) {
		this.fromRowNum = fromRowNum;
	}
	public long getToRowNum() {
		this.toRowNum = (this.pageNumber * this.pageDisplayLength);
		return toRowNum;
	}
	public void setToRowNum(long toRowNum) {
		this.toRowNum = toRowNum;
	}
	public long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public long getPageDisplayLength() {
		return pageDisplayLength;
	}
	public void setPageDisplayLength(long pageDisplayLength) {
		this.pageDisplayLength = pageDisplayLength;
	}
	
	public int compareTo(SearchProcess searchProcess) {
		int compareFlag = 0;
		if (this.processid != searchProcess.processid){
			compareFlag = 1;
		}
		if(!this.structElement.equals(searchProcess.structElement)){
			compareFlag = 1;
		}
		if(!this.orgLevel.equals(searchProcess.orgLevel)){
			compareFlag = 1;
		}
		if(!this.caseCount.equals(searchProcess.caseCount)){
			compareFlag = 1;
		}
		if(!this.dataStatus.equals(searchProcess.dataStatus)){
			compareFlag = 1;
		}
		if(!this.pdfStatus.equals(searchProcess.pdfStatus)){
			compareFlag = 1;
		}
		if(!this.targetStatus.equals(searchProcess.targetStatus)){
			compareFlag = 1;
		}
		if(!this.targerEmailStatus.equals(searchProcess.targerEmailStatus)){
			compareFlag = 1;
		}
		if(!this.processStatus.equals(searchProcess.processStatus)){
			compareFlag = 1;
		}
		if(!this.processLog.equals(searchProcess.processLog)){
			compareFlag = 1;
		}
		if(!this.sourceFileName.equals(searchProcess.sourceFileName)){
			compareFlag = 1;
		}
		if(!this.createdDate.equals(searchProcess.createdDate)){
			compareFlag = 1;
		}
		if(!this.updatedDate.equals(searchProcess.updatedDate)){
			compareFlag = 1;
		}
		if(!this.elementName.equals(searchProcess.elementName)){
			compareFlag = 1;
		}
		if(!this.customarCode.equals(searchProcess.customarCode)){
			compareFlag = 1;
		}
		if(!this.processedDateFrom.equals(searchProcess.processedDateFrom)){
			compareFlag = 1;
		}
		if(!this.processedDateTo.equals(searchProcess.processedDateTo)){
			compareFlag = 1;
		}
		if(!this.uuid.equals(searchProcess.uuid)){
			compareFlag = 1;
		}
		if(!this.recordId.equals(searchProcess.recordId)){
			compareFlag = 1;
		}
		if(!this.lastName.equals(searchProcess.lastName)){
			compareFlag = 1;
		}
		if(!this.exceptionCode.equals(searchProcess.exceptionCode)){
			compareFlag = 1;
		}
		if(!this.subjectCa.equals(searchProcess.subjectCa)){
			compareFlag = 1;
		}
		if(!this.sourceSystem.equals(searchProcess.sourceSystem)){
			compareFlag = 1;
		}
		if(!this.stateCode.equals(searchProcess.stateCode)){
			compareFlag = 1;
		}
		if(!this.form.equals(searchProcess.form)){
			compareFlag = 1;
		}
		if(!this.testElementId.equals(searchProcess.testElementId)){
			compareFlag = 1;
		}
		if(!this.barcode.equals(searchProcess.barcode)){
			compareFlag = 1;
		}
		if(!this.processId.equals(searchProcess.processId)){
			compareFlag = 1;
		}
		if(!this.sourceSystemDesc.equals(searchProcess.sourceSystemDesc)){
			compareFlag = 1;
		}
		if(!this.district.equals(searchProcess.district)){
			compareFlag = 1;
		}
		if(!this.subtest.equals(searchProcess.subtest)){
			compareFlag = 1;
		}
		if(!this.processIdStr.equals(searchProcess.processIdStr)){
			compareFlag = 1;
		}
		if(!this.grade.equals(searchProcess.grade)){
			compareFlag = 1;
		}
		if(this.fromRowNum != searchProcess.fromRowNum){
			compareFlag = 1;
		}
		if(this.toRowNum != searchProcess.toRowNum){
			compareFlag = 1;
		}
		if(this.pageNumber != searchProcess.pageNumber){
			compareFlag = 1;
		}
		if(this.pageDisplayLength != searchProcess.pageDisplayLength){
			compareFlag = 1;
		}
		return compareFlag;
	}
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	public String getSortCol() {
		return sortCol;
	}
	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}
	public String getSortDir() {
		return sortDir;
	}
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getImagingId() {
		return imagingId;
	}
	public void setImagingId(String imagingId) {
		this.imagingId = imagingId;
	}
	public String getTrackErrorStatus() {
		return trackErrorStatus;
	}
	public void setTrackErrorStatus(String trackErrorStatus) {
		this.trackErrorStatus = trackErrorStatus;
	}
}
