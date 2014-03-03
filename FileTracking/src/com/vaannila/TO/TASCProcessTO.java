package com.vaannila.TO;

import java.io.Serializable;

public class TASCProcessTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String processId;
	private String fileName;
	private String sourceSystem;
	private String hierValidation;
	private String bioValidation;
	private String demoValidation;
	private String contentValidation;
	private String objValidation;
	private String itemValidation;
	private String wkfPartitionName;
	private String processLog;
	private String dateTimestamp;
	private String timeMili;
	private String ppCount;
	private String olCount;
	private String coCount;
	private String erCount;
	private String overallStatus;
	
	public String getCoCount() {
		return coCount;
	}
	public void setCoCount(String coCount) {
		this.coCount = coCount;
	}
	public String getErCount() {
		return erCount;
	}
	public void setErCount(String erCount) {
		this.erCount = erCount;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getPpCount() {
		return ppCount;
	}
	public void setPpCount(String ppCount) {
		this.ppCount = ppCount;
	}
	public String getOlCount() {
		return olCount;
	}
	public void setOlCount(String olCount) {
		this.olCount = olCount;
	}
	public String getTimeMili() {
		return timeMili;
	}
	public void setTimeMili(String timeMili) {
		this.timeMili = timeMili;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getHierValidation() {
		if(hierValidation != null && "ER".equals(hierValidation)) return "<span style='color: red'>"+hierValidation+"</span>";
		return hierValidation;
	}
	public void setHierValidation(String hierValidation) {
		this.hierValidation = hierValidation;
	}
	public String getBioValidation() {
		if(bioValidation != null && "ER".equals(bioValidation)) return "<span style='color: red'>"+bioValidation+"</span>";
		return bioValidation;
	}
	public void setBioValidation(String bioValidation) {
		this.bioValidation = bioValidation;
	}
	public String getDemoValidation() {
		if(demoValidation != null && "ER".equals(demoValidation)) return "<span style='color: red'>"+demoValidation+"</span>";
		return demoValidation;
	}
	public void setDemoValidation(String demoValidation) {
		this.demoValidation = demoValidation;
	}
	public String getContentValidation() {
		if(contentValidation != null && "ER".equals(contentValidation)) return "<span style='color: red'>"+contentValidation+"</span>";
		return contentValidation;
	}
	public void setContentValidation(String contentValidation) {
		this.contentValidation = contentValidation;
	}
	public String getObjValidation() {
		if(objValidation != null && "ER".equals(objValidation)) return "<span style='color: red'>"+objValidation+"</span>";
		return objValidation;
	}
	public void setObjValidation(String objValidation) {
		this.objValidation = objValidation;
	}
	public String getItemValidation() {
		if(itemValidation != null && "ER".equals(itemValidation)) return "<span style='color: red'>"+itemValidation+"</span>";
		return itemValidation;
	}
	public void setItemValidation(String itemValidation) {
		this.itemValidation = itemValidation;
	}
	public String getWkfPartitionName() {
		return wkfPartitionName;
	}
	public void setWkfPartitionName(String wkfPartitionName) {
		this.wkfPartitionName = wkfPartitionName;
	}
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public String getDateTimestamp() {
		return dateTimestamp;
	}
	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}
	
	
}
