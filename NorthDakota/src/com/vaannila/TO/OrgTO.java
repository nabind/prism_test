package com.vaannila.TO;

import java.io.Serializable;

public class OrgTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nodeId;
	private String SchoolName;
	private String pdfFileName;
	private String pdfFileNameRoster;
	private String districtNumber;
	private String schoolNumber;
	private String SchoolFullName;
	
	public String getSchoolFullName() {
		return SchoolFullName;
	}
	public void setSchoolFullName(String schoolFullName) {
		SchoolFullName = schoolFullName;
	}
	public String getPdfFileNameRoster() {
		return pdfFileNameRoster;
	}
	public void setPdfFileNameRoster(String pdfFileNameRoster) {
		this.pdfFileNameRoster = pdfFileNameRoster;
	}
	public String getDistrictNumber() {
		return districtNumber;
	}
	public void setDistrictNumber(String districtNumber) {
		this.districtNumber = districtNumber;
	}
	public String getSchoolNumber() {
		return schoolNumber;
	}
	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}
	public String getPdfFileName() {
		return pdfFileName;
	}
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}
	public long getNodeId() {
		return nodeId;
	}
	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
	public String getSchoolName() {
		return SchoolName;
	}
	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}
	
	
}
