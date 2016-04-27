package com.vaannila.TO;

import java.io.Serializable;

public class TASCProcessTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId = "";
	private String processId = "";
	private String fileName;
	private String sourceSystem = "";
	private String erValidation;
	private String hierValidation;
	private String bioValidation;
	private String demoValidation;
	private String contentValidation;
	private String objValidation;
	private String itemValidation;
	private String wkfPartitionName;
	private String processLog;
	private String dateTimestamp;
	private String timeMili;
	private String ppCount;
	private String olCount;
	private String coCount;
	private String erCount;
	private String overallStatus;
	private String testElementId = "";
	private String erValidationError;
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
	private String district;
	private String grade;
	private String subtest;
	private String caseCount;
	private String dateFrom;
	private String dateTo;
	private String state;
	private String nc;
	private String ss;
	private String hse;
	private String status;
	private String customerCode;
	private String studentBioId;
	private String comments;
	private String isActive;
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(String studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getSs() {
		return ss;
	}
	public void setSs(String ss) {
		this.ss = ss;
	}
	public String getHse() {
		return hse;
	}
	public void setHse(String hse) {
		this.hse = hse;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getDistrict() {
		return district==null ? "" : district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getGrade() {
		return grade == null ? "" : grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubtest() {
		return subtest == null ? "" : subtest;
	}
	public void setSubtest(String subtest) {
		this.subtest = subtest;
	}
	public String getCaseCount() {
		return caseCount == null ? "NA" : caseCount;
	}
	public void setCaseCount(String caseCount) {
		this.caseCount = caseCount;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTestElementId() {
		return testElementId;
	}
	public void setTestElementId(String testElementId) {
		this.testElementId = testElementId;
	}
	public String getErValidationError() {
		return erValidationError;
	}
	public void setErValidationError(String erValidationError) {
		this.erValidationError = erValidationError;
	}
	public String getCoCount() {
		return coCount;
	}
	public void setCoCount(String coCount) {
		this.coCount = coCount;
	}
	public String getErCount() {
		return erCount;
	}
	public void setErCount(String erCount) {
		this.erCount = erCount;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getPpCount() {
		return ppCount;
	}
	public void setPpCount(String ppCount) {
		this.ppCount = ppCount;
	}
	public String getOlCount() {
		return olCount;
	}
	public void setOlCount(String olCount) {
		this.olCount = olCount;
	}
	public String getTimeMili() {
		return timeMili;
	}
	public void setTimeMili(String timeMili) {
		this.timeMili = timeMili;
	}
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
	public String getHierValidation() {
		if(hierValidation != null && "ER".equals(hierValidation)) return "<span style='color: red'>"+hierValidation+"</span>";
		return hierValidation;
	}
	public void setHierValidation(String hierValidation) {
		this.hierValidation = hierValidation;
	}
	public String getBioValidation() {
		if(bioValidation != null && "ER".equals(bioValidation)) return "<span style='color: red'>"+bioValidation+"</span>";
		return bioValidation;
	}
	public void setBioValidation(String bioValidation) {
		this.bioValidation = bioValidation;
	}
	public String getDemoValidation() {
		if(demoValidation != null && "ER".equals(demoValidation)) return "<span style='color: red'>"+demoValidation+"</span>";
		return demoValidation;
	}
	public void setDemoValidation(String demoValidation) {
		this.demoValidation = demoValidation;
	}
	public String getContentValidation() {
		if(contentValidation != null && "ER".equals(contentValidation)) return "<span style='color: red'>"+contentValidation+"</span>";
		return contentValidation;
	}
	public void setContentValidation(String contentValidation) {
		this.contentValidation = contentValidation;
	}
	public String getObjValidation() {
		if(objValidation != null && "ER".equals(objValidation)) return "<span style='color: red'>"+objValidation+"</span>";
		return objValidation;
	}
	public void setObjValidation(String objValidation) {
		this.objValidation = objValidation;
	}
	public String getItemValidation() {
		if(itemValidation != null && "ER".equals(itemValidation)) return "<span style='color: red'>"+itemValidation+"</span>";
		return itemValidation;
	}
	public void setItemValidation(String itemValidation) {
		this.itemValidation = itemValidation;
	}
	public String getWkfPartitionName() {
		return wkfPartitionName;
	}
	public void setWkfPartitionName(String wkfPartitionName) {
		this.wkfPartitionName = wkfPartitionName;
	}
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public String getDateTimestamp() {
		return dateTimestamp;
	}
	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
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
	public String getErSsHistId() {
		return erSsHistId;
	}
	public void setErSsHistId(String erSsHistId) {
		this.erSsHistId = erSsHistId;
	}
	public String getErExcdId() {
		return erExcdId;
	}
	public void setErExcdId(String erExcdId) {
		this.erExcdId = erExcdId;
	}
	
}
