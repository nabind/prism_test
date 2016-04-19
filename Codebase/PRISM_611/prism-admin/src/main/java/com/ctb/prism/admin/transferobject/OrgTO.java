package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS 
 * @version 1.1
 */
public class OrgTO extends BaseTO {

	private static final long serialVersionUID = 53818111201133053L;
	private long tenantId;
	private String tenantName;
	private String parentNameAlias;
	private String selectedOrgId;
	private String adminName;
	private String parentTenantId;
	private long noOfChildOrgs;
	private long noOfUsers;
	private String orgLevel;
	private String id;
	private long customerId;
	private long adminId;
	private String className;

	/**
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	public void setId(Long id) {
		this.id = String.valueOf(id);
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
	 * @return
	 */
	public String getParentNameAlias() {
		return parentNameAlias;
	}

	/**
	 * @param parentNameAlias
	 */
	public void setParentNameAlias(String parentNameAlias) {
		this.parentNameAlias = parentNameAlias;
	}

	/**
	 * @return
	 */
	public String getParentTenantId() {
		return parentTenantId;
	}

	/**
	 * @param parentTenantId
	 */
	public void setParentTenantId(String parentTenantId) {
		this.parentTenantId = parentTenantId;
	}
	public void setParentTenantId(long parentTenantId) {
		this.parentTenantId = String.valueOf(parentTenantId);
	}

	/**
	 * @return
	 */
	public long getNoOfChildOrgs() {
		return noOfChildOrgs;
	}

	/**
	 * @param noOfChildOrgs
	 */
	public void setNoOfChildOrgs(long noOfChildOrgs) {
		this.noOfChildOrgs = noOfChildOrgs;
	}

	/**
	 * @return
	 */
	public long getNoOfUsers() {
		return noOfUsers;
	}

	/**
	 * @param noOfUsers
	 */
	public void setNoOfUsers(long noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	/**
	 * @return
	 */
	public String getOrgLevel() {
		return orgLevel;
	}

	/**
	 * @param orgLevel
	 */
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public void setOrgLevel(long orgLevel) {
		this.orgLevel = String.valueOf(orgLevel);
	}

	/**
	 * @return
	 */
	public String getSelectedOrgId() {
		return selectedOrgId;
	}

	/**
	 * @param selectedOrgId
	 */
	public void setSelectedOrgId(String selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}

	/**
	 * @return
	 */
	public String getAdminName() {
		return adminName;
	}

	/**
	 * @param adminName
	 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/**
	 * @return
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return
	 */
	public long getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId
	 */
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

}
