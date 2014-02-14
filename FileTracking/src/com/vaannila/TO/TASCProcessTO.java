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
	public String getObjValidation() {
		return objValidation;
	}
	public void setObjValidation(String objValidation) {
		this.objValidation = objValidation;
	}
	public String getItemValidation() {
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
