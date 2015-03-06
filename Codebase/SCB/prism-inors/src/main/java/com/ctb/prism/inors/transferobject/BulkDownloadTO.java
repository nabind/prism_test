package com.ctb.prism.inors.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class BulkDownloadTO extends BaseTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String testAdministration;
	private String testProgram;
	private String corp;
	private String school;
	private String grade;
	private String orgClass;
	private String fileName;
	private String dateRequested;
	private long studentCount;
	private long schoolCount;
	private long classCount;
	
	private String querysheetFile;
	private String fileLocation;
	
	private String schoolNodes;
	private String classNodes;
	private String studentBioIds;
	
	private String username;
	private String email;
	
	private String groupFile;
	private String collationHierarchy;
	private String selectedNodes;
	
	private boolean istepPlus;
	private String customerId;
	private long jobId;
	private String reportUrl;
	private String status;
	private String fileSize;
	private String percentageDone;
	private String requestedDate;
	private String completedDate;
	private String requestType;
	private String log;
	private String downloadMode;
	
	private boolean dbStatus;
	
	private String startDate;
	private String endDate;
	
	private String[] subtest;
	
	public String[] getSubtest() {
		return subtest;
	}
	public void setSubtest(String[] subtest) {
		this.subtest = subtest;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDownloadMode() {
		return downloadMode;
	}
	public void setDownloadMode(String downloadMode) {
		this.downloadMode = downloadMode;
	}
	public boolean isDbStatus() {
		return dbStatus;
	}
	public void setDbStatus(boolean dbStatus) {
		this.dbStatus = dbStatus;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getPercentageDone() {
		return percentageDone;
	}
	public void setPercentageDone(String percentageDone) {
		this.percentageDone = percentageDone;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	public String getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public boolean isIstepPlus() {
		return istepPlus;
	}
	public void setIstepPlus(boolean istepPlus) {
		this.istepPlus = istepPlus;
	}
	public String getGroupFile() {
		return groupFile;
	}
	public void setGroupFile(String groupFile) {
		this.groupFile = groupFile;
	}
	public String getCollationHierarchy() {
		return collationHierarchy;
	}
	public void setCollationHierarchy(String collationHierarchy) {
		this.collationHierarchy = collationHierarchy;
	}
	public String getSelectedNodes() {
		return selectedNodes;
	}
	public void setSelectedNodes(String selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQuerysheetFile() {
		return querysheetFile;
	}
	public void setQuerysheetFile(String querysheetFile) {
		this.querysheetFile = querysheetFile;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTestAdministration() {
		return testAdministration;
	}
	public void setTestAdministration(String testAdministration) {
		this.testAdministration = testAdministration;
	}
	public String getTestProgram() {
		return testProgram;
	}
	public void setTestProgram(String testProgram) {
		this.testProgram = testProgram;
	}
	public String getCorp() {
		return corp;
	}
	public void setCorp(String corp) {
		this.corp = corp;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getOrgClass() {
		return orgClass;
	}
	public void setOrgClass(String orgClass) {
		this.orgClass = orgClass;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDateRequested() {
		return dateRequested;
	}
	public void setDateRequested(String dateRequested) {
		this.dateRequested = dateRequested;
	}
	public long getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(long studentCount) {
		this.studentCount = studentCount;
	}
	public long getSchoolCount() {
		return schoolCount;
	}
	public void setSchoolCount(long schoolCount) {
		this.schoolCount = schoolCount;
	}
	public long getClassCount() {
		return classCount;
	}
	public void setClassCount(long classCount) {
		this.classCount = classCount;
	}
	public String getSchoolNodes() {
		return schoolNodes;
	}
	public void setSchoolNodes(String schoolNodes) {
		this.schoolNodes = schoolNodes;
	}
	public String getClassNodes() {
		return classNodes;
	}
	public void setClassNodes(String classNodes) {
		this.classNodes = classNodes;
	}
	public String getStudentBioIds() {
		return studentBioIds;
	}
	public void setStudentBioIds(String studentBioIds) {
		this.studentBioIds = studentBioIds;
	}
	
	
}
