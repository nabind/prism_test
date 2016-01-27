package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MIcClaims {
	private String Student_id;
	private String ClaimDate;
	private String IsActive;
	
	public String getStudent_id() {
		return Student_id;
	}
	public void setStudent_id(String student_id) {
		Student_id = student_id;
	}
	public String getClaimDate() {
		return ClaimDate;
	}
	public void setClaimDate(String claimDate) {
		ClaimDate = claimDate;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
}
