package com.vaannila.TO;

import java.io.Serializable;

public class StudentDetailsTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String processId = "";
	private String fileName;
	private String sourceSystem = "";
	private String erValidation;
	private String overallStatus;
	private String testElementId = "";
	private String studentName = "";
	private String uuid = "";
	private String subtestName = "";
	private String exceptionCode = "";
	private String erSsHistId = "";
	private String barcode = "";
	private String dateScheduled = "";
	private String stateCode = "";
	private String form = "";
	private String erExcdId = "";
	private String testCenterCode = "";
	private String testCenterName = "";
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getErValidation() {
		return erValidation;
	}
	public void setErValidation(String erValidation) {
		this.erValidation = erValidation;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getTestElementId() {
		return testElementId;
	}
	public void setTestElementId(String testElementId) {
		this.testElementId = testElementId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSubtestName() {
		return subtestName;
	}
	public void setSubtestName(String subtestName) {
		this.subtestName = subtestName;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getErSsHistId() {
		return erSsHistId;
	}
	public void setErSsHistId(String erSsHistId) {
		this.erSsHistId = erSsHistId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getDateScheduled() {
		return dateScheduled;
	}
	public void setDateScheduled(String dateScheduled) {
		this.dateScheduled = dateScheduled;
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
	public String getErExcdId() {
		return erExcdId;
	}
	public void setErExcdId(String erExcdId) {
		this.erExcdId = erExcdId;
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		if("ERESOURCE".equals(sourceSystem)){
			buffer.append("\"").append(erSsHistId).append("\"");
		}else{
			buffer.append("\"").append(processId).append("\"");
		}
		buffer.append(",").append("\"").append(studentName).append("\"")
		.append(",").append("\"").append(uuid).append("\"")
		.append(",").append("\"").append(testElementId).append("\"")
		.append(",").append("\"").append(processId).append("\"")
		.append(",").append("\"").append(exceptionCode).append("\"");
		
		if("CO".equals(overallStatus)) {
			buffer.append(",").append("\"").append("Completed").append("\"");
		}else if("ER".equals(overallStatus) || "IN".equals(overallStatus)) {
			buffer.append(",").append("\"").append("Error").append("\"");
		} else{
			buffer.append(",").append("\"").append("In Progress").append("\"");
		}
		
		buffer.append(",").append("\"").append(barcode).append("\"")
		.append(",").append("\"").append(dateScheduled).append("\"")
		.append(",").append("\"").append(stateCode).append("\"")
		.append(",").append("\"").append(form).append("\"")
		.append(",").append("\"").append(subtestName).append("\"")
		.append(",").append("\"").append(testCenterCode).append("\"")
		.append(",").append("\"").append(testCenterName).append("\"");
		
		return buffer.toString();
	}
	
	
}
