/**
 * 
 */
package com.prism.to;

import java.util.List;

/**
 * @author Amitabha Roy
 * 
 */
public class OrgTO implements Comparable<OrgTO> {

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
	private List<UserTO> users;
	
	private String schoolCode;
	private String districtCode;

	private String userId;
	private String userName;
	private String fullName;
	private String password;
	private String salt;
	private String encPassword;

	private List<String> roleList;

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

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		if (schoolCode != null) {
			String[] codes = schoolCode.split("~");
			if (codes.length == 4) {
				this.schoolCode = codes[3];
				this.districtCode = codes[2];
			}
		} else {
			this.schoolCode = schoolCode;
			this.districtCode = "NA";
		}
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @param elementName
	 *            the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * @return the orgNodeId
	 */
	public String getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId
	 *            the orgNodeId to set
	 */
	public void setOrgNodeId(String orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	/**
	 * @return the orgNodeLevel
	 */
	public String getOrgNodeLevel() {
		return orgNodeLevel;
	}

	/**
	 * @param orgNodeLevel
	 *            the orgNodeLevel to set
	 */
	public void setOrgNodeLevel(String orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the jasperOrgId
	 */
	public String getJasperOrgId() {
		return jasperOrgId;
	}

	/**
	 * @param jasperOrgId
	 *            the jasperOrgId to set
	 */
	public void setJasperOrgId(String jasperOrgId) {
		this.jasperOrgId = jasperOrgId;
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

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode
	 *            the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the parentJasperOrgId
	 */
	public String getParentJasperOrgId() {
		return parentJasperOrgId;
	}

	/**
	 * @param parentJasperOrgId
	 *            the parentJasperOrgId to set
	 */
	public void setParentJasperOrgId(String parentJasperOrgId) {
		this.parentJasperOrgId = parentJasperOrgId;
	}

	/**
	 * @return the dateStr
	 */
	public String getDateStr() {
		return dateStr;
	}

	/**
	 * @param dateStr
	 *            the dateStr to set
	 */
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	/**
	 * @return the dateStrWtYear
	 */
	public String getDateStrWtYear() {
		return dateStrWtYear;
	}

	/**
	 * @param dateStrWtYear
	 *            the dateStrWtYear to set
	 */
	public void setDateStrWtYear(String dateStrWtYear) {
		this.dateStrWtYear = dateStrWtYear;
	}

	/**
	 * @return the users
	 */
	public List<UserTO> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<UserTO> users) {
		this.users = users;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the encPassword
	 */
	public String getEncPassword() {
		return encPassword;
	}

	/**
	 * @param encPassword
	 *            the encPassword to set
	 */
	public void setEncPassword(String encPassword) {
		this.encPassword = encPassword;
	}

	/**
	 * @return the roleList
	 */
	public List<String> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList
	 *            the roleList to set
	 */
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the stateCode
	 */
	public Integer getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName
	 *            the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the countyCode
	 */
	public Integer getCountyCode() {
		return countyCode;
	}

	/**
	 * @param countyCode
	 *            the countyCode to set
	 */
	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 * @return the countyName
	 */
	public String getCountyName() {
		return countyName;
	}

	/**
	 * @param countyName
	 *            the countyName to set
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	/**
	 * @return the testingSiteCode
	 */
	public Integer getTestingSiteCode() {
		return testingSiteCode;
	}

	/**
	 * @param testingSiteCode
	 *            the testingSiteCode to set
	 */
	public void setTestingSiteCode(Integer testingSiteCode) {
		this.testingSiteCode = testingSiteCode;
	}

	/**
	 * @return the testingSiteName
	 */
	public String getTestingSiteName() {
		return testingSiteName;
	}

	/**
	 * @param testingSiteName
	 *            the testingSiteName to set
	 */
	public void setTestingSiteName(String testingSiteName) {
		this.testingSiteName = testingSiteName;
	}

	/**
	 * @return the structureElement
	 */
	public String getStructureElement() {
		return structureElement;
	}

	/**
	 * @param structureElement
	 *            the structureElement to set
	 */
	public void setStructureElement(String structureElement) {
		this.structureElement = structureElement;
	}

	/**
	 * @return the studentCount
	 */
	public String getStudentCount() {
		return studentCount;
	}

	/**
	 * @param studentCount
	 *            the studentCount to set
	 */
	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
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
	 * @return the gradeSeq
	 */
	public int getGradeSeq() {
		return gradeSeq;
	}

	/**
	 * @param gradeSeq
	 *            the gradeSeq to set
	 */
	public void setGradeSeq(int gradeSeq) {
		this.gradeSeq = gradeSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + gradeSeq;
		return result;
	}
	@Override
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
	
	@Override
	public int compareTo(OrgTO arg0) {
		final int BEFORE = -1;
	    final int AFTER = 1;
	    
	    if(this.gradeSeq > arg0.getGradeSeq()) {
	    	return AFTER;
	    } else {
	    	return BEFORE;
	    }
	}

}
