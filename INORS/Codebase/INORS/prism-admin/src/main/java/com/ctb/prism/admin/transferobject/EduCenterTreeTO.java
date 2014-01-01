package com.ctb.prism.admin.transferobject;

public class EduCenterTreeTO {
	private String state;
	private String data;
	private long eduTreeId;
	private EduCenterTO metadata;
	private EduCenterTO attr;
	private String eduCenterName;
	private String id;

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
	public long getEduTreeId() {
		return eduTreeId;
	}

	/**
	 * @param eduTreeId
	 */
	public void setEduTreeId(long eduTreeId) {
		this.eduTreeId = eduTreeId;
	}

	/**
	 * @return
	 */
	public EduCenterTO getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 */
	public void setMetadata(EduCenterTO metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return
	 */
	public EduCenterTO getAttr() {
		return attr;
	}

	/**
	 * @param attr
	 */
	public void setAttr(EduCenterTO attr) {
		this.attr = attr;
	}

	/**
	 * @return
	 */
	public String getEduCenterName() {
		return eduCenterName;
	}

	/**
	 * @param eduCenterName
	 */
	public void setEduCenterName(String eduCenterName) {
		this.eduCenterName = eduCenterName;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

}
