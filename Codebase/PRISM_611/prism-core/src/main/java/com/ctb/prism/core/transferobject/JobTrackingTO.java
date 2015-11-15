package com.ctb.prism.core.transferobject;


/**
 * Transfer object holds details of a report. Report name, report URL, the roles who can access the report etc.
 */

public class JobTrackingTO extends BaseTO {

	private static final long serialVersionUID = 1L;

	private long jobId;
	private String jobName;
	private String userId;
	private String extractStartdate;
	private String extractEnddate;
	private String extractCategory;
	private String extractFiletype;
	private String requestType;
	private String requestSummary;
	private String requestDetails;
	private String requestFilename;
	private String requestEmail;
	private String jobLog;
	private String jobStatus;
	private long number;
	private String customerId;
	private String adminId;
	private String createdDateTime;
	private String updatedDateTime;
	private String otherRequestparams;
	private String fileSize;
	private String userName;
	private String filePath;
	private long  orgLevel;
	private String s3Key;
	private String contractName;
	
	private String orgCode;
	private String uuid;
	private String studentBioId;
	
	public String getS3Key() {
		return s3Key;
	}
	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(String studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getOtherRequestparams() {
		return otherRequestparams;
	}
	public void setOtherRequestparams(String otherRequestparams) {
		this.otherRequestparams = otherRequestparams;
	}
	private boolean success;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getExtractStartdate() {
		return extractStartdate;
	}
	public void setExtractStartdate(String extractStartdate) {
		this.extractStartdate = extractStartdate;
	}
	public String getExtractEnddate() {
		return extractEnddate;
	}
	public void setExtractEnddate(String extractEnddate) {
		this.extractEnddate = extractEnddate;
	}
	public String getExtractCategory() {
		return extractCategory;
	}
	public void setExtractCategory(String extractCategory) {
		this.extractCategory = extractCategory;
	}
	public String getExtractFiletype() {
		return extractFiletype;
	}
	public void setExtractFiletype(String extractFiletype) {
		this.extractFiletype = extractFiletype;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestSummary() {
		return requestSummary;
	}
	public void setRequestSummary(String requestSummary) {
		this.requestSummary = requestSummary;
	}
	public String getRequestDetails() {
		return requestDetails;
	}
	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}
	public String getRequestFilename() {
		return requestFilename;
	}
	public void setRequestFilename(String requestFilename) {
		this.requestFilename = requestFilename;
	}
	public String getRequestEmail() {
		return requestEmail;
	}
	public void setRequestEmail(String requestEmail) {
		this.requestEmail = requestEmail;
	}
	public String getJobLog() {
		if(jobLog != null && jobLog.length() > 4000) return jobLog.substring(0, 3999);
		return jobLog;
	}
	public void setJobLog(String jobLog) {
		this.jobLog = jobLog;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerid) {
		this.customerId = customerid;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(long orgLevel) {
		this.orgLevel = orgLevel;
	}

}
