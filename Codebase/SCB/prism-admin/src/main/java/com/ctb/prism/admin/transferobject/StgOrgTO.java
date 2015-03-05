package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class StgOrgTO extends BaseTO {
	private static final long serialVersionUID = 53818111201133053L;

	private long orgNodeId;
	private String orgNodeName;
	private String orgNodeCode;
	private long orgNodeLevel;
	private String strucElement;
	private String specialCodes;
	private String orgMode;
	private long parentOrgNodeId;
	private String orgNodeCodePath;
	private String email;
	private long adminId;
	private long customerId;

	/**
	 * @return
	 */
	public long getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId
	 */
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	/**
	 * @return
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}

	/**
	 * @param orgNodeName
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}

	/**
	 * @return
	 */
	public String getOrgNodeCode() {
		return orgNodeCode;
	}

	/**
	 * @param orgNodeCode
	 */
	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}

	/**
	 * @return
	 */
	public long getOrgNodeLevel() {
		return orgNodeLevel;
	}

	/**
	 * @param orgNodeLevel
	 */
	public void setOrgNodeLevel(long orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}

	/**
	 * @return
	 */
	public String getStrucElement() {
		return strucElement;
	}

	/**
	 * @param strucElement
	 */
	public void setStrucElement(String strucElement) {
		this.strucElement = strucElement;
	}

	/**
	 * @return
	 */
	public String getSpecialCodes() {
		return specialCodes;
	}

	/**
	 * @param specialCodes
	 */
	public void setSpecialCodes(String specialCodes) {
		this.specialCodes = specialCodes;
	}

	/**
	 * @return
	 */
	public String getOrgMode() {
		return orgMode;
	}

	/**
	 * @param orgMode
	 */
	public void setOrgMode(String orgMode) {
		this.orgMode = orgMode;
	}

	/**
	 * @return
	 */
	public long getParentOrgNodeId() {
		return parentOrgNodeId;
	}

	/**
	 * @param parentOrgNodeId
	 */
	public void setParentOrgNodeId(long parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}

	/**
	 * @return
	 */
	public String getOrgNodeCodePath() {
		return orgNodeCodePath;
	}

	/**
	 * @param orgNodeCodePath
	 */
	public void setOrgNodeCodePath(String orgNodeCodePath) {
		this.orgNodeCodePath = orgNodeCodePath;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
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

}
