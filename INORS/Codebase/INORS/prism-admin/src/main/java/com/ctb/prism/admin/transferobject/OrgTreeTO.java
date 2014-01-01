package com.ctb.prism.admin.transferobject;

/**
 * @author TCS
 * 
 */
public class OrgTreeTO {
	private String state;
	private String data;
	private long orgTreeId;
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
	public long getOrgTreeId() {
		return orgTreeId;
	}

	/**
	 * @param orgTreeId
	 */
	public void setOrgTreeId(long orgTreeId) {
		this.orgTreeId = orgTreeId;
	}

}
