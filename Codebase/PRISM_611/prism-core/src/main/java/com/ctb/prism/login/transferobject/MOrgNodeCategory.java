package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MOrgNodeCategory implements Serializable {
	
	private String orgCategory;
	private String orgNodeLevel;
	
	public String getOrgCategory() {
		return orgCategory;
	}
	public void setOrgCategory(String orgCategory) {
		this.orgCategory = orgCategory;
	}
	public String getOrgNodeLevel() {
		return orgNodeLevel;
	}
	public void setOrgNodeLevel(String orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}
	
}
