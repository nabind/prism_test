package com.ctb.prism.webservice.transferobject;

public class StudentBioTO {

	private boolean dataChanged = false;
	private String oasStudentId;
	private String lastName;
	private String firstName;
	private String middleInit;
	private String gender;
	private String grade;
	private String examineeId;
	private String birthDate;
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
