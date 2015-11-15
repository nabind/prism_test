package com.ctb.prism.webservice.erTransferobject;

import java.io.Serializable;
import java.util.List;

public class StudentDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477773094826787095L;

	private String UUID;
	private OrgDetails orgDetails;
	private BioDetails bioDetails;
	private DemoDetails demoDetails;
	private ScheduleList scheduleList;
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public OrgDetails getOrgDetails() {
		return orgDetails;
	}
	public void setOrgDetails(OrgDetails orgDetails) {
		this.orgDetails = orgDetails;
	}
	public BioDetails getBioDetails() {
		return bioDetails;
	}
	public void setBioDetails(BioDetails bioDetails) {
		this.bioDetails = bioDetails;
	}
	public DemoDetails getDemoDetails() {
		return demoDetails;
	}
	public void setDemoDetails(DemoDetails demoDetails) {
		this.demoDetails = demoDetails;
	}
	public ScheduleList getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(ScheduleList scheduleList) {
		this.scheduleList = scheduleList;
	}
	
}
