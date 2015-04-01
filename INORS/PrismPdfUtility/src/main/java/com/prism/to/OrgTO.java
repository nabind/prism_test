package com.prism.to;

import java.util.List;

public class OrgTO implements Comparable<OrgTO> {

	private List<UserTO> users;
	private List<String> roleList;

	private String elementName;
	private String orgNodeId;
	private String orgNodeLevel;
	private String tenantId;
	private String jasperOrgId;
	private String email;
	private String customerCode;
	private String parentJasperOrgId;
	private String dateStr;
	private String dateStrWtYear;
	private String schoolCode;
	private String districtCode;
	private String districtName;
	private String userId;
	private String userName;
	private String fullName;
	private String password;
	private String salt;
	private String encPassword;
	private Integer stateCode;
	private String stateName;
	private Integer countyCode;
	private String countyName;
	private Integer testingSiteCode;
	private String testingSiteName;
	private String structureElement;
	private String studentCount;
	private String grade;
	private int gradeSeq;
	private String orgMode;
	private String testAdministration;
	private String orgNodeCodePath;

	

	// Istep
	private String orgNode;
	private String parentElementName;
	private String parentStructureElement;
	private String parentCustomerCode;
	private String orgNodeCode;
	private String parentOrgNodeCode;
	private String userType = "";

	// Tasc
	private boolean sendLoginPdf;

	public List<UserTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserTO> users) {
		this.users = users;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getElementName() {
		// Tasc
		return elementName == null ? "" : elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getJasperOrgId() {
		return jasperOrgId;
	}

	public void setJasperOrgId(String jasperOrgId) {
		this.jasperOrgId = jasperOrgId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getParentJasperOrgId() {
		return parentJasperOrgId;
	}

	public void setParentJasperOrgId(String parentJasperOrgId) {
		this.parentJasperOrgId = parentJasperOrgId;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getDateStrWtYear() {
		return dateStrWtYear;
	}

	public void setDateStrWtYear(String dateStrWtYear) {
		this.dateStrWtYear = dateStrWtYear;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEncPassword() {
		return encPassword;
	}

	public void setEncPassword(String encPassword) {
		this.encPassword = encPassword;
	}

	public Integer getStateCode() {
		return stateCode;
	}

	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		// Tasc
		return stateName == null ? "" : stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getCountyCode() {
		// Tasc
		return countyCode == null ? 0 : countyCode;
	}

	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public Integer getTestingSiteCode() {
		// Tasc
		return testingSiteCode == null ? 0 : testingSiteCode;
	}

	public void setTestingSiteCode(Integer testingSiteCode) {
		this.testingSiteCode = testingSiteCode;
	}

	public String getTestingSiteName() {
		// Tasc
		return testingSiteName == null ? "" : testingSiteName;
	}

	public void setTestingSiteName(String testingSiteName) {
		this.testingSiteName = testingSiteName;
	}

	public String getStructureElement() {
		return structureElement;
	}

	public void setStructureElement(String structureElement) {
		this.structureElement = structureElement;
	}

	public String getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}

	public String getGrade() {
		// Inors
		return (grade == null) ? "" : grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeSeq() {
		return gradeSeq;
	}

	public void setGradeSeq(int gradeSeq) {
		this.gradeSeq = gradeSeq;
	}

	public String getOrgMode() {
		return orgMode;
	}

	public void setOrgMode(String orgMode) {
		this.orgMode = orgMode;
	}

	public String getTestAdministration() {
		return testAdministration;
	}

	public void setTestAdministration(String testAdministration) {
		this.testAdministration = testAdministration;
	}

	public String getOrgNode() {
		return orgNode;
	}

	public void setOrgNode(String orgNode) {
		this.orgNode = orgNode;
	}

	public String getParentElementName() {
		return parentElementName;
	}

	public void setParentElementName(String parentElementName) {
		this.parentElementName = parentElementName;
	}

	public String getParentStructureElement() {
		return parentStructureElement;
	}

	public void setParentStructureElement(String parentStructureElement) {
		this.parentStructureElement = parentStructureElement;
	}

	public String getParentCustomerCode() {
		return parentCustomerCode;
	}

	public void setParentCustomerCode(String parentCustomerCode) {
		this.parentCustomerCode = parentCustomerCode;
	}

	public String getOrgNodeCode() {
		// Tasc
		return orgNodeCode == null ? "" : orgNodeCode;
	}

	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}

	public String getParentOrgNodeCode() {
		return parentOrgNodeCode;
	}

	public void setParentOrgNodeCode(String parentOrgNodeCode) {
		this.parentOrgNodeCode = parentOrgNodeCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public boolean isSendLoginPdf() {
		return sendLoginPdf;
	}

	public void setSendLoginPdf(String sendLoginPdf) {
		// Tasc
		this.sendLoginPdf = "Y".equals(sendLoginPdf) ? true : false;
	}
	
	public String getOrgNodeCodePath() {
		return orgNodeCodePath;
	}

	public void setOrgNodeCodePath(String orgNodeCodePath) {
		this.orgNodeCodePath = orgNodeCodePath;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + gradeSeq;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgTO other = (OrgTO) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (gradeSeq != other.gradeSeq)
			return false;
		return true;
	}

	public int compareTo(OrgTO arg0) {
		final int BEFORE = -1;
		final int AFTER = 1;

		if (this.gradeSeq > arg0.getGradeSeq()) {
			return AFTER;
		} else {
			return BEFORE;
		}
	}

}
