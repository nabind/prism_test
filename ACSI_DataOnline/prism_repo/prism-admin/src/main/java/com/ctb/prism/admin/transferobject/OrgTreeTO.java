package com.ctb.prism.admin.transferobject;

import java.util.List;

public class OrgTreeTO {
	private String state;
	private String data;
	private long orgTreeId;
	private OrgTO metadata;
	private OrgTO attr;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public OrgTO getMetadata() {
		return metadata;
	}
	public void setMetadata(OrgTO metadata) {
		this.metadata = metadata;
	}
	public OrgTO getAttr() {
		return attr;
	}
	public void setAttr(OrgTO attr) {
		this.attr = attr;
	}
	
	public long getOrgTreeId() {
		return orgTreeId;
	}
	public void setOrgTreeId(long orgTreeId) {
		this.orgTreeId = orgTreeId;
	}
	
}
