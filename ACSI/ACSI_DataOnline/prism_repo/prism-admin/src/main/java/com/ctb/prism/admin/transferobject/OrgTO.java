package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * version 1.1
 */
public class OrgTO extends BaseTO {

	private static final long serialVersionUID = 53818111201133053L;
	private long tenantId; 
	private String tenantName;
	private String parentNameAlias;
	private String selectedOrgId;
	private String adminName;
	private long parentTenantId;
	private long noOfChildOrgs;
	private long noOfUsers;
	private long orgLevel;
	private long id; 

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getParentNameAlias() {
		return parentNameAlias;
	}
	public void setParentNameAlias(String parentNameAlias) {
		this.parentNameAlias = parentNameAlias;
	}
	public long getParentTenantId() {
		return parentTenantId;
	}
	public void setParentTenantId(long parentTenantId) {
		this.parentTenantId = parentTenantId;
	}
	public long getNoOfChildOrgs() {
		return noOfChildOrgs;
	}
	public void setNoOfChildOrgs(long noOfChildOrgs) {
		this.noOfChildOrgs = noOfChildOrgs;
	}
	public long getNoOfUsers() {
		return noOfUsers;
	}
	public void setNoOfUsers(long noOfUsers) {
		this.noOfUsers = noOfUsers;
	}
	public long getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(long orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getSelectedOrgId() {
		return selectedOrgId;
	}
	public void setSelectedOrgId(String selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
}
