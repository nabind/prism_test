package com.ctb.prism.core.transferobject;

public class UsabilityTO extends BaseTO {

	/**
	 * TO to transfered usability report data of the dashboard report
	 */
	private static final long serialVersionUID = 1L;

	private String reportUrl;
	private String userId;
	private String customerId;
	private String ipAddress;
	private Long activityTypeId;
	private boolean loginAs;

	public boolean isLoginAs() {
		return loginAs;
	}

	public void setLoginAs(boolean loginAs) {
		this.loginAs = loginAs;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Long activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	@Override
	public String toString() {
		return "UsabilityTO [reportUrl=" + reportUrl + ", userId=" + userId
				+ ", customerId=" + customerId + ", ipAddress=" + ipAddress
				+ ", activityTypeId=" + activityTypeId + ", loginAs=" + loginAs
				+ "]";
	}

}
