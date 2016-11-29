package com.ctb.prism.admin.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS version 1.1
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public long getTenantId() {
		return tenantId;
	}

	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public long getParentId() {
		return parentId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public boolean isFirstTimeUser() {
		return isFirstTimeUser;
	}

	public void setFirstTimeUser(boolean isFirstTimeUser) {
		this.isFirstTimeUser = isFirstTimeUser;
	}
	
	public List<RoleTO> getAvailableRoleList() {
		return availableRoleList;
	}

	public void setAvailableRoleList(List<RoleTO> availableRoleList) {
		this.availableRoleList = availableRoleList;
	}

	public List<RoleTO> getMasterRoleList() {
		return masterRoleList;
	}

	public void setMasterRoleList(List<RoleTO> masterRoleList) {
		this.masterRoleList = masterRoleList;
	}
	public long getLoggedInOrgId() {
		return loggedInOrgId;
	}

	public void setLoggedInOrgId(long loggedInOrgId) {
		this.loggedInOrgId = loggedInOrgId;
	}
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
