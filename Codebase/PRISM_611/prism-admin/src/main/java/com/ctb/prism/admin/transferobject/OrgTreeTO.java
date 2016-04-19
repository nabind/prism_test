package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * 
 */
public class OrgTreeTO extends BaseTO {
	private String state;
	private String data;
	// private String orgMode;
	//private String orgNodeLevel;
	private String orgTreeId;
	private OrgTO metadata;
	private OrgTO attr;

	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/*public String getOrgMode() {
		return orgMode;
	}

	public void setOrgMode(String orgMode) {
		this.orgMode = orgMode;
	}

	public String getOrgNodeLevel() {
		return orgNodeLevel;
	}

	public void setOrgNodeLevel(String orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}*/

	/**
	 * @return
	 */
	public OrgTO getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 */
	public void setMetadata(OrgTO metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return
	 */
	public OrgTO getAttr() {
		return attr;
	}

	/**
	 * @param attr
	 */
	public void setAttr(OrgTO attr) {
		this.attr = attr;
	}

	/**
	 * @return
	 */
	public String getOrgTreeId() {
		return orgTreeId;
	}

	/**
	 * @param orgTreeId
	 */
	public void setOrgTreeId(String orgTreeId) {
		this.orgTreeId = orgTreeId;
	}
	public void setOrgTreeId(long orgTreeId) {
		this.orgTreeId = String.valueOf(orgTreeId);
	}

}
