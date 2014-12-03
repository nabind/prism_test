package com.vaannila.TO;

import java.io.Serializable;

public class AdminTO implements Serializable {
	private String adminId;
	private String adminName;
	private String currentAdmin;
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getCurrentAdmin() {
		return currentAdmin;
	}
	public void setCurrentAdmin(String currentAdmin) {
		this.currentAdmin = currentAdmin;
	}
	
}
