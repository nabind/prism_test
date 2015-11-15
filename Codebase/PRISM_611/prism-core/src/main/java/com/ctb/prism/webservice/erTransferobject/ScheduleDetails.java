package com.ctb.prism.webservice.erTransferobject;

import java.io.Serializable;

public class ScheduleDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477773094826787095L;

	private String scheduleID;
	private String dateScheduled;
	private String timeOfDay;
	private String dateCheckedIn;
	private String contentAreaCode;
	private String contentTestType;
	private String contentTestCode;
	private String barcode;
	private String form;
	private String tASCReadiness;
	private String ECC;
	private String testCenterCode;
	private String testCenterName;
	private String scheduledAtTestCenterCountyParishCode;
	
	public String getScheduledAtTestCenterCountyParishCode() {
		return scheduledAtTestCenterCountyParishCode;
	}
	public void setScheduledAtTestCenterCountyParishCode(
			String scheduledAtTestCenterCountyParishCode) {
		this.scheduledAtTestCenterCountyParishCode = scheduledAtTestCenterCountyParishCode;
	}
	public String getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(String scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getDateScheduled() {
		return dateScheduled;
	}
	public void setDateScheduled(String dateScheduled) {
		this.dateScheduled = dateScheduled;
	}
	public String getTimeOfDay() {
		return timeOfDay;
	}
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	public String getDateCheckedIn() {
		return dateCheckedIn;
	}
	public void setDateCheckedIn(String dateCheckedIn) {
		this.dateCheckedIn = dateCheckedIn;
	}
	public String getContentAreaCode() {
		return contentAreaCode;
	}
	public void setContentAreaCode(String contentAreaCode) {
		this.contentAreaCode = contentAreaCode;
	}
	public String getContentTestType() {
		return contentTestType;
	}
	public void setContentTestType(String contentTestType) {
		this.contentTestType = contentTestType;
	}
	public String getContentTestCode() {
		return contentTestCode;
	}
	public void setContentTestCode(String contentTestCode) {
		this.contentTestCode = contentTestCode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String gettASCReadiness() {
		return tASCReadiness;
	}
	public void settASCReadiness(String tASCReadiness) {
		this.tASCReadiness = tASCReadiness;
	}
	public String getECC() {
		return ECC;
	}
	public void setECC(String eCC) {
		ECC = eCC;
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
	
	
}
