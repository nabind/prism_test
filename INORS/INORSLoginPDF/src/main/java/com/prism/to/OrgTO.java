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
	private String jasperOrgId;
	private String email;
	private String customerCode;
	private String parentJasperOrgId;
	private String dateStr;
	private String dateStrWtYear;
	private List<UserTO> users;

	private String userName;
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

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName == null ? "" : elementName;
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
		return stateName == null ? "" : stateName;
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
		return countyCode == null ? 0 : countyCode;
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
		return testingSiteCode == null ? 0 : testingSiteCode;
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
		return testingSiteName == null ? "" : testingSiteName;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
		result = prime * result + Integer.parseInt(parentJasperOrgId);
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
		if (elementName == null) {
			if (other.elementName != null)
				return false;
		} else if (!elementName.equals(other.elementName))
			return false;
		if (Integer.parseInt(parentJasperOrgId) != Integer.parseInt(other.parentJasperOrgId))
			return false;
		return true;
	}

	@Override
	public int compareTo(OrgTO arg0) {
		final int BEFORE = -1;
		final int AFTER = 1;

		if (Integer.parseInt(this.parentJasperOrgId) > Integer.parseInt(arg0.parentJasperOrgId)) {
			return AFTER;
		} else {
			return BEFORE;
		}
	}

}
