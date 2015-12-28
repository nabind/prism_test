package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MOrgNodeCategory extends BaseTO {
	
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
