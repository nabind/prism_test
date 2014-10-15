package com.ctb.prism.parent.transferobject;

import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * version 1.1
 */
public class ParentTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	private long userId; 
	private String successCode;
	private String invitationCode;
	private String userName;
	private String displayName;
	private String firstName;
	private String lastName;
	private String password;
	private String mail;
	private String mobile;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private List<StudentTO> studentToList;
	private List<QuestionTO> questionToList;
	private String lastLoginAttempt;
	private String status;
	private long orgId;
	private String orgName;
	private boolean isFirstTimeUser;
	private long totalAvailableCalim;
	private long totalAttemptedCalim;
	private String icExpirationStatus;
	private String icActivationStatus;
	private long isAlreadyClaimed = 0;
	private String fullName = "";
	private String contractName = "";
	private String salt = "";
	private boolean ldapFlag;
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public List<StudentTO> getStudentToList() {
		return studentToList;
	}
	public void setStudentToList(List<StudentTO> studentToList) {
		this.studentToList = studentToList;
	}
	public List<QuestionTO> getQuestionToList() {
		return questionToList;
	}
	public void setQuestionToList(List<QuestionTO> questionToList) {
		this.questionToList = questionToList;
	}	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getLastLoginAttempt() {
		return lastLoginAttempt;
	}
	public void setLastLoginAttempt(String lastLoginAttempt) {
		this.lastLoginAttempt = lastLoginAttempt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public boolean isFirstTimeUser() {
		return isFirstTimeUser;
	}
	public void setFirstTimeUser(boolean isFirstTimeUser) {
		this.isFirstTimeUser = isFirstTimeUser;
	}
	public long getTotalAvailableCalim() {
		return totalAvailableCalim;
	}
	public void setTotalAvailableCalim(long totalAvailableCalim) {
		this.totalAvailableCalim = totalAvailableCalim;
	}
	public long getTotalAttemptedCalim() {
		return totalAttemptedCalim;
	}
	public void setTotalAttemptedCalim(long totalAttemptedCalim) {
		this.totalAttemptedCalim = totalAttemptedCalim;
	}
	public String getIcExpirationStatus() {
		return icExpirationStatus;
	}
	public void setIcExpirationStatus(String icExpirationStatus) {
		this.icExpirationStatus = icExpirationStatus;
	}
	public String getIcActivationStatus() {
		return icActivationStatus;
	}
	public void setIcActivationStatus(String icActivationStatus) {
		this.icActivationStatus = icActivationStatus;
	}
	public long getIsAlreadyClaimed() {
		return isAlreadyClaimed;
	}
	public void setIsAlreadyClaimed(long isAlreadyClaimed) {
		this.isAlreadyClaimed = isAlreadyClaimed;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public boolean isLdapFlag() {
		return ldapFlag;
	}
	public void setLdapFlag(boolean ldapFlag) {
		this.ldapFlag = ldapFlag;
	}
}

