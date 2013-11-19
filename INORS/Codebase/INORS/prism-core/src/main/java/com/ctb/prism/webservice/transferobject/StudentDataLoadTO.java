package com.ctb.prism.webservice.transferobject;

import java.util.List;

public class StudentDataLoadTO {

	private String dataLoadId;
	private String status;
	private int statusCode;
	private String summary;
	private List<String> errorMessages;
	private long processId;
	private long studentBioDetailsId;
	private long subtestDetailsId;
	private long objectiveDetailsId;
	private String partitionName;
	private boolean success;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public long getSubtestDetailsId() {
		return subtestDetailsId;
	}
	public void setSubtestDetailsId(long subtestDetailsId) {
		this.subtestDetailsId = subtestDetailsId;
	}
	public long getObjectiveDetailsId() {
		return objectiveDetailsId;
	}
	public void setObjectiveDetailsId(long objectiveDetailsId) {
		this.objectiveDetailsId = objectiveDetailsId;
	}
	public long getStudentBioDetailsId() {
		return studentBioDetailsId;
	}
	public void setStudentBioDetailsId(long studentBioDetailsId) {
		this.studentBioDetailsId = studentBioDetailsId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getPartitionName() {
		return partitionName;
	}
	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}
	public long getProcessId() {
		return processId;
	}
	public void setProcessId(long processId) {
		this.processId = processId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDataLoadId() {
		return dataLoadId;
	}
	public void setDataLoadId(String dataLoadId) {
		this.dataLoadId = dataLoadId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
}
