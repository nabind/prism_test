package com.ctb.prism.admin.transferobject;

public class EduCenterTreeTO {
	private String state;
	private String data;
	private long eduTreeId;
	private EduCenterTO metadata;
	private EduCenterTO attr;
	private String eduCenterName;
	private String id;
	
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
	public long getEduTreeId() {
		return eduTreeId;
	}
	public void setEduTreeId(long eduTreeId) {
		this.eduTreeId = eduTreeId;
	}
	public EduCenterTO getMetadata() {
		return metadata;
	}
	public void setMetadata(EduCenterTO metadata) {
		this.metadata = metadata;
	}
	public EduCenterTO getAttr() {
		return attr;
	}
	public void setAttr(EduCenterTO attr) {
		this.attr = attr;
	}
	public String getEduCenterName() {
		return eduCenterName;
	}
	public void setEduCenterName(String eduCenterName) {
		this.eduCenterName = eduCenterName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
