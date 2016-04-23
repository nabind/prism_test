package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MReportAccess implements Serializable {
	private String roleid;
	private String org_level;
	private String custProdId;
	private String reportSequence;
	
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getOrg_level() {
		return org_level;
	}
	public void setOrg_level(String org_level) {
		this.org_level = org_level;
	}
	public String getCustProdId() {
		return custProdId;
	}
	public void setCustProdId(String custProdId) {
		this.custProdId = custProdId;
	}
	public String getReportSequence() {
		return reportSequence;
	}
	public void setReportSequence(String reportSequence) {
		this.reportSequence = reportSequence;
	}
	
	
	
	
}
