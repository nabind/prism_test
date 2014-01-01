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
	private long userId;
	private String userName;
	private String userDisplayName;
	private long tenantId;
	private long parentId;
	private long loggedInOrgId;
	private String tenantName;
	private String firstName;
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
	private String state;
	private String data;
	private long eduTreeId;
	private UserTO metadata;
	private UserTO attr;

	/**
	 * @return
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return
	 */
	public String getUserDisplayName() {
		return userDisplayName;
	}

	/**
	 * @param userDisplayName
	 */
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	/**
	 * @return
	 */
	public long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 */
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @return
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * @param parentId
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return
	 */
	public boolean isFirstTimeUser() {
		return isFirstTimeUser;
	}

	/**
	 * @param isFirstTimeUser
	 */
	public void setFirstTimeUser(boolean isFirstTimeUser) {
		this.isFirstTimeUser = isFirstTimeUser;
	}

	/**
	 * @return
	 */
	public List<RoleTO> getAvailableRoleList() {
		return availableRoleList;
	}

	/**
	 * @param availableRoleList
	 */
	public void setAvailableRoleList(List<RoleTO> availableRoleList) {
		this.availableRoleList = availableRoleList;
	}

	/**
	 * @return
	 */
	public List<RoleTO> getMasterRoleList() {
		return masterRoleList;
	}

	/**
	 * @param masterRoleList
	 */
	public void setMasterRoleList(List<RoleTO> masterRoleList) {
		this.masterRoleList = masterRoleList;
	}

	/**
	 * @return
	 */
	public long getLoggedInOrgId() {
		return loggedInOrgId;
	}

	/**
	 * @param loggedInOrgId
	 */
	public void setLoggedInOrgId(long loggedInOrgId) {
		this.loggedInOrgId = loggedInOrgId;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getEduCenterCode() {
		return eduCenterCode;
	}

	/**
	 * @param eduCenterCode
	 */
	public void setEduCenterCode(String eduCenterCode) {
		this.eduCenterCode = eduCenterCode;
	}

	/**
	 * @return
	 */
	public String getEduCenterName() {
		return eduCenterName;
	}

	/**
	 * @param eduCenterName
	 */
	public void setEduCenterName(String eduCenterName) {
		this.eduCenterName = eduCenterName;
	}

	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public long getEduTreeId() {
		return eduTreeId;
	}

	/**
	 * @param eduTreeId
	 */
	public void setEduTreeId(long eduTreeId) {
		this.eduTreeId = eduTreeId;
	}

	/**
	 * @return
	 */
	public UserTO getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 */
	public void setMetadata(UserTO metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return
	 */
	public UserTO getAttr() {
		return attr;
	}

	/**
	 * @param attr
	 */
	public void setAttr(UserTO attr) {
		this.attr = attr;
	}

}
