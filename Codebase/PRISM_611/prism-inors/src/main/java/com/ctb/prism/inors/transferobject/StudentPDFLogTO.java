package com.ctb.prism.inors.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class StudentPDFLogTO extends BaseTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studentBioId;
	private long subtestId;
	private long orgNodeId;
	private String fileName;
	
	/**
	 * @return the studentBioId
	 */
	public long getStudentBioId() {
		return studentBioId;
	}
	/**
	 * @param studentBioId the studentBioId to set
	 */
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	/**
	 * @return the subtestId
	 */
	public long getSubtestId() {
		return subtestId;
	}
	/**
	 * @param subtestId the subtestId to set
	 */
	public void setSubtestId(long subtestId) {
		this.subtestId = subtestId;
	}
	/**
	 * @return the orgNodeId
	 */
	public long getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
