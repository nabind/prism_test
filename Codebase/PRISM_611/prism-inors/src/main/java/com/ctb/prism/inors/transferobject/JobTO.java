package com.ctb.prism.inors.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;
/**
 * This class is the base Transfer Object. Other Transfer Objects should
 * extend this class
 *
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class JobTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	
	private String jobid, pdfFileName, fileLocation, querySheetFile, userId, 
					jobDetails, status, percentageDone, requestDate, 
					completionDate, fileSize, email, testAdmin, customerId, gradeId, reportUrl;
	
	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTestAdmin() {
		return testAdmin;
	}

	public void setTestAdmin(String testAdmin) {
		this.testAdmin = testAdmin;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getQuerySheetFile() {
		return querySheetFile;
	}

	public void setQuerySheetFile(String querySheetFile) {
		this.querySheetFile = querySheetFile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(String jobDetails) {
		this.jobDetails = jobDetails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPercentageDone() {
		return percentageDone;
	}

	public void setPercentageDone(String percentageDone) {
		this.percentageDone = percentageDone;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
