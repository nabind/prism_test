package com.ctb.prism.login.transferobject;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.BaseTO;

public class UserTO extends BaseTO{

	private String userName;
	private String firstName;
	private String lastName;
	private Timestamp lastLoginTime;
	private String password;
	private String userEmail;
	private String isAdminFlag;
	private String userStatus;
	private boolean active;
	private String orgCode;
	private String orgId;
	private long orgNodeLevel;
	private String orgNodeLevelStr;
	private String firstTimeLogin;
	private String userId;
	private String displayName;
	private List<GrantedAuthority> roles;
	private String salt;
	private String customerId;
	private String product;
	private String userType;
	
	public String getOrgNodeLevelStr() {
		return orgNodeLevelStr;
	}
	public void setOrgNodeLevelStr(String orgNodeLevelStr) {
		this.orgNodeLevelStr = orgNodeLevelStr;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public boolean isActive() {
		return (IApplicationConstants.ACTIVE_FLAG.equals(getUserStatus()) || IApplicationConstants.SS_FLAG.equals(getUserStatus()))? true : false;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<GrantedAuthority> getRoles() {
		return roles;
	}
	public void setRoles(List<GrantedAuthority> roles) {
		this.roles = roles;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}	
	public long getOrgNodeLevel() {
		return orgNodeLevel;
	}
	public void setOrgNodeLevel(long orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}
	public String getFirstTimeLogin() {
		return firstTimeLogin;
	}
	public void setFirstTimeLogin(String firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getIsAdminFlag() {
		return isAdminFlag;
	}
	public void setIsAdminFlag(String isAdminFlag) {
		this.isAdminFlag = isAdminFlag;
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}
