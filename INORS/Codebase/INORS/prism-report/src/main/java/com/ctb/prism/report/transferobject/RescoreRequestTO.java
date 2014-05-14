/**
 * 
 */
package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;

/**
 * @author Joy
 * 
 */
public class RescoreRequestTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private String button;
	private String testAdministrationVal;
	private String testAdministrationText;
	private String testProgram;
	private String district;
	private String school;
	private String grade;
	private String studentFullName = "";
	private long userId = 0;
	private String userName = "";
	private long adminId = 0;
	private long customerId = 0;
	private long orgNodeId= 0;
	private String parentRescoreDate = "";
	private long studentBioId = 0;
	private String requestedDate = "";
	private long subtestId = 0;
	private long sessionId = 0;
	private String performanceLevel = "";
	private long itemsetId = 0;
	private String isRequested = "";
	
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getTestAdministrationVal() {
		return testAdministrationVal;
	}
	public void setTestAdministrationVal(String testAdministrationVal) {
		this.testAdministrationVal = testAdministrationVal;
	}
	public String getTestAdministrationText() {
		return testAdministrationText;
	}
	public void setTestAdministrationText(String testAdministrationText) {
		this.testAdministrationText = testAdministrationText;
	}
	public String getTestProgram() {
		return testProgram;
	}
	public void setTestProgram(String testProgram) {
		this.testProgram = testProgram;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
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
	public String getStudentFullName() {
		return studentFullName;
	}
	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public String getParentRescoreDate() {
		return parentRescoreDate;
	}
	public void setParentRescoreDate(String parentRescoreDate) {
		this.parentRescoreDate = parentRescoreDate;
	}
	public long getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	public long getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(long subtestId) {
		this.subtestId = subtestId;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public String getPerformanceLevel() {
		return performanceLevel;
	}
	public void setPerformanceLevel(String performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
	public long getItemsetId() {
		return itemsetId;
	}
	public void setItemsetId(long itemsetId) {
		this.itemsetId = itemsetId;
	}
	public String getIsRequested() {
		return isRequested;
	}
	public void setIsRequested(String isRequested) {
		this.isRequested = isRequested;
	}
}
