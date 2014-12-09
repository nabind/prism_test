package com.vaannila.TO;

import java.io.Serializable;

public class SearchProcess implements Serializable {

	private long processid;
	private String structElement;
	private String orgLevel;
	private String caseCount;
	private String dataStatus;
	private String pdfStatus;
	private String targetStatus;
	private String targerEmailStatus;
	private String processStatus;
	private String processLog;
	private String sourceFileName;
	private String createdDate;
	private String updatedDate;
	private String elementName;
	private String customarCode;
	private String processedDateFrom = "";
	private String processedDateTo = "";
	private String uuid = "";
	private String recordId = "";
	private String lastName = "";
	private String exceptionCode = "";
	private String subjectCa = "";
	private String sourceSystem = "";
	
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
	
	
	
}
