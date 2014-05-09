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
	private String students;
	private String userId;
	private String userName;
	private String adminId;
	private String customerId;
	private String orgNodeLevel;
	private String extractStartDate;
	
	/**
	 * @return the button
	 */
	public String getButton() {
		return button;
	}

	/**
	 * @param button
	 *            the button to set
	 */
	public void setButton(String button) {
		this.button = button;
	}

	/**
	 * @return the testAdministrationVal
	 */
	public String getTestAdministrationVal() {
		return testAdministrationVal;
	}

	/**
	 * @param testAdministrationVal
	 *            the testAdministrationVal to set
	 */
	public void setTestAdministrationVal(String testAdministrationVal) {
		this.testAdministrationVal = testAdministrationVal;
	}

	/**
	 * @return the testAdministrationText
	 */
	public String getTestAdministrationText() {
		return testAdministrationText;
	}

	/**
	 * @param testAdministrationText
	 *            the testAdministrationText to set
	 */
	public void setTestAdministrationText(String testAdministrationText) {
		this.testAdministrationText = testAdministrationText;
	}

	/**
	 * @return the testProgram
	 */
	public String getTestProgram() {
		return testProgram;
	}

	/**
	 * @param testProgram
	 *            the testProgram to set
	 */
	public void setTestProgram(String testProgram) {
		this.testProgram = testProgram;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the students
	 */
	public String getStudents() {
		return students;
	}

	/**
	 * @param students
	 *            the students to set
	 */
	public void setStudents(String students) {
		this.students = students;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the adminId
	 */
	public String getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId
	 *            the adminId to set
	 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return
	 */
	public String getOrgNodeLevel() {
		return orgNodeLevel;
	}

	/**
	 * @param orgNodeLevel
	 */
	public void setOrgNodeLevel(String orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}

	/**
	 * @return the extractStartDate
	 */
	public String getExtractStartDate() {
		return extractStartDate;
	}

	/**
	 * @param extractStartDate
	 *            the extractStartDate to set
	 */
	public void setExtractStartDate(String extractStartDate) {
		this.extractStartDate = extractStartDate;
	}
}
