package com.ctb.prism.parent.transferobject;


import java.util.List;
import com.ctb.prism.core.transferobject.BaseTO;

public class StudentTO extends BaseTO{


	private static final long serialVersionUID = 1L;
	private String studentName = "";
	private String grade = "";
	private String administration = "";
	private String studentTestNumber = "";
	private List<ParentTO> parentAccount;
	private String structureElement = "";
	private long studentBioId = 0;
	private String invitationcode = "";
	private String activationStatus = "";
	private String orgName = "";
	private long orgId = 0;
	private String icExpirationStatus = "";
	private String expirationDate = "";
	private long totalAvailableClaim = 0;
	private String adminid = "";
	private String studentMode = "";
	private long studentGradeId = 0;
	private String testElementId ="";
	private long bioExists = 0;
	private String schoolName ="";
	private String icLetterUri ="";
	private String icLetterPath ="";
	
	
	public String getTestElementId() {
		return testElementId;
	}
	public void setTestElementId(String testElementId) {
		this.testElementId = testElementId;
	}
	
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getAdministration() {
		return administration;
	}
	public void setAdministration(String administration) {
		this.administration = administration;
	}
	
	public String getStudentTestNumber() {
		return studentTestNumber;
	}
	public void setStudentTestNumber(String studentTestNumber) {
		this.studentTestNumber = studentTestNumber;
	}
	
	public List<ParentTO> getParentAccount() {
		return parentAccount;
	}
	
	public void setParentAccount(List<ParentTO> parentAccount) {
		this.parentAccount = parentAccount;
	}
	
	public String getStructureElement() {
		return structureElement;
	}
	
	public void setStructureElement(String structureElement) {
		this.structureElement = structureElement;
	}
	
	public long getStudentBioId() {
		return studentBioId;
	}
	
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getInvitationcode() {
		return invitationcode;
	}
	
	public void setInvitationcode(String invitationcode) {
		this.invitationcode = invitationcode;
	}
	public String getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public String getIcExpirationStatus() {
		return icExpirationStatus;
	}
	public void setIcExpirationStatus(String icExpirationStatus) {
		this.icExpirationStatus = icExpirationStatus;
	}	
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public long getTotalAvailableClaim() {
		return totalAvailableClaim;
	}
	public void setTotalAvailableClaim(long totalAvailableClaim) {
		this.totalAvailableClaim = totalAvailableClaim;
	}
	public String getStudentMode() {
		return studentMode;
	}
	public void setStudentMode(String studentMode) {
		this.studentMode = studentMode;
	}
	public long getStudentGradeId() {
		return studentGradeId;
	}
	public void setStudentGradeId(long studentGradeId) {
		this.studentGradeId = studentGradeId;
	}
	public long getBioExists() {
		return bioExists;
	}
	public void setBioExists(long bioExists) {
		this.bioExists = bioExists;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getIcLetterUri() {
		return icLetterUri;
	}
	public void setIcLetterUri(String icLetterUri) {
		this.icLetterUri = icLetterUri;
	}
	public String getIcLetterPath() {
		return icLetterPath;
	}
	public void setIcLetterPath(String icLetterPath) {
		this.icLetterPath = icLetterPath;
	}
}
