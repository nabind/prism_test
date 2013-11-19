package com.ctb.prism.core.transferobject;

public class UsabilityTO extends BaseTO  {

	/**
	 * TO to transfered usability report data of the dashboard report
	 */
	private static final long serialVersionUID = 1L;
	
	private String reportUrl;
	private String reportId;
	private String reportName;
	private String username;
	private String currentOrg;
	
	
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCurrentOrg() {
		return currentOrg;
	}
	public void setCurrentOrg(String currentOrg) {
		this.currentOrg = currentOrg;
	}
	
}
