package com.ctb.prism.core.transferobject;

import com.ctb.prism.core.util.CustomStringUtil;

public class ProcessTO extends BaseTO {

	/**
	 * TO to transfered usability report data of the dashboard report
	 */
	private static final long serialVersionUID = 1L;
	
	private String processId;
	private String hierValidation;
	private String bioValidation;
	private String demoValidation;
	private String contentValidation;
	private String objectiveValidation;
	private String itemValidation;
	private String processLog;
	
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getHierValidation() {
		return hierValidation;
	}
	public void setHierValidation(String hierValidation) {
		this.hierValidation = hierValidation;
	}
	public String getBioValidation() {
		return bioValidation;
	}
	public void setBioValidation(String bioValidation) {
		this.bioValidation = bioValidation;
	}
	public String getDemoValidation() {
		return demoValidation;
	}
	public void setDemoValidation(String demoValidation) {
		this.demoValidation = demoValidation;
	}
	public String getContentValidation() {
		return contentValidation;
	}
	public void setContentValidation(String contentValidation) {
		this.contentValidation = contentValidation;
	}
	public String getObjectiveValidation() {
		return objectiveValidation;
	}
	public void setObjectiveValidation(String objectiveValidation) {
		this.objectiveValidation = objectiveValidation;
	}
	public String getItemValidation() {
		return itemValidation;
	}
	public void setItemValidation(String itemValidation) {
		this.itemValidation = itemValidation;
	}
	
	public String toString() {
		return CustomStringUtil.appendString("processId:", processId,
		" hierValidation:", hierValidation,
		" bioValidation:", bioValidation,
		" demoValidation:", demoValidation,
		" contentValidation:", contentValidation,
		" objectiveValidation:", objectiveValidation,
		" itemValidation:", itemValidation,
		" processLog:", processLog);
	}
	
}
