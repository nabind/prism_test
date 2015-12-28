package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MOrgTO extends BaseTO {
	private String AdminId;
	private String OrgNodePath;
	private String orgNodeid;
	private String IsActive;
	private long defultCustProdId;
	
	public String getAdminId() {
		return AdminId;
	}
	public void setAdminId(String adminId) {
		AdminId = adminId;
	}
	public String getOrgNodePath() {
		return OrgNodePath;
	}
	public void setOrgNodePath(String orgNodePath) {
		OrgNodePath = orgNodePath;
	}
	public String getOrgNodeid() {
		return orgNodeid;
	}
	public void setOrgNodeid(String orgNodeid) {
		this.orgNodeid = orgNodeid;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	public long getDefultCustProdId() {
		return defultCustProdId;
	}
	public void setDefultCustProdId(long defultCustProdId) {
		this.defultCustProdId = defultCustProdId;
	}
}
