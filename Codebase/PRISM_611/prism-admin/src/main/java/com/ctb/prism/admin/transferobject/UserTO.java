package com.ctb.prism.admin.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * @version 1.1
 */
public class UserTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String userName;
	private String userDisplayName;
	private String tenantId;
	private long parentId;
	private String loggedInOrgId;
	private String tenantName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String password;
	private String status;
	private String emailId;
	private String userType;
	private boolean isFirstTimeUser;
	private List<RoleTO> availableRoleList = new ArrayList<RoleTO>();
	private List<RoleTO> masterRoleList = new ArrayList<RoleTO>();

	private long id;
	private String eduCenterCode;
	private String eduCenterName;
	private String phoneNumber;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String data;
	private long eduTreeId;
	private UserTO metadata;
	private UserTO attr;

	private List<PwdHintTO> pwdHintList = new ArrayList<PwdHintTO>();

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
	public void setUserId(long userId) {
		this.userId = String.valueOf(userId);
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
	 * @return the userDisplayName
	 */
	public String getUserDisplayName() {
		return userDisplayName;
	}

	/**
	 * @param userDisplayName
	 *            the userDisplayName to set
	 */
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
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
	public void setTenantId(long tenantId) {
		this.tenantId = String.valueOf(tenantId);
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the loggedInOrgId
	 */
	public String getLoggedInOrgId() {
		return loggedInOrgId;
	}

	/**
	 * @param loggedInOrgId
	 *            the loggedInOrgId to set
	 */
	public void setLoggedInOrgId(String loggedInOrgId) {
		this.loggedInOrgId = loggedInOrgId;
	}
	public void setLoggedInOrgId(long loggedInOrgId) {
		this.loggedInOrgId = String.valueOf(loggedInOrgId);
	}

	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName
	 *            the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the isFirstTimeUser
	 */
	public boolean isFirstTimeUser() {
		return isFirstTimeUser;
	}

	/**
	 * @param isFirstTimeUser
	 *            the isFirstTimeUser to set
	 */
	public void setFirstTimeUser(boolean isFirstTimeUser) {
		this.isFirstTimeUser = isFirstTimeUser;
	}

	/**
	 * @return the availableRoleList
	 */
	public List<RoleTO> getAvailableRoleList() {
		return availableRoleList;
	}

	/**
	 * @param availableRoleList
	 *            the availableRoleList to set
	 */
	public void setAvailableRoleList(List<RoleTO> availableRoleList) {
		this.availableRoleList = availableRoleList;
	}

	/**
	 * @return the masterRoleList
	 */
	public List<RoleTO> getMasterRoleList() {
		return masterRoleList;
	}

	/**
	 * @param masterRoleList
	 *            the masterRoleList to set
	 */
	public void setMasterRoleList(List<RoleTO> masterRoleList) {
		this.masterRoleList = masterRoleList;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the eduCenterCode
	 */
	public String getEduCenterCode() {
		return eduCenterCode;
	}

	/**
	 * @param eduCenterCode
	 *            the eduCenterCode to set
	 */
	public void setEduCenterCode(String eduCenterCode) {
		this.eduCenterCode = eduCenterCode;
	}

	/**
	 * @return the eduCenterName
	 */
	public String getEduCenterName() {
		return eduCenterName;
	}

	/**
	 * @param eduCenterName
	 *            the eduCenterName to set
	 */
	public void setEduCenterName(String eduCenterName) {
		this.eduCenterName = eduCenterName;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the eduTreeId
	 */
	public long getEduTreeId() {
		return eduTreeId;
	}

	/**
	 * @param eduTreeId
	 *            the eduTreeId to set
	 */
	public void setEduTreeId(long eduTreeId) {
		this.eduTreeId = eduTreeId;
	}

	/**
	 * @return the metadata
	 */
	public UserTO getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 *            the metadata to set
	 */
	public void setMetadata(UserTO metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the attr
	 */
	public UserTO getAttr() {
		return attr;
	}

	/**
	 * @param attr
	 *            the attr to set
	 */
	public void setAttr(UserTO attr) {
		this.attr = attr;
	}

	/**
	 * @return the pwdHintList
	 */
	public List<PwdHintTO> getPwdHintList() {
		return pwdHintList;
	}

	/**
	 * @param pwdHintList
	 *            the pwdHintList to set
	 */
	public void setPwdHintList(List<PwdHintTO> pwdHintList) {
		this.pwdHintList = pwdHintList;
	}

}
