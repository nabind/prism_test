/**
 * 
 */
package com.ctb.prism.core.transferobject;

/**
 * @author 233208
 *
 */
public class StudentDataExtractTO extends BaseTO{
	private static final long serialVersionUID = 1L;
	
	private long jobId;
	private String studentDataXML;
	private long customerId;
	
	/**
	 * @return the jobId
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return the studentDataXML
	 */
	public String getStudentDataXML() {
		return studentDataXML;
	}
	/**
	 * @param studentDataXML the studentDataXML to set
	 */
	public void setStudentDataXML(String studentDataXML) {
		this.studentDataXML = studentDataXML;
	}
	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
}
