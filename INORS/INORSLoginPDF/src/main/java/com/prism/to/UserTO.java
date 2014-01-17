/**
 * 
 */
package com.prism.to;

import java.util.List;

/**
 * @author Amitabha Roy
 * 
 */
public class UserTO {

	private String userId;
	private String jasperUserId;
	private String userName;
	private String fullName;

	private String password;
	private String encPassword;
	private String salt;

	private List<String> roles;

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
	 * @return the jasperUserId
	 */
	public String getJasperUserId() {
		return jasperUserId;
	}

	/**
	 * @param jasperUserId
	 *            the jasperUserId to set
	 */
	public void setJasperUserId(String jasperUserId) {
		this.jasperUserId = jasperUserId;
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
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
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
	 * @return the orgNodeName
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}

	/**
	 * @param orgNodeName
	 *            the orgNodeName to set
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}

	/**
	 * @return the orgNodeCodePath
	 */
	public String getOrgNodeCodePath() {
		return orgNodeCodePath;
	}

	/**
	 * @param orgNodeCodePath
	 *            the orgNodeCodePath to set
	 */
	public void setOrgNodeCodePath(String orgNodeCodePath) {
		this.orgNodeCodePath = orgNodeCodePath;
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

}
