package com.ctb.prism.webservice.erTransferobject;

import java.io.Serializable;
import java.util.List;

public class ScheduleList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477773094826787095L;

	private List<ScheduleDetails> scheduleDetails;

	public List<ScheduleDetails> getScheduleDetails() {
		return scheduleDetails;
	}

	public void setScheduleDetails(List<ScheduleDetails> scheduleDetails) {
		this.scheduleDetails = scheduleDetails;
	}
	
	
}
