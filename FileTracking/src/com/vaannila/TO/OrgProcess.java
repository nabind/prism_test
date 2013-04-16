package com.vaannila.TO;

import java.io.Serializable;

public class OrgProcess implements Serializable {

	private static final long serialVersionUID = 7812968984241228187L;
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
	
	private String loginPDFLoc;
	private String letterPDFLoc;
	
	
	public String getLoginPDFLoc() {
		return loginPDFLoc;
	}
	public void setLoginPDFLoc(String loginPDFLoc) {
		this.loginPDFLoc = loginPDFLoc;
	}
	public String getLetterPDFLoc() {
		return letterPDFLoc;
	}
	public void setLetterPDFLoc(String letterPDFLoc) {
		this.letterPDFLoc = letterPDFLoc;
	}
	public String getElementName() {
		return (elementName == null)? "" : elementName;
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
		return statusConverter(dataStatus);
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getPdfStatus() {
		return statusConverter(pdfStatus);
	}
	public void setPdfStatus(String pdfStatus) {
		this.pdfStatus = pdfStatus;
	}
	public String getTargetStatus() {
		return statusConverter(targetStatus);
	}
	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}
	public String getTargerEmailStatus() {
		return statusConverter(targerEmailStatus);
	}
	public void setTargerEmailStatus(String targerEmailStatus) {
		this.targerEmailStatus = targerEmailStatus;
	}
	public String getProcessStatus() {
		return statusConverter(processStatus);
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
	
	private String statusConverter(String statusCode) {
		if(statusCode == null) return "";
		if(statusCode.equals("ER")) return "Error";
		if(statusCode.equals("SU")) return "Success";
		if(statusCode.equals("IP")) return "In Progress";
		if(statusCode.equals("IN")) return "Inactive";
		if(statusCode.equals("AC")) return "Active";
		if(statusCode.equals("CP")) return "Completed";
		if(statusCode.equals("DE")) return "Deleted";
		if(statusCode.equals("JU")) return "Ready to call Java Util";
		
		return statusCode;
	}
	
}
