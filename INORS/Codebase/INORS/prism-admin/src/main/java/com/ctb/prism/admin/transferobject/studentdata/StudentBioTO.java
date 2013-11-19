package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Student_Bio")
public class StudentBioTO {

	@XStreamAsAttribute
	@XStreamAlias("IsDataChange")
	private boolean dataChanged = false;

	@XStreamAsAttribute
	@XStreamAlias("OAS_Stnt_ID")
	private String oasStudentId;

	@XStreamAsAttribute
	@XStreamAlias("Last_Name")
	private String lastName;

	@XStreamAsAttribute
	@XStreamAlias("First_Name")
	private String firstName;

	@XStreamAsAttribute
	@XStreamAlias("Mdle_Initial")
	private String middleInit;

	@XStreamAsAttribute
	@XStreamAlias("Gender")
	private String gender;

	@XStreamAsAttribute
	@XStreamAlias("Grade")
	private String grade;

	@XStreamAsAttribute
	@XStreamAlias("Examinee_ID")
	private String examineeId;

	@XStreamAsAttribute
	@XStreamAlias("Birth_Date")
	private String birthDate;

	@XStreamAsAttribute
	@XStreamAlias("Chrnlgcl_Age")
	private String chrnlgclAge;

	public boolean isDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	public String getOasStudentId() {
		return oasStudentId;
	}

	public void setOasStudentId(String oasStudentId) {
		this.oasStudentId = oasStudentId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInit() {
		return middleInit;
	}

	public void setMiddleInit(String middleInit) {
		this.middleInit = middleInit;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getExamineeId() {
		return examineeId;
	}

	public void setExamineeId(String examineeId) {
		this.examineeId = examineeId;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getChrnlgclAge() {
		return chrnlgclAge;
	}

	public void setChrnlgclAge(String chrnlgclAge) {
		this.chrnlgclAge = chrnlgclAge;
	}

}
