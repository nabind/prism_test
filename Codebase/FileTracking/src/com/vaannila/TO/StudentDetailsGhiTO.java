package com.vaannila.TO;

import java.io.Serializable;

public class StudentDetailsGhiTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String recordId;
	private String fileName;
	private String fileGenDateTime;
	private String OrgIDTP;
	private String drcStudentID;
	private String stateCode;
	private String examineeID;
	private String errCodeErrDesc;
	private String studentName;
	private String dob;
	private String gender;
	private String procesDate;
	private String orgCodePath;
	private String testCenterCode;
	private String testCenterName;
	private String documentID;
	private String scheduleID;
	private String tcaScheduleDate;
	private String imagingID;
	private String lithoCode;
	private String testMode;
	private String testLanguage;
	private String contentName;
	private String form;
	private String dateTestTaken;
	private String barcodeID;
	private String contentScore;
	private String contentTestCode;
	private String scaleScore;
	private String scannedProcessDate;
	private String statusCodeContentArea;
	private String prismProcessStatus;
	private String testEventUpdateDate;
	private String docProcessStatus;
	private String expiredDate;
	private String checkinDate;
	private String fieldTestForm;
	private String scanBatch;
	private String scanStack;
	private String scanSequence;
	
	public String getScanBatch() {
		return scanBatch;
	}
	public void setScanBatch(String scanBatch) {
		this.scanBatch = scanBatch;
	}
	public String getScanStack() {
		return scanStack;
	}
	public void setScanStack(String scanStack) {
		this.scanStack = scanStack;
	}
	public String getScanSequence() {
		return scanSequence;
	}
	public void setScanSequence(String scanSequence) {
		this.scanSequence = scanSequence;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileGenDateTime() {
		return fileGenDateTime;
	}
	public void setFileGenDateTime(String fileGenDateTime) {
		this.fileGenDateTime = fileGenDateTime;
	}
	public String getOrgIDTP() {
		return OrgIDTP;
	}
	public void setOrgIDTP(String orgIDTP) {
		OrgIDTP = orgIDTP;
	}
	public String getDrcStudentID() {
		return drcStudentID;
	}
	public void setDrcStudentID(String drcStudentID) {
		this.drcStudentID = drcStudentID;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getExamineeID() {
		return examineeID;
	}
	public void setExamineeID(String examineeID) {
		this.examineeID = examineeID;
	}
	public String getErrCodeErrDesc() {
		return errCodeErrDesc;
	}
	public void setErrCodeErrDesc(String errCodeErrDesc) {
		this.errCodeErrDesc = errCodeErrDesc;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProcesDate() {
		return procesDate;
	}
	public void setProcesDate(String procesDate) {
		this.procesDate = procesDate;
	}
	public String getOrgCodePath() {
		return orgCodePath;
	}
	public void setOrgCodePath(String orgCodePath) {
		this.orgCodePath = orgCodePath;
	}
	public String getDocumentID() {
		return documentID;
	}
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	public String getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(String scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getTcaScheduleDate() {
		return tcaScheduleDate;
	}
	public void setTcaScheduleDate(String tcaScheduleDate) {
		this.tcaScheduleDate = tcaScheduleDate;
	}
	public String getImagingID() {
		return imagingID;
	}
	public void setImagingID(String imagingID) {
		this.imagingID = imagingID;
	}
	public String getLithoCode() {
		return lithoCode;
	}
	public void setLithoCode(String lithoCode) {
		this.lithoCode = lithoCode;
	}
	public String getTestMode() {
		return testMode;
	}
	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}
	public String getTestLanguage() {
		return testLanguage;
	}
	public void setTestLanguage(String testLanguage) {
		this.testLanguage = testLanguage;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getDateTestTaken() {
		return dateTestTaken;
	}
	public void setDateTestTaken(String dateTestTaken) {
		this.dateTestTaken = dateTestTaken;
	}
	public String getBarcodeID() {
		return barcodeID;
	}
	public void setBarcodeID(String barcodeID) {
		this.barcodeID = barcodeID;
	}
	public String getContentScore() {
		return contentScore;
	}
	public void setContentScore(String contentScore) {
		this.contentScore = contentScore;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getTestCenterCode() {
		return testCenterCode;
	}
	public void setTestCenterCode(String testCenterCode) {
		this.testCenterCode = testCenterCode;
	}
	public String getTestCenterName() {
		return testCenterName;
	}
	public void setTestCenterName(String testCenterName) {
		this.testCenterName = testCenterName;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getScaleScore() {
		return scaleScore;
	}
	public void setScaleScore(String scaleScore) {
		this.scaleScore = scaleScore;
	}
	public String getScannedProcessDate() {
		return scannedProcessDate;
	}
	public void setScannedProcessDate(String scannedProcessDate) {
		this.scannedProcessDate = scannedProcessDate;
	}
	public String getStatusCodeContentArea() {
		return statusCodeContentArea;
	}
	public void setStatusCodeContentArea(String statusCodeContentArea) {
		this.statusCodeContentArea = statusCodeContentArea;
	}
	public String getContentTestCode() {
		return contentTestCode;
	}
	public void setContentTestCode(String contentTestCode) {
		this.contentTestCode = contentTestCode;
	}
	public String getPrismProcessStatus() {
		return prismProcessStatus;
	}
	public void setPrismProcessStatus(String prismProcessStatus) {
		this.prismProcessStatus = prismProcessStatus;
	}	
	public String getTestEventUpdateDate() {
		return testEventUpdateDate;
	}
	public void setTestEventUpdateDate(String testEventUpdateDate) {
		this.testEventUpdateDate = testEventUpdateDate;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(recordId).append("\"")
		.append(",").append("\"").append(stateCode).append("\"")
		.append(",").append("\"").append(testMode).append("\"")
		.append(",").append("\"").append(studentName).append("\"")
		.append(",").append("\"").append(examineeID).append("\"")
		.append(",").append("\"").append(drcStudentID).append("\"")
		.append(",").append("\"").append(prismProcessStatus).append("\"")
		.append(",").append("\"").append(barcodeID).append("\"")
		.append(",").append("\"").append(scheduleID).append("\"")
		.append(",").append("\"").append(tcaScheduleDate).append("\"")
		.append(",").append("\"").append(dateTestTaken).append("\"")
		.append(",").append("\"").append(form).append("\"")
		.append(",").append("\"").append(contentName).append("\"")
		.append(",").append("\"").append(contentTestCode).append("\"")
		.append(",").append("\"").append(testLanguage).append("\"")
		.append(",").append("\"").append(lithoCode).append("\"")
		.append(",").append("\"").append(scaleScore).append("\"")
		.append(",").append("\"").append(contentScore).append("\"")
		.append(",").append("\"").append(statusCodeContentArea).append("\"")
		.append(",").append("\"").append(testCenterCode).append("\"")
		.append(",").append("\"").append(testCenterName).append("\"")
		.append(",").append("\"").append(errCodeErrDesc).append("\"")
		.append(",").append("\"").append(testEventUpdateDate).append("\"")
		.append(",").append("\"").append(scannedProcessDate).append("\"")
		.append(",").append("\"").append(orgCodePath).append("\"")
		.append(",").append("\"").append(procesDate).append("\"")
		.append(",").append("\"").append(documentID).append("\"")
		.append(",").append("\"").append(fileName).append("\"")
		.append(",").append("\"").append(fileGenDateTime).append("\"");
		return buffer.toString();
	}
	public String getDocProcessStatus() {
		return docProcessStatus;
	}
	public void setDocProcessStatus(String docProcessStatus) {
		this.docProcessStatus = docProcessStatus;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getCheckinDate() {
		return checkinDate;
	}
	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}
	public String getFieldTestForm() {
		return fieldTestForm;
	}
	public void setFieldTestForm(String fieldTestForm) {
		this.fieldTestForm = fieldTestForm;
	}
	
}
