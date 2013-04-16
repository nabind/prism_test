package com.vaannila.TO;

public class OrgTO {
	private String elementName;
	private String regionName;
	private String jasperOrgId;
	private String tenantId;
	private String orgNode;
	private String email;
	private String structureElement;
	private String customerCode;
	
	private String studentCount;
	private String grade;
	private int gradeSeq;
	
	private String userName;
	private String fullName;
	private String password;
	private String encPassword;
	private String userId;
	
	private long userCount;
	private long classCount;
	private long studCount;
	
	private String processNotExist;
	
	
	public String getProcessNotExist() {
		return processNotExist;
	}
	public void setProcessNotExist(String processNotExist) {
		this.processNotExist = processNotExist;
	}
	public long getStudCount() {
		return studCount;
	}
	public void setStudCount(long studCount) {
		this.studCount = studCount;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public long getUserCount() {
		return userCount;
	}
	public void setUserCount(long userCount) {
		this.userCount = userCount;
	}
	public long getClassCount() {
		return classCount;
	}
	public void setClassCount(long classCount) {
		this.classCount = classCount;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public int getGradeSeq() {
		return gradeSeq;
	}
	public void setGradeSeq(int gradeSeq) {
		this.gradeSeq = gradeSeq;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEncPassword() {
		return encPassword;
	}
	public void setEncPassword(String encPassword) {
		this.encPassword = encPassword;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getStructureElement() {
		return structureElement;
	}
	public void setStructureElement(String structureElement) {
		this.structureElement = structureElement;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGrade() {
		return (grade == null)? "" : grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getJasperOrgId() {
		return jasperOrgId;
	}
	public void setJasperOrgId(String jasperOrgId) {
		this.jasperOrgId = jasperOrgId;
	}
	public String getOrgNode() {
		return orgNode;
	}
	public void setOrgNode(String orgNode) {
		this.orgNode = orgNode;
	}
	
	
}
