/**
 * 
 */
package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author 796763
 * 
 */
public class GroupDownloadTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private String button;
	private String testAdministrationVal;
	private String testAdministrationText;
	private String testProgram;
	private String district;
	private String school;
	private String klass;
	private String grade;
	private String students;
	private String groupFile;
	private String collationHierarchy;
	private String fileName;
	private String email;

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
	 * @return the klass
	 */
	public String getKlass() {
		return klass;
	}

	/**
	 * @param klass
	 *            the klass to set
	 */
	public void setKlass(String klass) {
		this.klass = klass;
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
	 * @return the groupFile
	 */
	public String getGroupFile() {
		return groupFile;
	}

	/**
	 * @param groupFile
	 *            the groupFile to set
	 */
	public void setGroupFile(String groupFile) {
		this.groupFile = groupFile;
	}

	/**
	 * @return the collationHierarchy
	 */
	public String getCollationHierarchy() {
		return collationHierarchy;
	}

	/**
	 * @param collationHierarchy
	 *            the collationHierarchy to set
	 */
	public void setCollationHierarchy(String collationHierarchy) {
		this.collationHierarchy = collationHierarchy;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("button=");
		buf.append(button);
		buf.append("\n");
		buf.append("testAdministrationVal=");
		buf.append(testAdministrationVal);
		buf.append("\n");
		buf.append("testAdministrationText=");
		buf.append(testAdministrationText);
		buf.append("\n");
		buf.append("testProgram=");
		buf.append(testProgram);
		buf.append("\n");
		buf.append("district=");
		buf.append(district);
		buf.append("\n");
		buf.append("school=");
		buf.append(school);
		buf.append("\n");
		buf.append("klass=");
		buf.append(klass);
		buf.append("\n");
		buf.append("grade=");
		buf.append(grade);
		buf.append("\n");
		buf.append("students=");
		buf.append(students);
		buf.append("\n");
		buf.append("groupFile=");
		buf.append(groupFile);
		buf.append("\n");
		buf.append("collationHierarchy=");
		buf.append(collationHierarchy);
		buf.append("\n");
		buf.append("fileName=");
		buf.append(fileName);
		buf.append("\n");
		buf.append("email=");
		buf.append(email);
		return buf.toString();
	}
}
