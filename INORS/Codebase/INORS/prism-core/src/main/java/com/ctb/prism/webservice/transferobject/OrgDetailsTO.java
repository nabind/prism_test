package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class OrgDetailsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String orgName;
	private String orgLabel;
	private String orgLevel;
	private String orgNodeId;
	private String orgCode;
	private String parentOrgCode;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgLabel() {
		return orgLabel;
	}
	public void setOrgLabel(String orgLabel) {
		this.orgLabel = orgLabel;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(String orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getParentOrgCode() {
		return parentOrgCode;
	}
	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}
	
	
	
}
