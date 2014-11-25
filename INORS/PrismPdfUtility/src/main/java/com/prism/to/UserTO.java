package com.prism.to;

import java.util.List;
import java.util.Properties;

public class UserTO {

	private List<String> roles;

	private String userId;
	private String jasperUserId;
	private String userName;
	private String fullName;
	private String password;
	private String encPassword;
	private String salt;
	private String orgNodeId;
	private String orgNodeLevel;
	private String orgNodeName;
	private String orgNodeCodePath;
	private String stateName;
	private Integer stateCode;
	private String countyName;
	private Integer countyCode;
	private String testingSiteName;
	private Integer testingSiteCode;
	private boolean adminUser;
	private String studentName;
	private String grade;

	// Istep
	// private Properties istepProperties =
	// PropertyFile.loadProperties("istep.properties");
	private String tenantId;
	private String orgLevel;
	private String email;
	private String adminId;
	private String enabled;

	private String userType = "";

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJasperUserId() {
		return jasperUserId;
	}

	public void setJasperUserId(String jasperUserId) {
		this.jasperUserId = jasperUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncPassword() {
		return encPassword;
	}

	public void setEncPassword(String encPassword) {
		this.encPassword = encPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getOrgNodeId() {
		return orgNodeId;
	}

	public void setOrgNodeId(String orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	public String getOrgNodeLevel() {
		return orgNodeLevel;
	}

	public void setOrgNodeLevel(String orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}

	public String getOrgNodeName() {
		return orgNodeName;
	}

	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}

	public String getOrgNodeCodePath() {
		return orgNodeCodePath;
	}

	public void setOrgNodeCodePath(String orgNodeCodePath) {
		this.orgNodeCodePath = orgNodeCodePath;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getStateCode() {
		return stateCode;
	}

	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public Integer getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	public String getTestingSiteName() {
		return testingSiteName;
	}

	public void setTestingSiteName(String testingSiteName) {
		this.testingSiteName = testingSiteName;
	}

	public Integer getTestingSiteCode() {
		return testingSiteCode;
	}

	public void setTestingSiteCode(Integer testingSiteCode) {
		this.testingSiteCode = testingSiteCode;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType, Properties prop) {
		this.userType = userType;
		// Istep
		if (prop.getProperty("userTypeGrwP").equals(userType)) {
			this.userType = prop.getProperty("userTypeP");
		} else if (prop.getProperty("userTypeGrw").equals(userType)) {
			this.userType = prop.getProperty("userTypeT");
		}
	}

	@Override
	public String toString() {
		return "UserTO [userId=" + userId + ", userName=" + userName + "]";
	}

}
