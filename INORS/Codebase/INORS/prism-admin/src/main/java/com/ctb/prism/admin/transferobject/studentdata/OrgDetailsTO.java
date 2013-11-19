package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Org_Details")
public class OrgDetailsTO {

	@XStreamAsAttribute
	@XStreamAlias("Org_Name")
	private String orgName;

	@XStreamAsAttribute
	@XStreamAlias("Org_Label")
	private String orgLabel;

	@XStreamAsAttribute
	@XStreamAlias("Org_Level")
	private String orgLevel;

	@XStreamAsAttribute
	@XStreamAlias("Org_Node_Id")
	private String orgNodeId;

	@XStreamAsAttribute
	@XStreamAlias("Org_Code")
	private String orgCode;

	@XStreamAsAttribute
	@XStreamAlias("Pr_Org_Code")
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
