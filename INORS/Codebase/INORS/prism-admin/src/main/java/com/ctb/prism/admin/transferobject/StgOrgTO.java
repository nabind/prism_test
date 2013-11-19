package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class StgOrgTO  extends BaseTO {
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
	
	public long getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public String getOrgNodeName() {
		return orgNodeName;
	}
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	public String getOrgNodeCode() {
		return orgNodeCode;
	}
	public void setOrgNodeCode(String orgNodeCode) {
		this.orgNodeCode = orgNodeCode;
	}
	public long getOrgNodeLevel() {
		return orgNodeLevel;
	}
	public void setOrgNodeLevel(long orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}
	public String getStrucElement() {
		return strucElement;
	}
	public void setStrucElement(String strucElement) {
		this.strucElement = strucElement;
	}
	public String getSpecialCodes() {
		return specialCodes;
	}
	public void setSpecialCodes(String specialCodes) {
		this.specialCodes = specialCodes;
	}
	public String getOrgMode() {
		return orgMode;
	}
	public void setOrgMode(String orgMode) {
		this.orgMode = orgMode;
	}
	public long getParentOrgNodeId() {
		return parentOrgNodeId;
	}
	public void setParentOrgNodeId(long parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}
	public String getOrgNodeCodePath() {
		return orgNodeCodePath;
	}
	public void setOrgNodeCodePath(String orgNodeCodePath) {
		this.orgNodeCodePath = orgNodeCodePath;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
}
