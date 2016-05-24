/**
 * 
 */
package com.vaannila.TO;

import java.io.Serializable;

/**
 * @author d-abir_dutta
 *
 */
public class AdminTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long adminId;
	private String AdminName;
	private String AdminSeasonYear;
	private long adminSeq;
	

	/**
	 * @return the adminId
	 */
	public long getAdminId() {
		return adminId;
	}
	/**
	 * @param adminId the adminId to set
	 */
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	/**
	 * @return the adminName
	 */
	public String getAdminName() {
		return AdminName;
	}
	/**
	 * @param adminName the adminName to set
	 */
	public void setAdminName(String adminName) {
		AdminName = adminName;
	}
	/**
	 * @return the adminSeasonYear
	 */
	public String getAdminSeasonYear() {
		return AdminSeasonYear;
	}
	/**
	 * @param adminSeasonYear the adminSeasonYear to set
	 */
	public void setAdminSeasonYear(String adminSeasonYear) {
		AdminSeasonYear = adminSeasonYear;
	}
	/**
	 * @return the adminSeq
	 */
	public long getAdminSeq() {
		return adminSeq;
	}
	/**
	 * @param adminSeq the adminSeq to set
	 */
	public void setAdminSeq(long adminSeq) {
		this.adminSeq = adminSeq;
	}

}
